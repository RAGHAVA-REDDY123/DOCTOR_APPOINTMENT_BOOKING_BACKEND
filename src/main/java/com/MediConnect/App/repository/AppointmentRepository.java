package com.MediConnect.App.repository;

import com.MediConnect.App.entity.Appointment;
import com.MediConnect.App.entity.Doctor;
import com.MediConnect.App.entity.Patient;
import com.MediConnect.App.enums.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import jakarta.persistence.LockModeType;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    Optional<Appointment> findByBookingId(UUID bookingId);
    List<Appointment> findByPatientOrderByStartAtDesc(Patient patient);
    List<Appointment> findByDoctorOrderByStartAtDesc(Doctor doctor);
    boolean existsByDoctorAndStartAtAndStatusNot(Doctor doctor, LocalDateTime startAt, AppointmentStatus status);
    List<Appointment> findByDoctorAndStartAtBetween(Doctor doctor, LocalDateTime start, LocalDateTime end);
    List<Appointment> findByDoctorAndStartAtBetweenAndStatusNot(Doctor doctor, LocalDateTime start, LocalDateTime end, AppointmentStatus status);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT a FROM Appointment a WHERE a.doctor = :doctor AND ((a.startAt < :endAt) AND (a.endAt > :startAt)) AND a.status <> com.MediConnect.App.enums.AppointmentStatus.CANCELLED")
    List<Appointment> findOverlappingAppointmentsForUpdate(@Param("doctor") Doctor doctor, @Param("startAt") LocalDateTime startAt, @Param("endAt") LocalDateTime endAt);

    @Query("SELECT a FROM Appointment a WHERE a.doctor = :doctor AND a.startAt >= :rangeStart AND a.startAt < :rangeEnd AND a.status <> com.MediConnect.App.enums.AppointmentStatus.CANCELLED")
    List<Appointment> findActiveAppointmentsByDoctorAndStartAtBetween(@Param("doctor") Doctor doctor, @Param("rangeStart") LocalDateTime rangeStart, @Param("rangeEnd") LocalDateTime rangeEnd);
}
