
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class NN {
	int L;
	double learningRate=1;
	int[] layers;
	double[][][] preChanges=null;
	double[][][] weights;
	double[][] X;
	double[][] D;
	public ArrayList<double[]> trainingSet = new ArrayList<>();
	ArrayList<double[]> trainingAnswers = new ArrayList<>();
	
	HashMap<String,ArrayList<double[]>> answerToDataSet = new HashMap<>();
	
	ArrayList<double[]> testingSet = new ArrayList<>();
	ArrayList<double[]> testingAnswers = new ArrayList<>();
	
	HashMap<String,Boolean> damagedTable = new HashMap<>();
	
	public double[] getHiddenLayerActLevelToInput(int l,double[] input){
		if(l<=0 || l>=layers.length-1)
			return null;
		for (int ll = 1; ll <= l; ll++)
			input = theta(input, weights[ll]);
		return input;
	}
	
	public int getHiddenLayerSize(int l){
		if(l==0 || l>=layers.length-1)
			return 0;
		return layers[l];
	}
	
 	private int numOfWeights(){
		int r=0;
		for(double[][] ws : weights){
			try{
			for(double[] wss : ws)
				r+= wss.length;
			}
			catch(Exception e){}
		}
		return r;//weights.length * weights[0].length * weights[0][0].length;
	}
	
	private double nowDamagedPercentage(){
		return Double.valueOf(damagedTable.keySet().size())/Double.valueOf(numOfWeights());
	}
	
	public void damage(double goalPercentage){
		System.out.println(numOfWeights());
		// 被選到的機率
		double chance = 1.0/((goalPercentage)*numOfWeights());
		System.out.println(chance);
		while(nowDamagedPercentage() < goalPercentage){
			// randomly choose a weight to change.
			for(int i=1;i<weights.length;i++)
				for(int j=0;j<weights[i].length;j++)
					for(int k=0;k<weights[i][j].length;k++){
						if(Math.random()<=chance){
							String target = String.valueOf(i)+""+String.valueOf(j)+""+String.valueOf(k);
							if(damagedTable.get(target)==null){
								damagedTable.put(target, true);
								weights[i][j][k] = Math.random();
								if(nowDamagedPercentage() >= goalPercentage)
									return;
							}
						}
					}
		}
	}
	
	public void outputWeights(String filePath) throws IOException{
		System.out.println("權重輸出中, 請稍候");
		BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
		String r = "";
		for(int i=1;i<weights.length;i++)
			for(int j=0;j<weights[i].length;j++)
				for(int k=0;k<weights[i][j].length;k++)
					r+=weights[i][j][k]+"\t";
		writer.write(r);
		writer.close();
		System.out.println("輸出完成");
//		return r;
	}
	
	public void loadWeights(String filePath) throws IOException{
		BufferedReader reader = new BufferedReader(new FileReader(filePath));
		String line=reader.readLine();
		String[] p = line.split("\t");
		int ptr=0;
		for(int i=1;i<weights.length;i++)
			for(int j=0;j<weights[i].length;j++)
				for(int k=0;k<weights[i][j].length;k++){
					System.out.println(k);
					weights[i][j][k] = 
					Double.valueOf(p[ptr++]);
				}
		reader.close();
	}

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

	public double getTrainingSetAccuracyRate(double gap,int from,int to){
		double r = 0;
		double s = 0;
		for (int i = 0; i < trainingSet.size(); i++){
			double[] g = guess(trainingSet.get(i));
			double[] a = trainingAnswers.get(i);
			for(int j=from;j<=to;j++){
				s++;
				double d = g[j] - a[j];
				if(d>=gap || d<=-gap){
					r+=1;
//					break;
				}
			}
		}
//		return (trainingSet.size()-r)/trainingSet.size();
		return (s-r)/s;
	}
	
	public double getOuterSetAccuracyRate(ArrayList<double[][]>set,double gap,int from,int to){
		double r = 0;
		double s = 0;
		for (int i = 0; i < set.size(); i++){
			double[] g = guess(set.get(i)[0]);
			double[] a = set.get(i)[1];
			for(int j=from;j<=to;j++){
				s++;
				double d = g[j] - a[j];
				if(d>=gap || d<=-gap){
					r+=1;
//					break;
				}
			}
		}
//		return (trainingSet.size()-r)/trainingSet.size();
		return (s-r)/s;
	}
	
	public double getTestingSetAccuracyRate(double gap,int from,int to){
		double r = 0;
		double s = 0;
		for (int i = 0; i < testingSet.size(); i++){
			double[] g = guess(testingSet.get(i));
			double[] a = testingAnswers.get(i);
			for(int j=from;j<=to;j++){
				s++;
				double d = g[j] - a[j];
				if(d>=gap || d<=-gap){
					r+=1;
					break;
				}
			}
		}
//		return (testingSet.size()-r)/testingSet.size();
		return (s-r)/s;
	}

	public double getTrainingSetAccuracyRate(double gap){
		double r = 0;
		for (int i = 0; i < trainingSet.size(); i++){
			double[] g = guess(trainingSet.get(i));
			double[] a = trainingAnswers.get(i);
			for(int j=0;j<g.length;j++){
				double d = g[j] - a[j];
				if(d>=gap || d<=-gap){
					r+=1;
					break;
				}
			}
		}
		return (trainingSet.size()-r)/trainingSet.size();
	}
	
	public double getTestingSetAccuracyRate(double gap){
		double r = 0;
		for (int i = 0; i < testingSet.size(); i++){
			double[] g = guess(testingSet.get(i));
			double[] a = testingAnswers.get(i);
			for(int j=0;j<g.length;j++){
				double d = g[j] - a[j];
				if(d>=gap || d<=-gap){
					r+=1;
					break;
				}
			}
		}
		return (testingSet.size()-r)/testingSet.size();
	}
	
	public double error(double[] a, double[] b) {
		double r = 0;
		for (int i = 0; i < a.length; i++) {
			double x = (a[i] - b[i]);
			// TODO 
			if(x*x>0.25)
			r += x * x;
		}
//		r/=a.length;
		return r;
	}

	public void setLearningRate(double l) {
		learningRate = l;
	}

	public NN(int[] layers) {
		// 保存每層神經元數量
		this.layers = layers;
		// 確定層數
		L = layers.length - 1;
		// 初始化權重值
		weights = new double[layers.length][][];
		preChanges = new double[layers.length][][];
		for (int s = 0; s < layers.length - 1; s++) {
			int size = layers[s];
			int nextSize = layers[s + 1];
			double[][] d = new double[size][nextSize];
			for (int i = 0; i < size; i++)
				for (int j = 0; j < nextSize; j++)
					d[i][j] = Math.random()/nextSize;//TODO
			weights[s + 1] = d;
			preChanges[s + 1] = new double[size][nextSize];
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

		// 更新weights
		for (int l = L; l >= 1; l--)
			for (int i = 0; i < weights[l].length; i++) {
				double preX = X[l - 1][i];
				for (int j = 0; j < weights[l][i].length; j++){
//					double change = 0.9*preChanges[l][i][j] + 0.1 * preX * D[l][j];
//					preChanges[l][i][j] = change;
//					weights[l][i][j] -= change * learningRate;
					weights[l][i][j] -= preX * D[l][j] * learningRate;
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
