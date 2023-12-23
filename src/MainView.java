import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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
    private JLabel fromValueLabel, toValueLabel, subjectValueLabel;
    private JTextArea contentTextArea;
    private JPanel attachmentPanel;

    private EmailModel selectedEmail = null;
    private String selectedFolder = null;
    // private String selectedEmailId = null;

    private UserModel _user;
    private ConfigModel _config;
    private List<EmailModel> _emails;

    public MainView(UserModel user, ConfigModel config, List<EmailModel> emails) {
        this._user = user;
        this._config = config;
        this._emails = emails;
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(() -> {
            SwingUtilities.invokeLater(() -> {
                List<EmailModel> newEmails = Helper.getEmails(user, config, new ArrayList<EmailModel>());
                System.out.println("New emails: " + newEmails.size());

                if (!newEmails.isEmpty()) {
                    selectedFolder = folderList.getSelectedValue();
                    mailListModel.clear();
                    for (EmailModel newEmail : newEmails) {
                        if (newEmail.getTag().equals(selectedFolder)) {
                            _emails.add(0, newEmail);
                            for (EmailModel email : _emails) {
                                mailListModel.addElement(email);
                                // System.out.println("Selected email: " + selectedEmailId);
                                // if (email.getId().equals(selectedEmailId)) {
                                //     mailList.setSelectedValue(selectedEmail, true);
                                //     mailList.revalidate();
                                //     mailList.repaint();
                                // }
                            }
                        }
                    }
                    clearField();
                }
            });
        }, Long.valueOf(_config.getAutoload()), Long.valueOf(_config.getAutoload()), TimeUnit.SECONDS);
        // new Thread(() -> {
        //     while (true) {
        //         try {
        //             Thread.sleep(1000 * Integer.parseInt(_config.getAutoload()));
        //             List<EmailModel> newEmails = Helper.getMails(user, config, emails);

        //             if (!newEmails.isEmpty()) {
        //                 SwingUtilities.invokeLater(() -> {
        //                     selectedFolder = folderList.getSelectedValue();
        //                     mailListModel.clear();
        //                     for (EmailModel email : _emails) {
        //                         if (email.getTag().equals(selectedFolder)) {
        //                             mailListModel.addElement(email);
        //                             if (selectedEmail != null && email.getId().equals(selectedEmailId)) {
        //                                 System.out.println("selecting email");
        //                                 mailList.setSelectedValue(selectedEmail, true);
        //                                 mailList.revalidate();
        //                                 mailList.repaint();
        //                             }
        //                         }
        //                     }

        //                 });
        //             }
        //         } catch (Exception e) {
        //             e.printStackTrace();
        //         }
        //     }
        // }).start();
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
        leftPanel.add(folderList, BorderLayout.CENTER);

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
        mailDetailPanel.setLayout(new BorderLayout());
        mailDetailPanel.setBackground(PrimaryColor);
        mailDetailPanel.setForeground(OnPrimaryColor);

        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new GridBagLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        headerPanel.setBackground(PrimaryColor);
        headerPanel.setForeground(OnPrimaryColor);

        JLabel fromLabel = new JLabel("From:");
        fromLabel.setFont(new Font(FontName, Font.BOLD, 14));
        fromLabel.setBackground(PrimaryColor);
        fromLabel.setForeground(OnPrimaryColor);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.weightx = 0.15;
        headerPanel.add(fromLabel, gbc);

        fromValueLabel = new JLabel();
        fromValueLabel.setFont(new Font(FontName, Font.PLAIN, 14));
        fromValueLabel.setBackground(PrimaryColor);
        fromValueLabel.setForeground(OnPrimaryColor);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.weightx = 0.85;
        headerPanel.add(fromValueLabel, gbc);

        JLabel toLabel = new JLabel("To:");
        toLabel.setFont(new Font(FontName, Font.BOLD, 14));
        toLabel.setBackground(PrimaryColor);
        toLabel.setForeground(OnPrimaryColor);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.weightx = 0.15;
        headerPanel.add(toLabel, gbc);

        toValueLabel = new JLabel();
        toValueLabel.setFont(new Font(FontName, Font.PLAIN, 14));
        toValueLabel.setBackground(PrimaryColor);
        toValueLabel.setForeground(OnPrimaryColor);
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.weightx = 0.85;
        headerPanel.add(toValueLabel, gbc);

        JLabel subjectLabel = new JLabel("Subject:");
        subjectLabel.setFont(new Font(FontName, Font.BOLD, 14));
        subjectLabel.setBackground(PrimaryColor);
        subjectLabel.setForeground(OnPrimaryColor);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.weightx = 0.15;
        headerPanel.add(subjectLabel, gbc);

        subjectValueLabel = new JLabel();
        subjectValueLabel.setFont(new Font(FontName, Font.PLAIN, 14));
        subjectValueLabel.setBackground(PrimaryColor);
        subjectValueLabel.setForeground(OnPrimaryColor);
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.weightx = 0.85;
        headerPanel.add(subjectValueLabel, gbc);

        contentTextArea = new JTextArea();
        contentTextArea.setFont(new Font(FontName, Font.PLAIN, 14));
        contentTextArea.setBackground(PrimaryColor);
        contentTextArea.setForeground(OnPrimaryColor);
        contentTextArea.setMargin(new Insets(0, 10, 0, 10));
        contentTextArea.setLineWrap(true);
        contentTextArea.setWrapStyleWord(true);
        contentTextArea.setEditable(false);
        contentTextArea.setCaretColor(PrimaryColor);
        JScrollPane contentScrollPane = new JScrollPane(contentTextArea);
        contentScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        contentScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        attachmentPanel = new JPanel();
        attachmentPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        attachmentPanel.setBackground(PrimaryColor);
        attachmentPanel.setForeground(OnPrimaryColor);

        mailDetailPanel.add(headerPanel, BorderLayout.NORTH);
        mailDetailPanel.add(contentScrollPane, BorderLayout.CENTER);
        mailDetailPanel.add(attachmentPanel, BorderLayout.SOUTH);

        JSplitPane mailSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, mailListScrollPane, mailDetailPanel);
        mailSplitPane.setDividerSize(2);
        mailSplitPane.setDividerLocation(250);

        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BorderLayout());
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
                selectedFolder = folderList.getSelectedValue();
                mailListModel.clear();
                for (EmailModel email : _emails) {
                    if (email.getTag().equals(selectedFolder)) {
                        mailListModel.addElement(email);
                    }
                }
            }
        });

        mailList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                attachmentPanel.removeAll();
                attachmentPanel.revalidate();
                attachmentPanel.repaint();
                
                selectedEmail = mailList.getSelectedValue();
                if (selectedEmail != null) {
                    String selectedEmailId = selectedEmail.getId();
                    fromValueLabel.setText(selectedEmail.getFrom());
                    toValueLabel.setText(String.join(", ", selectedEmail.getTo()));
                    subjectValueLabel.setText(selectedEmail.getTitle());
                    contentTextArea.setText(selectedEmail.getContent());
                    if (selectedEmail.getAttachmentFiles() != null) {
                        for (String attachmentFile : Helper.getAttachmentFileNamesFromSavedEmail(_user.getEmail(), selectedEmailId)) {
                            File file = Paths.get("data", _user.getEmail(), attachmentFile).toFile();
                            JButton openFileButton = new JButton(file.getName());
                            openFileButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                            openFileButton.setFont(new Font(FontName, Font.PLAIN, 14));
                            openFileButton.setFocusPainted(false);
                            attachmentPanel.add(openFileButton);
                            openFileButton.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    try {
                                        Desktop.getDesktop().open(file);
                                    } catch (IOException e1) {
                                        e1.printStackTrace();
                                    }
                                }
                            });
                        }
                    }
                }
            }
        });
    }

    private void clearField() {
        fromValueLabel.setText("");
        toValueLabel.setText("");
        subjectValueLabel.setText("");
        contentTextArea.setText("");
        attachmentPanel.removeAll();
        attachmentPanel.revalidate();
        attachmentPanel.repaint();
    }

    public void show() {
        SwingUtilities.invokeLater(() -> {
            initializeUI();
            setupListeners();
        });
    }
}
