import java.util.*;

public class WaterJugPuzzleSolver
{
    private static class StepTup implements Comparable<StepTup>
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
            return new StringBuilder(12)
                    .append('(').append(a)
                    .append(", ").append(b)
                    .append(", ").append(c)
                    .append(')').toString();
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (!(obj instanceof StepTup)) return false;
            StepTup other = (StepTup) obj;
            return a == other.a && b == other.b && c == other.c;
        }

        @Override
        public int hashCode() {
            return (a << 16) | (b << 8) | c;
        }

        /* ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
           ~~~~~~~~~~~~~~~~~~~~~~ END OF StepTup CLASS ~~~~~~~~~~~~~~~~~~~~~~
           ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
         */
    }

    private int a;
    private int b;
    private int c; // Caps

    private int d;
    private int e;
    private int f;

    // Numeric representation of the water jugs
    private enum Jug {A, B, C};

    // Pool of all the states to run
    private Queue<StepTup> taskPool;
    StepTup finalState;
//    private final StepTup[][] vars; // Variations
    private Set<StepTup> visited = new HashSet<>();

    public WaterJugPuzzleSolver(int a, int b, int c, int d, int e, int f)
    {
        // a == 1; b == 2; c == 3; d == 4; e == 5; f == 6 (just numbering)
        // first three parameters (a,b,c) are the three jugs currently (0,0,8 in example)
        // last three (d,e,f) are the goal of the final state

        // Caps
        this.a = a;
        this.b = b;
        this.c = c;

        // Goals
        this.d = d;
        this.e = e;
        this.f = f;

        // Variations
        // 2D array of b+1 rows, & a+1 columns
        // +1 is needed because we need to use 0 as well (0 water in jug)
//        visited = new StepTup[a + 1][b + 1];
        this.visited = new HashSet<>();
        // set the final state from the params
        StepTup tmpState = new StepTup(d, e, f, d, e, f);
        finalState = new StepTup(d, e, f, tmpState);
//        this.taskPool = new LinkedList<>();
        this.taskPool = new ArrayDeque<>();
    }

    private void advancePoolBFS()
    {
        while (!taskPool.isEmpty())
        {
            StepTup state = taskPool.poll();

            if (state.compareTo(finalState) == 0)
            {
                printSolutionPath(state);
                break;
            }

            getWaysBFS(state); // get child's of pool's head
        }

        System.out.println("No solution.");
    }

    private void getWaysBFS(StepTup state)
    {
        // Reuse array to avoid repeated allocations
        StepTup[] moves = new StepTup[6];
        moves[0] = moveWater(state, 2, 0); // C to A
        moves[1] = moveWater(state, 1, 0); // B to A
        moves[2] = moveWater(state, 2, 1); // C to B
        moves[3] = moveWater(state, 0, 1); // A to B
        moves[4] = moveWater(state, 1, 2); // B to C
        moves[5] = moveWater(state, 0, 2); // A to C

        for (StepTup move : moves) {
            if (move != null && visited.add(move)) {  // add() returns false if already present
                move.setParent(state);
                taskPool.offer(move);
            }
        }
    }

    /**
     * Moves water from `from` jug to `to` jug
     * Ensures no more than capacity is moved
     */
    private StepTup moveWater(StepTup state, int from, int to) {
        int[] amounts = {state.getA(), state.getB(), state.getC()};
        int[] capacities = {state.getA_CAP(), state.getB_CAP(), state.getC_CAP()};

        if (amounts[from] == 0 || amounts[to] == capacities[to]) {
            return null; // No water to pour or destination full
        }

        int pourAmount = Math.min(amounts[from], capacities[to] - amounts[to]);

        amounts[from] -= pourAmount;
        amounts[to] += pourAmount;

        return new StepTup(amounts[0], amounts[1], amounts[2], state);
    }

    private void printSolutionPath(StepTup goal)
    {
        // build the path backwards
        List<StepTup> path = new ArrayList<>();
        for (StepTup cur = goal; cur != null; cur = cur.getParent())
            path.add(cur);
        Collections.reverse(path);

        // accumulate into a single StringBuilder
        StringBuilder out = new StringBuilder(path.size() * 30);
        out.append("Initial state. ").append(path.getFirst()).append('\n');

        for (int i = 1; i < path.size(); i++)
        {
            StepTup from = path.get(i - 1), to = path.get(i);
            int da = to.getA() - from.getA();
            int db = to.getB() - from.getB();
            int dc = to.getC() - from.getC();

            String src = da < 0 ? "A" : db < 0 ? "B" : "C";
            String dst = da > 0 ? "A" : db > 0 ? "B" : "C";
            int moved = da != 0 ? Math.abs(da)
                    : db != 0 ? Math.abs(db)
                    : Math.abs(dc);

            // simple concatenation, no format parsing
            out.append("Pour ")
                    .append(moved)
                    .append(" gallons from ")
                    .append(src)
                    .append(" to ")
                    .append(dst)
                    .append(". ")
                    .append(to)
                    .append('\n');
        }

        System.out.print(out);
        System.exit(0);
    }

    public void solve()
    {
        StepTup startState = new StepTup(a, b, c, a, b, c);
//        vars[a][b] = startState; // add init state to matrix
        visited.add(startState); // add init state to matrix

        if (startState.compareTo(finalState) == 0)
        {
            printSolutionPath(startState);
            return;
        }


        getWaysBFS(startState);

        advancePoolBFS();


    }


    private static boolean isDigitsOnly(String s) {
        if (s.isEmpty()) return false;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) < '0' || s.charAt(i) > '9') {
                return false;
            }
        }
        return true;
    }

    private static int[] checkInput(String[] args) {
        // arg count
        if (args.length != 6) {
            System.err.println(
                    "Usage: java WaterJugPuzzleSolver " +
                            "<cap A> <cap B> <cap C> <goal A> <goal B> <goal C>"
            );
            System.exit(1);
        }

        // numeric check & parse
        int[] vals = new int[6];
        String[] jugNames = {"A", "B", "C", "A", "B", "C"};
        String[] types = {"capacity", "capacity", "capacity", "goal", "goal", "goal"};

        for (int i = 0; i < 6; i++) {
            if (!isDigitsOnly(args[i])) {
                System.err.printf("Error: Invalid %s '%s' for jug %s.%n",
                        types[i], args[i], jugNames[i]);
                System.exit(1);
            }
            vals[i] = Integer.parseInt(args[i]);
        }

        // capacities must be > 0
        for (int i = 0; i < 3; i++) {
            if (vals[i] <= 0) {
                Jug jug = Jug.values()[i];
                System.err.printf(
                        "Error: Invalid capacity '%d' for jug %s.%n",
                        vals[i], jug
                );
                System.exit(1);
            }
        }

        // goal ≤ capacity
        if (vals[3] > vals[0]) {
            System.err.println("Error: Goal cannot exceed capacity of jug A.");
            System.exit(1);
        }
        if (vals[4] > vals[1]) {
            System.err.println("Error: Goal cannot exceed capacity of jug B.");
            System.exit(1);
        }
        if (vals[5] > vals[2]) {
            System.err.println("Error: Goal cannot exceed capacity of jug C.");
            System.exit(1);
        }

        // conservation: goals sum to capC
        if (vals[3] + vals[4] + vals[5] != vals[2]) {
            System.err.println(
                    "Error: Total gallons in goal state must be equal to the capacity of jug C."
            );
            System.exit(1);
        }

        return vals;
    }

    public static void main(String[] args)
    {
        int[] v = checkInput(args);
        new WaterJugPuzzleSolver(
                v[0], v[1], v[2],
                v[3], v[4], v[5]
        ).solve();


        System.exit(0);
    }


}