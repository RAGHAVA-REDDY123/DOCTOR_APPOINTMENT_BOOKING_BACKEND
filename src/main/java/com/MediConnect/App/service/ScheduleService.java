package com.MediConnect.App.service;

import com.MediConnect.App.dto.request.DoctorAvailabilityRequest;
import com.MediConnect.App.dto.request.DoctorBlockRequest;
import com.MediConnect.App.dto.request.BulkAvailabilityRequest;
import com.MediConnect.App.dto.response.AvailableDateResponse;
import com.MediConnect.App.dto.response.DateBasedSlotResponse;
import com.MediConnect.App.entity.Appointment;
import com.MediConnect.App.entity.Doctor;
import com.MediConnect.App.entity.DoctorAvailability;
import com.MediConnect.App.entity.DoctorBlock;
import com.MediConnect.App.enums.AppointmentStatus;
import com.MediConnect.App.exception.ApiException;
import com.MediConnect.App.repository.AppointmentRepository;
import com.MediConnect.App.repository.DoctorAvailabilityRepository;
import com.MediConnect.App.repository.DoctorBlockRepository;
import com.MediConnect.App.repository.DoctorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private static final int DEFAULT_SLOT_DURATION_MINUTES = 30;

    private final DoctorRepository doctorRepository;
    private final DoctorAvailabilityRepository availabilityRepository;
    private final DoctorBlockRepository blockRepository;
    private final AppointmentRepository appointmentRepository;

    @Transactional(readOnly = true)
    public List<DoctorAvailability> getDoctorAvailabilities(Long doctorId) {
        Doctor doctor = findDoctor(doctorId);
        return availabilityRepository.findByDoctorAndActiveTrueOrderByDayOfWeekAscStartTimeAsc(doctor);
    }

    @Transactional
    public DoctorAvailability createDoctorAvailability(Long doctorId, DoctorAvailabilityRequest request) {
        Doctor doctor = findDoctor(doctorId);
        validateAvailabilityRequest(request);
        if (availabilityRepository.findByDoctorAndDayOfWeekAndStartTimeAndActiveTrue(doctor, request.getDayOfWeek(), request.getStartTime()).isPresent()) {
            throw new ApiException("A recurring slot already exists for this doctor at the given day and time");
        }

        DoctorAvailability availability = DoctorAvailability.builder()
                .doctor(doctor)
                .dayOfWeek(request.getDayOfWeek())
                .startTime(request.getStartTime())
                .slotDurationMinutes(request.getSlotDurationMinutes() == null ? DEFAULT_SLOT_DURATION_MINUTES : request.getSlotDurationMinutes())
                .active(request.getActive() == null ? true : request.getActive())
                .build();
        return availabilityRepository.save(availability);
    }

    @Transactional
    public List<DoctorAvailability> createBulkAvailability(Long doctorId, BulkAvailabilityRequest request) {
        Doctor doctor = findDoctor(doctorId);
        
        if (request.getDayOfWeek() == null || request.getSlots() == null || request.getSlots().isEmpty()) {
            throw new ApiException("Day of week and slots list are required");
        }

        Set<LocalTime> uniqueSlots = new HashSet<>();
        List<DoctorAvailability> createdAvailabilities = new ArrayList<>();

        for (String timeString : request.getSlots()) {
            LocalTime slotTime = parseTimeString(timeString);
            
            // Check for duplicates in request
            if (!uniqueSlots.add(slotTime)) {
                throw new ApiException("Duplicate slot time in request: " + timeString);
            }

            // Check if slot already exists for this day
            if (availabilityRepository.findByDoctorAndDayOfWeekAndStartTimeAndActiveTrue(doctor, request.getDayOfWeek(), slotTime).isPresent()) {
                throw new ApiException("A recurring slot already exists for " + request.getDayOfWeek() + " at " + timeString);
            }

            DoctorAvailability availability = DoctorAvailability.builder()
                    .doctor(doctor)
                    .dayOfWeek(request.getDayOfWeek())
                    .startTime(slotTime)
                    .slotDurationMinutes(DEFAULT_SLOT_DURATION_MINUTES)
                    .active(true)
                    .build();
            
            createdAvailabilities.add(availabilityRepository.save(availability));
        }

        return createdAvailabilities;
    }

    @Transactional
    public DoctorAvailability updateDoctorAvailability(Long doctorId, Long availabilityId, DoctorAvailabilityRequest request) {
        Doctor doctor = findDoctor(doctorId);
        DoctorAvailability availability = availabilityRepository.findByIdAndDoctor(availabilityId, doctor)
                .orElseThrow(() -> new ApiException("Availability not found for this doctor"));

        DayOfWeek newDayOfWeek = request.getDayOfWeek() != null ? request.getDayOfWeek() : availability.getDayOfWeek();
        var newStartTime = request.getStartTime() != null ? request.getStartTime() : availability.getStartTime();
        var newActive = request.getActive() != null ? request.getActive() : availability.isActive();

        if (newActive && availabilityRepository.findByDoctorAndDayOfWeekAndStartTimeAndActiveTrue(doctor, newDayOfWeek, newStartTime)
                .filter(existing -> !existing.getId().equals(availability.getId()))
                .isPresent()) {
            throw new ApiException("A recurring slot already exists for this doctor at the given day and time");
        }

        availability.setDayOfWeek(newDayOfWeek);
        availability.setStartTime(newStartTime);
        if (request.getSlotDurationMinutes() != null) {
            availability.setSlotDurationMinutes(request.getSlotDurationMinutes());
        }
        availability.setActive(newActive);

        validateAvailabilityRequest(availability);
        return availabilityRepository.save(availability);
    }

    @Transactional
    public void deleteDoctorAvailability(Long doctorId, Long availabilityId) {
        Doctor doctor = findDoctor(doctorId);
        DoctorAvailability availability = availabilityRepository.findByIdAndDoctor(availabilityId, doctor)
                .orElseThrow(() -> new ApiException("Availability not found for this doctor"));
        availabilityRepository.delete(availability);
    }

    @Transactional(readOnly = true)
    public List<DoctorBlock> getDoctorBlocks(Long doctorId) {
        Doctor doctor = findDoctor(doctorId);
        return blockRepository.findByDoctorOrderByStartAtAsc(doctor);
    }

    @Transactional
    public DoctorBlock createDoctorBlock(Long doctorId, DoctorBlockRequest request) {
        Doctor doctor = findDoctor(doctorId);
        validateDoctorBlockRequest(request);
        if (request.getType() == null) {
            throw new ApiException("Doctor block type is required");
        }
        if (!blockRepository.findOverlappingBetween(doctor, request.getStartAt(), request.getEndAt()).isEmpty()) {
            throw new ApiException("Doctor block overlaps an existing blocked period");
        }

        DoctorBlock block = DoctorBlock.builder()
                .doctor(doctor)
                .startAt(request.getStartAt())
                .endAt(request.getEndAt())
                .reason(request.getReason())
                .type(request.getType())
                .build();

        return blockRepository.save(block);
    }

    @Transactional
    public void deleteDoctorBlock(Long doctorId, Long blockId) {
        Doctor doctor = findDoctor(doctorId);
        DoctorBlock block = blockRepository.findByIdAndDoctor(blockId, doctor)
                .orElseThrow(() -> new ApiException("Doctor block not found for this doctor"));
        blockRepository.delete(block);
    }

    @Transactional(readOnly = true)
    public List<DateBasedSlotResponse> getAvailableSlotsByDate(Long doctorId, LocalDate date) {
        Doctor doctor = findDoctor(doctorId);
        
        if (date == null) {
            throw new ApiException("Date is required");
        }

        if (!doctor.isAvailable()) {
            return List.of();
        }

        DayOfWeek dayOfWeek = date.getDayOfWeek();
        
        // Load recurring slots for this day of week
        List<DoctorAvailability> daySlots = availabilityRepository.findByDoctorAndDayOfWeekAndActiveTrueOrderByStartTimeAsc(doctor, dayOfWeek);
        if (daySlots.isEmpty()) {
            return List.of();
        }

        // Load blocked periods that overlap with this date
        LocalDateTime dateStart = LocalDateTime.of(date, LocalTime.MIDNIGHT);
        LocalDateTime dateEnd = LocalDateTime.of(date, LocalTime.MAX);
        List<DoctorBlock> blocks = blockRepository.findOverlappingBetween(doctor, dateStart, dateEnd);

        // Load already booked appointments for this date
        List<Appointment> bookedAppointments = appointmentRepository.findActiveAppointmentsByDoctorAndStartAtBetween(doctor, dateStart, dateEnd);

        List<DateBasedSlotResponse> availableSlots = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();

        for (DoctorAvailability slot : daySlots) {
            LocalDateTime slotStart = LocalDateTime.of(date, slot.getStartTime());
            LocalDateTime slotEnd = slotStart.plusMinutes(slot.getSlotDurationMinutes());

            // Skip past slots if date is today
            if (date.equals(LocalDate.now()) && slotStart.isBefore(now)) {
                continue;
            }

            // Skip if slot is blocked
            if (overlapsBlocks(slotStart, slotEnd, blocks)) {
                continue;
            }

            // Skip if slot is already booked
            if (isBooked(doctor, slotStart, bookedAppointments)) {
                continue;
            }

            DateBasedSlotResponse slotResponse = new DateBasedSlotResponse(
                    date,
                    slot.getStartTime(),
                    slot.getStartTime().plusMinutes(slot.getSlotDurationMinutes()),
                    slot.getSlotDurationMinutes()
            );
            availableSlots.add(slotResponse);
        }

        return availableSlots;
    }

    @Transactional(readOnly = true)
    public List<AvailableDateResponse> getCalendarAvailability(Long doctorId, String yearMonth) {
        Doctor doctor = findDoctor(doctorId);
        
        if (yearMonth == null || yearMonth.isBlank()) {
            throw new ApiException("Month in YYYY-MM format is required");
        }

        YearMonth month;
        try {
            month = YearMonth.parse(yearMonth);
        } catch (Exception ex) {
            throw new ApiException("Invalid month format. Use YYYY-MM format");
        }

        if (!doctor.isAvailable()) {
            return List.of();
        }

        List<AvailableDateResponse> availableDates = new ArrayList<>();
        LocalDate startDate = month.atDay(1);
        LocalDate endDate = month.atEndOfMonth();

        LocalDate currentDate = startDate;
        while (!currentDate.isAfter(endDate)) {
            List<DateBasedSlotResponse> slots = getAvailableSlotsByDate(doctorId, currentDate);
            if (!slots.isEmpty()) {
                availableDates.add(new AvailableDateResponse(currentDate, slots.size()));
            }
            currentDate = currentDate.plusDays(1);
        }

        return availableDates;
    }

    @Transactional(readOnly = true)
    public DoctorAvailability validateSlotForBooking(Doctor doctor, LocalDateTime slotStart) {
        if (doctor == null) {
            throw new ApiException("Doctor not found");
        }
        if (slotStart == null) {
            throw new ApiException("Slot start time is required");
        }
        if (!doctor.isAvailable()) {
            throw new ApiException("Doctor is not available");
        }

        DayOfWeek dayOfWeek = slotStart.getDayOfWeek();
        List<DoctorAvailability> availabilities = availabilityRepository.findByDoctorAndActiveTrueOrderByDayOfWeekAscStartTimeAsc(doctor);
        if (availabilities.isEmpty()) {
            throw new ApiException("No recurring slots are configured for this doctor");
        }

        DoctorAvailability matchedAvailability = availabilityRepository
                .findByDoctorAndDayOfWeekAndStartTimeAndActiveTrue(doctor, dayOfWeek, slotStart.toLocalTime())
                .orElseThrow(() -> new ApiException("The selected slot is not part of doctor's recurring schedule"));

        LocalDateTime slotEnd = slotStart.plusMinutes(matchedAvailability.getSlotDurationMinutes());
        if (overlapsBlocks(slotStart, slotEnd, blockRepository.findOverlappingSlots(doctor, slotStart, slotEnd))) {
            throw new ApiException("The selected slot is blocked");
        }

        return matchedAvailability;
    }

    private boolean isBooked(Doctor doctor, LocalDateTime slotStart, List<Appointment> appointments) {
        return appointments.stream()
                .anyMatch(appointment -> appointment.getStartAt().equals(slotStart)
                        && appointment.getStatus() != AppointmentStatus.CANCELLED);
    }

    private boolean overlapsBlocks(LocalDateTime slotStart, LocalDateTime slotEnd, List<DoctorBlock> blocks) {
        return blocks.stream().anyMatch(block -> slotStart.isBefore(block.getEndAt()) && slotEnd.isAfter(block.getStartAt()));
    }

    private LocalTime parseTimeString(String timeString) {
        try {
            return LocalTime.parse(timeString);
        } catch (Exception ex) {
            throw new ApiException("Invalid time format: " + timeString + ". Use HH:mm format");
        }
    }

    private Doctor findDoctor(Long doctorId) {
        return doctorRepository.findById(doctorId)
                .orElseThrow(() -> new ApiException("Doctor not found"));
    }

    private void validateAvailabilityRequest(DoctorAvailabilityRequest request) {
        if (request == null) {
            throw new ApiException("Availability request is required");
        }
        if (request.getDayOfWeek() == null) {
            throw new ApiException("Day of week is required");
        }
        if (request.getStartTime() == null) {
            throw new ApiException("Slot start time is required");
        }
        int duration = request.getSlotDurationMinutes() == null ? DEFAULT_SLOT_DURATION_MINUTES : request.getSlotDurationMinutes();
        if (duration != DEFAULT_SLOT_DURATION_MINUTES) {
            throw new ApiException("Only 30-minute slots are supported");
        }
    }

    private void validateAvailabilityRequest(DoctorAvailability availability) {
        if (availability == null) {
            throw new ApiException("Availability record is required");
        }
        if (availability.getDayOfWeek() == null) {
            throw new ApiException("Day of week is required");
        }
        if (availability.getStartTime() == null) {
            throw new ApiException("Slot start time is required");
        }
        int duration = availability.getSlotDurationMinutes();
        if (duration != DEFAULT_SLOT_DURATION_MINUTES) {
            throw new ApiException("Only 30-minute slots are supported");
        }
    }

    private void validateDoctorBlockRequest(DoctorBlockRequest request) {
        if (request == null) {
            throw new ApiException("Doctor block request is required");
        }
        if (request.getStartAt() == null || request.getEndAt() == null) {
            throw new ApiException("Both start and end date-times are required");
        }
        if (!request.getStartAt().isBefore(request.getEndAt())) {
            throw new ApiException("Block start time must be before end time");
        }
        if (request.getReason() == null || request.getReason().isBlank()) {
            throw new ApiException("Block reason is required");
        }
        if (request.getType() == null) {
            throw new ApiException("Block type is required");
        }
    }
}
