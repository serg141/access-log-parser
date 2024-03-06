import java.io.File;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        int count = 0;

        System.out.println("Введите текст и нажмите <Enter>: ");
        String text = new Scanner(System.in).nextLine();
        System.out.println("Длина текста: " + text.length());

        while(true) {
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
        }
    }
}
