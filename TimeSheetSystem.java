package main.java.backend;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class TimeSheetSystem {
    private double totalExactHoursWorked;
    private double totalAdjustedHoursWorked;

    public TimeSheetSystem(String employeeID, String startDate, String endDate) throws IOException, CsvException {
        CSVReader reader = new CSVReader(new FileReader("src/resources/data/Attendance.csv"));
        List<String[]> rows = reader.readAll();
        reader.close();

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("H:mm");

        LocalDate startDateObj = LocalDate.parse(startDate, dateFormatter);
        LocalDate endDateObj = LocalDate.parse(endDate, dateFormatter);

        for (String[] row : rows) {
            if (row[0].trim().equals(employeeID.trim())) {
                String dateStr = row[3];
                LocalDate dateObj = LocalDate.parse(dateStr, dateFormatter);

                if (dateObj.isEqual(startDateObj) || dateObj.isEqual(endDateObj) || (dateObj.isAfter(startDateObj) && dateObj.isBefore(endDateObj))) {
                    String timeInStr = row[4];
                    String timeOutStr = row[5];

                    LocalTime timeIn = LocalTime.parse(timeInStr, timeFormatter);
                    LocalTime timeOut = LocalTime.parse(timeOutStr, timeFormatter);

                    int minutesIn = timeIn.getMinute();
                    int minutesOut = timeOut.getMinute();

                    // Calculate exact hours worked
                    double exactHours = timeOut.getHour() - timeIn.getHour() + (minutesOut - minutesIn) / 60.0;
                    totalExactHoursWorked += exactHours;

                    // Calculate adjusted hours worked
                    double adjustedMinutesIn = minutesIn;
                    if (timeIn.isBefore(LocalTime.of(8, 10))) {
                        adjustedMinutesIn = 0;
                    }
                    double adjustedHours = timeOut.getHour() - timeIn.getHour() + (minutesOut - adjustedMinutesIn) / 60.0;
                    totalAdjustedHoursWorked += adjustedHours;
                }
            }
        }
    }

    public double getTotalExactHoursWorked() {
        return totalExactHoursWorked;
    }

    public double getTotalAdjustedHoursWorked() {
        return totalAdjustedHoursWorked;
    }

    public static void main(String[] args) throws IOException, CsvException {
        TimeSheetSystem timeSheet = new TimeSheetSystem("10009", "01/09/2022", "01/09/2022");
        System.out.println("Total exact hours worked: " + timeSheet.getTotalExactHoursWorked());
        System.out.println("Total adjusted hours worked: " + timeSheet.getTotalAdjustedHoursWorked());
    }
}
