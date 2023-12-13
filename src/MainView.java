import javax.swing.*;

public class MainView {
    private JFrame frame;

    public MainView() {
        initializeUI();
    }

    private void initializeUI() {
        frame = new JFrame("Hmail");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setSize(1200, 800);
    }

    public void show() {
        SwingUtilities.invokeLater(() -> {
            frame.setVisible(true);
        });
    }
}
