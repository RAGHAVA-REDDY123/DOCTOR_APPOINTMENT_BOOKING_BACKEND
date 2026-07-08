package com.MediConnect.App.dto.response;

import java.time.LocalDate;

public class AvailableDateResponse {
    private LocalDate date;
    private Integer availableSlots;

    public AvailableDateResponse() {}

    public AvailableDateResponse(LocalDate date, Integer availableSlots) {
        this.date = date;
        this.availableSlots = availableSlots;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Integer getAvailableSlots() {
        return availableSlots;
    }

    public void setAvailableSlots(Integer availableSlots) {
        this.availableSlots = availableSlots;
    }
}
