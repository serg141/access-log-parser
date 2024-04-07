package Examples;

public class Main {
    public static void main(String[] args) {
        Student student1 = new Student("Sergey", new AddRules(1, 100));
        student1.addGrade(5);
        student1.addGrade(100);
        student1.addGrade(101);
        System.out.println(student1);
    }
}
