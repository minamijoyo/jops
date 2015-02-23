package opsnetwork;

import java.util.ArrayList;

public class RoutingInformation {
	public final int maximum_hop = 16;
	private int id;
	private int[] outport = new int[maximum_hop];
	private int[] w_start = new int[maximum_hop];
	private int[] w_width = new int[maximum_hop];
	private Link[] ln_list = new Link[maximum_hop];
	private int[][] w_list = new int[maximum_hop][];
	private int[][] w_bit = new int[maximum_hop][];
	private int n_hop = 0;
	private double vc=0;
	private int min_width=Integer.MAX_VALUE;
	private int weight;
	private ArrayList ln_al = new ArrayList();

	public RoutingInformation(){
		//Note that 0 is valid. Fill invalid value to array
		for(int i=0; i<maximum_hop; i++){
			outport[i]= -1;
			w_start[i]= -1;
			w_width[i]= -1;
		}
	}
	public String toString(){
		return "id:"+id+"\tvc:"+vc+"\tmin_w:"+min_width+"\tweight:"+weight+"\tLink:"+ln_al;
	}
	public void add(int outport, int w_start, int w_width, Link ln){
		if(n_hop>=maximum_hop) throw new RuntimeException("Routing Information overs maximum hop");
		this.outport[n_hop]=outport;
		this.w_start[n_hop]=w_start;
		this.w_width[n_hop]=w_width;
		this.w_list[n_hop]=ln.getW_list();
		this.w_bit[n_hop]=ln.getW_bit();
		this.ln_list[n_hop]=ln;
		this.ln_al.add(ln);
		vc+=ln.getVirtualCost();
		if(min_width>ln.w_width){
			min_width=ln.w_width;
		}
		n_hop++;
	}
	
	public int getId(){
		return id;
	}
	
	public void setId(int id){
		this.id=id;
	}
	
	public int getOutport(int hop){
		return outport[hop];
	}
	/*
	public int getW_start(int hop){
		return w_start[hop];
	}
	*/
	public int getW_width(int hop){
		return w_width[hop];
	}
	public int[] getW_list(int hop){
		return w_list[hop];
	}
	public int[] getW_bit(int hop){
		return w_bit[hop];
	}
	
	public double getVc(){
		return vc;
	}
	
	public int getMin_width(){
		return min_width;
	}
	
	public void setWeight(int weight){
		this.weight=weight;
	}
	
	public int getWeight(){
		return weight;
	}
	public int getN_hop() {
		return n_hop;
	}
	public Link[] getLn_list() {
		return ln_list;
	}
}
