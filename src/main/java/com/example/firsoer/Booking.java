package com.example.firsoer;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

public class Booking {
    private int bookingID;
    private int kundeID;
    private int medarbejderID;
    private int oprettetAfID;
    private ArrayList<String> behandlingsTyper;
    private int tid;
    private String kommentar;
    private Timestamp startDato;
    private Time startTid;

    public Booking(int bookingID, int kundeID, int medarbejderID, int oprettetAfID, String kommentar, int tid, Timestamp startDato, Time startTid) {
        this.bookingID = bookingID;
        this.kundeID = kundeID;
        this.medarbejderID = medarbejderID;
        this.oprettetAfID = oprettetAfID;
        this.kommentar = kommentar;
        this.tid = tid;
        this.startDato = startDato;
        this.startTid = startTid;
    }

    @Override
    public String toString() {
        return "Booking{" +
                "bookingID=" + bookingID +
                ", kundeID=" + kundeID +
                ", medarbejderID=" + medarbejderID +
                ", oprettetAfID=" + oprettetAfID +
                ", tid=" + tid +
                ", kommentar='" + kommentar + '\'' +
                ", startDato=" + startDato +
                ", startTid=" + startTid +
                '}';
    }
}
