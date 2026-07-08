package com.MediConnect.App.dto.response;

import java.time.LocalDate;
import java.time.LocalTime;

public class DateBasedSlotResponse {
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private Integer slotDurationMinutes;

    public DateBasedSlotResponse() {}

    public DateBasedSlotResponse(LocalDate date, LocalTime startTime, LocalTime endTime, Integer slotDurationMinutes) {
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.slotDurationMinutes = slotDurationMinutes;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public Integer getSlotDurationMinutes() {
        return slotDurationMinutes;
    }

    public void setSlotDurationMinutes(Integer slotDurationMinutes) {
        this.slotDurationMinutes = slotDurationMinutes;
    }
}
