import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        int count = 0;
        int allStr;
        double googleBot, yandexBot;
        String line;

        List<String> ipAddresses = new ArrayList<>();
        List<String> lessProperties = new ArrayList<>();
        List<String> requestDate = new ArrayList<>();
        List<String> requestMethodAndPath = new ArrayList<>();
        List<String> httpResponse = new ArrayList<>();
        List<String> dataSize = new ArrayList<>();
        List<String> refererPath = new ArrayList<>();
        List<String> userAgent = new ArrayList<>();

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
                    int length = line.length();
                    if (length > 1024) throw new StrLengthException("The length is more than 1024");
                    allStr++;

                    //ipAddresses.add(line.substring(0, line.indexOf(" ")));

                    line = line.substring(line.indexOf(" ")).trim();
                    //lessProperties.add(line.substring(0, line.indexOf("[") - 1));

                    line = line.substring(line.indexOf("[")).trim();
                    //requestDate.add(line.substring(line.indexOf("["), line.indexOf("]") + 1));

                    line = line.substring(line.indexOf("\"") + 1).trim();
                    //requestMethodAndPath.add( line.substring(0, line.indexOf("\"")));

                    line = line.substring(line.indexOf("\"")).trim();
                    //httpResponse.add(line.substring(1, line.indexOf(" ") + 4).trim());

                    line = line.substring(5).trim();
                    //dataSize.add(line.substring(0, line.indexOf("\"")).trim());

                    line = line.substring(line.indexOf("\"") + 1).trim();
                    //if (line.startsWith("http")) refererPath.add(line.substring(line.indexOf("http"), line.indexOf(" ") - 1).trim());

                    line = line.substring(line.indexOf(" ") + 2).trim();
                    userAgent.add(line.trim());
                }

                for (String s : userAgent) {
                    if (s.contains(";") && s.contains("(") && s.contains(")")) {
                        s = s.substring(s.indexOf("(") + 1, s.indexOf(")")).trim();

                        String[] parts = s.split(";");

                        for (int j = 0; j < parts.length; j++) {
                            parts[j] = parts[j].replace(" ", "");
                        }

                        if (parts.length >= 2) {
                            String fragment = parts[1];
                            if (fragment.contains("Googlebot")) googleBot++;
                            if (fragment.contains("YandexBot")) yandexBot++;
                        }
                    }
                }

                System.out.println("Всего строк в файле: " + allStr);
                System.out.println("GoogleBot: " + googleBot / allStr);
                System.out.println("YandexBot: " + yandexBot / allStr);
                reader.close();
                System.out.println();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
