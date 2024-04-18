import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;

public class Statistics {
    private int totalTraffic;
    private LocalDateTime minTime, maxTime;
    private final HashSet<String> urlList = new HashSet<>();
    private final HashMap<String, Integer> countOs = new HashMap<>();
    private final HashMap<String, Double> traffic = new HashMap<>();

    public Statistics() {
        this.totalTraffic = 0;
        this.minTime = LocalDateTime.now();
        this.maxTime = LocalDateTime.now();
    }

    private void addEntry(LogEntry logEntry) {
        totalTraffic = logEntry.getResponseSize();
        if (minTime.isAfter(logEntry.getRequestDate())) minTime = logEntry.getRequestDate();
        if (maxTime.isBefore(logEntry.getRequestDate())) maxTime = logEntry.getRequestDate();

        if (logEntry.getResponseCode() == 200) urlList.add(logEntry.getRequestPath());

        String s = logEntry.getUserAgent().getOs().name();
        if (countOs.containsKey(s) && !s.equals(Eos.NO_EOS.name())) {
            countOs.put(s, countOs.get(s) + 1);
        }
        else countOs.put(s, 1);
    }

    private void countTrafficOS() {
        double sum = countOs.get(Eos.WINDOWS.name()) + countOs.get(Eos.LINUX.name()) + countOs.get(Eos.MACOS.name());
        traffic.put(Eos.WINDOWS.name(), countOs.get(Eos.WINDOWS.name())/sum);
        traffic.put(Eos.LINUX.name(), countOs.get(Eos.LINUX.name())/sum);
        traffic.put(Eos.MACOS.name(), countOs.get(Eos.MACOS.name())/sum);
    }

    public void getTrafficRate(String s) {
        LogEntry logEntry = new LogEntry(s);
        addEntry(logEntry);
        Duration time = Duration.between(minTime, maxTime);

        System.out.println("totalTraffic/time: " + totalTraffic / time.toHoursPart());
    }

    public HashSet<String> getUrlList() {
        return urlList;
    }

    public HashMap<String, Double> getTraffic() {
        countTrafficOS();
        return traffic;
    }
}
