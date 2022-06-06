package matrix;

import java.util.Scanner;

import matrix.util.*;
import matrix.util.inputOutput.MatrixInput;
import matrix.util.linearSystems.LinearSystem;
import matrix.util.linearSystems.solvers.SystemSolver;

public class RunSolver {
	public static void main(String[] args) {
		
		//determining silenced
		Scanner in = new Scanner(System.in);
		System.out.println("Solve system silently? (Y/n)");
		boolean isSilent = Character.toLowerCase((in.nextLine() + " ").charAt(0)) != 'n';
		
		boolean correctMatrix = false;
		LinearSystem system = null;
		do {
			//inputting matrix
			System.out.println("\nWrite Linear System (Total Matrix):");
			FractionMatrix totalMatrix = new FractionMatrix(MatrixInput.readMatrix());
			system = new LinearSystem(totalMatrix, totalMatrix.getWidth() - 1);

			//is correct matrix
			System.out.printf("Solve?... (Y/n)\n%s\n", LinearSystem.toString(system));
			correctMatrix = Character.toLowerCase(in.nextLine().charAt(0)) != 'n';
		}while(!correctMatrix);
		
		in.close();
		

		//solving matrix
		System.out.println("\nSolving...\n");
		boolean isSolved = SystemSolver.solve(system, isSilent);
		
		//outputting matrix
		if(isSolved) {
			
			int digitCount = 2;
			System.out.printf("Solved Linear System (Total Matrix):\n%s\n", LinearSystem.toString(system));
			System.out.printf("Approx (%d digits):\n%s\n", digitCount, LinearSystem.toDoubleString(system, digitCount));
			System.out.printf("Latex:\n%s\n", LinearSystem.asLatex(system));
			
			FractionMatrix solution = system.getEquality();
			System.out.printf("Linear System Solution:\n%s\n", FractionMatrix.toString(solution));
			System.out.printf("Solution Latex:\n%s\n", FractionMatrix.asLatex(solution));
			System.out.printf("Solution Approximation (%d digits):\n%s\n", digitCount, FractionMatrix.toDoubleString(solution, digitCount));
			
		}else {

			System.err.printf("Could not solve linear system; an inconsistency occurred:\n%s", LinearSystem.toString(system));
			
		}
	}

/*

TEST MATRICIES ----------------------------------------------------------------------------------------------

| -137/140    3/140    3/140  131/560    3/140    25/56   32/105 | 0 |
|    3/140 -137/140    3/140  131/560    3/140    3/140    3/140 | 0 |
|    3/140    1/278 -137/140    3/140     3/85    3/140    3/140 | 0 |
|     3/85    1/278    3/140 -137/140    3/140    25/56   32/105 | 0 |
|    3/140    3/140     3/85    3/140 -137/140    3/140   32/105 | 0 |
|    3/140    3/140    3/140  131/560    3/140 -137/140    3/140 | 0 |

| 1 2 3 4 | 5 |
| 5 4 3 2 | 1 |
| 3 2 1 5 | 4 |
| 5 3 2 1 | 2 |

| -8  4  4  4  4 | 0 |
|  2 -7  1  1  1 | 0 |
|  2  1 -7  1  1 | 0 |
|  2  1  1 -7  1 | 0 |
|  2  1  1  1 -7 | 0 |
|  1  1  1  1  1 | 1 |

| -12   0   0   3   0   6   4 | 0 |
|   0 -12   0   3   0   0   0 | 0 |
|   0   6 -12   0  12   0   0 | 0 |
|  12   6   0 -12   0   6   4 | 0 |
|   0   6  12   0 -12   0   4 | 0 |
|   0   0   0   3   0 -12   0 | 0 |
|   0   0   0   3   0   0 -12 | 0 |
|   1   1   1   1   1   1   1 | 1 |

| -6  0  0  2  0  3  3 | 0 |
|  0 -6  0  2  0  0  0 | 0 |
|  0  0 -6  0  6  0  0 | 0 |
|  6  6  0 -6  0  3  0 | 0 |
|  0  0  6  0 -6  0  3 | 0 |
|  0  0  0  2  0 -6  0 | 0 |
|  0  0  0  0  0  0 -6 | 0 |
|  1  1  1  1  1  1  1 | 1 |

| -240    6    6   57    6  108   74 | 0 |
|    6 -240    6   57    6    6    6 | 0 |
|    6  108 -240    6  210    6    6 | 0 |
|  210  108    6 -240    6  108   74 | 0 |
|    6    6  210    6 -240    6   74 | 0 |
|    6    6    6   57    6 -240    6 | 0 |
|    6    6    6   57    6    6 -240 | 0 |
|    1    1    1    1    1    1    1 | 1 |

|   -1 1/40 1/40 37/120 1/40 9/20 9/20 | 0 |
| 1/40   -1 1/40 37/120 1/40 1/40 1/40 | 0 |
| 1/40 1/40   -1   1/40  7/8 1/40 1/40 | 0 |
|  7/8  7/8 1/40     -1 1/40 9/20 1/40 | 0 |
| 1/40 1/40  7/8   1/40   -1 1/40 9/20 | 0 |
| 1/40 1/40 1/40 37/120 1/40   -1 1/40 | 0 |
| 1/40 1/40 1/40   1/40 1/40 1/40   -1 | 0 |
|    1    1    1      1    1    1    1 | 1 |

| -120    3    3   37    3   54   54 |   0 |
|    3 -120    3   37    3    3    3 |   0 |
|    3    3 -120    3  105    3    3 |   0 |
|  105  105    3 -120    3   54    3 |   0 |
|    3    3  105    3 -120    3   54 |   0 |
|    3    3    3   37    3 -120    3 |   0 |
|    3    3    3    3    3    3 -120 |   0 |
|  120  120  120  120  120  120  120 | 120 |

|   -1 1/40 1/40 70/120 1/40 9/20 9/20 | 0 |
| 1/40   -1 1/40 37/120 1/40 1/40 1/40 | 0 |
| 1/40 1/40   -1   1/40  7/8 1/40 1/40 | 0 |
|  7/8  7/8 1/40     -1 1/40 9/20 1/40 | 0 |
| 1/40 1/40  7/8   1/40   -1 1/40 9/20 | 0 |
| 1/40 1/40 1/40 37/120 1/40   -1 1/40 | 0 |
| 1/40 1/40 1/40   1/40 1/40 1/40   -1 | 0 |
|    1    1    1      1    1    1    1 | 1 |
 */
	
}
