package edu.iastate.cs228.hw4;

/**
 *
 * @author Nitin Nagavel
 *
 */

/**
 *
 * This class evaluates a postfix expression using one stack.
 *
 */

import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class PostfixExpression extends Expression
{
    private int leftOperand;            // left operand for the current evaluation step
    private int rightOperand;           // right operand (or the only operand in the case of
    // a unary minus) for the current evaluation step

    private PureStack<Integer> operandStack;  // stack of operands


    /**
     * Constructor stores the input postfix string and initializes the operand stack.
     *
     * @param st      input postfix string.
     * @param varTbl  hash map that stores variables from the postfix string and their values.
     */
    public PostfixExpression (String st, HashMap<Character, Integer> varTbl)
    {
        setVarTable(varTbl);
        st = removeExtraSpaces(st);
        postfixExpression = st;
        leftOperand = 0;
        rightOperand = 0;
        operandStack = new ArrayBasedStack<Integer>();
    }


    /**
     * Constructor supplies a default hash map.
     *
     * @param s
     */
    public PostfixExpression (String s)
    {
        s = removeExtraSpaces(s);
        postfixExpression = s;
        varTable = new HashMap<Character, Integer>();
        leftOperand = 0;
        rightOperand = 0;
        operandStack = new ArrayBasedStack<Integer>();
    }


    /**
     * Outputs the postfix expression according to the format in the project description.
     */
    @Override
    public String toString()
    {
        return postfixExpression;
    }


    /**
     * Resets the postfix expression.
     * @param st
     */
    public void resetPostfix (String st)
    {
        postfixExpression = st;
    }


    /**
     * Scan the postfixExpression and carry out the following:
     *
     *    1. Whenever an integer is encountered, push it onto operandStack.
     *    2. Whenever a binary (unary) operator is encountered, invoke it on the two (one) elements popped from
     *       operandStack,  and push the result back onto the stack.
     *    3. On encountering a character that is not a digit, an operator, or a blank space, stop
     *       the evaluation.
     *
     * @return value of the postfix expression
     * @throws ExpressionFormatException with one of the messages below:
     *
     *           -- "Invalid character" if encountering a character that is not a digit, an operator
     *              or a whitespace (blank, tab);
     *           --	"Too many operands" if operandStack is non-empty at the end of evaluation;
     *           -- "Too many operators" if getOperands() throws NoSuchElementException;
     *           -- "Divide by zero" if division or modulo is the current operation and rightOperand == 0;
     *           -- "0^0" if the current operation is "^" and leftOperand == 0 and rightOperand == 0;
     *           -- self-defined message if the error is not one of the above.
     *
     *         UnassignedVariableException if the operand as a variable does not have a value stored
     *            in the hash map.  In this case, the exception is thrown with the message
     *
     *           -- "Variable <name> was not assigned a value", where <name> is the name of the variable.
     *
     */
    public int evaluate() throws ExpressionFormatException, UnassignedVariableException, NoSuchElementException
    {
        int count = 0;
        Scanner se = new Scanner(postfixExpression);
        while(se.hasNext()){
            String op = se.next();

            if (op.charAt(0) == ' ') {
                continue;
            }
            else if(isInt(op)){
                operandStack.push(Integer.parseInt(op));
            }
            else if(isVariable(op.charAt(0))){
                if(!varTable.containsKey(op.charAt(0))){
                    throw new UnassignedVariableException("Variable " + op + " was not assigned a value. ");
                }
                operandStack.push(varTable.get(op.charAt(0)));
            }
            else if(isBinary(op.charAt(0)) || op.charAt(0) == '~'){
                try{
                    getOperands(op.charAt(0));
                }
                catch(NoSuchElementException e){
                    throw new ExpressionFormatException("Too many Operators");
                }
               if((op.charAt(0) == '/' || op.charAt(0) == '%') && rightOperand == 0){
                   throw new ExpressionFormatException("Divide by zero");
               }

               if(op.charAt(0) == '^' && leftOperand == 0 && rightOperand == 0){//deleted parentheses
                   throw new ExpressionFormatException("0^0");
               }
                //Placed lines 135 - 140 at lines 127 - 132

                operandStack.push(compute(op.charAt(0)));
            }
            else if(!isOperator(op.charAt(0))){
                throw new ExpressionFormatException("Invalid character");
            }
        }
        se.close();
        count = operandStack.pop();
        if(operandStack.size() != 0){
            throw new ExpressionFormatException("Too many operands");
        }
        return count;
    }


    /**
     * For unary operator, pops the right operand from operandStack, and assign it to rightOperand. The stack must have at least
     * one entry. Otherwise, throws NoSuchElementException.
     * For binary operator, pops the right and left operands from operandStack, and assign them to rightOperand and leftOperand, respectively. The stack must have at least
     * two entries. Otherwise, throws NoSuchElementException.
     * @param op
     * 			char operator for checking if it is binary or unary operator.
     */
    private void getOperands(char op) throws NoSuchElementException
    {
        if(op == '~'){
            if(operandStack.size() <= 0){
                throw new NoSuchElementException();
            }
            else{
                rightOperand = operandStack.pop();
            }
        }
        else if(isBinary(op)){
           if(operandStack.size() <= 1){
               throw new NoSuchElementException();
           }
           else{
               rightOperand = operandStack.pop();
               leftOperand = operandStack.pop();
           }
        }
    }


    /**
     * Computes "leftOperand op rightOprand" or "op rightOprand" if a unary operator.
     *
     * @param op operator that acts on leftOperand and rightOperand.
     * @return
     */
    private int compute(char op)
    {
        int op2 = 0;
        switch (op){
            case '~':
                op2 = -rightOperand;
                break;
            case '-':
                op2 = leftOperand - rightOperand;
                break;
            case '+':
                op2 = leftOperand + rightOperand;
                break;
            case '*':
                op2 = leftOperand * rightOperand;
                break;
            case '/':
                op2 = leftOperand / rightOperand;
                break;
            case '%':
                op2 = leftOperand % rightOperand;
                break;
            case '^':
                op2 = (int) Math.pow(leftOperand, rightOperand);
                break;
        }
        return op2;
    }
    //Added javadoc from lines: 222 - 225
    /**
     * Checks if char is a binary operator
     * @return
     */
    private boolean isBinary(char op){
        switch (op){
            case '-':
            case '+':
            case '*':
            case '/':
            case '%':
            case '^':
                return true;
            default:
                return false;
        }
    }
}

