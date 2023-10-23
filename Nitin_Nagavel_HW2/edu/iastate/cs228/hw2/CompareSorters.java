package iastate.cs228.hw2;

/**
 *  
 * @author Nitin Nagavel
 *
 */

/**
 * 
 * This class executes four sorting algorithms: selection sort, insertion sort, mergesort, and
 * quicksort, over randomly generated integers as well integers from a file input. It compares the 
 * execution times of these algorithms on the same input. 
 *
 */

import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Random; 

public class CompareSorters 
{
	/**
	 * Repeatedly take integer sequences either randomly generated or read from files. 
	 * Use them as coordinates to construct points.  Scan these points with respect to their 
	 * median coordinate point four times, each time using a different sorting algorithm.  
	 * 
	 * @param args
	 **/
	public static void main(String[] args) throws FileNotFoundException
	{
		// Conducts multiple rounds of comparison of four sorting algorithms.  Within each round,
		// set up scanning as follows: 
		// 
		//    a) If asked to scan random points, calls generateRandomPoints() to initialize an array 
		//       of random points. 
		// 
		//    b) Reassigns to the array scanners[] (declared below) the references to four new 
		//       RotationalPointScanner objects, which are created using four different values  
		//       of the Algorithm type:  SelectionSort, InsertionSort, MergeSort and QuickSort.
		int trial_num = 1;
		int choice = -1; //any number but 1, 2, 3, 4
		Scanner key = new Scanner(System.in);
		RotationalPointScanner[] scanners = new RotationalPointScanner[4];
		System.out.println("Performances of Four Sorting Algorithms in Point Scanning");
		System.out.println("keys: 1 (random integers)  2 (file input)  3 (exit)");
		while(choice != 3){
			int randompts;
			String filename;
			System.out.print("Trial " + (trial_num + "") + ": ");
			choice = key.nextInt();
			if(choice == 1) {
				System.out.print("Enter number of random points: ");
				randompts = key.nextInt();
				System.out.println("algorithm size time (ns)");
				System.out.println("----------------------------------");
				Random gen = new Random();
				Point[] randompt = generateRandomPoints(randompts, gen);
				scanners[0] = new RotationalPointScanner(randompt, Algorithm.SelectionSort);
				scanners[0].scan();
				scanners[0].draw();
				System.out.println(scanners[0].stats());
				////////////////////////////////////////
				scanners[1] = new RotationalPointScanner(randompt, Algorithm.InsertionSort);
				scanners[1].scan();
				scanners[1].draw();
				System.out.println(scanners[1].stats());
				////////////////////////////////////////
				scanners[2] = new RotationalPointScanner(randompt, Algorithm.MergeSort);
				scanners[2].scan();
				scanners[2].draw();
				System.out.println(scanners[2].stats());
				////////////////////////////////////////
				scanners[3] = new RotationalPointScanner(randompt, Algorithm.QuickSort);
				scanners[3].scan();
				scanners[3].draw();
				System.out.println(scanners[3].stats());
				System.out.println("----------------------------------");
			}
			else if(choice == 2){
				System.out.println("Points from a file");
				System.out.print("File name: ");
				filename = key.next();
				System.out.println("algorithm size time (ns)");
				System.out.println("----------------------------------");
				/////////////////////////////////////////////////////////
				scanners[0] = new RotationalPointScanner(filename, Algorithm.SelectionSort); //Does the graph for SelectionSort from the file
				scanners[0].scan();
				scanners[0].draw(); //Draws
				System.out.println(scanners[0].stats()); //Outputs the stats
				////////////////////////////////////////////////////////
				scanners[1] = new RotationalPointScanner(filename, Algorithm.InsertionSort); //Does the graph for InsertionSort from file
				scanners[1].scan();
				scanners[1].draw();
				System.out.println(scanners[1].stats());
				///////////////////////////////////////////////////////
				scanners[2] = new RotationalPointScanner(filename, Algorithm.MergeSort); //Does the graph for MergeSort from the file
				scanners[2].scan();
				scanners[2].draw();
				System.out.println(scanners[2].stats());
				//////////////////////////////////////////////////////
				scanners[3] = new RotationalPointScanner(filename, Algorithm.QuickSort); //Does the graph for QuickSort from the file
				scanners[3].scan();
				scanners[3].draw();
				System.out.println(scanners[3].stats());
				System.out.println("----------------------------------");
			}
			else{
				break;
			}


			trial_num += 1;
		}
		key.close();
		// For each input of points, do the following.
		// 
		//     a) Initialize the array scanners[].  
		//
		//     b) Iterate through the array scanners[], and have every scanner call the scan() and draw() 
		//        methods in the RotationalPointScanner class.  You can visualize the result of each scan.  
		//        (Windows have to be closed manually before rerun.)  
		// 
		//     c) After all four scans are done for the input, print out the statistics table (cf. Section 2). 
		//
		// A sample scenario is given in Section 2 of the project description.
	}

	/**
	 * This method generates a given number of random points.
	 * The coordinates of these points are pseudo-random numbers within the range 
	 * [-50,50] � [-50,50]. Please refer to Section 3 on how such points can be generated.
	 * 
	 * Ought to be private. Made public for testing. 
	 * 
	 * @param numPts  	number of points
	 * @param rand      Random object to allow seeding of the random number generator
	 * @throws IllegalArgumentException if numPts < 1
	 */
	public static Point[] generateRandomPoints(int numPts, Random rand) throws IllegalArgumentException
	{
		if(numPts < 1) {
			throw new IllegalArgumentException();
		}
		Point[] pts = new Point[numPts];
		int r;
		for(r = 0; r < pts.length; r++) {
			pts[r] = new Point(rand.nextInt(101) - 50, rand.nextInt(101) - 50);
		}
		return pts;
	}
	
}
