import java.io.*;
import java.time.LocalDateTime;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        int count = 0;

        int allStr, responseCode, responseSize;
        double googleBot, yandexBot;
        String line, ipAddress, requestMethod, requestPath, referer, browser, os;
        LocalDateTime requestDate;
        UserAgent userAgent;
        Statistics statistics = new Statistics();


        System.out.println("Введите текст и нажмите <Enter>: ");
        String text = new Scanner(System.in).nextLine();
        System.out.println("Длина текста: " + text.length());

        while(true) {
            try {
                allStr = 0;
                googleBot = 0;
                yandexBot = 0;

                TimeUnit.SECONDS.sleep(1);
                System.out.print("Укажите путь к файлу: ");
                String path = new Scanner(System.in).nextLine();

                File file = new File(path);
                boolean fileExists = file.exists();
                boolean isDirectory = file.isDirectory();

                if (!fileExists) {
                    System.out.println("Такого файла не существует");
                    continue;
                }

                if (isDirectory) {
                    System.out.println("Это путь к папке, а не файлу");
                    continue;
                }

                count++;
                System.out.println("Путь указан верно\nЭто файл номер " + count);

                FileReader fileReader = new FileReader(path);
                BufferedReader reader = new BufferedReader(fileReader);

                while ((line = reader.readLine()) != null) {
                    statistics.getTrafficRate(line);
                    int length = line.length();
                    if (length > 1024) throw new StrLengthException("The length is more than 1024");
                    allStr++;


                    LogEntry logEntry = new LogEntry(line);
                    ipAddress = logEntry.getIpAddress();
                    requestDate = logEntry.getRequestDate();
                    requestMethod = String.valueOf(logEntry.getRequestMethod());
                    requestPath = logEntry.getRequestPath();
                    responseCode = logEntry.getResponseCode();
                    responseSize = logEntry.getResponseSize();
                    referer = logEntry.getReferer();
                    userAgent = logEntry.getUserAgent();
                    browser = userAgent.getBrowser();
                    os = userAgent.getOs().toString();

                    CountBot countBot = new CountBot(userAgent.getUa());
                    googleBot += countBot.getGoogle();
                    yandexBot += countBot.getYandex();

                    System.out.println("ipAddresses - " + ipAddress);
                    System.out.println("requestDate - " + requestDate);
                    System.out.println("requestMethod - " + requestMethod);
                    System.out.println("requestPath - " + requestPath);
                    System.out.println("responseCode - " + responseCode);
                    System.out.println("responseSize - " + responseSize);
                    System.out.println("referer - " + referer);
                    System.out.println("userAgent - " + userAgent.getUa());
                    System.out.println("browser - " + browser);
                    System.out.println("OS - " + os);
                    System.out.println();

                }
                reader.close();

                System.out.println("Всего строк в файле: " + allStr);
                System.out.println("GoogleBot: " + googleBot / allStr);
                System.out.println("YandexBot: " + yandexBot / allStr);
                System.out.println("Страницы сайта: " + statistics.getUrlList());
                System.out.println("Доли операционных систем: " + statistics.getTraffic());

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
