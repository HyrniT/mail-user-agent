import java.awt.*;
import javax.swing.*;

public class FolderListCellRender extends JPanel implements ListCellRenderer<String> {
    private JLabel titleLabel;

    public FolderListCellRender() {
        setLayout(new BorderLayout());

        titleLabel = new JLabel();
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        add(titleLabel, BorderLayout.CENTER);

        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.BLACK, 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends String> list, String value, int index,
            boolean isSelected, boolean cellHasFocus) {
        titleLabel.setText(value);

        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }

        return this;
    }
}
