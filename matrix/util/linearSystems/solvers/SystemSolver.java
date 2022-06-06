package matrix.util.linearSystems.solvers;

import matrix.util.inputOutput.Snitch;
import matrix.util.linearSystems.LinearSystem;

public class SystemSolver {
	
	/**Solves the given linear system in place. 
	 * Prints if isSilent = false. 
	 * Returns true if the system was solved. 
	 * Returns false if an inconsistency was found.*/
	public static boolean solve(LinearSystem system, boolean isSilent) {
		
		Snitch output = new Snitch(isSilent);
		
		GaussElemination gauss = new GaussElemination(output, system);
		gauss.solve();
		
		return gauss.isSolved();
	}
}
