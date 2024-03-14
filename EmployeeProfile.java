package main.java.backend;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EmployeeProfile {
    private String employeeID, firstName, lastName, address, status, position, immediateSupervisor, birthday, phoneNumber, sss, philHealth, tin, pagIbig;

    private String formattedBasicSalary, formattedRiceSubsidy, formattedPhoneAllowance, formattedClothingAllowance, formattedGrossRate, formattedHourlyRate;
    private double basicSalary, riceSubsidy, phoneAllowance, clothingAllowance, grossRate, hourlyRate;

    // Constructor
    public EmployeeProfile(String employeeID) {
        this.employeeID = employeeID;
        initialize();
    }

    public void initialize(){

        try {
            // Open the CSV file and create a reader
            CSVReader reader = new CSVReader(new FileReader("src/resources/data/Details.csv"));

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
                    birthday = row[3];
                    address = row[4];
                    phoneNumber = row[5];
                    sss = row[6];
                    philHealth = row[7];
                    tin = row[8];
                    pagIbig = row[9];
                    status = row[10];
                    position = row[11];
                    immediateSupervisor = row[12];

                    // Create a DecimalFormat pattern for displaying double values with leading zeros
                    DecimalFormat decimalFormat = new DecimalFormat("0.00");

                    // Extract the data and format the double values
                    riceSubsidy = Double.parseDouble(row[13]);
                    phoneAllowance = Double.parseDouble(row[14]);
                    clothingAllowance = Double.parseDouble(row[15]);
                    basicSalary = Double.parseDouble(row[16]);
                    grossRate = Double.parseDouble(row[17]);
                    hourlyRate = Double.parseDouble(row[18]);

                    // Format the double values with leading zeros
                    formattedBasicSalary = decimalFormat.format(basicSalary);
                    formattedRiceSubsidy = decimalFormat.format(riceSubsidy);
                    formattedPhoneAllowance = decimalFormat.format(phoneAllowance);
                    formattedClothingAllowance = decimalFormat.format(clothingAllowance);
                    formattedGrossRate = decimalFormat.format(grossRate);
                    formattedHourlyRate = decimalFormat.format(hourlyRate);

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
    public Date getBirthday() {
        String pattern = "MMMM dd, yyyy";

        DateFormat dateFormat = new SimpleDateFormat(pattern);
        Date bDate = null;
        try {
            bDate = dateFormat.parse(birthday);
            System.out.println(bDate); // Print the parsed Date object
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return bDate;
    }
    public String getPosition(){
        return position;
    }
    public String getAddress(){
        return address;
    }
    public String getStatus(){
        return status;
    }
    public String getImmediateSupervisor(){
        return immediateSupervisor;
    }
    public String getPhoneNumber(){
        return phoneNumber;
    }
    public String getSSS(){
        return sss;
    }
    public String getPhilHealth(){
        return philHealth;
    }
    public String getPagIbig(){
        return pagIbig;
    }
    public String getTin(){
        return tin;
    }
    public String getBasicSalary(){
        return String.valueOf(formattedBasicSalary);
    }
    public String getRiceSubsidy(){
        return String.valueOf(formattedRiceSubsidy);
    }
    public String getPhoneAllowance(){
        return String.valueOf(formattedPhoneAllowance);
    }
    public String getClothingAllowance(){
        return String.valueOf(formattedClothingAllowance);
    }
    public String getGrossRate(){
        return String.valueOf(formattedGrossRate);
    }
    public String getHourlyRate(){
        return String.valueOf(formattedHourlyRate);
    }

    public static void main(String[] args) {
        EmployeeProfile employeeProfile = new EmployeeProfile("10001");
        System.out.println(employeeProfile.getBirthday());
    }
}