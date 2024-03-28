package main.java.frontend;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

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
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class DashBoard {

    private JPanel mainPanel;
    public JPanel contentPanel;
    private JPanel cntnr_NoOfEmp;
    private JLabel lbl_totalEmployeeValue;
    private JPanel cntnr_PayrollC;
    private JLabel noOfEmployeesLabel;
    private JLabel payrollCalendarLabel;
    private JPanel cntnr_PyrllExpenses;
    private JTable tbl_EmployeeDetails;
    private JLabel salaryDetailsLabel;
    private JTable tbl_SalaryDetails;
    private JPanel cntnr_Rmndr;
    private JLabel remindersLabel;
    private JLabel announcementsLabel;
    private JPanel cntnr_Anncmnt;
    private JLabel employeeDetailsLabel;
    private String dataPath = "src/resources/data/Details.csv";
    private DefaultTableModel tableModel;

    public DashBoard(){
        initComponents();
        tbl_EmployeeDetailsComponents();
        tbl_SalaryDetailsComponents();
        setDataTableColumnWidth(tbl_EmployeeDetails);
        setDataTableColumnWidth(tbl_SalaryDetails);
    }

    private void tbl_EmployeeDetailsComponents() {
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
        int[] columnIndices = new int[]{0, 1, 2, 5};

        // Add the column names to the table model
        for (int columnIndex : columnIndices) {
            tableModel.addColumn(columnHeaders[columnIndex]);
        }

        // add the data to the table model
        for (int i = 1; i < csvData.size(); i++) {
            String[] data = csvData.get(i);
            Object[] rowData = new Object[columnIndices.length];

            for (int j = 0; j < columnIndices.length; j++) {
                rowData[j] = data[columnIndices[j]];
            }
            tableModel.addRow(rowData);
        }
        tbl_EmployeeDetails.setModel(tableModel);
    }

    private void tbl_SalaryDetailsComponents() {
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
        int[] columnIndices = new int[]{0, 11, 16, 18};

        // Add the column names to the table model
        for (int columnIndex : columnIndices) {
            tableModel.addColumn(columnHeaders[columnIndex]);
        }
        // Add the "Benefits" column to the table model
        tableModel.addColumn("Benefits");

        // Add the data to the table model
        for (int i = 1; i < csvData.size(); i++) {
            String[] data = csvData.get(i);
            Object[] rowData = new Object[columnIndices.length + 1]; // +1 for the "Benefits" column

            for (int j = 0; j < columnIndices.length; j++) {
                rowData[j] = data[columnIndices[j]];
            }
            // Calculate the sum for the "Benefits" column
            BigDecimal sum = BigDecimal.ZERO;
            for (int j = 13; j <= 15; j++) {
                sum = sum.add(new BigDecimal(data[j]));
            }
            rowData[columnIndices.length] = sum; // Set the sum in the "Benefits" column
            tableModel.addRow(rowData);
        }
        // Set the table model and adjust column widths
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

    public static void main(String[] args) {
        JFrame frame = new JFrame("MotorPH");
        frame.setContentPane(new DashBoard().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    private void initComponents() {
        mainPanel = new JPanel();
        contentPanel = new JPanel();
        cntnr_NoOfEmp = new JPanel();
        lbl_totalEmployeeValue = new JLabel();
        cntnr_PayrollC = new JPanel();
        var label1 = new JLabel();
        noOfEmployeesLabel = new JLabel();
        payrollCalendarLabel = new JLabel();
        var label2 = new JLabel();
        cntnr_PyrllExpenses = new JPanel();
        var label3 = new JLabel();
        var scrollPane1 = new JScrollPane();
        tbl_EmployeeDetails = new JTable();
        salaryDetailsLabel = new JLabel();
        var scrollPane2 = new JScrollPane();
        tbl_SalaryDetails = new JTable();
        cntnr_Rmndr = new JPanel();
        var label4 = new JLabel();
        remindersLabel = new JLabel();
        announcementsLabel = new JLabel();
        cntnr_Anncmnt = new JPanel();
        var label5 = new JLabel();
        employeeDetailsLabel = new JLabel();

        //======== mainPanel ========
        {
            mainPanel.setPreferredSize(new Dimension(1104, 746));
            mainPanel.setLayout(new BorderLayout());

            //======== contentPanel ========
            {
                contentPanel.setBackground(new Color(0xb2d8d8));
                contentPanel.setBorder(new TitledBorder(new BevelBorder(BevelBorder.LOWERED), ""));
                contentPanel.setLayout(new GridBagLayout());

                //======== cntnr_NoOfEmp ========
                {
                    cntnr_NoOfEmp.setBackground(new Color(0xced3cc));
                    cntnr_NoOfEmp.setMinimumSize(new Dimension(200, 125));
                    cntnr_NoOfEmp.setPreferredSize(new Dimension(200, 125));
                    cntnr_NoOfEmp.setBorder(new TitledBorder(new BevelBorder(BevelBorder.RAISED), ""));
                    cntnr_NoOfEmp.setLayout(new GridBagLayout());

                    //---- lbl_totalEmployeeValue ----
                    lbl_totalEmployeeValue.setFont(new Font("Arial Black", lbl_totalEmployeeValue.getFont().getStyle(), 36));
                    lbl_totalEmployeeValue.setText("25");
                    cntnr_NoOfEmp.add(lbl_totalEmployeeValue, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.VERTICAL,
                        new Insets(0, 0, 0, 0), 0, 0));
                }
                contentPanel.add(cntnr_NoOfEmp, new GridBagConstraints(0, 1, 2, 1, 0.0, 0.0,
                    GridBagConstraints.WEST, GridBagConstraints.VERTICAL,
                    new Insets(5, 5, 5, 5), 0, 0));

                //======== cntnr_PayrollC ========
                {
                    cntnr_PayrollC.setBackground(new Color(0xced3cc));
                    cntnr_PayrollC.setMinimumSize(new Dimension(200, 125));
                    cntnr_PayrollC.setPreferredSize(new Dimension(200, 125));
                    cntnr_PayrollC.setBorder(new TitledBorder(new BevelBorder(BevelBorder.RAISED), ""));
                    cntnr_PayrollC.setLayout(new GridBagLayout());

                    //---- label1 ----
                    label1.setText("N/A");
                    cntnr_PayrollC.add(label1, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.VERTICAL,
                        new Insets(0, 0, 0, 0), 0, 0));
                }
                contentPanel.add(cntnr_PayrollC, new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0,
                    GridBagConstraints.WEST, GridBagConstraints.VERTICAL,
                    new Insets(5, 5, 5, 5), 0, 0));

                //---- noOfEmployeesLabel ----
                noOfEmployeesLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
                noOfEmployeesLabel.setText("No. of Employees");
                contentPanel.add(noOfEmployeesLabel, new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0,
                    GridBagConstraints.WEST, GridBagConstraints.NONE,
                    new Insets(35, 5, 5, 5), 0, 0));

                //---- payrollCalendarLabel ----
                payrollCalendarLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
                payrollCalendarLabel.setText("Payroll Calendar");
                contentPanel.add(payrollCalendarLabel, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.WEST, GridBagConstraints.NONE,
                    new Insets(35, 5, 5, 5), 0, 0));

                //---- label2 ----
                label2.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
                label2.setText("Payroll Expenses");
                contentPanel.add(label2, new GridBagConstraints(6, 2, 3, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.NONE,
                    new Insets(15, 5, 5, 5), 0, 0));

                //======== cntnr_PyrllExpenses ========
                {
                    cntnr_PyrllExpenses.setBackground(new Color(0xced3cc));
                    cntnr_PyrllExpenses.setMinimumSize(new Dimension(300, 400));
                    cntnr_PyrllExpenses.setPreferredSize(new Dimension(300, 600));
                    cntnr_PyrllExpenses.setBorder(new TitledBorder(new BevelBorder(BevelBorder.RAISED), ""));
                    cntnr_PyrllExpenses.setLayout(new GridBagLayout());

                    //---- label3 ----
                    label3.setText("N/A");
                    cntnr_PyrllExpenses.add(label3, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.NONE,
                        new Insets(0, 0, 0, 0), 0, 0));
                }
                contentPanel.add(cntnr_PyrllExpenses, new GridBagConstraints(6, 3, 3, 7, 0.0, 0.0,
                    GridBagConstraints.NORTH, GridBagConstraints.NONE,
                    new Insets(5, 5, 15, 5), 0, 0));

                //======== scrollPane1 ========
                {
                    scrollPane1.setAlignmentY(1.0F);
                    scrollPane1.setMinimumSize(new Dimension(370, 200));
                    scrollPane1.setOpaque(false);
                    scrollPane1.setPreferredSize(new Dimension(420, 427));

                    //---- tbl_EmployeeDetails ----
                    tbl_EmployeeDetails.setAutoCreateColumnsFromModel(true);
                    tbl_EmployeeDetails.setAutoCreateRowSorter(true);
                    tbl_EmployeeDetails.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
                    tbl_EmployeeDetails.setCellSelectionEnabled(true);
                    tbl_EmployeeDetails.setColumnSelectionAllowed(true);
                    tbl_EmployeeDetails.setDoubleBuffered(false);
                    tbl_EmployeeDetails.setDragEnabled(true);
                    tbl_EmployeeDetails.setEnabled(false);
                    tbl_EmployeeDetails.setFont(new Font("Fira Code", tbl_EmployeeDetails.getFont().getStyle(), 14));
                    tbl_EmployeeDetails.setMinimumSize(new Dimension(150, 32));
                    tbl_EmployeeDetails.setRequestFocusEnabled(true);
                    tbl_EmployeeDetails.setRowMargin(1);
                    tbl_EmployeeDetails.setShowVerticalLines(true);
                    tbl_EmployeeDetails.setUpdateSelectionOnSort(true);
                    tbl_EmployeeDetails.putClientProperty("JTable.autoStartsEdit", true);
                    scrollPane1.setViewportView(tbl_EmployeeDetails);
                }
                contentPanel.add(scrollPane1, new GridBagConstraints(1, 5, 5, 1, 0.0, 1.0,
                    GridBagConstraints.NORTH, GridBagConstraints.NONE,
                    new Insets(0, 0, 0, 0), 0, 0));

                //---- salaryDetailsLabel ----
                salaryDetailsLabel.setText("Salary Details");
                contentPanel.add(salaryDetailsLabel, new GridBagConstraints(0, 6, 6, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.NONE,
                    new Insets(5, 5, 5, 5), 0, 0));

                //======== scrollPane2 ========
                {
                    scrollPane2.setAlignmentY(1.0F);
                    scrollPane2.setMinimumSize(new Dimension(480, 200));
                    scrollPane2.setPreferredSize(new Dimension(440, 427));

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
                    scrollPane2.setViewportView(tbl_SalaryDetails);
                }
                contentPanel.add(scrollPane2, new GridBagConstraints(0, 7, 6, 1, 0.0, 1.0,
                    GridBagConstraints.NORTH, GridBagConstraints.NONE,
                    new Insets(0, 0, 0, 0), 0, 0));

                //======== cntnr_Rmndr ========
                {
                    cntnr_Rmndr.setBackground(new Color(0xced3cc));
                    cntnr_Rmndr.setMinimumSize(new Dimension(200, 125));
                    cntnr_Rmndr.setPreferredSize(new Dimension(200, 125));
                    cntnr_Rmndr.setBorder(new TitledBorder(new BevelBorder(BevelBorder.RAISED), ""));
                    cntnr_Rmndr.setLayout(new GridBagLayout());

                    //---- label4 ----
                    label4.setText("N/A");
                    cntnr_Rmndr.add(label4, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.VERTICAL,
                        new Insets(0, 0, 0, 0), 0, 0));
                }
                contentPanel.add(cntnr_Rmndr, new GridBagConstraints(5, 1, 1, 1, 0.0, 0.0,
                    GridBagConstraints.WEST, GridBagConstraints.VERTICAL,
                    new Insets(5, 5, 5, 5), 0, 0));

                //---- remindersLabel ----
                remindersLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
                remindersLabel.setText("Reminders");
                contentPanel.add(remindersLabel, new GridBagConstraints(5, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.WEST, GridBagConstraints.NONE,
                    new Insets(35, 5, 5, 5), 0, 0));

                //---- announcementsLabel ----
                announcementsLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
                announcementsLabel.setText("Announcements");
                contentPanel.add(announcementsLabel, new GridBagConstraints(7, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.WEST, GridBagConstraints.NONE,
                    new Insets(35, 5, 5, 5), 0, 0));

                //======== cntnr_Anncmnt ========
                {
                    cntnr_Anncmnt.setBackground(new Color(0xced3cc));
                    cntnr_Anncmnt.setMinimumSize(new Dimension(200, 125));
                    cntnr_Anncmnt.setPreferredSize(new Dimension(300, 125));
                    cntnr_Anncmnt.setBorder(new TitledBorder(new BevelBorder(BevelBorder.RAISED), ""));
                    cntnr_Anncmnt.setLayout(new GridBagLayout());

                    //---- label5 ----
                    label5.setText("N/A");
                    cntnr_Anncmnt.add(label5, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.VERTICAL,
                        new Insets(0, 0, 0, 0), 0, 0));
                }
                contentPanel.add(cntnr_Anncmnt, new GridBagConstraints(7, 1, 1, 1, 0.0, 0.0,
                    GridBagConstraints.WEST, GridBagConstraints.VERTICAL,
                    new Insets(5, 5, 5, 5), 0, 0));

                //---- employeeDetailsLabel ----
                employeeDetailsLabel.setText("Employee Details");
                contentPanel.add(employeeDetailsLabel, new GridBagConstraints(0, 4, 6, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.NONE,
                    new Insets(15, 5, 5, 5), 0, 0));
            }
            mainPanel.add(contentPanel, BorderLayout.CENTER);
        }
    }
}
