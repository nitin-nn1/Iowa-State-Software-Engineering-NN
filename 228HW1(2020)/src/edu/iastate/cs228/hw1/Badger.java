package edu.iastate.cs228.hw1;

/**
 *  
 * @author
 * Nitin Nagavel
 *
 */

/**
 * A badger eats a rabbit and competes against a fox. 
 */
public class Badger extends Animal
{
	/**
	 * Constructor 
	 * @param p: plain
	 * @param r: row position 
	 * @param c: column position
	 * @param a: age 
	 */
	public Badger (Plain p, int r, int c, int a) 
	{
		// TODO
		plain = p;
		row = r;
		column = c;
		age = a;
	}
	
	/**
	 * A badger occupies the square. 	 
	 */
	public State who()
	{
		// TODO 
		return State.BADGER;
	}
	
	/**
	 * A badger dies of old age or hunger, or from isolation and attack by a group of foxes. 
	 * @param pNew     plain of the next cycle
	 * @return Living  life form occupying the square in the next cycle. 
	 */
	public Living next(Plain pNew){
		// TODO 
		// 
		// See Living.java for an outline of the function. 
		// See the project description for the survival rules for a badger.
		int arr[] = new int[NUM_LIFE_FORMS];
		this.census(arr);
		if(this.age >= BADGER_MAX_AGE){
			return new Empty(pNew, this.row, this.column);
		}
		else if(arr[FOX] > 1 && arr[BADGER] == 1 ){
			return new Fox(pNew, this.row, this.column, 0);
		}
		else if((arr[BADGER] + arr[FOX]) > arr[Living.RABBIT]){
			return new Empty(pNew, this.row, this.column);
		}
		else{
			return new Badger(pNew, row,column, age+=1);
		}
	}
}
