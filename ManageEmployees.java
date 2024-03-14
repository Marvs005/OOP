package main.java.frontend;

import main.java.backend.EmployeeProfile;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
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
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class ManageEmployees {
    private final String dataPath = "src/resources/data/Details.csv";
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("MMMMMMMMM dd, yyyy");
    public JPanel contentPanel;
    private JPanel mainPanel, southContents, infoPanel, infoCOntainer, govIdContainer;
    private JLabel employeeIDLabel, firstNameLabel, lastNameLabel, phoneLabel, addressLabel, TINLabel,
            governmentIdentificationLabel;
    private JTextField empIdField, firstNameField, lastNameField, phoneField, addressField;
    private JLabel birthdayLabel, employeeInformationLabel;
    private JPanel birthdayContainer, statusContainer;
    private JComboBox<String> statusPick, positionPick, supervisorPick;
    private JLabel immediateSupervisorLabel, positionLabel, statusLabel, employeeStatusLabel, SSSLabel, pagIbigLabel,
            philHealthLabel;
    private JTextField sssField, pgbgField, phlHlthField, tinField;
    private JPanel salaryInfoContainer;
    private JLabel basicSalaryLabel, grossSemiMonthlyRateLabel, hourlyRateLabel;
    private JTextField bscSlryField, grossSmRateField, hrlyRateField;
    private JLabel salaryInformationLabel;
    private JPanel allowanceContainer;
    private JComboBox<String> clothAPick;
    private JLabel clothingAllowanceLabel;
    private JComboBox<String> phoneAPick;
    private JLabel phoneAllowanceLabel;
    private JComboBox<String> riceAPick;
    private JLabel riceSubsidyLabel, allowanceLabel;
    private JPanel buttonsPanel;
    private JButton clearButton, deleteButton, updateButton, addButton;
    private JPanel searchPanel;
    private JButton filterSearchButton;
    private JTextField searchField;
    private JButton updateTableButton;
    private JPanel searchButtonsPanel;
    private JButton viewAllButton;
    private JComboBox<String> tableMode;
    private JTable dataTable;
    private JDateChooser BirthdayChooser;
    private DefaultTableModel tableModel, currentModel;

    public ManageEmployees() {
        initComponents();
        actions();
        // create a blank table data
        viewAllData();
        setFilteredData(null);
        setDataTableColumnWidth();
        createUIComponents();
    }

    //-----------------Method to view all employee data
    private void viewAllData() {
        tableModel = new DefaultTableModel();
        // Read the CSV file and retrieve the data
        List<String[]> csvData = new ArrayList<>();
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
        phoneField.setText(employeeProfile.getPhoneNumber());
        addressField.setText(employeeProfile.getAddress());
        BirthdayChooser.setDate(employeeProfile.getBirthday());
        //employee status
        statusPick.setSelectedItem(employeeProfile.getStatus());
        positionPick.setSelectedItem(employeeProfile.getPosition());
        supervisorPick.setSelectedItem(employeeProfile.getImmediateSupervisor());
        //gov Id
        sssField.setText(employeeProfile.getSSS());
        pgbgField.setText(employeeProfile.getPagIbig());
        phlHlthField.setText(employeeProfile.getPhilHealth());
        tinField.setText(employeeProfile.getTin());
        //allowances
        clothAPick.setSelectedItem(employeeProfile.getClothingAllowance());
        phoneAPick.setSelectedItem(employeeProfile.getPhoneAllowance());
        riceAPick.setSelectedItem(employeeProfile.getRiceSubsidy());
        //salary info
        bscSlryField.setText(employeeProfile.getBasicSalary());
        grossSmRateField.setText(employeeProfile.getGrossRate());
        hrlyRateField.setText(employeeProfile.getHourlyRate());
    }

    //-----------------Method to clear content in infoPanel
    private void clearContent() {
        //employee info
        empIdField.setText("");
        firstNameField.setText("");
        lastNameField.setText("");
        phoneField.setText("");
        addressField.setText("");
        BirthdayChooser.setDate(new Date());
        //employee status
        statusPick.setSelectedItem("");
        positionPick.setSelectedItem("");
        supervisorPick.setSelectedItem("");
        //gov Id
        sssField.setText("");
        pgbgField.setText("");
        phlHlthField.setText("");
        tinField.setText("");
        //allowances
        clothAPick.setSelectedItem("");
        phoneAPick.setSelectedItem("");
        riceAPick.setSelectedItem("");
        //salary info
        bscSlryField.setText("");
        grossSmRateField.setText("");
        hrlyRateField.setText("");
    }

    //-----------------Method to Update Csv File based on input
    private void updateCSVFile(String employeeNumber) {
        // Read the existing CSV file data
        List<String[]> csvData;
        try (CSVReader reader = new CSVReader(new FileReader(dataPath))) {
            csvData = reader.readAll();
        } catch (IOException | CsvException e) {
            e.printStackTrace();
            return;
        }


        // Find the row for the given employee ID and update the status
        boolean foundUser = false;

        for (String[] row : csvData) {
            if (row[0].equals(employeeNumber)) {
                row[0] = empIdField.getText();
                row[1] = lastNameField.getText();
                row[2] = firstNameField.getText();
                row[3] = dateFormat.format(BirthdayChooser.getDate());
                row[4] = addressField.getText();
                row[5] = phoneField.getText();
                row[6] = sssField.getText();
                row[7] = phlHlthField.getText();
                row[8] = tinField.getText();
                row[9] = pgbgField.getText();
                row[10] = (String) statusPick.getSelectedItem();
                row[11] = (String) positionPick.getSelectedItem();
                row[12] = (String) supervisorPick.getSelectedItem();
                row[13] = bscSlryField.getText();
                row[14] = (String) riceAPick.getSelectedItem();
                row[15] = (String) phoneAPick.getSelectedItem();
                row[16] = (String) clothAPick.getSelectedItem();
                row[17] = grossSmRateField.getText();
                row[18] = hrlyRateField.getText();
                foundUser = true;
                break;
            }
        }

        // Check if the user was found in the CSV file
        if (!foundUser) {
            JOptionPane.showMessageDialog(null, "Error: Could not find user with ID " + employeeNumber, "Error", JOptionPane.ERROR_MESSAGE);
            System.out.println("Error: Could not find user with ID " + employeeNumber + " in CSV file");
            return;
        }

        // Display a confirmation dialog
        int choice =
                JOptionPane.showConfirmDialog(null, "Confirm Updating employee data", "Update Confirmation", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            // Write the updated data back to the CSV file
            try (CSVWriter writer = new CSVWriter(new FileWriter(dataPath))) {
                writer.writeAll(csvData);
                JOptionPane.showMessageDialog(null, "Employee " + employeeNumber + " has been updated.", "Update Successful", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //-----------------Method to Add data to Csv File based on input
    private void addData(String employeeNumber) {
        // Read the existing CSV file data
        List<String[]> csvData;
        try (CSVReader reader = new CSVReader(new FileReader(dataPath))) {
            csvData = reader.readAll();
        } catch (IOException | CsvException e) {
            e.printStackTrace();
            return;
        }

        // Check for duplicate employee number
        boolean isDuplicate = csvData.stream()
                .anyMatch(row -> row[0].equals(employeeNumber));

        if (isDuplicate) {
            JOptionPane.showMessageDialog(null, "Employee number " + employeeNumber + " already exists.", "Duplicate Employee Number", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Create a new row for the employee data
        String[] newRow = new String[csvData.get(0).length];
        newRow[0] = empIdField.getText();
        newRow[1] = lastNameField.getText();
        newRow[2] = firstNameField.getText();
        newRow[3] = dateFormat.format(BirthdayChooser.getDate());
        newRow[4] = addressField.getText();
        newRow[5] = phoneField.getText();
        newRow[6] = sssField.getText();
        newRow[7] = phlHlthField.getText();
        newRow[8] = tinField.getText();
        newRow[9] = pgbgField.getText();
        newRow[10] = (String) statusPick.getSelectedItem();
        newRow[11] = (String) positionPick.getSelectedItem();
        newRow[12] = (String) supervisorPick.getSelectedItem();
        newRow[13] = bscSlryField.getText();
        newRow[14] = (String) riceAPick.getSelectedItem();
        newRow[15] = (String) phoneAPick.getSelectedItem();
        newRow[16] = (String) clothAPick.getSelectedItem();
        newRow[17] = grossSmRateField.getText();
        newRow[18] = hrlyRateField.getText();

        // Add the new row to the CSV data
        csvData.add(newRow);

        // Display a confirmation dialog
        int choice =
                JOptionPane.showConfirmDialog(null, "Confirm Adding employee data", "Add Confirmation", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            // Write the updated data back to the CSV file
            try (CSVWriter writer = new CSVWriter(new FileWriter(dataPath))) {
                writer.writeAll(csvData);
                JOptionPane.showMessageDialog(null, "Employee " + employeeNumber + " has been added.", "Inserting Successful", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //-----------------Method to delete data
    private void deleteData(String employeeNumber) {
        // Read the existing CSV file data
        List<String[]> csvData;
        try (CSVReader reader = new CSVReader(new FileReader(dataPath))) {
            csvData = reader.readAll();
        } catch (IOException | CsvException e) {
            e.printStackTrace();
            return;
        }

        // Find the row for the given employee ID
        boolean foundUser = false;
        int rowIndex = -1;
        for (int i = 0; i < csvData.size(); i++) {
            String[] row = csvData.get(i);
            if (row[0].equals(employeeNumber)) {
                rowIndex = i;
                foundUser = true;
                break;
            }
        }

        // Check if the user was found in the CSV file
        if (!foundUser) {
            JOptionPane.showMessageDialog(null, "Error: Could not find user with ID " + employeeNumber, "Error", JOptionPane.ERROR_MESSAGE);
            System.out.println("Error: Could not find user with ID " + employeeNumber + " in CSV file");
            return;
        }

        // Display a confirmation dialog
        int choice =
                JOptionPane.showConfirmDialog(null, "Confirm Deleting employee data", "Delete Confirmation", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            // Remove the row from the CSV data
            csvData.remove(rowIndex);

            // Write the updated data back to the CSV file
            try (CSVWriter writer = new CSVWriter(new FileWriter(dataPath))) {
                writer.writeAll(csvData);
                JOptionPane.showMessageDialog(null, "Employee " + employeeNumber + " has been removed.", "Remove Successful", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //-----------------Method to allow editing the data in the Jtable
    private void editCSVFile(DefaultTableModel model) {
        try (CSVReader reader = new CSVReader(new FileReader(dataPath));
             CSVWriter writer = new CSVWriter(new FileWriter("src/resources/data/Details_temp.csv"))) {
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
        File tempFile = new File("src/resources/data/Details_temp.csv");
        if (tempFile.renameTo(originalFile)) {
            System.out.println("CSV file updated successfully.");
        } else {
            System.out.println("Failed to update CSV file.");
        }
    }

    //-----------------Method to get Model index for data table
    private int getModelIndex(DefaultTableModel model, String employeeNumber) {
        int rowCount = model.getRowCount();
        int columnCount = model.getColumnCount();

        for (int row = 0; row < rowCount; row++) {
            String value = model.getValueAt(row, 0).toString();
            if (value.equals(employeeNumber)) {
                return row;
            }
        }
        return -1; // Employee number not found in the model
    }


    //-----------------Method for Ui components
    private void createUIComponents() {
        BirthdayChooser = new JDateChooser();
        BirthdayChooser.setPreferredSize(new Dimension(126, 20));
        BirthdayChooser.setDateFormatString("MMMMMMMMM dd, yyyy");
        BirthdayChooser.setDate(new Date()); // Set the initial date
        birthdayContainer.add(BirthdayChooser);

    }

    //-----------------Method for actions
    private void actions() {
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

        clearButton.addActionListener(e -> clearContent());

        updateButton.addActionListener(e -> {
            String employeeNumber = empIdField.getText();
            updateCSVFile(employeeNumber);
        });

        addButton.addActionListener(e -> {
            String employeeNumber = empIdField.getText();
            addData(employeeNumber);
        });

        deleteButton.addActionListener(e -> {
            String employeeNumber = empIdField.getText();
            deleteData(employeeNumber);
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

        updateTableButton.addActionListener(e -> editCSVFile(currentModel));

        dataTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int rowNo = dataTable.getSelectedRow();
                DefaultTableModel model = (DefaultTableModel) dataTable.getModel();

                if (rowNo != -1) {
                    //employee info
                    empIdField.setText((String) model.getValueAt(rowNo, 0));
                    lastNameField.setText((String) model.getValueAt(rowNo, 1));
                    firstNameField.setText((String) model.getValueAt(rowNo, 2));

                    Date date;
                    try {
                        date = dateFormat.parse(model.getValueAt(rowNo, 3).toString());
                    } catch (ParseException ex) {
                        throw new RuntimeException(ex);
                    }

                    // Set the parsed date in the BirthdayChooser
                    BirthdayChooser.setDate(date);

                    addressField.setText((String) model.getValueAt(rowNo, 4));
                    phoneField.setText((String) model.getValueAt(rowNo, 5));

                    //gov Id
                    sssField.setText((String) model.getValueAt(rowNo, 6));
                    phlHlthField.setText((String) model.getValueAt(rowNo, 7));
                    tinField.setText((String) model.getValueAt(rowNo, 8));
                    pgbgField.setText((String) model.getValueAt(rowNo, 9));

                    //employee status
                    statusPick.setSelectedItem(model.getValueAt(rowNo, 10));
                    positionPick.setSelectedItem(model.getValueAt(rowNo, 11));
                    supervisorPick.setSelectedItem(model.getValueAt(rowNo, 12));

                    //allowances
                    riceAPick.setSelectedItem(model.getValueAt(rowNo, 13));
                    phoneAPick.setSelectedItem(model.getValueAt(rowNo, 14));
                    clothAPick.setSelectedItem(model.getValueAt(rowNo, 15));

                    //salary info
                    bscSlryField.setText((String) model.getValueAt(rowNo, 16));
                    grossSmRateField.setText((String) model.getValueAt(rowNo, 17));
                    hrlyRateField.setText((String) model.getValueAt(rowNo, 18));

                    // Highlight the selected cell
                    dataTable.setSelectionBackground(Color.YELLOW);
                    dataTable.setSelectionForeground(Color.BLACK);
                }
            }
        });

    }

    //-----------------main Method
    public static void main(String[] args) {
        JFrame frame = new JFrame("HomePage");
        frame.setContentPane(new ManageEmployees().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void initComponents() {
        mainPanel = new JPanel();
        contentPanel = new JPanel();
        southContents = new JPanel();
        infoPanel = new JPanel();
        infoCOntainer = new JPanel();
        employeeIDLabel = new JLabel();
        firstNameLabel = new JLabel();
        lastNameLabel = new JLabel();
        empIdField = new JTextField();
        firstNameField = new JTextField();
        lastNameField = new JTextField();
        phoneLabel = new JLabel();
        phoneField = new JTextField();
        addressLabel = new JLabel();
        addressField = new JTextField();
        birthdayLabel = new JLabel();
        employeeInformationLabel = new JLabel();
        birthdayContainer = new JPanel();
        statusContainer = new JPanel();
        statusPick = new JComboBox<>();
        positionPick = new JComboBox<>();
        supervisorPick = new JComboBox<>();
        immediateSupervisorLabel = new JLabel();
        positionLabel = new JLabel();
        statusLabel = new JLabel();
        employeeStatusLabel = new JLabel();
        govIdContainer = new JPanel();
        SSSLabel = new JLabel();
        pagIbigLabel = new JLabel();
        philHealthLabel = new JLabel();
        sssField = new JTextField();
        pgbgField = new JTextField();
        phlHlthField = new JTextField();
        TINLabel = new JLabel();
        tinField = new JTextField();
        governmentIdentificationLabel = new JLabel();
        salaryInfoContainer = new JPanel();
        basicSalaryLabel = new JLabel();
        grossSemiMonthlyRateLabel = new JLabel();
        hourlyRateLabel = new JLabel();
        bscSlryField = new JTextField();
        grossSmRateField = new JTextField();
        hrlyRateField = new JTextField();
        salaryInformationLabel = new JLabel();
        allowanceContainer = new JPanel();
        clothAPick = new JComboBox<>();
        clothingAllowanceLabel = new JLabel();
        phoneAPick = new JComboBox<>();
        phoneAllowanceLabel = new JLabel();
        riceAPick = new JComboBox<>();
        riceSubsidyLabel = new JLabel();
        allowanceLabel = new JLabel();
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

                //======== southContents ========
                {
                    southContents.setLayout(new BorderLayout());

                    //======== infoPanel ========
                    {
                        infoPanel.setBackground(new Color(0xb8d9b6));
                        infoPanel.setFont(new Font("Fira Code", infoPanel.getFont().getStyle(), infoPanel.getFont().getSize()));
                        infoPanel.setLayout(new GridBagLayout());

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

                            //---- phoneLabel ----
                            phoneLabel.setFont(new Font("Fira Code", phoneLabel.getFont().getStyle(), phoneLabel.getFont().getSize()));
                            phoneLabel.setText("Phone #");
                            infoCOntainer.add(phoneLabel, new GridBagConstraints(0, 4, 1, 1, 1.0, 0.0,
                                GridBagConstraints.EAST, GridBagConstraints.NONE,
                                new Insets(0, 5, 5, 5), 0, 0));

                            //---- phoneField ----
                            phoneField.setColumns(15);
                            phoneField.setFont(new Font("Fira Code", phoneField.getFont().getStyle(), phoneField.getFont().getSize()));
                            infoCOntainer.add(phoneField, new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0,
                                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                                new Insets(0, 5, 5, 5), 0, 0));

                            //---- addressLabel ----
                            addressLabel.setFont(new Font("Fira Code", addressLabel.getFont().getStyle(), addressLabel.getFont().getSize()));
                            addressLabel.setText("Address");
                            infoCOntainer.add(addressLabel, new GridBagConstraints(0, 5, 1, 1, 1.0, 0.0,
                                GridBagConstraints.EAST, GridBagConstraints.NONE,
                                new Insets(0, 5, 5, 5), 0, 0));

                            //---- addressField ----
                            addressField.setColumns(15);
                            addressField.setFont(new Font("Fira Code", addressField.getFont().getStyle(), addressField.getFont().getSize()));
                            infoCOntainer.add(addressField, new GridBagConstraints(1, 5, 1, 1, 0.0, 0.0,
                                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                                new Insets(0, 5, 5, 5), 0, 0));

                            //---- birthdayLabel ----
                            birthdayLabel.setFont(new Font("Fira Code", birthdayLabel.getFont().getStyle(), birthdayLabel.getFont().getSize()));
                            birthdayLabel.setText("Birthday");
                            infoCOntainer.add(birthdayLabel, new GridBagConstraints(0, 6, 1, 1, 1.0, 0.0,
                                GridBagConstraints.EAST, GridBagConstraints.NONE,
                                new Insets(0, 5, 5, 5), 0, 0));

                            //---- employeeInformationLabel ----
                            employeeInformationLabel.setFont(new Font("Fira Code", Font.BOLD, 16));
                            employeeInformationLabel.setText("Employee Information");
                            infoCOntainer.add(employeeInformationLabel, new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0,
                                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                                new Insets(5, 0, 10, 0), 0, 0));

                            //======== birthdayContainer ========
                            {
                                birthdayContainer.setBackground(new Color(0xf1faee));
                                birthdayContainer.setLayout(new BorderLayout());
                            }
                            infoCOntainer.add(birthdayContainer, new GridBagConstraints(1, 6, 1, 1, 0.0, 0.0,
                                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                                new Insets(0, 5, 5, 5), 0, 0));
                        }
                        infoPanel.add(infoCOntainer, new GridBagConstraints(0, 0, 1, 2, 1.0, 1.0,
                            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                            new Insets(0, 0, 0, 0), 0, 0));

                        //======== statusContainer ========
                        {
                            statusContainer.setBackground(new Color(0xb8d9b6));
                            statusContainer.setLayout(new GridBagLayout());

                            //---- statusPick ----
                            statusPick.setFont(new Font("Fira Code", statusPick.getFont().getStyle(), statusPick.getFont().getSize()));
                            statusPick.setMinimumSize(new Dimension(126, 25));
                            statusPick.setModel(new DefaultComboBoxModel<>(new String[] {
                                "",
                                "Regular",
                                "Probationary"
                            }));
                            statusPick.setPreferredSize(new Dimension(175, 25));
                            statusContainer.add(statusPick, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
                                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                                new Insets(0, 5, 5, 5), 0, 0));

                            //---- positionPick ----
                            positionPick.setFont(new Font("Fira Code", positionPick.getFont().getStyle(), positionPick.getFont().getSize()));
                            positionPick.setMinimumSize(new Dimension(126, 25));
                            positionPick.setModel(new DefaultComboBoxModel<>(new String[] {
                                "",
                                "HR Manager",
                                "HR Team Leader",
                                "HR Rank and File",
                                "Account Manager",
                                "Account Team Leader",
                                "Account Rank and File",
                                "Payroll Manager",
                                "Payroll Team Leader",
                                "Payroll Rank and File"
                            }));
                            positionPick.setPreferredSize(new Dimension(126, 25));
                            statusContainer.add(positionPick, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0,
                                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                                new Insets(0, 5, 5, 5), 0, 0));

                            //---- supervisorPick ----
                            supervisorPick.setFont(new Font("Fira Code", supervisorPick.getFont().getStyle(), supervisorPick.getFont().getSize()));
                            supervisorPick.setMinimumSize(new Dimension(126, 25));
                            supervisorPick.setModel(new DefaultComboBoxModel<>(new String[] {
                                "",
                                "N/A",
                                "Crisostomo, Jose",
                                "De Leon, Selena",
                                "Farala, Martha",
                                "Mata, Christian",
                                "Romualdez, Fredrick",
                                "Salcedo, Anthony",
                                "San Jose, Brad"
                            }));
                            supervisorPick.setPreferredSize(new Dimension(126, 25));
                            statusContainer.add(supervisorPick, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0,
                                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                                new Insets(0, 5, 5, 5), 0, 0));

                            //---- immediateSupervisorLabel ----
                            immediateSupervisorLabel.setFont(new Font("Fira Code", immediateSupervisorLabel.getFont().getStyle(), immediateSupervisorLabel.getFont().getSize()));
                            immediateSupervisorLabel.setText("Immediate Supervisor:");
                            statusContainer.add(immediateSupervisorLabel, new GridBagConstraints(0, 3, 1, 1, 1.0, 0.0,
                                GridBagConstraints.EAST, GridBagConstraints.NONE,
                                new Insets(0, 5, 5, 5), 0, 0));

                            //---- positionLabel ----
                            positionLabel.setFont(new Font("Fira Code", positionLabel.getFont().getStyle(), positionLabel.getFont().getSize()));
                            positionLabel.setText("Position:");
                            statusContainer.add(positionLabel, new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0,
                                GridBagConstraints.EAST, GridBagConstraints.NONE,
                                new Insets(0, 5, 5, 5), 0, 0));

                            //---- statusLabel ----
                            statusLabel.setFont(new Font("Fira Code", statusLabel.getFont().getStyle(), statusLabel.getFont().getSize()));
                            statusLabel.setText("Status:");
                            statusContainer.add(statusLabel, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0,
                                GridBagConstraints.EAST, GridBagConstraints.NONE,
                                new Insets(0, 5, 5, 5), 0, 0));

                            //---- employeeStatusLabel ----
                            employeeStatusLabel.setFont(new Font("Fira Code", Font.BOLD, 16));
                            employeeStatusLabel.setText("Employee Status");
                            statusContainer.add(employeeStatusLabel, new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0,
                                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                                new Insets(5, 0, 10, 0), 0, 0));
                        }
                        infoPanel.add(statusContainer, new GridBagConstraints(1, 0, 1, 1, 1.0, 1.0,
                            GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 0, 0, 0), 0, 0));

                        //======== govIdContainer ========
                        {
                            govIdContainer.setBackground(new Color(0xb8d9b6));
                            govIdContainer.setLayout(new GridBagLayout());

                            //---- SSSLabel ----
                            SSSLabel.setFont(new Font("Fira Code", SSSLabel.getFont().getStyle(), SSSLabel.getFont().getSize()));
                            SSSLabel.setText("SSS:");
                            govIdContainer.add(SSSLabel, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0,
                                GridBagConstraints.EAST, GridBagConstraints.NONE,
                                new Insets(0, 5, 5, 5), 0, 0));

                            //---- pagIbigLabel ----
                            pagIbigLabel.setFont(new Font("Fira Code", pagIbigLabel.getFont().getStyle(), pagIbigLabel.getFont().getSize()));
                            pagIbigLabel.setText("Pag-Ibig:");
                            govIdContainer.add(pagIbigLabel, new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0,
                                GridBagConstraints.EAST, GridBagConstraints.NONE,
                                new Insets(0, 5, 5, 5), 0, 0));

                            //---- philHealthLabel ----
                            philHealthLabel.setFont(new Font("Fira Code", philHealthLabel.getFont().getStyle(), philHealthLabel.getFont().getSize()));
                            philHealthLabel.setText("PhilHealth:");
                            govIdContainer.add(philHealthLabel, new GridBagConstraints(0, 3, 1, 1, 1.0, 0.0,
                                GridBagConstraints.EAST, GridBagConstraints.NONE,
                                new Insets(0, 5, 5, 5), 0, 0));

                            //---- sssField ----
                            sssField.setColumns(15);
                            sssField.setFont(new Font("Fira Code", sssField.getFont().getStyle(), sssField.getFont().getSize()));
                            govIdContainer.add(sssField, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
                                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                                new Insets(0, 5, 5, 5), 0, 0));

                            //---- pgbgField ----
                            pgbgField.setColumns(15);
                            pgbgField.setFont(new Font("Fira Code", pgbgField.getFont().getStyle(), pgbgField.getFont().getSize()));
                            govIdContainer.add(pgbgField, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0,
                                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                                new Insets(0, 5, 5, 5), 0, 0));

                            //---- phlHlthField ----
                            phlHlthField.setColumns(15);
                            phlHlthField.setFont(new Font("Fira Code", phlHlthField.getFont().getStyle(), phlHlthField.getFont().getSize()));
                            govIdContainer.add(phlHlthField, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0,
                                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                                new Insets(0, 5, 5, 5), 0, 0));

                            //---- TINLabel ----
                            TINLabel.setFont(new Font("Fira Code", TINLabel.getFont().getStyle(), TINLabel.getFont().getSize()));
                            TINLabel.setText("TIN:");
                            govIdContainer.add(TINLabel, new GridBagConstraints(0, 4, 1, 1, 1.0, 0.0,
                                GridBagConstraints.EAST, GridBagConstraints.NONE,
                                new Insets(0, 5, 5, 5), 0, 0));

                            //---- tinField ----
                            tinField.setColumns(15);
                            tinField.setFont(new Font("Fira Code", tinField.getFont().getStyle(), tinField.getFont().getSize()));
                            govIdContainer.add(tinField, new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0,
                                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                                new Insets(0, 5, 5, 5), 0, 0));

                            //---- governmentIdentificationLabel ----
                            governmentIdentificationLabel.setFont(new Font("Fira Code", Font.BOLD, 16));
                            governmentIdentificationLabel.setText("Government Identification");
                            govIdContainer.add(governmentIdentificationLabel, new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0,
                                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                                new Insets(5, 0, 10, 0), 0, 0));
                        }
                        infoPanel.add(govIdContainer, new GridBagConstraints(2, 0, 1, 1, 1.0, 1.0,
                            GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 0, 0, 0), 0, 0));

                        //======== salaryInfoContainer ========
                        {
                            salaryInfoContainer.setBackground(new Color(0xb8d9b6));
                            salaryInfoContainer.setLayout(new GridBagLayout());

                            //---- basicSalaryLabel ----
                            basicSalaryLabel.setFont(new Font("Fira Code", basicSalaryLabel.getFont().getStyle(), basicSalaryLabel.getFont().getSize()));
                            basicSalaryLabel.setText("Basic Salary:");
                            salaryInfoContainer.add(basicSalaryLabel, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0,
                                GridBagConstraints.EAST, GridBagConstraints.NONE,
                                new Insets(0, 5, 5, 5), 0, 0));

                            //---- grossSemiMonthlyRateLabel ----
                            grossSemiMonthlyRateLabel.setFont(new Font("Fira Code", grossSemiMonthlyRateLabel.getFont().getStyle(), grossSemiMonthlyRateLabel.getFont().getSize()));
                            grossSemiMonthlyRateLabel.setText("Gross Semi-monthly Rate:");
                            salaryInfoContainer.add(grossSemiMonthlyRateLabel, new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0,
                                GridBagConstraints.EAST, GridBagConstraints.NONE,
                                new Insets(0, 5, 5, 5), 0, 0));

                            //---- hourlyRateLabel ----
                            hourlyRateLabel.setFont(new Font("Fira Code", hourlyRateLabel.getFont().getStyle(), hourlyRateLabel.getFont().getSize()));
                            hourlyRateLabel.setText("Hourly Rate:");
                            salaryInfoContainer.add(hourlyRateLabel, new GridBagConstraints(0, 3, 1, 1, 1.0, 0.0,
                                GridBagConstraints.EAST, GridBagConstraints.NONE,
                                new Insets(0, 5, 5, 5), 0, 0));

                            //---- bscSlryField ----
                            bscSlryField.setAutoscrolls(false);
                            bscSlryField.setColumns(15);
                            bscSlryField.setFont(new Font("Fira Code", bscSlryField.getFont().getStyle(), bscSlryField.getFont().getSize()));
                            salaryInfoContainer.add(bscSlryField, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
                                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                                new Insets(0, 5, 5, 5), 0, 0));

                            //---- grossSmRateField ----
                            grossSmRateField.setColumns(15);
                            grossSmRateField.setFont(new Font("Fira Code", grossSmRateField.getFont().getStyle(), grossSmRateField.getFont().getSize()));
                            salaryInfoContainer.add(grossSmRateField, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0,
                                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                                new Insets(0, 5, 5, 5), 0, 0));

                            //---- hrlyRateField ----
                            hrlyRateField.setColumns(15);
                            hrlyRateField.setFont(new Font("Fira Code", hrlyRateField.getFont().getStyle(), hrlyRateField.getFont().getSize()));
                            salaryInfoContainer.add(hrlyRateField, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0,
                                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                                new Insets(0, 5, 5, 5), 0, 0));

                            //---- salaryInformationLabel ----
                            salaryInformationLabel.setFont(new Font("Fira Code", Font.BOLD, 16));
                            salaryInformationLabel.setText("Salary Information");
                            salaryInfoContainer.add(salaryInformationLabel, new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0,
                                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                                new Insets(5, 0, 10, 0), 0, 0));
                        }
                        infoPanel.add(salaryInfoContainer, new GridBagConstraints(2, 1, 1, 1, 1.0, 1.0,
                            GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 0, 0, 0), 0, 0));

                        //======== allowanceContainer ========
                        {
                            allowanceContainer.setBackground(new Color(0xb8d9b6));
                            allowanceContainer.setLayout(new GridBagLayout());

                            //---- clothAPick ----
                            clothAPick.setFont(new Font("Fira Code", clothAPick.getFont().getStyle(), clothAPick.getFont().getSize()));
                            clothAPick.setMinimumSize(new Dimension(126, 25));
                            clothAPick.setModel(new DefaultComboBoxModel<>(new String[] {
                                "",
                                "0.00",
                                "50.00",
                                "800.00",
                                "1000.00"
                            }));
                            clothAPick.setPreferredSize(new Dimension(175, 25));
                            allowanceContainer.add(clothAPick, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
                                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                                new Insets(0, 5, 5, 5), 0, 0));

                            //---- clothingAllowanceLabel ----
                            clothingAllowanceLabel.setFont(new Font("Fira Code", clothingAllowanceLabel.getFont().getStyle(), clothingAllowanceLabel.getFont().getSize()));
                            clothingAllowanceLabel.setText("Clothing Allowance");
                            allowanceContainer.add(clothingAllowanceLabel, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0,
                                GridBagConstraints.EAST, GridBagConstraints.NONE,
                                new Insets(0, 5, 5, 5), 0, 0));

                            //---- phoneAPick ----
                            phoneAPick.setFont(new Font("Fira Code", phoneAPick.getFont().getStyle(), phoneAPick.getFont().getSize()));
                            phoneAPick.setMinimumSize(new Dimension(126, 25));
                            phoneAPick.setModel(new DefaultComboBoxModel<>(new String[] {
                                "",
                                "0.00",
                                "500.00",
                                "800.00",
                                "1000.00"
                            }));
                            phoneAPick.setPreferredSize(new Dimension(126, 25));
                            allowanceContainer.add(phoneAPick, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0,
                                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                                new Insets(0, 5, 5, 5), 0, 0));

                            //---- phoneAllowanceLabel ----
                            phoneAllowanceLabel.setFont(new Font("Fira Code", phoneAllowanceLabel.getFont().getStyle(), phoneAllowanceLabel.getFont().getSize()));
                            phoneAllowanceLabel.setText("Phone Allowance");
                            allowanceContainer.add(phoneAllowanceLabel, new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0,
                                GridBagConstraints.EAST, GridBagConstraints.NONE,
                                new Insets(0, 5, 5, 5), 0, 0));

                            //---- riceAPick ----
                            riceAPick.setFont(new Font("Fira Code", riceAPick.getFont().getStyle(), riceAPick.getFont().getSize()));
                            riceAPick.setMinimumSize(new Dimension(126, 25));
                            riceAPick.setModel(new DefaultComboBoxModel<>(new String[] {
                                "",
                                "0.00",
                                "1500.00"
                            }));
                            riceAPick.setPreferredSize(new Dimension(126, 25));
                            allowanceContainer.add(riceAPick, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0,
                                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                                new Insets(0, 5, 5, 5), 0, 0));

                            //---- riceSubsidyLabel ----
                            riceSubsidyLabel.setFont(new Font("Fira Code", riceSubsidyLabel.getFont().getStyle(), riceSubsidyLabel.getFont().getSize()));
                            riceSubsidyLabel.setText("Rice Subsidy");
                            allowanceContainer.add(riceSubsidyLabel, new GridBagConstraints(0, 3, 1, 1, 1.0, 0.0,
                                GridBagConstraints.EAST, GridBagConstraints.NONE,
                                new Insets(0, 5, 5, 5), 0, 0));

                            //---- allowanceLabel ----
                            allowanceLabel.setFont(new Font("Fira Code", Font.BOLD, 16));
                            allowanceLabel.setText("Allowance");
                            allowanceContainer.add(allowanceLabel, new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0,
                                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                                new Insets(5, 0, 10, 0), 0, 0));
                        }
                        infoPanel.add(allowanceContainer, new GridBagConstraints(1, 1, 1, 1, 1.0, 1.0,
                            GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 0, 0, 0), 0, 0));
                    }
                    southContents.add(infoPanel, BorderLayout.CENTER);

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
                    southContents.add(buttonsPanel, BorderLayout.SOUTH);
                }
                contentPanel.add(southContents, BorderLayout.SOUTH);

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
                    dataTable.setFont(new Font("Fira Code", dataTable.getFont().getStyle(), 14));
                    dataTable.setRequestFocusEnabled(true);
                    dataTable.setRowMargin(1);
                    dataTable.setShowVerticalLines(true);
                    dataTable.setUpdateSelectionOnSort(true);
                    dataTable.putClientProperty("JTable.autoStartsEdit", true);
                    scrollPane1.setViewportView(dataTable);
                }
                contentPanel.add(scrollPane1, BorderLayout.CENTER);
            }
            mainPanel.add(contentPanel, BorderLayout.CENTER);
        }
    }
}
