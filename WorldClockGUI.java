import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

public class WorldClockGUI extends JFrame {
    private ClockPanel clockPanel1;
    private ClockPanel clockPanel2;
    private ClockPanel clockPanel3;

    public WorldClockGUI() {
        // Set up the main frame
        setTitle("World Clocks");
        setSize(600, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create clock panels
        clockPanel1 = new ClockPanel("GMT", TimeZone.getTimeZone("GMT"));
        clockPanel2 = new ClockPanel("New York", TimeZone.getTimeZone("America/New_York"));
        clockPanel3 = new ClockPanel("Tokyo", TimeZone.getTimeZone("Asia/Tokyo"));

        // Add clock panels to the content pane
        Container contentPane = getContentPane();
        contentPane.setLayout(new GridLayout(1, 3));
        contentPane.add(clockPanel1);
        contentPane.add(clockPanel2);
        contentPane.add(clockPanel3);

        // Set up a timer to update the clocks every second
        Timer timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clockPanel1.updateTime();
                clockPanel2.updateTime();
                clockPanel3.updateTime();
            }
        });
        timer.start();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new WorldClockGUI().setVisible(true);
            }
        });
    }
}

class ClockPanel extends JPanel {
    private String city;
    private TimeZone timeZone;
    private SimpleDateFormat dateFormat;

    public ClockPanel(String city, TimeZone timeZone) {
        this.city = city;
        this.timeZone = timeZone;
        this.dateFormat = new SimpleDateFormat("HH:mm:ss");
        setPreferredSize(new Dimension(200, 200));
    }

    public void updateTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(timeZone);
        String time = dateFormat.format(calendar.getTime());
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int width = getWidth();
        int height = getHeight();

        // Draw clock circle
        g.setColor(new Color(220, 220, 220)); // Light gray
        g.fillOval(5, 5, width - 10, height - 10);

        // Draw bold border
        g.setColor(Color.BLACK);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(new BasicStroke(3));
        g2d.drawOval(5, 5, width - 10, height - 10);

        // Draw numbers around the clock
        g.setColor(Color.BLACK);
        Font numberFont = new Font("Arial", Font.BOLD, 12);
        g.setFont(numberFont);

        for (int i = 1; i <= 12; i++) {
            double angle = 2 * Math.PI * i / 12;
            int numberX = width / 2 + (int) (width / 2 * 0.85 * Math.sin(angle));
            int numberY = height / 2 - (int) (height / 2 * 0.85 * Math.cos(angle));
            String numberStr = Integer.toString(i);
            g.drawString(numberStr, numberX - g.getFontMetrics().stringWidth(numberStr) / 2, numberY);
        }

        // Draw city name inside the clock
        g.setColor(Color.BLACK);
        Font cityFont = new Font("Arial", Font.BOLD, 12);
        g.setFont(cityFont);
        g.drawString(city, width / 2 - g.getFontMetrics().stringWidth(city) / 2, height / 2);

        // Draw clock hands
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(timeZone);
        int hours = calendar.get(Calendar.HOUR);
        int minutes = calendar.get(Calendar.MINUTE);
        int seconds = calendar.get(Calendar.SECOND);

        drawHand(g, (double) hours % 12 / 12, width / 2, height / 2, width / 4);
        drawHand(g, (double) minutes / 60, width / 2, height / 2, width / 3);
        drawHand(g, (double) seconds / 60, width / 2, height / 2, width / 2);

        // Draw current time below the clock hands
        String time = getTimeAsString();
        g.drawString(time, width / 2 - g.getFontMetrics().stringWidth(time) / 2, height - 2);

        /*
            //Draw city name below the clock
              g.setColor(Color.BLACK);
              g.drawString(city, width / 2 - g.getFontMetrics().stringWidth(city) / 2, height - 5);

        //  Draw current time inside the clock
            String time = getTimeAsString();
            g.drawString(time, width / 2 - g.getFontMetrics().stringWidth(time) / 2, height / 2);
         */
    }

    private void drawHand(Graphics g, double percentage, int centerX, int centerY, int length) {
        int handX = centerX + (int) (length * Math.sin(2 * Math.PI * percentage));
        int handY = centerY - (int) (length * Math.cos(2 * Math.PI * percentage));

        g.drawLine(centerX, centerY, handX, handY);
    }

    private String getTimeAsString() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(timeZone);
        return String.format("%02d:%02d:%02d", calendar.get(Calendar.HOUR), calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND));
    }
}
