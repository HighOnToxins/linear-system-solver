package matrix.util.inputOutput;

import matrix.util.Fraction;
import matrix.util.linearSystems.LinearSystem;

public class Snitch {

	//fields
	private boolean isSilent;
	
	//constructor
	public Snitch(boolean isSilent) {
		this.isSilent = isSilent;
		
	}

	// PRINT FUNCTIONS -------------------------------------------------------------------------------
	
	/***Prints the entire linear system.*/
	public void printSystem(LinearSystem system) {
		if(isSilent) return;
		System.out.println(LinearSystem.toString(system));
	}

	/***Prints the general equation for the addition of two rows.*/
	public void printRowAddition(int r1, int r2, Fraction scalar) {
		if(isSilent) return;
		System.out.printf("row-%d = row-%d + %s * row-%d\n", r2, r2, scalar.toString(), r1);
	}

	/***Prints the entire linear system.*/
	public void printRowScale(int r, Fraction scalar) {
		if(isSilent) return;
		System.out.printf("row-%d = %s * row-%d\n", r, scalar.toString(), r);
	}
	
	/***Prints the entire linear system.*/
	public void printRowSwap(int r1, int r2) {
		if(isSilent) return;
		System.out.printf("swap row-%d and row-%d\n", r1, r2);
	}
	
	/***Prints inconsistency.*/
	public void printInconsistency(Fraction value) {
		if(isSilent) return;
		System.err.printf("0 = %s?\n\n", Fraction.toString(value));
	}
}
