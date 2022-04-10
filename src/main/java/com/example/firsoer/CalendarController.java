package com.example.firsoer;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

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
    private ArrayList<Label> weekDaysLabels = new ArrayList<>();

    private String[] longWeekDays = {"Mandag", "Tirsdag", "Onsdag", "Torsdag", "Fredag", "Lørdag", "Søndag"};
    private String[] shortWeekDays = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
    private final int FIRSTDAYOFWEEK = 0;


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

    public void doThat() {

    }

    public void initialize() throws ParseException {
        this.bookings = db.getCalendarForNumberOfWeeks(1);
        for (int i = 0; i < this.bookings.size(); i++) {
            System.out.println(this.bookings.get(i));
            setAppointmentsForCurrentWeek(this.bookings.get(i));
        }
        generateLabelsForWeekDays();
        updateLabelsForWeekDaysAndWeekNumber(0); // Show current week
        generateLabelsForWorkingHours();
        generateBackgroundAndPaneFields();
    }

    // sættes int til +/-7, 14, 21 osv... indlæses den pågældende uge.
    private void updateLabelsForWeekDaysAndWeekNumber(int showWeekBasedOnCurrentDate) throws ParseException {
//        System.out.println(LocalDate.now()); // yyyy-MM-dd
//        System.out.println(LocalTime.now().toString()); // hh:mm:ss
//        System.out.println(Calendar.getInstance().getTime()); // Sat Apr 09 23:27:01 CEST 2022
        LocalDate date = LocalDate.parse(LocalDate.now().toString());
        date = date.plusDays(showWeekBasedOnCurrentDate);
        int firstDayInWeek = 0;
        for (int i = 0; i < shortWeekDays.length; i++) {
            if (Calendar.getInstance().getTime().toString().substring(0, 3).equalsIgnoreCase(shortWeekDays[i])) firstDayInWeek = i;
        }
        for (int i = 0; i < weekDaysLabels.size(); i++) {
            weekDaysLabels.get(i).setText(longWeekDays[i] + " d. " +
                        date.minusDays(firstDayInWeek-i).toString().substring(8, 10).replaceFirst("^0+(?!$)", "") +
                        "/" + date.minusDays(firstDayInWeek-i).toString().substring(5, 7).replaceFirst("^0+(?!$)", ""));
        }
        updateWeekNumber(showWeekBasedOnCurrentDate);
    }

    private void updateWeekNumber(int showWeekBasedOnCurrentDate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date date = sdf.parse(LocalDate.now().plusDays(showWeekBasedOnCurrentDate).toString().replaceAll("[^a-zA-Z0-9]", ""));
        cal.setTime(date);
        weekNumber.setText(Integer.toString(cal.get(Calendar.WEEK_OF_YEAR)));
    }

    private void generateLabelsForWeekDays() {
        int incXPos = 0;
        int columnNumber = 1;
        for (int i = 0; i < workDays; i++) {
            Label l = new Label();
            l.setAlignment(Pos.CENTER);
            l.setLayoutX(110+incXPos);
            l.setLayoutY(10);
            l.setPrefHeight(500);
            l.setPrefWidth(500);
            l.getStyleClass().add("weekDay");
            // i = width / i1 = Height / i2 = Width in grid / i3 = Height in grid
            calendarGrid.add(l,columnNumber, 0, 4, 1);
            weekDaysLabels.add(l);
            incXPos += 140;
            columnNumber += 4;
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