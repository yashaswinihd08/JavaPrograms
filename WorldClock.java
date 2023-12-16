/* import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

public class WorldClock {

    private JLabel[] timeLabels;

    public WorldClock() {
        // Create the JFrame
        JFrame frame = new JFrame("World Clock");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 200);
        frame.setLayout(new GridLayout(3, 3, 10, 10));

        // Create time labels for different time zones
        String[] timeZoneIds = {"America/New_York", "Europe/London", "Asia/Tokyo", "Australia/Sydney", "Africa/Cairo", "America/Los_Angeles", "Europe/Paris", "Asia/Shanghai", "Pacific/Honolulu"};
        timeLabels = new JLabel[timeZoneIds.length];

        for (int i = 0; i < timeZoneIds.length; i++) {
            timeLabels[i] = new JLabel();
            timeLabels[i].setFont(new Font("Arial", Font.PLAIN, 18));
            frame.add(timeLabels[i]);
        }

        // Create a Timer to update the time every second
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                updateTime();
            }
        }, 0, 1000);

        // Set the frame visibility to true
        frame.setVisible(true);
    }

    private void updateTime() {
        // Get the current time for each time zone
        for (int i = 0; i < timeLabels.length; i++) {
            Date now = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
            dateFormat.setTimeZone(TimeZone.getTimeZone(timeZoneIds[i]));

            String time = timeZoneIds[i].substring(timeZoneIds[i].indexOf('/') + 1) + ": " + dateFormat.format(now);
            timeLabels[i].setText(time);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new WorldClock());
    }
} 

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
    private ClockPanel clockPanel4;
    private ClockPanel clockPanel5;
    private ClockPanel clockPanel6;

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
        clockPanel4 = new ClockPanel("London", TimeZone.getTimeZone("Europe/London"));
        clockPanel5 = new ClockPanel("Sydney", TimeZone.getTimeZone("Australia/Sydney"));
        clockPanel6 = new ClockPanel("Paris", TimeZone.getTimeZone("Europe/Paris"));

        // Add clock panels to the content pane
        Container contentPane = getContentPane();
        contentPane.setLayout(new GridLayout(2, 3));
        contentPane.add(clockPanel1);
        contentPane.add(clockPanel2);
        contentPane.add(clockPanel3);
        contentPane.add(clockPanel4);
        contentPane.add(clockPanel5);
        contentPane.add(clockPanel6);

        // Set up a timer to update the clocks every second
        Timer timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clockPanel1.updateTime();
                clockPanel2.updateTime();
                clockPanel3.updateTime();
                clockPanel4.updateTime();
                clockPanel5.updateTime();
                clockPanel6.updateTime();
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
        setPreferredSize(new Dimension(100, 100));
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

        // Draw clock hands
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(timeZone);
        int hours = calendar.get(Calendar.HOUR);
        int minutes = calendar.get(Calendar.MINUTE);
        int seconds = calendar.get(Calendar.SECOND);

        drawHand(g, (double) hours % 12 / 12, width / 2, height / 2, width / 4);
        drawHand(g, (double) minutes / 60, width / 2, height / 2, width / 3);
        drawHand(g, (double) seconds / 60, width / 2, height / 2, width / 2);

        // Draw city name below the clock
        g.setColor(Color.BLACK);
        g.drawString(city, width / 2 - g.getFontMetrics().stringWidth(city) / 2, height - 5);

        // Draw current time inside the clock
        String time = getTimeAsString();
        g.drawString(time, width / 2 - g.getFontMetrics().stringWidth(time) / 2, height / 2);
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
*/
import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

public class WorldClock {

    private JLabel[] timeLabels;
    private String[] timeZoneIds = {"America/New_York", "Europe/London", "Asia/Tokyo", "Australia/Sydney", "Africa/Cairo", "America/Los_Angeles", "Europe/Paris", "Asia/Shanghai", "Pacific/Honolulu"};

    public WorldClock() {
        // Create the JFrame
        JFrame frame = new JFrame("World Clock");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 200);
        frame.setLayout(new GridLayout(3, 3, 10, 10));

        // Create time labels for different time zones
        timeLabels = new JLabel[timeZoneIds.length];

        for (int i = 0; i < timeZoneIds.length; i++) {
            timeLabels[i] = new JLabel();
            timeLabels[i].setFont(new Font("Arial", Font.PLAIN, 18));
            frame.add(timeLabels[i]);
        }

        // Create a Timer to update the time every second
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                updateTime();
            }
        }, 0, 1000);

        // Set the frame visibility to true
        frame.setVisible(true);
    }

    private void updateTime() {
        // Get the current time for each time zone
        for (int i = 0; i < timeLabels.length; i++) {
            Date now = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
            dateFormat.setTimeZone(TimeZone.getTimeZone(timeZoneIds[i]));

            String time = timeZoneIds[i].substring(timeZoneIds[i].indexOf('/') + 1) + ": " + dateFormat.format(now);
            timeLabels[i].setText(time);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new WorldClock());
    }
}

