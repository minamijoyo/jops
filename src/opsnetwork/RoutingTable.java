package opsnetwork;

import java.util.ArrayList;
import java.util.Arrays;

import simuframe.WeightedRoundRobin;

public class RoutingTable {
	private RoutingInformation[][][] table;
	private WeightedRoundRobin round_robin[][];
	private int rr_max[][];
	private int nth_shortestpath;
	public static final int wrr_max=1024;
	private int n_nodes;
	private ArrayList ri_list = new ArrayList();
	
	
	public RoutingTable(Network net){
		this.n_nodes = net.nodes.length;
		this.nth_shortestpath = net.getNwset().nth_shortestpath;
		table = new RoutingInformation[n_nodes][n_nodes][nth_shortestpath];
		round_robin = new WeightedRoundRobin[n_nodes][n_nodes];
		rr_max = new int[n_nodes][n_nodes];
		
		for(int i=0; i<n_nodes; i++){
			for(int j=0; j<n_nodes; j++){
				rr_max[i][j]=0;
				for(int k=0; k< nth_shortestpath; k++)
				table[i][j][k] = null;
			}
		}
	}

	public RoutingInformation getRoutingInformation(int src, int dst){
		int r= round_robin[src][dst].getNext();
		return table[src][dst][r];
	}
	
	public RoutingInformation getRoutingInformation(int src, int dst, int rr){
		return table[src][dst][rr];
	}
	
	public void setRoutingInformation(int src, int dst, int rr, RoutingInformation ri){
		table[src][dst][rr]=ri;
		rr_max[src][dst]++;
		addRi_list(ri);
	}
	
	public int getRr_max(int src, int dst){
		return rr_max[src][dst];
	}
	
	public boolean isEmptyEntry(int src, int dst){
		return table[src][dst][0]==null;
	}
	
	
	private void addRi_list(RoutingInformation ri){
		ri.setId(ri_list.size());
		ri_list.add(ri);
	}
	
	public ArrayList getRi_list(){
		return ri_list;
	}
	public int getNentry(){
		return ri_list.size();
	}
	
	
	public void setupWrr(int rr_mode){
		for(int i=0; i<n_nodes; i++){
			for(int j=0; j<n_nodes; j++){
				if(rr_max[i][j]==0){
					continue;//no route
				}
				//weight vector
				int[] w = new int[rr_max[i][j]];
				if(rr_mode==0){
					Arrays.fill(w,1);//uniform
				}else if(rr_mode==1){//weight = (1-vc/max_vc)*15+1;
					double max_vc=0;
					double[] tmp = new double[w.length];
					for(int k=0; k<tmp.length; k++){
						tmp[k]=(int)table[i][j][k].getVc();
						if(tmp[k]>max_vc){
							max_vc=tmp[k];
						}
					}
					for(int k=0; k<w.length; k++){
						//w[k] = (int)((1-tmp[k]/max_vc)*8);
						//w[k] = (int)(1000 / tmp[k]);
						//w[k]=(int)((100-tmp[k])/10);
						w[k]=table[i][j][k].getMin_width();
						if(w[k]<=0){
							w[k]=1;
						}
					
					}
				}else{
					throw new RuntimeException("Unknown Round Robin mode:"+rr_mode);
				}
			
				round_robin[i][j]= new WeightedRoundRobin(w,wrr_max);
				//for statistics
				for(int k=0; k<w.length; k++){
					table[i][j][k].setWeight(w[k]);
				}
			}
		}
	}
	

}
