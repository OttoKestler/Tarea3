import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

class Student {
    private int id;
    private String name;
    private int age;

    public Student(int id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    @Override
    public String toString() {
        return "Student{id=" + id + ", name='" + name + "', age=" + age + "}";
    }
}

class StudentNotFoundException extends Exception {
    public StudentNotFoundException(String message) {
        super(message);
    }
}

class GenericList<T> {
    private List<T> list = new ArrayList<>();

    public void add(T item) {
        list.add(item);
    }

    public void remove(T item) {
        list.remove(item);
    }

    public Optional<T> find(int id) {
        return list.stream()
                .filter(item -> item instanceof Student && ((Student) item).getId() == id)
                .map(item -> (T) item)
                .findFirst();
    }

    public int size() {
        return list.size();
    }

    public List<T> getAll() {
        return list;
    }
}

interface StudentManager {
    void addStudent(Student student);
    void removeStudent(int id) throws StudentNotFoundException;
    Student findStudent(int id) throws StudentNotFoundException;
    List<Student> getAllStudents();
}

class StudentManagerImpl implements StudentManager {
    private GenericList<Student> studentList = new GenericList<>();

    @Override
    public void addStudent(Student student) {
        studentList.add(student);
    }

    @Override
    public void removeStudent(int id) throws StudentNotFoundException {
        Student student = findStudent(id);
        studentList.remove(student);
    }

    @Override
    public Student findStudent(int id) throws StudentNotFoundException {
        return studentList.find(id).orElseThrow(() -> new StudentNotFoundException("Student not found"));
    }

    @Override
    public List<Student> getAllStudents() {
        return studentList.getAll();
    }
}

public class Main {
    public static void main(String[] args) {
        StudentManager studentManager = new StudentManagerImpl();

        studentManager.addStudent(new Student(1, "Alice", 22));
        studentManager.addStudent(new Student(2, "Bob", 20));
        studentManager.addStudent(new Student(3, "Charlie", 23));

        System.out.println("Lista de estudiantes:");
        studentManager.getAllStudents().forEach(System.out::println);

        System.out.println("\nEstudiantes mayores de 21:");
        studentManager.getAllStudents().stream()
                .filter(student -> student.getAge() > 21)
                .forEach(System.out::println);

        System.out.println("\nEstudiantes ordenados por nombre:");
        studentManager.getAllStudents().stream()
                .sorted((s1, s2) -> s1.getName().compareTo(s2.getName()))
                .forEach(System.out::println);
    }
}