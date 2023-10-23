package edu.iastate.cs228.hw5;


import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 *
 * @author Nitin Nagavel
 *
 */

/**
 *
 * The Transactions class simulates video transactions at a video store.
 *
 */
public class Transactions
{

    /**
     * The main method generates a simulation of rental and return activities.
     *
     * @param args
     * @throws FileNotFoundException
     */
    public static void main(String[] args) throws FileNotFoundException, AllCopiesRentedOutException, FilmNotInInventoryException {
        System.out.println("Transactions at a Video Store");
        System.out.println("keys:\n1 (Rent)\t2 (bulk rent)\n3 (return)\t4 (bulk return)\n5 (summary)\t6 (exit)\n");
        VideoStore vi = new VideoStore("videoList1.txt");
        int choice = -1; //any number but 1, 2, 3, 4
        String filename; // our scanner input for key

        Scanner key = new Scanner(System.in);
        while(choice != 6){
            System.out.print("Transaction: ");
            choice = key.nextInt();
            if(choice == 1) {
                key = new Scanner(System.in);
                System.out.print("Film to rent: ");
                filename = key.nextLine();
                try {
                    vi.videoRent(VideoStore.parseFilmName(filename), VideoStore.parseNumCopies(filename));
                }catch(IllegalArgumentException | FilmNotInInventoryException | AllCopiesRentedOutException e){

                }
            }
            else if(choice == 2){
                key = new Scanner(System.in);
                System.out.print("Video file (rent): ");
                filename = key.nextLine();
                try{
                    vi.bulkRent(filename);
                }catch (FileNotFoundException | IllegalArgumentException | FilmNotInInventoryException | AllCopiesRentedOutException e){

                }
            }
            else if(choice == 3){
                key = new Scanner(System.in);
                System.out.print("Film to return: ");
                filename = key.nextLine();
                try{
                    vi.videoReturn(VideoStore.parseFilmName(filename), VideoStore.parseNumCopies(filename));
                }catch (IllegalArgumentException | FilmNotInInventoryException e){

                }
            }
            else if(choice == 4){
                key = new Scanner(System.in);
                System.out.print("Video file (return): ");
                filename = key.nextLine();
                try {
                    vi.bulkReturn(filename);
                }catch (FileNotFoundException | IllegalArgumentException | FilmNotInInventoryException e){

                }
            }
            else if(choice == 5){
                try{
                    System.out.print(vi.transactionsSummary());
                }catch (IllegalArgumentException e){

                }
            }
            else if(choice == 6){
                break;
            }
            else{
                System.out.print("\n");
                continue;
            }
            System.out.print("\n");
        }
        key.close();
    }
}

