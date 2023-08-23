package org.example.createbooking.cucumber;

import java.util.ArrayList;
import java.util.List;

public class ServicesContext {
    List<Integer> bookingIds;

    public ServicesContext() {
        bookingIds = new ArrayList<>();
    }

    public List<Integer> getBookingIds() {
        return bookingIds;
    }

    public void setBookingIds(List<Integer> bookingIds) {
        this.bookingIds = bookingIds;
    }
}
