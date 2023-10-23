package edu.iastate.cs228.hw1;

import org.junit.Test;

import java.io.FileNotFoundException;
import java.util.Scanner;
import static org.junit.Assert.*;

import java.io.FileNotFoundException;

import org.junit.*;
/**
 *  
 * @author
 * Nitin Nagavel;
 */

/**
 * 
 * The Wildlife class performs a simulation of a grid plain with
 * squares inhabited by badgers, foxes, rabbits, grass, or none. 
 *
 */
public class Wildlife 
{
	/**
	 * Update the new plain from the old plain in one cycle. 
	 * @param pOld  old plain
	 * @param pNew  new plain 
	 */
	public static void updatePlain(Plain pOld, Plain pNew){
	// TODO
		// 
		// For every life form (i.e., a Living object) in the grid pOld, generate  
		// a Living object in the grid pNew at the corresponding location such that 
		// the former life form changes into the latter life form. 
		// 
		// Employ the method next() of the Living class.
		int r, c;
		for(r = 0; r < pOld.getWidth(); r++) {
			for(c = 0; c < pOld.getWidth(); c++) {
				pNew.grid[r][c] = pOld.grid[r][c].next(pNew);
			}
		}
	}

	
	/**
	 * Repeatedly generates plains either randomly or from reading files. 
	 * Over each plain, carries out an input number of cycles of evolution. 
	 * @param args
	 * @throws FileNotFoundException
	 */
	public static void main(String[] args) throws FileNotFoundException {
		//Test for the Plain Class

		/*
		Plain p2 = new Plain(9);
		p2.randomInit(); //call only when using second constructor
		p2.write("grid2.txt");
		Plain p3 = new Plain("grid2.txt");

		System.out.println(p3.toString()); */

		// TODO
		Plain even;   				 // the plain after an even number of cycles
		Plain odd;                   // the plain after an odd number of cycles
		int trial_num = 1;
		int choice = -1; //any number but 1, 2, 3, 4
		Scanner keys = new Scanner(System.in); //First Scanner
		System.out.println("Simulation of Wildlife of the Plain");
		System.out.println("keys: 1 (random world) 2 (file input) 3 (exit)");
		while(choice != 3){
			int newwidth;
			int cycles = 0;
			String filename;
			System.out.print("Trial " + (trial_num + "") + ": ");
			choice = keys.nextInt();
			if(choice == 1){
				System.out.println("Random Plain");
				System.out.print("Enter grid width: ");
				newwidth = keys.nextInt();
				even = new Plain(newwidth);
				odd = new Plain(newwidth);
				even.randomInit();
			}
			else if(choice == 2){
				System.out.println("Plain input from a file");
				System.out.print("File name: ");
				filename = keys.next();//file name is stored in the first scanner
				even = new Plain(filename);
				odd = new Plain(even.getWidth());
			}
			else{

				break;
			}
			boolean odd2 = false;
			String init = even.toString();
			String fin = "";

			System.out.print("Enter the number of cycles: ");
			cycles = keys.nextInt();
			for(int col = 0; col < cycles; col++) {
				if(col % 2 == 1) {
					updatePlain(odd, even); //the odd rotations
					odd2 = true;
				}
				else {
					updatePlain(even, odd); //the even rotations
					odd2 = false;
				}
			}
			if (odd2 == true){
				fin = even.toString();
			}
			else{
				fin = odd.toString();
			}
			System.out.println("Initial world: " + '\n' + init);
			System.out.println("Final world: " + '\n' + fin);
			trial_num += 1;
		}
		keys.close();
		// Generate wildlife simulations repeatedly like shown in the 
		// sample run in the project description. 
		// 
		// 1. Enter 1 to generate a random plain, 2 to read a plain from an input
		//    file, and 3 to end the simulation. (An input file always ends with 
		//    the suffix .txt.)
		// 
		// 2. Print out standard messages as given in the project description. 
		// 
		// 3. For convenience, you may define two plains even and odd as below. 
		//    In an even numbered cycle (starting at zero), generate the plain 
		//    odd from the plain even; in an odd numbered cycle, generate even 
		//    from odd. 
		

		
		// 4. Print out initial and final plains only.  No intermediate plains should
		//    appear in the standard output.  (When debugging your program, you can 
		//    print intermediate plains.)
		// 
		// 5. You may save some randomly generated plains as your own test cases. 
		// 
		// 6. It is not necessary to handle file input & output exceptions for this 
		//    project. Assume data in an input file to be correctly formated. 
	}
}
