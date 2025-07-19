import java.util.LinkedList;

public class WaterJugPuzzleSolver
{
    private class StepTup implements Comparable<StepTup>
    {
        private int a, b, c;
        private final int A_CAP, B_CAP, C_CAP; // capacity for each cap


        public StepTup(int a, int b, int c, int aCap, int bCap, int cCap)
        {
            this.a = 0; // zero?
            this.b = 0; // zero?
            this.c = c;

            this.A_CAP = aCap;
            this.B_CAP = bCap;
            this.C_CAP = cCap;
        }

        // To copy the previous capacity
        public StepTup(int aAmt, int bAmt, int cAmt, StepTup old)
        {
            this.a = aAmt;
            this.b = bAmt;
            this.c = cAmt;

            // Capacity never change
            this.A_CAP = old.getA_CAP();
            this.B_CAP = old.getB_CAP();
            this.C_CAP = old.getC_CAP();
        }


        public int getA()
        {
            return a;
        }

        public int getB()
        {
            return b;
        }

        public int getC()
        {
            return c;
        }

        public int getA_CAP()
        {
            return A_CAP;
        }

        public int getB_CAP()
        {
            return B_CAP;
        }

        public int getC_CAP()
        {
            return C_CAP;
        }

        public void setA(int a)
        {
            if (a <= this.A_CAP)
                this.a = a;
        }

        public void setB(int b)
        {
            if (b <= this.B_CAP)
                this.b = b;
        }

        public void setC(int c)
        {
            if (c <= this.C_CAP)
                this.c = c;
        }

        @Override
        public int compareTo(StepTup o)
        {
            if (this.a == o.a && this.b == o.b && this.c == o.c)
                return 0;
            else if (this.a < o.a || this.b < o.b || this.c < o.c)
                return -1;
            else
                return 1;
        }

        @Override
        public String toString()
        {
            return "(" + a + "," + b + "," + c + ")";
        }

        /* ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
           ~~~~~~~~~~~~~~~~~~~~~~ END OF StepTup CLASS ~~~~~~~~~~~~~~~~~~~~~~
           ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
         */
    }

    private int a, b, c; // Caps
    private int d, e, f; // Goals

    // Amount of traverses to perform for each state
    private final int TRAVERSES_NUM = 6;

    private enum Jug {A, B, C};

    StepTup finalState;

    //    private final int[][] vars; // Variations
    private final LinkedList<StepTup>[][] vars; // Variations

    // TODO: change from Integer type to a different type
//    private LinkedList<StepTup> step;

    public WaterJugPuzzleSolver(int a, int b, int c, int d, int e, int f)
    {

        // a == 1; b == 2; c == 3; d == 4; e == 5; f == 6 (just numbering)
        // first three parameters (a,b,c) are the three jugs currently (0,0,8 in example)
        // last three (d,e,f) are the goal of the final state

        // Caps
        this.a = a;
        this.b = b;
        this.c = c;

        // Goal
        this.d = d;
        this.e = e;
        this.f = f;

        // Variations
        // 2D array of b+1 rows, & a+1 columns
        // +1 is needed because we need to use 0 as well (0 water in jug)
        vars = new LinkedList[a+1][b+1];

        // set the final state from the params
        // calling the capacity of the initial jugs (though it shouldn't matter)
        finalState = new StepTup(d, e, f, a, b, c);
    }

//    private void resetVarsArr()
//    { // TODO: fix these loops' constrains (hard-coded values)
//        for (int i = 0; i < 4; i++)
//        {
//            for (int j = 0; j < 6; j++)
//            {
//                vars[i][j] = new LinkedList<>();
//                vars[i][j].add(new StepTup(i, j, 0));
//                vars[i][j].add(new StepTup(0, 0, 0));
//            }
//        }
//    }

    private void setDemoVals()
    {
        for (int i = 0; i < (a+1); i++)
        {
            for (int j = 0; j < (b+1); j++)
            {
                vars[i][j] = new LinkedList<>();
                vars[i][j].add(new StepTup(i, j, (c-(i+j)),
                        i, j,0));
            }
        }
    }

    private LinkedList<StepTup> getWaysBFS(StepTup state)
    {
        StepTup newState;

        // 1) move c → a
        newState = moveWater(state, Jug.C, Jug.A);
        int newA = newState.getA(), newB = newState.getB();
        if (newState != null && vars[newA][newB] == null ) // not in vars
        {
            LinkedList<StepTup> statesList = vars[newA][newB] = new LinkedList<>();
            statesList.add(newState);
            return statesList;
//                BFS_State(vars[newA][newB]);
        }

        // 2) move b → a
        newState = moveWater(state, Jug.B, Jug.A);
        newA = newState.getA();
        newB = newState.getB();
        if (state != null && vars[newA][newB] == null ) // not in vars
        {
            LinkedList<StepTup> statesList= new LinkedList<>();
            statesList.add(newState);
            return statesList;
//                BFS_State(vars[newA][newB]);
        }

        // 3) move c → b
        newState = moveWater(state, Jug.C, Jug.B);
        newA = newState.getA();
        newB = newState.getB();
        if (state != null && vars[newA][newB] == null ) // not in vars
        {
            LinkedList<StepTup> statesList= new LinkedList<>();
            statesList.add(newState);
            return statesList;
        }

        // 4) move a → b
        newState = moveWater(state, Jug.A, Jug.B);
        newA = newState.getA();
        newB = newState.getB();
        if (state != null && vars[newA][newB]  == null ) // not in vars
        {
            LinkedList<StepTup> statesList= new LinkedList<>();
            statesList.add(newState);
            return statesList;
        }

        // 5) move b → c
        newState = moveWater(state, Jug.B, Jug.C);
        newA = newState.getA();
        newB = newState.getB();
        if (state != null && vars[newA][newB]  == null ) // not in vars
        {
            LinkedList<StepTup> statesList= new LinkedList<>();
            statesList.add(newState);
            return statesList;
        }

        // 6) move a → c
        newState = moveWater(state, Jug.A, Jug.C);
        newA = newState.getA();
        newB = newState.getB();
        if (state != null && vars[newA][newB]  == null ) // not in vars
        {
            LinkedList<StepTup> statesList= new LinkedList<>();
            statesList.add(newState);
            return statesList;
        }

        return null;
    }

    private LinkedList<StepTup> getWaysBFS(StepTup state)
    {
        StepTup newState;

        for (int i = 0; i < TRAVERSES_NUM; i++)
        {
            newState = switch (i)
            {
                case 0 -> moveWater(state, Jug.C, Jug.A);
                case 1 -> moveWater(state, Jug.B, Jug.A);
                case 2 -> moveWater(state, Jug.C, Jug.B);
                case 3 -> moveWater(state, Jug.A, Jug.B);
                case 4 -> moveWater(state, Jug.B, Jug.C);
                default -> // case 5
                        moveWater(state, Jug.A, Jug.C);
            };

            int newA = newState.getA(), newB = newState.getB();
            if (state != null && vars[newA][newB]  == null ) // not in vars
            {
                LinkedList<StepTup> statesList= new LinkedList<>();
                statesList.add(newState);
                return statesList;
            }
        }
    }

    public LinkedList<StepTup> BFS_State(LinkedList<StepTup> StatesList)
    {
        // this method should check for the possible ways in this state
        // and add to the given list more states
        System.out.println("Given States List: " + StatesList);

        // TODO: change for loop values (b??)
        for (int i = 0; i < b+1; i++)
        {
            System.out.println("Given state in BFS_STate: " + StatesList);
            getWaysBFS(StatesList);
        }

        if (StatesList != null && StatesList.getLast() == finalState)
        {
            System.out.println("Final state: " + StatesList.getLast());
            return StatesList; // actually need to iterate over to see solution
        }

        return null;
    }

    /**
     * Moves water from `from` jug to `to` jug
     * Ensures no more than capacity is moved
     */
    private StepTup moveWater(StepTup state, Jug from, Jug to)
    {
        // TODO: perhaps it's cheaper to change the reference instead of creating a new one?
        // Some switch cases uses enhanced so that intellij won't complain
        // on (false) duplicated code

        if (state == null) return null;
//        System.out.println("Moving from " + from + " to " + to);
//        System.out.println("Current state: " + state);
//        System.out.println("----------------------------------------");

        // extract current amounts
        int aAmt = state.getA(), bAmt = state.getB(), cAmt = state.getC();

        // extract capacities
        int aCap = state.getA_CAP(), bCap = state.getB_CAP(), cCap = state.getC_CAP();

        // figure out `from` and `to` amounts & caps
        int amountFrom, amountTo, capacityTo;

        switch (from)
        {
            case A: amountFrom = aAmt; break;
            case B: amountFrom = bAmt; break;
            case C: amountFrom = cAmt; break;
            default: System.out.println("null"); return null; // error
        }

        switch (to)
        {
            case A: amountTo = aAmt; capacityTo = aCap; break;
            case B: amountTo = bAmt; capacityTo = bCap; break;
            case C: amountTo = cAmt; capacityTo = cCap; break;
            default: System.out.println("null"); return null; // error
        }

        // compute how much we can transfer
//        int transferable = amountFrom - (amountFrom - capacityTo);
        int transferable = Math.min(amountFrom, capacityTo - amountTo);

        // TODO: check transferable math:max of c out of the capacity

        amountFrom -= transferable;
        amountTo   += transferable;


//        System.out.println("transferable: " + transferable);
//        System.out.println("amountFrom: " + amountFrom);
//        System.out.println("amountTo: " + amountTo);


        // write back into aAmt, bAmt, cAmt
        switch (from)
        {
            case A -> aAmt = amountFrom;
            case B -> bAmt = amountFrom;
            case C -> cAmt = amountFrom;
            default ->
            {
                System.out.println("null");
                return null; // error
            }
        }

        switch (to)
        {
            case A -> aAmt = amountTo;
            case B -> bAmt = amountTo;
            case C -> cAmt = amountTo;
            default ->
            {
                System.out.println("null");
                return null; // error
            }
        }


//        if (amountFrom < 0) return null;
        if (amountFrom < 0) return state;

        // Print Debugger
//        System.out.println("A amount: " + aAmt);
//        System.out.println("B amount: " + bAmt);
//        System.out.println("C amount: " + cAmt);
        if (amountFrom > 0)
            System.out.println("Moved " + amountFrom + "oz water from " +
                    from + " to " + to);
//        StepTup newState = new StepTup(aAmt, bAmt, cAmt, state);
//        System.out.println(newState);
//        System.out.println("----------------------------------------\n\n");

        // return new state
        return new StepTup(aAmt, bAmt, cAmt, state);
    }


    // TODO: I'm a debugger, delete me
    private void printVarsArr()
    {
//        System.out.println(Arrays.deepToString(vars));
        for (int i = 0; i < (a+1); i++)
        {
            for (int j = 0; j < (b+1); j++)
            {
                if (vars[i][j] == null) System.out.print("");
                else
                    System.out.print(vars[i][j].toString() + (j + 1 == (b+1) ? "" : ", "));
            }

            System.out.println();
        }
    }


    public void solve()
    {
        StepTup startState = new StepTup(a, b, c, a, b, c);
        LinkedList<StepTup> statesList = getWaysBFS(startState);
//        setDemoVals();
//        getWaysBFS();
//        resetVarsArr();

        BFS_State(statesList);
        printVarsArr(); // debugger
    }


    public static void main(String[] args)
    {
        // Commented for dev, uncomment later
//        if (args.length != 6)
//        {
//            System.err.println("Usage: java WaterJugPuzzleSolver " +
//                    "<cap A> <cap B> <cap C> <goal A> <goal B> <goal C>");
//            System.exit(1);
//        }

        WaterJugPuzzleSolver solver = new
                WaterJugPuzzleSolver(3, 5, 8, 0, 4, 4);

        solver.solve();

        /* TODO: enter input checks:
        ------ Can be done in one function! ---------
        1. $ java WaterJugPuzzleSolver 3 5 X 0 4 4
        Error: Invalid capacity 'X' for jug C.

        2. $ java WaterJugPuzzleSolver 3 5 8 0 R 4
        Error: Invalid goal 'R' for jug B.

        3. $ java WaterJugPuzzleSolver 3 5 8 4 0 4
        Error: Goal cannot exceed capacity of jug A.

        4. $ java WaterJugPuzzleSolver 3 5 8 0 4 5
        Error: Total gallons in goal state must be equal to the capacity of jug C.
         */


        System.exit(0);
    }
}


// breadth-first
// create a 2D array of linked lists that links their parent to see the solutions
// the order should then be entered into an array list (or optimization of it)
// and then print it in reverse order
// the 2D array would have (a,b)
/*

        0       1       2       3       4       5
    0 (0,0,8)
    1
    2
    3 (3,0,5)

(3,0,5) --> (0,0,8)
(0,0,8) is the parent (the solution that leads to (3,0,5))

 */