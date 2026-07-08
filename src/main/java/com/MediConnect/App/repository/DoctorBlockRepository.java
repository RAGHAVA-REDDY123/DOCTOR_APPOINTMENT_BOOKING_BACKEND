package com.MediConnect.App.repository;

import com.MediConnect.App.entity.Doctor;
import com.MediConnect.App.entity.DoctorBlock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorBlockRepository extends JpaRepository<DoctorBlock, Long> {
    List<DoctorBlock> findByDoctorOrderByStartAtAsc(Doctor doctor);

    @Query("SELECT b FROM DoctorBlock b WHERE b.doctor = :doctor AND ((b.startAt < :endAt) AND (b.endAt > :startAt))")
    List<DoctorBlock> findOverlappingSlots(@Param("doctor") Doctor doctor,
                                           @Param("startAt") LocalDateTime startAt,
                                           @Param("endAt") LocalDateTime endAt);

    @Query("SELECT b FROM DoctorBlock b WHERE b.doctor = :doctor AND ((b.startAt < :rangeEnd) AND (b.endAt > :rangeStart))")
    List<DoctorBlock> findOverlappingBetween(@Param("doctor") Doctor doctor,
                                             @Param("rangeStart") LocalDateTime rangeStart,
                                             @Param("rangeEnd") LocalDateTime rangeEnd);

    Optional<DoctorBlock> findByIdAndDoctor(Long id, Doctor doctor);

    List<DoctorBlock> findByDoctorAndTypeOrderByStartAtAsc(Doctor doctor, com.MediConnect.App.enums.DoctorBlockType type);
}
