package edu.iastate.cs228.hw3;

/**
 *
 * @author Nitin Nagavel
 *
 */
import java.util.ListIterator;
import java.util.NoSuchElementException;

public class PrimeFactorization implements Iterable<PrimeFactor>
{
    private static final long OVERFLOW = -1;
    private long value; 	// the factored integer
    // it is set to OVERFLOW when the number is greater than 2^63-1, the
    // largest number representable by the type long.

    /**
     * Reference to dummy node at the head.
     */
    private Node head;

    /**
     * Reference to dummy node at the tail.
     */
    private Node tail;

    private int size;     	// number of distinct prime factors


    // ------------
    // Constructors
    // ------------

    /**
     *  Default constructor constructs an empty list to represent the number 1.
     *
     *  Combined with the add() method, it can be used to create a prime factorization.
     */
    public PrimeFactorization()
    {
       // head = new Node(null);
        //tail = new Node(null);
        head = new Node();
        tail = new Node();
        head.next = tail;
        tail.previous = head;

        value = 1;
        size = 0;
    }


    /**
     * Obtains the prime factorization of n and creates a doubly linked list to store the result.
     * Follows the direct search factorization algorithm in Section 1.2 of the project description.
     *
     * @param n
     * @throws IllegalArgumentException if n < 1
     */
    public PrimeFactorization(long n) throws IllegalArgumentException
    {
        if(n < 1){
            throw new IllegalArgumentException();
        }
        this.head = new Node();
        this.tail = new Node();
        head.next = tail;
        tail.previous = head;
        value = n;
        size = 0;
        long numb = n;
        int multi = 0;
        for (int i = 2; i <= Math.sqrt(n) /*numb / i*/ ; i++) {
            multi = 0;
            while(numb % i == 0){
                multi++;
                numb /= i;
            }
            if(multi != 0){
                add(i, multi);
                //size++;
            }
        }
        if(n % numb == 0 && isPrime(numb)){
            add((int) numb, 1);
        }
        if(isPrime(value)){
            size = 1;
        }
        updateValue();
    }


    /**
     * Copy constructor. It is unnecessary to verify the primality of the numbers in the list.
     *
     * @param pf
     */
    public PrimeFactorization(PrimeFactorization pf)
    {
        this.head = new Node();
        this.tail = new Node();
        head.next = tail;
        tail.previous = head;
        value = pf.value;
        //size = pf.size();
        PrimeFactorizationIterator it = pf.iterator();
        while(it.hasNext()){
            PrimeFactor pri = it.next();
            add(pri.prime, pri.multiplicity);
        }
        updateValue();
    }

    /**
     * Constructs a factorization from an array of prime factors.  Useful when the number is
     * too large to be represented even as a long integer.
     *
     * @param pfList
     */
    public PrimeFactorization (PrimeFactor[] pfList)
    {
        this.head = new Node();
        this.tail = new Node();
        head.next = tail;
        tail.previous = head;
        //size = pfList.length;
        for (int i = 0; i < pfList.length; i++) {
            add(pfList[i].prime, pfList[i].multiplicity);
        }
        updateValue();
    }



    // --------------
    // Primality Test
    // --------------

    /**
     * Test if a number is a prime or not.  Check iteratively from 2 to the largest
     * integer not exceeding the square root of n to see if it divides n.
     *
     *@param n
     *@return true if n is a prime
     * 		  false otherwise
     */
       public static boolean isPrime(long n)
    {
        if(n <= 1){
            return false;
        }
        for (int i = 2; i <= Math.sqrt(n); i++) {
            if(n % i == 0){
                return false;
            }
        }
        return true;
    }


    // ---------------------------
    // Multiplication and Division
    // ---------------------------

    /**
     * Multiplies the integer v represented by this object with another number n.  Note that v may
     * be too large (in which case this.value == OVERFLOW). You can do this in one loop:
     * Factor n and traverse the doubly linked list simultaneously.
     *
     * For details refer to Section 3.1 in the project description.
     * \
     * Store the prime factorization of the product. Update value and size.
     *
     * @param n
     * @throws IllegalArgumentException if n < 1
     */
    public void multiply(long n) throws IllegalArgumentException
    {
        if(n < 1){
            throw new IllegalArgumentException();
        }
        //if(!(this.value == OVERFLOW)){

            PrimeFactorization p1 = new PrimeFactorization(n);
            PrimeFactorizationIterator iterate = p1.iterator();

            while (iterate.hasNext()) {
                PrimeFactor pri = iterate.next();
                add(pri.prime, pri.multiplicity);
            }
            updateValue();
        //}
    }

    /**
     * Multiplies the represented integer v with another number in the factorization form.  Traverse both
     * linked lists and store the result in this list object.  See Section 3.1 in the project description
     * for details of algorithm.
     *
     * @param pf
     */
    public void multiply(PrimeFactorization pf)
    {
        PrimeFactorizationIterator pi = pf.iterator();
        while(pi.hasNext()){
            PrimeFactor p = pi.next();
            add(p.prime, p.multiplicity);
        }
        updateValue();
    }


    /**
     * Multiplies the integers represented by two PrimeFactorization objects.
     *
     * @param pf1
     * @param pf2
     * @return object of PrimeFactorization to represent the product
     */
    public static PrimeFactorization multiply(PrimeFactorization pf1, PrimeFactorization pf2)
    {
        PrimeFactorization all = new PrimeFactorization();
        //all.value = pf1.value + pf2.value;
        pf1.multiply(pf2);
        return pf1;
    }


    /**
     * Divides the represented integer v by n.  Make updates to the list, value, size if divisible.
     * No update otherwise. Refer to Section 3.2 in the project description for details.
     *
     * @param n
     * @return  true if divisible
     *          false if not divisible
     * @throws IllegalArgumentException if n <= 0
     */
    public boolean dividedBy(long n) throws IllegalArgumentException
    {
        if(n <= 0){
            throw new IllegalArgumentException();
        }
        if(value != -1 && value < n){
            return false;
        }
        PrimeFactorization p1 = new PrimeFactorization(n);
        return dividedBy(p1);
        //dividedBy(p1);
        //return true;

    }


    /**
     * Division where the divisor is represented in the factorization form.  Update the linked
     * list of this object accordingly by removing those nodes housing prime factors that disappear
     * after the division.  No update if this number is not divisible by pf. Algorithm details are
     * given in Section 3.2.
     *
     * @param pf
     * @return	true if divisible by pf
     * 			false otherwise
     */
    public boolean dividedBy(PrimeFactorization pf)
    {
        if (this.value != -1 && pf.value != -1 && this.value < pf.value){
            return false;
        }
        if (this.value != -1 && pf.value == -1){
            return false;
        }

        if (pf.value == this.value){
            value = 1;
            clearList();
            return true;
        }
        PrimeFactorization copy = new PrimeFactorization(this);

        PrimeFactorizationIterator p = copy.iterator();
        PrimeFactorizationIterator iter = pf.iterator();
        PrimeFactor pr = iter.next();
        while (p.hasNext()){
            PrimeFactor sec = p.next();
            if(sec.prime >= pr.prime){
                if(sec.prime > pr.prime){
                    return false;
                }
                else if((sec.prime == pr.prime) && (sec.multiplicity < pr.multiplicity)){
                    return false;
                }
                if((pr.multiplicity - sec.multiplicity) == 0){
                    p.index--;
                }
                copy.remove(pr.prime, pr.multiplicity);
                try{
                    pr = iter.next();
                }
                catch (NoSuchElementException e){
                    this.head = copy.head;
                    this.tail = copy.tail;
                    this.size = copy.size;
                    updateValue();
                    return true;
                }

            }

        }
        return false;
        /*
        while (p.hasNext()){
            PrimeFactor x = p.next();
            while(iter.hasNext()){
                PrimeFactor y = iter.next();
                if (!iter.hasNext()&& p.hasNext()){
                    return false;
                }
                else if (y.prime >= x.prime){
                    if (y.prime > x.prime){
                        return false;
                    }
                    else if (y.prime == x.prime && y.multiplicity < x.multiplicity){
                        return false;
                    }
                    else {
                        y.multiplicity = y.multiplicity - x.multiplicity;
                        if (y.multiplicity == 0){
                            iter.remove();
                            continue;
                        }
                    }
                }
            }
        }
        this.head = copy.head;
        this.tail = copy.tail;
        this.size = copy.size;
        this.updateValue();
        return true;*/
        /*if((this.value != -1 && pf.value != -1 && this.value < pf.value) ){
            return false;
        }
        if ((this.value != -1 && pf.value == -1)){
            return false;
        }

        if(this.value == pf.value){
            clearList();
            value = 1;
            return true;
        }
        PrimeFactorization cpy = new PrimeFactorization(this);
        PrimeFactorizationIterator pr = pf.iterator();
        PrimeFactorizationIterator cpyit = cpy.iterator();
		*//*
		while(pr.hasNext()){

		} *//*
        while(pr.hasNext()){
            PrimeFactor p = pr.next();
            while(cpyit.hasNext()){
                PrimeFactor q = cpyit.next();
                if(!cpyit.hasNext() && pr.hasNext()){
                    return false;
                }
                else if(q.prime >= p.prime){
                    if(q.prime > p.prime){
                        return false;
                    }
                    else if(q.prime == p.prime && q.multiplicity < p.multiplicity){
                        return false;
                    }
                    else{
                        q.multiplicity = q.multiplicity - p.multiplicity;
                        if(q.multiplicity == 0){
                            cpyit.remove();
                            continue;
                        }
                    }
                }
            }
            //cpyit = cpy.iterator();
        }
        this.head = cpy.head;
        this.tail = cpy.tail;
        this.size = cpy.size;
        this.updateValue();
        return true;*/
    }


    /**
     * Divide the integer represented by the object pf1 by that represented by the object pf2.
     * Return a new object representing the quotient if divisible. Do not make changes to pf1 and
     * pf2. No update if the first number is not divisible by the second one.
     *
     * @param pf1
     * @param pf2
     * @return quotient as a new PrimeFactorization object if divisible
     *         null otherwise
     */
    public static PrimeFactorization dividedBy(PrimeFactorization pf1, PrimeFactorization pf2)
    {
        PrimeFactorization p1 = new PrimeFactorization(pf1);
        PrimeFactorization p2 = new PrimeFactorization(pf2);
        if(p1.dividedBy(p2)){
            return p1;
        }
        return null;
    }


    // -----------------------
    // Greatest Common Divisor
    // -----------------------

    /**
     * Computes the greatest common divisor (gcd) of the represented integer v and an input integer n.
     * Returns the result as a PrimeFactor object.  Calls the method Euclidean() if
     * this.value != OVERFLOW.
     *
     * It is more efficient to factorize the gcd than n, which can be much greater.
     *
     * @param n
     * @return prime factorization of gcd
     * @throws IllegalArgumentException if n < 1
     */
    public PrimeFactorization gcd(long n) throws IllegalArgumentException
    {
        if(n < 1){
            throw new IllegalArgumentException();
        }
        PrimeFactorization pf = new PrimeFactorization();
        if(!(this.valueOverflow())){
            long lo = Euclidean(this.value, n);
            pf = new PrimeFactorization(lo);
        }
        return pf;
    }


    /**
     * Implements the Euclidean algorithm to compute the gcd of two natural numbers m and n.
     * The algorithm is described in Section 4.1 of the project description.
     *
     * @param m
     * @param n
     * @return gcd of m and n.
     * @throws IllegalArgumentException if m < 1 or n < 1
     */
    public static long Euclidean(long m, long n) throws IllegalArgumentException
    {
        if(m < 1 || n < 1){
            throw new IllegalArgumentException();
        }
        while (n != 0){
            long var = n;
            n = m%n;
            m = var;
        }
        return m;
    }


    /**
     * Computes the gcd of the values represented by this object and pf by traversing the two lists.  No
     * direct computation involving value and pf.value. Refer to Section 4.2 in the project description
     * on how to proceed.
     *
     * @param  pf
     * @return prime factorization of the gcd
     */
    public PrimeFactorization gcd(PrimeFactorization pf)
    {
        //return this.gcd(pf.value);
        PrimeFactorizationIterator iter = pf.iterator();
        PrimeFactorization returner = new PrimeFactorization();

        while(iter.hasNext()){
            //prime & multi of given PF
            PrimeFactor f = iter.next();
            int prime = f.prime;
            int multi = f.multiplicity;
            PrimeFactorizationIterator piter = iterator();
            while(piter.hasNext()){
                //prime & multi of this PF
                PrimeFactor f1 = piter.next();
                int Pprime = f1.prime;
                int Pmulti = f1.multiplicity;

                //compare the two primes & multis
                if (prime == Pprime){
                    if (multi < Pmulti){
                        returner.add(prime, multi);
                    }
                    else {
                        returner.add(prime, Pmulti);
                    }
                }
            }
        }
        return returner;
        /*
        PrimeFactorizationIterator p = pf.iterator();
        PrimeFactorization prime = new PrimeFactorization();
        while (p.hasNext()){
            PrimeFactor prim = p.next();
            int pri = prim.prime;
            int multi = prim.multiplicity;
            PrimeFactorizationIterator pit = this.iterator();
            while(pit.hasNext()){
                PrimeFactor prim1 = pit.next();
                int pri2 = prim1.prime;
                int multi2 = prim1.multiplicity;


                if(pri == pri2){
                    if(multi < multi2){
                        prime.add(pri, multi);
                    }
                    else{
                        prime.add(pri2, multi2);
                    }
                }
            }
        }
        return prime; */
    }


    /**
     *
     * @param pf1
     * @param pf2
     * @return prime factorization of the gcd of two numbers represented by pf1 and pf2
     */
    public static PrimeFactorization gcd(PrimeFactorization pf1, PrimeFactorization pf2)
    {
        long t = Euclidean(pf1.value, pf2.value);
        PrimeFactorization t1 = new PrimeFactorization(t);
        return t1;
    }

    // ------------
    // List Methods
    // ------------

    /**
     * Traverses the list to determine if p is a prime factor.
     *
     * Precondition: p is a prime.
     *
     * @param p
     * @return true  if p is a prime factor of the number v represented by this linked list
     *         false otherwise
     * @throws IllegalArgumentException if p is not a prime
     */
    public boolean containsPrimeFactor(int p) throws IllegalArgumentException
    {
        if(!isPrime(p)){
            throw new IllegalArgumentException();
        }
        PrimeFactorizationIterator it = this.iterator();
        while(it.hasNext()){
            PrimeFactor pri = it.next();
            if(pri.prime == p){
                return true;
            }
        }
        return false;

    }

    // The next two methods ought to be private but are made public for testing purpose. Keep
    // them public

    /**
     * Adds a prime factor p of multiplicity m.  Search for p in the linked list.  If p is found at
     * a node N, add m to N.multiplicity.  Otherwise, create a new node to store p and m.
     *
     * Precondition: p is a prime.
     *
     * @param p  prime
     * @param m  multiplicity
     * @return   true  if m >= 1
     *           false if m < 1
     */
    public boolean add(int p, int m)
    {
        if(m < 1){
            return false;
        }
        PrimeFactorizationIterator pri = this.iterator();
        // first condition checks if the number of prime factors are 0

		if(size == 0){ //checks if the number of prime factors is 0
			PrimeFactor prim = new PrimeFactor(p, m);
			pri.add(prim);
			updateValue();
			return true; //if m == 1
		}
        while (pri.hasNext()){
            PrimeFactor p1 = pri.next();
            if(p1.prime == p){
                p1.multiplicity += m;
                updateValue();
                return true;
            }
			else if(p1.prime > p){
			    pri.cursor = pri.cursor.previous;
				//PrimeFactor p2 = new PrimeFactor(p, m);
				//pri.add(p2);
				//return true;
                break;
			}
        }
        PrimeFactor p3 = new PrimeFactor(p, m);
        pri.add(p3);
        updateValue();
        return true;
    }


    /**
     * Removes m from the multiplicity of a prime p on the linked list.  It starts by searching
     * for p.
     * Returns false if p is not found, and true if p is found.
     * In the latter case, let N be the node that stores p.
     * If N.multiplicity > m, subtracts m from N.multiplicity.
     * If N.multiplicity <= m, removes the node N.
     *
     * Precondition: p is a prime.
     *
     * @param p
     * @param m
     * @return true  when p is found.
     *         false when p is not found.
     * @throws IllegalArgumentException if m < 1
     */
    public boolean remove(int p, int m) throws IllegalArgumentException
    {
        if (m < 1){
            throw new IllegalArgumentException();
        }
        PrimeFactorizationIterator iterat = this.iterator();
        while(iterat.hasNext()){
            PrimeFactor p4 = iterat.next();
            if(p4.prime == p) {
                if(p4.multiplicity > m) {
                    p4.multiplicity -= m;
                    updateValue();
                    return true;
                }
                else {
                    iterat.remove();
                    updateValue();
                    return true;
                }
            }
        }
        return false;
    }


    /**
     *
     * @return size of the list
     */
    public int size()
    {
        return size;
    }


    /**
     * Writes out the list as a factorization in the form of a product. Represents exponentiation
     * by a caret.  For example, if the number is 5814, the returned string would be printed out
     * as "2 * 3^2 * 17 * 19".
     */
    @Override
    public String toString()
    {
        String st = "";
       if(isPrime(value) || value == 1){
           st += value;
       }
       else{
           PrimeFactorizationIterator it = this.iterator();
           while(it.hasNext()){
               PrimeFactor p = it.next();
               if(p.multiplicity == 1){
                   st += p.prime;
               }
               if(p.multiplicity > 1){
                   st += p.toString();
               }
               if(it.hasNext()){
                   st += " * ";
               }
           }
       }
       return st;
    }


    // The next three methods are for testing, but you may use them as you like.

    /**
     * @return true if this PrimeFactorization is representing a value that is too large to be within
     *              long's range. e.g. 999^999. false otherwise.
     */
    public boolean valueOverflow() {
        return value == OVERFLOW;
    }

    /**
     * @return value represented by this PrimeFactorization, or -1 if valueOverflow()
     */
    public long value() {
        return value;
    }


    public PrimeFactor[] toArray() {
        PrimeFactor[] arr = new PrimeFactor[size];
        int i = 0;
        for (PrimeFactor pf : this)
            arr[i++] = pf;
        return arr;
    }



    @Override
    public PrimeFactorizationIterator iterator()
    {
        return new PrimeFactorizationIterator();
    }

    /**
     * Doubly-linked node type for this class.
     */
    private class Node
    {
        public PrimeFactor pFactor;			// prime factor
        public Node next;
        public Node previous;

        /**
         * Default constructor for creating a dummy node.
         */
        public Node()
        {
            this.pFactor = null;
            this.next = null;
            this.previous = null;
        }

        /**
         * Precondition: p is a prime
         *
         * @param p	 prime number
         * @param m  multiplicity
         * @throws IllegalArgumentException if m < 1
         */
        public Node(int p, int m) throws IllegalArgumentException
        {
            if(m < 1){
                throw new IllegalArgumentException();
            }
            else{
                this.pFactor = new PrimeFactor(p, m);
                this.next = null;
                this.previous = null;
            }
        }

        /**
         * Constructs a node over a provided PrimeFactor object.
         *
         * @param pf
         * @throws IllegalArgumentException
         */
        public Node(PrimeFactor pf)
        {
            if (pf == null){
                throw new IllegalArgumentException();
            }
            else{
                this.pFactor = pf;
                this.next = null;
                this.previous = null;
            }
        }


        /**
         * Printed out in the form: prime + "^" + multiplicity.  For instance "2^3".
         * Also, deal with the case pFactor == null in which a string "dummy" is
         * returned instead.
         */
        @Override
        public String toString()
        {
            if(pFactor == null){
                return "dummy";
            }
            else{
                return pFactor.prime + "^" + pFactor.multiplicity;
            }
        }
    }


    private class PrimeFactorizationIterator implements ListIterator<PrimeFactor>
    {
        // Class invariants:
        // 1) logical cursor position is always between cursor.previous and cursor
        // 2) after a call to next(), cursor.previous refers to the node just returned
        // 3) after a call to previous() cursor refers to the node just returned
        // 4) index is always the logical index of node pointed to by cursor

        private Node cursor = head.next;
        private Node pending = null;    // node pending for removal
        private int index = 0;

        // other instance variables ...


        /**
         * Default constructor positions the cursor before the smallest prime factor.
         */
        public PrimeFactorizationIterator()
        {
            //cursor = head.next;
            //cursor.next = head.next;
            //cursor.previous = head;

        }

        @Override
        public boolean hasNext()
        {
            return nextIndex() < size;
        }


        @Override
        public boolean hasPrevious()
        {
            return previousIndex() > 0;
        }


        @Override
        public PrimeFactor next()
        {
            if(!hasNext()){
                throw new NoSuchElementException();
            }
            else{
                pending = cursor;
                cursor = cursor.next;
                index++;
                return pending.pFactor;
            }
        }


        @Override
        public PrimeFactor previous()
        {
            if(!hasPrevious()){
                throw new NoSuchElementException();
            }
            else{
                pending = cursor;
                cursor = cursor.previous;
                index--;
                return pending.pFactor;
            }
        }


        /**
         *  Removes the prime factor returned by next() or previous()
         *
         *  @throws IllegalStateException if pending == null
         */
        @Override
        public void remove() throws IllegalStateException
        {
            if (pending == null){
                throw new IllegalStateException();
            }
            if(cursor == pending){
                cursor = cursor.next;
            }
            unlink(pending);
            pending = null;
            index--;
            size--;
        }


        /**
         * Adds a prime factor at the cursor position.  The cursor is at a wrong position
         * in either of the two situations below:
         *
         *    a) pf.prime < cursor.previous.pFactor.prime if cursor.previous != head.
         *    b) pf.prime > cursor.pFactor.prime if cursor != tail.
         *
         * Take into account the possibility that pf.prime == cursor.pFactor.prime.
         *
         * Precondition: pf.prime is a prime.
         *
         * @param pf
         * @throws IllegalArgumentException if the cursor is at a wrong position.
         */
        @Override
        public void add(PrimeFactor pf) throws IllegalArgumentException
        {
            if(cursor.previous != head){
                if(pf.prime < cursor.previous.pFactor.prime){
                    throw new IllegalArgumentException();
                }
            }
            else if(cursor != tail){
                if(pf.prime > cursor.pFactor.prime){
                    throw new IllegalArgumentException();
                }
            }
            Node no = new Node(pf);
            link(cursor.previous, no);
            pending = null;
            size++;
            index++;
        }


        @Override
        public int nextIndex()
        {
            return index;
        }


        @Override
        public int previousIndex()
        {
            return index - 1;
        }

        @Deprecated
        @Override
        public void set(PrimeFactor pf)
        {
            throw new UnsupportedOperationException(getClass().getSimpleName() + " does not support set method");
        }

        // Other methods you may want to add or override that could possibly facilitate
        // other operations, for instance, addition, access to the previous element, etc.
        //
        // ...
        //
    }


    // --------------
    // Helper methods
    // --------------

    /**
     * Inserts toAdd into the list after current without updating size.
     *
     * Precondition: current != null, toAdd != null
     */
    private void link(Node current, Node toAdd)
    {
        Node no = current.next;

        current.next = toAdd;
        toAdd.previous = current;

        no.previous = toAdd;
        toAdd.next = no;
    }


    /**
     * Removes toRemove from the list without updating size.
     */
    private void unlink(Node toRemove)
    {
        Node nex = toRemove.next;
        Node prev = toRemove.previous;

        prev.next = nex;
        nex.previous = prev;

        toRemove.next = null;
        toRemove.previous = null;
    }


    /**
     * Remove all the nodes in the linked list except the two dummy nodes.
     *
     * Made public for testing purpose.  Ought to be private otherwise.
     */
    public void clearList()
    {
        size = 0;
        head.next = tail;
        tail.previous = head;
    }

    /**
     * Multiply the prime factors (with multiplicities) out to obtain the represented integer.
     * Use Math.multiply(). If an exception is throw, assign OVERFLOW to the instance variable value.
     * Otherwise, assign the multiplication result to the variable.
     *
     */
    private void updateValue()
    {
        PrimeFactorizationIterator it = this.iterator();
        value = 1;
        long temp = 1;
        while(it.hasNext()){
            PrimeFactor pr = it.next();
            temp = (long) Math.pow(pr.prime, pr.multiplicity);
            try{
                value = Math.multiplyExact(value, temp);
            }catch(ArithmeticException e){
                value = OVERFLOW;
                break;
            }
        }
        /*
        try {
            PrimeFactorizationIterator it = this.iterator();
            //value = 1;
            long temp = 1;
            while (it.hasNext()) {
                PrimeFactor pr = it.next();
                //value *= Math.pow(pr.prime, pr.multiplicity);
                value = (long) (temp * Math.pow(pr.prime, pr.multiplicity));
                temp = value;
            }
            if(value > (Long.MAX_VALUE - 1)){
                value = OVERFLOW;
            }
        }
        catch (ArithmeticException e){
            value = OVERFLOW;
        }*/
        /*
        if(value > (Long.MAX_VALUE - 1)){
            value = OVERFLOW;
        } //
        //}
        /*
        catch (ArithmeticException e)
        {
            value = OVERFLOW;
        } */
    }
}

