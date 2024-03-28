package main.java.backend;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import com.opencsv.exceptions.CsvValidationException;

import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;

public class NetPayCalculator {
    private double calculator;
    private double basicSalary;
    private double semi_GrossPay;
    private double hourly_Rate;
    private double riceSubsidy;
    private double phoneAllowance;
    private double clothingAllowance ;

    private double org_riceSubsidy;
    private double org_phoneAllowance;
    private double org_clothingAllowance ;
    private double sssContribution;

    private double philhealthContribution;

    private double pagibigContribution;
    private double totalDeductions;
    private double totalAllowances;
    private double withholdingTax;
    private double netPay;
    private double grossPay;
    private String employeeID;
    private String mode;
    private String start_Date;
    private String end_Date;

    public NetPayCalculator() throws IOException, CsvException {
    }

    public void Calculate() throws IOException, CsvException {
        ReadData();
        Allowances();
        ModeSet();
        Deductions();
        // Compute Withholding Tax
        totalDeductions = sssContribution + philhealthContribution + pagibigContribution;
        //calculate gross pay
        grossPay = calculator + totalAllowances;
        double taxableIncome = grossPay - totalDeductions;

        if (taxableIncome <= 0) {
            withholdingTax = 0;
        } else if (taxableIncome <= 20833.33) {
            withholdingTax = taxableIncome * 0.20;
        } else if (taxableIncome <= 33333.33) {
            withholdingTax = 20833.33 * 0.20 + (taxableIncome - 20833.33) * 0.25;
        } else if (taxableIncome <= 66666.67) {
            withholdingTax = 20833.33 * 0.20 + 12500 * 0.25 + (taxableIncome - 33333.33) * 0.30;
        } else if (taxableIncome <= 166666.67) {
            withholdingTax = 20833.33 * 0.20 + 12500 * 0.25 + 33333.33 * 0.30 + (taxableIncome - 66666.67) * 0.32;
        } else if (taxableIncome <= 666666.67) {
            withholdingTax = 20833.33 * 0.20 + 12500 * 0.25 + 33333.33 * 0.30 + 100000.00 * 0.32 + (taxableIncome - 166666.67) * 0.35;
        } else {
            withholdingTax = 20833.33 * 0.20 + 12500 * 0.25 + 33333.33 * 0.30 + 100000.00 * 0.32 + 500000.00 * 0.35 + (taxableIncome - 666666.67) * 0.40;
        }

        netPay = calculator - (totalDeductions + withholdingTax);
    }

    private void ReadData() throws IOException, CsvValidationException {
        // Read Details.csv file to get the basic salary based on employee ID
        CSVReader reader = new CSVReader(new FileReader("src/resources/data/Details.csv"));
        String[] line;

        while ((line = reader.readNext()) != null) {
            if (line[0].equals(employeeID)) {
                org_riceSubsidy = Double.parseDouble(line[13].replace(",", ""));
                org_phoneAllowance = Double.parseDouble(line[14].replace(",", ""));
                org_clothingAllowance = Double.parseDouble(line[15].replace(",", ""));
                basicSalary = Double.parseDouble(line[16].replace(",", ""));
                semi_GrossPay = basicSalary/2;
                hourly_Rate = (basicSalary/21)/8;
                break;
            }
        }
        reader.close();
    }

    private void Allowances() throws IOException, CsvException {
        switch (mode) {
            case "Weekly" -> {
                riceSubsidy = org_riceSubsidy / 4;
                phoneAllowance = org_phoneAllowance / 4;
                clothingAllowance = org_clothingAllowance / 4;
            }
            case "Semi-Monthly" -> {
                riceSubsidy = org_riceSubsidy/2;
                phoneAllowance = org_phoneAllowance/2;
                clothingAllowance = org_clothingAllowance/2;
            }
            case "Monthly" -> {
                riceSubsidy = org_riceSubsidy;
                phoneAllowance = org_phoneAllowance;
                clothingAllowance = org_clothingAllowance;
            }
            case "Annually" -> {
                riceSubsidy = org_riceSubsidy * 12;
                phoneAllowance = org_phoneAllowance * 12;
                clothingAllowance = org_clothingAllowance * 12;
            }

            case "Choose Date" -> {
                TimeSheetSystem timeSheetSystem = new TimeSheetSystem(employeeID, start_Date, end_Date);
                double total_HoursWorked = timeSheetSystem.getTotalAdjustedHoursWorked();
                double daily_riceSubsidy = org_riceSubsidy/21;
                double daily_phoneAllowance = org_phoneAllowance/21;
                double daily_clothingAllowance = org_clothingAllowance/21;

                double daysWorked = total_HoursWorked / 8;

                riceSubsidy = daily_riceSubsidy * daysWorked;
                phoneAllowance = daily_phoneAllowance * daysWorked;
                clothingAllowance = daily_clothingAllowance * daysWorked;
            }
        }
        totalAllowances = riceSubsidy + phoneAllowance + clothingAllowance;
    }

    private void ModeSet() throws IOException, CsvException {
        switch (mode) {
            case "Weekly" -> calculator = basicSalary / 4;
            case "Semi-Monthly" -> calculator = semi_GrossPay;
            case "Monthly" -> calculator = basicSalary;
            case "Annually" -> calculator = basicSalary * 12;
            case "Choose Date" -> {
                TimeSheetSystem timeSheetSystem = new TimeSheetSystem(employeeID, start_Date, end_Date);
                double total_HoursWorked = timeSheetSystem.getTotalAdjustedHoursWorked();
                calculator = hourly_Rate * total_HoursWorked;
            }
            default -> throw new IllegalArgumentException("Invalid mode: " + mode);
        }
        if (calculator <= 0) {
            System.out.println("Basic salary not found for the specified employee");
            return;
        }
    }
    private void Deductions(){
        // Compute SSS contribution
        double[] sssCompensationRange = {0, 3250, 3750, 4250, 4750, 5250, 5750, 6250, 6750, 7250, 7750, 8250, 8750, 9250, 9750, 10250, 10750, 11250, 11750, 12250, 12750, 13250, 13750, 14250, 14750, 15250, 15750, 16250, 16750, 17250, 17750, 18250, 18750, 19250, 19750, 20250, 20750, 21250, 21750, 22250, 22750, 23250, 23750, 24250, 24750};
        double[] sssContributionTable = {135.00, 157.50, 180.00, 202.50, 225.00, 247.50, 270.00, 292.50, 315.00, 337.50, 360.00, 382.50, 405.00, 427.50, 450.00, 472.50, 495.00, 517.50, 540.00, 562.50, 585.00, 607.50, 630.00, 652.50, 675.00, 697.50, 720.00, 742.50, 765.00, 787.50, 810.00, 832.50, 855.00, 877.50, 900.00, 922.50, 945.00, 967.50, 990.00, 1012.50, 1035.00, 1057.50, 1080.00, 1102.50, 1125.00};

        for (int i = 1; i < sssCompensationRange.length; i++) {
            if (calculator <= sssCompensationRange[i]) {
                sssContribution = sssContributionTable[i - 1];
                break;
            }
            if (i == sssCompensationRange.length - 1) {
                sssContribution = sssContributionTable[i];
            }
        }

        // Compute PhilHealth contribution
        philhealthContribution = calculator * 0.03 * 0.5;

        // Compute Pag-ibig contribution
        double pagibigContributionRateEmployee = 0.00;
        double pagibigContributionMax = 100.00;

        if (calculator >= 1000 && calculator <= 1500) {
            pagibigContributionRateEmployee = 0.01;
        } else if (calculator > 1500) {
            pagibigContributionRateEmployee = 0.02;
        }

        pagibigContribution = pagibigContributionRateEmployee * calculator;

        if (pagibigContribution > pagibigContributionMax) {
            pagibigContribution = pagibigContributionMax;
        }
    }

    //getter methods
    public String getSssContribution(){
        return String.valueOf(sssContribution);
    }

    public String getPhilHealthContribution(){
        return String.valueOf(philhealthContribution);
    }

    public String getPagIbigContrbution(){
        return String.valueOf(pagibigContribution);
    }

    public String getTotalDeductions(){
        return String.valueOf(totalDeductions);
    }

    public String getWithHoldingTax(){
        return String.valueOf(withholdingTax);
    }

    public String getTotalAllowances(){
        return String.valueOf(totalAllowances);
    }

    public String getGrossPay(){
        return String.valueOf(grossPay);
    }

    public String getNetPay(){
        return String.valueOf(netPay);
    }

    public String getCalculatorValue(){
        return String.valueOf(calculator);
    }

    //setter methods
    public void setEmployeeID(String employeeID) {
        this.employeeID = employeeID;
    }

    public void setMode(String selected_mode) {
        this.mode = selected_mode;
    }
    public void setDates(String start_Date, String end_Date){
        this.start_Date = start_Date;
        this.end_Date = end_Date;
    }

    //main method
    public static void main(String[] args) throws IOException, CsvException, ParseException {
       NetPayCalculator netPayCalculator = new NetPayCalculator();
       netPayCalculator.setEmployeeID("10009");
       netPayCalculator.setMode("Choose Date");
       netPayCalculator.setDates("01/09/2022", "09/09/2022");
       netPayCalculator.Calculate();
        System.out.println("SSS Contribution: " + netPayCalculator.getSssContribution());
        System.out.println("PhilHealth Contribution: " + netPayCalculator.getPhilHealthContribution());
        System.out.println("Pag-IBIG Contribution: " + netPayCalculator.getPagIbigContrbution());
        System.out.println("Total Allowances: " + netPayCalculator.getTotalAllowances());
        System.out.println("Withholding Tax: " + netPayCalculator.getWithHoldingTax());
        System.out.println("Net Pay: " + netPayCalculator.getNetPay());
    }
}



