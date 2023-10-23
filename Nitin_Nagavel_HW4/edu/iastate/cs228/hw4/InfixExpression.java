package edu.iastate.cs228.hw4;

/**
 *
 * @author Nitin Nagavel
 *
 */

import java.util.HashMap;
import java.util.NoSuchElementException;

/**
 *
 * This class represents an infix expression. It implements infix to postfix conversion using
 * one stack, and evaluates the converted postfix expression.
 *
 */

public class InfixExpression extends Expression
{
    private String infixExpression;   	// the infix expression to convert
    private boolean postfixReady = false;   // postfix already generated if true
    private int rankTotal = 0;		// Keeps track of the cumulative rank of the infix expression.

    private PureStack<Operator> operatorStack; 	  // stack of operators


    /**
     * Constructor stores the input infix string, and initializes the operand stack and
     * the hash map.
     *
     * @param st  input infix string.
     * @param varTbl  hash map storing all variables in the infix expression and their values.
     */
    public InfixExpression (String st, HashMap<Character, Integer> varTbl)
    {
        setVarTable(varTbl);
        st = removeExtraSpaces(st);
        infixExpression = st;
        operatorStack = new ArrayBasedStack<Operator>();
    }


    /**
     * Constructor supplies a default hash map.
     *
     * @param s
     */
    public InfixExpression (String s)
    {
        s = removeExtraSpaces(s);
        infixExpression = s;
        varTable = new HashMap<Character, Integer>();
        operatorStack = new ArrayBasedStack<>();
    }
    /**
     * Outputs the infix expression according to the format in the project description.
     */
    @Override
    public String toString()
    {
        String inf = "";
        for (int i = 0; i < infixExpression.length(); i++) {
            char a = infixExpression.charAt(i);
            if(i != 0){

                if(a == ' ' && infixExpression.charAt(i - 1) == '('){
                    continue;
                }
            }
            if(i != infixExpression.length() - 1){
                if(a == ' ' && infixExpression.charAt(i + 1) == ')'){
                    continue;
                }
            }
            inf += a;
        }
        return inf;
    }


    /**
     * @return equivalent postfix expression, or
     *
     *         a null string if a call to postfix() inside the body (when postfixReady
     * 		   == false) throws an exception.
     */
    public String postfixString() throws IllegalArgumentException, ExpressionFormatException {
        if(postfixReady == false){
            try {
                postfix();
            }catch(ExpressionFormatException e){
                return null;
            }
        }

        String s = "";
        for (int i = 0; i < postfixExpression.length(); i++) {
            s += postfixExpression.charAt(i);
            if(postfixExpression.charAt(i) != ' ')
                try {
                    char b = postfixExpression.charAt(i + 1);
                    if (((!isInt(b + "") || isOperator(postfixExpression.charAt(i)) || isVariable(postfixExpression.charAt(i)))) && b != ' ') {
                        s += ' ';
                    }
                }catch (IndexOutOfBoundsException e){
                    break;
                }
        }
        postfixExpression = s;
        return s;
    }


    /**
     * Resets the infix expression.
     *
     * @param st
     */
    public void resetInfix (String st)
    {
        infixExpression = st;
    }


    /**
     * Converts infix expression to an equivalent postfix string stored at postfixExpression.
     * If postfixReady == false, the method scans the infixExpression, and does the following
     * (for algorithm details refer to the relevant PowerPoint slides):
     *
     *     1. Skips a whitespace character.
     *     2. Writes a scanned operand to postfixExpression.
     *     3. When an operator is scanned, generates an operator object.  In case the operator is
     *        determined to be a unary minus, store the char '~' in the generated operator object.
     *     4. If the scanned operator has a higher input precedence than the stack precedence of
     *        the top operator on the operatorStack, push it onto the stack.
     *     5. Otherwise, first calls outputHigherOrEqual() before pushing the scanned operator
     *        onto the stack. No push if the scanned operator is ).
     *     6. Keeps track of the cumulative rank of the infix expression.
     *
     *  During the conversion, catches errors in the infixExpression by throwing
     *  ExpressionFormatException with one of the following messages:
     *
     *      -- "Operator expected" if the cumulative rank goes above 1;
     *      -- "Operand expected" if the rank goes below 0;
     *      -- "Missing '('" if scanning a ‘)’ results in popping the stack empty with no '(';
     *      -- "Missing ')'" if a '(' is left unmatched on the stack at the end of the scan;
     *      -- "Invalid character" if a scanned char is neither a digit nor an operator;
     *
     *  If an error is not one of the above types, throw the exception with a message you define.
     *
     *  Sets postfixReady to true.
     */
    public void postfix() throws ExpressionFormatException
    {
        int left = 0; //number of (
        int right = 0; //number of )
        if (postfixReady == false){
            postfixExpression = "";
            for (int i = 0; i < infixExpression.length(); i++) {

                char op4 = infixExpression.charAt(i);
                if(op4 == ' '){
                    continue;
                }
                else if(isInt(op4 + "") || isVariable(op4)){
                    postfixExpression += op4;
                    try{
                        char a = infixExpression.charAt(i + 1);
                        if(!isInt(a + "")){
                            postfixExpression += ' ';
                        }
                        rankTotal++;
                    }
                    catch(IndexOutOfBoundsException e){
                        rankTotal++;
                    }
                    if(rankTotal > 1){
                        throw new ExpressionFormatException("Operator Expected");
                    }

                }
                else if(isOperator(op4)){
                    Operator op = new Operator(op4);
                    if(op.getOp() == '('){
                      left++;
                    }
                   if(op.getOp() == '-'){
                       if(i == 0){
                           op = new Operator('~');
                       }
                       else if(isUnary(infixExpression.charAt(i - 1), i - 1)){
                           op = new Operator('~');
                       }
                   }
                   if(op.getOp() != '~' && op.getOp() != '(' && op.getOp() != ')'){
                       rankTotal--;
                   }
                   if (rankTotal < 0){
                       throw new ExpressionFormatException("Operand Expected");
                   }

                   if(operatorStack.isEmpty()){
                       operatorStack.push(op);
                   }
                   else if(operatorStack.peek().compareTo(op) == -1){
                       operatorStack.push(op);
                   }
                   else{
                       outputHigherOrEqual(op);
                       if(op.getOp() != ')'){
                           operatorStack.push(op);
                       }
                       else{
                           right++;
                       }
                   }
                }
                else{
                    throw new ExpressionFormatException("Invalid Character");
                }
            }
            if (left > right){ //right ) left (
                while(!(operatorStack.isEmpty())){
                    operatorStack.pop();
                }
                throw new ExpressionFormatException("Missing '('");
            }
            else if (left < right){
                throw new ExpressionFormatException("Missing ')'");
            }
            while (!operatorStack.isEmpty()){
                postfixExpression += operatorStack.pop().getOp();
            }
            if(rankTotal != 1){
                throw new ExpressionFormatException("RankTotal does not equal :");
            }
            postfixReady = true;
        }
        else{
            postfixReady = true;
        }
    }


    /**
     * This function first calls postfix() to convert infixExpression into postfixExpression. Then
     * it creates a PostfixExpression object and calls its evaluate() method (which may throw
     * an exception).  It also passes any exception thrown by the evaluate() method of the
     * PostfixExpression object upward the chain.
     *
     * @return value of the infix expression
     * @throws ExpressionFormatException, UnassignedVariableException
     */
    public int evaluate() throws ExpressionFormatException, UnassignedVariableException, NoSuchElementException
    {
        postfix();
        PostfixExpression po;
       if(this.varTable.isEmpty()){
           po = new PostfixExpression(this.postfixString());
       }
       else{
           po = new PostfixExpression(this.postfixString(), this.varTable);
       }
        return po.evaluate();
    }


    /**
     * Pops the operator stack and output as long as the operator on the top of the stack has a
     * stack precedence greater than or equal to the input precedence of the current operator op.
     * Writes the popped operators to the string postfixExpression.
     *
     * If op is a ')', and the top of the stack is a '(', also pops '(' from the stack but does
     * not write it to postfixExpression.
     *
     * @param op  current operator
     */
    private void outputHigherOrEqual(Operator op)
    {
       while(operatorStack.peek().compareTo(op) >= 0) {
           postfixExpression += operatorStack.pop().getOp();

           if (operatorStack.isEmpty()) {
               break;
           }
       }
       if(!operatorStack.isEmpty()) {
           if (op.getOp() == ')' && operatorStack.peek().getOp() == '(') {
               operatorStack.pop();
           }
       }
    }
    private boolean isUnary(char oper, int x){
        if(oper == ' '){
            oper = infixExpression.charAt(x - 1);
        }
        switch(oper){
            case '-':
            case '+':
            case '*':
            case '/':
            case '%':
            case '^':
            case '(':
                return true;
        }
        return false;
    }
}

