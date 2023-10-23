package edu.iastate.cs228.hw4;
/**
 *
 * @author Nitin Nagavel
 *
 */
import java.util.HashMap;
import java.util.Map;

public abstract class Expression
{
    protected String postfixExpression;
    protected HashMap<Character, Integer> varTable; // hash map to store variables in the


    protected Expression()
    {
        // no implementation needed
        // removable when you are done
    }


    /**
     * Initialization with a provided hash map.
     *
     * @param varTbl
     */
    protected Expression(String st, HashMap<Character, Integer> varTbl)
    {
        postfixExpression = st;
        varTable = varTbl;
    }


    /**
     * Initialization with a default hash map.
     *
     * @param st
     */
    protected Expression(String st) //empty ha
    {
        varTable = new HashMap<Character, Integer>();
        postfixExpression = st;
    }


    /**
     * Setter for instance variable varTable.
     * @param varTbl
     */
    public void setVarTable(HashMap<Character, Integer> varTbl)
    {
        varTable = new HashMap<Character, Integer>();
        varTable = varTbl;
    }


    /**
     * Evaluates the infix or postfix expression.
     *
     * @return value of the expression
     * @throws ExpressionFormatException, UnassignedVariableException
     */
    public abstract int evaluate() throws ExpressionFormatException, UnassignedVariableException;



    // --------------------------------------------------------
    // Helper methods for InfixExpression and PostfixExpression
    // --------------------------------------------------------

    /**
     * Checks if a string represents an integer.  You may call the static method
     * Integer.parseInt().
     *
     * @param s
     * @return
     */
    protected static boolean isInt(String s)
    {
        try{
            Integer.parseInt(s);
            return true;
        }catch(NumberFormatException e){
            return false;
        }
    }


    /**
     * Checks if a char represents an operator, i.e., one of '~', '+', '-', '*', '/', '%', '^', '(', ')'.
     *
     * @param c
     * @return
     */
    protected static boolean isOperator(char c)
    {

        switch(c){
            case '~':
            case '+':
            case '-':
            case '*':
            case '/':
            case '%':
            case '^':
            case '(':
            case ')':
                return true;
            default:
                return false;
        }
    }


    /**
     * Checks if a char is a variable, i.e., a lower case English letter.
     *
     * @param c
     * @return
     */
    protected static boolean isVariable(char c)
    {
       if(Character.isLowerCase(c)){
           return true;
       }
       else{
           return false;
       }
    }


    /**
     * Removes extra blank spaces in a string.
     * @param s
     * @return
     */
    protected static String removeExtraSpaces(String s)
    {
        s = s.trim().replaceAll(" +", " ");
        return s;
    }

}

