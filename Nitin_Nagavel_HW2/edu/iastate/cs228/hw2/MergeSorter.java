package iastate.cs228.hw2;

import java.io.FileNotFoundException;
import java.lang.NumberFormatException; 
import java.lang.IllegalArgumentException; 
import java.util.InputMismatchException;

/**
 *  
 * @author Nitin Nagavel
 *
 */

/**
 * 
 * This class implements the mergesort algorithm.   
 *
 */

public class MergeSorter extends AbstractSorter
{
	// Other private instance variables if you need ... 
	
	/** 
	 * Constructor takes an array of points.  It invokes the superclass constructor, and also 
	 * set the instance variables algorithm in the superclass.
	 *  
	 * @param pts   input array of integers
	 */
	public MergeSorter(Point[] pts) 
	{
		super(pts);
		super.algorithm = "mergesort";
	}


	/**
	 * Perform mergesort on the array points[] of the parent class AbstractSorter. 
	 * 
	 */
	@Override 
	public void sort()
	{
		mergeSortRec(points);
	}

	
	/**
	 * This is a recursive method that carries out mergesort on an array pts[] of points. One 
	 * way is to make copies of the two halves of pts[], recursively call mergeSort on them, 
	 * and merge the two sorted subarrays into pts[].   
	 * 
	 * @param pts	point array 
	 */
	private void mergeSortRec(Point[] pts)
	{
		int length = pts.length;
		if(length <= 1){
			return;
		}
		Point[] left = new Point[length/2];
		Point[] right = new Point[length - length/2];

		for(int l = 0; l < length /2; l+=1){
			left[l] = pts[l];
		}
		for(int r = length/2, j = 0; r < length; r+=1){
			right[j++] = pts[r];
		}

		mergeSortRec(left);
		mergeSortRec(right);

		Point[] temp = new Point[pts.length];
		temp = merge(left, right);

		for (int iter = 0; iter < temp.length ; iter+=1) {
			pts[iter] = temp[iter];
		}
	}

	
	// Other private methods in case you need ...
	private Point[] merge(Point[] left, Point[] right){
		Point[] merged = new Point[left.length + right.length];

		int iter = 0, jiter = 0;
		int kiter = 0;
		while(iter < left.length && jiter < right.length){
			if(pointComparator.compare(left[iter], right[jiter]) <= 0){ //Compares each of sides of the merger
				merged[kiter] = left[iter];
				iter+=1;
			}
			else{
				merged[kiter] = right[jiter];
				jiter+=1;
			}
			kiter+=1;
		}
		while (iter < left.length){
			merged[kiter] = left[iter];
			iter+=1;
			kiter+=1;
		}
		while (jiter < right.length){
			merged[kiter] = right[jiter];
			jiter+=1;
			kiter+=1;
		}
		return merged;
	}
}
