import java.io.IOException;

public class main {

	public static void main(String[] args) throws IOException {

		FourCCC fourCCC = new FourCCC(args[0], args[1], args[2], args[3]); // input file, output file
		fourCCC.loadImage();
		
		// PASS 1
		fourCCC.fourCCC_Pass1();
		fourCCC.prettyPrintArray();
		fourCCC.printEQAry();
		fourCCC.printCounter++;
		
		// PASS 2
		fourCCC.fourCCC_Pass2();
		fourCCC.prettyPrintArray();
		fourCCC.printEQAry();
		fourCCC.printCounter++;

		// PASS 3
		fourCCC.fourCCC_Pass3();
		fourCCC.prettyPrintArray();
		fourCCC.printEQAry();
		
		// Print Binary Image
		fourCCC.printFullImage();

		// Print CC Properties
		fourCCC.findCCProperties();
		fourCCC.printProperties();

	}

}
