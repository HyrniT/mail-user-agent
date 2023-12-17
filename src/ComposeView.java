import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

import javax.swing.*;

public class ComposeView {
    private final Color PrimaryColor = Color.WHITE;
    private final Color OnPrimaryColor = Color.BLACK;
    private final String FontName = "Arial";

    private JFrame frame;

    public ComposeView() {
        initializeUI();
    }

    public void initializeUI() {
        frame = new JFrame("Hmail");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setSize(600, 600);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(PrimaryColor);
        mainPanel.setForeground(OnPrimaryColor);

        // Top Panel
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        topPanel.setBackground(PrimaryColor);
        topPanel.setForeground(OnPrimaryColor);

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

        JTextField fromTextField = new JTextField();
        fromTextField.setPreferredSize(new Dimension(500, 24));
        fromTextField.setBackground(PrimaryColor);
        fromTextField.setForeground(OnPrimaryColor);
        fromTextField.setCaretColor(OnPrimaryColor);

        JTextField toTextField = new JTextField();
        toTextField.setPreferredSize(new Dimension(500, 24));
        toTextField.setBackground(PrimaryColor);
        toTextField.setForeground(OnPrimaryColor);
        toTextField.setCaretColor(OnPrimaryColor);

        JTextField ccTextField = new JTextField();
        ccTextField.setPreferredSize(new Dimension(500, 24));
        ccTextField.setBackground(PrimaryColor);
        ccTextField.setForeground(OnPrimaryColor);
        ccTextField.setCaretColor(OnPrimaryColor);

        JTextField bccTextField = new JTextField();
        bccTextField.setPreferredSize(new Dimension(500, 24));
        bccTextField.setBackground(PrimaryColor);
        bccTextField.setForeground(OnPrimaryColor);
        bccTextField.setCaretColor(OnPrimaryColor);

        JTextField titleTextField = new JTextField();
        titleTextField.setPreferredSize(new Dimension(500, 24));
        titleTextField.setBackground(PrimaryColor);
        titleTextField.setForeground(OnPrimaryColor);
        titleTextField.setCaretColor(OnPrimaryColor);

        topPanel.add(fromLabel);
        topPanel.add(fromTextField);
        topPanel.add(toLabel);
        topPanel.add(toTextField);
        topPanel.add(ccLabel);
        topPanel.add(ccTextField);
        topPanel.add(bccLabel);
        topPanel.add(bccTextField);
        topPanel.add(titleLabel);
        topPanel.add(titleTextField);

        mainPanel.add(topPanel, BorderLayout.CENTER);
        frame.add(mainPanel);
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
