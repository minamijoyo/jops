package wbnetwork;

import java.util.Comparator;

public class TrafficDemand implements Comparable{
	protected int from;
	protected int to;
	protected int demand;
	public TrafficDemand(int from, int to, int demand) {
		this.from = from;
		this.to = to;
		this.demand = demand;
	}
	
	//compare by demand
	public int compareTo(Object o) {
		// TODO 自動生成されたメソッド・スタブ
		int d =((TrafficDemand)o).demand;
		if(demand < d) return -1;
		else if(demand > d) return 1;
		else return 0;
	}
	
	public static class CompByDemand implements Comparator{

		public int compare(Object o1, Object o2) {
			// TODO 自動生成されたメソッド・スタブ
			return ((TrafficDemand)o2).demand - ((TrafficDemand)o1).demand;
		}
		
	
	}
	
}
