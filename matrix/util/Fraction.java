package matrix.util;

import java.math.BigInteger;

@SuppressWarnings("serial")
public class Fraction extends Number{

	// : numerator / denominator
	private BigInteger numerator, denominator; 
	
	public BigInteger numerator() {return numerator;}
	public BigInteger denominator() {return denominator;}

	/***A fraction.*/
	public Fraction(long numerator, long denominator) {
		this.numerator = BigInteger.valueOf(numerator);
		this.denominator = BigInteger.valueOf(denominator);
	}

	/***A fraction.*/
	public Fraction(long value) {
		this.numerator = BigInteger.valueOf(value);
		this.denominator = BigInteger.ONE;
	}
	
	/***A fraction.*/
	public Fraction(BigInteger numerator, BigInteger denominator) {
		this.numerator = numerator;
		this.denominator = denominator;
	}
	
	/***Given a single long, the fraction is assumed to be value/1.*/
	public Fraction(BigInteger value) {
		this.numerator = value;
		this.denominator = BigInteger.ONE;
	}

	/***Returns the simplest form of the current fraction.*/
	public static Fraction simplify(BigInteger numerator, BigInteger denominator) {
		BigInteger gcd = numerator.gcd(denominator);
		boolean negate = !denominator.abs().equals(denominator);
		
		if(negate) return new Fraction(numerator.divide(gcd).negate(), denominator.divide(gcd).negate());
		return new Fraction(numerator.divide(gcd), denominator.divide(gcd));
	}
	
	/***Returns the simplest form of the current fraction.*/
	public Fraction simplify() {return simplify(this.numerator, this.denominator);}
	
//	/***Returns the greatest common denominator.*/
//	private long gcd(long a, long b) {
//	    while(b != 0) {
//	    	long t = b;
//	        b = a % b;
//	        a = t;
//	    }
//	    return a;
//	}
	
	/***Sums together a number of fractions.*/
	public static Fraction sum(Fraction... f) throws ArithmeticException{
		
		Fraction sum = f[0];
		
		for (int i = 1; i < f.length; i++) {
			sum = sum.add(f[i]);
		}
		
		return sum;
	}
	
	/***Adds two given fractions, such that a common denominator is found.*/
	public Fraction add(Fraction f) throws ArithmeticException {

		// n1/d1 + n2/d2 = (n1*d2 + n2*d1) / (d1 * d2)
		return simplify(
				numerator.multiply(f.denominator).add(f.numerator.multiply(denominator)),
				denominator.multiply(f.denominator)).simplify();
	}
	
	/***Subtracts two given fractions, such that a common denominator is found.*/
	public Fraction sub(Fraction f) throws ArithmeticException {
		
		// n1/d1 - n2/d2 = (n1*d2 - n2*d1) / (d1 * d2)
		return simplify(
				numerator.multiply(f.denominator).subtract(f.numerator.multiply(denominator)),
				denominator.multiply(f.denominator));
	}

	/***Multiplying the current fraction with the given.*/
	public Fraction multi(Fraction f) throws ArithmeticException {
		
		// n1/d1 * n2/d2 = (n1 * n2) / (d1 * d2)
		return simplify(
				numerator.multiply(f.numerator),
				denominator.multiply(f.denominator));
	}
	
	/***Divides the current fraction with the given such that no rounding error occur.*/
	public Fraction div(Fraction f) throws ArithmeticException {
		
		// n1/d1 / n2/d2 = (n1 * d2) / (d1 * n2)
		return simplify(
				numerator.multiply(f.denominator),
				denominator.multiply(f.numerator));
	}
	
	/***Multiplies the current fraction with negative 1.*/
	public Fraction negate() {
		return new Fraction(numerator.negate(), denominator);
	}
	
	/***Returns the current fraction powered by -1.*/
	public Fraction powNegOne() {
		return new Fraction(denominator, numerator);
	}
	
	/***Clones the fraction to a new instance with all of the same values.*/
	public Fraction clone() {
		return new Fraction(numerator, denominator);
	}
	
	/***Parses from string (the form [integer/integer] or [integer])*/
	public static Fraction parse(String s) throws NumberFormatException{
		
		//getting strings
		String[] s2 = s.split("/");
		if(s2.length > 2 || s2.length == 0) throw new NumberFormatException("For input string: \"" + s + "\"");
		
		//parsing values
		long[] i = new long[s2.length]; 
		
		for (int j = 0; j < i.length; j++) {
			i[j] = Long.parseLong(s2[j]);
		}
		
		//returning fraction
		if(i.length == 1) {
			return new Fraction(i[0]);
		}else {
			return new Fraction(i[0], i[1]).simplify();
		}
	}
	
	/***Returns a string representation of the given fraction, either in the form "numerator/denominator" or if the denominator is equal to one, then the from "numerator"*/
	public static String toString(Fraction f) {
		if(f.denominator().equals(BigInteger.ONE)) return f.numerator().toString();
		else return String.format("%s/%s", f.numerator().toString(), f.denominator().toString());
	}
	
	/***Returns this fraction as a string, using the same rules as Fraction.toString(Fraction f).*/
	@Override
	public String toString() {return toString(this);}
	
	/***Returns this fraction as a double, by dividing the numerator by the denominator.*/
	@Override
	public double doubleValue() {return numerator.doubleValue() / denominator.doubleValue();}
	
	/***Returns this fraction as a double, by dividing the numerator by the denominator.*/
	@Override
	public float floatValue() {return numerator.floatValue() / denominator.floatValue();}
	
	/***Returns this fraction as an int, by dividing the numerator by the denominator.*/
	@Override
	public int intValue() {return numerator.divide(denominator).intValue();}
	
	/***Returns this fraction as a long, by dividing the numerator by the denominator.*/
	@Override
	public long longValue() {return numerator.divide(denominator).longValue();}
	
	/***Returns the given fraction as a string in latex form.*/
	public static String asLatex(Fraction f) {
		if(f.denominator().equals(BigInteger.ONE)) return f.numerator().toString();
		else return String.format("\\frac{%s}{%s}", f.numerator().toString(), f.denominator().toString());
	}

	/***Returns the given fraction as a string in latex form.*/
	public String asLatex() {return asLatex(this);}
	
}
