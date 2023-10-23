package iastate.cs228.hw2;

/**
 * 
 * @author Nitin Nagavel
 *
 */
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Comparator;
import java.io.PrintWriter;
import java.util.Scanner;
import java.lang.IllegalArgumentException;

/**
 * 
 * This class sorts all the points in an array by polar angle with respect to a reference point whose x and y 
 * coordinates are respectively the medians of the x and y coordinates of the original points. 
 * 
 * It records the employed sorting algorithm as well as the sorting time for comparison. 
 *
 */
public class RotationalPointScanner  
{
	private Point[] points; 
	
	private Point medianCoordinatePoint;  // point whose x and y coordinates are respectively the medians of 
	                                      // the x coordinates and y coordinates of those points in the array points[].
	private Algorithm sortingAlgorithm;    
	
	protected String outputFileName;   // "select.txt", "insert.txt", "merge.txt", or "quick.txt"
	
	protected long scanTime; 	       // execution time in nanoseconds.

	protected long start;
	protected long end;
	protected long total1;
	protected long total2;
	protected long total3;
	
	/**
	 * This constructor accepts an array of points and one of the four sorting algorithms as input. Copy 
	 * the points into the array points[]. Set outputFileName. 
	 * 
	 * @param  pts  input array of points 
	 * @throws IllegalArgumentException if pts == null or pts.length == 0.
	 */
	public RotationalPointScanner(Point[] pts, Algorithm algo) throws IllegalArgumentException
	{
		if(pts == null || pts.length == 0){
			throw new IllegalArgumentException();
		}
		else{
			points = new Point[pts.length];
			for (int i = 0; i < pts.length; i++) {
				points[i] = new Point(pts[i].getX(), pts[i].getY());
			}
			sortingAlgorithm = algo;
			//NEED TO DOUBLE CHECK THIS
			if(sortingAlgorithm == Algorithm.SelectionSort){
				outputFileName = "select.txt";
			}
			else if(sortingAlgorithm == Algorithm.InsertionSort){
				outputFileName = "insert.txt";
			}
			else if(sortingAlgorithm == Algorithm.MergeSort){
				outputFileName = "merge.txt";
			}
			else if(sortingAlgorithm == Algorithm.QuickSort){
				outputFileName = "quick.txt";
			}
			else{
				outputFileName = null;
			}


		}
	}

	
	/**
	 * This constructor reads points from a file. Set outputFileName. 
	 * 
	 * @param  inputFileName
	 * @throws FileNotFoundException 
	 * @throws InputMismatchException   if the input file contains an odd number of integers
	 */
	protected RotationalPointScanner(String inputFileName, Algorithm algo) throws FileNotFoundException, InputMismatchException
	{
		File file = new File(inputFileName);
		Scanner sc = new Scanner(file);
		Scanner sc1 = new Scanner(file);
		int num = 0;
		while(sc.hasNextInt()) {
			sc.nextInt();
			num++;
		}
		if(num % 2 != 0) {
			throw new InputMismatchException();
		}
		points = new Point[num/2];
		int index = 0;
		while (sc1.hasNextInt()){
			int x1 = sc1.nextInt();
			int y1 = sc1.nextInt();
			Point p = new Point(x1, y1);
			points[index] = p;
			index++;
		}
		sc.close();
		sc1.close();
		sortingAlgorithm = algo;
		//NEED TO DOUBLE CHECK THIS
		if(sortingAlgorithm == Algorithm.SelectionSort){
			outputFileName = "select.txt";
		}
		else if(sortingAlgorithm == Algorithm.InsertionSort){
			outputFileName = "insert.txt";
		}
		else if(sortingAlgorithm == Algorithm.MergeSort){
			outputFileName = "merge.txt";
		}
		else if(sortingAlgorithm == Algorithm.QuickSort){
			outputFileName = "quick.txt";
		}
		else{
			outputFileName = null;
		}
	}

	
	/**
	 * Carry out three rounds of sorting using the algorithm designated by sortingAlgorithm as follows:  
	 *    
	 *     a) Sort points[] by the x-coordinate to get the median x-coordinate. 
	 *     b) Sort points[] again by the y-coordinate to get the median y-coordinate.
	 *     c) Construct medianCoordinatePoint using the obtained median x- and y-coordinates. 
	 *     d) Sort points[] again by the polar angle with respect to medianCoordinatePoint.
	 *  
	 * Based on the value of sortingAlgorithm, create an object of SelectionSorter, InsertionSorter, MergeSorter,
	 * or QuickSorter to carry out sorting. Copy the sorting result back onto the array points[] by calling 
	 * the method getPoints() in AbstractSorter. 
	 *      
	 * @param //algo
	 * @return
	 */
	public void scan()
	{
		// TODO
		AbstractSorter aSorter;
		if (sortingAlgorithm == Algorithm.SelectionSort){
			aSorter = new SelectionSorter(points);

		}
		else if (sortingAlgorithm == Algorithm.InsertionSort){
			aSorter = new InsertionSorter(points);
		}
		else if(sortingAlgorithm == Algorithm.QuickSort){
			aSorter = new QuickSorter(points);
		}
		else if(sortingAlgorithm == Algorithm.MergeSort){
			aSorter = new MergeSorter(points);
		}
		else{
			aSorter = null;
		}
		//First Round
		aSorter.setComparator(0);
		start = System.nanoTime();
		aSorter.sort();
		end = System.nanoTime();
		total1 = end - start;
		aSorter.getPoints(points);
		Point medX = aSorter.getMedian();
		//Second Round
		/////////////////////////////////
		aSorter.setComparator(1);
		start = System.nanoTime();
		aSorter.sort();
		end = System.nanoTime();
		total2 = end - start;
		aSorter.getPoints(points);
		Point medY = aSorter.getMedian();
		//Third Round
		/////////////////////////////////
		medianCoordinatePoint = new Point(medX.getX(), medY.getY());
		aSorter.setReferencePoint(medianCoordinatePoint);
		aSorter.setComparator(2);
		start = System.nanoTime();
		aSorter.sort();
		end = System.nanoTime();
		total3 = end - start;
		scanTime = total1 + total2 + total3;
		aSorter.getPoints(points);
		/////////////////////////////////


		// create an object to be referenced by aSorter according to sortingAlgorithm. for each of the three 
		// rounds of sorting, have aSorter do the following: 
		// 
		//     a) call setComparator() with an argument 0, 1, or 2. in case it is 2, must have made 
		//        the call setReferencePoint(medianCoordinatePoint) already. 
		//
		//     b) call sort(). 		
		// 
		// sum up the times spent on the three sorting rounds and set the instance variable scanTime. 
		
	}
	
	
	/**
	 * Outputs performance statistics in the format: 
	 * 
	 * <sorting algorithm> <size>  <time>
	 * 
	 * For instance, 
	 * 
	 * selection sort   1000	  9200867
	 * 
	 * Use the spacing in the sample run in Section 2 of the project description. 
	 */
	public String stats()
	{
		return "" + sortingAlgorithm + "\t" + points.length + "\t" + scanTime + "\n";
	}
	
	
	/**
	 * Write points[] after a call to scan().  When printed, the points will appear 
	 * in order of polar angle with respect to medianCoordinatePoint with every point occupying a separate 
	 * line.  The x and y coordinates of the point are displayed on the same line with exactly one blank space 
	 * in between. 
	 */

	@Override
	public String toString()
	{
		String str = "";
		for (int i = 0; i < points.length; i++){
			str += "(";
			str += points[i].getX();
			str += ",";
			str += " ";
			str += points[i].getY();
			str += ")";
			str += "\n";
		}
		return str;
	}

	
	/**
	 *  
	 * This method, called after scanning, writes point data into a file by outputFileName. The format 
	 * of data in the file is the same as printed out from toString().  The file can help you verify 
	 * the full correctness of a sorting result and debug the underlying algorithm. 
	 * 
	 * @throws FileNotFoundException
	 */
	public void writePointsToFile() throws FileNotFoundException
	{
		try{
			File f = new File(outputFileName); //Came straight from my first project
			PrintWriter p = new PrintWriter(f);
			String s = this.toString();
			p.write(s);
			p.close();
		}
		catch (FileNotFoundException e){
			System.out.println("File does not exist");
		}
	}

	/**
	 * This method is called after each scan for visually check whether the result is correct.  You  
	 * just need to generate a list of points and a list of segments, depending on the value of 
	 * sortByAngle, as detailed in Section 4.1. Then create a Plot object to call the method myFrame().  
	 */
	public void draw()
	{

		int numSegs = points.length;  // number of segments to draw
		Segment[] segments = new Segment[numSegs * 2];
		int nextSeg = 1;
		for (int i = 0; i < numSegs; i++) {
			segments[i] = new Segment(medianCoordinatePoint, points[i]);
			//System.out.println(points[i].toString());
			segments[numSegs+i] = new Segment(points[i], points[nextSeg++]);

			if(nextSeg > numSegs - 1){
				nextSeg = 0;
			}
		}

		// Based won Section 4.1, generate the line segments to draw for display of the sorting result.
		// Assign their number to numSegs, and store them in segments[] in the order.
		String sort = null; 
		
		switch(sortingAlgorithm)
		{
		case SelectionSort: 
			sort = "Selection Sort"; 
			break; 
		case InsertionSort: 
			sort = "Insertion Sort"; 
			break; 
		case MergeSort: 
			sort = "Mergesort"; 
			break; 
		case QuickSort: 
			sort = "Quicksort"; 
			break; 
		default: 
			break; 		
		}
		// The following statement creates a window to display the sorting result.
		Plot.myFrame(points, segments, sort);
		
	}
		
}
