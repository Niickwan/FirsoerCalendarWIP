package com.example.firsoer;

import java.util.ArrayList;

public class BookingContainer {
    private static BookingContainer instance;
    private ArrayList<Booking> bookingForNumberOfWeeks;
    private DB_Controller db;

    private BookingContainer() {
        this.db = new DB_Controller();
    }

    public static BookingContainer getInstance() {
        if(instance == null) {
            instance = new BookingContainer();
        }
        return instance;
    }
}
