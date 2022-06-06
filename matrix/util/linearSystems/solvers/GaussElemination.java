package matrix.util.linearSystems.solvers;

import java.math.BigInteger;

import matrix.util.*;
import matrix.util.inputOutput.Snitch;
import matrix.util.linearSystems.LinearSystem;

public class GaussElemination {
	
	//fields
	private Snitch output;
	private LinearSystem totalMatrix;
	private boolean isSolved;
	
	//constructor
	public GaussElemination(Snitch output, LinearSystem totalMatrix) {
		this.output = output;
		this.totalMatrix = totalMatrix;
		isSolved = false;
	}
	
	/***Getter for is solved boolean.*/
	public boolean isSolved() {return isSolved;}
	
	/***Solves a linear system (total matrix). Returns true if the system was solved, otherwise false.*/
	public void solve(){
		computeEchelon(totalMatrix);
		if(isInconsitent(totalMatrix)) return;
		computeReduced(totalMatrix);
		scalePivots(totalMatrix);
		buryZeroRows(totalMatrix);
		isSolved = true;
	}
	
	/***Computes the row echelon form of the given total-matrix.*/
	private void computeEchelon(LinearSystem totalMatrix){
		for (int pivot = 0; pivot < totalMatrix.getCoefficientWidth(); pivot++) {
			if(totalMatrix.isZero(pivot, pivot) && !fixZeroPivot(totalMatrix, pivot)) continue; 
			computeColumn(totalMatrix, pivot, pivot, +1);
		}
	}

	/***Computes the reduced row echelon form of the given total-matrix of the echelon form.*/
	private void computeReduced(LinearSystem totalMatrix){
		for (int pivot = totalMatrix.getCoefficientWidth() - 1; pivot > 0; pivot--) {
			if(totalMatrix.isZero(pivot, pivot)) continue; 
			computeColumn(totalMatrix, pivot, pivot, -1);
		}
	}
	
	/***Adds zeroes beneath or above the specified element.*/
	private void computeColumn(LinearSystem totalMatrix, int r1, int c, int direction){
		boolean computedElements = false;
		for (int r2 = r1 + direction; r2 >= 0 && r2 < totalMatrix.getHeight(); r2 += direction) {
			Fraction scalar = totalMatrix.getScalar(r1, c, r2, c);
			
			if(scalar.numerator().equals(BigInteger.ZERO)) continue;
			
			LinearSystem.addRows(totalMatrix, r1, r2, scalar);
			output.printRowAddition(r1, r2, scalar);
			computedElements = true;
		}
		if(computedElements) output.printSystem(totalMatrix);
	}

	/***Loops down through the column to find any non-zero element to swap with. Returns true if the given element has been fixed, otherwise false.*/
	private boolean fixZeroPivot(LinearSystem totalMatrix, int pivot) {
		for (int r = pivot + 1; r < totalMatrix.getHeight(); r++) {
			if(totalMatrix.isZero(r, pivot) || r == pivot)  continue;

			LinearSystem.swapRows(totalMatrix, r, pivot);
			output.printRowSwap(r, pivot);
			output.printSystem(totalMatrix);
			return true;
		}
		return false;
	}
	
	/***Determines inconsistency; Returning true if the given row echelon form is inconsistent, otherwise false.*/
	private boolean isInconsitent(LinearSystem totalMatrix) {
		for (int r = 0; r < totalMatrix.getHeight(); r++) {
			if(!totalMatrix.isZeroRow(r, totalMatrix.getCoefficientWidth())) continue;
				
			for (int e = totalMatrix.getCoefficientWidth(); e < totalMatrix.getWidth(); e++) {
				if(totalMatrix.isZero(r, e)) continue;
				output.printInconsistency(totalMatrix.getElement(r, e));
				return true;
			}
		}
		return false;
	}
	
	/***Scales pivots to one.*/
	private void scalePivots(LinearSystem totalMatrix) {
		boolean computedElements = false;
		for (int pivot = 0; pivot < totalMatrix.getHeight(); pivot++) {
			if(totalMatrix.isZero(pivot, pivot)  || totalMatrix.isOne(pivot, pivot)) continue;
			
			Fraction scalar = totalMatrix.getScalar(pivot, pivot);
			
			if(scalar.numerator().equals(BigInteger.ZERO)) continue;
			
			LinearSystem.scaleRow(totalMatrix, pivot, scalar);
			output.printRowScale(pivot, scalar);
			computedElements = true;
		}
		if(computedElements) output.printSystem(totalMatrix);
	}
	
	
	
	/***Sets all empty rows to the bottom.*/
	private void buryZeroRows(LinearSystem totalMatrix) {
		
		int bottom = totalMatrix.getHeight() - 1;
		for (int r = bottom; r >= 0; r--) {
			if(!totalMatrix.isZeroRow(r, totalMatrix.getWidth()) || r != bottom) continue;
			
			LinearSystem.moveRow(totalMatrix, r, bottom);
			bottom--;
		}
		
	}
	
}
