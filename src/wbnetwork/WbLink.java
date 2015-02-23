package wbnetwork;

import java.util.Comparator;

public class WbLink {
	protected WbNetwork net;
	protected int id;
	protected int from;
	protected int to;
	protected double cost;
	protected int max_w;
	protected int grid;
	protected int[] w_used;
	public WbLink(WbNetwork net, int id, int from, int to, double cost, int max_w, int grid) {
		this.net = net;
		this.id = id;
		this.from = from;
		this.to = to;
		this.cost = cost;
		this.max_w = max_w;
		this.grid = grid;
		w_used = new int[max_w];
		for(int i=0; i< max_w; i++){
			w_used[i]= -1;
		}
		
	}
	
	public String toString() {
		// TODO 自動生成されたメソッド・スタブ
		return "[Node:" + from + "-> Link:"+ id + " -> Node:" + to +"]";
	}
	
	public boolean is_available_wb(int start, int width){
		for(int j=0; j<width; j++){
			if(start+j>=this.max_w) return false;
			if(this.w_used[start+j]!=-1) return false;
		}
		return true;
	}
	public int find_free_wb(int width){
		int found=-1;
		for(int i=0; i< max_w; i+=grid){
			if((is_available_wb(i,width))){
				found=i;
				break;
			}
		}
		return found;
	}
	
	public int count_free_wb(){
		int count=0;
		for(int i=0; i<max_w;i+=grid){
			if(is_available_wb(i,grid)){
				count++;
			}
		}
		return count;
	}
	
	public double getVirtualCost(){
		int fw=count_free_wb();
		return (fw==0)? Double.POSITIVE_INFINITY : cost/fw;
	}

	public double getUtilWithVlink(){
		int sum=0;
		for(int i=0; i<max_w; i++){
			if(w_used[i]!=-1){
				sum++;
			}
		}
		return (double)sum/max_w;
	}
	
	public double getUtilWithoutVlink(){
		int sum=0;
		for(int i=0; i<max_w; i++){
			if(w_used[i]>=0){
				sum++;
			}
		}
		return (double)sum/max_w;
	}
		
	public static class CompByUtil implements Comparator{

		public int compare(Object o1, Object o2) {
			// TODO 自動生成されたメソッド・スタブ
			double dif=((WbLink)o2).getUtilWithoutVlink() - ((WbLink)o1).getUtilWithoutVlink();
			if(dif<0) return -1;
			else if(dif>0) return 1;
			else return 0;
		}
		
	
	}
}
