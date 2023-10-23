package edu.iastate.cs228.hw4;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;
/**
 *
 * @author Nitin Nagavel
 *
 */

/**
 *
 * This class evaluates input infix and postfix expressions.
 *
 */

public class InfixPostfix
{

    /**
     * Repeatedly evaluates input infix and postfix expressions.  See the project description
     * for the input description. It constructs a HashMap object for each expression and passes it
     * to the created InfixExpression or PostfixExpression object.
     *
     * @param args
     **/
    public static void main(String[] args) throws ExpressionFormatException, UnassignedVariableException, NoSuchElementException, FileNotFoundException {
        int trial_num = 1;
        int choice = -1; //any number but 1, 2, 3, 4
        ArrayList<Character> input = new ArrayList<Character>();

        Scanner key = new Scanner(System.in);
        System.out.println("Evaluation of Infix and Postfix Expressions");
        System.out.println("keys: 1 (standard input)  2 (file input)  3 (exit)");
        System.out.println("(Enter 'I' before an infix expression, 'P' before a postfix expression)");
        while(choice != 3){
            String filename; // our scanner input for key
            String ses; //for our scanner for file
            System.out.print("Trial " + (trial_num + "") + ": ");
            choice = key.nextInt();
            if(choice == 1) {
                System.out.print("Expression: ");
                filename = key.next();
                if(filename.charAt(0) == 'P'){ //Test P a 6 + b 3 ^ -

                    filename = key.nextLine();
                    PostfixExpression exp = new PostfixExpression(filename.substring(1));

                    System.out.println("Postfix form: " + exp.postfixExpression);
                    input = getVariables(exp.postfixExpression);

                    if(input.size() != 0){
                        System.out.println("where");
                        for (int i = 0; i < input.size(); i++) {

                            System.out.print(input.get(i) + " = ");
                            int ch = key.nextInt();
                            exp.varTable.put(input.get(i), ch);
                        }
                    }

                    System.out.print("Expression value: " + exp.evaluate() + "\n");
                }
                else if(filename.charAt(0) == 'I'){ //I - ( 2 * i + - 3 ) * 5

                    filename = key.nextLine();
                    InfixExpression iff = new InfixExpression(filename.substring(1));

                    System.out.println("Infix form: " + iff.toString());
                    System.out.println("Postfix form: " + iff.postfixString());
                    input = getVariables(iff.postfixExpression);

                    if(input.size() != 0){
                        System.out.println("where");
                        for (int i = 0; i < input.size(); i++) {

                            System.out.print(input.get(i) + " = ");
                            int ch = key.nextInt();
                            iff.varTable.put(input.get(i), ch);
                        }
                    }
                    System.out.print("Expression value: " + iff.evaluate() + "\n");
                }

            }
            else if(choice == 2){
                System.out.println("Input from a file");
                System.out.print("Enter file name: "); //text.txt
                filename = key.next();//file name is stored in the first scanner


                File fi = new File(filename);
                Scanner sca = new Scanner(fi);
                while (sca.hasNextLine()){
                    ses = sca.next();
                    if(ses.charAt(0) == 'P'){
                        ses = sca.nextLine();
                        PostfixExpression exp = new PostfixExpression(ses.substring(1));
                        System.out.println("Postfix form: " + exp.postfixExpression);
                        input = getVariables(exp.postfixExpression);

                        if(input.size() != 0){
                            System.out.println("where");
                            for (int i = 0; i < input.size(); i++) {
                                sca.next();
                                sca.next();
                                int ch = sca.nextInt();
                                System.out.println(input.get(i) + " = " + ch);
                                exp.varTable.put(input.get(i), ch);
                            }
                        }
                        System.out.println("Expression value: " + exp.evaluate() + "\n\n");
                    }
                    else if(ses.charAt(0) == 'I'){
                        ses = sca.nextLine();
                        InfixExpression iff = new InfixExpression(ses.substring(1));

                        System.out.println("Infix form: " + iff.toString());
                        System.out.println("Postfix form: " + iff.postfixString());
                        input = getVariables(iff.postfixExpression);

                        if(input.size() != 0){
                            System.out.println("where");
                            for (int i = 0; i < input.size(); i++) {
                                sca.next();
                                sca.next();
                                int ch = sca.nextInt();
                                System.out.println(input.get(i) + " = " + ch);
                                iff.varTable.put(input.get(i), ch);
                            }
                        }
                        System.out.println("Expression value: " + iff.evaluate() + "\n\n");
                    }
                }
                sca.close();
            }
            else{
                break;
            }


            trial_num += 1;
        }
        key.close();
    }

    public static ArrayList<Character> getVariables(String s){
        ArrayList<Character> ex = new ArrayList<Character>();
        for (int i = 0; i < s.length(); i++) {
            if(Character.isLowerCase(s.charAt(i)) && !ex.contains(s.charAt(i))){
                ex.add(s.charAt(i));
            }
        }
        return ex;
    }

}
