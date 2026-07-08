package com.MediConnect.App.dto.request;

import com.MediConnect.App.enums.DoctorBlockType;

import java.time.LocalDateTime;

public class DoctorBlockRequest {
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private String reason;
    private DoctorBlockType type;

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

    public DoctorBlockType getType() {
        return type;
    }

    public void setType(DoctorBlockType type) {
        this.type = type;
    }
}
