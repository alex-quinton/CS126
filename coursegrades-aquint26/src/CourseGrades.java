import com.google.gson.Gson;

import java.util.ArrayList;

public class CourseGrades {

    // Makes arrayLists that wil contain names of Json files and all of the course objects
    private static Gson gson = new Gson();
    private static ArrayList<String> fileNames = new ArrayList<String>(Data.getJsonFilesAsList());
    private static ArrayList<Course> courses = createCourseArrayList();

    // Creates an ArrayList that contains all the courses in the Json files
    public static ArrayList<Course> createCourseArrayList() {
        ArrayList<Course> arrayListToReturn = new ArrayList<Course>();

        // Iterates over the list of filenames
        for (String s : fileNames) {

            // Turns each file into a string, and uses that string to make an array of Course objects
            String jsonData = Data.getFileContentsAsString(s);
            Course[] coursesFromCurrentSemester = gson.fromJson(jsonData, Course[].class);

            for (int j = 0; j < coursesFromCurrentSemester.length; j++) {
                //adds each course to the main courses arraylist
                arrayListToReturn.add(coursesFromCurrentSemester[j]);
            }
        }
        return arrayListToReturn;
    }

    public static ArrayList<Course> getCourses() {
        return courses;
    }

    /*
     *Filter methods
     */

    // Filters courses by subject
    public static ArrayList<Course> getCoursesOfSubject(ArrayList<Course> coursesToSearch, String subject) {

        if (coursesToSearch == null || subject == null) {
            return null;
        }

        ArrayList<Course> filteredList = new ArrayList<Course>();

        for (Course c : coursesToSearch) {
            //Checks if this course has the specified subject
            if (c.getSubject().equals(subject)) {
                filteredList.add(c);
            }
        }

        return filteredList;
    }

    // Filters courses by instructor
    public static ArrayList<Course> getCoursesOfInstructor(ArrayList<Course> coursesToSearch, String instructor) {

        if (coursesToSearch == null || instructor == null) {
            return null;
        }

        ArrayList<Course> filteredList = new ArrayList<Course>();

        for (Course c : coursesToSearch) {
            //checks if this course has the specified instructor
            if (c.getInstructor().equals(instructor)) {
                filteredList.add(c);
            }
        }

        return filteredList;
    }

    // Filters courses by course number
    public static ArrayList<Course> getCoursesInRange(ArrayList<Course> coursesToSearch, int min, int max) {

        if (min > max || coursesToSearch == null) {
            return null;
        }

        ArrayList<Course> filteredList = new ArrayList<Course>();

        for (Course c : coursesToSearch) {
            //Checks if this course's number is within the specified range
            if (c.getNumber() <= max
                    && c.getNumber() >= min) {
                filteredList.add(c);
            }
        }

        return filteredList;
    }

    // Filters courses by number of enrolled students
    public static ArrayList<Course> getCoursesWithStudentRange(ArrayList<Course> coursesToSearch, int min, int max) {

        if (min > max || coursesToSearch == null) {
            return null;
        }

        ArrayList<Course> filteredList = new ArrayList<Course>();

        for (Course c : coursesToSearch) {
            //checks if this course has the specified range of students
            if (c.calculateNumStudents() <= max
                    && c.calculateNumStudents() >= min) {
                filteredList.add(c);
            }
        }

        return filteredList;
    }

    // Filters courses by average grade
    public static ArrayList<Course> getCoursesWithGradeRange(ArrayList<Course> coursesToSearch, double min, double max) {

        if (min > max || coursesToSearch == null) {
            return null;
        }

        ArrayList<Course> filteredList = new ArrayList<Course>();

        for (Course c : coursesToSearch) {
            //checks if this course has a grade average that's within the boundaries
            if (c.getAverageGrade() <= max
                    && c.getAverageGrade() >= min) {
                filteredList.add(c);
            }
        }

        return filteredList;
    }

    // Filters courses by term
    public static ArrayList<Course> getCoursesOfTerm(ArrayList<Course> coursesToSearch, int term) {

        if (coursesToSearch == null) {
            return null;
        }

        ArrayList<Course> filteredList = new ArrayList<Course>();

        for (Course c : coursesToSearch) {
            //Checks if this course is in the specified term
            if (c.getTerm() == term) {
                filteredList.add(c);
            }
        }

        return filteredList;
    }


    /*
     *Aggregating methods
     */

    // Calculates total number of students in the ArrayList parameter
    public static int calculateTotalStudents(ArrayList<Course> coursesToCountFrom) {

        if (coursesToCountFrom == null) {
            return -1;
        }

        int sum = 0;

        for (Course c : coursesToCountFrom) {
            sum += c.calculateNumStudents();
        }

        return sum;
    }

    // Calculates how many students received grades within the specified range (uses helper method convertLetterGrade)
    public static int calculateStudentsWithGrade(ArrayList<Course> coursesToCountFrom, String minLetterGrade, String maxLetterGrade) {

        if (coursesToCountFrom == null || minLetterGrade == null || maxLetterGrade == null) {
            return -1;
        }

        int min = convertLetterGrade(minLetterGrade); // Index of the minimum grade
        int max = convertLetterGrade(maxLetterGrade); // Index of the maximum grade
        int sum = 0;

        if (min < max) {
            return -1;
        }

        // Outer loop iterates over list, inner loop iterates over grades in the grade array
        for (Course c : coursesToCountFrom) {
            for (int gradeIndex = max; gradeIndex <= min; gradeIndex++) {
                sum += c.getGrades()[gradeIndex];
            }
        }

        return sum;
    }

    // Calculates the average GPA of the courses in the ArrayList Parameter
    public static double calculateAverageGpa(ArrayList<Course> coursesToCountFrom) {

        if (coursesToCountFrom == null) {
            return -1.0;
        }

        if (coursesToCountFrom.size() == 0) {
            return 0;
        }

        double intermediateSum = 0.0;

        for (Course c : coursesToCountFrom) {
            intermediateSum += c.getAverageGrade();
        }

        double averageOfCourses = intermediateSum / (double) (coursesToCountFrom.size());
        return averageOfCourses;
    }

    private static final String ILLEGAL_LETTER_GRADE = "Parameter must be a letter grade from A+ to F, or W";

    // Helper method to turn letter grade string into an index within the grades array
    private static int convertLetterGrade(String letter) throws IllegalArgumentException {
        try {
            if (letter.equals("A+")) {
                return 0;
            }
            if (letter.equals("A")) {
                return 1;
            }
            if (letter.equals("A-")) {
                return 2;
            }
            if (letter.equals("B+")) {
                return 3;
            }
            if (letter.equals("B")) {
                return 4;
            }
            if (letter.equals("B-")) {
                return 5;
            }
            if (letter.equals("C+")) {
                return 6;
            }
            if (letter.equals("C")) {
                return 7;
            }
            if (letter.equals("C-")) {
                return 8;
            }
            if (letter.equals("D+")) {
                return 9;
            }
            if (letter.equals("D")) {
                return 10;
            }
            if (letter.equals("D-")) {
                return 11;
            }
            if (letter.equals("F")) {
                return 12;
            }
            if (letter.equals("W")) {
                return 13;
            }

            throw new IllegalArgumentException(ILLEGAL_LETTER_GRADE);

        } catch (IllegalArgumentException e) {
            return -1;
        }
    }
}
