import java.util.LinkedList;
import java.util.Queue;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class WaterJugPuzzleSolver
{
    private class StepTup implements Comparable<StepTup>
    {
        private int a, b, c;
        private final int A_CAP, B_CAP, C_CAP; // capacity for each cap

        // parent of this state
        private StepTup parent;

        public StepTup(int a, int b, int c, int aCap, int bCap, int cCap)
        {
            this.a = 0;
            this.b = 0;
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

        public void setParent(StepTup parent)
        {
            this.parent = parent;
        }

        public StepTup getParent()
        {
            return this.parent;
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

    // Numeric representation of the water jugs
    private enum Jug {A, B, C};

    // Pool of all the states to run
    private Queue<StepTup> taskPool;

    StepTup finalState;

    //    private final int[][] vars; // Variations
    private final StepTup[][] vars; // Variations

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
        vars = new StepTup[a+1][b+1];

        // set the final state from the params
        // calling the capacity of the initial jugs (though it shouldn't matter)
        StepTup tmpState = new StepTup(d, e, f, d, e, f);
        finalState = new StepTup(d, e, f, tmpState);
        this.taskPool = new LinkedList<>();
    }

    private void advancePoolBFS()
    {
        while (!taskPool.isEmpty())
        {
            StepTup state = taskPool.poll();
            getWaysBFS(state); // get child's of pool's head

            if (state.compareTo(finalState) == 0)
            {
                printSolutionPath(state);
                break;
            }
        }
    }

    private void printSolutionPath(StepTup goal)
    {
        // build the path from goal back to the start
        List<StepTup> path = new ArrayList<>();
        for (StepTup cur = goal; cur != null; cur = cur.getParent())
            path.add(cur);


        // reverse so it goes start → goal
        Collections.reverse(path);

        // print the initial state
        System.out.println("Initial state: " + path.get(0));

        // for each step, compute delta and print the pour
        for (int i = 1; i < path.size(); i++)
        {
            StepTup from = path.get(i - 1);
            StepTup to   = path.get(i);

            int da = to.getA() - from.getA();
            int db = to.getB() - from.getB();
            int dc = to.getC() - from.getC();

            // Determine source and destination jugs
            String src = da < 0 ? "A" : db < 0 ? "B" : "C";
            String dst = da > 0 ? "A" : db > 0 ? "B" : "C";

            // Compute the actual amount moved (only one delta is non-zero)
            int moved;
            if (da != 0) moved = Math.abs(da);
            else if (db != 0) moved = Math.abs(db);
            else moved = Math.abs(dc);

            // Print the instruction
            System.out.printf(
                    "Pour %d gallons from %s to %s. %s%n",
                    moved, src, dst, to
            );
        }
    }

    private void getWaysBFS(StepTup state)
    {
        StepTup candidate;

        // Amount of traverses to perform for each state
        int TRAVERSES_NUM = 6;
        for (int i = 0; i < TRAVERSES_NUM; i++)
        {
            candidate = switch (i)
            {
                case 0 -> moveWater(state, Jug.C, Jug.A);
                case 1 -> moveWater(state, Jug.B, Jug.A);
                case 2 -> moveWater(state, Jug.C, Jug.B);
                case 3 -> moveWater(state, Jug.A, Jug.B);
                case 4 -> moveWater(state, Jug.B, Jug.C);
                default -> // case 5
                        moveWater(state, Jug.A, Jug.C);
            };

            if (candidate == null) continue;

            int x = candidate.getA(), y = candidate.getB();
            if (vars[x][y] == null)
            {
                candidate.setParent(state);
                vars[x][y] = candidate;
                taskPool.add(candidate);
            }
        }
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

        if (transferable <= 0)
            return null;

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

        if (amountFrom < 0) return state;

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
        vars[a][b] = startState; // add init state to matrix

        getWaysBFS(startState);
//        setDemoVals();
//        getWaysBFS();
//        resetVarsArr();

        advancePoolBFS();
//        printVarsArr(); // debugger
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