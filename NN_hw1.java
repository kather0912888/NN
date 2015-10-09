
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;

public class NN_hw1 {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		double learningRate = .1;
		int numLayer1 = 203;
		int numLayer2 = 80;
		int numLayer3 = 26;
		node[][] nodeTable = new node[3][numLayer1];
		double[] inputTable;
		double[][] actualTable;
		double[][] deltaTable = new double[3][80];
		// formula a = new formula();
		int interationCounter = 1;
		double accurateSum = 0;
		int Wordcount = 0;
		int learningAmount = 1;
		double avgAccurate = 0;

		// create nodes and set weight between 0 to 1
		initializationForNode(nodeTable, numLayer1, numLayer2, numLayer3);

		// do{

		// open file from txt
		FileReader fr = new FileReader("nettalk.txt");
		BufferedReader br = new BufferedReader(fr);
		inputTable = readData_inputTable(br);
		actualTable = readData_actualTable(br);


		// temp
		double[] tempinputTable = new double[25];
		double[][] tempactualTable = new double[25][26];
		
		for(int i=0;i<25;i++)
			tempinputTable[i] = inputTable[i];
		for(int i=0;i<25;i++)
			for(int j=0;j<26;j++)
				tempactualTable[i][j] = actualTable[i][j];
		
		// one interation per word
		int t = 0;
		while (t++ <= 100) {
			

			for(int i=0;i<25;i++)
				inputTable[i] = tempinputTable[i];
			for(int i=0;i<25;i++)
				for(int j=0;j<26;j++)
					actualTable[i][j] = tempactualTable[i][j];
			
			for (Wordcount = 0; Wordcount < learningAmount ; Wordcount++) {

				// put the info to table form txt

				// count char in one word
				int charcount = 0;
				for (int i = 0; i <= inputTable.length - 1; i++)
					if (inputTable[i] != 28)
						charcount++;

				// one interation per char count
				for (int no = 0; no <= charcount - 1; no++) {

					printWeights(2, 0, nodeTable);

					initializationInputForNodeTable(nodeTable, inputTable);

					printResult(nodeTable, actualTable);

					// System.out.println();

					accurateSum += accurate(actualTable, nodeTable);
					avgAccurate = accurateSum / interationCounter;
					System.out.printf("interation : %d		Avgaccurate : %.3f \n", interationCounter++, avgAccurate);
					// System.out.printf("interation : %d accurate : %.3f
					// \n",interationCounter++,accurate(actualTable,nodeTable));

					// System.out.print(" ");
					// initialize deltaTable at layer3
					for (int j = 0; j <= 25; j++) {
						deltaTable[2][j] = deltaFuction(actualTable[0][j], nodeTable[2][j]);
						// System.out.printf("%.3f " , deltaTable[2][j]);
					}
					// System.out.println();
					// System.out.print(" ");
					// initialize deltaTable at layer2
					for (int j = 0; j <= 79; j++) {
						deltaTable[1][j] = deltaFuction(nodeTable[1][j], nodeTable, deltaTable);
						// System.out.printf("%.3f " , deltaTable[1][j]);
					}
					System.out.println();

					// adjust the weight according to delta
					for (int j = 0; j <= 25; j++)
						applyGDToWeight(nodeTable[2][j], learningRate, deltaTable);
					for (int j = 0; j <= 79; j++)
						applyGDToWeight(nodeTable[1][j], learningRate, deltaTable);

					// transfer inputTable and actualTable
					shiftLeft(inputTable);
					shiftLeft(actualTable);

				}

			}
		}
		fr.close();
		// }while(avgAccurate<=0.85);
		// performanceAfterLearning(nodeTable) ;

	}

	public static void initializationForNode(node[][] nodeTable, int numLayer1, int numLayer2, int numLayer3) {
		// establish input layer for 203 units
		for (int i = 0; i <= numLayer1 - 1; i++) {
			nodeTable[0][i] = new node(0, i);

		}
		// establish hidden layer for 80 units
		for (int i = 0; i <= numLayer2 - 1; i++) {
			nodeTable[1][i] = new node(1, i);
			for (int j = 0; j <= numLayer1 - 1; j++) {
				double temp = Math.random();
				nodeTable[1][i].setWeight(j, temp);
			}
		}
		// establish output layer for 26 units
		for (int i = 0; i <= numLayer3 - 1; i++) {
			nodeTable[2][i] = new node(2, i);
			for (int j = 0; j <= numLayer2 - 1; j++) {
				double temp = Math.random();
				nodeTable[2][i].setWeight(j, temp);

			}
		}
	}

	public static void initializationInputForNodeTable(node[][] nodeTable, double[] inputTable) {
		// initialize the nodeTable input
		for (int i = 0; i <= 202; i++) {
			nodeTable[0][i].input[0] = 0;
		}

		// initialize the input of the nodeTable at layer1
		int position = 0;
		for (int i = 0; i <= 6; i++) {
			position += (int) inputTable[i];
			nodeTable[0][position].input[0] = 1;
			position = 29 * (i + 1);
		}

		// initialize the input of the nodeTable at layer2
		for (int i = 0; i <= 79; i++) {
			for (int j = 0; j <= 202; j++) {
				nodeTable[1][i].input[j] = nodeTable[0][j].input[0];
			}
		}
		// initialize the input of the nodeTable at layer3
		for (int i = 0; i <= 25; i++) {
			for (int j = 0; j <= 79; j++) {
				nodeTable[2][i].input[j] = nodeTable[1][j].output(1);
			}
		}
	}

	public static int inputTransform(char eng) {
		if (eng >= 97 && eng <= 97 + 26)
			return eng - 97;
		else if (eng == ' ')
			return 26;
		else if (eng == ',')
			return 27;
		else
			return 28;
	}

	// read one word from txt and create a input table for that word
	public static double[] readData_inputTable(BufferedReader br) throws IOException {
		double[] inputTable = new double[25];
		for (int i = 0; i <= inputTable.length - 1; i++) {
			inputTable[i] = 28;
		}
		int count = 3;
		while (br.ready()) {
			int temp = inputTransform((char) br.read());
			// System.out.print(temp + " ");
			if (temp != 28) {
				inputTable[count++] = temp;
			} else
				break;
		}
		return inputTable;
	}

	// read one char of actual value form txt
	public static double[] readData_actual_per_char(char word) throws IOException {

		double[] r = new double[26];
		switch (word) {
		case 'a':
			r[16] = r[13] = r[3] = 1;
			break;
		case 'b':
			r[12] = r[0] = r[6] = 1;
			break;
		case 'c':
			r[15] = r[4] = 1;
			break;
		case 'd':
			r[12] = r[2] = r[6] = 1;
			break;
		case 'e':
			r[15] = r[13] = r[1] = 1;
			break;
		case 'f':
			// TODO
			r[0] = r[8] = 1;
			break;
		case 'g':
			r[12] = r[4] = r[6] = 1;
			break;
		case 'h':
			// TODO
			r[5] = r[10] = 1;
			break;
		case 'i':
			r[14] = r[13] = r[0] = 1;
			break;
		case 'k':
			// TODO
			r[4] = r[6] = 1;
			break;
		case 'l':
			r[12] = r[1] = r[11] = 1;
			break;
		case 'm':
			r[12] = r[0] = r[7] = 1;
			break;
		case 'n':
			r[12] = r[2] = r[7] = 1;
			break;
		case 'o':
			r[15] = r[13] = r[5] = 1;
			break;
		case 'p':
			// TODO
			r[0] = r[6] = 1;
			break;
		case 'r':
			r[12] = r[3] = r[11] = 1;
			break;
		case 's':
			// TODO
			r[2] = r[8] = 1;
			break;
		case 't':
			// TODO
			r[2] = r[6] = 1;
			break;
		case 'u':
			r[14] = r[13] = r[5] = 1;
			break;
		case 'v':
			r[12] = r[0] = r[8] = 1;
			break;
		case 'w':
			r[12] = r[0] = r[10] = 1;
			break;
		case 'x':
			r[15] = r[3] = 1;
			break;
		case 'y':
			r[12] = r[3] = r[10] = 1;
			break;
		case 'z':
			r[12] = r[2] = r[8] = 1;
			break;

		case 'A':
			r[15] = r[13] = r[1] = r[2] = 1;
			break;
		case 'C':
			// TODO
			r[3] = r[9] = 1;
			break;
		case 'D':
			r[12] = r[1] = r[8] = 1;
			break;
		case 'E':
			r[15] = r[0] = r[1] = 1;
			break;
		case 'G':
			r[12] = r[4] = r[7] = 1;
			break;
		case 'I':
			r[14] = r[0] = 1;
			break;
		case 'J':
			r[12] = r[4] = r[7] = 1;
			break;
		case 'K':
			// TODO
			r[3] = r[8] = r[4] = r[9] = 1;
			break;
		case 'L':
			r[12] = r[2] = r[11] = 1;
			break;
		case 'M':
			r[12] = r[1] = r[7] = 1;
			break;
		case 'N':
			r[12] = r[3] = r[7] = 1;
			break;
		case 'O':
			r[15] = r[13] = r[2] = r[3] = 1;
			break;
		case 'Q':
			r[12] = r[0] = r[4] = r[9] = r[6] = 1;
			break;
		case 'R':
			r[12] = r[4] = r[11] = 1;
			break;
		case 'S':
			// TODO
			r[3] = r[8] = 1;
			break;
		case 'T':
			// TODO
			r[1] = r[8] = 1;
			break;
		case 'U':
			r[14] = r[4] = 1;
			break;
		case 'W':
			r[14] = r[15] = r[13] = r[3] = r[4] = 1;
			break;
		case 'X':
			// TODO
			r[9] = r[1] = r[2] = 1;
			break;
		case 'Y':
			r[14] = r[13] = r[0] = r[1] = r[2] = 1;
			break;
		case 'Z':
			r[12] = r[3] = r[8] = 1;
			break;
		case '@':
			r[16] = r[1] = 1;
			break;
		case '!':
			// TODO
			r[3] = r[8] = 1;
			break;
		case '#':
			r[12] = r[3] = r[4] = r[9] = 1;
			break;
		case '*':
			r[12] = r[10] = r[0] = r[16] = r[2] = 1;
			break;
		case '|':
			r[14] = r[0] = r[1] = 1;
			break;
		case '^':
			r[16] = r[2] = 1;
			break;
		case '-':
			r[17] = r[18] = 1;
			break;
		case '_':
			r[19] = r[18] = 1;
			break;
		case '.':
			r[19] = r[20] = 1;
			break;

		}
		return r;
	}

	// arrange the actual value to table, index1: one char , index2 : char to
	// actualValue set(26)
	public static double[][] readData_actualTable(BufferedReader br) throws IOException {
		double[][] actualTable = new double[25][26];

		char temp = (char) br.read();
		int count = 0;
		while (temp != '	') {
			actualTable[count] = readData_actual_per_char(temp);
			temp = (char) br.read();
			count++;
		}
		for (int i = 0; i <= count - 1; i++) {

			temp = (char) br.read();
			switch (temp) {
			case '<':
				actualTable[i][21] = 1;
				break;
			case '>':
				actualTable[i][22] = 1;
				break;
			case '1':
				actualTable[i][23] = actualTable[i][24] = 1;
				break;
			case '2':
				actualTable[i][23] = 1;
				break;
			case '0':
				actualTable[i][21] = actualTable[i][22] = actualTable[i][25] = 1;
				break;
			default:
				break;
			}
		}
		br.read();
		br.read();
		br.read(); // skip the 0 1 number
		return actualTable;
	}

	public static void printResult(node[][] nodeTable, double[][] actualTable) {
		System.out.print("num	 ");

		for (int i = 0; i <= 202; i++) {
			System.out.printf(" 	%s ", i);
		}
		System.out.println();
		System.out.print("layer1_output");
		for (int i = 0; i <= 202; i++) {
			System.out.printf(" 	%.2f ", nodeTable[0][i].input[0]);
		}
		System.out.println();
		System.out.print("layer2_output");
		for (int i = 0; i <= 79; i++) {
			System.out.printf(" 	%.2f ", nodeTable[1][i].output(1));
		}
		System.out.println();
		System.out.print("layer3_output");
		for (int i = 0; i <= 25; i++) {
			System.out.printf(" 	%.2f ", nodeTable[2][i].output(2));
		}
		System.out.println();
		System.out.print("actualValue");
		for (int i = 0; i <= 25; i++) {
			System.out.printf(" 	%s ", actualTable[0][i]);
		}
		System.out.println();
		System.out.printf("accurate : %.2f", accurate(actualTable, nodeTable));
	}

	public static void printWeights(int targetLayer, int targetUnit, node[][] nodeTable) {
		System.out.print("weight	");
		for (int i = 0; i <= 79; i++) {
			System.out.printf(" 	%.2f ", nodeTable[targetLayer][targetUnit].getWeight(i));
		}

	}

	public static void shiftLeft(double[] table) {
		for (int i = 0; i <= table.length - 1 - 1; i++) {
			table[i] = table[i + 1];

		}
	}

	public static void shiftLeft(double[][] table) {
		for (int i = 0; i <= table.length - 1 - 1; i++) {
			table[i] = table[i + 1];
		}

	}

	// evaluate the performance, whether the output hit the actual value
	public static double accurate(double[][] actualTable, node[][] nodeTable) {
		double flag = 0;
		for (int i = 0; i <= 25; i++) {
			if (Math.abs(actualTable[0][i] - nodeTable[2][i].output(2)) >= 0.5)
				return 0;
		}
		return 1;
	}

	public static double deltaFuction(double actualValue, node node) {
		return (node.output(2) - actualValue) * node.output(2) * (1 - node.output(2));
	}

	public static double deltaFuction(node node, node[][] nodeTable, double[][] deltaTable) {
		double result = 0;

		for (int w = 0; w <= 25; w++) {
			result += deltaTable[node.getLayer() + 1][w] * nodeTable[node.getLayer() + 1][w].getWeight(node.getUnit())
					* node.output(1) * (1 - node.output(1));
		}
		return result;
	}

	public static double gradientDescent(node node, double learningRate, int targetUnit, double[][] deltaTable) {
		return -learningRate * deltaTable[node.getLayer()][node.getUnit()] * (node.getInput(targetUnit));
	}

	public static void applyGDToWeight(node node, double learningRate, double[][] deltaTable) {
		for (int i = 0; i <= (node.weight).length - 1; i++)
			node.weightAdjust(i, gradientDescent(node, learningRate, i, deltaTable));
	}

	public static void performanceAfterLearning(node[][] nodeTable) throws IOException {
		double learningRate = .3;
		int numLayer1 = 203;
		int numLayer2 = 80;
		int numLayer3 = 26;
		double[] inputTable;
		double[][] actualTable;
		// formula a = new formula();
		int interationCounter = 1;
		double accurateSum = 0;
		int Wordcount = 0;
		int learningAmount = 100;

		initializationForNode(nodeTable, numLayer1, numLayer2, numLayer3);

		// open file from txt
		FileReader fr = new FileReader("nettalk.txt");
		BufferedReader br = new BufferedReader(fr);

		// one interation per word
		for (Wordcount = 0; Wordcount <= 20000; Wordcount++) {

			// put the info to table form txt
			inputTable = readData_inputTable(br);
			actualTable = readData_actualTable(br);

			// count char in one word
			int charcount = 0;
			for (int i = 0; i <= inputTable.length - 1; i++)
				if (inputTable[i] != 28)
					charcount++;

			// one interation per char count
			for (int no = 0; no <= charcount - 1; no++) {

				initializationInputForNodeTable(nodeTable, inputTable);

				printResult(nodeTable, actualTable);
				System.out.println();

				accurateSum += accurate(actualTable, nodeTable);
				double avgAccurate = accurateSum / interationCounter;
				System.out.printf("interation : %d 		Avgaccurate : %.3f \n", interationCounter++, avgAccurate);
				System.out.println();

				// transfer inputTable and actualTable
				shiftLeft(inputTable);
				shiftLeft(actualTable);

			}
		}

		// 1000
		fr.close();
	}
}

class node {
	private int layer;
	private int unit;
	double[] weight = new double[203];
	double[] input = new double[203];

	// construtor
	public node(int layer, int unit) {
		this.layer = layer;
		this.unit = unit;
	}

	public int getLayer() {
		return this.layer;
	}

	public int getUnit() {
		return this.unit;
	}

	public double getWeight(int targetUnit) {
		return this.weight[targetUnit];
	}

	public double getInput(int targetUnit) {
		return this.input[targetUnit];
	}

	public double getTotalInput(int Layer) {
		double totalInput = 0;
		if (Layer == 2) {
			for (int i = 0; i <= 79; i++)
				totalInput += this.weight[i] * this.input[i];
		} else if (Layer == 1) {
			for (int i = 0; i <= 202; i++)
				totalInput += this.weight[i] * this.input[i];
		} else
			;
		return totalInput;
	}

	public void setWeight(int targetUnit, double newWeight) {
		this.weight[targetUnit] = newWeight;
	}

	public void weightAdjust(int targetUnit, double weightMove) {
		this.weight[targetUnit] += weightMove;
	}

	public double output(int Layer) {
		/*
		 * if(Layer == 2) return Math.round((1/(1 +
		 * Math.exp(getTotalInput())))); else
		 */
		 layer /= 10000;
		double d = Math.pow(Math.E, -getTotalInput(Layer));
		return 1 / (1 + d);
		// return (1 / (1 + Math.exp(-getTotalInput(Layer))));
	}
}
/*
 * class formula{
 * 
 * double[][] deltaTable = new double[3][80];
 * 
 * public static double error(double[] output, double[] actualValue){ double sum
 * = 0; for(int i =0 ; i<=output.length-1 ; i++) sum += Math.pow(output[i] -
 * actualValue[i], 2); return sum; } public void deltaFuction(double
 * actualValue, node node){ deltaTable[node.getLayer()][node.getUnit()] =
 * (node.output() - actualValue)*node.output()*(1-node.output()); } public void
 * deltaFuction(node node ,node[][] nodeTable){ for(int w = 0 ;w <=25 ;w++){
 * deltaTable[node.getLayer()][node.getUnit()]=0;
 * deltaTable[node.getLayer()][node.getUnit()] +=
 * deltaTable[node.getLayer()+1][w
 * ]*nodeTable[node.getLayer()+1][w].getWeight(node
 * .getUnit())*node.output()*(1-node.output()); } } public double
 * gradientDescent(node node, double learningRate, int targetUnit){ return
 * -learningRate
 * *deltaTable[node.getLayer()][node.getUnit()]*(node.getInput(targetUnit)); }
 * public void applyGDToWeight(node node, double learningRate){ for(int i=0 ;
 * i<= (node.weight).length-1 ; i++)
 * node.weightAdjust(i,gradientDescent(node,learningRate ,i)); }
 * 
 * 
 * }
 */