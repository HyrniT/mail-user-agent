import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

class EmailListCellRenderer extends JPanel implements ListCellRenderer<EmailModel> {
    private JLabel titleLabel;
    private JLabel contentLabel;
    private JLabel timeLabel;

    public EmailListCellRenderer() {
        setLayout(new BorderLayout());
        titleLabel = new JLabel();
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        add(titleLabel, BorderLayout.NORTH);

        contentLabel = new JLabel();
        contentLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        add(contentLabel, BorderLayout.WEST);

        timeLabel = new JLabel();
        timeLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        add(timeLabel, BorderLayout.EAST);

        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends EmailModel> list, EmailModel value, int index,
            boolean isSelected, boolean cellHasFocus) {
        String formattedDateTime = LocalDateTime
                .parse(value.getDate(),
                        DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH))
                .format(DateTimeFormatter.ofPattern("HH/mm dd/MM/yy"));

        titleLabel.setToolTipText(value.getTitle());
        contentLabel.setToolTipText(value.getContent());
        timeLabel.setText(formattedDateTime);

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