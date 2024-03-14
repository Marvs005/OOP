package main.java.frontend;

import main.java.backend.EmployeeProfile;
import main.java.backend.NetPayCalculator;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class ManageSalary {
    public JPanel contentPanel;
    private JPanel s_contentPanel;
    private JLabel employeeDetailsLabel;
    private JPanel cntnr_EmployeeDetails;
    private JLabel employeeNoLabel;
    private JLabel employeeNameLabel;
    private JLabel positionLabel;
    private JLabel statusLabel;
    private JTextField txt_EmployeeNo;
    private JTextField txt_Name;
    private JTextField txt_Position;
    private JTextField txt_Status;
    private JLabel basicSalaryLabel;
    private JTextField txt_BasicSalary;
    private JLabel hourlyRateLabel;
    private JTextField txt_HrlyRate;
    private JTable tbl_SalaryDetails;
    private JPanel cntnr_PyrllExpenses;
    private JTextArea txtArea_PaySlip;
    private JComboBox<String> picker;
    private JButton netWagesButton;
    private JButton paySlipButton;
    private JLabel salaryDetailsLabel;
    private JLabel paySlipLabel;
    private JPanel cntnr_payDate;
    private JPanel cntnr_Benefits;
    private JLabel riceSubsidyLabel;
    private JLabel phoneAllowanceLabel;
    private JTextField txt_Rice;
    private JTextField txt_PhoneAllowance;
    private JTextField txt_Clothing;
    private JLabel clothingAllowanceLabel;
    private JPanel cntnr_Deductions;
    private JLabel SSSLabel;
    private JLabel philHealthLabel;
    private JTextField txt_SSS;
    private JTextField txt_PhlHlth;
    private JLabel pagIbigLabel;
    private JTextField txt_Pgbg;
    private JTextField txt_ttlBenefits;
    private JTextField txt_WthTax;
    private JTextField txt_GrossPay;
    private JTextField txt_NetPay;
    private JLabel withholdingTaxLabel;
    private JLabel grossPayLabel;
    private JLabel netPayLabel;
    private JTextField txt_ttlDeductions;
    private JLabel deductionsLabel;
    private JPanel searchPanel;
    private JButton filterSearchButton;
    private JTextField searchField;
    private JDateChooser payDate_Start;
    private JDateChooser payDate_End;
    private DefaultTableModel tableModel;
    private String dataPath = "src/resources/data/Details.csv";

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

    public ManageSalary(){
        initComponents();
        tableView();
        actions();
        createUIComponents();
        setDataTableColumnWidth(tbl_SalaryDetails);
    }

    private void showDetails(String employeeNumber) throws IOException, CsvException {
        // Create an Employee Profile and NetPay Calculator object with the employee ID, selected start and end date
        EmployeeProfile employeeProfile = new EmployeeProfile(employeeNumber);
        NetPayCalculator netPayCalculator = new NetPayCalculator();
        netPayCalculator.setEmployeeID(employeeNumber);

        // Update employee info
        txt_EmployeeNo.setText(employeeNumber);
        txt_Name.setText(employeeProfile.getFirstName() + " " + employeeProfile.getLastName());
        // Allowances
        txt_Clothing.setText(employeeProfile.getClothingAllowance());
        txt_PhoneAllowance.setText(employeeProfile.getPhoneAllowance());
        txt_Rice.setText(employeeProfile.getRiceSubsidy());
        // Employee status
        txt_Status.setText(employeeProfile.getStatus());
        txt_Position.setText(employeeProfile.getPosition());
        // Salary info
        txt_BasicSalary.setText(employeeProfile.getBasicSalary());
        txt_HrlyRate.setText(employeeProfile.getHourlyRate());
    }

    private void tableView() {
        tableModel = new DefaultTableModel();
        // Read the CSV file and retrieve the data
        List<String[]> csvData = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new FileReader(dataPath))) {
            csvData = reader.readAll();
        } catch (IOException | CsvException e) {
            e.printStackTrace();
        }

        // Get column headers from the first row of the CSV file
        String[] columnHeaders = csvData.get(0);
        int[] columnIndices = new int[]{0, 12, 13, 14, 15, 16, 17, 18};

        // Add the column names to the table model
        tableModel.addColumn("Employee Name");
        for (int columnIndex : columnIndices) {
            tableModel.addColumn(columnHeaders[columnIndex]);
        }

        // Add the data to the table model
        for (int i = 1; i < csvData.size(); i++) {
            String[] data = csvData.get(i);
            Object[] rowData = new Object[columnIndices.length + 1];

            // Combine the first and second columns as "Employee Name"
            rowData[0] = data[1] + " " + data[2];

            for (int j = 0; j < columnIndices.length; j++) {
                rowData[j + 1] = data[columnIndices[j]];
            }
            tableModel.addRow(rowData);
        }
        tbl_SalaryDetails.setModel(tableModel);
    }



    private void setDataTableColumnWidth(JTable dataTable) {
        // Calculate and set preferred column width
        TableColumnModel columnModel = dataTable.getColumnModel();
        for (int column = 0; column < dataTable.getColumnCount(); column++) {
            TableColumn tableColumn = columnModel.getColumn(column);
            int preferredWidth = 0;

            // Get the width of the header
            TableCellRenderer headerRenderer = tableColumn.getHeaderRenderer();
            if (headerRenderer == null) {
                headerRenderer = dataTable.getTableHeader().getDefaultRenderer();
            }
            Component headerComponent = headerRenderer.getTableCellRendererComponent(
                    dataTable, tableColumn.getHeaderValue(), false, false, 0, column
            );
            int headerWidth = headerComponent.getPreferredSize().width;
            preferredWidth = Math.max(preferredWidth, headerWidth);

            // Get the width of the cells
            for (int row = 0; row < dataTable.getRowCount(); row++) {
                TableCellRenderer cellRenderer = dataTable.getCellRenderer(row, column);
                Component cellComponent = dataTable.prepareRenderer(cellRenderer, row, column);
                int cellWidth = cellComponent.getPreferredSize().width;
                preferredWidth = Math.max(preferredWidth, cellWidth);
            }
            // Set the preferred width for the column
            tableColumn.setPreferredWidth(preferredWidth + 5);
        }
    }

    private void netWagesCalculator(String employeeNumber, String selected_mode) throws IOException, CsvException {
        // Get the selected month and year from the Date Chooser
        Date s_date = payDate_Start.getDate();
        Date e_date = payDate_End.getDate();

        if (s_date == null || e_date == null) {
            JOptionPane.showMessageDialog(null, "Start date and end date are required.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Format the date using the SimpleDateFormat
        String start_date = dateFormat.format(s_date);
        String end_date = dateFormat.format(e_date);

        NetPayCalculator netPayCalculator = new NetPayCalculator();
        netPayCalculator.setEmployeeID(employeeNumber);
        netPayCalculator.setMode(selected_mode);
        netPayCalculator.setDates(start_date, end_date);
        netPayCalculator.Calculate();
        // Allowances
        txt_ttlBenefits.setText(netPayCalculator.getTotalAllowances());
        // Deductions
        txt_SSS.setText(netPayCalculator.getSssContribution());
        txt_Pgbg.setText(netPayCalculator.getPagIbigContrbution());
        txt_PhlHlth.setText(netPayCalculator.getPhilHealthContribution());
        txt_ttlDeductions.setText(netPayCalculator.getTotalDeductions());
        txt_WthTax.setText(netPayCalculator.getWithHoldingTax());
        // Gross pay
        txt_GrossPay.setText(netPayCalculator.getGrossPay());
        // Net pay
        txt_NetPay.setText(netPayCalculator.getNetPay());
    }

    // Method to generate payslip and update the text area
    private void generatePayslip(String selected_mode) {
        // Get the current date
        Date currentDate = new Date();
        String formattedDate = dateFormat.format(currentDate);

        // Create the payslip content
        StringBuilder payslipContent = new StringBuilder();
        payslipContent.append("MotorPH").append("\n");
        payslipContent.append("Payslip generated on: ").append(formattedDate).append("\n");

        if (Objects.equals(selected_mode, "Choose Date")){
            // Get the selected month and year from the Date Chooser
            Date s_date = payDate_Start.getDate();
            Date e_date = payDate_End.getDate();

            // Format the date using the SimpleDateFormat
            String start_date = dateFormat.format(s_date);
            String end_date = dateFormat.format(e_date);

            payslipContent.append("Date Range: ").append(start_date).append(" ").append(end_date).append("\n\n");
        } else {
            payslipContent.append("Date Range: ").append(selected_mode).append("\n\n");
        }

        // Append employee information from the text area.
        payslipContent.append("Employee Number: ").append(txt_EmployeeNo.getText()).append("\n");
        payslipContent.append("Employee Name: ").append(txt_Name.getText()).append("\n");
        payslipContent.append("Status: ").append(txt_Status.getText()).append("\n");
        payslipContent.append("Position: ").append(txt_Position.getText()).append("\n");
        payslipContent.append("Basic Salary: ").append(txt_BasicSalary.getText()).append("\n");
        payslipContent.append("Hourly Rate: ").append(txt_HrlyRate.getText()).append("\n\n");


        // Append Allowances information from the text fields
        payslipContent.append("Allowances\n");
        payslipContent.append("Clothing Allowance: ").append(txt_Clothing.getText()).append("\n");
        payslipContent.append("Phone Allowance: ").append(txt_PhoneAllowance.getText()).append("\n");
        payslipContent.append("Rice Subsidy: ").append(txt_Rice.getText()).append("\n");
        payslipContent.append("Total Benefits: ").append(txt_ttlBenefits.getText()).append("\n\n");

        // Append deductions from the text fields
        payslipContent.append("Deductions\n");
        payslipContent.append("SSS: ").append(txt_SSS.getText()).append("\n");
        payslipContent.append("PhilHealth: ").append(txt_PhlHlth.getText()).append("\n");
        payslipContent.append("Pag-Ibig: ").append(txt_Pgbg.getText()).append("\n");
        payslipContent.append("Total Deductions: ").append(txt_ttlDeductions.getText()).append("\n\n");

        // Append withholding tax gross pay and net pay
        payslipContent.append("Withholding Tax: ").append(txt_WthTax.getText()).append("\n");
        payslipContent.append("Gross Pay: ").append(txt_GrossPay.getText()).append("\n");
        payslipContent.append("Net Pay: ").append(txt_NetPay.getText()).append("\n");

        // Update the text area with the payslip content
        txtArea_PaySlip.setText(payslipContent.toString());
    }
        private void actions(){
        searchField.addActionListener(e -> {
            String employeeNumber = searchField.getText();
            try {
                showDetails(employeeNumber);
                tableView();
                setDataTableColumnWidth(tbl_SalaryDetails);
            } catch (IOException | CsvException ex) {
                throw new RuntimeException(ex);
            }
        });

        picker.addActionListener(e -> {
            if (picker.getSelectedItem() == "Choose Date"){
                cntnr_payDate.setVisible(true);
            }else{
                cntnr_payDate.setVisible(false);
            }
        });

        netWagesButton.addActionListener(e -> {
            String employeeNumber = searchField.getText();
            String selectedItem = (String) picker.getSelectedItem();

            // Check if there is an employee Number input
            if (employeeNumber.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Employee Number is required.", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            } else {
                try {
                    netWagesCalculator(employeeNumber, selectedItem);
                } catch (IOException | CsvException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        paySlipButton.addActionListener(e -> {
            String employeeNumber = searchField.getText();
            String selectedItem = (String) picker.getSelectedItem();

            // Check if there is an employee Number input
            if (employeeNumber.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Employee Number is required.", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            } else {
                try {
                    generatePayslip(selectedItem);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }

    private void createUIComponents(){
        payDate_Start = new JDateChooser();
        payDate_Start.setPreferredSize(new Dimension(126, 20));
        payDate_Start.setDateFormatString("MM/dd/yyyy");
        payDate_Start.setDate(new Date()); // Set the initial date

        payDate_End = new JDateChooser();
        payDate_End.setPreferredSize(new Dimension(126, 20));
        payDate_End.setDateFormatString("MM/dd/yyyy");
        payDate_End.setDate(new Date()); // Set the initial date

        cntnr_payDate.add(payDate_Start);
        cntnr_payDate.add(payDate_End);
        cntnr_payDate.setVisible(false);
    }


    public static void main(String[] args) {
        JFrame frame = new JFrame("ManageSalary");
        frame.setContentPane(new ManageSalary().contentPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    private void initComponents() {
        contentPanel = new JPanel();
        s_contentPanel = new JPanel();
        employeeDetailsLabel = new JLabel();
        cntnr_EmployeeDetails = new JPanel();
        employeeNoLabel = new JLabel();
        employeeNameLabel = new JLabel();
        positionLabel = new JLabel();
        statusLabel = new JLabel();
        txt_EmployeeNo = new JTextField();
        txt_Name = new JTextField();
        txt_Position = new JTextField();
        txt_Status = new JTextField();
        basicSalaryLabel = new JLabel();
        txt_BasicSalary = new JTextField();
        hourlyRateLabel = new JLabel();
        txt_HrlyRate = new JTextField();
        var scrollPane1 = new JScrollPane();
        tbl_SalaryDetails = new JTable();
        cntnr_PyrllExpenses = new JPanel();
        var scrollPane2 = new JScrollPane();
        txtArea_PaySlip = new JTextArea();
        var panel1 = new JPanel();
        picker = new JComboBox<>();
        netWagesButton = new JButton();
        paySlipButton = new JButton();
        salaryDetailsLabel = new JLabel();
        paySlipLabel = new JLabel();
        cntnr_payDate = new JPanel();
        var label1 = new JLabel();
        cntnr_Benefits = new JPanel();
        riceSubsidyLabel = new JLabel();
        phoneAllowanceLabel = new JLabel();
        txt_Rice = new JTextField();
        txt_PhoneAllowance = new JTextField();
        txt_Clothing = new JTextField();
        clothingAllowanceLabel = new JLabel();
        cntnr_Deductions = new JPanel();
        SSSLabel = new JLabel();
        philHealthLabel = new JLabel();
        txt_SSS = new JTextField();
        txt_PhlHlth = new JTextField();
        pagIbigLabel = new JLabel();
        txt_Pgbg = new JTextField();
        var panel2 = new JPanel();
        txt_ttlBenefits = new JTextField();
        txt_WthTax = new JTextField();
        txt_GrossPay = new JTextField();
        txt_NetPay = new JTextField();
        var label2 = new JLabel();
        withholdingTaxLabel = new JLabel();
        grossPayLabel = new JLabel();
        netPayLabel = new JLabel();
        var label3 = new JLabel();
        txt_ttlDeductions = new JTextField();
        var label4 = new JLabel();
        deductionsLabel = new JLabel();
        searchPanel = new JPanel();
        filterSearchButton = new JButton();
        var panel3 = new JPanel();
        searchField = new JTextField();

        //======== contentPanel ========
        {
            contentPanel.setMinimumSize(new Dimension(1000, 745));
            contentPanel.setPreferredSize(new Dimension(1000, 745));
            contentPanel.setLayout(new BorderLayout());

            //======== s_contentPanel ========
            {
                s_contentPanel.setBackground(new Color(0xb2d8d8));
                s_contentPanel.setMinimumSize(new Dimension(0, 0));
                s_contentPanel.setPreferredSize(new Dimension(0, 0));
                s_contentPanel.setBorder(new TitledBorder(new BevelBorder(BevelBorder.LOWERED), ""));
                s_contentPanel.setLayout(new GridBagLayout());

                //---- employeeDetailsLabel ----
                employeeDetailsLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
                employeeDetailsLabel.setText("Employee Details");
                s_contentPanel.add(employeeDetailsLabel, new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0,
                    GridBagConstraints.WEST, GridBagConstraints.NONE,
                    new Insets(5, 5, 5, 5), 0, 0));

                //======== cntnr_EmployeeDetails ========
                {
                    cntnr_EmployeeDetails.setBackground(new Color(0xced3cc));
                    cntnr_EmployeeDetails.setMinimumSize(new Dimension(350, 160));
                    cntnr_EmployeeDetails.setPreferredSize(new Dimension(350, 160));
                    cntnr_EmployeeDetails.setBorder(new TitledBorder(new BevelBorder(BevelBorder.LOWERED), ""));
                    cntnr_EmployeeDetails.setLayout(new GridBagLayout());

                    //---- employeeNoLabel ----
                    employeeNoLabel.setText("Employee No.:");
                    cntnr_EmployeeDetails.add(employeeNoLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                        GridBagConstraints.WEST, GridBagConstraints.NONE,
                        new Insets(2, 5, 2, 5), 0, 0));

                    //---- employeeNameLabel ----
                    employeeNameLabel.setText("Employee Name:");
                    cntnr_EmployeeDetails.add(employeeNameLabel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                        GridBagConstraints.WEST, GridBagConstraints.NONE,
                        new Insets(2, 5, 2, 5), 0, 0));

                    //---- positionLabel ----
                    positionLabel.setText("Position:");
                    cntnr_EmployeeDetails.add(positionLabel, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
                        GridBagConstraints.WEST, GridBagConstraints.NONE,
                        new Insets(2, 5, 2, 5), 0, 0));

                    //---- statusLabel ----
                    statusLabel.setText("Status:");
                    cntnr_EmployeeDetails.add(statusLabel, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0,
                        GridBagConstraints.WEST, GridBagConstraints.NONE,
                        new Insets(2, 5, 2, 5), 0, 0));

                    //---- txt_EmployeeNo ----
                    txt_EmployeeNo.setColumns(15);
                    cntnr_EmployeeDetails.add(txt_EmployeeNo, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                        new Insets(2, 5, 2, 5), 0, 0));

                    //---- txt_Name ----
                    txt_Name.setColumns(15);
                    cntnr_EmployeeDetails.add(txt_Name, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                        new Insets(2, 5, 2, 5), 0, 0));

                    //---- txt_Position ----
                    txt_Position.setColumns(15);
                    cntnr_EmployeeDetails.add(txt_Position, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                        new Insets(2, 5, 2, 5), 0, 0));

                    //---- txt_Status ----
                    txt_Status.setColumns(15);
                    cntnr_EmployeeDetails.add(txt_Status, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                        new Insets(2, 5, 2, 5), 0, 0));

                    //---- basicSalaryLabel ----
                    basicSalaryLabel.setText("Basic Salary");
                    cntnr_EmployeeDetails.add(basicSalaryLabel, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0,
                        GridBagConstraints.WEST, GridBagConstraints.NONE,
                        new Insets(2, 5, 2, 5), 0, 0));

                    //---- txt_BasicSalary ----
                    txt_BasicSalary.setColumns(15);
                    cntnr_EmployeeDetails.add(txt_BasicSalary, new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                        new Insets(2, 5, 2, 5), 0, 0));

                    //---- hourlyRateLabel ----
                    hourlyRateLabel.setText("Hourly Rate:");
                    cntnr_EmployeeDetails.add(hourlyRateLabel, new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0,
                        GridBagConstraints.WEST, GridBagConstraints.NONE,
                        new Insets(2, 5, 2, 5), 0, 0));

                    //---- txt_HrlyRate ----
                    txt_HrlyRate.setColumns(15);
                    cntnr_EmployeeDetails.add(txt_HrlyRate, new GridBagConstraints(1, 5, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                        new Insets(2, 5, 2, 5), 0, 0));
                }
                s_contentPanel.add(cntnr_EmployeeDetails, new GridBagConstraints(1, 5, 2, 2, 0.0, 0.0,
                    GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
                    new Insets(5, 5, 5, 5), 0, 0));

                //======== scrollPane1 ========
                {
                    scrollPane1.setAlignmentY(1.0F);
                    scrollPane1.setMinimumSize(new Dimension(350, 275));
                    scrollPane1.setPreferredSize(new Dimension(350, 275));
                    scrollPane1.setBorder(new TitledBorder(new BevelBorder(BevelBorder.RAISED), ""));

                    //---- tbl_SalaryDetails ----
                    tbl_SalaryDetails.setAutoCreateColumnsFromModel(true);
                    tbl_SalaryDetails.setAutoCreateRowSorter(true);
                    tbl_SalaryDetails.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
                    tbl_SalaryDetails.setCellSelectionEnabled(true);
                    tbl_SalaryDetails.setColumnSelectionAllowed(true);
                    tbl_SalaryDetails.setDoubleBuffered(false);
                    tbl_SalaryDetails.setDragEnabled(true);
                    tbl_SalaryDetails.setEnabled(false);
                    tbl_SalaryDetails.setFont(new Font("Fira Code", tbl_SalaryDetails.getFont().getStyle(), 14));
                    tbl_SalaryDetails.setRequestFocusEnabled(true);
                    tbl_SalaryDetails.setRowMargin(1);
                    tbl_SalaryDetails.setShowVerticalLines(true);
                    tbl_SalaryDetails.setUpdateSelectionOnSort(true);
                    tbl_SalaryDetails.putClientProperty("JTable.autoStartsEdit", true);
                    scrollPane1.setViewportView(tbl_SalaryDetails);
                }
                s_contentPanel.add(scrollPane1, new GridBagConstraints(1, 1, 2, 3, 0.0, 0.0,
                    GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
                    new Insets(5, 5, 5, 15), 0, 0));

                //======== cntnr_PyrllExpenses ========
                {
                    cntnr_PyrllExpenses.setBackground(new Color(0xced3cc));
                    cntnr_PyrllExpenses.setMinimumSize(new Dimension(350, 275));
                    cntnr_PyrllExpenses.setPreferredSize(new Dimension(350, 275));
                    cntnr_PyrllExpenses.setBorder(new TitledBorder(new BevelBorder(BevelBorder.LOWERED), ""));
                    cntnr_PyrllExpenses.setLayout(new GridBagLayout());

                    //======== scrollPane2 ========
                    {
                        scrollPane2.setViewportView(txtArea_PaySlip);
                    }
                    cntnr_PyrllExpenses.add(scrollPane2, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(5, 5, 5, 5), 0, 0));
                }
                s_contentPanel.add(cntnr_PyrllExpenses, new GridBagConstraints(4, 1, 1, 3, 0.0, 0.0,
                    GridBagConstraints.NORTH, GridBagConstraints.NONE,
                    new Insets(5, 15, 5, 5), 0, 0));

                //======== panel1 ========
                {
                    panel1.setMinimumSize(new Dimension(125, 155));
                    panel1.setOpaque(false);
                    panel1.setPreferredSize(new Dimension(125, 155));
                    panel1.setLayout(new GridBagLayout());

                    //---- picker ----
                    picker.setModel(new DefaultComboBoxModel<>(new String[] {
                        "Weekly",
                        "Semi-Monthly",
                        "Monthly",
                        "Annualy",
                        "Choose Date"
                    }));
                    panel1.add(picker, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                        new Insets(5, 0, 5, 0), 0, 0));

                    //---- netWagesButton ----
                    netWagesButton.setFocusable(false);
                    netWagesButton.setText("Net Wages");
                    panel1.add(netWagesButton, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                        new Insets(5, 0, 5, 0), 0, 0));

                    //---- paySlipButton ----
                    paySlipButton.setFocusable(false);
                    paySlipButton.setText("Pay Slip");
                    panel1.add(paySlipButton, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                        new Insets(5, 0, 5, 0), 0, 0));
                }
                s_contentPanel.add(panel1, new GridBagConstraints(3, 2, 1, 1, 0.0, 0.0,
                    GridBagConstraints.NORTH, GridBagConstraints.NONE,
                    new Insets(0, 5, 5, 5), 0, 0));

                //---- salaryDetailsLabel ----
                salaryDetailsLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
                salaryDetailsLabel.setText("Salary Details");
                s_contentPanel.add(salaryDetailsLabel, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.WEST, GridBagConstraints.NONE,
                    new Insets(15, 5, 5, 5), 0, 0));

                //---- paySlipLabel ----
                paySlipLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
                paySlipLabel.setText("Pay Slip");
                s_contentPanel.add(paySlipLabel, new GridBagConstraints(4, 0, 2, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.NONE,
                    new Insets(15, 5, 5, 5), 0, 0));

                //======== cntnr_payDate ========
                {
                    cntnr_payDate.setMinimumSize(new Dimension(10, 70));
                    cntnr_payDate.setPreferredSize(new Dimension(-1, 70));
                    cntnr_payDate.setBorder(new TitledBorder(new BevelBorder(BevelBorder.LOWERED), "Date Range", TitledBorder.CENTER, TitledBorder.ABOVE_TOP));
                    cntnr_payDate.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 2));
                }
                s_contentPanel.add(cntnr_payDate, new GridBagConstraints(3, 3, 1, 1, 0.0, 0.0,
                    GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                    new Insets(5, 0, 0, 0), 0, 0));

                //---- label1 ----
                label1.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
                label1.setText("Benefits");
                s_contentPanel.add(label1, new GridBagConstraints(1, 7, 1, 1, 0.0, 0.0,
                    GridBagConstraints.WEST, GridBagConstraints.NONE,
                    new Insets(5, 5, 5, 5), 0, 0));

                //======== cntnr_Benefits ========
                {
                    cntnr_Benefits.setBackground(new Color(0xced3cc));
                    cntnr_Benefits.setMinimumSize(new Dimension(350, 90));
                    cntnr_Benefits.setPreferredSize(new Dimension(350, 90));
                    cntnr_Benefits.setBorder(new TitledBorder(new BevelBorder(BevelBorder.LOWERED), ""));
                    cntnr_Benefits.setLayout(new GridBagLayout());

                    //---- riceSubsidyLabel ----
                    riceSubsidyLabel.setText("Rice Subsidy:");
                    cntnr_Benefits.add(riceSubsidyLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                        GridBagConstraints.WEST, GridBagConstraints.NONE,
                        new Insets(2, 5, 2, 5), 0, 0));

                    //---- phoneAllowanceLabel ----
                    phoneAllowanceLabel.setText("Phone Allowance:");
                    cntnr_Benefits.add(phoneAllowanceLabel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                        GridBagConstraints.WEST, GridBagConstraints.NONE,
                        new Insets(2, 5, 2, 5), 0, 0));

                    //---- txt_Rice ----
                    txt_Rice.setColumns(15);
                    txt_Rice.setText("");
                    cntnr_Benefits.add(txt_Rice, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                        new Insets(2, 5, 2, 5), 0, 0));

                    //---- txt_PhoneAllowance ----
                    txt_PhoneAllowance.setColumns(15);
                    cntnr_Benefits.add(txt_PhoneAllowance, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                        new Insets(2, 5, 2, 5), 0, 0));

                    //---- txt_Clothing ----
                    txt_Clothing.setColumns(15);
                    cntnr_Benefits.add(txt_Clothing, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                        new Insets(2, 5, 2, 5), 0, 0));

                    //---- clothingAllowanceLabel ----
                    clothingAllowanceLabel.setText("Clothing Allowance:");
                    cntnr_Benefits.add(clothingAllowanceLabel, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
                        GridBagConstraints.WEST, GridBagConstraints.NONE,
                        new Insets(2, 5, 2, 5), 0, 0));
                }
                s_contentPanel.add(cntnr_Benefits, new GridBagConstraints(1, 8, 2, 1, 0.0, 0.0,
                    GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
                    new Insets(5, 5, 5, 5), 0, 0));

                //======== cntnr_Deductions ========
                {
                    cntnr_Deductions.setBackground(new Color(0xced3cc));
                    cntnr_Deductions.setMinimumSize(new Dimension(350, 90));
                    cntnr_Deductions.setPreferredSize(new Dimension(350, 90));
                    cntnr_Deductions.setBorder(new TitledBorder(new BevelBorder(BevelBorder.LOWERED), ""));
                    cntnr_Deductions.setLayout(new GridBagLayout());

                    //---- SSSLabel ----
                    SSSLabel.setText("SSS:");
                    cntnr_Deductions.add(SSSLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                        GridBagConstraints.WEST, GridBagConstraints.NONE,
                        new Insets(2, 5, 2, 5), 0, 0));

                    //---- philHealthLabel ----
                    philHealthLabel.setText("PhilHealth:");
                    cntnr_Deductions.add(philHealthLabel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                        GridBagConstraints.WEST, GridBagConstraints.NONE,
                        new Insets(2, 5, 2, 5), 0, 0));

                    //---- txt_SSS ----
                    txt_SSS.setColumns(15);
                    cntnr_Deductions.add(txt_SSS, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                        new Insets(2, 5, 2, 5), 0, 0));

                    //---- txt_PhlHlth ----
                    txt_PhlHlth.setColumns(15);
                    cntnr_Deductions.add(txt_PhlHlth, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                        new Insets(2, 5, 2, 5), 0, 0));

                    //---- pagIbigLabel ----
                    pagIbigLabel.setText("Pag-ibig:");
                    cntnr_Deductions.add(pagIbigLabel, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
                        GridBagConstraints.WEST, GridBagConstraints.NONE,
                        new Insets(2, 5, 2, 5), 0, 0));

                    //---- txt_Pgbg ----
                    txt_Pgbg.setColumns(15);
                    cntnr_Deductions.add(txt_Pgbg, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                        new Insets(2, 5, 2, 5), 0, 0));
                }
                s_contentPanel.add(cntnr_Deductions, new GridBagConstraints(4, 8, 1, 1, 0.0, 0.0,
                    GridBagConstraints.WEST, GridBagConstraints.NONE,
                    new Insets(5, 15, 5, 5), 0, 0));

                //======== panel2 ========
                {
                    panel2.setBackground(new Color(0xced3cc));
                    panel2.setMinimumSize(new Dimension(350, 160));
                    panel2.setOpaque(true);
                    panel2.setPreferredSize(new Dimension(350, 160));
                    panel2.setBorder(new TitledBorder(new BevelBorder(BevelBorder.LOWERED), ""));
                    panel2.setLayout(new GridBagLayout());

                    //---- txt_ttlBenefits ----
                    txt_ttlBenefits.setColumns(15);
                    panel2.add(txt_ttlBenefits, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.NONE,
                        new Insets(2, 5, 2, 5), 0, 0));

                    //---- txt_WthTax ----
                    txt_WthTax.setColumns(15);
                    panel2.add(txt_WthTax, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                        new Insets(2, 5, 2, 5), 0, 0));

                    //---- txt_GrossPay ----
                    txt_GrossPay.setColumns(15);
                    panel2.add(txt_GrossPay, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                        new Insets(2, 5, 2, 5), 0, 0));

                    //---- txt_NetPay ----
                    txt_NetPay.setColumns(15);
                    panel2.add(txt_NetPay, new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                        new Insets(2, 5, 2, 5), 0, 0));

                    //---- label2 ----
                    label2.setText("Total Benefits:");
                    panel2.add(label2, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                        GridBagConstraints.WEST, GridBagConstraints.NONE,
                        new Insets(2, 5, 2, 5), 0, 0));

                    //---- withholdingTaxLabel ----
                    withholdingTaxLabel.setText("Withholding Tax:");
                    panel2.add(withholdingTaxLabel, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
                        GridBagConstraints.WEST, GridBagConstraints.NONE,
                        new Insets(2, 5, 2, 5), 0, 0));

                    //---- grossPayLabel ----
                    grossPayLabel.setText("Gross Pay:");
                    panel2.add(grossPayLabel, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0,
                        GridBagConstraints.WEST, GridBagConstraints.NONE,
                        new Insets(2, 5, 2, 5), 0, 0));

                    //---- netPayLabel ----
                    netPayLabel.setText("Net Pay:");
                    panel2.add(netPayLabel, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0,
                        GridBagConstraints.WEST, GridBagConstraints.NONE,
                        new Insets(2, 5, 2, 5), 0, 0));

                    //---- label3 ----
                    label3.setText("Total Deductions:");
                    panel2.add(label3, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                        GridBagConstraints.WEST, GridBagConstraints.NONE,
                        new Insets(2, 5, 2, 5), 0, 0));

                    //---- txt_ttlDeductions ----
                    txt_ttlDeductions.setColumns(15);
                    panel2.add(txt_ttlDeductions, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.NONE,
                        new Insets(2, 5, 2, 5), 0, 0));
                }
                s_contentPanel.add(panel2, new GridBagConstraints(4, 5, 1, 2, 0.0, 0.0,
                    GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
                    new Insets(5, 15, 5, 5), 0, 0));

                //---- label4 ----
                label4.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
                label4.setText("Calculations");
                s_contentPanel.add(label4, new GridBagConstraints(4, 4, 1, 1, 0.0, 0.0,
                    GridBagConstraints.WEST, GridBagConstraints.NONE,
                    new Insets(5, 15, 5, 5), 0, 0));

                //---- deductionsLabel ----
                deductionsLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
                deductionsLabel.setText("Deductions");
                s_contentPanel.add(deductionsLabel, new GridBagConstraints(4, 7, 1, 1, 0.0, 0.0,
                    GridBagConstraints.WEST, GridBagConstraints.NONE,
                    new Insets(5, 15, 5, 5), 0, 0));
            }
            contentPanel.add(s_contentPanel, BorderLayout.CENTER);

            //======== searchPanel ========
            {
                searchPanel.setBackground(new Color(0xb8d9b6));
                searchPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

                //---- filterSearchButton ----
                filterSearchButton.setBackground(new Color(0xb8d9b6));
                filterSearchButton.setBorderPainted(false);
                filterSearchButton.setContentAreaFilled(false);
                filterSearchButton.setFocusPainted(false);
                filterSearchButton.setFocusable(true);
                filterSearchButton.setForeground(Color.black);
                filterSearchButton.setHorizontalAlignment(SwingConstants.CENTER);
                filterSearchButton.setHorizontalTextPosition(SwingConstants.CENTER);
                filterSearchButton.setIcon(new ImageIcon(new ImageIcon("src/resources/images/icons/icons8-search-24.png").getImage()));
                filterSearchButton.setIconTextGap(4);
                filterSearchButton.setOpaque(true);
                filterSearchButton.setRequestFocusEnabled(false);
                filterSearchButton.setRolloverEnabled(false);
                filterSearchButton.setText("Employee #");
                filterSearchButton.setVerticalTextPosition(SwingConstants.BOTTOM);
                searchPanel.add(filterSearchButton);

                //======== panel3 ========
                {
                    panel3.setBackground(new Color(0xb8d9b6));
                    panel3.setLayout(new BorderLayout());

                    //---- searchField ----
                    searchField.setColumns(25);
                    searchField.setEditable(true);
                    searchField.setMargin(new Insets(2, 6, 2, 6));
                    searchField.setMinimumSize(new Dimension(49, 25));
                    searchField.setPreferredSize(new Dimension(281, 25));
                    searchField.setText("");
                    searchField.setToolTipText("");
                    panel3.add(searchField, BorderLayout.CENTER);
                }
                searchPanel.add(panel3);
            }
            contentPanel.add(searchPanel, BorderLayout.NORTH);
        }
    }
}
