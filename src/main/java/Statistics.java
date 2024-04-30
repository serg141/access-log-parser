import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class Statistics {
    private int totalTraffic, visitRealUser, countErrorRequest, countVisitRealIPAddress;
    private LocalDateTime minTime, maxTime, start, end;
    private final HashSet<String> urlList = new HashSet<>();
    private final HashMap<String, Integer> countOs = new HashMap<>();
    private final HashMap<String, Double> traffic = new HashMap<>();
    private final HashSet<String> notExistPage = new HashSet<>();
    private final HashMap<String, Integer> statisticUserBrowser = new HashMap<>();
    private final HashMap<String, Double> userBrowserTraffic = new HashMap<>();
    private final List<String> ipAddresses = new ArrayList<>();

    public Statistics() {
        this.totalTraffic = 0;
        this.visitRealUser = 0;
        this.countVisitRealIPAddress = 0;
        this.minTime = LocalDateTime.now();
        this.maxTime = LocalDateTime.now();
        this.start = LocalDateTime.now();
        this.end = LocalDateTime.now();
    }

    /*Фиксирует объем трафика, минимальное и максимальное время запроса, минимальное и максимальноевремя запроса НЕ от
    бота, количество запросов не от бота, список всех существующих страниц сайта, количество уникальных пользователей
     */
    private void addEntry(LogEntry logEntry) {
        totalTraffic = logEntry.getResponseSize();
        if (minTime.isAfter(logEntry.getRequestDate())) minTime = logEntry.getRequestDate();
        if (maxTime.isBefore(logEntry.getRequestDate())) maxTime = logEntry.getRequestDate();

        if (logEntry.getResponseCode() == 200) urlList.add(logEntry.getRequestPath());
        if (logEntry.getResponseCode() >= 400) setCountErrorRequest();

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

        if (!logEntry.getUserAgent().toString().contains("bot")) {
            visitRealUser++;
            countVisitRealIPAddress++;
            if (start.isAfter(logEntry.getRequestDate())) start = logEntry.getRequestDate();
            if (end.isBefore(logEntry.getRequestDate())) end = logEntry.getRequestDate();
            if (!ipAddresses.contains(logEntry.getIpAddress())) ipAddresses.add(logEntry.getIpAddress());
        }
    }

    //Считает статистику операционных систем пользователей сайта в процентном соотношении
    private void countTrafficOS() {
        double sum = countOs.get(Eos.WINDOWS.name()) + countOs.get(Eos.LINUX.name()) + countOs.get(Eos.MACOS.name());
        traffic.put(Eos.WINDOWS.name(), countOs.get(Eos.WINDOWS.name())/sum);
        traffic.put(Eos.LINUX.name(), countOs.get(Eos.LINUX.name())/sum);
        traffic.put(Eos.MACOS.name(), countOs.get(Eos.MACOS.name())/sum);
    }

    //Подсчет статистики браузеров пользователей сайта
    private void countUserBrowserTraffic() {
        double sum = 0;
        Set<String> set = statisticUserBrowser.keySet();
        set.remove("Тип браузера не передан в запросе");
        for (String s : set) sum += statisticUserBrowser.get(s);
        for (String s : statisticUserBrowser.keySet()) {
            userBrowserTraffic.put(s, statisticUserBrowser.get(s) / sum);
        }
    }

    //Среднее количество посещений сайта за час
    public void checkBrowserTrafficBot() {
        Duration time = Duration.between(start, end);
        System.out.println("Среднее количество посещений сайта за час: " + visitRealUser / time.toHoursPart());
    }

    //Расчет объема часового трафика сайта, также считает кол-во несуществующих страниц сайта
    public void getTrafficRate(String s) {
        LogEntry logEntry = new LogEntry(s);
        addEntry(logEntry);
        Duration time = Duration.between(minTime, maxTime);

        if (logEntry.getResponseCode() == 404) notExistPage.add(logEntry.getRequestPath());

        System.out.println("totalTraffic/time: " + totalTraffic / time.toHoursPart());
    }

    //Подсчет количества ошибочных запросов
    private void setCountErrorRequest() {
        countErrorRequest++;
    }

    //Возвращает список всех существующих страниц сайта
    public HashSet<String> getUrlList() {
        return urlList;
    }

    //Возвращает список гесуществующих страниц сайта
    public HashSet<String> getNotExistPage() {
        return notExistPage;
    }

    //возвращать статистику операционных систем пользователей сайта
    public HashMap<String, Integer> getStatisticUserBrowser() {
        return statisticUserBrowser;
    }

    //Возвращает статистику браузеров пользователей сайта
    public HashMap<String, Double> getUserBrowserTraffic() {
        countUserBrowserTraffic();
        return userBrowserTraffic;
    }

    //Возвращает статистику операционных систем пользователей сайта в процентном соотношении
    public HashMap<String, Double> getTraffic() {
        countTrafficOS();
        return traffic;
    }

    //Возвращает среднее количества ошибочных запросов в час
    public void avgErrorRequestByHour() {
        Duration time = Duration.between(minTime, maxTime);
        System.out.println("Среднее количество ошибочных запросов в час: " + countErrorRequest / time.toHoursPart());
    }

    //Возвращает среднее количество посещений сайта одним пользователем
    public void avgOneUserVisit() {
        int i = ipAddresses.size() - 1;
        System.out.println("Средняя посещаемость одним пользователем: " + countVisitRealIPAddress / i);
    }
}
