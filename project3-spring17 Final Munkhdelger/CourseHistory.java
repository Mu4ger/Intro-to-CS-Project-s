import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.util.ArrayList;

import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.util.ArrayList;



//NOTES:  You will need the import statements that appear at the top of this file, so you should
//leave them in place.  Follow the list of steps on the project writeup to complete the CourseHistory
//class that is started below.  This entire block of comments should be deleted when you are done. 


public class CourseHistory
{
    private ArrayList <CompletedCourse> courseList; // An ArrayList of completed Course
    
    public CourseHistory ()
    {
        courseList = new ArrayList <CompletedCourse>();
        String department;
        String courseNumber;
        String semesterTaken;
        String credit;
        String grade;
        String competency;
        String distArea;
        try
       {
           FileReader reader = new FileReader("finishedcourses.txt");
           Scanner in = new Scanner(reader);
       
           while(in.hasNextLine()) // this loops reads from the finishedcouse.txt file 
           {  department = in.nextLine();  
              courseNumber = in.nextLine();
              semesterTaken = in.nextLine();
              credit = in.nextLine();
              grade = in.nextLine();
              competency = in.nextLine();
              distArea = in.nextLine();  
              CompletedCourse theCourse = new CompletedCourse (department, courseNumber, semesterTaken, credit, grade, competency, distArea);
              courseList.add(theCourse);
              theCourse.displayCourse();   
          }
          in.close();  //Close the file when we are done reading it
       } catch (IOException exception)
       {
          System.out.println("Error processing file: " + exception);
       }  
    }
    public void displayCourseHistory() // shows/dispalys every course
    {
        System.out.println ("Course History");
        for (int i = 0; i < courseList.size() ; i++)
        {
            courseList.get(i).displayCourse();
        }
    }
    public void displaySummaryReport () // calculates the GPA
    {
        System.out.println ("Summary Report"); 
        double CourseCred = 0;
        double GradeValue = 0;
        double totalGradePoint = 0;
        double coursecredAtm = 0;
        for (int i = 0; i<courseList.size();i++)
        {
            if ( courseList.get(i).getGrade() > 0)
            {
                totalGradePoint += courseList.get(i).getGrade() * courseList.get(i).getCredit();
            }
            coursecredAtm += courseList.get(i).getCredit();
        }
        System.out.println("Final GPA: " + totalGradePoint/coursecredAtm);
    }
    public void distAreaReport() // shows the completion of each competency
    {
        System.out.println ("Distribution Area Report");
        int c=0;
        for (int i= 0; i < courseList.size(); i++)
        {
            if (courseList.get(i).getGrade() > 0.0)
            {
                if (courseList.get(i).getDistArea().equals("AH"))
                {
                    c += courseList.get(i).getCredit();
                    courseList.get(i).displayCourse();
                }
            }
        }
        System.out.println("AH credits completed: " + c);
        c = 0;
        for (int i= 0; i < courseList.size(); i++)
        {
            if (courseList.get(i).getGrade() > 0.0)
            {
                if (courseList.get(i).getDistArea().equals("SM"))
                {
                    c += courseList.get(i).getCredit();
                    courseList.get(i).displayCourse();
                }
            }
        }
        System.out.println ("SM credits completed: "+ c);
        c=0;
        for (int i= 0; i < courseList.size(); i++)
        {
            if (courseList.get(i).getGrade() > 0.0)
            {
                if (courseList.get(i).getDistArea().equals("LA"))
                {
                    c += courseList.get(i).getCredit();
                    courseList.get(i).displayCourse();
                }
            }
        }
        System.out.println ("LA credits completed: "+ c);
        c=0;
        for (int i= 0; i < courseList.size(); i++)
        {
            if (courseList.get(i).getGrade() > 0.0)
            {
                if (courseList.get(i).getDistArea().equals("SS"))
                {
                    c += courseList.get(i).getCredit();
                    courseList.get(i).displayCourse();
                }
            }
        }
        System.out.println ("SS credits completed: "+ c);
        c=0;
    }
    public void compReport() // showsw how many credits are left for each competency
    {
        int W =0;
        int S = 0;
        int Q = 0;
        System.out.println ("Competency Report: ");
        for (int i=0; i < courseList.size() ; i++)
        {
            if (courseList.get(i).getGrade() > 0)
            {
                if (courseList.get(i).getCompetency().equals("W"))
                {
                    W++;
                }
                else if(courseList.get(i).getCompetency().equals("S"))
                {
                    S++;
                }
                else if(courseList.get(i).getCompetency().equals("Q"))
                {
                    Q++;
                }
            }
        }
        if (W >= 1 & S >= 1 & Q >= 1)
        {
            System.out.println ("All Competencies completed.");
        }
        else if ( W == 0 & S == 0 & Q == 0)
        {
            System.out.println("No competencies Completed");
        }
        else 
        {
            System.out.println ("Competencies Partially Completed");
            if ( W >= 1 )
            {
                System.out.println ("W credits complete: ");
            }
            else 
            {
                System.out.println ("W Credits incomplete: ");
            }
            if ( S >= 1 )
            {
                System.out.println ("S credits complete: ");
            }
            else 
            {
                System.out.println ("S Credits incomplete: ");
            }
            if ( Q >= 1 )
            {
                System.out.println ("Q credits complete: ");
            }
            else 
            {
                System.out.println ("Q Credits incomplete: ");
            }
        }
    }
    public void fullReport() // calls all methods above to show full report
    {
        System.out.println("Full Report");
        displaySummaryReport ();
        distAreaReport();
        compReport();
    }
    public void sortListGPA() // sorts the gpa in descending order 
    {
        ArrayList <CompletedCourse> sortGPA;
        sortGPA = new ArrayList <CompletedCourse> ();
        for (int i = 0; i< courseList.size(); i++)
        {
            sortGPA.add(courseList.get(i));
        }
        ArrayList <CompletedCourse> xyz = new ArrayList <CompletedCourse>();
        for (int x = 0 ; x < sortGPA.size()-1; x++)
        {
            for (int i = 0; i < sortGPA.size()-1; i++)
            {
                if (sortGPA.get(i).getGrade() < sortGPA.get(i+1).getGrade())
                {
                    xyz.add (0,sortGPA.get(i));
                    sortGPA.set(i,sortGPA.get(i+1));
                    sortGPA.set(i+1,xyz.get(0));
                }
            }
        }
        for(int i = 0; i<sortGPA.size();i++)
        {
            sortGPA.get(i).displayCourse();
        }
    }
}













