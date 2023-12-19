// Java Program to display the current timings in a Digital Clock (GUI) using Swing
// The program contains 9 countries timings (can be edited by changing java timezone id of different countries) 
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
