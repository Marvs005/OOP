import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
public class AttendanceGUI extends JFrame {
    private JLabel monthLabel, hoursLabel, daysLabel, latesLabel, absencesLabel;
    private JTextField monthField, hoursField, daysField, latesField, absencesField;
    private JButton viewAttendanceButton;

    public AttendanceGUI() {
        setTitle(" Employee Attendance Tracker");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);

        monthLabel = new JLabel("Work Month:");
        monthLabel.setBounds(50, 50, 100, 30);
        add(monthLabel);

        monthField = new JTextField();
        monthField.setBounds(160, 50, 150, 30);
        add(monthField);

        hoursLabel = new JLabel("Work Hours:");
        hoursLabel.setBounds(50, 90, 100, 30);
        add(hoursLabel);

        hoursField = new JTextField();
        hoursField.setBounds(160, 90, 150, 30);
        add(hoursField);

        daysLabel = new JLabel("Work Days:");
        daysLabel.setBounds(50, 130, 100, 30);
        add(daysLabel);

        daysField = new JTextField();
        daysField.setBounds(160, 130, 150, 30);
        add(daysField);

        latesLabel = new JLabel("Lates:");
        latesLabel.setBounds(50, 170, 100, 30);
        add(latesLabel);

        latesField = new JTextField();
        latesField.setBounds(160, 170, 150, 30);
        add(latesField);

        absencesLabel = new JLabel("Absences:");
        absencesLabel.setBounds(50, 210, 100, 30);
        add(absencesLabel);

        absencesField = new JTextField();
        absencesField.setBounds(160, 210, 150, 30);
        add(absencesField);

        viewAttendanceButton = new JButton("View Attendance");
        viewAttendanceButton.setBounds(120, 250, 150, 30);
        add(viewAttendanceButton);

        viewAttendanceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Add functionality to view attendance here
                JOptionPane.showMessageDialog(null, "Viewing Attendance...");
            }
        });

        setVisible(true);
    }

    public static void main(String[] args) {
        new AttendanceGUI();

    }
}
