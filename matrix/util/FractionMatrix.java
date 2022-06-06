package matrix.util;

public class FractionMatrix{

	//elements
	protected Fraction[][] elements;
	
	//constructor
	public FractionMatrix(Fraction[][] elements) {
		this.elements = new Fraction[elements.length][elements[0].length];
		for (int r = 0; r < elements.length; r++) {
			this.elements[r] = new Fraction[elements[0].length];
			System.arraycopy(elements[r], 0, this.elements[r], 0, elements[r].length);
		}
	}

	public FractionMatrix(FractionMatrix m) {
		elements = new Fraction[m.getHeight()][m.getWidth()];
		for (int r = 0; r < m.elements.length; r++) {
			System.arraycopy(m.elements[r], 0, this.elements[r], 0, m.elements[r].length);
		}
	}

	public FractionMatrix(Fraction f) {
		elements = new Fraction[1][1];
		elements[0][0] = f.clone();
	}
	
	
	//variables
	public int getWidth(){return elements[0].length;}
	public int getHeight(){return elements.length;}

	/**Returns a copy of a specific element.*/
	public Fraction getElement(int r, int c) {return elements[r][c].clone();}
	
	// OPERATORS --------------------------------------------------------------------------------------------------
	
	
	
	/***Adds two matrices together element wise.*/
	public static FractionMatrix add(FractionMatrix m1, FractionMatrix m2){return operate(m1, m2, (Fraction f1, Fraction f2) -> f1.add(f2));}
	
	/***Compute the scaling of a fraction-matrix by a fraction.*/
	public static FractionMatrix scale(FractionMatrix m, Fraction f){return operate(m, (Fraction f2) -> f2.multi(f));}
	
	/***Computes the given operation on the two matrices.*/
	public static FractionMatrix operate(FractionMatrix m1, FractionMatrix m2, FracFracOperation func){
		
		Fraction[][] outElements = new Fraction[m1.getHeight()][m1.getWidth()];
		
		for (int r = 0; r < m2.getHeight(); r++) {
			for (int c = 0; c < m2.getWidth(); c++) {
				outElements[r][c] = func.run(m1.elements[r][c], m2.elements[r][c]);
			}
		}
		
		return new FractionMatrix(outElements);
	}
	
	/***Computes a given operation on the given matrix.*/
	public static FractionMatrix operate(FractionMatrix m, FractionOperation func){
		
		Fraction[][] outElements = new Fraction[m.getHeight()][m.getWidth()];
		
		for (int r = 0; r < m.getHeight(); r++) {
			for (int c = 0; c < m.getWidth(); c++) {
				outElements[r][c] = func.run(m.elements[r][c]);
			}
		}
		
		return new FractionMatrix(outElements);
	}
	
	
	
	// UTIL --------------------------------------------------------------------------------------------------
	
	
	
	/***Clones the fraction to a new instance with all of the same values.*/
	public FractionMatrix clone() {return new FractionMatrix(elements);}
	
	/***Returns a string representation of the given fraction-matrix.*/
	public static String toString(FractionMatrix m) {
		return toNiceString(m, 
				(int r, int c) -> defaultFormater(c, m.getWidth(), 
						"| ", " ", " |\n"), 
				(Fraction f) -> Fraction.toString(f));
	}
	
	/***Returns a string representation of the given fraction-matrix.*/
	public static String asLatex(FractionMatrix m) {
		return String.format("\\begin{bmatrix}\n%s\\end{bmatrix}\n", 
				toNiceString(m, 
					(int r, int c) -> defaultFormater(c, m.getWidth(), 
						"\t", " & ", " \\\\\n"), 
					(Fraction f) -> f.asLatex()));
	}

	/**Returns a string representation of the given fraction-matrix.*/
	public static String toDoubleString(FractionMatrix m, int digitCount) {
		int zeroes = (int) Math.pow(10, digitCount);
		return FractionMatrix.toNiceString(m, 
				(int r, int c) -> defaultFormater(c, m.getWidth(), "| ", " ", " |\n"), 
				(Fraction f) -> {
					double value = f.doubleValue();	
					if(value % 1 == 0) return Integer.toString((int)value);	
					else return Double.toString((double)(Math.round(value * zeroes)) / zeroes);
				});
	}

	/***The default way to format a fraction-matrix string.*/
	private static String defaultFormater(int c, int matrixWidth, String rowBegin, String columnDelimiter, String rowEnd) {
		String format = "%s";
		if(c == 0) format = rowBegin + format;
		if(c <= matrixWidth - 2) format = format + columnDelimiter;
		else format = format + rowEnd;
		return format;
	}
	
	/***Returns a nicer string representation of the given fraction-matrix.*/
	public static String toNiceString(FractionMatrix m, FractionFormat formatFunc, FractionString stringFunc) {
		
		//Getting a matrix of all fractions as strings
		String strMatrix[][] = new String[m.getHeight()][m.getWidth()];
		for (int r = 0; r < m.getHeight(); r++) {
			for (int c = 0; c < m.getWidth(); c++) {
				strMatrix[r][c] = stringFunc.run(m.elements[r][c]);
			}
		}
		
		//Gets the size of each row.
		int rowSizes[] = new int[m.getWidth()];
		
		for (int c = 0; c < rowSizes.length; c++) {
			int size = 1;
			
			for (int r = 0; r < m.getHeight(); r++) {
				int len = strMatrix[r][c].length();
				size = len > size ? len : size;
			}
			
			rowSizes[c] = size;
		}
		
		//Combines each element using the formatter
		return toString(m, (int r, int c, Fraction f) -> {
			return String.format(formatFunc.run(r, c).replaceAll("%", "%" + rowSizes[c]), strMatrix[r][c]);
		});
	}
	
	/***Returns a nicer string representation of the given fraction-matrix.*/
	public static String toString(FractionMatrix m, FormatFracString func) {
		String str = "";
		for (int r = 0; r < m.getHeight(); r++) {
			for (int c = 0; c < m.getWidth(); c++) {
				str += func.run(r, c, m.elements[r][c]); 
			}
		}
		return str;
	}

	/***Parses from string.*/
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
	
	/***Splits a sting into a double array using a regular expression for the row separation and one for the column separation.*/
	private static String[][] splitMatrix(String src, String rowRegex, String columnRegex){
		String[] stringRows = src.split(rowRegex);
		
		String[][] stringMatrix = new String[stringRows.length][];
		for (int r = 0; r < stringRows.length; r++) {
			stringMatrix[r] = stringRows[r].split(columnRegex);
		}
		
		return stringMatrix;
	}
	
	/***Combine matrices.*/
	public static FractionMatrix combine(FractionMatrix m1, FractionMatrix m2) {
		
		Fraction[][] outElements = new Fraction[m1.getHeight()][m1.getWidth() + m2.getWidth()];
		
		for (int r = 0; r < outElements.length; r++) {
			for (int c = 0; c < m1.getWidth(); r++) {
				outElements[r][c] = m1.elements[r][c];
			}

			for (int c = 0; c < m2.getWidth(); r++) {
				outElements[r][c + m1.getWidth()] = m1.elements[r][c];
			}
		}
		
		return new FractionMatrix(outElements);
	}

	/***Splits a matrix in two, the c column is with the right matrix.*/
	public static FractionMatrix submatrix(FractionMatrix m, int startColumn, int endColumn) {
		
		Fraction[][] elements = new Fraction[m.getHeight()][endColumn - startColumn];
		
		for (int r = 0; r < m.getHeight(); r++) {
			for (int c = 0; c < elements[0].length; c++) {
				elements[r][c] = m.elements[r][c + startColumn];
			}
		}
		
		return new FractionMatrix(elements);
	}
	
	// INTERFACES --------------------------------------------------------------------------------------------
	
	
	
	private interface FracFracOperation{
		Fraction run(Fraction f1, Fraction f2);
	}

	private interface FractionOperation{
		Fraction run(Fraction f);
	}

	public interface FormatFracString{
		String run(int r, int c, Fraction f);
	}

	public interface FractionString{
		String run(Fraction f);
	}

	public interface FractionFormat{
		String run(int r, int c);
	}
	
}
