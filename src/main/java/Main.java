import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Integer[] marks = {2, 4, 5, 3, 4, 5};
        ArrayList<Integer> m = new ArrayList<>();
        m.addAll(List.of(marks));

        Student s1 = new Student("Sergey");
        Student s2 = new Student("Mark", m);

        System.out.println(s1);
        System.out.println(s2);
    }
}
