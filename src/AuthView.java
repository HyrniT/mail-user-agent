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
        frame = new JFrame("Authentication");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setSize(400, 300);

        tabbedPane = new JTabbedPane();

        // Login Panel
        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(new BorderLayout());

        JLabel loginLabel = new JLabel("LOGIN");
        setupLabel(loginLabel);

        JLabel loginEmailLabel = createLabel("Email:");
        JLabel loginPasswordLabel = createLabel("Password:");

        loginEmailField = createTextField();
        loginPasswordField = createPasswordField();

        loginMessageLabel = createMessageLabel();
        loginButton = createButton("Login");

        JPanel loginFormPanel = createFormPanel(
                new JLabel[]{loginEmailLabel, loginPasswordLabel},
                new JComponent[]{loginEmailField, loginPasswordField, loginMessageLabel});

        JPanel loginButtonPanel = createButtonPanel(loginButton);

        setupPanel(loginPanel, loginLabel, loginFormPanel, loginButtonPanel);

        // Register Panel
        JPanel registerPanel = new JPanel();
        registerPanel.setLayout(new BorderLayout());

        JLabel registerLabel = new JLabel("REGISTER");
        setupLabel(registerLabel);

        JLabel registerFullnameLabel = createLabel("Fullname:");
        JLabel registerEmailLabel = createLabel("Email:");
        JLabel registerPasswordLabel = createLabel("Password:");
        JLabel registerConfirmPasswordLabel = createLabel("Confirm password:");

        registerEmailField = createTextField();
        registerFullnameField = createTextField();
        registerPasswordField = createPasswordField();
        registerConfirmPasswordField = createPasswordField();

        registerMessageLabel = createMessageLabel();
        registerButton = createButton("Register");

        JPanel registerFormPanel = createFormPanel(
                new JLabel[]{registerFullnameLabel, registerEmailLabel, registerPasswordLabel, registerConfirmPasswordLabel},
                new JComponent[]{registerFullnameField, registerEmailField, registerPasswordField, registerConfirmPasswordField, registerMessageLabel});

        JPanel registerButtonPanel = createButtonPanel(registerButton);

        setupPanel(registerPanel, registerLabel, registerFormPanel, registerButtonPanel);

        // Add login & register panel into tabbed panel
        tabbedPane.addTab("Login", loginPanel);
        tabbedPane.addTab("Register", registerPanel);

        // Add tabbed panel into frame
        frame.add(tabbedPane);
        frame.setVisible(true);
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font(FontName, Font.BOLD, 13));
        label.setForeground(OnPrimaryColor);
        label.setBackground(PrimaryColor);
        label.setPreferredSize(new Dimension(120, 24));
        return label;
    }

    private JTextField createTextField() {
        JTextField textField = new JTextField();
        textField.setBackground(PrimaryColor);
        textField.setForeground(OnPrimaryColor);
        textField.setCaretColor(OnPrimaryColor);
        textField.setPreferredSize(new Dimension(230, 24));
        return textField;
    }

    private JPasswordField createPasswordField() {
        JPasswordField passwordField = new JPasswordField();
        passwordField.setBackground(PrimaryColor);
        passwordField.setForeground(OnPrimaryColor);
        passwordField.setCaretColor(OnPrimaryColor);
        passwordField.setPreferredSize(new Dimension(230, 24));
        return passwordField;
    }

    private JLabel createMessageLabel() {
        JLabel messageLabel = new JLabel();
        messageLabel.setFont(new Font(FontName, Font.ITALIC, 12));
        messageLabel.setForeground(Color.RED);
        messageLabel.setBackground(PrimaryColor);
        messageLabel.setPreferredSize(new Dimension(360, 24));
        return messageLabel;
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setFocusPainted(false);
        return button;
    }

    private JPanel createFormPanel(JLabel[] labels, JComponent[] components) {
        JPanel formPanel = new JPanel();
        formPanel.setBackground(PrimaryColor);
        formPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        for (int i = 0; i < labels.length; i++) {
            formPanel.add(labels[i]);
            formPanel.add(components[i]);
        }

        return formPanel;
    }

    private JPanel createButtonPanel(JButton button) {
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(PrimaryColor);
        buttonPanel.add(button);
        return buttonPanel;
    }

    private void setupLabel(JLabel label) {
        label.setOpaque(true);
        label.setBackground(PrimaryColor);
        label.setForeground(OnPrimaryColor);
        label.setFont(new Font(FontName, Font.BOLD, 20));
        label.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        label.setHorizontalAlignment(SwingConstants.CENTER);
    }

    private void setupPanel(JPanel panel, JLabel label, JPanel formPanel, JPanel buttonPanel) {
        panel.add(label, BorderLayout.NORTH);
        panel.add(formPanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
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
                _emails = Helper.getEmails(_user, _config, _emails);
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
