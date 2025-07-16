//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class WaterJugPuzzleSolver
{
    int a, b, c, d, e, f;

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
    }





    public static void main(String[] args)
    {
        if (args.length != 6)
        {
            System.err.println("Usage: java WaterJugPuzzleSolver " +
                    "<cap A> <cap B> <cap C> <goal A> <goal B> <goal C>");
            System.exit(1);
        }

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