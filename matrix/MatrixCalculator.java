package matrix;

import java.util.Scanner;

import matrix.util.Fraction;
import matrix.util.FractionMatrix;
import matrix.util.inputOutput.MatrixInput;

public class MatrixCalculator {

	public static void main(String[] args) {
		
		//inputting first matrix or fraction
		System.out.println("Matrix A:");
		FractionMatrix a = new FractionMatrix(MatrixInput.readMatrix());
		
		//inputting operator
		Scanner in = new Scanner(System.in);
		System.out.println("Operator:");
		String s = in.nextLine();
		
		//inputting second matrix or fraction
		System.out.println("\nMatrix B:");
		FractionMatrix b = new FractionMatrix(MatrixInput.readMatrix());
		in.close();
		
		//outputting calculation
		FractionMatrix c = null;
		switch(s) {
		case "*": 
			if(a.getWidth() == 1 && a.getHeight() == 1) c = FractionMatrix.scale(b, a.getElement(0, 0));
			else if(b.getWidth() == 1 && b.getHeight() == 1) c = FractionMatrix.scale(a, b.getElement(0, 0));
			break;
		case "/": 
			if(a.getWidth() == 1 && a.getHeight() == 1) c = FractionMatrix.scale(b, a.getElement(0, 0).powNegOne());
			else if(b.getWidth() == 1 && b.getHeight() == 1) c = FractionMatrix.scale(a, b.getElement(0, 0).powNegOne());
			break;
		case "+": c = FractionMatrix.add(a, b); break;
		case "-": c = FractionMatrix.add(a, FractionMatrix.scale(b, new Fraction(-1))); break;
		case "s": 
			c = new FractionMatrix(a.getElement(0, 0));
			
			for (int i = 0; i < a.getHeight(); i++) {
				for (int j = 0; j < a.getWidth(); j++) {
					if(i == 0 && j == 0) continue;
					c = FractionMatrix.add(c, new FractionMatrix(a.getElement(i, j)));
				}
			}
			break;
		}
		
		System.out.printf("Matrix C = A %s B:\n%s%s\n%s=\n%s", 
				s, 
				a.getWidth() == 1 && a.getHeight() == 1 ? Fraction.toString(a.getElement(0, 0)) + "\n" : FractionMatrix.toString(a),
				s,
				b.getWidth() == 1 && b.getHeight() == 1 ? Fraction.toString(b.getElement(0, 0)) + "\n" : FractionMatrix.toString(b),
				c.getWidth() == 1 && c.getHeight() == 1 ? Fraction.toString(c.getElement(0, 0)) + "\n" : FractionMatrix.toString(c));
		
	}


}
