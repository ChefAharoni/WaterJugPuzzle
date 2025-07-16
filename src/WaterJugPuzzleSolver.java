import java.util.Arrays;
import java.util.LinkedList;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class WaterJugPuzzleSolver
{
    private class StepTup
    {
        private int x, y, z;

        public StepTup(int x, int y, int z)
        {
            this.x = x;
            this.y = y;
            this.z = z;
        }
    }

    private int a, b, c; // Caps
    private int d, e, f; // Goals

    private final int[][] vars; // Variations

    // TODO: change from Integer type to a different type
    private LinkedList<StepTup> step;

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
        vars = new int[4][6];
    }

    private void resetVarsArr()
    { // TODO: fix these loops' constrains (hard-coded values)
        for (int i = 0; i < 4; i++)
        {
            for (int j = 0; j < 6; j++)
            {
//                vars[i][j] = (0,0,this.c);
                vars[i][j] = -1;
            }
        }
    }

    // TODO: I'm a debugger, delete me
    private void printVarsArr()
    {
        System.out.println(Arrays.deepToString(vars));
    }

    public void solve()
    {
        //
//        System.out.println("I'm a solver");
        resetVarsArr();
        printVarsArr();
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