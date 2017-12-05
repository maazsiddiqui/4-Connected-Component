import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class FourCCC {
	
	public int printCounter;
	private int[][] zeroFramedArray;
	private int[] EQAry;
	private property[] propertyCC;
	private int neighborArray[]; // remember this to initiate at 3
	private int numRows;
	private int numCols;
	private int minVal;
	private int maxVal;
	private int newMin;
	private int newMax;
	private int newLabel;
	private int newLabelUsed;
	private String inFile; // pointer to args[0]
	private String outFile; // pointer to args[1]
	private String outFile2; // pointer to args[2]
	private String outFile3; // pointer to args[3]
	
	public FourCCC(String inFile, String outFile, String outFile2, String outFile3) throws FileNotFoundException {  // Constructor

		this.inFile = inFile;
		this.outFile = outFile;
		this.outFile2 = outFile2;
		this.outFile3 = outFile3;
		
		Scanner dataFile = new Scanner(new File(inFile));
		
		numRows = dataFile.nextInt();
		numCols = dataFile.nextInt();
		minVal = dataFile.nextInt();
		maxVal = dataFile.nextInt();
		
		dataFile.close(); // input file closed
		
		zeroFramedArray = new int[numRows+2][numCols+2];
		
		EQAry = new int[(numRows*numCols)/4];
		for(int i = 0; i < EQAry.length; i++) EQAry[i] = i;
		
		neighborArray = new int[3];

		newLabel = 0;
		newMin = 0;
		newMax = 0;
		printCounter = 1;
	
	}
	
	public void loadImage() throws FileNotFoundException {
		
		Scanner dataFile = new Scanner(new File(inFile));
		
		dataFile.nextLine();	// skip header line
		
		for (int r = 1; r <= numRows; r++) {
			for (int c = 1; c <= numCols; c++) {
				zeroFramedArray[r][c] = dataFile.nextInt();
			}
		}
		
		dataFile.close(); 	// input file closed	
	}
	
	void fourCCC_Pass1() {	
		for (int r = 1; r <= numRows; r++) {
			for (int c = 1; c <= numCols; c++) {
				if (zeroFramedArray[r][c] > 0) {
					zeroFramedArray[r][c] = casesForPassOne(r, c);	
				}
			}
		}			
	}
	
	int casesForPassOne(int row, int col) {
		
		neighborArray[0] = zeroFramedArray[row-1][col];
		neighborArray[1] = zeroFramedArray[row][col-1];
		
		int min = findMin(neighborArray, 2);
		int max = findMax(neighborArray, 2);
		
		// CASE 1
		if (max == 0) {
			newLabel++;
			return newLabel;
		}	
		
		// CASE 2
		if (max > 0 && min == max) {
			return min;
		}
		
		// CASE 3
		if (max > 0 && min > 0) {
			manageEQAry(neighborArray, 2, min);
			return min;
		}
		else {
			return max;
		}

	}
	
	void fourCCC_Pass2() {
		for (int r = numRows; r >= 1; r--) {
			for (int c = numCols; c >= 1; c--) {
				if (zeroFramedArray[r][c] > 0) {
					zeroFramedArray[r][c] = casesForPassTwo(r, c);		
				}
			}
		}	
	}
	
	
	int casesForPassTwo(int row, int col) {
		
		neighborArray[0] = zeroFramedArray[row][col];
		neighborArray[1] = zeroFramedArray[row][col+1];
		neighborArray[2] = zeroFramedArray[row+1][col];
		
		int min = findMin(neighborArray, 3);
		int max = findMax(neighborArray, 3);
		
		// CASE 1
		if (neighborArray[1] == 0 && neighborArray[2] == 0) {
			return neighborArray[0];
		}	
		
		// CASE 2
		if ((neighborArray[0] == neighborArray[1]) &&
			(neighborArray[0] == neighborArray[2])) {
			return neighborArray[0];
		}
		
		// CASE 3
		if (min > 0) {
			manageEQAry(neighborArray, 3, min);
			return min;
		}
		else {
			int temp = neighborArray[0];
			for (int i = 0; i < 3; i++) {
				if (neighborArray[i] > 0 && neighborArray[i] < temp) {
					temp = neighborArray[i];
				}
			}
			manageEQAry(neighborArray, 3, temp);
			return temp;
		}

	}
	
	void fourCCC_Pass3() {
		
		updateEQAry();
		
		newMin = zeroFramedArray[0][0];
		newMax = zeroFramedArray[0][0];
		
		for (int r = 1; r <= numRows; r++) {
			for (int c = 1; c <= numCols; c++) {
				zeroFramedArray[r][c] = EQAry[zeroFramedArray[r][c]];
				if (newMin > zeroFramedArray[r][c]) newMin = zeroFramedArray[r][c];
				if (newMax < zeroFramedArray[r][c]) newMax = zeroFramedArray[r][c];
			}
		}
	}
	
	void printFullImage() throws IOException {
		
		PrintWriter out2 = new PrintWriter(new FileWriter(outFile2));
		
		out2.println(numRows + " " + numCols + " " + newMin + " " + newMax);
		
		for (int r = 1; r <= numRows; r++) {
			for (int c = 1; c <= numCols; c++) {
				out2.print(zeroFramedArray[r][c] + " ");
			}
			out2.println();
		}
				
		out2.close();
		return;
	}
	
	void updateEQAry() {
		
		newLabelUsed = 1;
		
		for (int i = 1; i <= newLabel; i++) {
			if(EQAry[i] == i) {
//				if (newLabelUsed < i) EQAry[i] = EQAry[newLabelUsed];
//				else 
					EQAry[i] = newLabelUsed++;	
			}
			else {
				EQAry[i] = EQAry[EQAry[i]];
			}
		}
	}
    
	
	void manageEQAry(int arr[], int size, int val) {		
		for(int i = 0; i < size; i++) {
			if (EQAry[arr[i]] > val ) EQAry[arr[i]] = val;
		}
	}
	
	int findMin(int arr[], int size) {
	
		int min = arr[0];
		
		for (int i = 1; i < size; i++) {
			if (arr[i] < min) min = arr[i];
		}
		
		return min;
	}

	int findMax(int arr[], int size) {
		
		int max = arr[0];
		
		for (int i = 1; i < size; i++) {
			if (max < arr[i]) max = arr[i];
		}
		
		return max;
	}
	
	void prettyPrintArray() throws IOException {
		
		PrintWriter out1 = new PrintWriter(new FileWriter(outFile, true));
		
		out1.println("Pretty Print After Pass " + printCounter);
		out1.println("=========================");
		
		for (int r = 1; r <= numRows; r++) {
			for (int c = 1; c <= numCols; c++) {
				if (zeroFramedArray[r][c] > 0) {
					out1.print(zeroFramedArray[r][c] + " ");		
				}
				else out1.print(" ");
			}
			out1.println();
		}
		
		out1.close();	// close output file
		return;
	}
	
	void printEQAry() throws IOException {
	
		PrintWriter out1 = new PrintWriter(new FileWriter(outFile, true));
		
		out1.println();	
		out1.println("EQAry After Pass " + printCounter);
		out1.println("==================");
		
		out1.println("Index" + "\t" + "Value");
		for(int i = 0; i <= newLabel; i++) out1.println(i + "\t\t" + EQAry[i]);
		
		out1.println();
		out1.println();
		out1.println();
		
		out1.close();	// close output file
		return;
	}
	
	void findCCProperties() {
		
		propertyCC = new property[newLabelUsed+1];
		for(int i = 0; i <= newLabelUsed; i++) propertyCC[i] = new property();
		
		int count = 1;
		
		while (count <= newLabelUsed) {
			
			propertyCC[count].bb_minRow = numRows;
			propertyCC[count].bb_minCol = numCols;
			
			for (int r = 1; r <= numRows; r++) {
				for (int c = 1; c <= numCols; c++) {
					if (zeroFramedArray[r][c] == count) {
						
						if(propertyCC[count].bb_minRow > r) propertyCC[count].bb_minRow = r;
						if(propertyCC[count].bb_minCol > c) propertyCC[count].bb_minCol = c;
						if(propertyCC[count].bb_maxRow < r) propertyCC[count].bb_maxRow = r;
						if(propertyCC[count].bb_maxCol < c) propertyCC[count].bb_maxCol = c;	
						
						propertyCC[count].ccLabel = count;
						propertyCC[count].noOfPixels++;	
					}	
				}
			}
			
			if (propertyCC[count].bb_minRow > 0) propertyCC[count].bb_minRow--;
			if (propertyCC[count].bb_minCol > 0) propertyCC[count].bb_minCol--;
			if (propertyCC[count].bb_maxRow > 0) propertyCC[count].bb_maxRow--;
			if (propertyCC[count].bb_maxCol > 0) propertyCC[count].bb_maxCol--;
			count++;
		}	
	}
	
	void printProperties() throws IOException {
		
		PrintWriter out3 = new PrintWriter(new FileWriter(outFile3));
		
		for(int i = 1; i < newLabelUsed; i++) {
			out3.println("Properties of Label " + propertyCC[i].ccLabel);
			out3.println("======================");
			out3.println("Min Row: " + propertyCC[i].bb_minRow);
			out3.println("Min Col: " + propertyCC[i].bb_minCol);
			out3.println("Max Row: " + propertyCC[i].bb_maxRow);
			out3.println("Max Col: " + propertyCC[i].bb_maxCol);
			out3.println("No. of Pixels: " + propertyCC[i].noOfPixels);	
			out3.println();
		}
		
		out3.close();
		return;	
	}

}
