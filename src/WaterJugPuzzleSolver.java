import java.util.LinkedList;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class WaterJugPuzzleSolver
{
    private class StepTup implements Comparable<StepTup>
    {
        private int a, b, c;
        private final int A_CAP, B_CAP, C_CAP; // capacity for each cap

        public StepTup(int a, int b, int c)
        {
            this.a = 0;
            this.b = 0;
            this.c = c; // Jug C will always start out completely full

            this.A_CAP = a;
            this.B_CAP = b;
            this.C_CAP = c;
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

    private enum Jug {A, B, C};

    StepTup finalState;

    //    private final int[][] vars; // Variations
    private final LinkedList<StepTup>[][] vars; // Variations

    // TODO: change from Integer type to a different type
//    private LinkedList<StepTup> step;

    public WaterJugPuzzleSolver(int a, int b, int c, int d, int e, int f)
    {
        super(); // TODO: not sure if this is correct

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
//        vars = new int[b+1][a+1];
        // TODO: fix this horrible array init
        vars = new LinkedList[4][6];

        // set the final state from the params
        finalState = new StepTup(d, e, f);
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

    private void getWaysBFS()
    {
        StepTup currState = new StepTup(a, b, c); // not sure if needed

//        while (a != d && b != e && c != f)
        while (currState != finalState)
        {
            // TODO: add stop if no solution

            // 1) move c → a
            moveWater(currState, Jug.C, Jug.A);
            // 2) move b → a

            // 3) move c → b

            // 4) move a → b

            // 5) move b → c

            // 6) move a → c


        }
    }

    /**
     * e.g. `from` is c and `to` is a
     * Ensures no more than capacity is moved
     */
    private StepTup moveWater(StepTup state, Jug from, Jug to)
    {
        StepTup newState = new StepTup(state.a, state.b, state.c); // copy of
        switch(from)
        {
            case A:

        }
        newState.to = newState.from - (newState.from - newState.toCapacity);
        newState.from = newState.from - newState.to;

        return newState;
    }


    // TODO: I'm a debugger, delete me
    private void printVarsArr()
    {
//        System.out.println(Arrays.deepToString(vars));
        for (int i = 0; i < 4; i++)
        {
            for (int j = 0; j < 6; j++)
            {
                System.out.print(vars[i][j].toString() + (j + 1 == 6 ? "" : ", "));
            }

            System.out.println();
        }
    }


    public void solve()
    {
        //
//        resetVarsArr();
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
                WaterJugPuzzleSolver(0, 0, 8, 0, 4, 4);

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