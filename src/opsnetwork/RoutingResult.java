package opsnetwork;

import java.util.ArrayList;

import simuframe.SimuResult;

public class RoutingResult extends SimuResult {
	private final int max_hop=16;
	
	Network net;
	private int[] hops = new int[max_hop];
	private int[] w_widths;
	private int[] vonly_widths;
	private int max_w;
	private ArrayList paths;
	private int n_paths=0;
	private int n_acc_links=0;
	private int n_acc_vlinks=0;
	private int[] link_mult;
	public RoutingResult(Network net) {
		// TODO 自動生成されたコンストラクター・スタブ
		SimuResult.addResultList(this);
		
		this.net = net;
		this.max_w = net.max_w;
		w_widths = new int[max_w];
		vonly_widths = new int[max_w];
		link_mult = new int[net.links.length];
		paths = new ArrayList();
	}
	
	public void printResult() {
		// TODO 自動生成されたメソッド・スタブ
		
		//Path Information
		for(int i=0; i< paths.size(); i++){
			System.out.println(""+i+":"+paths.get(i));
		}
		System.out.println("n_path:"+n_paths);
		
		//Hop Information
		int sum_hop=0;
		System.out.println("hop_info:dist_hop:");
		for(int i=0; i< max_hop; i++){
			sum_hop+=hops[i]*i;
			System.out.println("hop_info:" + hops[i]);
		}
		System.out.println("hop_info:sum_hop:"+sum_hop);
		System.out.println("hop_info:n_path:"+n_paths);
		System.out.println("hop_info:ave_hop:"+(sum_hop/(double)n_paths));
		
		//Wavelength Information
		int sum_w_width=0;
		int sum_vonly_width=0;
		System.out.println("dist_w_width:");
		for(int i=0; i< max_w; i++){
			sum_w_width+=w_widths[i]*i;
			sum_vonly_width+=vonly_widths[i]*i;
			System.out.println(i+":" + w_widths[i]);
		}
		System.out.println("sum_w_width:"+sum_w_width);
		System.out.println("ave_w_width:"+(sum_w_width/(double)n_acc_links));
		System.out.println("sum_vonly_width:"+sum_vonly_width);
		System.out.println("ave_vonly_width:"+(sum_vonly_width/(double)n_acc_vlinks));
		
		//Link Multiplicity Information
		int unused_link=0;
		for(int i=0; i<link_mult.length; i++){
			Link ln = net.links[i];
			System.out.println("Link_id:"+ln.getId()+ "\tmult:"+link_mult[i] + "\tw:"+ln.getW_width()+"\tvc:"+ln.getVirtualCost() );
			if(link_mult[i]==0){
				unused_link++;
			}
		}
		System.out.println("unused_link:"+unused_link);
		System.out.println("link_used_ratio:"+(1-unused_link/(double)link_mult.length));
		
		printRrHistgram();
	}
	
	private void printRrHistgram(){
		RoutingTable rtable = net.getRtable();
		int sum_rr=0;
		int count=0;
		int histgram[]= new int[net.getNwset().nth_shortestpath+1];
		int n_nodes=net.getN_nodes();
		for(int i=0; i<n_nodes; i++){
			for(int j=0; j<n_nodes; j++){
				if(!rtable.isEmptyEntry(i, j)){
					int rr=rtable.getRr_max(i, j);
					histgram[rr]++;
					sum_rr+=rr;
					count++;
				}
			}
		}
		
		for(int i=0; i<histgram.length; i++){
			System.out.println(""+i+": "+histgram[i]);
		}
		System.out.println("ave_rr:"+(double)sum_rr/count);
	}
	
	public void addPath(ArrayList path){
		paths.add(path);
		n_paths++;
		hops[path.size()]++;
		for(int i=0; i< path.size(); i++){
			Link ln = (Link) path.get(i);
			n_acc_links++;
			w_widths[ln.getW_width()]++;
			link_mult[ln.getId()]++;
			if(ln.getGroup()==1){
				//vlink only
				n_acc_vlinks++;
				vonly_widths[ln.getW_width()]++;
			}
		}
	}

}
