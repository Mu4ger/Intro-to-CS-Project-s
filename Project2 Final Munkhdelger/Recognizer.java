import java.awt.Point;
import java.io.*;

/**
 * This class performs unistroke handwriting recognition using an algorithm
 * known as "elastic matching."
 * 
 * @author Dave Berque
 * @version August, 2004 Slightly modified by David E. Maharry and Carl Singer
 *          10/27/2004
 * 
 */

public class Recognizer {
    public static final int STROKESIZE = 150; // Max number of points in each
                                                // stroke
    private static final int NUMSTROKES = 10; // Number of strokes in the base
                                                // set (0 through 9)
    private Point[] userStroke; // holds points that comprise the user's stroke
    private int nextFree; // next free cell of the userStroke array

    private Point[][] baseSet; // holds points for each stroke (0-9) in the
                                // base set.

    // baseset is an array of arrays, a 2-D array.

    /**
     * Constructor for the recognizer class. Sets up the arrays and loads the
     * base set from an existing data file which is assumed to have the right
     * number of points in it. The file is organized so that there are 150
     * points for stroke 0 followed by 150 points for stroke 1, ... 150 poinpts
     * for stroke 9. Each stroke is organized as an alternating series of x, y
     * pairs. For example, stroke 0 consists of 300 lines with the first line
     * being x0 for stroke 0, the next line being y0 for stroke 0, the next line
     * being x1 for stroke 0 and so on.
     */
    public Recognizer()
    {
        int row, col, stroke, pointNum, x, y;
        String inputLine;

        userStroke = new Point[STROKESIZE];
        baseSet = new Point[NUMSTROKES][STROKESIZE];

        try {
            FileReader myReader = new FileReader("strokedata.txt");
            BufferedReader myBufferedReader = new BufferedReader(myReader);
            for (stroke = 0; stroke < NUMSTROKES; stroke++)
                for (pointNum = 0; pointNum < STROKESIZE; pointNum++) {
                    inputLine = myBufferedReader.readLine();
                    x = Integer.parseInt(inputLine);
                    inputLine = myBufferedReader.readLine();
                    y = Integer.parseInt(inputLine);
                    baseSet[stroke][pointNum] = new Point(x, y);
                }
            myBufferedReader.close();
            myReader.close();
        }
        catch (IOException e) {
            System.out.println("Error writing to file.\n");
        }
    }

    public void translate ()
    {
       int minx = (int) findMinX();
       int miny = (int) findMinY();
        for (int i = 0 ; i< nextFree; i++)
            {
                Point p = userStroke[i];
                p.translate (-minx, -miny);
            }
    }
    public void scale ()
    {
        double max = 0;
        double x = findMaxX();
        double y = findMaxY();
        if (x > y)
        {
            max = x;
            double ScaleFactor = 250.0 / (double) max;
            for (int i = 0; i < nextFree; i++)
            {
                userStroke [i].x = userStroke [i].x* (int) ScaleFactor;
                userStroke [i].y = userStroke [i].y* (int) ScaleFactor;
            }
        }
        else if (x < y)
        {
            max = y;
            double ScaleFactor = 250.0 / (double) max;
            for (int i = 0; i < nextFree; i++)
            {
                userStroke [i].x = userStroke [i].x* (int) ScaleFactor;
                userStroke [i].y = userStroke [i].y* (int) ScaleFactor;
            }
        }
    }
    
    private void insertOnePoint()
    {
        int maxPosition = 0, newX, newY, distance;
        // compute distance between point 0 and point 1
        int maxDistance = (int) userStroke[0].distance(userStroke[1]);
      
        for (int i = 1; i < nextFree - 1; i++) 
        {
            distance = (int) userStroke[i].distance(userStroke[i + 1]);
            if (distance > maxDistance) 
            {
                maxDistance = distance;
                maxPosition = i;
            }
        }
        // slide that are to the right of cell maxPosition right by one
        for (int i = nextFree; i > maxPosition + 1; i--)
            userStroke[i] = userStroke[i - 1];

        // Insert the average
        newX = (int) (userStroke[maxPosition].getX() + userStroke[maxPosition + 2]
                .getX()) / 2;
        newY = (int) (userStroke[maxPosition].getY() + userStroke[maxPosition + 2]
                .getY()) / 2;
        userStroke[maxPosition + 1] = new Point(newX, newY);

        nextFree++;
    }

    /**
     * normalizeNumPoints - Adds points to the userStroke by inserting points
     * repeatedly until there are STROKESIZE points in the stroke
     */
    public void normalizeNumPoints()
    {
        while (nextFree < STROKESIZE) {
            insertOnePoint();
        }
    }
    public double computeScore (int digitToCompare)
    {
        double sum = 0;
        for (int i = 0; i< nextFree; i++)
        {
            sum += userStroke[i].distance(baseSet[digitToCompare][i]);
        }
        return sum;
    }

    
    public int findMatch()
    {
        translate ();
        scale ();
        normalizeNumPoints();
        double min = computeScore(0);
        int count = 0;
            for (int i=0; i <= 9 ; i++)
            {
                if (min > computeScore(i))
                {
                    min = computeScore(i);
                    count = i;
                }
            }
        return count;
    }

    public double findMinX()
    {
     double minX = userStroke[0].x;
        for (int i = 1; i < nextFree; i++) 
        {
            if (userStroke[i].x < minX) 
            {
                minX = userStroke[i].x;
            }
        }
     return minX;
    }
    public double findMinY() 
    {
     double minY = userStroke[0].y;
        for (int i = 1; i < nextFree; i++) 
        {
            if (userStroke[i].y < minY) 
            {
                minY = userStroke[i].y;
            }
        }
     return minY;
    }
    public double findMaxX() 
    {
     double maxX = userStroke[0].x;
        for (int i = 1; i < nextFree; i++) 
        {
            if (userStroke[i].x > maxX) 
            {
                maxX = userStroke[i].x;
            }
        }
     return maxX;
    }
    
    public double findMaxY () 
    {
     double maxY = userStroke[0].y;
        for (int i = 1; i < nextFree; i++) 
        {
            if (userStroke[i].y > maxY) 
            {
                maxY = userStroke[i].y;
            }
        }
     return maxY;
    }
    
    public void resetUserStroke()
    {
        nextFree = 0;
    }

    // Returns the number of points currently in the user stroke array.
    public int numUserPoints()
    {
        return nextFree;
    }

    // This returns the x portion of the i'th point in the user array
    public int getUserPointX(int i)
    {
        if ((i >= 0) && (i < nextFree))
            return ((int) userStroke[i].getX());
        else {
            System.out.println("Invalid value of i in getUserPoint");
            return (0);
        }
    }

    // This returns the y portion of the i'th point in the user array
    public int getUserPointY(int i)
    {
        if ((i >= 0) && (i < nextFree))
            return ((int) userStroke[i].getY());
        else {
            System.out.println("Invalid value of i in getUserPoint");
            return (0);
        }
    }

    public void addUserPoint(Point newPoint)
    {
        if (nextFree < STROKESIZE) {
            userStroke[nextFree] = newPoint;
            nextFree++;
        }
    }
}
