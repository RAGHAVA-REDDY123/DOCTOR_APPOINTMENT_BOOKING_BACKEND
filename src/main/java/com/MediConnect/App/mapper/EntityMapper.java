package com.MediConnect.App.mapper;

import com.MediConnect.App.dto.response.*;
import com.MediConnect.App.entity.*;

import java.util.stream.Collectors;

public class EntityMapper {

    public static UserResponse toUserResponse(User u) {
        if (u == null) return null;
        UserResponse r = new UserResponse();
        r.setId(u.getId());
        r.setName(u.getName());
        r.setEmail(u.getEmail());
        r.setPhone(u.getPhone());
        r.setRole(u.getRole());
        r.setProfileImageUrl(u.getProfileImageUrl());
        r.setVerified(u.isVerified());
        r.setEnabled(u.isEnabled());
        r.setCreatedAt(u.getCreatedAt());
        r.setUpdatedAt(u.getUpdatedAt());
        return r;
    }

    public static DoctorResponse toDoctorResponse(Doctor d) {
        if (d == null) return null;
        DoctorResponse r = new DoctorResponse();
        r.setId(d.getId());
        r.setUserId(d.getUser() != null ? d.getUser().getId() : null);
        r.setRegistrationNumber(d.getRegistrationNumber());
        r.setSpecialization(d.getSpecialization());
        r.setQualification(d.getQualification());
        r.setExperienceYears(d.getExperienceYears());
        r.setConsultationFee(d.getConsultationFee());
        r.setHospitalName(d.getHospitalName());
        r.setClinicAddress(d.getClinicAddress());
        r.setCity(d.getCity());
        r.setState(d.getState());
        r.setBio(d.getBio());
        r.setRating(d.getRating());
        r.setTotalReviews(d.getTotalReviews());
        r.setAvailable(d.isAvailable());
        return r;
    }

    public static PatientResponse toPatientResponse(Patient p) {
        if (p == null) return null;
        PatientResponse r = new PatientResponse();
        r.setId(p.getId());
        r.setUserId(p.getUser() != null ? p.getUser().getId() : null);
        r.setDateOfBirth(p.getDateOfBirth());
        r.setGender(p.getGender());
        r.setBloodGroup(p.getBloodGroup());
        r.setHeightCm(p.getHeightCm());
        r.setWeightKg(p.getWeightKg());
        r.setEmergencyContactName(p.getEmergencyContactName());
        r.setEmergencyContactPhone(p.getEmergencyContactPhone());
        return r;
    }

    public static HealthSummaryResponse toHealthSummaryResponse(HealthSummary s) {
        if (s == null) return null;
        HealthSummaryResponse r = new HealthSummaryResponse();
        r.setId(s.getId());
        r.setPatientId(s.getPatient() != null ? s.getPatient().getId() : null);
        r.setMedicalConditions(s.getMedicalConditions());
        r.setCurrentMedications(s.getCurrentMedications());
        r.setAllergies(s.getAllergies());
        r.setPreviousSurgeries(s.getPreviousSurgeries());
        r.setFamilyMedicalHistory(s.getFamilyMedicalHistory());
        r.setAdditionalNotes(s.getAdditionalNotes());
        return r;
    }

    public static ConsultationResponse toConsultationResponse(Consultation c) {
        if (c == null) return null;
        ConsultationResponse r = new ConsultationResponse();
        r.setId(c.getId());
        r.setAppointmentId(c.getAppointment() != null ? c.getAppointment().getId() : null);
        r.setDoctorId(c.getDoctor() != null ? c.getDoctor().getId() : null);
        r.setPatientId(c.getPatient() != null ? c.getPatient().getId() : null);
        r.setChiefComplaint(c.getChiefComplaint());
        r.setDiagnosis(c.getDiagnosis());
        r.setConsultationNotes(c.getConsultationNotes());
        r.setFollowUpDate(c.getFollowUpDate());
        return r;
    }

    public static PrescriptionResponse toPrescriptionResponse(Prescription p) {
        if (p == null) return null;
        PrescriptionResponse r = new PrescriptionResponse();
        r.setId(p.getId());
        r.setPrescriptionNumber(p.getPrescriptionNumber());
        r.setAppointmentId(p.getAppointment() != null ? p.getAppointment().getId() : null);
        r.setDoctorId(p.getDoctor() != null ? p.getDoctor().getId() : null);
        r.setPatientId(p.getPatient() != null ? p.getPatient().getId() : null);
        r.setIssuedAt(p.getIssuedAt());
        r.setValidityDays(p.getValidityDays());
        r.setNotes(p.getNotes());
        if (p.getItems() != null) {
            r.setItems(p.getItems().stream().map(it -> {
                PrescriptionResponse.PrescriptionItemResponse ir = new PrescriptionResponse.PrescriptionItemResponse();
                ir.id = it.getId();
                ir.medicineName = it.getMedicineName();
                ir.dosage = it.getDosage();
                ir.frequency = it.getFrequency();
                ir.durationDays = it.getDurationDays();
                ir.instructions = it.getInstructions();
                return ir;
            }).collect(Collectors.toList()));
        }
        return r;
    }

    public static AppointmentResponse toAppointmentResponse(Appointment a) {
        if (a == null) return null;
        AppointmentResponse r = new AppointmentResponse();
        r.setBookingId(a.getBookingId());
        r.setPatientId(a.getPatient() != null ? a.getPatient().getId() : null);
        r.setDoctorId(a.getDoctor() != null ? a.getDoctor().getId() : null);
        r.setStartAt(a.getStartAt());
        r.setEndAt(a.getEndAt());
        r.setStatus(a.getStatus());
        r.setConsultationFee(a.getConsultationFee());
        return r;
    }

    public static DoctorAvailabilityResponse toDoctorAvailabilityResponse(DoctorAvailability availability) {
        if (availability == null) return null;
        DoctorAvailabilityResponse response = new DoctorAvailabilityResponse();
        response.setId(availability.getId());
        response.setDayOfWeek(availability.getDayOfWeek());
        response.setStartTime(availability.getStartTime());
        response.setSlotDurationMinutes(availability.getSlotDurationMinutes());
        response.setActive(availability.isActive());
        return response;
    }

    public static DoctorBlockResponse toDoctorBlockResponse(DoctorBlock block) {
        if (block == null) return null;
        DoctorBlockResponse response = new DoctorBlockResponse();
        response.setId(block.getId());
        response.setType(block.getType());
        response.setStartAt(block.getStartAt());
        response.setEndAt(block.getEndAt());
        response.setReason(block.getReason());
        return response;
    }
}
