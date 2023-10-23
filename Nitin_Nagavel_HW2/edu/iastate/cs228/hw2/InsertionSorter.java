package iastate.cs228.hw2;

import java.io.FileNotFoundException;
import java.lang.NumberFormatException; 
import java.lang.IllegalArgumentException;
import java.sql.Array;
import java.sql.SQLOutput;
import java.util.InputMismatchException;


/**
 *  
 * @author Nitin Nagavel
 *
 */

/**
 * 
 * This class implements insertion sort.   
 *
 */

public class InsertionSorter extends AbstractSorter 
{
	// Other private instance variables if you need ... 
	
	/**
	 * Constructor takes an array of points.  It invokes the superclass constructor, and also 
	 * set the instance variables algorithm in the superclass.
	 * 
	 * @param pts  
	 */
	public InsertionSorter(Point[] pts) 
	{
		super(pts);
		super.algorithm = "insertion sort";
	}	

	
	/** 
	 * Perform insertion sort on the array points[] of the parent class AbstractSorter.  
	 */
	@Override 
	public void sort()
	{
		int iter;
		for(iter = 1; iter < points.length; iter++) {
			Point point = points[iter];
			int part = iter;
			while(part > 0 && pointComparator.compare(points[part - 1], point) > 0) {
				points[part] = points[part-1];
				part-=1;
			}
			if(part != iter) {
				points[part] = point;
			}
		}
	}
}
