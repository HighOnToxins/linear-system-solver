package matrix.util.linearSystems;

import java.math.BigInteger;

import matrix.util.Fraction;
import matrix.util.FractionMatrix;

public class LinearSystem extends FractionMatrix{

	//matrix
	private int coefficientWidth;
	
	//constructor
	public LinearSystem(FractionMatrix matrix, int coefficientWidth) {
		super(matrix.clone());
		this.coefficientWidth = coefficientWidth;
	}
	

	public LinearSystem(FractionMatrix coefficients, FractionMatrix equality) {
		super(FractionMatrix.combine(coefficients, equality));
		coefficientWidth = coefficients.getWidth();
	}

	/**Returns the width of the coefficient.*/
	public int getCoefficientWidth(){return coefficientWidth;}

	/**Determines of a specific element is zero.*/
	public boolean isZero(int r, int c) {return elements[r][c].numerator().equals(BigInteger.ZERO);}

	/**Determines of a specific element is zero.*/
	public boolean isOne(int r, int c) {return elements[r][c].numerator().equals(BigInteger.ONE) && elements[r][c].denominator().equals(BigInteger.ONE);}

	/**Computes the scalar for the product of an element to be one.*/
	public Fraction getScalar(int r, int c) {return elements[r][c].powNegOne();}

	/**Computes the scalar for multiplying one element (1) and adding that product to the other element (2) to get zero.*/
	public Fraction getScalar(int r1, int c1, int r2, int c2) {return elements[r2][c2].div(elements[r1][c1]).negate();}

	/***Returns true if the specified row is zero and false if at least on element is not zero.*/
	public boolean isZeroRow(int r, int width) {
		for (int c = 0; c < width; c++) {
			if(!isZero(r, c)) return false;
		}
		return true;
	}

	/**Returns a matrix representing the coefficients.*/
	public FractionMatrix getCoefficients() {return FractionMatrix.submatrix(this, 0, coefficientWidth);}
		
	/**Returns a matrix representing the equality.*/
	public FractionMatrix getEquality() {return FractionMatrix.submatrix(this, coefficientWidth, getWidth());}

	// - - - - - - - - - - - - - - - - - - - - ROW OPERATIONS - - - - - - - - - - - - - - - - - - - - 
	
	/**Adds a scaled row (r1*scalar) to another (r2).*/
	public static void addRows(LinearSystem totalMatrix, int r1, int r2, Fraction scalar){
		for (int c = 0; c < totalMatrix.getWidth(); c++) {
			if(totalMatrix.isZero(r1, c)) continue; 
			
			totalMatrix.elements[r2][c] = Fraction.sum(totalMatrix.elements[r2][c], totalMatrix.elements[r1][c].multi(scalar)); // row-2 = row-2 + row1 * scalar
		}
	}
	
	/**Scales a row.*/
	public static void scaleRow(LinearSystem totalMatrix, int r, Fraction scalar){
		for (int c = 0; c < totalMatrix.getWidth(); c++) {
			if(totalMatrix.isZero(r, c)) continue;
			totalMatrix.elements[r][c] = totalMatrix.elements[r][c].multi(scalar); // row = row * scalar
		}
	}
	
	/**Swaps the given rows.*/
	public static void swapRows(LinearSystem totalMatrix, int r1, int r2) {
		Fraction[] t = copyRow(totalMatrix, r1);
		assignRow(totalMatrix.elements[r1], totalMatrix.elements[r2]);
		assignRow(totalMatrix.elements[r2], t);
	}
	
	/**Moves row r1 to r2 such that the total matrix is in the same order.*/
	public static void moveRow(LinearSystem totalMatrix, int r1, int r2) {
		Fraction[] t = copyRow(totalMatrix, r1);
		
		int i = r2 > r1 ? 1 : -1;
		for (int r = r1+i; i > 0 ? r <= r2 : r >= r2; r += i) {
			assignRow(totalMatrix.elements[r - i], totalMatrix.elements[r]);
		}

		assignRow(totalMatrix.elements[r2], t);
	}
	
	/**Assigns the values of the row r into a2.*/
	private static Fraction[] copyRow(LinearSystem totalMatrix, int r) {
		Fraction[] a = new Fraction[totalMatrix.getWidth()];
		for (int c = 0; c < totalMatrix.getWidth(); c++) {
			a[c] = totalMatrix.elements[r][c];
		}
		return a;
	}
	
	/**Assigns the values the row r2 into r1*/
	private static void assignRow(Fraction[] r1, Fraction[] r2) {System.arraycopy(r2, 0, r1, 0, r1.length);}
	
	
	
	// UTIL -------------------------------------------------------
	
	
	
	/**Clones the fraction to a new instance with all of the same values.*/
	public LinearSystem clone() {return new LinearSystem(new FractionMatrix(elements), coefficientWidth);}

	/**Returns a string representation of the given fraction-matrix.*/
	public static String toDoubleString(LinearSystem system, int digitCount) {
		int zeroes = (int) Math.pow(10, digitCount);
		return FractionMatrix.toNiceString(system, 
				(int r, int c) -> defaultFormater(c, system.getWidth(), system.getCoefficientWidth(), "| ", " ", " | ", " |\n"), 
				(Fraction f) -> {
					double value = f.doubleValue();	
					if(value % 1 == 0) return Integer.toString((int)value);	
					else return Double.toString((double)(Math.round(value * zeroes)) / zeroes);
				});
	}

	/**Returns a string representation of the given fraction-matrix.*/
	public static String toString(LinearSystem system) {
		return FractionMatrix.toNiceString(system, 
				(int r, int c) -> defaultFormater(c, system.getWidth(), system.getCoefficientWidth(),
						"| ", " ", " | ", " |\n"), 
				(Fraction f) -> Fraction.toString(f));
	}

	/**Returns a string representation of the given fraction-matrix.*/
	public static String asLatex(LinearSystem system) {
		return String.format("\\begin{bmatrix}\n%s\\end{bmatrix}\n", 
				FractionMatrix.toNiceString(system, 
					(int r, int c) -> defaultFormater(c, system.getWidth(), system.getCoefficientWidth(),
							"\t", " & ", " & \\vrule & ", " \\\\\n"), 
					(Fraction f) -> f.asLatex()));
	}

	/**The default way to format a linear system string.*/
	private static String defaultFormater(int c, int matrixWidth, int coefficientWidth, String rowBegin, String columnDelimiter, String matrixDelimiter, String rowEnd) {
		String format = "%s";
		if(c == 0) format = rowBegin + format;
		if(c < coefficientWidth - 1) format = format + columnDelimiter;
		else if(c == coefficientWidth - 1) format = format + matrixDelimiter;
		else format = format + rowEnd;
		return format;
	}
	
	/**Parses from string.*/
	public static FractionMatrix parse(String src, String rowRegex, String columnRegex) throws NumberFormatException{
		String[][] stringElements = splitMatrix(src, rowRegex, columnRegex);
		
		Fraction[][] elements = new Fraction[stringElements.length][stringElements[0].length];
		for (int r = 0; r < stringElements.length; r++) {
			for (int c = 0; c < stringElements[0].length; c++) {
				elements[r][c] = Fraction.parse(stringElements[r][c]);
			}
		}
		
		return new FractionMatrix(elements);
	}
	
	/**Splits a sting into a double array using a regular expression for the row separation and one for the column separation.*/
	private static String[][] splitMatrix(String src, String rowRegex, String columnRegex){
		String[] stringRows = src.split(rowRegex);
		
		String[][] stringMatrix = new String[stringRows.length][];
		for (int r = 0; r < stringRows.length; r++) {
			stringMatrix[r] = stringRows[r].split(columnRegex);
		}
		
		return stringMatrix;
	}
	
}
