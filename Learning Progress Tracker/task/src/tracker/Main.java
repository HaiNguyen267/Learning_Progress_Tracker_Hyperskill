package tracker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import static tracker.Course.*;

public class Main {
    public static StudentManager studentManager = new StudentManager();
    public static Course[] courses = {JAVA, DSA, DATABASE, SPRING};

    public static void main(String[] args) {
        System.out.println("Learning Progress Tracker");
        Scanner sc = new Scanner(System.in);
        boolean exit = false;
        String input;

        while (!exit) {
            input = sc.nextLine();

            switch (input) {
                case "add students":
                    addStudentCommand();
                    break;
                case "add points":
                    addStudentPoint();
                    break;
                case "list":
                    listStudentId();
                    break;
                case "find":
                    findStudentCommand();
                    break;
                case "statistics":
                    printCourseStatistics();
                    break;
                case "notify":
                    notifyCommand();
                    break;
                case "exit":
                    exit = true;
                    System.out.println("Bye!");
                    break;
                default:
                    if (input.equals("back")) {
                        System.out.println("Enter 'exit' to exit the program.");
                    } else if (input.isBlank()) {
                        System.out.println("No input.");
                    } else {
                        System.out.println("ERROR: Unknown command");
                    }
            }
        }
    }

    private static void notifyCommand() {
        List<Student> notifiedStudent = new ArrayList<>();

        for (Course course : courses) {
            List<Student> studentsToNotify = course.getStudentsToNotify();

            if (!studentsToNotify.isEmpty()) {
                for (Student student : studentsToNotify) {
                    System.out.println(String.format("To: %s", student.getEmail()));
                    System.out.println("Re: Your Learning Progress");
                    System.out.println(String.format("Hello, %s %s! You have accomplished our %s course!", student.getFirstName(), student.getLastName(), course.getName()));

                }
            }

            notifiedStudent.addAll(studentsToNotify);
        }

        long totalNotifiedStudent = notifiedStudent.stream()
                .distinct()
                .count(); // if a student is notified for more than 1 course, it's still a student

        System.out.println(String.format("Total %d students have been notified.", totalNotifiedStudent));
    }

    private static void addStudentCommand() {

        Scanner sc = new Scanner(System.in);
        boolean back = false;
        String input;
        int addedStudentCount = 0;
        System.out.println("Enter student credentials or 'back' to return:");
        while (!back) {
            input = sc.nextLine();

            switch (input) {
                case "back":
                    System.out.println(String.format("Total %d students have been added.", addedStudentCount));
                    back = true;
                    break;
                default:
                    String[] credentials = input.split("\\s+");
                    if (credentials.length < 3) {
                        System.out.println("Incorrect credentials");
                    } else {
                        String firstName = credentials[0];// the first part the first name
                        String email = credentials[credentials.length - 1]; // the last part is the email:
                        String lastName = input.substring(firstName.length(), input.length() - email.length()).trim(); // the last name is the rest part

                        if (!isFirstNameCorrect(firstName)) {
                            System.out.println("Incorrect first name");
                        } else if (!isLastNameCorrect(lastName)) {
                            System.out.println("Incorrect last name");
                        } else if (!isEmailCorrect(email)) {
                            System.out.println("Incorrect email");
                        } else {
                            Student student = new Student(firstName, lastName, email);

                            if (studentManager.addStudent(student)) {
                                addedStudentCount++;
                            }
                        }
                    }
            }
        }
    }

    private static boolean isFirstNameCorrect(String firstName) {
        return isValidWord(firstName);
    }

    private static boolean isLastNameCorrect(String lastName) {
        String[] words = lastName.split("\\s+");

        for (String word : words) {
            // if a word in the last name is invalid, the whole last name is invalid
            if (!isValidWord(word)) {
                return false;
            }
        }

        return true;
    }

    private static boolean isValidWord(String word) {

        // if the word contains less than 2 characters
        if (word.length() < 2) {
            return false;
        }

        // if the word contains invalid characters
        String invalidCharacter = ".*[^A-Za-z-'].*";
        if (word.matches(invalidCharacter)) {
            return false;
        }

        // if the word contain invalid start or end characters
        String validStartAndEnd = "^[A-Za-z].*[A-Za-z]$";
        if (!word.matches(validStartAndEnd)) {
            return false;
        }

        // if the word contains 2 adjacent special characters
        String twoAdjacentSpecialCharacters = ".*--.*|.*''.*|.*-'.*|.*'-.*";
        if (word.matches(twoAdjacentSpecialCharacters)) {
            return false;
        }

        return true;// if the word is valid
    }

    private static boolean isEmailCorrect(String email) {
        String regex = "[^@]+@\\w+\\.\\w+"; // @ symbol only appears once at the end of the email, followed by the domain name
        return email.matches(regex);
    }

    private static void addStudentPoint() {

        Scanner sc = new Scanner(System.in);
        boolean back = false;
        String input;

        System.out.println("Enter an id and points or 'back' to return:");
        while (!back) {
            input = sc.nextLine();

            switch (input) {
                case "back":
                    back = true;
                default:
                   if (isCorrectPointFormat(input)) {
                       String[] parts = input.split("\\s+");
                       String studentId = parts[0];

                       int[] points = {Integer.parseInt(parts[1])
                               ,Integer.parseInt(parts[2])
                               ,Integer.parseInt(parts[3])
                               ,Integer.parseInt(parts[4])};


                       studentManager.addStudentPoint(studentId, points);
                   } else {
                       System.out.println("Incorrect points format.");
                   }
            }
        }
    }

    private static boolean isCorrectPointFormat(String input) {
        String[] parts = input.split("\\s+");

        if (parts.length != 5) {
            return false;
        }


        for (int i = 1; i < 5; i++) {

            try {
                int point = Integer.parseInt(parts[i]);

                if (point < 0) {
                    return false; // if the point is less than 0
                }
            } catch (NumberFormatException e) {
                return false; // if the point is not a number
            }
        }
        return true; // if the points are all correct
    }

    private static void listStudentId() {
        studentManager.listStudentId();
    }

    private static void findStudentCommand() {

        Scanner sc = new Scanner(System.in);
        boolean back = false;
        String input;
        System.out.println("Enter an id or 'back' to return:");
        while (!back) {
            input = sc.nextLine();

            switch (input) {
                case "back":
                    back = true;
                default:
                    studentManager.findStudent(input); // find the student with the specified id
            }
        }
    }

    private static void printCourseStatistics() {
        boolean dataAvailable = false;

        for (Course course : courses) {
            if (course.getEnrolledStudentNum() > 0) {
                dataAvailable = true; // if there is at least a course having enrolled student
                break;
            }
        }
        System.out.println("Type the name of a course to see details or 'back' to quit:");

        if (!dataAvailable) {
            System.out.println("Most popular: n/a\n" +
                    "Least popular: n/a\n" +
                    "Highest activity: n/a\n" +
                    "Lowest activity: n/a\n" +
                    "Easiest course: n/a\n" +
                    "Hardest course: n/a");
        } else {

            // find the highest number of enrollment
            int highestEnrollment = Arrays.stream(courses)
                    .map(Course::getEnrolledStudentNum).
                    max(Integer::compareTo).get();

            // find the name of the course with highest enrollment number
            List<String> mostPopularCourses = Arrays.stream(courses)
                    .filter(course -> course.getEnrolledStudentNum() == highestEnrollment)
                    .map(Course::getName)
                    .collect(Collectors.toList());

            // find the lowest number on enrollment
            int lowestEnrollment = Arrays.stream(courses)
                    .map(Course::getEnrolledStudentNum)
                    .min(Integer::compareTo).get();

            // find the course with lowest number of enrolment, the course must not exist in the most popular courses
            List<String> leastPopularCourses = Arrays.stream(courses)
                    .filter(course -> course.getEnrolledStudentNum() == lowestEnrollment)
                    .map(Course::getName)
                    .filter(name -> !mostPopularCourses.contains(name))
                    .collect(Collectors.toList());

            // find the highest number of activity (submission)
            int highestActivity = Arrays.stream(courses)
                    .map(Course::getTotalSubmission)
                    .max(Integer::compareTo).get();

            // find the course name with the highest number of activity (submission)
            List<String> highestActivityCourses = Arrays.stream(courses)
                    .filter(course -> course.getTotalSubmission() == highestActivity)
                    .map(Course::getName)
                    .collect(Collectors.toList());

            // find the lowest number of activity (submission)
            int lowestActivity = Arrays.stream(courses)
                    .map(Course::getTotalSubmission)
                    .min(Integer::compareTo).get();

            // find the course name with lowest number of activity (submission), the course must not exist the highest activity courses
            List<String> lowestActivityCourses = Arrays.stream(courses)
                    .filter(course -> course.getTotalSubmission() == lowestActivity)
                    .map(Course::getName)
                    .filter(name -> !highestActivityCourses.contains(name))
                    .collect(Collectors.toList());

            // find the highest average point per activity (submission)
            double highestAveragePoint = Arrays.stream(courses)
                    .filter(course -> course.getEnrolledStudentNum() > 0)
                    .map(Course::getAveragePoint)
                    .max(Double::compareTo).get();


            // find the course name with the highest average point per submission (easiest course)
            List<String> easiestCourses = Arrays.stream(courses)
                    .filter(course -> course.getEnrolledStudentNum() > 0)
                    .filter(course -> course.getAveragePoint() == highestAveragePoint)
                    .map(Course::getName)
                    .collect(Collectors.toList());

            // find the lowest average point per submission
            double lowestAveragePoint = Arrays.stream(courses)
                    .filter(course -> course.getEnrolledStudentNum() > 0)
                    .map(Course::getAveragePoint)
                    .min(Double::compareTo).get();

            // find the course name with lowest point per submission (hardest courses), the course must not exist in the easiest course before
            List<String> hardestCourses = Arrays.stream(courses)
                    .filter(course -> course.getEnrolledStudentNum() > 0)
                    .filter(course -> course.getAveragePoint() == lowestAveragePoint)
                    .map(Course::getName)
                    .filter(name -> !easiestCourses.contains(name))
                    .collect(Collectors.toList());


            String mostPopular = mostPopularCourses.isEmpty() ? "n/a" : String.join(", ", mostPopularCourses);
            String leastPopular = leastPopularCourses.isEmpty() ? "n/a" :  String.join(", ", leastPopularCourses);
            String mostActivity = highestActivityCourses.isEmpty() ? "n/a" : String.join(", ", highestActivityCourses);
            String leastActivity = lowestActivityCourses.isEmpty() ? "n/a" : String.join(", ", lowestActivityCourses);
            String easiest = easiestCourses.isEmpty() ? "n/a" : String.join(", ", easiestCourses);
            String hardest = hardestCourses.isEmpty() ? "n/a" : String.join(", ", hardestCourses);

            System.out.println("Most popular: " + mostPopular);
            System.out.println("Least popular: " + leastPopular);
            System.out.println("Highest activity: " + mostActivity);
            System.out.println("Lowest activity: " + leastActivity);
            System.out.println("Easiest course: " + easiest);
            System.out.println("Hardest course: " + hardest);


        }

        Scanner sc = new Scanner(System.in);
        boolean back = false;
        String input;

        while (!back) {
            input = sc.nextLine();

            switch (input) {
                case "Java":
                    JAVA.printCourseStatistics();
                    break;
                case "DSA":
                    DSA.printCourseStatistics();
                    break;
                case "Databases":
                    DATABASE.printCourseStatistics();
                    break;
                case "Spring":
                    SPRING.printCourseStatistics();
                    break;
                case "back":
                    back = true;
                case "exit":
                    System.out.println("Enter 'exit' to exit the program");
                    break;
                default:
                    System.out.println("Unknown course.");
            }
        }
    }

    private static void notifyLearners() {
        // TODO: your code here
    }
}
