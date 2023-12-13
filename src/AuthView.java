import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

import javax.swing.*;

public class AuthView {
    private final Color PrimaryColor = Color.WHITE;
    private final Color OnPrimaryColor = Color.BLACK;
    private final String FontName = "Arial";

    private Map<String, String> _users = new HashMap<>();

    private JFrame frame;
    private JTabbedPane tabbedPane;
    private JTextField loginUsernameField, registerUsernameField;
    private JPasswordField loginPasswordField, registerPasswordField, registerConfirmPasswordField;
    private JButton loginButton, registerButton;
    private JLabel loginMessageLabel, registerMessageLabel;

    public AuthView() {
        new Thread(() -> {
            loadUsers();
        }).start();
        initializeUI();
        setupListeners();
    }

    private void initializeUI() {
        frame = new JFrame("Hmail Login");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setSize(400, 260);

        tabbedPane = new JTabbedPane();

        // Login Panel
        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(new BorderLayout());

        JLabel loginLabel = new JLabel("LOGIN");
        loginLabel.setOpaque(true);
        loginLabel.setBackground(PrimaryColor);
        loginLabel.setForeground(OnPrimaryColor);
        loginLabel.setFont(new Font(FontName, Font.BOLD, 20));
        loginLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        loginLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel loginUsernameLabel = new JLabel("Username:");
        loginUsernameLabel.setFont(new Font(FontName, Font.BOLD, 13));
        loginUsernameLabel.setForeground(OnPrimaryColor);
        loginUsernameLabel.setBackground(PrimaryColor);
        loginUsernameLabel.setPreferredSize(new Dimension(120, 24));

        JLabel loginPasswordLabel = new JLabel("Password:");
        loginPasswordLabel.setFont(new Font(FontName, Font.BOLD, 13));
        loginPasswordLabel.setForeground(OnPrimaryColor);
        loginPasswordLabel.setBackground(PrimaryColor);
        loginPasswordLabel.setPreferredSize(new Dimension(120, 24));

        loginUsernameField = new JTextField();
        loginUsernameField.setBackground(PrimaryColor);
        loginUsernameField.setForeground(OnPrimaryColor);
        loginUsernameField.setCaretColor(OnPrimaryColor);
        loginUsernameField.setPreferredSize(new Dimension(230, 24));

        loginPasswordField = new JPasswordField();
        loginPasswordField.setBackground(PrimaryColor);
        loginPasswordField.setForeground(OnPrimaryColor);
        loginPasswordField.setCaretColor(OnPrimaryColor);
        loginPasswordField.setPreferredSize(new Dimension(230, 24));

        loginMessageLabel = new JLabel();
        loginMessageLabel.setFont(new Font(FontName, Font.ITALIC, 12));
        loginMessageLabel.setForeground(Color.RED);
        loginMessageLabel.setBackground(PrimaryColor);
        loginMessageLabel.setPreferredSize(new Dimension(360, 24));

        loginButton = new JButton("Login");
        loginButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        loginButton.setFocusPainted(false);
        JPanel loginButtonPanel = new JPanel(new FlowLayout());
        loginButtonPanel.setBackground(PrimaryColor);
        loginButtonPanel.add(loginButton);

        JPanel loginFormPanel = new JPanel();
        loginFormPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        loginFormPanel.setBackground(PrimaryColor);
        loginFormPanel.add(loginUsernameLabel);
        loginFormPanel.add(loginUsernameField);
        loginFormPanel.add(loginPasswordLabel);
        loginFormPanel.add(loginPasswordField);
        loginFormPanel.add(loginMessageLabel);

        loginPanel.add(loginLabel, BorderLayout.NORTH);
        loginPanel.add(loginFormPanel, BorderLayout.CENTER);
        loginPanel.add(loginButtonPanel, BorderLayout.SOUTH);

        // Register Panel
        JPanel registerPanel = new JPanel();
        registerPanel.setLayout(new BorderLayout());

        JLabel registerLabel = new JLabel("REGISTER");
        registerLabel.setOpaque(true);
        registerLabel.setBackground(PrimaryColor);
        registerLabel.setForeground(OnPrimaryColor);
        registerLabel.setFont(new Font(FontName, Font.BOLD, 20));
        registerLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        registerLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel registerUsernameLabel = new JLabel("Username:");
        registerUsernameLabel.setFont(new Font(FontName, Font.BOLD, 13));
        registerUsernameLabel.setForeground(OnPrimaryColor);
        registerUsernameLabel.setBackground(PrimaryColor);
        registerUsernameLabel.setPreferredSize(new Dimension(120, 24));

        JLabel registerPasswordLabel = new JLabel("Password:");
        registerPasswordLabel.setFont(new Font(FontName, Font.BOLD, 13));
        registerPasswordLabel.setForeground(OnPrimaryColor);
        registerPasswordLabel.setBackground(PrimaryColor);
        registerPasswordLabel.setPreferredSize(new Dimension(120, 24));

        JLabel registerConfirmPasswordLabel = new JLabel("Confirm password:");
        registerConfirmPasswordLabel.setFont(new Font(FontName, Font.BOLD, 13));
        registerConfirmPasswordLabel.setForeground(OnPrimaryColor);
        registerConfirmPasswordLabel.setBackground(PrimaryColor);
        registerConfirmPasswordLabel.setPreferredSize(new Dimension(120, 24));

        registerUsernameField = new JTextField();
        registerUsernameField.setBackground(PrimaryColor);
        registerUsernameField.setForeground(OnPrimaryColor);
        registerUsernameField.setCaretColor(OnPrimaryColor);
        registerUsernameField.setPreferredSize(new Dimension(230, 24));

        registerPasswordField = new JPasswordField();
        registerPasswordField.setBackground(PrimaryColor);
        registerPasswordField.setForeground(OnPrimaryColor);
        registerPasswordField.setCaretColor(OnPrimaryColor);
        registerPasswordField.setPreferredSize(new Dimension(230, 24));

        registerConfirmPasswordField = new JPasswordField();
        registerConfirmPasswordField.setBackground(PrimaryColor);
        registerConfirmPasswordField.setForeground(OnPrimaryColor);
        registerConfirmPasswordField.setCaretColor(OnPrimaryColor);
        registerConfirmPasswordField.setPreferredSize(new Dimension(230, 24));

        registerMessageLabel = new JLabel();
        registerMessageLabel.setFont(new Font(FontName, Font.ITALIC, 12));
        registerMessageLabel.setForeground(Color.RED);
        registerMessageLabel.setBackground(PrimaryColor);
        registerMessageLabel.setPreferredSize(new Dimension(360, 24));

        registerButton = new JButton("Register");
        registerButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        registerButton.setFocusPainted(false);
        JPanel registerButtonPanel = new JPanel(new FlowLayout());
        registerButtonPanel.setBackground(PrimaryColor);
        registerButtonPanel.add(registerButton);

        JPanel registerFormPanel = new JPanel();
        registerFormPanel.setBackground(PrimaryColor);
        registerFormPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        registerFormPanel.add(registerUsernameLabel);
        registerFormPanel.add(registerUsernameField);
        registerFormPanel.add(registerPasswordLabel);
        registerFormPanel.add(registerPasswordField);
        registerFormPanel.add(registerConfirmPasswordLabel);
        registerFormPanel.add(registerConfirmPasswordField);
        registerFormPanel.add(registerMessageLabel);

        registerPanel.add(registerLabel, BorderLayout.NORTH);
        registerPanel.add(registerFormPanel, BorderLayout.CENTER);
        registerPanel.add(registerButtonPanel, BorderLayout.SOUTH);

        // Add login & register panel into tabbed panel
        tabbedPane.addTab("Login", loginPanel);
        tabbedPane.addTab("Register", registerPanel);

        // Add tabbed panel into frame
        frame.add(tabbedPane);
    }

    private void setupListeners() {
        loginButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String username = loginUsernameField.getText();
                String password = new String(loginPasswordField.getPassword());
                loginUser(username, password);
            }
            
        });

        registerButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String username = registerUsernameField.getText();
                String password = new String(registerPasswordField.getPassword());
                String confirmPassword = new String(registerConfirmPasswordField.getPassword());
                registerUser(username, password, confirmPassword);
            }
            
        });
    }

    public void show() {
        SwingUtilities.invokeLater(() -> {
            frame.setVisible(true);
        });
    }

    private void showMessage(String message, boolean type) {
        if (type) {
            loginMessageLabel.setText(message);
        } else {
            registerMessageLabel.setText(message);
        }
    }

    private boolean isValidUsername(String username) {
        return username.matches("^[a-zA-Z][a-zA-Z0-9]+$") && username.length() > 1 && username.length() < 51;
    }

    private boolean isValidPassword(String password) {
        return password.length() > 1 && password.length() < 51;
    }

    private void loadUsers() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("./.data/users.txt"));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    String username = parts[0];
                    String password = parts[1];
                    _users.put(username, password);
                }
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void insertUser(String username, String password) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("./.data/users.txt", true));
            writer.write(username + "," + password);
            writer.newLine();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean checkExistkUser(String username) {
        return _users.containsKey(username);
    }

    private boolean checkLogin(String username, String password) {
        return checkExistkUser(username) && _users.get(username).equals(password);
    }

    private void registerUser(String username, String password, String confirmPassword) {
        if (!isValidUsername(username)) {
            showMessage("Invalid username, username only includes letters and numbers", false);
            return;
        }

        if (!isValidPassword(password)) {
            showMessage("Invalid password, password has at least 2 characters", false);
            return;
        }
        
        if (!password.equals(confirmPassword)) {
            showMessage("Confirm password does not match password", false);
            return;
        }

        if (checkExistkUser(username)) {
            showMessage("Username has been registered, please change to another name", false);
            return;
        }

        clearField();
        JOptionPane.showMessageDialog(frame, "Register Successfully! Please login again.", "", JOptionPane.INFORMATION_MESSAGE);
        tabbedPane.setSelectedIndex(0);
        insertUser(username, password);
        _users.put(username, password);
    }

    private void loginUser(String username, String password) {
        if (!isValidUsername(username)) {
            showMessage("Invalid username, username only includes letters and numbers", true);
            return;
        }

        if (!isValidPassword(password)) {
            showMessage("Invalid password, password has at least 2 characters", true);
            return;
        }

        if (!checkLogin(username, password)) {
            showMessage("Username or password not corrected, please try again", true);
            return;
        }

        // JOptionPane.showMessageDialog(frame, "Login Successfully!", "", JOptionPane.INFORMATION_MESSAGE);
        showMainFrame();
    }

    private void clearField() {
        loginUsernameField.setText("");
        loginPasswordField.setText("");
        registerUsernameField.setText("");
        registerPasswordField.setText("");
        registerConfirmPasswordField.setText("");
    }

    private void showMainFrame() {
        MainView mainView = new MainView();
        mainView.show();
        frame.setVisible(false);
    }
}
