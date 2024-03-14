package main.java.frontend;

import main.java.backend.EmployeeProfile;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;
import com.opencsv.exceptions.CsvValidationException;
import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AttendanceRecords {
    private JDateChooser dateChooser;
    private DefaultTableModel tableModel, currentModel;
    private final String dataPath = "src/resources/data/Attendance.csv";
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

    private JPanel mainPanel;
    public JPanel contentPanel;
    private JPanel centerContents;
    private JPanel infoPanel;
    private JPanel RecordContainer;
    private JLabel RecordLabel;
    private JLabel dateLabel;
    private JLabel timeInLabel;
    private JLabel timeOutLabel;
    private JTextField txt_In;
    private JTextField txt_Out;
    private JPanel dateContainer;
    private JTable dataTable;
    private JPanel infoCOntainer;
    private JLabel employeeIDLabel;
    private JLabel firstNameLabel;
    private JLabel lastNameLabel;
    private JTextField empIdField;
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JLabel employeeInformationLabel;
    private JPanel buttonsPanel;
    private JButton clearButton;
    private JButton deleteButton;
    private JButton updateButton;
    private JButton addButton;
    private JPanel searchPanel;
    private JButton filterSearchButton;
    private JTextField searchField;
    private JButton updateTableButton;
    private JPanel searchButtonsPanel;
    private JButton viewAllButton;
    private JComboBox<String> tableMode;
    private JLabel attendanceSheetLabel;

    public AttendanceRecords() {
        initComponents();
        createUIComponents();
        actions();
        // create a blank table data
        viewAllData();
        setFilteredData(null);
        setDataTableColumnWidth();
    }

    //-----------------Method to view all employee data
    private void viewAllData() {
        tableModel = new DefaultTableModel();
        // Read the CSV file and retrieve the data
        java.util.List<String[]> csvData = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new FileReader(dataPath))) {
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
        dataTable.setModel(tableModel);
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
        dataTable.setModel(filteredModel);
        currentModel = filteredModel;
    }

    //-----------------Method to set column width to maximum content width
    private void setDataTableColumnWidth() {
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

    //-----------------Method to show contents in infoPanel
    private void showContent(String employeeNumber) {
        if (employeeNumber.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Employee ID is required.", "Invalid", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Create an Employee Profile object with the employee ID, selected start and end date

        EmployeeProfile employeeProfile;
        //profile
        employeeProfile = new EmployeeProfile(employeeNumber);
        //employee info
        empIdField.setText(employeeNumber);
        firstNameField.setText(employeeProfile.getFirstName());
        lastNameField.setText(employeeProfile.getLastName());
    }

    //-----------------Method to show record based on date
    private void showRecord(String employeeNumber, String date) throws IOException, CsvValidationException {
        if (employeeNumber.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Employee ID is required.", "Invalid", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Read Attendance.csv file to get the attendance record based on employee ID and date
        CSVReader reader = new CSVReader(new FileReader("src/resources/data/Attendance.csv"));
        String[] line;

        while ((line = reader.readNext()) != null) {
            if (line[0].equals(employeeNumber) && line[3].equals(date)) {
                txt_In.setText(line[4]);
                txt_Out.setText(line[5]);
                break;
            }
        }
        reader.close();
    }

    //-----------------Method to clear contents
    private void clearContent() {
        //employee info
        empIdField.setText("");
        firstNameField.setText("");
        lastNameField.setText("");
        txt_In.setText("");
        txt_Out.setText("");
    }

    //-----------------Method to Update Csv File based on input
    private void updateCSVFile(String employeeNumber, String date) {
        // Read the existing CSV file data
        java.util.List<String[]> csvData = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new FileReader(dataPath))) {
            csvData = reader.readAll();
        } catch (IOException | CsvException e) {
            e.printStackTrace();
            return;
        }


        // Find the row for the given employee ID and update the status
        boolean foundUser = false;
        boolean foundRecord = false;

        for (String[] row : csvData) {
            if (row[0].equals(employeeNumber) && row[3].equals(date)) {
                row[0] = empIdField.getText();
                row[1] = lastNameField.getText();
                row[2] = firstNameField.getText();
                row[3] = dateFormat.format(dateChooser.getDate());
                row[4] = txt_In.getText();
                row[5] = txt_Out.getText();
                foundUser = true;
                foundRecord = true;
                break;
            }
        }

        // Check if the user was found in the CSV file
        if (!foundUser) {
            JOptionPane.showMessageDialog(null, "Error: Could not find user with ID " + employeeNumber, "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Check if the record was found in the CSV file
        if (!foundRecord) {
            JOptionPane.showMessageDialog(null, "Error: Could not find record for user " + employeeNumber, "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Display a confirmation dialog
        int choice =
                JOptionPane.showConfirmDialog(null, "Confirm Updating employee attendance record", "Update " +
                        "Confirmation", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            // Write the updated data back to the CSV file
            try (CSVWriter writer = new CSVWriter(new FileWriter(dataPath))) {
                writer.writeAll(csvData);
                JOptionPane.showMessageDialog(null, "Attendance record for employee " + employeeNumber + " has been " +
                        "updated.", "Update Successful", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //-----------------Method to Add data to Csv File based on input
    private void addData(String employeeNumber, String date) {
        // Read the existing CSV file data
        java.util.List<String[]> csvData = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new FileReader(dataPath))) {
            csvData = reader.readAll();
        } catch (IOException | CsvException e) {
            e.printStackTrace();
            return;
        }

        // Check for duplicate employee number and date
        boolean isDuplicate = csvData.stream()
                .anyMatch(row -> row[0].equals(employeeNumber) && row[3].equals(date));

        if (isDuplicate) {
            JOptionPane.showMessageDialog(null, "Record for " + employeeNumber + " already exists at this date.",
                    "Duplicate Record Found", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Create a new row for the employee data
        String[] newRow = new String[csvData.get(0).length];
        newRow[0] = empIdField.getText();
        newRow[1] = lastNameField.getText();
        newRow[2] = firstNameField.getText();
        newRow[3] = dateFormat.format(dateChooser.getDate());
        newRow[4] = txt_In.getText();
        newRow[5] = txt_Out.getText();

        // Add the new row to the CSV data
        csvData.add(newRow);

        // Display a confirmation dialog
        int choice =
                JOptionPane.showConfirmDialog(null, "Confirm Adding employee attendance record", "Add Confirmation",
                        JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            // Write the updated data back to the CSV file
            try (CSVWriter writer = new CSVWriter(new FileWriter(dataPath))) {
                writer.writeAll(csvData);
                JOptionPane.showMessageDialog(null, "Employee record for " + employeeNumber + " has been added.",
                        "Inserting Successful", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //-----------------Method to delete data
    private void deleteData(String employeeNumber, String date) {
        // Read the existing CSV file data
        java.util.List<String[]> csvData = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new FileReader(dataPath))) {
            csvData = reader.readAll();
        } catch (IOException | CsvException e) {
            e.printStackTrace();
            return;
        }

        // Find the row for the given employee ID
        boolean foundUser = false;
        boolean foundDate = false;
        int rowIndex = -1;
        for (int i = 0; i < csvData.size(); i++) {
            String[] row = csvData.get(i);
            if (row[0].equals(employeeNumber) && row[3].equals(date)) {
                rowIndex = i;
                foundUser = true;
                foundDate = true;
                break;
            }
        }

        // Check if the user was found in the CSV file
        if (!foundUser) {
            JOptionPane.showMessageDialog(null, "Error: Could not find user with ID " + employeeNumber, "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Check if the record was found in the CSV file
        if (!foundDate) {
            JOptionPane.showMessageDialog(null, "Error: Could not find record for user " + employeeNumber, "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Display a confirmation dialog
        int choice = JOptionPane.showConfirmDialog(null, "Confirm Deleting attendance data", "Delete Confirmation", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            // Remove the row from the CSV data
            csvData.remove(rowIndex);

            // Write the updated data back to the CSV file
            try (CSVWriter writer = new CSVWriter(new FileWriter(dataPath))) {
                writer.writeAll(csvData);
                JOptionPane.showMessageDialog(null, "Employee record for " + employeeNumber + " has been removed.",
                        "Remove Successful", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //-----------------Method to allow editing the data in the Jtable
    private void editCSVFile(DefaultTableModel model) {
        try (CSVReader reader = new CSVReader(new FileReader(dataPath));
             CSVWriter writer = new CSVWriter(new FileWriter("src/resources/data/Attendance_temp.csv"))) {
            List<String[]> csvData = reader.readAll();

            int rowCount = model.getRowCount();
            int columnCount = model.getColumnCount();

            // Write the header row
            writer.writeNext(csvData.get(0));

            // Write the data rows
            for (int row = 1; row < csvData.size(); row++) {
                String[] rowData = csvData.get(row);
                String employeeNumber = rowData[0]; // Get the employee number from the current row

                // Check if the employee number exists in the model
                int modelRowIndex = getModelIndex(model, employeeNumber);
                if (modelRowIndex != -1) {
                    // Found a matching row in the model, update the data
                    for (int column = 0; column < columnCount; column++) {
                        Object value = model.getValueAt(modelRowIndex, column);
                        String cellData = value != null ? value.toString() : "";
                        rowData[column] = cellData;
                    }
                }
                writer.writeNext(rowData);
            }
        } catch (IOException | CsvException ex) {
            ex.printStackTrace();
        }

        // Replace the original CSV file with the updated file
        File originalFile = new File(dataPath);
        File tempFile = new File("src/resources/data/Attendance_temp.csv");
        if (tempFile.renameTo(originalFile)) {
            System.out.println("CSV file updated successfully.");
        } else {
            System.out.println("Failed to update CSV file.");
        }
    }

    //-----------------Method to get Model index for data table
    private int getModelIndex(DefaultTableModel model, String employeeNumber) {
        int rowCount = model.getRowCount();

        for (int row = 0; row < rowCount; row++) {
            String value = model.getValueAt(row, 0).toString();
            if (value.equals(employeeNumber)) {
                return row;
            }
        }
        return -1; // Employee number not found in the model
    }

    //-----------------Method for actions
    private void actions(){
        searchField.addActionListener(e -> {
            String employeeNumber = searchField.getText();
            viewAllData();
            setFilteredData(employeeNumber);
            setDataTableColumnWidth();
            showContent(employeeNumber);
        });

        filterSearchButton.addActionListener(e -> {
            String employeeNumber = searchField.getText();
            viewAllData();
            setFilteredData(employeeNumber);
            setDataTableColumnWidth();
            showContent(employeeNumber);
        });

        viewAllButton.addActionListener(e -> {
            viewAllData();
            setDataTableColumnWidth();
        });

        clearButton.addActionListener(e -> {
            clearContent();
        });

        updateButton.addActionListener(e -> {
            String employeeNumber = empIdField.getText();
            String formattedDate = dateFormat.format(dateChooser.getDate());
            updateCSVFile(employeeNumber, formattedDate);
        });

        addButton.addActionListener(e -> {
            String employeeNumber = empIdField.getText();
            String formattedDate = dateFormat.format(dateChooser.getDate());
            addData(employeeNumber, formattedDate);
        });

        deleteButton.addActionListener(e -> {
            String employeeNumber = empIdField.getText();
            String formattedDate = dateFormat.format(dateChooser.getDate());
            deleteData(employeeNumber, formattedDate);
        });

        dateChooser.getDateEditor().addPropertyChangeListener("date", evt -> {
            if ("date".equals(evt.getPropertyName())) {
                Date selectedDate = (Date) evt.getNewValue();

                // Format the selected date to String
                String formattedDate = dateFormat.format(selectedDate);
                String employeeNumber = empIdField.getText();

                try {
                    showRecord(employeeNumber, formattedDate);
                } catch (IOException | CsvValidationException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        tableMode.addActionListener(e -> { //allows to edit table
            if (tableMode.getSelectedItem() == "Edit Mode") {
                dataTable.setEnabled(true);
                updateTableButton.setVisible(true);
            } else {
                dataTable.setEnabled(false);
                updateTableButton.setVisible(false);
            }
        });

        updateTableButton.addActionListener(e -> {
            editCSVFile(currentModel);
        });

        dataTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int rowNo = dataTable.getSelectedRow();
                DefaultTableModel model = (DefaultTableModel) dataTable.getModel();

                if (rowNo != -1) {
                    //employee info
                    empIdField.setText((String) model.getValueAt(rowNo, 0));
                    lastNameField.setText((String) model.getValueAt(rowNo, 1));
                    firstNameField.setText((String) model.getValueAt(rowNo, 2));

                    String int_date = (String) model.getValueAt(rowNo, 3);
                    try {
                        // Parse the input date string to a Date object
                        Date date = dateFormat.parse(int_date);

                        // Set the formatted date in the JDateChooser
                        dateChooser.setDate(date);
                    } catch (ParseException ex) {
                        throw new RuntimeException(ex);
                    }

                    txt_In.setText((String) model.getValueAt(rowNo, 4));
                    txt_Out.setText((String) model.getValueAt(rowNo, 5));


                    // Highlight the selected cell
                    dataTable.setSelectionBackground(Color.YELLOW);
                    dataTable.setSelectionForeground(Color.BLACK);
                }
            }
        });
    }

    //-----------------Method for Ui components
    private void createUIComponents() {
        dateChooser = new JDateChooser();
        dateChooser.setPreferredSize(new Dimension(126, 20));
        dateChooser.setDate(new Date()); // Set the initial date
        dateContainer.add(dateChooser);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("AttendanceRecords");
        frame.setContentPane(new AttendanceRecords().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    private void initComponents() {
        mainPanel = new JPanel();
        contentPanel = new JPanel();
        centerContents = new JPanel();
        infoPanel = new JPanel();
        infoCOntainer = new JPanel();
        employeeIDLabel = new JLabel();
        firstNameLabel = new JLabel();
        lastNameLabel = new JLabel();
        attendanceSheetLabel = new JLabel();
        empIdField = new JTextField();
        firstNameField = new JTextField();
        lastNameField = new JTextField();
        employeeInformationLabel = new JLabel();
        RecordContainer = new JPanel();
        RecordLabel = new JLabel();
        dateLabel = new JLabel();
        timeInLabel = new JLabel();
        timeOutLabel = new JLabel();
        txt_In = new JTextField();
        txt_Out = new JTextField();
        dateContainer = new JPanel();
        buttonsPanel = new JPanel();
        clearButton = new JButton();
        deleteButton = new JButton();
        updateButton = new JButton();
        addButton = new JButton();
        searchPanel = new JPanel();
        filterSearchButton = new JButton();
        var panel1 = new JPanel();
        searchField = new JTextField();
        updateTableButton = new JButton();
        var separator1 = new JSeparator();
        searchButtonsPanel = new JPanel();
        viewAllButton = new JButton();
        tableMode = new JComboBox<>();
        var scrollPane1 = new JScrollPane();
        dataTable = new JTable();

        //======== mainPanel ========
        {
            mainPanel.setBackground(Color.white);
            mainPanel.setDoubleBuffered(false);
            mainPanel.setEnabled(false);
            mainPanel.setFocusable(false);
            mainPanel.setForeground(Color.white);
            mainPanel.setMaximumSize(new Dimension(-1, -1));
            mainPanel.setMinimumSize(new Dimension(1250, 800));
            mainPanel.setOpaque(false);
            mainPanel.setPreferredSize(new Dimension(1250, 800));
            mainPanel.setRequestFocusEnabled(false);
            mainPanel.setVerifyInputWhenFocusTarget(false);
            mainPanel.setVisible(true);
            mainPanel.setLayout(new BorderLayout());

            //======== contentPanel ========
            {
                contentPanel.setBackground(new Color(0xf1faee));
                contentPanel.setLayout(new BorderLayout());

                //======== centerContents ========
                {
                    centerContents.setLayout(new BorderLayout());

                    //======== infoPanel ========
                    {
                        infoPanel.setBorder(new TitledBorder(new BevelBorder(BevelBorder.LOWERED), ""));
                        infoPanel.setBackground(new Color(0xb8d9b6));
                        infoPanel.setFont(new Font("Fira Code", infoPanel.getFont().getStyle(), infoPanel.getFont().getSize()));
                        infoPanel.setLayout(new GridBagLayout());

                        //======== RecordContainer ========
                        {
                            RecordContainer.setBackground(new Color(0xb8d9b6));
                            RecordContainer.setLayout(new GridBagLayout());

                            //---- RecordLabel ----
                            RecordLabel.setFont(new Font("Fira Code", Font.BOLD, 16));
                            RecordLabel.setText("Records");
                            RecordContainer.add(RecordLabel, new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0,
                                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                                new Insets(5, 0, 10, 0), 0, 0));

                            //---- dateLabel ----
                            dateLabel.setText("Date:");
                            dateLabel.setFont(new Font("Fira Code", dateLabel.getFont().getStyle(), dateLabel.getFont().getSize()));
                            RecordContainer.add(dateLabel, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0,
                                GridBagConstraints.EAST, GridBagConstraints.VERTICAL,
                                new Insets(0, 5, 5, 5), 0, 0));

                            //---- timeInLabel ----
                            timeInLabel.setText("Time-In:");
                            timeInLabel.setFont(new Font("Fira Code", timeInLabel.getFont().getStyle(), timeInLabel.getFont().getSize()));
                            RecordContainer.add(timeInLabel, new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0,
                                GridBagConstraints.EAST, GridBagConstraints.VERTICAL,
                                new Insets(0, 5, 5, 5), 0, 0));

                            //---- timeOutLabel ----
                            timeOutLabel.setText("Time-Out:");
                            timeOutLabel.setFont(new Font("Fira Code", timeOutLabel.getFont().getStyle(), timeOutLabel.getFont().getSize()));
                            RecordContainer.add(timeOutLabel, new GridBagConstraints(0, 3, 1, 1, 1.0, 0.0,
                                GridBagConstraints.EAST, GridBagConstraints.VERTICAL,
                                new Insets(0, 5, 5, 5), 0, 0));

                            //---- txt_In ----
                            txt_In.setColumns(15);
                            txt_In.setFont(new Font("Fira Code", txt_In.getFont().getStyle(), txt_In.getFont().getSize()));
                            txt_In.setText("");
                            RecordContainer.add(txt_In, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0,
                                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                                new Insets(0, 5, 5, 5), 0, 0));

                            //---- txt_Out ----
                            txt_Out.setColumns(15);
                            txt_Out.setFont(new Font("Fira Code", txt_Out.getFont().getStyle(), txt_Out.getFont().getSize()));
                            txt_Out.setText("");

                            RecordContainer.add(txt_Out, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0,
                                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                                new Insets(0, 5, 5, 5), 0, 0));

                            //======== dateContainer ========
                            {
                                dateContainer.setBackground(new Color(0xf1faee));
                                dateContainer.setLayout(new BorderLayout());
                            }
                            RecordContainer.add(dateContainer, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
                                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                                new Insets(0, 5, 5, 5), 0, 0));
                        }
                        infoPanel.add(RecordContainer, new GridBagConstraints(1, 3, 1, 1, 1.0, 1.0,
                            GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
                            new Insets(0, 0, 0, 0), 0, 0));

                        //======== scrollPane1 ========
                        {
                            scrollPane1.setAutoscrolls(true);
                            scrollPane1.setBorder(new TitledBorder(new BevelBorder(BevelBorder.LOWERED), ""));

                            //---- dataTable ----
                            dataTable.setAutoCreateColumnsFromModel(true);
                            dataTable.setAutoCreateRowSorter(true);
                            dataTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
                            dataTable.setCellSelectionEnabled(true);
                            dataTable.setColumnSelectionAllowed(true);
                            dataTable.setDoubleBuffered(false);
                            dataTable.setDragEnabled(true);
                            dataTable.setEnabled(false);
                            dataTable.setRequestFocusEnabled(true);
                            dataTable.setRowMargin(1);
                            dataTable.setShowVerticalLines(true);
                            dataTable.setUpdateSelectionOnSort(true);
                            dataTable.putClientProperty("JTable.autoStartsEdit", true);
                            scrollPane1.setViewportView(dataTable);
                        }
                        infoPanel.add(scrollPane1, new GridBagConstraints(0, 2, 1, 2, 1.0, 1.0,
                            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                            new Insets(0, 10, 0, 5), 0, 0));

                        //======== infoCOntainer ========
                        {
                            infoCOntainer.setBackground(new Color(0xb8d9b6));
                            infoCOntainer.setLayout(new GridBagLayout());

                            //---- employeeIDLabel ----
                            employeeIDLabel.setFont(new Font("Fira Code", employeeIDLabel.getFont().getStyle(), employeeIDLabel.getFont().getSize()));
                            employeeIDLabel.setText("Employee ID:");
                            infoCOntainer.add(employeeIDLabel, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0,
                                    GridBagConstraints.EAST, GridBagConstraints.NONE,
                                    new Insets(0, 5, 5, 5), 0, 0));

                            //---- firstNameLabel ----
                            firstNameLabel.setFont(new Font("Fira Code", firstNameLabel.getFont().getStyle(), firstNameLabel.getFont().getSize()));
                            firstNameLabel.setText("First Name:");
                            infoCOntainer.add(firstNameLabel, new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0,
                                    GridBagConstraints.EAST, GridBagConstraints.NONE,
                                    new Insets(0, 5, 5, 5), 0, 0));

                            //---- lastNameLabel ----
                            lastNameLabel.setFont(new Font("Fira Code", lastNameLabel.getFont().getStyle(), lastNameLabel.getFont().getSize()));
                            lastNameLabel.setText("Last Name:");
                            infoCOntainer.add(lastNameLabel, new GridBagConstraints(0, 3, 1, 1, 1.0, 0.0,
                                    GridBagConstraints.EAST, GridBagConstraints.NONE,
                                    new Insets(0, 5, 5, 5), 0, 0));

                            //---- empIdField ----
                            empIdField.setColumns(15);
                            empIdField.setFont(new Font("Fira Code", empIdField.getFont().getStyle(), empIdField.getFont().getSize()));
                            empIdField.setText("");
                            infoCOntainer.add(empIdField, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
                                    GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                                    new Insets(0, 5, 5, 5), 0, 0));

                            //---- firstNameField ----
                            firstNameField.setColumns(15);
                            firstNameField.setFont(new Font("Fira Code", firstNameField.getFont().getStyle(), firstNameField.getFont().getSize()));
                            infoCOntainer.add(firstNameField, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0,
                                    GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                                    new Insets(0, 5, 5, 5), 0, 0));

                            //---- lastNameField ----
                            lastNameField.setColumns(15);
                            lastNameField.setFont(new Font("Fira Code", lastNameField.getFont().getStyle(), lastNameField.getFont().getSize()));
                            infoCOntainer.add(lastNameField, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0,
                                    GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                                    new Insets(0, 5, 5, 5), 0, 0));

                            //---- employeeInformationLabel ----
                            employeeInformationLabel.setFont(new Font("Fira Code", Font.BOLD, 16));
                            employeeInformationLabel.setText("Employee Information");
                            infoCOntainer.add(employeeInformationLabel, new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0,
                                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                                new Insets(5, 0, 10, 0), 0, 0));
                        }
                        infoPanel.add(infoCOntainer, new GridBagConstraints(1, 1, 1, 2, 1.0, 1.0,
                            GridBagConstraints.WEST, GridBagConstraints.NONE,
                            new Insets(0, 0, 0, 0), 0, 0));

                        //---- label1 ----
                        attendanceSheetLabel.setText("Attendance Sheet");
                        infoPanel.add(attendanceSheetLabel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                            GridBagConstraints.CENTER, GridBagConstraints.NONE,
                            new Insets(5, 0, 10, 0), 0, 0));
                    }
                    centerContents.add(infoPanel, BorderLayout.CENTER);

                    //======== buttonsPanel ========
                    {
                        buttonsPanel.setAlignmentX(0.5F);
                        buttonsPanel.setBackground(new Color(0xb8d9b6));
                        buttonsPanel.setPreferredSize(new Dimension(360, 40));
                        buttonsPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 5));

                        //---- clearButton ----
                        clearButton.setBackground(new Color(0xe63946));
                        clearButton.setContentAreaFilled(false);
                        clearButton.setFont(new Font("Fira Code", clearButton.getFont().getStyle(), clearButton.getFont().getSize()));
                        clearButton.setForeground(Color.white);
                        clearButton.setOpaque(true);
                        clearButton.setText("Clear");
                        buttonsPanel.add(clearButton);

                        //---- deleteButton ----
                        deleteButton.setBackground(new Color(0xe63946));
                        deleteButton.setContentAreaFilled(false);
                        deleteButton.setFont(new Font("Fira Code", deleteButton.getFont().getStyle(), deleteButton.getFont().getSize()));
                        deleteButton.setForeground(Color.white);
                        deleteButton.setOpaque(true);
                        deleteButton.setText("Delete");
                        buttonsPanel.add(deleteButton);

                        //---- updateButton ----
                        updateButton.setBackground(new Color(0x457b9d));
                        updateButton.setContentAreaFilled(false);
                        updateButton.setFont(new Font("Fira Code", updateButton.getFont().getStyle(), updateButton.getFont().getSize()));
                        updateButton.setForeground(Color.white);
                        updateButton.setOpaque(true);
                        updateButton.setText("Update");
                        buttonsPanel.add(updateButton);

                        //---- addButton ----
                        addButton.setBackground(new Color(0x457b9d));
                        addButton.setContentAreaFilled(false);
                        addButton.setFont(new Font("Fira Code", addButton.getFont().getStyle(), addButton.getFont().getSize()));
                        addButton.setForeground(Color.white);
                        addButton.setOpaque(true);
                        addButton.setText("Add");
                        buttonsPanel.add(addButton);
                    }
                    centerContents.add(buttonsPanel, BorderLayout.SOUTH);
                }
                contentPanel.add(centerContents, BorderLayout.CENTER);

                //======== searchPanel ========
                {
                    searchPanel.setBackground(new Color(0xb8d9b6));
                    searchPanel.setLayout(new BorderLayout());

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
                    searchPanel.add(filterSearchButton, BorderLayout.WEST);

                    //======== panel1 ========
                    {
                        panel1.setBackground(new Color(0xb8d9b6));
                        panel1.setLayout(new GridBagLayout());

                        //---- searchField ----
                        searchField.setColumns(25);
                        searchField.setEditable(true);
                        searchField.setMargin(new Insets(2, 6, 2, 6));
                        searchField.setMinimumSize(new Dimension(49, 25));
                        searchField.setPreferredSize(new Dimension(281, 25));
                        searchField.setText("");
                        searchField.setToolTipText("");
                        panel1.add(searchField, new GridBagConstraints(0, 0, 1, 1, 0.0, 1.0,
                                GridBagConstraints.WEST, GridBagConstraints.NONE,
                                new Insets(10, 0, 10, 0), 0, 0));

                        //---- updateTableButton ----
                        updateTableButton.setText("Update Table");
                        updateTableButton.setVisible(false);
                        panel1.add(updateTableButton, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                                GridBagConstraints.WEST, GridBagConstraints.NONE,
                                new Insets(0, 0, 0, 0), 0, 0));

                        //---- separator1 ----
                        separator1.setDoubleBuffered(true);
                        separator1.setEnabled(true);
                        separator1.setFocusable(true);
                        separator1.setForeground(new Color(0xf1faee));
                        separator1.setOpaque(true);
                        separator1.setRequestFocusEnabled(true);
                        separator1.setVerifyInputWhenFocusTarget(true);
                        panel1.add(separator1, new GridBagConstraints(2, 0, 1, 1, 1.0, 0.0,
                                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                                new Insets(0, 0, 0, 0), 0, 0));
                    }
                    searchPanel.add(panel1, BorderLayout.CENTER);

                    //======== searchButtonsPanel ========
                    {
                        searchButtonsPanel.setBackground(new Color(0xb8d9b6));
                        searchButtonsPanel.setLayout(new GridBagLayout());

                        //---- viewAllButton ----
                        viewAllButton.setFocusable(false);
                        viewAllButton.setText("View All");
                        searchButtonsPanel.add(viewAllButton, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
                                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                                new Insets(0, 0, 0, 0), 0, 0));

                        //---- tableMode ----
                        tableMode.setModel(new DefaultComboBoxModel<>(new String[] {
                                "View Mode",
                                "Edit Mode"
                        }));
                        searchButtonsPanel.add(tableMode, new GridBagConstraints(1, 0, 1, 1, 0.0, 1.0,
                                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                                new Insets(0, 0, 0, 0), 0, 0));
                    }
                    searchPanel.add(searchButtonsPanel, BorderLayout.EAST);
                }
                contentPanel.add(searchPanel, BorderLayout.NORTH);
            }
            mainPanel.add(contentPanel, BorderLayout.CENTER);
        }
    }
}