
public class testHAC {
	
	private static class vector implements HAC.item{
		
		String name;
		int val;
		
		public vector(String n,int v){
			name = n;
			val = v;
		}

		@Override
		public String getName() {
			// TODO Auto-generated method stub
			return name;
		}

		@Override
		public double distance(Object i) {
			// TODO Auto-generated method stub
			return Math.abs(val - ((vector)i).val);
		}
		
	}
	
	public static void main(String[] args) {
		HAC hac = new HAC(HAC.completeLinkage);
		for(int i=1;i<=10;i++)
			hac.addItems(new vector(String.valueOf(i),i));
		for(int i=25;i<=26;i++)
			hac.addItems(new vector(String.valueOf(i),i));
		for(int i=99;i<=100;i++)
			hac.addItems(new vector(String.valueOf(i),i));
		
		hac.runClustering().showHierachy(0);
	}
}
