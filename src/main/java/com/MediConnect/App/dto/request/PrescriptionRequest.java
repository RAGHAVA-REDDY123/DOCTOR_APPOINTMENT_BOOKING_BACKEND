package com.MediConnect.App.dto.request;

import java.util.List;

public class PrescriptionRequest {
    private Long appointmentId;
    private Long doctorId;
    private Long patientId;
    private String notes;
    private List<PrescriptionItemDTO> items;

    public static class PrescriptionItemDTO {
        public String medicineName;
        public String dosage;
        public String frequency;
        public Integer durationDays;
        public String instructions;
    }

    public Long getAppointmentId() { return appointmentId; }
    public void setAppointmentId(Long appointmentId) { this.appointmentId = appointmentId; }
    public Long getDoctorId() { return doctorId; }
    public void setDoctorId(Long doctorId) { this.doctorId = doctorId; }
    public Long getPatientId() { return patientId; }
    public void setPatientId(Long patientId) { this.patientId = patientId; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public List<PrescriptionItemDTO> getItems() { return items; }
    public void setItems(List<PrescriptionItemDTO> items) { this.items = items; }
}
