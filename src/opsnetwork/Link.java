package opsnetwork;

import java.util.Comparator;

/**
 * @author m-morita
 *
 */
public class Link implements Comparable {
	protected Network net;
	protected int id;
	protected int from;
	protected int to;
	protected int w_start;
	protected int w_width;
	protected double cost;
	protected int group;
	protected int max_w;
	protected double link_cost_alpha;
	protected double link_cost_beta;
	protected double vc;
	protected int route_count=0;
	
	protected int[] w_list;
	protected int[] w_bit;
	public Link(Network net, int id, int from, int to, int w_start, int w_width, double cost, int group){
		init_common(net,id,from,to,w_start,w_width,cost,group);
		w_list = Link.toWlist(w_start, w_width);
		w_bit = Link.toWbit(w_start, w_width, max_w);
	}
	public Link(Network net, int id, int from, int to, int[] w_bit, int w_width, double cost, int group){
		init_common(net,id,from,to,-1,w_width,cost,group);
		//create w_list
		this.w_bit = w_bit;
		w_list = new int[w_width];
		int cur=0;
		for(int i=0;i<max_w;i++){
			if(w_bit[i]==1){
				w_list[cur++]=i;
			}
		}
		
		//for Debug
		if(cur!=w_width){
			throw new RuntimeException("Logical Error:"+"cur:"+cur+",w_width:"+w_width);
		}
		
	}
	private void init_common(Network net, int id, int from, int to, int w_start, int w_width, double cost, int group){
		this.net=net;
		this.id=id;
		this.from=from;
		this.to=to;
		this.w_start=w_start;
		this.w_width=w_width;
		this.cost=cost;
		this.group=group;
		max_w = net.getNwset().max_w;
		link_cost_alpha = (group==0)? 0 : net.getNwset().link_cost_alpha;
		link_cost_beta = net.getNwset().link_cost_beta;
	}
	public int getId(){
		return id;
	}
	public int getFrom(){
		return from;
	}
	
	public int getTo(){
		return to;
	}
	
	public int getW_start(){
		return w_start;
	}
	public int getW_width(){
		return w_width;
	}
	
	public void addTrivialCost(){
		route_count++;
	}
	public double getVirtualCost(){
		//vc= cost+link_cost_alpha + link_cost_beta *((double)max_w / w_width);
		//vc= cost+ link_cost_beta *((double)max_w / w_width);
		//vc= cost+ link_cost_beta *((double)max_w / w_width)+0.001*route_count;
		vc= 1000 +cost+ link_cost_beta *((double)max_w / w_width)+0.001*route_count;
		
		return vc;
	}
	public double getCost(){
		return cost;
	}
	public int getGroup(){
		return group;
	}
	
	public int compareTo(Object o){
		double diff = this.getVirtualCost() - ((Link)o).getVirtualCost();
		if(diff > 0) return 1;
		else if(diff < 0) return -1;
		else return 0;
	}
	public String toString() {
		// TODO 自動生成されたメソッド・スタブ
		return "[Node:" + from + "-> Link:"+ id + ", w:" +w_width+"@"+ w_start + "-" + (w_start+w_width) + " -> Node:" + to +"]";
	}
	
	public static int[] toWlist(int w_start, int w_width){
		int[] list = new int[w_width];
		for(int i=0; i<w_width; i++){
			list[i]=w_start+i;
		}
		return list;
	}
	public static int[] toWbit(int w_start, int w_width, int max_w){
		int []bit = new int[max_w];
		for(int i=0; i< max_w; i++){
			bit[i]=0;
		}
		for(int i=0; i<w_width; i++){
			bit[i+w_start]=1;
		}
		return bit;
	}
	public int[] getW_bit() {
		return w_bit;
	}

	public int[] getW_list() {
		return w_list;
	}
	
	public static class CompByWidth implements Comparator{

		public int compare(Object o1, Object o2) {
			// TODO 自動生成されたメソッド・スタブ
			return ((Link)o2).w_width - ((Link)o1).w_width;
		}
		
	
	}
	
}
