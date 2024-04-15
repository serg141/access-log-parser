import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        int count = 0;
        int allStr;
        List<String> ipAddresses = new ArrayList<>();
        List<String> lessProperties = new ArrayList<>();
        List<String> requestDate = new ArrayList<>();
        List<String> requestMethodAndPath = new ArrayList<>();
        List<String> httpResponse = new ArrayList<>();
        List<String> dataSize = new ArrayList<>();
        List<String> refererPath = new ArrayList<>();
        List<String> userAgent = new ArrayList<>();
/*
        System.out.println("Введите текст и нажмите <Enter>: ");
        String text = new Scanner(System.in).nextLine();
        System.out.println("Длина текста: " + text.length());

*/

        while(true) {
            try {
                allStr = 0;

                TimeUnit.SECONDS.sleep(1);
                System.out.print("Укажите путь к файлу: ");
                //String path = new Scanner(System.in).nextLine();
                String path = "C:\\Users\\skamynin\\Desktop\\access.log";
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
                String line;
                while ((line = reader.readLine()) != null) {
                    int length = line.length();
                    if (length > 1024) throw new StrLengthException("The length is more than 1024");
                    allStr++;

                    ipAddresses.add(line.substring(0, line.indexOf(" ")));

                    lessProperties.add(line.substring(line.indexOf(" ") + 1, line.indexOf("[") - 1));

                    requestDate.add(line.substring(line.indexOf("["), line.indexOf("]") + 1));

                    requestMethodAndPath.add(line.substring(line.indexOf("\"") + 1, line.indexOf("/1.0") + 4));

                    line = line.substring(line.indexOf("/1.0") + 4);
                    httpResponse.add(line.substring(1, line.indexOf(" ") + 4).trim());

                    line = line.substring(4);
                    dataSize.add(line.substring(1, line.indexOf("\"")).trim());

                    line = line.substring(line.indexOf("\"")).trim();
                    if (line.startsWith("\"http")) refererPath.add(line.substring(line.indexOf("http"),
                            line.indexOf(" ") - 1).trim());

                    userAgent.add(line.substring(line.indexOf(" ") + 2, line.lastIndexOf("\"")).trim());
                }

                System.out.println("Всего строк в файле: " + allStr);
                reader.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
