package wbnetwork;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class VlinkAssignImplAltState implements Cloneable {
	private ArrayList path=new ArrayList();
	private boolean[] nodes;
	private int[] w_bit;
	private int cur_node;
	private int max_w;
	private int n_nodes;
	
	//static member
	public static int best_score;
	
	//cached value
	private int w_width;
	private int consecutive_max_width;
	private int consecutive_max_start;
	
	//for new Object
	public VlinkAssignImplAltState(int cur_node, int max_w, int n_nodes){
		this.cur_node=cur_node;
		this.max_w=max_w;
		this.n_nodes=n_nodes;
	
		w_bit = new int[max_w];
		Arrays.fill(w_bit,1);
		nodes = new boolean[n_nodes];
		nodes[cur_node]=true;
		
		w_width=max_w;
		consecutive_max_width=max_w;
		consecutive_max_start=0;
	}
	
	public static void static_init(){
		best_score=0;
	}
	
	//for clone()
	private VlinkAssignImplAltState(){
		
	}

	
	protected Object clone(){
		VlinkAssignImplAltState ret = new VlinkAssignImplAltState();
		ret.path=(ArrayList)this.path.clone();
		ret.nodes=(boolean[])this.nodes.clone();
		ret.w_bit=(int[])this.w_bit.clone();
		ret.cur_node=this.cur_node;
		ret.max_w=this.max_w;
		ret.n_nodes=this.n_nodes;
		ret.w_width=this.w_width;
		ret.consecutive_max_width=this.w_width;
		ret.consecutive_max_start=this.consecutive_max_start;
		return ret;
		
	}
	
	public boolean containNode(int n){
		return nodes[n];
	}
	
	public void gotoNextNode(WbLink ln){
		path.add(ln);
		nodes[ln.to]=true;
		cur_node=ln.to;
	}
	
	public void updateMask(WbLink ln){
		for(int i=0; i<w_bit.length; i++){
			if(w_bit[i]==1 && ln.w_used[i]!=-1){
				w_bit[i]=0;
			}
		}
		updateW_width();
		updateConsecutiveMaxWidth();
	}
	
	public void setW_bit(int pos, int val){
		w_bit[pos]=val;
		updateW_width();
		updateConsecutiveMaxWidth();
	}
	
	public void setW_bit(int[] w_bit){
		this.w_bit=w_bit;
		updateW_width();
		updateConsecutiveMaxWidth();
	}
	
	//get clone for read, original data cannot be modified from other. 
	//if you need modification, use setW_bit() method.
	public int[] getCloneW_bit(){
		return (int[])w_bit.clone();
	}
	
	private void updateW_width(){
		int sum=0;
		for(int i=0; i<w_bit.length; i++){
			sum+=w_bit[i];
		}
		this.w_width=sum;
	}
	
	private void updateConsecutiveMaxWidth(){
		int max=0;
		int start=0;
		int width=0;
		for(int i=0; i<w_bit.length; i++){
			if(w_bit[i]==1){
				width++;
			}else if(width!=0){
				if(max<width){
					max=width;
					start= i-max;
				}
				width=0;
			}
		}
		if(width!=0){
			if(max<width){
				max=width;
				start= w_bit.length-max;
			}
		}
		this.consecutive_max_width=max;
		this.consecutive_max_start=start;
	}
	
	public int getScore(){
		return getW_width();
	}
	
	public int getW_width(){
		return w_width;
	}
	
	
	public int getConsecutiveMaxWidth(){
		return consecutive_max_width;
	}
	
	public int getConsecutiveMaxStart(){
		return consecutive_max_start;
	}
	
	public double getCost(){
		double sum=0;
		for(int i=0; i<path.size(); i++){
			sum+= ((WbLink)path.get(i)).cost;
		}
		return sum;
	}

	public int getCur_node() {
		return cur_node;
	}

	public void setCur_node(int cur_node) {
		this.cur_node = cur_node;
	}
	
	public int getDepth(){
		return path.size();
	}
	
	public ArrayList getPath(){
		return path;
	}
	
	public static class CompByScore implements Comparator{

		public int compare(Object o1, Object o2) {
			// TODO 自動生成されたメソッド・スタブ
			int dif= ((VlinkAssignImplAltState)o2).getScore() - ((VlinkAssignImplAltState)o1).getScore();
			if(dif!=0){
				return dif;
			}else{
				double dc =((VlinkAssignImplAltState)o2).getDepth() - ((VlinkAssignImplAltState)o1).getDepth();
				if(dc>0){
					return -1;
				}else if(dc<0){
					return 1;
				}else{
					return 0;
				}
			}
		}
		
	
	}
	/*
	public static class CompByWidth implements Comparator{

		public int compare(Object o1, Object o2) {
			// TODO 自動生成されたメソッド・スタブ
			int dif= ((VlinkAssignImplAltState)o2).getW_width() - ((VlinkAssignImplAltState)o1).getW_width();
			if(dif!=0){
				return dif;
			}else{
				double dc =((VlinkAssignImplAltState)o2).getDepth() - ((VlinkAssignImplAltState)o1).getDepth();
				if(dc>0){
					return -1;
				}else if(dc<0){
					return 1;
				}else{
					return 0;
				}
			}
		}
		
	
	}
	public static class CompByConsecutiveWidth implements Comparator{

		public int compare(Object o1, Object o2) {
			// TODO 自動生成されたメソッド・スタブ
			int dif= ((VlinkAssignImplAltState)o2).getConsecutiveMaxWidth() - ((VlinkAssignImplAltState)o1).getConsecutiveMaxWidth();
			if(dif!=0){
				return dif;
			}else{
				double dc =((VlinkAssignImplAltState)o2).getDepth() - ((VlinkAssignImplAltState)o1).getDepth();
				if(dc>0){
					return -1;
				}else if(dc<0){
					return 1;
				}else{
					return 0;
				}
			}
		}
		
	
	}
	*/
}
