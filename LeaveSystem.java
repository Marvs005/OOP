package main.java.backend;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LeaveSystem {
    private String employeeID, firstName, lastName;

    private int used_Sick, used_Vacation, used_Emergency, remain_Sick,
            remain_Vacation, remain_Emergency, used_Total, remain_Total;
    private String date, reason;

    private final String recordPath = "src/resources/data/Leave.csv";
    private final String historyPath = "src/resources/data/LeaveHistory.csv";
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

    // Constructor
    public LeaveSystem(String employeeID) {
        this.employeeID = employeeID;
        getRecord();
        getHistory();
    }

    public void getRecord(){

        try {
            // Open the CSV file and create a reader
            CSVReader reader = new CSVReader(new FileReader(recordPath));

            // Read the header row and get the index of the Employee # column
            String[] header = reader.readNext();
            int empNumIndex = -1;
            for (int i = 0; i < header.length; i++) {
                if (header[i].equals("Employee #")) {
                    empNumIndex = i;
                    break;
                }
            }
            if (empNumIndex == -1) {
                System.out.println("Error: Could not find 'Employee #' column in CSV file");
                return;
            }

            // Find the row for the given user ID and extract the data
            String[] row;
            boolean foundUser = false;
            while ((row = reader.readNext()) != null) {
                if (row[empNumIndex].equals(employeeID)) {
                    lastName = row[1];
                    firstName = row[2];
                    used_Sick = Integer.parseInt(row[3]);
                    used_Emergency = Integer.parseInt(row[4]);
                    used_Vacation = Integer.parseInt(row[5]);
                    remain_Sick = Integer.parseInt(row[6]);
                    remain_Vacation = Integer.parseInt(row[7]);
                    remain_Emergency = Integer.parseInt(row[8]);
                    used_Total = Integer.parseInt(row[9]);
                    remain_Total = Integer.parseInt(row[10]);

                    foundUser = true;
                    break;
                }
            }
            if (!foundUser) {
                System.out.println("Error: Could not find user with ID " + employeeID + " in CSV file");
            }

            // Close the reader
            reader.close();
        } catch (IOException | CsvValidationException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    public void getHistory(){

        try {
            // Open the CSV file and create a reader
            CSVReader reader = new CSVReader(new FileReader(historyPath));

            // Read the header row and get the index of the Employee # column
            String[] header = reader.readNext();
            int empNumIndex = -1;
            for (int i = 0; i < header.length; i++) {
                if (header[i].equals("Employee #")) {
                    empNumIndex = i;
                    break;
                }
            }
            if (empNumIndex == -1) {
                System.out.println("Error: Could not find 'Employee #' column in CSV file");
                return;
            }

            // Find the row for the given user ID and extract the data
            String[] row;
            boolean foundUser = false;
            while ((row = reader.readNext()) != null) {
                if (row[empNumIndex].equals(employeeID)) {
                    date = row[3];
                    reason = row[4];

                    foundUser = true;
                    break;
                }
            }
            if (!foundUser) {
                System.out.println("Error: Could not find user with ID " + employeeID + " in CSV file");
            }

            // Close the reader
            reader.close();
        } catch (IOException | CsvValidationException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    //getter methods
    public String getFirstName(){
        return firstName;
    }

    public String getLastName(){
        return lastName;
    }

    public String getReason(){
        return reason;
    }
    public Date getDateRecord() {
        String pattern = "MMMM dd, yyyy";

        DateFormat dateFormat = new SimpleDateFormat(pattern);
        Date Date = null;
        try {
            Date = dateFormat.parse(date);
            System.out.println(Date); // Print the parsed Date object
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return Date;
    }
    public int getUsedSick(){
        return used_Sick;
    }
    public int getUsedVacation(){
        return used_Vacation;
    }
    public int getUsedEmergency(){
        return used_Emergency;
    }
    public int getRemainingSick(){
        return remain_Sick;
    }
    public int getRemainingVacation(){
        return remain_Vacation;
    }
    public int getRemainingEmergency(){
        return remain_Emergency;
    }
    public int getRemainingTotal(){
        return remain_Total;
    }
    public int getUsedTotal(){
        return used_Total;
    }

}