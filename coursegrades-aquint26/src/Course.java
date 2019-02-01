import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

public class Course {
    @SerializedName("CRN")
    private int crn;

    @SerializedName("Subject")
    private String subject;

    @SerializedName("Number")
    private int number;

    @SerializedName("Title")
    private String title;

    @SerializedName("Grades")
    private int[] grades;

    @SerializedName("Average")
    private double averageGrade;

    @SerializedName("Instructor")
    private String instructor;

    @SerializedName("Term")
    private int term;

    // For debugging
    public String toString(){
        return  "Subject: " + subject + "Instructor: " + instructor;
    }

    public int[] getGrades() {
        return grades;
    }

    public String getInstructor() {
        return instructor;
    }

    public int getCrn() {
        return crn;
    }

    public String getSubject() {
        return subject;
    }

    public int getNumber() {
        return number;
    }

    public double getAverageGrade() {
        return averageGrade;
    }

    public String getTitle() {
        return title;
    }

    public int getTerm(){
        return term;
    }

    public int calculateNumStudents(){
        int sum = 0;

        for (int i = 0; i < grades.length; i++) {
            sum += grades[i];
        }
        return sum;
    }
}
