package main.java.frontend.Login;

import main.java.frontend.HomePage;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import javax.swing.*;
import java.awt.*;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;



public class LoginGUI {
    public JPanel mainPanel, topPanel, contentPanel, loginPanel, logoPanel;
    private JLabel motorPHPayrollSystemLabel, usernameLabel, passwordLabel, logoLabel, titleLabel;
    private JTextField usernameField;
    private JPasswordField passwordField1;
    private JButton loginButton, signInButton;
    private static String employeeID;
    private String password;
    public static JFrame frame;

    public LoginGUI() {
        initComponents();
        actions();
    }

    private void initComponents() {
        mainPanel = new JPanel();
        topPanel = new JPanel();
        motorPHPayrollSystemLabel = new JLabel();
        contentPanel = new JPanel();
        loginPanel = new JPanel();
        usernameField = new JTextField();
        passwordLabel = new JLabel();
        passwordField1 = new JPasswordField();
        usernameLabel = new JLabel();
        loginButton = new JButton();
        signInButton = new JButton();
        logoPanel = new JPanel();
        titleLabel = new JLabel();
        logoLabel = new JLabel();

        //======== mainPanel ========
        {
            mainPanel.setBackground(new Color(0x1d3557));
            mainPanel.setMaximumSize(new Dimension(700, 250));
            mainPanel.setMinimumSize(new Dimension(600, 250));
            mainPanel.setPreferredSize(new Dimension(600, 250));
            mainPanel.setLayout(new BorderLayout());

            //======== topPanel ========
            {
                topPanel.setBackground(new Color(0xe63946));
                topPanel.setMaximumSize(new Dimension(-1, -1));
                topPanel.setMinimumSize(new Dimension(-1, -1));
                topPanel.setPreferredSize(new Dimension(600, 43));
                topPanel.setLayout(new FlowLayout());

                //---- motorPHPayrollSystemLabel ----
                motorPHPayrollSystemLabel.setFont(new Font("Arial Black", Font.BOLD, 20));
                motorPHPayrollSystemLabel.setForeground(new Color(0xf1faee));
                motorPHPayrollSystemLabel.setText("MotorPH Payroll System");
                topPanel.add(motorPHPayrollSystemLabel);
            }
            mainPanel.add(topPanel, BorderLayout.NORTH);

            //======== contentPanel ========
            {
                contentPanel.setEnabled(false);
                contentPanel.setMaximumSize(new Dimension(-1, -1));
                contentPanel.setMinimumSize(new Dimension(592, 207));
                contentPanel.setPreferredSize(new Dimension(592, 207));
                contentPanel.setLayout(new GridBagLayout());

                //======== loginPanel ========
                {
                    loginPanel.setEnabled(false);
                    loginPanel.setLayout(new GridBagLayout());

                    //---- usernameField ----
                    usernameField.setColumns(15);
                    loginPanel.add(usernameField, new GridBagConstraints(1, 0, 2, 1, 1.0, 1.0,
                        GridBagConstraints.WEST, GridBagConstraints.NONE,
                        new Insets(5, 5, 5, 5), 0, 0));

                    //---- passwordLabel ----
                    passwordLabel.setText("Password");
                    loginPanel.add(passwordLabel, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
                        GridBagConstraints.EAST, GridBagConstraints.NONE,
                        new Insets(5, 5, 5, 5), 0, 0));

                    //---- passwordField1 ----
                    passwordField1.setColumns(15);
                    loginPanel.add(passwordField1, new GridBagConstraints(1, 1, 2, 1, 1.0, 1.0,
                        GridBagConstraints.WEST, GridBagConstraints.NONE,
                        new Insets(5, 5, 5, 5), 0, 0));

                    //---- usernameLabel ----
                    usernameLabel.setHorizontalAlignment(SwingConstants.CENTER);
                    usernameLabel.setHorizontalTextPosition(SwingConstants.CENTER);
                    usernameLabel.setText("Username");
                    loginPanel.add(usernameLabel, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
                        GridBagConstraints.EAST, GridBagConstraints.NONE,
                        new Insets(5, 5, 5, 5), 0, 0));

                    //---- loginButton ----
                    loginButton.setText("Login");
                    loginPanel.add(loginButton, new GridBagConstraints(1, 2, 1, 1, 1.0, 1.0,
                        GridBagConstraints.WEST, GridBagConstraints.NONE,
                        new Insets(5, 5, 5, 5), 0, 0));

                    //---- signInButton ----
                    signInButton.setText("Sign In");
                    loginPanel.add(signInButton, new GridBagConstraints(2, 2, 1, 1, 1.0, 1.0,
                        GridBagConstraints.WEST, GridBagConstraints.NONE,
                        new Insets(5, 5, 5, 5), 0, 0));
                }
                contentPanel.add(loginPanel, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
                    GridBagConstraints.CENTER, GridBagConstraints.NONE,
                    new Insets(0, 0, 0, 0), 0, 0));

                //======== logoPanel ========
                {
                    logoPanel.setLayout(new GridBagLayout());

                    //---- logoLabel ----
                    logoLabel.setText("");
                    ImageIcon logoIcon = new ImageIcon(new ImageIcon("src/resources/images/motorcycles-icon-png-2710.png").getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH));
                    logoLabel.setIcon(logoIcon);
                    logoPanel.add(logoLabel, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
                        GridBagConstraints.CENTER, GridBagConstraints.VERTICAL,
                        new Insets(10, 0, 0, 0), 0, 0));

                    //---- titleLabel ----
                    titleLabel.setEnabled(false);
                    titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
                    titleLabel.setHorizontalTextPosition(SwingConstants.CENTER);
                    titleLabel.setText("Welcome to MotorPH");
                    logoPanel.add(titleLabel, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(5, 0, 0, 0), 0, 0));
                }
                contentPanel.add(logoPanel, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
                    GridBagConstraints.CENTER, GridBagConstraints.NONE,
                    new Insets(0, 0, 0, 0), 0, 0));
            }
            mainPanel.add(contentPanel, BorderLayout.CENTER);
        }
    }
    private HashMap<String, String> readLoginDataFromCSV() {
        HashMap<String, String> loginInfo = new HashMap<>();

        try (CSVReader reader = new CSVReader(new FileReader("src/resources/data/IDandPass.csv"))) {
            // Skip the header row
            reader.readNext();

            String[] row;
            while ((row = reader.readNext()) != null) {
                String employeeID = row[2];
                String password = row[3];

                loginInfo.put(employeeID, password);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CsvValidationException e) {
            throw new RuntimeException(e);
        }
        return loginInfo;
    }
    public void actions(){
        // Read the CSV file and populate the loginInfo HashMap
        HashMap<String, String> loginInfo = readLoginDataFromCSV();

        loginButton.addActionListener(e -> {
            employeeID = usernameField.getText();
            password = String.valueOf(passwordField1.getPassword());

            if (loginInfo.containsKey(employeeID)) {
                if (loginInfo.get(employeeID).equals(password)) {
                    frame.dispose();
                    HomePage.main(null);

                } else {
                    JOptionPane.showMessageDialog(frame, "Invalid username or password", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Invalid username or password", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        signInButton.addActionListener(e -> {
            Registration.main(null);
        });
    }

    public static void main(String[] args) {
        frame = new JFrame("LoginGUI");
        frame.setContentPane(new LoginGUI().mainPanel);
        frame.setPreferredSize(new Dimension(600, 400));
        frame.setMinimumSize(new Dimension(600, 400));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.pack();
        frame.setVisible(true);
    }
}
