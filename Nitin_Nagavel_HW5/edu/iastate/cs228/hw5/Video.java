package edu.iastate.cs228.hw5;

/**
 *
 * @author Nitin Nagavel
 *
 */

public class Video implements Comparable<Video>
{
    private String film;           // film title for the video
    private int numCopies;
    private int numRentedCopies;

    /**
     *
     * @param film
     * @param n
     * @throws IllegalArgumentException if copies <= 0
     */
    public Video(String film, int n) throws IllegalArgumentException
    {
        if(n <= 0){
            System.out.print("Film " + film + " has an invalid request\n");
            throw new IllegalArgumentException();
        }
        else{
            this.film = film;
            numCopies = n;
        }
    }


    public Video(String film)
    {
        this(film, 1);
    }


    public String getFilm()
    {
        return film;
    }


    public int getNumCopies()
    {
        return numCopies;
    }


    /**
     *
     * @param n
     * @throws IllegalArgumentException if n <= 0
     */
    public void addNumCopies(int n) throws IllegalArgumentException
    {
        if(n <= 0){
            System.out.print("Film " + film + " has an invalid request\n");
            throw new IllegalArgumentException();
        }
        else{
            numCopies += n;
        }
    }

    public int getNumAvailableCopies()
    {
        return numCopies - numRentedCopies;
    }


    public int getNumRentedCopies()
    {
        return numRentedCopies;
    }


    /**
     * Updates numRentedCopies.  If n + numRentedCopies > numCopies, sets numRentedCopies
     * to numCopies.  (In other words, rent out all the available copies.)
     *
     * @param n
     * @throws IllegalArgumentException if n <= 0
     * @throws AllCopiesRentedOutException if numRentedCopies == numCopies
     */
    public void rentCopies(int n) throws IllegalArgumentException, AllCopiesRentedOutException
    {
        if(n <= 0){
            System.out.print("Film " + film + " has an invalid request\n");
            throw new IllegalArgumentException();
        }
        if(numRentedCopies == numCopies){
            System.out.print("Film " + film + " has been rented out\n");
            throw new AllCopiesRentedOutException();
        }
        if((n + numRentedCopies) > numCopies){
            numRentedCopies = numCopies;
        }
        numRentedCopies = n;
    }


    /**
     * Updates numRentedCopies.  If n > numRentedCopies, set numRentedCopies to zero.
     *
     * @param n
     * @throws IllegalArgumentException if n <= 0
     */
    public void returnCopies(int n) throws IllegalArgumentException
    {
        if(n <= 0){
            System.out.print("Film " + film + " has an invalid request\n");
            throw new IllegalArgumentException();
        }
        if(n > numRentedCopies){
            numRentedCopies = 0;
        }
        numRentedCopies -= n;
    }


    /**
     * Compares two videos by name using string comparison.
     */
    public int compareTo(Video vd)
    {
        return this.getFilm().compareTo(vd.getFilm());
    }


    /**
     * Write in the format "<film> (<numCopies>:<numRentedCopies>)", where every substring
     * in the form of a variable name delimited by a pair of angle brackets is replaced with the
     * value of the variable. For example, if a Video object has its private instance variables
     * take on the values below:
     *
     * film == "Forrest Gump"
     * numCopies == 2
     * numRentedCopies == 1
     *
     * then the method returns the string "Forrest Gump (2:1)".
     */
    @Override
    public String toString()
    {
        return film + " " + "(" + numCopies + ":" + numRentedCopies + ")";
    }
}
