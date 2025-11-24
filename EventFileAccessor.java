package datastore;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Reads events from a CSV file of the form: M-d-yyyy,Event text
 * Handles IO and parsing errors properly using try-catch.
 */
public class EventFileAccessor {
    private final Map<Date, String> eventList;
    private final SimpleDateFormat sdFormat = new SimpleDateFormat("M-d-yyyy");
    private final String fileName;

    public EventFileAccessor(String fileName) {
        this.fileName = fileName;
        this.eventList = new HashMap<>();
        sdFormat.setLenient(false);
        loadEvents();
    }

    private void loadEvents() {
        File file = new File(fileName);
        if (!file.exists()) {
            System.err.println("Event file not found: " + file.getAbsolutePath());
            return;
        }

        try (Scanner input = new Scanner(file)) {
            while (input.hasNextLine()) {
                String line = input.nextLine().trim();
                if (line.isEmpty()) continue;

                String[] parts = line.split(",", 2);
                if (parts.length < 2) continue;

                try {
                    Date eventDate = sdFormat.parse(parts[0].trim());
                    String eventText = parts[1].trim();
                    eventList.put(eventDate, eventText);
                } catch (ParseException e) {
                    System.err.println("Skipping invalid date: " + parts[0]);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }

    public Map<Date, String> getEventList() {
        return Collections.unmodifiableMap(eventList);
    }
}