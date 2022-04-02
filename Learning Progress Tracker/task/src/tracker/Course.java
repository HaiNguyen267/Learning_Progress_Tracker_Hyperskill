package tracker;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public enum Course {
    JAVA("Java", 600),
    DSA("DSA", 400),
    DATABASE("Databases", 480),
    SPRING("Spring", 550);

    final String name;
    final int completionPoint;
    List<Student> enrolledStudents;
    List<Student> studentHasBeenNotified;
    int totalSubmission;
    int totalPoint;

    Course(String name, int completionPoint) {
        this.name = name;
        this.completionPoint = completionPoint;

        this.enrolledStudents = new ArrayList<>();
        this.studentHasBeenNotified = new ArrayList<>();
        this.totalSubmission = 0;
        this.totalPoint = 0;
    }


    public String getName() {
        return name;
    }

    public int getCompletionPoint() {
        return completionPoint;
    }

    public int getEnrolledStudentNum() {
        return this.enrolledStudents.size();
    }

    public int getTotalSubmission() {
        return totalSubmission;
    }

    public int getTotalPoint() {
        return totalPoint;
    }

    public double getAveragePoint() {
        if (totalSubmission != 0) {
            return (double) totalPoint / totalSubmission;
        }

        return 0.0;
    }

    public List<Student> getStudentsToNotify() {
        // find the student who completes the course, and has not been notified yet
        List<Student> studentsToNotify = enrolledStudents.stream()
                .filter(student -> student.getPoint(this) == this.completionPoint)
                .filter(student -> !studentHasBeenNotified.contains(student))
                .collect(Collectors.toList());

        // a student can be notified once for a course completion
        studentHasBeenNotified.addAll(studentsToNotify); // these students will be notified very soon, so add them to list of student has been notified
        return studentsToNotify;
    }


    public void addEnrolledStudentIfNotYet(Student student) {
        if (!this.enrolledStudents.contains(student)) {
            this.enrolledStudents.add(student);
        }
    }

    public void addTotalPoint(int point) {
        totalSubmission++; // when a student get new point, meaning he do 1 more submission on the course
        totalPoint += point; // add the point student get to the total point of all students  the course
    }

    public void sortStudentBasedOnCompletion() {

        Comparator<Student> sortById = (s1, s2) -> Integer.compare(s1.getId(), s2.getId()); // ascending order
        Comparator<Student> sortByCourseCompletion = (s1, s2) -> Double.compare(s2.getCourseCompletion(this), s1.getCourseCompletion(this)); // descending order

        enrolledStudents.sort(sortById);
        enrolledStudents.sort(sortByCourseCompletion);

    }


    public void printCourseStatistics() {
        sortStudentBasedOnCompletion();
        System.out.println(name);
        System.out.println("id  points  completed");
        for (Student student : enrolledStudents) {
            System.out.println(String.format("%-5d %-6d    %.1f%%", student.getId(), student.getPoint(this), student.getCourseCompletion(this)));
        }
    }
}
