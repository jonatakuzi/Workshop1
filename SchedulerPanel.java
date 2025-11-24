package gui;

import datastore.EventFileAccessor;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.*;

/**
 * Displays a monthly calendar grid with events loaded from a CSV file.
 */
public class SchedulerPanel extends JPanel {
    private final JTextArea[] cells = new JTextArea[42]; // 6 weeks × 7 days
    private final JLabel monthLabel = new JLabel("", SwingConstants.CENTER);
    private final JButton editButton = new JButton("EDIT");
    private boolean editable = false;

    private int year;
    /** Store month as a Calendar month constant (Calendar.JANUARY..Calendar.DECEMBER). */
    private int monthConst;

    public SchedulerPanel() {
        setLayout(new BorderLayout(8, 8));
        setBorder(new EmptyBorder(10, 10, 10, 10));

        // Prompt for month and year (defaults match sample file)
        String mIn = JOptionPane.showInputDialog(this, "Enter month (1-12):", "8");
        String yIn = JOptionPane.showInputDialog(this, "Enter year:", "2022");

        int monthInput, yearInput;
        try {
            monthInput = Integer.parseInt(mIn);
            yearInput = Integer.parseInt(yIn);
        } catch (Exception e) {
            monthInput = 8; // August
            yearInput = 2022;
        }
        // Clamp month 1..12 and convert to Calendar constant
        monthInput = Math.max(1, Math.min(12, monthInput));
        this.monthConst = toCalendarMonthConst(monthInput); // 1->JANUARY, 12->DECEMBER
        this.year = yearInput;

        // Month label
        monthLabel.setFont(new Font("Arial", Font.BOLD, 20));
        add(monthLabel, BorderLayout.NORTH);

        // Calendar grid
        JPanel grid = new JPanel(new GridLayout(7, 7, 2, 2));
        add(grid, BorderLayout.CENTER);

        // Day headers
        String[] days = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        for (String d : days) {
            JLabel lbl = new JLabel(d, SwingConstants.CENTER);
            lbl.setOpaque(true);
            lbl.setBackground(new Color(0x00AEEF));
            lbl.setForeground(Color.WHITE);
            lbl.setFont(lbl.getFont().deriveFont(Font.BOLD));
            grid.add(lbl);
        }

        // Calendar cells
        for (int i = 0; i < cells.length; i++) {
            JTextArea ta = new JTextArea();
            ta.setEditable(false);
            ta.setLineWrap(true);
            ta.setWrapStyleWord(true);
            ta.setBorder(BorderFactory.createLineBorder(Color.GRAY));
            cells[i] = ta;
            grid.add(new JScrollPane(ta));
        }

        // Edit button
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(editButton);
        add(bottomPanel, BorderLayout.SOUTH);
        editButton.addActionListener(e -> toggleEdit());

        // Fill and load
        fillMonth(year, monthConst);
        loadEvents("data/EventsList.csv");
    }

    private void toggleEdit() {
        editable = !editable;
        for (JTextArea ta : cells) ta.setEditable(editable);
        editButton.setText(editable ? "LOCK" : "EDIT");
    }

    /** Map 1..12 to Calendar.JANUARY..Calendar.DECEMBER */
    private static int toCalendarMonthConst(int oneBasedMonth) {
        switch (oneBasedMonth) {
            case 1:  return Calendar.JANUARY;
            case 2:  return Calendar.FEBRUARY;
            case 3:  return Calendar.MARCH;
            case 4:  return Calendar.APRIL;
            case 5:  return Calendar.MAY;
            case 6:  return Calendar.JUNE;
            case 7:  return Calendar.JULY;
            case 8:  return Calendar.AUGUST;
            case 9:  return Calendar.SEPTEMBER;
            case 10: return Calendar.OCTOBER;
            case 11: return Calendar.NOVEMBER;
            case 12: return Calendar.DECEMBER;
            default: return Calendar.JANUARY;
        }
    }

    private void fillMonth(int year, int monthConst) {
        Calendar cal = new GregorianCalendar(year, monthConst, 1);
        int firstDayOfWeek = cal.get(Calendar.DAY_OF_WEEK); // 1=Sun..7=Sat
        int daysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH);

        // Set month label
        monthLabel.setText(
                cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault()) + " " + year
        );

        // Clear all cells
        for (JTextArea ta : cells) ta.setText("");

        // Start index for the first day
        int startIndex = firstDayOfWeek - Calendar.SUNDAY; // 0..6
        for (int day = 1; day <= daysInMonth; day++) {
            int cellIndex = startIndex + (day - 1);
            cells[cellIndex].setText(day + "\n");
        }
    }

    public void loadEvents(String csvPath) {
        EventFileAccessor accessor = new EventFileAccessor(csvPath);
        Map<Date, String> events = accessor.getEventList();
        if (events.isEmpty()) return;

        Calendar tmp = Calendar.getInstance();

        //  start index for this month/year
        Calendar cal = new GregorianCalendar(year, monthConst, 1);
        int firstDayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        int startIndex = firstDayOfWeek - Calendar.SUNDAY;

        for (Map.Entry<Date, String> entry : events.entrySet()) {
            tmp.setTime(entry.getKey());
            if (tmp.get(Calendar.YEAR) == year && tmp.get(Calendar.MONTH) == monthConst) {
                int day = tmp.get(Calendar.DAY_OF_MONTH);
                int cellIndex = startIndex + (day - 1);
                if (cellIndex >= 0 && cellIndex < cells.length) {
                    String current = cells[cellIndex].getText();
                    if (!current.endsWith("\n")) current += "\n";
                    cells[cellIndex].setText(current + entry.getValue());
                }
            }
        }
    }
}