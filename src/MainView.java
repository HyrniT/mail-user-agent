import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

import javax.swing.*;

public class MainView {
    private final Color PrimaryColor = Color.WHITE;
    private final Color OnPrimaryColor = Color.BLACK;
    private final String FontName = "Arial";

    private JFrame frame;
    private JButton newMailButton;

    private UserModel _user;
    private ConfigModel _config;

    public MainView(UserModel user) {
        this._user = user;
        new Thread(() -> {
            _config = Helper.readXML();
        }).start();
        initializeUI();
        setupListeners();
    }

    // Mốt xóa thằng này
    public MainView() {
        new Thread(() -> {
            _config = Helper.readXML();
        }).start();
        initializeUI();
        setupListeners();
    }

    private void initializeUI() {
        frame = new JFrame("Hmail");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setSize(900, 600);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(PrimaryColor);
        mainPanel.setForeground(OnPrimaryColor);

        // Top Panel
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        topPanel.setBackground(PrimaryColor);
        topPanel.setForeground(OnPrimaryColor);

        newMailButton = new JButton("NEW EMAIL");
        newMailButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        newMailButton.setFont(new Font(FontName, Font.BOLD, 14));      
        newMailButton.setMargin(new Insets(5, 20, 5, 20));
        newMailButton.setFocusPainted(false);

        topPanel.add(newMailButton);

        JPanel leftPanel = new JPanel();
        // leftPanel.setBackground(Color.GREEN);
        leftPanel.setLayout(new BorderLayout());
        leftPanel.setBackground(PrimaryColor);
        leftPanel.setForeground(OnPrimaryColor);

        DefaultListModel<String> defaultListModel = new DefaultListModel<>();
        defaultListModel.addElement("Inbox");
        defaultListModel.addElement("Project");
        defaultListModel.addElement("Important");
        defaultListModel.addElement("Work");
        defaultListModel.addElement("Spam");

        JList<String> folderList = new JList<>(defaultListModel);
        folderList.setCellRenderer(new FolderListCellRender());
        folderList.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        leftPanel.add(folderList, BorderLayout.NORTH);

        JPanel mailPanel = new JPanel();
        mailPanel.setBackground(PrimaryColor);
        mailPanel.setForeground(OnPrimaryColor);

        JPanel mailDetailPanel = new JPanel();
        mailDetailPanel.setBackground(PrimaryColor);
        mailDetailPanel.setForeground(OnPrimaryColor);

        JSplitPane mailSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, mailPanel, mailDetailPanel);
        mailSplitPane.setDividerSize(2);
        mailSplitPane.setDividerLocation(200);
        
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BorderLayout());
        rightPanel.setBackground(Color.BLUE);
        rightPanel.add(mailSplitPane, BorderLayout.CENTER);

        JSplitPane mainSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
        mainSplitPane.setDividerSize(2);
        mainSplitPane.setDividerLocation(150);

        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(mainSplitPane, BorderLayout.CENTER);

        frame.add(mainPanel);
    }

    private void setupListeners() {
        newMailButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ComposeView(_user, _config).show();
            }
        });
    }

    // Mốt mở comment cho thằng này
    public void show() {
        // frame.setTitle(_user.toString());
        frame.setVisible(true);
    }

    // Mốt xóa hàm main
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // frame.setVisible(true);
            new MainView().show();
        });
    }
}
