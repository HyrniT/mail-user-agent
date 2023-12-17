import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

public class ComposeView {
    private final Color PrimaryColor = Color.WHITE;
    private final Color OnPrimaryColor = Color.BLACK;
    private final String FontName = "Arial";

    private UserModel _user;
    private ConfigModel _config;
    private EmailModel _email;

    private List<String> attachmentFiles = new ArrayList<>();

    private JFrame frame;
    private JTextField fromTextField, toTextField, ccTextField, bccTextField, titleTextField;
    private JButton sendButton, attachButton;
    private JTextArea contentTextArea;
    private JLabel attachLabel;

    public ComposeView(UserModel user, ConfigModel config) {
        this._user = user;
        this._config = config;
        initializeUI();
        setupListeners();
    }

    // Mốt xóa thằng này
    public ComposeView() {
        _config = Helper.readXML();
        _user = new UserModel();
        initializeUI();
        setupListeners();
    }

    public void initializeUI() {
        frame = new JFrame("New Email");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setSize(600, 600);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(PrimaryColor);
        mainPanel.setForeground(OnPrimaryColor);

        // Top Panel
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 12, 10));
        topPanel.setBackground(PrimaryColor);
        topPanel.setForeground(OnPrimaryColor);

        sendButton = new JButton("Send");
        sendButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        sendButton.setFont(new Font(FontName, Font.BOLD, 13));
        sendButton.setMargin(new Insets(0, 20, 0, 20));
        sendButton.setFocusPainted(false);

        topPanel.add(sendButton);

        // Center Panel
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        centerPanel.setBackground(PrimaryColor);
        centerPanel.setForeground(OnPrimaryColor);

        JLabel fromLabel = new JLabel("From:");
        fromLabel.setFont(new Font(FontName, Font.BOLD, 13));
        fromLabel.setForeground(OnPrimaryColor);
        fromLabel.setBackground(PrimaryColor);
        fromLabel.setPreferredSize(new Dimension(60, 24));

        JLabel toLabel = new JLabel("To:");
        toLabel.setFont(new Font(FontName, Font.BOLD, 13));
        toLabel.setForeground(OnPrimaryColor);
        toLabel.setBackground(PrimaryColor);
        toLabel.setPreferredSize(new Dimension(60, 24));

        JLabel ccLabel = new JLabel("Cc:");
        ccLabel.setFont(new Font(FontName, Font.BOLD, 13));
        ccLabel.setForeground(OnPrimaryColor);
        ccLabel.setBackground(PrimaryColor);
        ccLabel.setPreferredSize(new Dimension(60, 24));

        JLabel bccLabel = new JLabel("Bcc:");
        bccLabel.setFont(new Font(FontName, Font.BOLD, 13));
        bccLabel.setForeground(OnPrimaryColor);
        bccLabel.setBackground(PrimaryColor);
        bccLabel.setPreferredSize(new Dimension(60, 24));

        JLabel titleLabel = new JLabel("Title:");
        titleLabel.setFont(new Font(FontName, Font.BOLD, 13));
        titleLabel.setForeground(OnPrimaryColor);
        titleLabel.setBackground(PrimaryColor);
        titleLabel.setPreferredSize(new Dimension(60, 24));

        fromTextField = new JTextField();
        fromTextField.setPreferredSize(new Dimension(500, 24));
        fromTextField.setBackground(PrimaryColor);
        fromTextField.setForeground(OnPrimaryColor);
        fromTextField.setCaretColor(OnPrimaryColor);
        // fromTextField.setText(_user.getEmail());

        toTextField = new JTextField();
        toTextField.setPreferredSize(new Dimension(500, 24));
        toTextField.setBackground(PrimaryColor);
        toTextField.setForeground(OnPrimaryColor);
        toTextField.setCaretColor(OnPrimaryColor);

        ccTextField = new JTextField();
        ccTextField.setPreferredSize(new Dimension(500, 24));
        ccTextField.setBackground(PrimaryColor);
        ccTextField.setForeground(OnPrimaryColor);
        ccTextField.setCaretColor(OnPrimaryColor);

        bccTextField = new JTextField();
        bccTextField.setPreferredSize(new Dimension(500, 24));
        bccTextField.setBackground(PrimaryColor);
        bccTextField.setForeground(OnPrimaryColor);
        bccTextField.setCaretColor(OnPrimaryColor);

        titleTextField = new JTextField();
        titleTextField.setPreferredSize(new Dimension(500, 24));
        titleTextField.setBackground(PrimaryColor);
        titleTextField.setForeground(OnPrimaryColor);
        titleTextField.setCaretColor(OnPrimaryColor);

        contentTextArea = new JTextArea();
        contentTextArea.setBackground(PrimaryColor);
        contentTextArea.setForeground(OnPrimaryColor);
        contentTextArea.setCaretColor(OnPrimaryColor);
        contentTextArea.setLineWrap(true);
        contentTextArea.setWrapStyleWord(true);
        contentTextArea.setFont(new Font(FontName, Font.PLAIN, 13));
        contentTextArea.setBorder(BorderFactory.createLineBorder(OnPrimaryColor));

        JScrollPane scrollPane = new JScrollPane(contentTextArea);
        scrollPane.setPreferredSize(new Dimension(560, 330));
        scrollPane.setBackground(PrimaryColor);
        scrollPane.setForeground(OnPrimaryColor);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));

        centerPanel.add(fromLabel);
        centerPanel.add(fromTextField);
        centerPanel.add(toLabel);
        centerPanel.add(toTextField);
        centerPanel.add(ccLabel);
        centerPanel.add(ccTextField);
        centerPanel.add(bccLabel);
        centerPanel.add(bccTextField);
        centerPanel.add(titleLabel);
        centerPanel.add(titleTextField);
        centerPanel.add(scrollPane);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 10));
        bottomPanel.setBackground(PrimaryColor);
        bottomPanel.setForeground(OnPrimaryColor);

        attachButton = new JButton("Attach");
        attachButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        attachButton.setFont(new Font(FontName, Font.BOLD, 13));
        attachButton.setMargin(new Insets(0, 20, 0, 20));
        attachButton.setFocusPainted(false);

        attachLabel = new JLabel();
        attachLabel.setFont(new Font(FontName, Font.BOLD, 13));
        attachLabel.setForeground(OnPrimaryColor);
        attachLabel.setBackground(PrimaryColor);
        attachLabel.setPreferredSize(new Dimension(460, 24));
        // attachLabel.setBorder(BorderFactory.createLineBorder(OnPrimaryColor));

        bottomPanel.add(attachButton);
        bottomPanel.add(attachLabel);

        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        frame.add(mainPanel);
    }

    private void setupListeners() {
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String from = fromTextField.getText();
                String to = toTextField.getText();
                String cc = ccTextField.getText();
                String bcc = bccTextField.getText();
                String title = titleTextField.getText();
                String content = contentTextArea.getText();

                if (from.isEmpty() || to.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Please fill all required fields!", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                String[] toList = null, ccList = null, bccList = null;

                if (!to.trim().isEmpty()) {
                    toList = to.split(",");
                }
                if (!cc.trim().isEmpty()) {
                    ccList = cc.split(",");
                }
                if (!bcc.trim().isEmpty()) {
                    bccList = bcc.split(",");
                }

                if (attachLabel.getText().length() > 0) {
                    _email = new EmailModel(from, toList, ccList, bccList, title, content, attachmentFiles);
                } else {
                    _email = new EmailModel(from, toList, ccList, bccList, title, content);
                }

                // mốt xóa dòng bên dưới
                _user.setEmail(from);
                //

                Helper.sendMail(_user, _email, _config);
                // JOptionPane.showMessageDialog(frame, "Email sent successfully!", "Success",
                //         JOptionPane.INFORMATION_MESSAGE);
                new Thread(() -> {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                    frame.dispose();
                }).start();
            }
        });

        attachButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setMultiSelectionEnabled(true);
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                fileChooser.setDialogTitle("Choose file(s) to attach");

                int result = fileChooser.showOpenDialog(frame);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File[] files = fileChooser.getSelectedFiles();
                    String filesName = "";

                    for (File file : files) {
                        long fileSizeInBytes = file.length();
                        long fileSizeInKB = fileSizeInBytes / 1024;
                        long fileSizeInMB = fileSizeInKB / 1024;

                        if (fileSizeInMB > 3) {
                            JOptionPane.showMessageDialog(frame, "File " + file.getName() + " is larger than 3MB.",
                                    "Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }

                        filesName += file.getName() + ", ";
                        attachmentFiles.add(file.getAbsolutePath());
                    }

                    attachLabel.setText(filesName);
                }
            }
        });
    }

    public void show() {
        SwingUtilities.invokeLater(() -> {
            frame.setVisible(true);
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ComposeView().show();
        });
    }
}
