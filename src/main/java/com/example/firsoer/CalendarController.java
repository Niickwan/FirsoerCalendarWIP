package com.example.firsoer;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.*;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class CalendarController {
    @FXML
    GridPane calendarGrid;

    @FXML
    Label weekNumber;

    Calendar cal = Calendar.getInstance();

    private int openingHour = 9;
    private int closingHour = 16;
    private int workDays = 5;
    private int workingHours = 7;
    private final int quatersInAnHour = 4;
    private final String format = "yyyyMMdd";

    private DB_Controller db = new DB_Controller();
    private ArrayList<Booking> bookings = new ArrayList<>();

    private ArrayList<Pane> paneHolder = new ArrayList<>();

    public void loadDatabase() {
        Pane p = new Pane();
        Tooltip t = new Tooltip("THIS IS A TEST");
        p.getStyleClass().add("appointmentCalendar");
        p.setOnMouseClicked(mouseEvent -> System.out.println("Hello"));
        Tooltip.install(p, t);
        paneHolder.add(p);
        // i = width / i1 = Height / i2 = Appointment Width / i3 = Appointment Height
        calendarGrid.add(p, 5, 6, 4, 4);
    }

    public void doThis() {
        /* REMOVE PANE(APPOINTMENT) FROM CALENDAR */
        calendarGrid.getChildren().remove(paneHolder.get(0));
    }

    public void doThat() throws ParseException {
        //db.testUpdateDate();
        String input = "20220408";
        SimpleDateFormat df = new SimpleDateFormat(format);
        Date date = df.parse(input);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        weekNumber.setText(Integer.toString(cal.get(Calendar.WEEK_OF_YEAR)));
        cal.clear();
        cal.setTimeInMillis(Calendar.getInstance().getTimeInMillis());
        System.out.println(Calendar.getInstance().getTimeInMillis());
        while (cal.get(Calendar.DAY_OF_WEEK) > cal.getFirstDayOfWeek()) {
            cal.add(Calendar.DATE, -1); // Substract 1 day until first day of week.
        }
        long firstDayOfWeekTimestamp = cal.getTimeInMillis();
        Instant instant = Instant.ofEpochMilli ( firstDayOfWeekTimestamp );
        System.out.println(instant);
    }

    public void initialize() throws ParseException {
        // Get weekNumber based on date
        // https://java.tutorialink.com/java-get-week-number-from-any-date/
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date date = sdf.parse(LocalDate.now().toString().replaceAll("[^a-zA-Z0-9]", ""));
        cal.setTime(date);
        weekNumber.setText(Integer.toString(cal.get(Calendar.WEEK_OF_YEAR)));
        generateLabelsForWorkingHours();
        generateBackgroundAndPaneFields();
        this.bookings = db.getCalendarForNumberOfWeeks(1);
        for (int i = 0; i < this.bookings.size(); i++) {
            System.out.println(this.bookings.get(i));
            setAppointmentsForCurrentWeek(this.bookings.get(i));
        }
    }

    private void setAppointmentsForCurrentWeek(Booking booking) {

    }

    public void generateLabelsForWorkingHours() {
        int hour = 9;
        int minutes = 00;
        int row = 1;
        int tempMinutes = 0;
        int tempHour = 0;
        boolean isOddEven = false;

        for (int i = 0; i < workingHours; i++) {
            for (int j = 0; j < quatersInAnHour; j++) {
                Label l = new Label();
                if(minutes == 60) {
                    minutes = 0;
                    hour++;
                }
                if(minutes == 0) {
                    minutes += 15;
                    l.setText(hour + ":00 - " + hour + ":" + minutes);
                } else if(minutes == 45) {
                    tempHour = hour + 1;
                    l.setText(hour + ":" + minutes + " - " + tempHour + ":00");
                    minutes +=15;
                } else {
                    tempMinutes = minutes+15;
                    l.setText(hour + ":" + minutes + " - " + hour + ":" + tempMinutes);
                    minutes += 15;
                }
                l.setAlignment(Pos.CENTER);
                l.setPrefHeight(500);
                l.setPrefWidth(500);
                l.setMinHeight(17);
                l.getStylesheets().removeAll();
                if(isOddEven) l.getStyleClass().add("rowEvenTime");
                else l.getStyleClass().add("rowOddTime");
                calendarGrid.add(l, 0, row, 1, 1);
                row++;
            }
            isOddEven = !isOddEven;
        }
    }

    public void generateBackgroundAndPaneFields() {
        int columnIndex = 1;
        int rowIndex = 1;
        boolean isRowOddEven = false;

        for (int i = 1; i < workDays+1; i++) {
            for (int j = 1; j < workingHours+1; j++) {
                for (int k = 1; k < quatersInAnHour+1; k++) {
                    Pane p = new Pane();
                    if (isRowOddEven) p.getStyleClass().add("rowEven");
                    else p.getStyleClass().add("rowOdd");
                    calendarGrid.add(p, columnIndex, rowIndex, 4, 1);
                    rowIndex++;
                }
                isRowOddEven = !isRowOddEven;
            }
            isRowOddEven = false;
            rowIndex = 1;
            columnIndex += 4;
        }
    }
}
