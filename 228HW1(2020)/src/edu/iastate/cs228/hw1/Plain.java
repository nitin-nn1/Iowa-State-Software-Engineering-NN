package edu.iastate.cs228.hw1;

/**
 *  
 * @author
 * Nitin Nagavel
 */

import java.io.File; 
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.Random; 

/**
 * 
 * The plain is represented as a square grid of size width x width. 
 *
 */
public class Plain{
	private int width; // grid size: width X width 
	//protected int age; //gets the age to use for other classes within the package
	public Living[][] grid;
	
	/**
	 *  Default constructor reads from a file 
	 */
	public Plain(String inputFileName) throws FileNotFoundException{
		// Assumption: The input file is in correct format. 
		// 
		// You may create the grid plain in the following steps: 
		// 
		// 1) Reads the first line to determine the width of the grid.
		// 
		// 2) Creates a grid object. 
		// 
		// 3) Fills in the grid according to the input file. 
		// 
		// Be sure to close the input file when you are done.
		File file = new File(inputFileName);
		Scanner scanner = new Scanner(file);
		Scanner scanner1 = new Scanner(file);


		while (scanner.hasNext()){ //checks for the filename
			String temp = scanner.next();
			if (scanner.nextLine() != "\n") {
				width += 1;
			}
			//System.out.println(width);
			//scanner.next();

		}
		grid = new Living[width][width];
		for (int r = 0; r < width; r++) {
			for (int c = 0; c < width; c++) {
				String living = scanner1.next();
				if (living.contains("B")){
					grid[r][c] = new Badger(this, r, c, Integer.parseInt(living.charAt(1) + ""));
				}
				if (living.contains("E")){
					grid[r][c] = new Empty(this, r, c);
				}
				if (living.contains("F")){
					grid[r][c] = new Fox(this, r, c, Integer.parseInt(living.charAt(1) + ""));
				}
				if (living.contains("G")){
					grid[r][c] = new Grass(this, r, c);
				}
				if (living.contains("R")){
					grid[r][c] = new Rabbit(this, r, c, Integer.parseInt(living.charAt(1) + ""));
				}
			}

		}
		scanner.close();
		scanner1.close();
	}
	
	/**
	 * Constructor that builds a w x w grid without initializing it.
	 * @param w - width of the the grid
	 */
	public Plain(int w){
		width = w;
		grid = new Living[width][width];
	}
	
	
	public int getWidth(){
		return width;
	}
	
	/**
	 * Initialize the plain by randomly assigning to every square of the grid  
	 * one of BADGER, EMPTY, FOX, GRASS, or RABBIT.
	 * 
	 * Every animal starts at age 0.
	 */
	public void randomInit(){
		//Plain p = null;
		Random generator = new Random();
		for(int r = 0; r < width; r++){
			for(int c = 0; c < width; c++){
				int gen = generator.nextInt(Living.NUM_LIFE_FORMS);
				if (gen == 0){
					grid[r][c] = new Badger(this, r, c, 0);
				}
				else if (gen == 1){
					grid[r][c] = new Empty(this, r, c);
				}
				else if (gen == 2){
					grid[r][c] = new Fox(this, r, c, 0);
				}
				else if (gen == 3){
					grid[r][c] = new Grass(this, r, c);
				}
				else if (gen == 4){
					grid[r][c] = new Rabbit(this, r, c, 0);
				}
			}
		}
	}
	
	
	/**
	 * Output the plain grid. For each square, output the first letter of the living form
	 * occupying the square. If the living form is an animal, then output the age of the animal 
	 * followed by a blank space; otherwise, output two blanks.  
	 */

	public String toString(){
		String animals = ""; //DOUBLE CHECK TO MAKE SURE THIS IS RIGHT
		String total = "";
		for(int r = 0;r < width; r++) {
			for (int c = 0; c < width; c++) {
				if (grid[r][c] instanceof Badger){
					animals = ("B" + ((Animal)grid[r][c]).myAge() + " ");
				}
				else if(grid[r][c] instanceof Empty){
					animals = ("E" + "  ");
				}
				else if(grid[r][c] instanceof Fox){
					animals = ("F" + ((Animal)grid[r][c]).myAge() + " ");
				}
				else if(grid[r][c] instanceof Grass){
					animals = ("G" + "  ");
				}
				else if(grid[r][c] instanceof Rabbit){
					animals = ("R" + ((Animal)grid[r][c]).myAge() + " ");
				}

				total += animals;
			}
			//total += animals;
			total += "\n";
		}
		return total;
	}


	/**
	 * Write the plain grid to an output file.  Also useful for saving a randomly 
	 * generated plain for debugging purpose. 
	 * @throws FileNotFoundException
	 */
	public void write(String outputFileName) throws FileNotFoundException {
		// 
		// 1. Open the file. 
		// 
		// 2. Write to the file. The five life forms are represented by characters 
		//    B, E, F, G, R. Leave one blank space in between. Examples are given in
		//    the project description. 
		// 
		// 3. Close the file.
		File f = new File(outputFileName);
		PrintWriter p = new PrintWriter(f);
		String s = this.toString();
		p.write(s);
		p.close();
	}			
}
