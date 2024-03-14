package main.java.frontend;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;
import com.opencsv.exceptions.CsvValidationException;
import com.toedter.calendar.JDateChooser;
import main.java.backend.LeaveCalculator;
import main.java.backend.LeaveSystem;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LeaveApplication {
    private DefaultTableModel tableModel, currentModel;
    private final String recordPath = "src/resources/data/Leave.csv";
    private final String historyPath = "src/resources/data/LeaveHistory.csv";
    private JDateChooser dateChooser;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

    private JPanel mainPanel;
    public JPanel contentPanel;
    private JPanel s_contentPanel;
    private JLabel employeeDetailsLabel;
    private JLabel usedLeavesLabel;
    private JTextField txt_usedSick;
    private JTextField txt_usedEmergency;
    private JTextField txt_usedTTL;
    private JLabel sickLeavesLabel;
    private JLabel emergencyLeaveUsedLabel;
    private JLabel totalLeavesUsedLabel;
    private JLabel vacationLeaveUsedLabel;
    private JTextField txt_usedVacation;
    private JPanel cntnr_EmployeeDetails;
    private JLabel employeeNoLabel;
    private JLabel firstNameLabel;
    private JTextField txt_EmployeeNo;
    private JTextField txt_FirstName;
    private JTextField txt_LastName;
    private JScrollPane pane1;
    private JTable tbl_Details;
    private JLabel leaveDetailsLabel;
    private JLabel remainingLeavesLabel;
    private JTextField txt_remainSick;
    private JTextField txt_remainEmergency;
    private JTextField txt_remainTTL;
    private JTextField txt_remainVacation;
    private JScrollPane pane2;
    private JTable tbl_History;
    private JLabel leaveHistoryLabel;
    private JPanel cntnr_payDate;
    private JComboBox<String> picker;
    private JButton applyForLeaveButton;
    private JPanel searchPanel;
    private JButton filterSearchButton;
    private JTextField searchField;
    private JPanel cntnr_Used;
    private JPanel cntnr_Remain;
    private JLabel totalLeavesRemainingLabel;
    private JLabel sickLeaveRemainingLabel;
    private JLabel vacationLeaveRemainingLabel;
    private JLabel emergencyLeaveRemainingLabel;

    private String check_ForLeaveUse, check_ForLeaveRemaining, check_ForTotalUsed, check_ForTotalRemaining;

    public LeaveApplication(){
        initComponents();
        tableViewDetails();
        actions();
        createUIComponents();
        setDataTableColumnWidth(tbl_Details);
        setDataTableColumnWidth(tbl_History);
    }

    //-----------------Method to show employee leave data
    private void showDetails(String employeeNumber) throws IOException, CsvException {
        // Create a Leave System object with the employee ID
        LeaveSystem leaveSystem = new LeaveSystem(employeeNumber);

        // Update employee info
        txt_EmployeeNo.setText(employeeNumber);
        txt_FirstName.setText(leaveSystem.getFirstName());
        txt_LastName.setText(leaveSystem.getLastName());

        //Update employee leaves data
        txt_usedSick.setText(String.valueOf(leaveSystem.getUsedSick()));
        txt_usedVacation.setText(String.valueOf(leaveSystem.getUsedVacation()));
        txt_usedEmergency.setText(String.valueOf(leaveSystem.getUsedEmergency()));
        txt_remainSick.setText(String.valueOf(leaveSystem.getRemainingSick()));
        txt_remainVacation.setText(String.valueOf(leaveSystem.getRemainingVacation()));
        txt_remainEmergency.setText(String.valueOf(leaveSystem.getRemainingEmergency()));
        txt_usedTTL.setText(String.valueOf(leaveSystem.getUsedTotal()));
        txt_remainTTL.setText(String.valueOf(leaveSystem.getRemainingTotal()));
    }

    //-----------------Method to view all employee data
    private void tableViewDetails() {
        tableModel = new DefaultTableModel();
        // Read the CSV file and retrieve the data
        List<String[]> csvData = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new FileReader(recordPath))) {
            csvData = reader.readAll();
        } catch (IOException | CsvException e) {
            e.printStackTrace();
        }

        // Get column headers from the first row of the CSV file
        String[] columnHeaders = csvData.get(0);
        int[] columnIndices = new int[]{3, 4, 5, 6, 7, 8, 9, 10};

        // Add the column names to the table model
        tableModel.addColumn(columnHeaders[0]); // Add the first column as it is
        tableModel.addColumn("Employee Name"); // Add the "Employee Name" column as the second column
        for (int columnIndex : columnIndices) {
            tableModel.addColumn(columnHeaders[columnIndex]);
        }

        // Add the data to the table model
        for (int i = 1; i < csvData.size(); i++) {
            String[] data = csvData.get(i);
            Object[] rowData = new Object[columnIndices.length + 2];

            rowData[0] = data[0]; // Set the value of the first column
            rowData[1] = data[1] + " " + data[2]; // Set the "Employee Name" value in the second column

            for (int j = 0; j < columnIndices.length; j++) {
                rowData[j + 2] = data[columnIndices[j]];
            }
            tableModel.addRow(rowData);
        }
        tbl_Details.setModel(tableModel);
    }


    //-----------------Method to retrieve all employee records on leave history
    private void tableViewHistory() {
        tableModel = new DefaultTableModel();
        // Read the CSV file and retrieve the data
        List<String[]> csvData = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new FileReader(historyPath))) {
            csvData = reader.readAll();
        } catch (IOException | CsvException e) {
            e.printStackTrace();
        }

        // Assuming the first row of the CSV file contains column headers
        String[] columnHeaders = csvData.get(0);
        tableModel.setColumnIdentifiers(columnHeaders);
        // Add the data rows to the table model
        for (int i = 1; i < csvData.size(); i++) {
            tableModel.addRow(csvData.get(i));
        }

        // Set the table model to the JTable component and update the currentModel
        tbl_History.setModel(tableModel);
        currentModel = tableModel;
    }


    //-----------------Method to filter the table data based on employee number
    private void setFilteredData(String employeeNumber) {
        // Create a new filtered table model
        DefaultTableModel filteredModel = new DefaultTableModel();

        // Get the column headers from the original table model
        Object[] columnHeaders = new Object[tableModel.getColumnCount()];
        for (int column = 0; column < tableModel.getColumnCount(); column++) {
            columnHeaders[column] = tableModel.getColumnName(column);
        }
        filteredModel.setColumnIdentifiers(columnHeaders);

        // Filter the data and add the matching rows to the filtered model
        for (int row = 0; row < tableModel.getRowCount(); row++) {
            if (tableModel.getValueAt(row, 0).equals(employeeNumber)) {
                Object[] rowData = new Object[tableModel.getColumnCount()];
                for (int column = 0; column < tableModel.getColumnCount(); column++) {
                    rowData[column] = tableModel.getValueAt(row, column);
                }
                filteredModel.addRow(rowData);
            }
        }

        // Set the table model to the JTable component
        tbl_History.setModel(filteredModel);
        currentModel = filteredModel;
    }

    //-----------------Method to set tables column width
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

    //-----------------Method to apply for leave
    private void applyLeave(String employeeNumber, String reason){
        try {
            // Open the CSV file and create a reader
            CSVReader reader = new CSVReader(new FileReader(recordPath));

            // Read the header row and get the index of the "Employee #" column
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
                reader.close();
                return;
            }

            // Find the row for the given user ID and extract the data
            String[] row;
            boolean foundUser = false;
            boolean canApply = false;
            while ((row = reader.readNext()) != null) {
                if (row[empNumIndex].equals(employeeNumber)) {
                    foundUser = true;

                    switch (reason) {
                        case "Sick" -> {
                            check_ForLeaveUse = row[3];
                            check_ForLeaveRemaining = row[6];
                            check_ForTotalUsed = row[9];
                            check_ForTotalRemaining = row[10];
                        }
                        case "Vacation" -> {
                            check_ForLeaveUse = row[4];
                            check_ForLeaveRemaining = row[7];
                            check_ForTotalUsed = row[9];
                            check_ForTotalRemaining = row[10];
                        }
                        case "Emergency" -> {
                            check_ForLeaveUse = row[5];
                            check_ForLeaveRemaining = row[8];
                            check_ForTotalUsed = row[9];
                            check_ForTotalRemaining = row[10];
                        }
                    }
                    if (!check_ForLeaveRemaining.equals("0")) {
                        canApply = true;

                        LeaveCalculator leaveCalculator = new LeaveCalculator(employeeNumber, reason);

                        // Display a confirmation dialog
                        int choice = JOptionPane.showConfirmDialog(null, "Confirm " + reason + " Leave Application for Employee: "
                                + employeeNumber + " at date: " + dateFormat.format(dateChooser.getDate()), "Confirm Leave Application", JOptionPane.YES_NO_OPTION);
                        if (choice == JOptionPane.YES_OPTION) {
                            //calculate and update leave record
                            leaveCalculator.calculateLeave(Integer.parseInt(check_ForLeaveUse), Integer.parseInt(check_ForLeaveRemaining), Integer.parseInt(check_ForTotalUsed), Integer.parseInt(check_ForTotalRemaining));
                            addLeaveHistory(employeeNumber, reason);

                            showDetails(employeeNumber);
                            tableViewHistory();
                            setFilteredData(employeeNumber);
                            setDataTableColumnWidth(tbl_History);
                        }
                        break;
                    }
                    break;
                }
            }
            reader.close();

            if (!foundUser) {
                System.out.println("Error: Could not find user with ID " + employeeNumber + " in CSV file");
                JOptionPane.showMessageDialog(null, "Error: Could not find user with ID " + employeeNumber, "Error", JOptionPane.ERROR_MESSAGE);
            }

            if (!canApply) {
                System.out.println("Error: Could not apply for leave as remaining leave is fully used");
                JOptionPane.showMessageDialog(null, reason + " Leave has been fully consumed for Employee: " + employeeNumber, "Application Rejected", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (IOException | CsvValidationException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (CsvException e) {
            throw new RuntimeException(e);
        }
    }

    //-----------------Method to add leave history
    private void addLeaveHistory(String employeeNumber, String reason) {
        // Validate required fields
        if (txt_EmployeeNo.getText().isEmpty() || txt_LastName.getText().isEmpty() || txt_FirstName.getText().isEmpty() ||
                dateChooser.getDate() == null || picker.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(null, "Please fill in all the required fields.", "Missing Information", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Read the existing CSV file data
        List<String[]> csvData;
        try (CSVReader reader = new CSVReader(new FileReader(historyPath))) {
            csvData = reader.readAll();
        } catch (IOException | CsvException e) {
            e.printStackTrace();
            return;
        }

        // Create a new row for the employee data
        String[] newRow = new String[csvData.get(0).length];
        newRow[0] = txt_EmployeeNo.getText();
        newRow[1] = txt_LastName.getText();
        newRow[2] = txt_FirstName.getText();
        newRow[3] = dateFormat.format(dateChooser.getDate());
        newRow[4] = reason;

        // Add the new row to the CSV data
        csvData.add(newRow);

        try (CSVWriter writer = new CSVWriter(new FileWriter(historyPath))) {
            writer.writeAll(csvData);
            JOptionPane.showMessageDialog(null, "Employee " + employeeNumber + " has been added.", "Application Successful", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "An error occurred while adding the employee.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void actions() {
        searchField.addActionListener(e -> {
            String employeeNumber = searchField.getText();
            try {
                showDetails(employeeNumber);
                tableViewHistory();
                setFilteredData(employeeNumber);
                setDataTableColumnWidth(tbl_History);
            } catch (IOException | CsvException ex) {
                throw new RuntimeException(ex);
            }
        });

        filterSearchButton.addActionListener(e -> {
            String employeeNumber = searchField.getText();
            try {
                showDetails(employeeNumber);
                tableViewHistory();
                setFilteredData(employeeNumber);
                setDataTableColumnWidth(tbl_History);
            } catch (IOException | CsvException ex) {
                throw new RuntimeException(ex);
            }
        });

        applyForLeaveButton.addActionListener(e -> {
            String employeeNumber = txt_EmployeeNo.getText();
            String selectedItem = (String) picker.getSelectedItem();

            applyLeave(employeeNumber, selectedItem);
        });
    }

    private void createUIComponents(){
        dateChooser = new JDateChooser();
        dateChooser.setPreferredSize(new Dimension(126, 20));
        dateChooser.setDateFormatString("MM/dd/yyyy");
        dateChooser.setDate(new Date()); // Set the initial date

        cntnr_payDate.add(dateChooser);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("LeaveApplication");
        frame.setContentPane(new LeaveApplication().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    private void initComponents() {
        mainPanel = new JPanel();
        contentPanel = new JPanel();
        s_contentPanel = new JPanel();
        employeeDetailsLabel = new JLabel();
        usedLeavesLabel = new JLabel();
        var cntnr_Used = new JPanel();
        txt_usedSick = new JTextField();
        txt_usedEmergency = new JTextField();
        txt_usedTTL = new JTextField();
        sickLeavesLabel = new JLabel();
        emergencyLeaveUsedLabel = new JLabel();
        totalLeavesUsedLabel = new JLabel();
        vacationLeaveUsedLabel = new JLabel();
        txt_usedVacation = new JTextField();
        cntnr_EmployeeDetails = new JPanel();
        employeeNoLabel = new JLabel();
        firstNameLabel = new JLabel();
        txt_EmployeeNo = new JTextField();
        txt_FirstName = new JTextField();
        var lbl_LastName = new JLabel();
        txt_LastName = new JTextField();
        pane1 = new JScrollPane();
        tbl_Details = new JTable();
        leaveDetailsLabel = new JLabel();
        remainingLeavesLabel = new JLabel();
        var cntnr_Remain = new JPanel();
        txt_remainSick = new JTextField();
        txt_remainEmergency = new JTextField();
        txt_remainTTL = new JTextField();
        var sickLeaveRemainingLabel = new JLabel();
        var emergencyLeaveRemainingLabel = new JLabel();
        var totalLeaveRemainingLabel = new JLabel();
        var vacationLeaveRemainingLabel = new JLabel();
        txt_remainVacation = new JTextField();
        pane2 = new JScrollPane();
        tbl_History = new JTable();
        leaveHistoryLabel = new JLabel();
        var panel3 = new JPanel();
        cntnr_payDate = new JPanel();
        picker = new JComboBox<>();
        applyForLeaveButton = new JButton();
        searchPanel = new JPanel();
        filterSearchButton = new JButton();
        var panel4 = new JPanel();
        searchField = new JTextField();

        //======== mainPanel ========
        {
            mainPanel.setMinimumSize(new Dimension(1000, 745));
            mainPanel.setPreferredSize(new Dimension(1000, 745));
            mainPanel.setLayout(new BorderLayout());

            //======== contentPanel ========
            {
                contentPanel.setMinimumSize(new Dimension(1000, 745));
                contentPanel.setPreferredSize(new Dimension(1000, 745));
                contentPanel.setLayout(new BorderLayout());

                //======== s_contentPanel ========
                {
                    s_contentPanel.setBackground(new Color(0xb2d8d8));
                    s_contentPanel.setMinimumSize(new Dimension(375, 0));
                    s_contentPanel.setPreferredSize(new Dimension(375, 0));
                    s_contentPanel.setBorder(new TitledBorder(new BevelBorder(BevelBorder.LOWERED), ""));
                    s_contentPanel.setLayout(new GridBagLayout());

                    //---- employeeDetailsLabel ----
                    employeeDetailsLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
                    employeeDetailsLabel.setText("Employee Details");
                    s_contentPanel.add(employeeDetailsLabel, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
                        GridBagConstraints.WEST, GridBagConstraints.NONE,
                        new Insets(5, 5, 5, 5), 0, 0));

                    //---- usedLeavesLabel ----
                    usedLeavesLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
                    usedLeavesLabel.setText("Used Leaves");
                    s_contentPanel.add(usedLeavesLabel, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0,
                        GridBagConstraints.WEST, GridBagConstraints.NONE,
                        new Insets(5, 5, 5, 5), 0, 0));

                    //======== cntnr_Used ========
                    {
                        cntnr_Used.setBackground(new Color(0xced3cc));
                        cntnr_Used.setMinimumSize(new Dimension(415, 140));
                        cntnr_Used.setPreferredSize(new Dimension(415, 140));
                        cntnr_Used.setOpaque(true);
                        cntnr_Used.setBorder(new TitledBorder(new BevelBorder(BevelBorder.LOWERED), ""));
                        cntnr_Used.setLayout(new GridBagLayout());

                        //---- txt_usedSick ----
                        txt_usedSick.setColumns(15);
                        txt_usedSick.setEditable(false);
                        cntnr_Used.add(txt_usedSick, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0,
                            GridBagConstraints.CENTER, GridBagConstraints.NONE,
                            new Insets(2, 5, 2, 5), 0, 0));

                        //---- txt_usedEmergency ----
                        txt_usedEmergency.setColumns(15);
                        txt_usedEmergency.setEditable(false);
                        cntnr_Used.add(txt_usedEmergency, new GridBagConstraints(1, 2, 1, 1, 1.0, 0.0,
                            GridBagConstraints.CENTER, GridBagConstraints.NONE,
                            new Insets(2, 5, 2, 5), 0, 0));

                        //---- txt_usedTTL ----
                        txt_usedTTL.setColumns(15);
                        txt_usedTTL.setEditable(false);
                        cntnr_Used.add(txt_usedTTL, new GridBagConstraints(1, 3, 1, 1, 1.0, 0.0,
                            GridBagConstraints.CENTER, GridBagConstraints.NONE,
                            new Insets(2, 5, 2, 5), 0, 0));

                        //---- sickLeavesLabel ----
                        sickLeavesLabel.setText("Sick Leave (Used):");
                        cntnr_Used.add(sickLeavesLabel, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0,
                            GridBagConstraints.WEST, GridBagConstraints.NONE,
                            new Insets(2, 10, 2, 5), 0, 0));

                        //---- emergencyLeaveUsedLabel ----
                        emergencyLeaveUsedLabel.setText("Emergency Leave (Used):");
                        cntnr_Used.add(emergencyLeaveUsedLabel, new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0,
                            GridBagConstraints.WEST, GridBagConstraints.NONE,
                            new Insets(2, 10, 2, 5), 0, 0));

                        //---- totalLeavesUsedLabel ----
                        totalLeavesUsedLabel.setText("Total Leaves Used:");
                        cntnr_Used.add(totalLeavesUsedLabel, new GridBagConstraints(0, 3, 1, 1, 1.0, 0.0,
                            GridBagConstraints.WEST, GridBagConstraints.NONE,
                            new Insets(2, 10, 2, 5), 0, 0));

                        //---- vacationLeaveUsedLabel ----
                        vacationLeaveUsedLabel.setText("Vacation Leave (Used):");
                        cntnr_Used.add(vacationLeaveUsedLabel, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0,
                            GridBagConstraints.WEST, GridBagConstraints.NONE,
                            new Insets(2, 10, 2, 5), 0, 0));

                        //---- txt_usedVacation ----
                        txt_usedVacation.setColumns(15);
                        txt_usedVacation.setEditable(false);
                        cntnr_Used.add(txt_usedVacation, new GridBagConstraints(1, 1, 1, 1, 1.0, 0.0,
                            GridBagConstraints.CENTER, GridBagConstraints.NONE,
                            new Insets(2, 5, 2, 5), 0, 0));
                    }
                    s_contentPanel.add(cntnr_Used, new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0,
                        GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
                        new Insets(5, 5, 5, 5), 0, 0));

                    //======== cntnr_EmployeeDetails ========
                    {
                        cntnr_EmployeeDetails.setBackground(new Color(0xced3cc));
                        cntnr_EmployeeDetails.setMinimumSize(new Dimension(415, 85));
                        cntnr_EmployeeDetails.setPreferredSize(new Dimension(415, 85));
                        cntnr_EmployeeDetails.setBorder(new TitledBorder(new BevelBorder(BevelBorder.LOWERED), ""));
                        cntnr_EmployeeDetails.setLayout(new GridBagLayout());

                        //---- employeeNoLabel ----
                        employeeNoLabel.setText("Employee No.:");
                        cntnr_EmployeeDetails.add(employeeNoLabel, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0,
                            GridBagConstraints.WEST, GridBagConstraints.NONE,
                            new Insets(2, 10, 2, 5), 0, 0));

                        //---- firstNameLabel ----
                        firstNameLabel.setText("First Name:");
                        cntnr_EmployeeDetails.add(firstNameLabel, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0,
                            GridBagConstraints.WEST, GridBagConstraints.NONE,
                            new Insets(2, 10, 2, 5), 0, 0));

                        //---- txt_EmployeeNo ----
                        txt_EmployeeNo.setColumns(15);
                        txt_EmployeeNo.setEditable(false);
                        cntnr_EmployeeDetails.add(txt_EmployeeNo, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0,
                            GridBagConstraints.CENTER, GridBagConstraints.NONE,
                            new Insets(2, 5, 2, 5), 0, 0));

                        //---- txt_FirstName ----
                        txt_FirstName.setColumns(15);
                        txt_FirstName.setEditable(false);
                        cntnr_EmployeeDetails.add(txt_FirstName, new GridBagConstraints(1, 1, 1, 1, 1.0, 0.0,
                            GridBagConstraints.CENTER, GridBagConstraints.NONE,
                            new Insets(2, 5, 2, 5), 0, 0));

                        //---- lbl_LastName ----
                        lbl_LastName.setText("Last Name:");
                        cntnr_EmployeeDetails.add(lbl_LastName, new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0,
                            GridBagConstraints.WEST, GridBagConstraints.NONE,
                            new Insets(2, 10, 2, 5), 0, 0));

                        //---- txt_LastName ----
                        txt_LastName.setColumns(15);
                        txt_LastName.setEditable(false);
                        cntnr_EmployeeDetails.add(txt_LastName, new GridBagConstraints(1, 2, 1, 1, 1.0, 0.0,
                            GridBagConstraints.CENTER, GridBagConstraints.NONE,
                            new Insets(2, 5, 2, 5), 0, 0));
                    }
                    s_contentPanel.add(cntnr_EmployeeDetails, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0,
                        GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
                        new Insets(5, 5, 5, 5), 0, 0));

                    //======== pane1 ========
                    {
                        pane1.setAlignmentY(1.0F);
                        pane1.setMinimumSize(new Dimension(350, 100));
                        pane1.setPreferredSize(new Dimension(350, 100));
                        pane1.setBorder(new TitledBorder(new BevelBorder(BevelBorder.RAISED), ""));

                        //---- tbl_Details ----
                        tbl_Details.setAutoCreateColumnsFromModel(true);
                        tbl_Details.setAutoCreateRowSorter(true);
                        tbl_Details.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
                        tbl_Details.setCellSelectionEnabled(true);
                        tbl_Details.setColumnSelectionAllowed(true);
                        tbl_Details.setDoubleBuffered(false);
                        tbl_Details.setDragEnabled(true);
                        tbl_Details.setEnabled(false);
                        tbl_Details.setRequestFocusEnabled(true);
                        tbl_Details.setRowMargin(1);
                        tbl_Details.setShowVerticalLines(true);
                        tbl_Details.setUpdateSelectionOnSort(true);
                        tbl_Details.putClientProperty("JTable.autoStartsEdit", true);
                        pane1.setViewportView(tbl_Details);
                    }
                    s_contentPanel.add(pane1, new GridBagConstraints(0, 1, 2, 1, 0.0, 0.0,
                        GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                        new Insets(5, 5, 5, 15), 0, 0));

                    //---- leaveDetailsLabel ----
                    leaveDetailsLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
                    leaveDetailsLabel.setText("Leave Details");
                    s_contentPanel.add(leaveDetailsLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                        GridBagConstraints.WEST, GridBagConstraints.NONE,
                        new Insets(15, 5, 5, 5), 0, 0));

                    //---- remainingLeavesLabel ----
                    remainingLeavesLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
                    remainingLeavesLabel.setText("Remaining Leaves");
                    s_contentPanel.add(remainingLeavesLabel, new GridBagConstraints(0, 6, 1, 1, 0.0, 0.0,
                        GridBagConstraints.WEST, GridBagConstraints.NONE,
                        new Insets(5, 5, 5, 5), 0, 0));

                    //======== cntnr_Remain ========
                    {
                        cntnr_Remain.setBackground(new Color(0xced3cc));
                        cntnr_Remain.setMinimumSize(new Dimension(415, 140));
                        cntnr_Remain.setPreferredSize(new Dimension(415, 140));
                        cntnr_Remain.setOpaque(true);
                        cntnr_Remain.setBorder(new TitledBorder(new BevelBorder(BevelBorder.LOWERED), ""));
                        cntnr_Remain.setLayout(new GridBagLayout());

                        //---- txt_remainSick ----
                        txt_remainSick.setColumns(15);
                        txt_remainSick.setEditable(false);
                        cntnr_Remain.add(txt_remainSick, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0,
                            GridBagConstraints.CENTER, GridBagConstraints.VERTICAL,
                            new Insets(2, 5, 2, 5), 0, 0));

                        //---- txt_remainEmergency ----
                        txt_remainEmergency.setColumns(15);
                        txt_remainEmergency.setEditable(false);
                        cntnr_Remain.add(txt_remainEmergency, new GridBagConstraints(1, 2, 1, 1, 1.0, 0.0,
                            GridBagConstraints.CENTER, GridBagConstraints.VERTICAL,
                            new Insets(2, 5, 2, 5), 0, 0));

                        //---- txt_remainTTL ----
                        txt_remainTTL.setColumns(15);
                        txt_remainTTL.setEditable(false);
                        cntnr_Remain.add(txt_remainTTL, new GridBagConstraints(1, 3, 1, 1, 1.0, 0.0,
                            GridBagConstraints.CENTER, GridBagConstraints.VERTICAL,
                            new Insets(2, 5, 2, 5), 0, 0));

                        //---- sickLeaveRemainingLabel ----
                        sickLeaveRemainingLabel.setText("Sick Leave (Remaining):");
                        cntnr_Remain.add(sickLeaveRemainingLabel, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0,
                            GridBagConstraints.WEST, GridBagConstraints.NONE,
                            new Insets(2, 10, 2, 5), 0, 0));

                        //---- emergencyLeaveRemainingLabel ----
                        emergencyLeaveRemainingLabel.setText("Emergency Leave (Remaining):");
                        cntnr_Remain.add(emergencyLeaveRemainingLabel, new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0,
                            GridBagConstraints.WEST, GridBagConstraints.NONE,
                            new Insets(2, 10, 2, 5), 0, 0));

                        //---- totalLeaveRemainingLabel ----
                        totalLeaveRemainingLabel.setText("Total Leaves Remaining:");
                        cntnr_Remain.add(totalLeaveRemainingLabel, new GridBagConstraints(0, 3, 1, 1, 1.0, 0.0,
                            GridBagConstraints.WEST, GridBagConstraints.NONE,
                            new Insets(2, 10, 2, 5), 0, 0));

                        //---- vacationLeaveRemainingLabel ----
                        vacationLeaveRemainingLabel.setText("Vacation Leave (Remaining):");
                        cntnr_Remain.add(vacationLeaveRemainingLabel, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0,
                            GridBagConstraints.WEST, GridBagConstraints.NONE,
                            new Insets(2, 10, 2, 5), 0, 0));

                        //---- txt_remainVacation ----
                        txt_remainVacation.setColumns(15);
                        txt_remainVacation.setEditable(false);
                        cntnr_Remain.add(txt_remainVacation, new GridBagConstraints(1, 1, 1, 1, 1.0, 0.0,
                            GridBagConstraints.CENTER, GridBagConstraints.VERTICAL,
                            new Insets(2, 5, 2, 5), 0, 0));
                    }
                    s_contentPanel.add(cntnr_Remain, new GridBagConstraints(0, 7, 1, 1, 0.0, 0.0,
                        GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
                        new Insets(5, 5, 5, 5), 0, 0));

                    //======== pane2 ========
                    {
                        pane2.setAlignmentY(1.0F);
                        pane2.setMinimumSize(new Dimension(350, 150));
                        pane2.setPreferredSize(new Dimension(350, 150));
                        pane2.setBorder(new TitledBorder(new BevelBorder(BevelBorder.RAISED), ""));

                        //---- tbl_History ----
                        tbl_History.setAutoCreateColumnsFromModel(true);
                        tbl_History.setAutoCreateRowSorter(true);
                        tbl_History.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
                        tbl_History.setCellSelectionEnabled(true);
                        tbl_History.setColumnSelectionAllowed(true);
                        tbl_History.setDoubleBuffered(false);
                        tbl_History.setDragEnabled(true);
                        tbl_History.setEnabled(false);
                        tbl_History.setRequestFocusEnabled(true);
                        tbl_History.setRowMargin(1);
                        tbl_History.setShowVerticalLines(true);
                        tbl_History.setUpdateSelectionOnSort(true);
                        tbl_History.putClientProperty("JTable.autoStartsEdit", true);
                        pane2.setViewportView(tbl_History);
                    }
                    s_contentPanel.add(pane2, new GridBagConstraints(1, 3, 1, 3, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(5, 5, 5, 15), 0, 0));

                    //---- leaveHistoryLabel ----
                    leaveHistoryLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
                    leaveHistoryLabel.setText("Leave History");
                    s_contentPanel.add(leaveHistoryLabel, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.NONE,
                        new Insets(15, 5, 5, 5), 0, 0));

                    //======== panel3 ========
                    {
                        panel3.setOpaque(false);
                        panel3.setLayout(new GridBagLayout());

                        //======== cntnr_payDate ========
                        {
                            cntnr_payDate.setBorder(new TitledBorder(new BevelBorder(BevelBorder.LOWERED), "Leave Date", TitledBorder.CENTER, TitledBorder.ABOVE_TOP));
                            cntnr_payDate.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 15));
                        }
                        panel3.add(cntnr_payDate, new GridBagConstraints(0, 0, 1, 2, 0.0, 0.0,
                            GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 5, 5), 0, 0));

                        //---- picker ----
                        picker.setModel(new DefaultComboBoxModel<>(new String[] {
                            "Sick",
                            "Vacation",
                            "Emergency"
                        }));
                        panel3.add(picker, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                            GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 5, 5), 0, 0));

                        //---- applyForLeaveButton ----
                        applyForLeaveButton.setFocusable(false);
                        applyForLeaveButton.setText("Apply for Leave");
                        panel3.add(applyForLeaveButton, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
                            GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 5, 5, 5), 0, 0));
                    }
                    s_contentPanel.add(panel3, new GridBagConstraints(1, 6, 1, 2, 0.0, 0.0,
                        GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                        new Insets(0, 0, 0, 0), 0, 0));
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

                    //======== panel4 ========
                    {
                        panel4.setLayout(new BorderLayout());

                        //---- searchField ----
                        searchField.setColumns(25);
                        searchField.setEditable(true);
                        searchField.setMargin(new Insets(2, 6, 2, 6));
                        searchField.setText("");
                        searchField.setToolTipText("");
                        panel4.add(searchField, BorderLayout.CENTER);
                    }
                    searchPanel.add(panel4);
                }
                contentPanel.add(searchPanel, BorderLayout.NORTH);
            }
            mainPanel.add(contentPanel, BorderLayout.CENTER);
        }
    }
}

