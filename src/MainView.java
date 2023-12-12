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
        frame.setSize(400, 300);
    }

    public void show() {
        SwingUtilities.invokeLater(() -> {
            frame.setVisible(true);
        });
    }

    public void hide() {
        frame.setVisible(false);
    }
}
