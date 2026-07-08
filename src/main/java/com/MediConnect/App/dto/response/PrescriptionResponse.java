package com.MediConnect.App.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class PrescriptionResponse {
    private Long id;
    private UUID prescriptionNumber;
    private Long appointmentId;
    private Long doctorId;
    private Long patientId;
    private LocalDateTime issuedAt;
    private Integer validityDays;
    private String notes;
    private List<PrescriptionItemResponse> items;

    public static class PrescriptionItemResponse {
        public Long id;
        public String medicineName;
        public String dosage;
        public String frequency;
        public Integer durationDays;
        public String instructions;
    }

    // getters/setters omitted for brevity in this scaffold
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public UUID getPrescriptionNumber() { return prescriptionNumber; }
    public void setPrescriptionNumber(UUID prescriptionNumber) { this.prescriptionNumber = prescriptionNumber; }
    public Long getAppointmentId() { return appointmentId; }
    public void setAppointmentId(Long appointmentId) { this.appointmentId = appointmentId; }
    public Long getDoctorId() { return doctorId; }
    public void setDoctorId(Long doctorId) { this.doctorId = doctorId; }
    public Long getPatientId() { return patientId; }
    public void setPatientId(Long patientId) { this.patientId = patientId; }
    public LocalDateTime getIssuedAt() { return issuedAt; }
    public void setIssuedAt(LocalDateTime issuedAt) { this.issuedAt = issuedAt; }
    public Integer getValidityDays() { return validityDays; }
    public void setValidityDays(Integer validityDays) { this.validityDays = validityDays; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public List<PrescriptionItemResponse> getItems() { return items; }
    public void setItems(List<PrescriptionItemResponse> items) { this.items = items; }
}
