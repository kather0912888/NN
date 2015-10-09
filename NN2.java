
// please see http://ieeexplore.ieee.org/stamp/stamp.jsp?tp=&arnumber=298623 (RPROP algo) for the changes.

import java.text.DecimalFormat;
import java.util.ArrayList;

public class NN2 {
	int L;
	double learningRate=1;
	int[] layers;
	double[][][] weights;
	double[][] X;
	double[][] D;

	double max = 50 , min = .000001;
	static double n_plus = 1.1, n_minus=0.3;
	double[][][] preDerivatives;
	double[][][] preChanges;
	
	
	
	public ArrayList<double[]> trainingSet = new ArrayList<>();
	ArrayList<double[]> trainingAnswers = new ArrayList<>();
	ArrayList<double[]> testingSet = new ArrayList<>();
	ArrayList<double[]> testingAnswers = new ArrayList<>();

	public void addTrainingSample(double[] input, double[] answer) {
		trainingSet.add(input);
		trainingAnswers.add(answer);
	}

	public void addTestingSample(double[] input, double[] answer) {
		testingSet.add(input);
		testingAnswers.add(answer);
	}

	public void learnFromTrainingData() {
		for(int i=0;i<trainingSet.size();i++)
			this.learn(trainingSet.get(i), trainingAnswers.get(i));
	}

	int now=0;
	public void learnFromSingleTrainingData() {
		this.learn(trainingSet.get(now), trainingAnswers.get(now));
		now = (now+1)%trainingSet.size();
	}

	public double getEin() {
		double r = 0.0;
		for (int i = 0; i < trainingSet.size(); i++)
			r += error(guess(trainingSet.get(i)), trainingAnswers.get(i));
		return r;
	}

	public double getEout() {
		double r = 0.0;
		for (int i = 0; i < testingSet.size(); i++)
			r += error(guess(testingSet.get(i)), testingAnswers.get(i));
		return r;
	}

	public double error(double[] a, double[] b) {
		double r = 0;
		for (int i = 0; i < a.length; i++) {
			double x = (a[i] - b[i]);
			r += x * x;
		}
//		r/=a.length;
		return r;
	}

	public void setLearningRate(double l) {
		learningRate = l;
	}
	
	boolean changed = false;

	public NN2(int[] layers) {
		// 保存每層神經元數量
		this.layers = layers;
		// 確定層數
		L = layers.length - 1;
		// 初始化權重值
		weights = new double[layers.length][][];
		preDerivatives = new double[layers.length][][];
		preChanges     = new double[layers.length][][];
		for (int s = 0; s < layers.length - 1; s++) {
			int size = layers[s];
			int nextSize = layers[s + 1];
			double[][] d = new double[size][nextSize];
			for (int i = 0; i < size; i++)
				for (int j = 0; j < nextSize; j++)
					d[i][j] = Math.random()/nextSize;//TODO
			weights[s + 1] = d;
			preDerivatives[s + 1] = new double[size][nextSize];
			preChanges    [s + 1] = new double[size][nextSize];
			for(int i=0;i<size;i++)
				for(int j=0;j<nextSize;j++)
					preChanges[s+1][i][j] = 1;
		}
		// 倒傳遞備用變數
		X = new double[layers.length][];
		D = new double[layers.length][];
		for (int l = L; l >= 0; l--) {
			D[l] = new double[this.layers[l]];
			X[l] = new double[this.layers[l]];
		}
	}

	double sigmoid(double x) {
		return 1 / (1 + Math.pow(Math.E, -(x)));
	}

	// 每層輸出向量, 輸入為 input向量與該層的權重
	private double[] theta(double[] input, double[][] weight) {
		double[] next = new double[weight[0].length];
		for (int j = 0; j < next.length; j++)
			for (int i = 0; i < input.length; i++) {
				next[j] += input[i] * weight[i][j];
			}
		for (int i = 0; i < next.length; i++)
			next[i] = sigmoid(next[i]);// TODO small tip
		return next;
	}

	public double[] guess(double[] input) {
		for (int l = 1; l <= L; l++)
			input = theta(input, weights[l]);
		return input;
	}

	private double delta(double x, double[] w, double[] d) {
		double r = 0.0;
		for (int j = 0; j < w.length; j++)
			r += w[j] * d[j];
		// TODO 每層delta之間乘上偏導數
		// r *= (1 - Math.pow(x, 2)); <=這是tanh的偏導數 先留著
		r *= x * (1 - x);// <=sigmoid(x)的偏導數
		return r;
	}
	

	public void learn(double[] x, double[] y) {
		X[0] = x;
		for (int l = 1; l < weights.length; l++)
			X[l] = theta(X[l - 1], weights[l]);

		// 最外層誤差之Delta偏導數(每一個)
		for (int i = 0; i < y.length; i++) 
			D[L][i] = (X[L][i] - y[i]) * ((X[L][i]) * (1 - (X[L][i])));

		// 每層之Delta誤差
		for (int l = L; l >= 1; l--)
			for (int i = 0; i < X[l - 1].length; i++)
				D[l - 1][i] = delta(X[l - 1][i], weights[l][i], D[l]);

		if(changed)
		// 更新weights
		for (int l = L; l >= 1; l--)
			for (int i = 0; i < weights[l].length; i++) {
				double preX = X[l - 1][i];
				for (int j = 0; j < weights[l][i].length; j++){
//					double change = 0.9*preDerivatives[l][i][j] + 0.1 * preX * D[l][j];
//					preDerivatives[l][i][j] = 
					double nowDerivative = preX * D[l][j];
//					weights[l][i][j] -= change * learningRate;
//					weights[l][i][j] -= preX * D[l][j] * learningRate;
					
					if(preDerivatives[l][i][j] * nowDerivative > 0){ // we can go further more
						preChanges[l][i][j] = preChanges[l][i][j]*n_plus;//Math.min(preChanges[l][i][j]*n_plus, max);
						double w = -(nowDerivative)*preChanges[l][i][j];
						weights[l][i][j] += w;
//						preChanges[l][i][j] = Math.max(preChanges[l][i][j]*n_plus, nowDerivative);
//						double w = preChanges[l][i][j];
//						weights[l][i][j] -= w;
					}
					else if(preDerivatives[l][i][j] * nowDerivative < 0){ // we need to go back
						preChanges[l][i][j] = preChanges[l][i][j]*n_minus;//Math.max(preChanges[l][i][j]*n_minus, min);
//						double w = -(nowDerivative)*preChanges[l][i][j];
						weights[l][i][j] -= preChanges[l][i][j];
						preDerivatives[l][i][j] = 0;
					}
					// = 0
					else{
						double w = -(nowDerivative) * preChanges[l][i][j];
						weights[l][i][j] += w;
					}
					
				}
			}
		else{
			changed = true;
			// 更新weights
			for (int l = L; l >= 1; l--)
				for (int i = 0; i < weights[l].length; i++) {
					double preX = X[l - 1][i];
					for (int j = 0; j < weights[l][i].length; j++){
//						double change = 0.9*preChanges[l][i][j] + 0.1 * preX * D[l][j];
//						preChanges[l][i][j] = change;
//						weights[l][i][j] -= change * learningRate;
						weights[l][i][j] -= preX * D[l][j];
						preDerivatives[l][i][j] = preX * D[l][j];
					}
				}
		}
	}

	public void showTheAnswer(double[] input){
		System.out.print("對於輸入:\t\t\t");
		for(double d :input){
			System.out.print(d + "\t");
		}
		System.out.println();
		System.out.print("得到的答案(只顯示到小數後兩位):\t");
		for(double d :guess(input)){
			DecimalFormat df=new DecimalFormat("#.##");
			String s=df.format(d); 
			System.out.print(s + "\t");
		}
		System.out.println();
	}
}
