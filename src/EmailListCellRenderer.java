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
        titleLabel.setPreferredSize(new Dimension(50, 20));
        add(titleLabel, BorderLayout.CENTER);

        contentLabel = new JLabel();
        contentLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        contentLabel.setPreferredSize(new Dimension(50, 20));
        add(contentLabel, BorderLayout.SOUTH);

        timeLabel = new JLabel();
        timeLabel.setForeground(Color.GRAY);
        timeLabel.setFont(new Font("Arial", Font.PLAIN, 10));
        timeLabel.setBackground(Color.BLUE);
        timeLabel.setHorizontalAlignment(JLabel.RIGHT);
        add(timeLabel, BorderLayout.NORTH);

        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY, 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends EmailModel> list, EmailModel value, int index,
            boolean isSelected, boolean cellHasFocus) {
        String formattedDateTime = LocalDateTime
                .parse(value.getDate(),
                        DateTimeFormatter.ofPattern("EEE, d MMM yyyy HH:mm:ss Z", Locale.ENGLISH))
                .format(DateTimeFormatter.ofPattern("HH:mm dd/MM/yy"));

        titleLabel.setText(value.getTitle());
        contentLabel.setText(value.getContent());
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