package com.MediConnect.App.repository;

import com.MediConnect.App.entity.Doctor;
import com.MediConnect.App.entity.DoctorAvailability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorAvailabilityRepository extends JpaRepository<DoctorAvailability, Long> {
    List<DoctorAvailability> findByDoctorAndActiveTrueOrderByDayOfWeekAscStartTimeAsc(Doctor doctor);
    Optional<DoctorAvailability> findByIdAndDoctor(Long id, Doctor doctor);
    Optional<DoctorAvailability> findByDoctorAndDayOfWeekAndStartTimeAndActiveTrue(Doctor doctor, DayOfWeek dayOfWeek, LocalTime startTime);
    List<DoctorAvailability> findByDoctorAndDayOfWeekAndActiveTrueOrderByStartTimeAsc(Doctor doctor, DayOfWeek dayOfWeek);
}
