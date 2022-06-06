package matrix.util.inputOutput;

import java.util.ArrayList;
import java.util.Scanner;

import matrix.util.FractionMatrix;

public class MatrixInput {
	
	/***Reads a total matrix from the standard input.*/
	public static FractionMatrix readMatrix(){
		String input = " " + readLines() + " ";

		input = input.replaceAll("[^\\d\\/\\-\\n]+", " ");
		input = input.replaceAll("[^\\S\\n]+", " ");

		input = input.replaceAll(" \n", "\n");
		input = input.replaceAll("\n ", "\n");
		
		return FractionMatrix.parse(input.substring(1, input.length() - 1), "\n", " ");
	}
	
	/***This function returns an array of all lines taken as input until an empty line is inputed (that is not included).*/
	private static String readLines(){
		@SuppressWarnings("resource")
		Scanner in = new Scanner(System.in);
		ArrayList<String> lines = new ArrayList<>();
		
		//reading one line at a time
		boolean flag = true;
		do {
			
			String line = in.nextLine();
			
			flag = !line.equals("");
			if(flag) lines.add(line);
		}while(flag);
		
		return String.join("\n", lines);
	}
}

