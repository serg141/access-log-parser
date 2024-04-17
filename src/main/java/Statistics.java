import java.time.Duration;
import java.time.LocalDateTime;

public class Statistics {
    private int totalTraffic;
    private LocalDateTime minTime, maxTime;

    public Statistics() {
        this.totalTraffic = 0;
        this.minTime = LocalDateTime.now();
        this.maxTime = LocalDateTime.now();
    }

    private void addEntry(LogEntry logEntry) {
        totalTraffic = logEntry.getResponseSize();
        if (minTime.isAfter(logEntry.getRequestDate())) minTime = logEntry.getRequestDate();
        if (maxTime.isBefore(logEntry.getRequestDate())) maxTime = logEntry.getRequestDate();
    }

    public void getTrafficRate(String s) {
        LogEntry logEntry = new LogEntry(s);
        addEntry(logEntry);
        Duration time = Duration.between(minTime, maxTime);

        System.out.println("totalTraffic/time: " + totalTraffic / time.toHoursPart());
    }
}
