import org.junit.Test;

import static org.junit.Assert.*;

import java.util.ArrayList;

public class CourseGradesTest {
    /*
    Testing only on courses from fall 2013 in order to make testing easier, since
    I'll need to calculate the expected results from these methods by hand. All other
    Json files in the data folder follow the exact same format, so these will work for any number of terms
     */
    private static ArrayList<Course> testCourses = CourseGrades.getCoursesOfTerm(CourseGrades.getCourses(), 120135);

    /*
    Filter methods tests for expected values
     */

    private final int TOTAL_NUM_COURSES = 9994;// This is the hand-calculated answer

    @Test
    public void jsonParseTest() {
        ArrayList<Course> allCourses = CourseGrades.createCourseArrayList();
        assertEquals(TOTAL_NUM_COURSES, allCourses.size());
    }

    @Test
    public void termFilterTest() {
        ArrayList<Course> filteredList = CourseGrades.getCoursesOfTerm(CourseGrades.getCourses(), 120135);

        for (Course c : filteredList) {
            assertEquals(true, c.getTerm() == 120135);
        }

        assertEquals(NUM_SUMMER2013_COURSES, filteredList.size());
    }

    private final int NUM_ADV_COURSES = 2;// This is the hand-calculated answer

    @Test
    public void subjectFilterTest() {
        ArrayList<Course> filteredList = CourseGrades.getCoursesOfSubject(testCourses, "ADV");

        for (Course c : filteredList) {
            assertEquals("ADV", c.getSubject());
        }

        assertEquals(NUM_ADV_COURSES, filteredList.size());
    }

    private final int NUM_IBRAHIM_COURSES = 3;// This is the hand-calculated answer

    @Test
    public void instructorFilterTest() {
        ArrayList<Course> filteredList = CourseGrades.getCoursesOfInstructor(testCourses, "Ibrahim, Adel Nematallah");

        for (Course c : filteredList) {
            assertEquals("Ibrahim, Adel Nematallah", c.getInstructor());
        }

        assertEquals(NUM_IBRAHIM_COURSES, filteredList.size());
    }

    private final int NUM_300_TO_303_COURSES = 7;// This is the hand-calculated answer

    @Test
    public void numRangeFilterTest() {
        ArrayList<Course> filteredList = CourseGrades.getCoursesInRange(testCourses, 300, 303);

        for (Course c : filteredList) {
            assertEquals(true, (c.getNumber() >= 300 && c.getNumber() <= 303));
        }

        assertEquals(NUM_300_TO_303_COURSES, filteredList.size());
    }

    private final int NUM_SUMMER2013_COURSES = 186;// This is the hand-calculated answer

    @Test
    public void studentRangeFilterTest() {
        ArrayList<Course> filteredList = CourseGrades.getCoursesWithStudentRange(testCourses, 20, 100);

        for (Course c : filteredList) {
            assertEquals(true, (c.calculateNumStudents() >= 20 && c.calculateNumStudents() <= 100));
        }

        // Accounts for edge cases instead of measuring against exact number of expected courses
        assertEquals(true,filteredList.size() > 0 && filteredList.size() < NUM_SUMMER2013_COURSES);
    }

    @Test
    public void gradeRangeFilterTest() {
        ArrayList<Course> filteredList = CourseGrades.getCoursesWithGradeRange(testCourses, 1.0, 2.0);

        for (Course c : filteredList) {
            assertEquals(true, (c.getAverageGrade() >= 1.0 && c.getAverageGrade() <= 2.0));
        }

        // Only one course with average gpa below 2.00
        assertEquals(1, filteredList.size());
    }

    /*
    Aggregate methods tests for expected values
     */

    private static final int NUM_TOTAL_TEST_STUDENTS = 108;// This is the hand-calculated answer

    @Test
    public void aggregateTotalStudentsTest() {
        ArrayList<Course> totalStudentTestCourses = CourseGrades.getCoursesOfInstructor(testCourses, "Ibrahim, Adel Nematallah");
        assertEquals(NUM_TOTAL_TEST_STUDENTS, CourseGrades.calculateTotalStudents(totalStudentTestCourses));
    }

    private static final int NUM_GRADE_TEST_STUDENTS = 63;// This is the hand-calculated answer

    @Test
    public void aggregateNumGradeTest() {
        ArrayList<Course> aggNumGradeTestCourses = CourseGrades.getCoursesOfInstructor(testCourses, "Ibrahim, Adel Nematallah");
        assertEquals(NUM_GRADE_TEST_STUDENTS,
                CourseGrades.calculateStudentsWithGrade(aggNumGradeTestCourses, "A-", "A+"));
    }

    private static final double NUM_AVERAGE_TEST_STUDENTS = 3.56;// This is the hand-calculated answer

    @Test
    public void aggregateAverageGpaTest() {
        ArrayList<Course> aggAvgGpaTestCourses = CourseGrades.getCoursesOfInstructor(testCourses, "Ibrahim, Adel Nematallah");
        assertEquals(NUM_AVERAGE_TEST_STUDENTS, CourseGrades.calculateAverageGpa(aggAvgGpaTestCourses), 0.001);
    }

    /*
    Invalid parameter tests for all methods with ranges as parameters
     */

    @Test
    public void courseRangeParameterTest() {
        assertEquals(null, CourseGrades.getCoursesInRange(testCourses, 1, 0));
    }

    @Test
    public void studentRangeParameterTest() {
        assertEquals(null, CourseGrades.getCoursesWithStudentRange(testCourses, 1, 0));
    }

    @Test
    public void avgGradeRangeParameterTest() {
        assertEquals(null, CourseGrades.getCoursesWithGradeRange(testCourses, 1, 0));
    }

    @Test
    public void aggregateNumGradeParameterTest() {
        assertEquals(-1, CourseGrades.calculateStudentsWithGrade(testCourses, "A+", "A-"));
    }

    /*
    Null tests for all methods
     */

    @Test
    public void termFilterNullTest() {
        assertEquals(null, CourseGrades.getCoursesOfTerm(null, 120135));
    }

    @Test
    public void subjectFilterNullTest() {
        assertEquals(null, CourseGrades.getCoursesOfSubject(null, "ADV"));
    }

    @Test
    public void instructorFilterNullTest() {
        assertEquals(null, CourseGrades.getCoursesOfInstructor(null, "Ibrahim, Adel Nematallah"));
    }

    @Test
    public void numRangeFilterNullTest() {
        assertEquals(null, CourseGrades.getCoursesInRange(null, 300, 303));
    }

    @Test
    public void studentRangeFilterNullTest() {
        assertEquals(null, CourseGrades.getCoursesWithStudentRange(null, 20, 100));
    }

    @Test
    public void gradeRangeFilterNullTest() {
        assertEquals(null, CourseGrades.getCoursesWithGradeRange(null, 1.0, 2.0));
    }

    @Test
    public void aggregateStudentsNullTest() {
        assertEquals(-1, CourseGrades.calculateTotalStudents(null));
    }

    @Test
    public void aggregateNumGradeNullTest() {
        assertEquals(-1, CourseGrades.calculateStudentsWithGrade(null, "A+", "A-"));
    }

    @Test
    public void aggregateAvgGpaNullTest() {
        assertEquals(-1.0, CourseGrades.calculateAverageGpa(null), 0.0);
    }

    /*
    Empty parameter tests for all methods
     */

    private ArrayList<Course> emptyList = new ArrayList<>();

    @Test
    public void termFilterEmptyTest() {
        assertEquals(0, CourseGrades.getCoursesOfTerm(emptyList, 120135).size());
    }

    @Test
    public void subjectFilterEmptyTest() {
        assertEquals(0, CourseGrades.getCoursesOfSubject(emptyList, "ADV").size());
    }

    @Test
    public void instructorFilterEmptyTest() {
        assertEquals(0, CourseGrades.getCoursesOfInstructor(emptyList, "Ibrahim, Adel Nematallah").size());
    }

    @Test
    public void numRangeFilterEmptyTest() {
        assertEquals(0, CourseGrades.getCoursesInRange(emptyList, 300, 303).size());
    }

    @Test
    public void studentRangeFilterEmptyTest() {
        assertEquals(0, CourseGrades.getCoursesWithStudentRange(emptyList, 20, 100).size());
    }

    @Test
    public void gradeRangeFilterEmptyTest() {
        assertEquals(0, CourseGrades.getCoursesWithGradeRange(emptyList, 1.0, 2.0).size());
    }

    @Test
    public void aggregateStudentsEmptyTest() {
        assertEquals(0, CourseGrades.calculateTotalStudents(emptyList));
    }

    @Test
    public void aggregateNumGradeEmptyTest() {
        assertEquals(0, CourseGrades.calculateStudentsWithGrade(emptyList, "A-", "A+"));
    }

    @Test
    public void aggregateAvgGpaEmptyTest() {
        assertEquals(0, CourseGrades.calculateAverageGpa(emptyList), 0.0);
    }
}
