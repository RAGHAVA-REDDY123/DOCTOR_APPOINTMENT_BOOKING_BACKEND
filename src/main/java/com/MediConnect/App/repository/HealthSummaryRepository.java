package com.MediConnect.App.repository;

import com.MediConnect.App.entity.HealthSummary;
import com.MediConnect.App.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HealthSummaryRepository extends JpaRepository<HealthSummary, Long> {
    Optional<HealthSummary> findByPatient(Patient patient);
}
