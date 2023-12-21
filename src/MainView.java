import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.util.List;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class MainView {
    private final Color PrimaryColor = Color.WHITE;
    private final Color OnPrimaryColor = Color.BLACK;
    private final String FontName = "Arial";

    private JFrame frame;
    private JButton newMailButton;
    private JList<String> folderList;
    private JList<EmailModel> mailList;
    private DefaultListModel<EmailModel> mailListModel;

    private UserModel _user;
    private ConfigModel _config;
    private List<EmailModel> _emails;

    public MainView(UserModel user, ConfigModel config, List<EmailModel> emails) {
        this._user = user;
        this._config = config;
        this._emails = emails;
        
    }

    private void initializeUI() {
        frame = new JFrame("Mail Client");
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
        leftPanel.setLayout(new BorderLayout());
        leftPanel.setBackground(PrimaryColor);
        leftPanel.setForeground(OnPrimaryColor);

        DefaultListModel<String> defaultListModel = new DefaultListModel<>();
        defaultListModel.addElement("Inbox");
        defaultListModel.addElement("Project");
        defaultListModel.addElement("Important");
        defaultListModel.addElement("Work");
        defaultListModel.addElement("Spam");

        folderList = new JList<>(defaultListModel);
        folderList.setCellRenderer(new FolderListCellRender());
        folderList.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        leftPanel.add(folderList, BorderLayout.NORTH);

        JPanel mailPanel = new JPanel();
        mailPanel.setLayout(new BorderLayout());
        mailPanel.setBackground(PrimaryColor);
        mailPanel.setForeground(OnPrimaryColor);
        
        mailListModel = new DefaultListModel<>();
        mailList = new JList<>(mailListModel);
        mailList.setCellRenderer(new EmailListCellRenderer());
        mailList.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        JScrollPane mailListScrollPane = new JScrollPane(mailList);
        mailListScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        mailListScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        mailPanel.add(mailListScrollPane, BorderLayout.CENTER);

        JPanel mailDetailPanel = new JPanel();
        mailDetailPanel.setBackground(PrimaryColor);
        mailDetailPanel.setForeground(OnPrimaryColor);

        JSplitPane mailSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, mailListScrollPane, mailDetailPanel);
        mailSplitPane.setDividerSize(2);
        mailSplitPane.setDividerLocation(250);
        
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
        frame.setVisible(true);
    }

    private void setupListeners() {
        newMailButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ComposeView(_user, _config).show();
            }
        });

        folderList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                String selectedFolder = folderList.getSelectedValue();
                mailListModel.clear();
                for (EmailModel email : _emails) {
                    if (email.getTag() == selectedFolder) {
                        mailListModel.addElement(email);
                    }
                }
            }
        });
    }

    public void show() {
        SwingUtilities.invokeLater(() -> {
            initializeUI();
            setupListeners();
        });
    }
}
