package com.MediConnect.App.dto.request;

import java.time.DayOfWeek;
import java.util.List;

public class BulkAvailabilityRequest {
    private DayOfWeek dayOfWeek;
    private List<String> slots; // List of time strings in HH:mm format (e.g., "09:00", "09:30", "10:00")

    public BulkAvailabilityRequest() {}

    public BulkAvailabilityRequest(DayOfWeek dayOfWeek, List<String> slots) {
        this.dayOfWeek = dayOfWeek;
        this.slots = slots;
    }

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(DayOfWeek dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public List<String> getSlots() {
        return slots;
    }

    public void setSlots(List<String> slots) {
        this.slots = slots;
    }
}
