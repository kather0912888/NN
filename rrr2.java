import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

//import NeuralNetwork.NN;

public class rrr2 {
	static // features to phoneme & stress table
	HashMap<String, String> map = new HashMap<>();

	// phoneme to articulatory features
	private static double[] phonemeToFeatures(char c, char d) {
		double[] r = new double[21 + 5];
		/*
		 * 0 1 2 3 front1 front2 central1 central2
		 * 
		 * 4 5 6 7 8 back1 back2 stop nasal fricative
		 * 
		 * 9 10 11 affricative glide liquid
		 * 
		 * 12 13 14 15 16 voiced tensed high medium low
		 * 
		 * 17 18 19 20 silent elide pause full_stop
		 * 
		 * 21 22 23 24 25 right left strong weak boundary
		 */

		// ���P�_syllable
		switch (d) {
		case '<':
			r[21] = 1;
			break;
		case '>':
			r[22] = 1;
			break;
		case '1':
			r[23] = r[24] = 1;
			break;
		case '2':
			r[23] = 1;
			break;
		case '0':
			r[21] = r[22] = r[25] = 1;
			break;
		default:
			break;
		}

		// �A�P�_phoneme
		switch (c) {
		case 'a':
			r[6] = r[13] = r[3] = 1;
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
		default:
			break;
		}
		// �إߪ�
		// String k = "";
		// for (int index = 0; index < r.length; index++) {
		// if (r[index] == 1)
		// k += String.valueOf(r[index]);
		// }
		// map.put(k, c + "\t" + d);
		return r;
	}

	private static String featureToPhoneme(double[] feature) {
		String k = "";
		for (int index = 0; index < feature.length; index++) {
			if (feature[index] > .8)
				k += String.valueOf(index);
		}
		return map.get(k);
	}

	private static double[] symbolToArray(char c) {
		// a(97) -> 0 , b(98) -> 1 , ...
		double[] r = new double[29];
		if (c >= 'a' && c <= 'z')
			r[c - 'a'] = 1;
		else if (c == ' ')
			r[26] = 1;
		else if (c == ',')
			r[27] = 1;
		else if (c == '.')
			r[28] = 1;
		return r;
	}

	private static double[][][] preprocess(String word, String phoneme, String stress) {
		// prepare the output
		double[][][] output = new double[word.length()][2][];
		// [ - - - p - - - ] , [ feature ]

		// for each char in word
		int iForOutput = 0;
		for (int i = 0; i < word.length(); i++) {
			// �p�Gword.charAt(i)���Oc�N���U�@��
//			if (word.charAt(i) != 'c')
//				continue;
			// System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
			// ���g�J����
			output[iForOutput][1] = phonemeToFeatures(phoneme.charAt(i), stress.charAt(i));
			output[iForOutput][0] = new double[203];
			// �A�g�J��J
			int nowPtr = 0;
			for (int p = i - 3; p <= i + 3; p++) {
				// �p�Gp�u�쪺�a��O���~�� -> �ť�
				if (p < 0 || p >= word.length()) {
					double[] temp = symbolToArray(' ');
					int ptr = 0;
					for (int j = 0; j < temp.length; j++)
						output[iForOutput][0][nowPtr++] = temp[j];
				} else {
					double[] temp = symbolToArray(word.charAt(p));
					int ptr = 0;
					for (int j = 0; j < temp.length; j++)
						output[iForOutput][0][nowPtr++] = temp[j];
				}
			}
			iForOutput++;
		}
		double[][][] realOutput = new double[iForOutput][2][];
		for (int i = 0; i < realOutput.length; i++)
			for (int j = 0; j < realOutput[i].length; j++) {
				realOutput[i][j] = new double[output[i][j].length];
				for (int k = 0; k < realOutput[i][j].length; k++)
					realOutput[i][j][k] = output[i][j][k];
			}
		return realOutput;
	}

	private static double[][][] preprocessForCk(String word, String phoneme, String stress) {
		// prepare the output
		double[][][] output = new double[word.length()][2][];
		// [ - - - p - - - ] , [ feature ]

		// for each char in word
		int iForOutput = 0;
		for (int i = 0; i < word.length(); i++) {
			// �p�Gword.charAt(i)���Oc�N���U�@��
			if (word.charAt(i) != 'c')
				continue;
			if (phoneme.charAt(i) != 'k')
				continue;
			// System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
			// ���g�J����
			output[iForOutput][1] = phonemeToFeatures(phoneme.charAt(i), stress.charAt(i));
			output[iForOutput][0] = new double[203];
			// �A�g�J��J
			int nowPtr = 0;
			for (int p = i - 3; p <= i + 3; p++) {
				// �p�Gp�u�쪺�a��O���~�� -> �ť�
				if (p < 0 || p >= word.length()) {
					double[] temp = symbolToArray(' ');
					int ptr = 0;
					for (int j = 0; j < temp.length; j++)
						output[iForOutput][0][nowPtr++] = temp[j];
				} else {
					double[] temp = symbolToArray(word.charAt(p));
					int ptr = 0;
					for (int j = 0; j < temp.length; j++)
						output[iForOutput][0][nowPtr++] = temp[j];
				}
			}
			iForOutput++;
		}
		double[][][] realOutput = new double[iForOutput][2][];
		for (int i = 0; i < realOutput.length; i++)
			for (int j = 0; j < realOutput[i].length; j++) {
				realOutput[i][j] = new double[output[i][j].length];
				for (int k = 0; k < realOutput[i][j].length; k++)
					realOutput[i][j][k] = output[i][j][k];
			}
		return realOutput;
	}

	private static double[][][] preprocessForCs(String word, String phoneme, String stress) {
		// prepare the output
		double[][][] output = new double[word.length()][2][];
		// [ - - - p - - - ] , [ feature ]

		// for each char in word
		int iForOutput = 0;
		for (int i = 0; i < word.length(); i++) {
			// �p�Gword.charAt(i)���Oc�N���U�@��
			if (word.charAt(i) != 'c')
				continue;
			if (phoneme.charAt(i) != 's')
				continue;
			// System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
			// ���g�J����
			output[iForOutput][1] = phonemeToFeatures(phoneme.charAt(i), stress.charAt(i));
			output[iForOutput][0] = new double[203];
			// �A�g�J��J
			int nowPtr = 0;
			for (int p = i - 3; p <= i + 3; p++) {
				// �p�Gp�u�쪺�a��O���~�� -> �ť�
				if (p < 0 || p >= word.length()) {
					double[] temp = symbolToArray(' ');
					int ptr = 0;
					for (int j = 0; j < temp.length; j++)
						output[iForOutput][0][nowPtr++] = temp[j];
				} else {
					double[] temp = symbolToArray(word.charAt(p));
					int ptr = 0;
					for (int j = 0; j < temp.length; j++)
						output[iForOutput][0][nowPtr++] = temp[j];
				}
			}
			iForOutput++;
		}
		double[][][] realOutput = new double[iForOutput][2][];
		for (int i = 0; i < realOutput.length; i++)
			for (int j = 0; j < realOutput[i].length; j++) {
				realOutput[i][j] = new double[output[i][j].length];
				for (int k = 0; k < realOutput[i][j].length; k++)
					realOutput[i][j][k] = output[i][j][k];
			}
		return realOutput;
	}

	public static void readData(int t, ArrayList<double[][]> CDataSet, ArrayList<double[][]> CkDataSet,
			ArrayList<double[][]> CsDataSet,

	BufferedReader reader) throws IOException {
		String line;
		int w = 0;
		int words = t;
		while ((line = reader.readLine()) != null && w++ <= words) {
			String item[] = line.split("\t");
			for (int i = 0; i < item.length; i += 4) {
				try {
					String word = item[i + 0];
					String pronounce = item[i + 1];
					String pitch = item[i + 2];
					String cate = item[i + 3];

					System.out.println(word);
					// for each word, we can define several inputs and
					// outputs.

					// �@����
					double[][][] pairsC = preprocess(word, pronounce, pitch);
					// ������C�o����k�����
					double[][][] pairsCk = preprocessForCk(word, pronounce, pitch);
					// ������C�o����s�����
					double[][][] pairsCs = preprocessForCs(word, pronounce, pitch);

					for (double[][] data : pairsC)
						CDataSet.add(data);
					for (double[][] data : pairsCk)
						CkDataSet.add(data);
					for (double[][] data : pairsCs)
						CsDataSet.add(data);
				} catch (Exception e) {
				}
			}
		}
	}

	public static ArrayList<double[][]> extractLetterCData(ArrayList<double[][]> input) {
		ArrayList<double[][]> r = new ArrayList<double[][]>();
		for (double[][] d : input) {
			// �Yd[0][] == letter C , �h�[�J

			// 29*3 -> ���줤���r������0��element
			if (d[0][29 * 3 - 'a' + 'c'] == 1) {
				r.add(d);
			}
		}
		return r;
	}

	public static void main(String[] args) throws IOException {

		// �V�m��r�q
		int numOfTrainingData = 1000;

		// �C�V�m�X���e�@����?
		int drawGap = 1200;
		// �V�m�X��?
		int maxTrainingTime = 300000;

		// ������
		lineChart letterCChart = new lineChart("���r��C���o��");

		// ���g����
		NN nn = new NN(new int[] { 203, 80, 26 });
		nn.setLearningRate(.1);

		// �V�m��
		ArrayList<double[][]> DataSet = new ArrayList<>();
		// �����r����'c'�B�o����'k'�����X
		ArrayList<double[][]> CkDataSet = new ArrayList<>();
		// �����r����'c'�B�o����'s'�����X
		ArrayList<double[][]> CsDataSet = new ArrayList<>();
		BufferedReader reader = new BufferedReader(new FileReader("nettalk.txt"));
		readData(numOfTrainingData, DataSet, CkDataSet, CsDataSet, reader);
		reader.close();

		System.out.println(DataSet.size());
		System.out.println(CkDataSet.size());
		System.out.println(CsDataSet.size());
		// ��data��J�u�V�m�����r����C�����g������
		for (double[][] data : DataSet)
			nn.addTrainingSample(data[0], data[1]);

		// �}�l�V�m
		int trainedTimes = 0;
		do {
			if (trainedTimes % drawGap == 0) {
				double a = nn.getOuterSetAccuracyRate(CkDataSet,0.1, 0, 20);
				double b = nn.getOuterSetAccuracyRate(CsDataSet,0.1, 0, 20);
				// double b = n.getTestingSetAccuracyRate(0.1, 0, 20);
				double c = nn.getOuterSetAccuracyRate(CkDataSet,0.1, 21, 25);
				double d = nn.getOuterSetAccuracyRate(CsDataSet,0.1, 21, 25);
				// double d = n.getTestingSetAccuracyRate(0.1, 21, 25);

				letterCChart.addData(new double[] { trainedTimes, a }, "k : phoneme");
				letterCChart.addData(new double[] { trainedTimes, b }, "s : phoneme");
				letterCChart.addData(new double[] { trainedTimes, c }, "k : syllable");
				letterCChart.addData(new double[] { trainedTimes, d }, "s : syllable");
				letterCChart.setVisible(true);
				letterCChart.redraw();
			}
			trainedTimes++;
			nn.learnFromSingleTrainingData();
		} while (trainedTimes < maxTrainingTime);

	}
}
