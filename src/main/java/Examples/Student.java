package Examples;

import java.util.ArrayList;
import java.util.List;

public final class Student{
    private final List<Integer> grades = new ArrayList<>();
    public String name;
    private final AddRules addRules;

    public Student(String name, AddRules addRules) {
        this.name = name;
        this.addRules = addRules;
    }

    public void addGrade(int grade) {
        if(addRules.rule(grade)) grades.add(grade);
    }

    public String toString() {
        return "Student{ name: " + name + ", grades = " + grades + "}";
    }
}