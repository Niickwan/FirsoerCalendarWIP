package com.example.firsoer;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;

public class DB_Controller {
    public DB_Controller() {
        // Empty
    }

    public Connection connectToDB() {
        try {
            String url = "";
            return DriverManager.getConnection(url, "", "");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public ArrayList<Booking> getCalendarForNumberOfWeeks(int weeks) {
        // Get day(dd), month(mm), year(yyyy), hour(hh), minutes(mm) and seconds(ss)
        // Format using SimpleDateFormat to get what we want
        // SimpleDateFormat sdf = new SimpleDateFormat("dd"); <- get day
        // SimpleDateFormat sdf2 = new SimpleDateFormat("hh"); <- get hour
        // sdf.format(rs.getTimestamp("StartDatoOgTid")); <- pass TimeStamp from db to what we want (day)
        // sdf2.format(rs.getTimestamp("StartDatoOgTid")); <- pass TimeStamp from db to what we want (hour)

        ArrayList<Booking> bookings = new ArrayList<>();
        try {
            Connection conn = connectToDB();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM test_booking");
            while(rs.next()) {
                Booking b = new Booking(
                        rs.getInt("BookingID"),
                        rs.getInt("KundeID"),
                        rs.getInt("MedarbejderID"),
                        rs.getInt("OprettetAfID"),
                        rs.getString("Kommentar"),
                        rs.getInt("Tid"),
                        rs.getTimestamp("StartDatoOgTid"),
                        rs.getTime("StartTid"));
                bookings.add(b);
            }
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookings;
    }

    public void testUpdateDate() {
        try {
            Connection conn = connectToDB();
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("UPDATE test_booking SET StartDatoOgTid = ('2022-04-10 12:00:00') WHERE test_booking.BookingID = 1");
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
