package com.MediConnect.App.dto.response;

import com.MediConnect.App.enums.DoctorBlockType;

import java.time.LocalDateTime;

public class DoctorBlockResponse {
    private Long id;
    private DoctorBlockType type;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private String reason;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DoctorBlockType getType() {
        return type;
    }

    public void setType(DoctorBlockType type) {
        this.type = type;
    }

    public LocalDateTime getStartAt() {
        return startAt;
    }

    public void setStartAt(LocalDateTime startAt) {
        this.startAt = startAt;
    }

    public LocalDateTime getEndAt() {
        return endAt;
    }

    public void setEndAt(LocalDateTime endAt) {
        this.endAt = endAt;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
