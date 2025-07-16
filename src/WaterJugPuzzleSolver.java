//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class WaterJugPuzzleSolver
{
    public static void main(String[] args)
    {
        if (args.length != 6)
        {
            System.out.println("Usage: java WaterJugPuzzleSolver " +
                    "<cap A> <cap B> <cap C> <goal A> <goal B> <goal C>");
        }
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