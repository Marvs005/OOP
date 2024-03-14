package main.java.frontend;

import main.java.run.App;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class HomePage {
    private static JFrame frame;
    private JPanel current_panel;
    private JButton prev_Clicked;
    private JPanel homePanel;
    private JPanel mainPanel;
    private JPanel menuBar;
    private JPanel sidePanel;
    private JPanel homeContainer;
    private JButton homePageButton;
    private JPanel featuresContainer;
    private JButton manageEmployeesButton, manageSalaryButton, attendanceButton, leaveButton, logoutButton;
    private JPanel welcomeContainer, logoutContainer;
    private JPanel m_contentPanel;

    public HomePage() {
        initComponents();
        DashBoard dashBoard = new DashBoard();
        m_contentPanel.add(dashBoard.contentPanel);
        current_panel = dashBoard.contentPanel;

        actions();
    }

    private void actions() {
    // Create an ActionListener instance to handle button clicks
    ActionListener buttonClickListener = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            AbstractButton clickedButton = (AbstractButton) e.getSource();

            // Revert the previous clicked button's background color
            if (prev_Clicked != null) {
                prev_Clicked.setBackground(Color.decode("#1D3557"));
            }

            // Change the background color of the clicked button
            clickedButton.setBackground(Color.decode("#457B9D"));

            // Update the current panel based on the clicked button
            m_contentPanel.remove(current_panel);

            if (clickedButton == homePageButton) {
                DashBoard dashBoard = new DashBoard();
                m_contentPanel.add(dashBoard.contentPanel);
                current_panel = dashBoard.contentPanel;
            }
            else if (clickedButton == manageEmployeesButton) {
                ManageEmployees manageEmployees = new ManageEmployees();
                m_contentPanel.add(manageEmployees.contentPanel);
                current_panel = manageEmployees.contentPanel;
            } else if (clickedButton == manageSalaryButton) {
                ManageSalary manageSalary = new ManageSalary();
                m_contentPanel.add(manageSalary.contentPanel);
                current_panel = manageSalary.contentPanel;
            } else if (clickedButton == attendanceButton) {
                AttendanceRecords attendanceRecords = new AttendanceRecords();
                m_contentPanel.add(attendanceRecords.contentPanel);
                current_panel = attendanceRecords.contentPanel;
            } else if (clickedButton == leaveButton) {
                LeaveApplication leaveApplication = new LeaveApplication();
                m_contentPanel.add(leaveApplication.contentPanel);
                current_panel = leaveApplication.contentPanel;
            }

            m_contentPanel.revalidate();
            m_contentPanel.repaint();
            prev_Clicked = (JButton) clickedButton;
        }
    };

    // Add the ActionListener to the buttons
    homePageButton.addActionListener(buttonClickListener);
    manageEmployeesButton.addActionListener(buttonClickListener);
    manageSalaryButton.addActionListener(buttonClickListener);
    attendanceButton.addActionListener(buttonClickListener);
    leaveButton.addActionListener(buttonClickListener);
    logoutButton.addActionListener(e -> {
        App.main(null);
        frame.dispose();
    });

    // Hover effects
    setupHoverEffect(manageEmployeesButton);
    setupHoverEffect(manageSalaryButton);
    setupHoverEffect(attendanceButton);
    setupHoverEffect(leaveButton);
}

    // -----------------Method to setup hover effects
    private void setupHoverEffect(AbstractButton button) {
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                if (button != prev_Clicked) {
                    button.setBackground(Color.decode("#B2D8D8"));
                }
            }

            public void mouseExited(MouseEvent e) {
                if (button != prev_Clicked) {
                    button.setBackground(Color.decode("#1D3557"));
                }
            }
        });
    }

    //-----------------main Method
    public static void main(String[] args) {
        frame = new JFrame("MotorPH");
        frame.setContentPane(new HomePage().homePanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void initComponents() {
        homePanel = new JPanel();
        mainPanel = new JPanel();
        menuBar = new JPanel();
        var label1 = new JLabel();
        var separator1 = new JSeparator();
        var separator2 = new JSeparator();
        var label2 = new JLabel();
        sidePanel = new JPanel();
        homeContainer = new JPanel();
        homePageButton = new JButton();
        featuresContainer = new JPanel();
        manageEmployeesButton = new JButton();
        leaveButton = new JButton();
        manageSalaryButton = new JButton();
        attendanceButton = new JButton();
        welcomeContainer = new JPanel();
        var label3 = new JLabel();
        logoutContainer = new JPanel();
        logoutButton = new JButton();
        m_contentPanel = new JPanel();

        //======== homePanel ========
        {
            homePanel.setMinimumSize(new Dimension(1350, 800));
            homePanel.setPreferredSize(new Dimension(1350, 800));
            homePanel.setLayout(new BorderLayout());

            //======== mainPanel ========
            {
                mainPanel.setBackground(Color.white);
                mainPanel.setDoubleBuffered(false);
                mainPanel.setEnabled(false);
                mainPanel.setFocusable(false);
                mainPanel.setForeground(Color.white);
                mainPanel.setMaximumSize(new Dimension(-1, -1));
                mainPanel.setMinimumSize(new Dimension(1350, 800));
                mainPanel.setOpaque(false);
                mainPanel.setPreferredSize(new Dimension(1350, 800));
                mainPanel.setRequestFocusEnabled(false);
                mainPanel.setVerifyInputWhenFocusTarget(false);
                mainPanel.setVisible(true);
                mainPanel.setLayout(new BorderLayout());

                //======== menuBar ========
                {
                    menuBar.setAlignmentX(0.0F);
                    menuBar.setBackground(new Color(0xe63946));
                    menuBar.setDoubleBuffered(false);
                    menuBar.setEnabled(false);
                    menuBar.setFocusable(false);
                    menuBar.setMinimumSize(new Dimension(0, 0));
                    menuBar.setPreferredSize(new Dimension(10, 85));
                    menuBar.setLayout(new FlowLayout(FlowLayout.LEADING, 10, 12));

                    //---- label1 ----
                    label1.setAlignmentX(0.5F);
                    label1.setForeground(Color.white);
                    label1.setHorizontalAlignment(SwingConstants.TRAILING);
                    label1.setHorizontalTextPosition(SwingConstants.TRAILING);
                    label1.setIcon(new ImageIcon(new ImageIcon("src/resources/images/icons/icons8_menu_48px_1.png").getImage()));
                    label1.setIconTextGap(0);
                    label1.setInheritsPopupMenu(true);
                    label1.setMaximumSize(new Dimension(40, 40));
                    label1.setMinimumSize(new Dimension(40, 40));
                    label1.setPreferredSize(new Dimension(40, 40));
                    label1.setText("");
                    label1.setVerifyInputWhenFocusTarget(true);
                    menuBar.add(label1);

                    //---- separator1 ----
                    separator1.setEnabled(false);
                    separator1.setForeground(Color.white);
                    separator1.setOpaque(true);
                    separator1.setOrientation(SwingConstants.VERTICAL);
                    separator1.setPreferredSize(new Dimension(2, 45));
                    separator1.setRequestFocusEnabled(false);
                    menuBar.add(separator1);

                    //---- separator2 ----
                    separator2.setPreferredSize(new Dimension(10, 0));
                    menuBar.add(separator2);

                    //---- label2 ----
                    label2.setFont(new Font("Arial Black", label2.getFont().getStyle(), 22));
                    label2.setForeground(Color.white);
                    label2.setHorizontalAlignment(SwingConstants.CENTER);
                    label2.setHorizontalTextPosition(SwingConstants.CENTER);
                    label2.setText("Payroll System");
                    menuBar.add(label2);
                }
                mainPanel.add(menuBar, BorderLayout.NORTH);

                //======== sidePanel ========
                {
                    sidePanel.setAutoscrolls(false);
                    sidePanel.setBackground(new Color(0x1d3557));
                    sidePanel.setMinimumSize(new Dimension(200, 121));
                    sidePanel.setPreferredSize(new Dimension(240, 0));
                    sidePanel.setLayout(new GridBagLayout());

                    //======== homeContainer ========
                    {
                        homeContainer.setBackground(new Color(0xe63946));
                        homeContainer.setPreferredSize(new Dimension(200, 75));
                        homeContainer.setVisible(true);
                        homeContainer.setLayout(new BorderLayout());

                        //---- homePageButton ----
                        homePageButton.setBorderPainted(true);
                        homePageButton.setContentAreaFilled(false);
                        homePageButton.setFocusable(false);
                        homePageButton.setForeground(Color.white);
                        homePageButton.setHorizontalAlignment(SwingConstants.CENTER);
                        homePageButton.setIcon(new ImageIcon(new ImageIcon("src/resources/images/icons/icons8-home-64.png").getImage()));
                        homePageButton.setText("HomePage");
                        homeContainer.add(homePageButton, BorderLayout.NORTH);
                    }
                    sidePanel.add(homeContainer, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                        GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                        new Insets(0, 0, 0, 0), 0, 0));

                    //======== featuresContainer ========
                    {
                        featuresContainer.setBackground(new Color(0x1d3557));
                        featuresContainer.setMinimumSize(new Dimension(200, 50));
                        featuresContainer.setPreferredSize(new Dimension(200, 215));
                        featuresContainer.setLayout(new GridBagLayout());

                        //---- manageEmployeesButton ----
                        manageEmployeesButton.setAlignmentX(1.0F);
                        manageEmployeesButton.setBackground(new Color(0x1d3557));
                        manageEmployeesButton.setBorderPainted(false);
                        manageEmployeesButton.setContentAreaFilled(false);
                        manageEmployeesButton.setFocusable(false);
                        manageEmployeesButton.setForeground(Color.white);
                        manageEmployeesButton.setHorizontalAlignment(SwingConstants.LEFT);
                        manageEmployeesButton.setIcon(new ImageIcon(new ImageIcon("src/resources/images/icons/icons8-staff-50.png").getImage()));
                        manageEmployeesButton.setMargin(new Insets(0, 10, 0, 0));
                        manageEmployeesButton.setOpaque(true);
                        manageEmployeesButton.setText("Manage Employees");
                        featuresContainer.add(manageEmployeesButton, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0,
                            GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 0, 0, 0), 0, 0));

                        //---- manageSalaryButton ----
                        manageSalaryButton.setBackground(new Color(0x1d3557));
                        manageSalaryButton.setBorderPainted(false);
                        manageSalaryButton.setContentAreaFilled(false);
                        manageSalaryButton.setFocusable(false);
                        manageSalaryButton.setForeground(Color.white);
                        manageSalaryButton.setHorizontalAlignment(SwingConstants.LEFT);
                        manageSalaryButton.setIcon(new ImageIcon(new ImageIcon("src/resources/images/icons/icons8-receive-dollar-50.png").getImage()));
                        manageSalaryButton.setMargin(new Insets(0, 10, 0, 0));
                        manageSalaryButton.setOpaque(true);
                        manageSalaryButton.setText("Payroll");
                        featuresContainer.add(manageSalaryButton, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                            GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 0, 0, 0), 0, 0));

                        //---- viewAttendanceRecordsButton ----
                        attendanceButton.setBackground(new Color(0x1d3557));
                        attendanceButton.setBorderPainted(false);
                        attendanceButton.setContentAreaFilled(false);
                        attendanceButton.setFocusable(false);
                        attendanceButton.setForeground(Color.white);
                        attendanceButton.setHorizontalAlignment(SwingConstants.LEFT);
                        attendanceButton.setIcon(new ImageIcon(new ImageIcon("src/resources/images/icons/icons8-attendance-48.png").getImage()));
                        attendanceButton.setMargin(new Insets(0, 10, 0, 0));
                        attendanceButton.setOpaque(true);
                        attendanceButton.setText("Attendance Records");
                        featuresContainer.add(attendanceButton, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
                            GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 0, 0, 0), 0, 0));

                        //---- Leave Application Button ----
                        leaveButton.setBackground(new Color(0x1d3557));
                        leaveButton.setBorderPainted(false);
                        leaveButton.setContentAreaFilled(false);
                        leaveButton.setFocusable(false);
                        leaveButton.setForeground(Color.white);
                        leaveButton.setHorizontalAlignment(SwingConstants.LEFT);
                        leaveButton.setIcon(new ImageIcon(new ImageIcon("src/resources/images/icons/icons8-leave-50.png").getImage()));
                        leaveButton.setMargin(new Insets(0, 10, 0, 0));
                        leaveButton.setOpaque(true);
                        leaveButton.setText("Leave Application");
                        featuresContainer.add(leaveButton, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0,
                                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                                new Insets(0, 0, 0, 0), 0, 0));
                    }
                    sidePanel.add(featuresContainer, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
                        GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                        new Insets(0, 0, 0, 0), 0, 0));

                    //======== welcomeContainer ========
                    {
                        welcomeContainer.setBackground(new Color(0x1d3557));
                        welcomeContainer.setLayout(new GridBagLayout());

                        //---- label3 ----
                        label3.setForeground(Color.white);
                        label3.setHorizontalAlignment(SwingConstants.CENTER);
                        label3.setHorizontalTextPosition(SwingConstants.CENTER);
                        label3.setIcon(new ImageIcon(new ImageIcon("src/resources/images/icons/icons8-user-64.png").getImage()));
                        label3.setInheritsPopupMenu(true);
                        label3.setText("Welcome, Admin");
                        label3.setVerticalAlignment(SwingConstants.CENTER);
                        label3.setVerticalTextPosition(SwingConstants.BOTTOM);
                        welcomeContainer.add(label3, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
                            GridBagConstraints.CENTER, GridBagConstraints.NONE,
                            new Insets(0, 0, 0, 0), 0, 0));
                    }
                    sidePanel.add(welcomeContainer, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 0, 0), 0, 0));

                    //======== logoutContainer ========
                    {
                        logoutContainer.setBackground(new Color(0x1d3557));
                        logoutContainer.setLayout(new GridBagLayout());

                        //---- logoutButton ----
                        logoutButton.setAutoscrolls(false);
                        logoutButton.setBorderPainted(false);
                        logoutButton.setContentAreaFilled(false);
                        logoutButton.setFocusable(false);
                        logoutButton.setForeground(Color.white);
                        logoutButton.setHorizontalAlignment(SwingConstants.CENTER);
                        logoutButton.setHorizontalTextPosition(SwingConstants.TRAILING);
                        logoutButton.setIcon(new ImageIcon(new ImageIcon("src/resources/images/icons/icons8_Exit_26px_2.png").getImage()));
                        logoutButton.setText("Logout");
                        logoutButton.setVerticalAlignment(SwingConstants.CENTER);
                        logoutButton.setVerticalTextPosition(SwingConstants.CENTER);
                        logoutContainer.add(logoutButton, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
                            GridBagConstraints.CENTER, GridBagConstraints.NONE,
                            new Insets(0, 0, 0, 0), 0, 0));
                    }
                    sidePanel.add(logoutContainer, new GridBagConstraints(0, 3, 1, 1, 1.0, 1.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 0, 0), 0, 0));
                }
                mainPanel.add(sidePanel, BorderLayout.WEST);

                //======== m_contentPanel ========
                {
                    m_contentPanel.setBackground(new Color(0xf1faee));
                    m_contentPanel.setLayout(new BorderLayout());
                }
                mainPanel.add(m_contentPanel, BorderLayout.CENTER);
            }
            homePanel.add(mainPanel, BorderLayout.NORTH);
        }
    }
}





