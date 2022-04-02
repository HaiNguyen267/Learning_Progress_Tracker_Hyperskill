package tracker;


import tracker.Course;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Student {
    private int id;
    private String firstName;
    private String lastName;
    private String email;

    private int[] points;
    private double[] courseCompletion;

    public Student(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;

        this.points = new int[4];
        this.courseCompletion = new double[4];
    }

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public int getPoint(Course course) {
        String courseName = course.getName();
        return courseName.equals("Java") ? points[0] :
                courseName.equals("DSA") ? points[1] :
                courseName.equals("Databases") ? points[2] :
                                                points[3];
    }

    public double getCourseCompletion(Course course) {
        String courseName = course.getName();
        return courseName.equals("Java") ? courseCompletion[0] :
                courseName.equals("DSA") ? courseCompletion[1] :
                courseName.equals("Databases") ? courseCompletion[2] :
                                courseCompletion[3];
    }


    public void setId(int id) {
        this.id = id;
    }

    public void addPoint(Course course, int point) {

        int currentPoint = getPoint(course);
        int newPoint = currentPoint + point;

        setPoint(course, newPoint);
        updateCourseCompletion(course);
    }

    public void updateCourseCompletion(Course course) {
        int currentPoint = getPoint(course);
        double newPercent = (double) currentPoint * 100 / course.getCompletionPoint();

        BigDecimal bd = new BigDecimal(Double.toString(newPercent));
        bd.setScale(1, RoundingMode.HALF_UP);
        newPercent = bd.doubleValue();

        setCourseCompletion(course, newPercent);
    }


    private void setPoint(Course course, int newPoint) {
        switch (course.getName()) {
            case "Java":
                points[0] = newPoint;
                break;
            case "DSA":
                points[1] = newPoint;
                break;
            case "Databases":
                points[2] = newPoint;
                break;
            case "Spring":
                points[3] = newPoint;
                break;
        }
    }

    private void setCourseCompletion(Course course, double newPercent) {
        switch (course.getName()) {
            case "Java":
                courseCompletion[0] = newPercent;
                break;
            case "DSA":
                courseCompletion[1] = newPercent;
                break;
            case "Databases":
                courseCompletion[2] = newPercent;
                break;
            case "Spring":
                courseCompletion[3] = newPercent;
                break;
        }
    }
}
