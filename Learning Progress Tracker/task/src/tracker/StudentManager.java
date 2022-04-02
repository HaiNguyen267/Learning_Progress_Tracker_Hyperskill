package tracker;


import tracker.Course;
import tracker.Student;

import java.util.*;

import static tracker.Course.*;

public class StudentManager {
    private Course[] courses = {JAVA, DSA, DATABASE, SPRING};

    private Map<String, Student> map;
    private Set<String> existEmail;
    private int currentStudentId;

    public StudentManager() {
        this.map = new LinkedHashMap<>();
        this.existEmail = new HashSet<>();
        this.currentStudentId = 10000;
    }

    public boolean addStudent(Student student) {
        if (!existEmail.contains(student.getEmail())) {
            student.setId(currentStudentId);
            map.put(Integer.toString(currentStudentId), student);
            existEmail.add(student.getEmail());
            currentStudentId++;
            System.out.println("The student has been added.");
            return true;
        } else {
            System.out.println("This email is already taken.");
            return false;
        }
    }

    public void addStudentPoint(String studentId, int[] points) {
        if (!map.containsKey(studentId)) {
            // if the student doesn't exist in the map
            System.out.println(String.format("No student is found for id=%s.", studentId));
        } else {
            Student student = map.get(studentId);

            for (int i = 0; i < points.length; i++) {
                // if the point student get for a course > 0
                if (points[i] > 0) {
                    // add point for the student for the course
                    student.addPoint(courses[i],points[i]);

                    // enroll student to the course if not yet, and add the point the student get for the total point of course
                    courses[i].addEnrolledStudentIfNotYet(student);
                    courses[i].addTotalPoint(points[i]);
                }
            }
            System.out.println("Points updated.");
        }
    }

    public void listStudentId() {
        if (map.isEmpty()) {
            System.out.println("No student found.");
        } else {
            // print all student id of students in the map
            System.out.println("Students:");
            for (Map.Entry<String, Student> entry : map.entrySet()) {
                System.out.println(entry.getKey());// print the student id
            }
        }
    }

    public void findStudent(String studentId) {
        if (!map.containsKey(studentId)) {
            System.out.println(String.format("No student found od=%s", studentId));
        } else {
            Student student = map.get(studentId);
            int javaPoint = student.getPoint(JAVA);
            int dsaPoint = student.getPoint(DSA);
            int databasesPoint = student.getPoint(DATABASE);
            int springPoint = student.getPoint(SPRING);
            System.out.println(String.format("%s points: Java=%d; DSA=%d; Databases=%d; Spring=%d", studentId, javaPoint, dsaPoint, databasesPoint, springPoint));
        }
    }


}