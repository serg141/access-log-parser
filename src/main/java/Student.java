import java.util.ArrayList;

public class Student {
    private final String name;
    private ArrayList<Integer> grades = new ArrayList<Integer>();

    public Student(String name) {
        this.name = name;
    }

    public Student(String name, ArrayList<Integer> grades) {
        this.name = name;
        privateSetGrades(grades);
    }

    public String getName() {
        return name;
    }

    public ArrayList<Integer> getGrades() {
        return grades;
    }

    public void setGrades(ArrayList<Integer> grades) {
        privateSetGrades(grades);
    }

    private void privateSetGrades(ArrayList<Integer> grades) {
        for (Integer grade : grades) {
            if (grade < 2 || grade > 5)
                throw new IllegalArgumentException("grade must be between 2 and 5");
        }
        this.grades = grades;
    }

    @Override
    public String toString() {
        return name + ": " + grades;
    }
}
