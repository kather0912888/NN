import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.TreeSet;

public class HAC {

	// method
	public static final int singleLinkage = 0;
	public static final int completeLinkage = 1;
	public static final int averageLinkage = 2;
	private static int method;

	public HAC(int method) {
		this.method = method;
	}

	//
	public static interface item {
		int tag();
		public String getName();

		public double distance(Object i);
	}

	private ArrayList<item> itemList = new ArrayList<>();
	private static double[][] distanceTable;

	public void addItems(item i) {
		itemList.add(i);
	}

	private void setDistanceTable() {
		distanceTable = new double[itemList.size()][itemList.size()];
		for (int i = 0; i < itemList.size(); i++)
			for (int j = 0; j < itemList.size(); j++)
				distanceTable[itemList.get(i).tag()][itemList.get(j).tag()] = itemList.get(i).distance(itemList.get(j));

	}

	private static class clustersDis implements Comparable<clustersDis> {
		private clustersDis(cluster a, cluster b) {
			x = a;
			y = b;
		}

		cluster x, y;

		double dis() {
			// System.out.println(x.distance(y));
			return x.distance(y);
		}

		// @Override
		// public int compare(clustersDis o1, clustersDis o2) {
		// if (o1.dis() < o2.dis())
		// return -1;
		// if (o1.dis() > o2.dis())
		// return 1;
		// return 0;
		// }

		@Override
		public int compareTo(clustersDis o2) {
			// TODO Auto-generated method stub
			if (dis() < o2.dis())
				return -1;
			else if (dis() > o2.dis())
				return 1;
			else
				return 0;
		}
	}

	public cluster runClustering() {
		setDistanceTable();
		// 目前層
		cluster[] first = new cluster[itemList.size()];
		for (int i = 0; i < itemList.size(); i++) {
			first[i] = new cluster();
			first[i].items.add(itemList.get(i));
			first[i].name = itemList.get(i).getName();
		}

		// while (first.length > 1) {
//		System.out.println(first.length);
//		for (int i = 0; i < first.length; i++)
//			System.out.println("\t" + first[i].name);
		// 取上高斯 - 合併層
		cluster[] second = new cluster[(first.length + 1) / 2];

		// 前處理 - 把所有cluster扔進儲存結構內
		ArrayList<cluster> temp = new ArrayList<>();
		for (int i = 0; i < first.length; i++)
			temp.add(first[i]);

		// 前處理 - 對距離排序(minheap)
		PriorityQueue<clustersDis> q = new PriorityQueue<>();
		// 抓距離 扔進q內
		for (int i = 0; i < first.length; i++)
			for (int j = 0; j < first.length; j++)
				if (i != j)
					q.add(new clustersDis(first[i], first[j]));
		// 開始merge
		while (q.size() > 0) {
			clustersDis dis = q.poll();
			if (dis.x.mother != null || dis.y.mother != null) {
//				System.out.println("!");
				continue;
			}
			// 把dis內的兩個elements做一個mother, 並指向mother, mother指向兩個children,
			// 把mother存入second[]
			cluster m = new cluster();
			for (item i : dis.x.items)
				m.items.add(i);
			for (item i : dis.y.items)
				m.items.add(i);
			m.dis = dis.dis();
			m.name = dis.x.name + "|" + dis.y.name;

			m.children[0] = dis.x;
			m.children[1] = dis.y;
			dis.x.mother = m;
			dis.y.mother = m;
//			System.out.println("\t merged");
			// temp除掉x,y
			temp.remove(dis.x);
			temp.remove(dis.y);
			// TODO 把新的cluster(m)與其他的(除了dis.x , dis.y)cluster的距離算完後扔進q內
			for (int j = 0; j < temp.size(); j++)
				q.add(new clustersDis(m, temp.get(j)));
			temp.add(m);

		}
//		System.out.println("TEMP" + temp.size());
		return temp.get(0);
	}

	public cluster runClusteringOld() {
		setDistanceTable();
		// 目前層
		cluster[] first = new cluster[itemList.size()];
		for (int i = 0; i < itemList.size(); i++) {
			first[i] = new cluster();
			first[i].items.add(itemList.get(i));
			first[i].name = itemList.get(i).getName();
		}

		while (first.length > 1) {
			System.out.println(first.length);
			for (int i = 0; i < first.length; i++)
				System.out.println("\t" + first[i].name);
			// 取上高斯 - 合併層
			cluster[] second = new cluster[(first.length + 1) / 2];

			// 前處理 - 把所有cluster扔進儲存結構內
			// ArrayList<cluster> temp = new ArrayList<>();

			// 前處理 - 對距離排序(minheap)
			PriorityQueue<clustersDis> q = new PriorityQueue<>();
			// 抓距離 扔進q內
			for (int i = 0; i < first.length; i++)
				for (int j = 0; j < first.length; j++)
					if (i != j)
						q.add(new clustersDis(first[i], first[j]));
			int ptr = 0;
			// 開始merge
			while (q.size() > 0) {
				clustersDis dis = q.poll();
				if (dis.x.mother != null || dis.y.mother != null) {
					System.out.println("!");
					continue;
				}
				// 把dis內的兩個elements做一個mother, 並指向mother, mother指向兩個children,
				// 把mother存入second[]
				cluster m = new cluster();
				for (item i : dis.x.items)
					m.items.add(i);
				for (item i : dis.y.items)
					m.items.add(i);
				m.dis = dis.dis();
				m.name = dis.x.name + "|" + dis.y.name;

				m.children[0] = dis.x;
				m.children[1] = dis.y;
				dis.x.mother = m;
				dis.y.mother = m;
				second[ptr++] = m;
				System.out.println("\t merged");
				// TODO 把新的cluster(m)與其他的(除了dis.x , dis.y)cluster的距離算完後扔進q內

			}
			// if there is something lost, add into second[]
			for (cluster c : first)
				if (c.mother == null)
					second[second.length - 1] = c;
			first = second;
			// System.out.println();
			// for(int i=0;i<first.length;i++)
			// System.out.println("\t"+first[i]);
		}
		System.out.println(first.length);
		System.out.println(first[0]);
		return first[0];
	}

	public static class cluster {
		String name;
		double dis = 0;
		cluster mother = null;
		cluster[] children = new cluster[2];
		// a bunch of items
		ArrayList<item> items = new ArrayList<>();

		public void showHierachy(int tab) {
			for (int i = 0; i < tab; i++)
				System.out.print("\t");
			System.out.print(name);
			if (children[0] != null)
				System.out.print(" --- " + dis);
			System.out.println();
			if(children[0] !=null && children[1]!=null){
				// 差距大的先印
				if(children[0].dis > children[1].dis){
					children[0].showHierachy(tab + 1);
					children[1].showHierachy(tab + 1);
				}
				else{
					children[1].showHierachy(tab + 1);
					children[0].showHierachy(tab + 1);
				}
			}

//			if (children[0] != null)
//				children[0].showHierachy(tab + 1);
//			if (children[1] != null)
//				children[1].showHierachy(tab + 1);
		}

		// distance according to the method we use.
		public double distance(cluster c2) {
			switch (method) {
			case singleLinkage:
				// find the closest 2 points
				double min = Double.MAX_VALUE;
				for (int i = 0; i < items.size(); i++)
					for (int j = 0; j < c2.items.size(); j++) {
						item a = items.get(i);
						item b = c2.items.get(j);
						if (a != b && min >= distanceTable[a.tag()][b.tag()])
							min = a.distance(b);
					}
				//
				// for (double ds[] : distanceTable)
				// for (double d : ds)
				// if (min >= d)
				// min = d;
				return min;
			case completeLinkage:
				double max = -1;
				// for(int i=0;i<distanceTable.length;i++)
				// for(int j=0;j<distanceTable[i].length;j++)
				// if(i!=j && max<=distanceTable[i][j])
				// max=distanceTable[i][j];
				for (int i = 0; i < items.size(); i++)
					for (int j = 0; j < c2.items.size(); j++) {
						item a = items.get(i);
						item b = c2.items.get(j);
						if (a != b && max <= distanceTable[a.tag()][b.tag()]/*a.distance(b)*/)
							max = a.distance(b);
					}
				return max;
			}
			return 0;
		}
	}
}
