import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Main extends JFrame implements ActionListener {
    private JTextField idField, lastNameField, firstNameField, phoneField;
    private JButton previousButton, nextButton;
    private Connection con;
    private Statement stmt;
    private ResultSet rs;

    public Main() {
        // Initialize the database connection
        initializeDB();

        setTitle("Customer");
        setLayout(new GridLayout(5, 2, 10, 10));

        // ID
        add(new JLabel("ID:"));
        idField = new JTextField();
        idField.setEditable(false);
        add(idField);

        // Last Name
        add(new JLabel("Last Name:"));
        lastNameField = new JTextField();
        lastNameField.setEditable(false);
        add(lastNameField);

        // First Name
        add(new JLabel("First Name:"));
        firstNameField = new JTextField();
        firstNameField.setEditable(false);
        add(firstNameField);

        // Phone
        add(new JLabel("Phone:"));
        phoneField = new JTextField();
        phoneField.setEditable(false);
        add(phoneField);

        // Buttons
        previousButton = new JButton("Previous");
        previousButton.addActionListener(this);
        add(previousButton);

        nextButton = new JButton("Next");
        nextButton.addActionListener(this);
        add(nextButton);

        // Frame settings
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        // Display the first record
        displayFirstRecord();
    }

    private void initializeDB() {
        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost:3307/testDB", "root", "");
            stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            String query = "SELECT * FROM tbCustomer";
            rs = stmt.executeQuery(query);
            if (rs.next()) {
                displayRecord();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void displayRecord() {
        try {
            idField.setText(String.valueOf(rs.getInt("customer_id")));
            lastNameField.setText(rs.getString("customer_last_name"));
            firstNameField.setText(rs.getString("customer_first_name"));
            phoneField.setText(rs.getString("customer_phone"));
//            System.out.println("Connection success");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void displayFirstRecord() {
        try {
            rs.first();
            displayRecord();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            if (e.getSource() == previousButton) {
                if (rs.previous()) {
                    displayRecord();
                } else {
                    rs.next();
                }
            } else if (e.getSource() == nextButton) {
                if (rs.next()) {
                    displayRecord();
                } else {
                    rs.previous();
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Main();
    }
}