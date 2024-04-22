import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Statistics {
    private int totalTraffic;
    private LocalDateTime minTime, maxTime;
    private final HashSet<String> urlList = new HashSet<>();
    private final HashMap<String, Integer> countOs = new HashMap<>();
    private final HashMap<String, Double> traffic = new HashMap<>();
    private final HashSet<String> notExistPage = new HashSet<>();
    private final HashMap<String, Integer> statisticUserBrowser = new HashMap<>();
    private final HashMap<String, Double> userBrowserTraffic = new HashMap<>();

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

        String b = logEntry.getUserAgent().getBrowser();
        if (b.equals(Eos.NO_EOS.name())) notExistPage.add(logEntry.getRequestPath());
        if (statisticUserBrowser.containsKey(b)) statisticUserBrowser.put(b, statisticUserBrowser.get(b) + 1);
        else statisticUserBrowser.put(b, 1);
        statisticUserBrowser.remove("Тип браузера не передан в запросе");
    }

    private void countTrafficOS() {
        double sum = countOs.get(Eos.WINDOWS.name()) + countOs.get(Eos.LINUX.name()) + countOs.get(Eos.MACOS.name());
        traffic.put(Eos.WINDOWS.name(), countOs.get(Eos.WINDOWS.name())/sum);
        traffic.put(Eos.LINUX.name(), countOs.get(Eos.LINUX.name())/sum);
        traffic.put(Eos.MACOS.name(), countOs.get(Eos.MACOS.name())/sum);
    }

    private void countUserBrowserTraffic() {
        double sum = 0;
        Set<String> set = statisticUserBrowser.keySet();
        set.remove("Тип браузера не передан в запросе");
        for (String s : set) sum += statisticUserBrowser.get(s);
        for (String s : statisticUserBrowser.keySet()) {
            userBrowserTraffic.put(s, statisticUserBrowser.get(s) / sum);
        }
    }

    public void getTrafficRate(String s) {
        LogEntry logEntry = new LogEntry(s);
        addEntry(logEntry);
        Duration time = Duration.between(minTime, maxTime);

        if (logEntry.getResponseCode() == 404) notExistPage.add(logEntry.getRequestPath());

        System.out.println("totalTraffic/time: " + totalTraffic / time.toHoursPart());
    }

    public HashSet<String> getUrlList() {
        return urlList;
    }

    public HashSet<String> getNotExistPage() {
        return notExistPage;
    }

    public HashMap<String, Integer> getStatisticUserBrowser() {
        return statisticUserBrowser;
    }

    public HashMap<String, Double> getUserBrowserTraffic() {
        countUserBrowserTraffic();
        return userBrowserTraffic;
    }

    public HashMap<String, Double> getTraffic() {
        countTrafficOS();
        return traffic;
    }
}
