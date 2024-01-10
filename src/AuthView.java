import java.awt.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.List;
import javax.swing.*;

public class AuthView {
    private final Color PrimaryColor = Color.WHITE;
    private final Color OnPrimaryColor = Color.BLACK;
    private final String FontName = "Arial";

    private List<UserModel> _users = new ArrayList<>();
    private List<EmailModel> _emails = new ArrayList<>();
    private UserModel _user = new UserModel();
    private ConfigModel _config = new ConfigModel();

    private JFrame frame;
    private JTabbedPane tabbedPane;
    private JTextField loginEmailField, registerEmailField, registerFullnameField;
    private JPasswordField loginPasswordField, registerPasswordField, registerConfirmPasswordField;
    private JButton loginButton, registerButton;
    private JLabel loginMessageLabel, registerMessageLabel;

    public AuthView() {

    }

    private void initializeUI() {
        frame = new JFrame("Authenication");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setSize(400, 300);

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

        JLabel loginEmailLabel = new JLabel("Email:");
        loginEmailLabel.setFont(new Font(FontName, Font.BOLD, 13));
        loginEmailLabel.setForeground(OnPrimaryColor);
        loginEmailLabel.setBackground(PrimaryColor);
        loginEmailLabel.setPreferredSize(new Dimension(120, 24));

        JLabel loginPasswordLabel = new JLabel("Password:");
        loginPasswordLabel.setFont(new Font(FontName, Font.BOLD, 13));
        loginPasswordLabel.setForeground(OnPrimaryColor);
        loginPasswordLabel.setBackground(PrimaryColor);
        loginPasswordLabel.setPreferredSize(new Dimension(120, 24));

        loginEmailField = new JTextField();
        loginEmailField.setBackground(PrimaryColor);
        loginEmailField.setForeground(OnPrimaryColor);
        loginEmailField.setCaretColor(OnPrimaryColor);
        loginEmailField.setPreferredSize(new Dimension(230, 24));

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
        loginFormPanel.add(loginEmailLabel);
        loginFormPanel.add(loginEmailField);
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

        JLabel registerFullnameLabel = new JLabel("Fullname:");
        registerFullnameLabel.setFont(new Font(FontName, Font.BOLD, 13));
        registerFullnameLabel.setForeground(OnPrimaryColor);
        registerFullnameLabel.setBackground(PrimaryColor);
        registerFullnameLabel.setPreferredSize(new Dimension(120, 24));

        JLabel registerEmailLabel = new JLabel("Email:");
        registerEmailLabel.setFont(new Font(FontName, Font.BOLD, 13));
        registerEmailLabel.setForeground(OnPrimaryColor);
        registerEmailLabel.setBackground(PrimaryColor);
        registerEmailLabel.setPreferredSize(new Dimension(120, 24));

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

        registerEmailField = new JTextField();
        registerEmailField.setBackground(PrimaryColor);
        registerEmailField.setForeground(OnPrimaryColor);
        registerEmailField.setCaretColor(OnPrimaryColor);
        registerEmailField.setPreferredSize(new Dimension(230, 24));

        registerFullnameField = new JTextField();
        registerFullnameField.setBackground(PrimaryColor);
        registerFullnameField.setForeground(OnPrimaryColor);
        registerFullnameField.setCaretColor(OnPrimaryColor);
        registerFullnameField.setPreferredSize(new Dimension(230, 24));

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
        registerFormPanel.add(registerFullnameLabel);
        registerFormPanel.add(registerFullnameField);
        registerFormPanel.add(registerEmailLabel);
        registerFormPanel.add(registerEmailField);
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
        frame.setVisible(true);
    }
    
    private void setupListeners() {
        loginButton.addActionListener(e -> {
            String email = loginEmailField.getText().trim();
            String password = new String(loginPasswordField.getPassword());
            loginUser(email, password);
        });

        registerButton.addActionListener(e -> {
            String email = registerEmailField.getText().trim();
            String fullname = registerFullnameField.getText().trim();
            String password = new String(registerPasswordField.getPassword());
            String confirmPassword = new String(registerConfirmPasswordField.getPassword());
            registerUser(email, password, confirmPassword, fullname);
        });
    }

    public void show() {
        SwingUtilities.invokeLater(() -> {
            new Thread(() -> {
                loadUsers();
            }).start();
            initializeUI();
            setupListeners();
        });
    }

    private void showMessage(String message, boolean type) {
        if (type) {
            loginMessageLabel.setText(message);
        } else {
            registerMessageLabel.setText(message);
        }
    }

    private boolean isValidEmail(String email) {
        return email.matches("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$");
    }

    private boolean isValidPassword(String password) {
        return password.length() > 2;
    }

    private boolean isValidFullname(String fullname) {
        return fullname.matches("^[a-zA-Z\\s]+$") && fullname.length() > 1;
    }

    private void loadUsers() {
        try {
            Path directoryPath = Paths.get(Helper.jarPath, "data");
            if (!Files.exists(directoryPath)) {
                Files.createDirectories(directoryPath);
            }

            Path filePath = Paths.get(directoryPath.toString(), "users.txt");

            if (!Files.exists(filePath)) {
                Files.createFile(filePath);
            }

            BufferedReader reader = new BufferedReader(new FileReader(filePath.toString()));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    String email = parts[0];
                    String password = parts[1];
                    String fullname = parts[2];
                    _users.add(new UserModel(email, password, fullname));
                }
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void insertUser(String email, String password, String fullname) {
        try {
            Path directoryPath = Paths.get(Helper.jarPath, "data");
            if (!Files.exists(directoryPath)) {
                Files.createDirectories(directoryPath);
            }

            Path filePath = Paths.get(directoryPath.toString(), "users.txt");

            if (!Files.exists(filePath)) {
                Files.createFile(filePath);
            }

            BufferedWriter writer = new BufferedWriter(new FileWriter(filePath.toString(), true));
            writer.write(email + "," + password + "," + fullname);
            writer.newLine();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean checkExistUser(String email) {
        return _users.stream().anyMatch(user -> user.getEmail().equals(email));
    }

    private boolean checkLogin(String email, String password) {
        return _users.stream()
                .anyMatch(user -> user.getEmail().equals(email) && user.getPassword().equals(password));
    }

    private void registerUser(String email, String password, String confirmPassword, String fullname) {
        if (!isValidFullname(fullname)) {
            showMessage("Invalid fullname, fullname contains only letters and spaces", false);
            return;
        }

        if (!isValidEmail(email)) {
            showMessage("Invalid email, email must have correct format", false);
            return;
        }

        if (!isValidPassword(password)) {
            showMessage("Invalid password, password has at least 3 characters", false);
            return;
        }

        if (!password.equals(confirmPassword)) {
            showMessage("Confirm password does not match password", false);
            return;
        }

        if (checkExistUser(email)) {
            showMessage("Email has been registered, please change to another email", false);
            return;
        }

        clearField();
        JOptionPane.showMessageDialog(frame, "Register Successfully! Please login again.", "", JOptionPane.INFORMATION_MESSAGE);
        insertUser(email, password, fullname);
        tabbedPane.setSelectedIndex(0);
        loadUsers();
        _users.add(new UserModel(email, fullname, password));
    }

    private void loginUser(String email, String password) {
        if (!isValidEmail(email)) {
            showMessage("Invalid email, email must have correct format", true);
            return;
        }

        if (!isValidPassword(password)) {
            showMessage("Invalid password, password has at least 3 characters", true);
            return;
        }

        if (!checkLogin(email, password)) {
            showMessage("Email or password not corrected, please try again", true);
            return;
        }

        String fullname = getFullnameByEmail(email);
        _user.setEmail(email);
        _user.setPassword(password);
        _user.setFullname(fullname);
        showMainView();
    }

    private String getFullnameByEmail(String email) {
        for (UserModel user : _users) {
            if (user.getEmail().equals(email)) {
                return user.getFullname();
            }
        }
        return "";
    }

    private void clearField() {
        loginEmailField.setText("");
        loginPasswordField.setText("");
        registerEmailField.setText("");
        registerFullnameField.setText("");
        registerPasswordField.setText("");
        registerConfirmPasswordField.setText("");
    }

    private void showMainView() {
        _config = Helper.readXML();
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                // Load emails from local
                _emails = Helper.loadEmails(_user, _config, _emails);

                // Get emails from the server
                // _emails = Helper.getEmails(_user, _config, _emails);
                return null;
            }

            @Override
            protected void done() {
                frame.setVisible(false);
                new MainView(_user, _config, _emails).show();
            }
        };

        worker.execute();
    }
}
