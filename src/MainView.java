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

    private UserModel user;

    public MainView(UserModel user) {
        this.user = user;
        initializeUI();
    }

    public MainView() {
        initializeUI();
    }

    private void initializeUI() {
        frame = new JFrame("Hmail");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setSize(1200, 800);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        // Top Panel
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        topPanel.setBackground(Color.RED);

        newMailButton = new JButton("New Email");
        newMailButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        newMailButton.setFocusPainted(false);

        topPanel.add(newMailButton);

        JPanel leftPanel = new JPanel();
        leftPanel.setBackground(Color.GREEN);

        JPanel mailPanel = new JPanel();
        mailPanel.setBackground(Color.BLACK);

        JPanel mailDetailPanel = new JPanel();
        mailDetailPanel.setBackground(Color.ORANGE);

        JSplitPane mailSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, mailPanel, mailDetailPanel);
        mailSplitPane.setDividerSize(5);
        mailSplitPane.setDividerLocation(200);
        
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BorderLayout());
        rightPanel.setBackground(Color.BLUE);
        rightPanel.add(mailSplitPane, BorderLayout.CENTER);

        JSplitPane mainSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
        mainSplitPane.setDividerSize(5);
        mainSplitPane.setDividerLocation(200);

        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(mainSplitPane, BorderLayout.CENTER);

        frame.add(mainPanel);
    }

    public void show() {
        SwingUtilities.invokeLater(() -> {
            frame.setTitle(user.toString());
            frame.setVisible(true);
        });
    }
}
