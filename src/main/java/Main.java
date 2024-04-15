import java.io.*;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        int count = 0;
        int allStr, minStr, maxStr;

        System.out.println("Введите текст и нажмите <Enter>: ");
        String text = new Scanner(System.in).nextLine();
        System.out.println("Длина текста: " + text.length());

        while(true) {
            try {
                allStr = 0;
                maxStr = Integer.MIN_VALUE;
                minStr = Integer.MAX_VALUE;

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
                String line;
                while ((line = reader.readLine()) != null) {
                    int length = line.length();
                    if (length > 1024) throw new StrLengthException("The length is more than 1024");
                    allStr++;
                    if (length > maxStr) maxStr = length;
                    if (length < minStr) minStr = length;
                }

                System.out.println("Всего строк в файле: " + allStr);
                System.out.println("Максимальная длина строки в файле: " + maxStr);
                System.out.println("Минимальная длина строки в файле: " + minStr);
                reader.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
