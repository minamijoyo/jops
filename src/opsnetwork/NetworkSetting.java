package opsnetwork;

import simuframe.SimuSetting;

public class NetworkSetting extends SimuSetting {

	public String topology_file;
	public int max_w;
	public double link_cost_alpha;
	public double link_cost_beta;
	public int rlink_w_start;
	public int rlink_w_width;
	public int nth_shortestpath;
	public long seed;
	public long total_packet;
	public double lambda;
	public int vlink_mode;
	public int vlink_w_width;
	public int wc;
	public int traffic_mode;
	public int routing_table_rr;
	public int grid;
	public int routing_alg;
	public int add_hop_limit;
	
	public NetworkSetting(){
		String[] dummy=new String[1];
		analyzeCmdParam(dummy);
	}
	
	public NetworkSetting(String[] args){
		analyzeCmdParam(args);
	}
	
	public boolean isValid() {
		// TODO 自動生成されたメソッド・スタブ
		if(!topology_file.endsWith(".rst.txt")) return false;
		if(total_packet<=0) return false;
		if(lambda<=0) return false;
		if(vlink_w_width<0) return false;
		if(rlink_w_width<=0) return false;
		if(wc<0) return false;
		if(link_cost_alpha<0) return false;
		if(link_cost_beta<0) return false;
		if(vlink_mode<0 || vlink_mode>2) return false;
		if(routing_alg<0 || routing_alg>3) return false;
		if(add_hop_limit<0) return false;
		if(nth_shortestpath<1) return false;
		if(grid<1) return false;
		
		return true;
	}
	
	private boolean analyzeCmdParam(String[] args){
		if(args.length!=10){
			System.out.println("Invalid args:"+args.length);
			System.out.println("Usage: topology_file total_packet lambda vlink_w_width rlink_w_width wc link_cost_alpha link_cost_beta vlink_mode grid");
			return false;
		}
		topology_file=args[0];
		total_packet=Long.parseLong(args[1]);
		lambda=Double.parseDouble(args[2]);
		vlink_w_width=Integer.parseInt(args[3]);
		rlink_w_width=Integer.parseInt(args[4]);
		wc=Integer.parseInt(args[5]);
		link_cost_alpha=Double.parseDouble(args[6]);
		link_cost_beta=Double.parseDouble(args[7]);
		vlink_mode=Integer.parseInt(args[8]);
		if(vlink_mode==0){
			routing_alg=3;//RoutingImpl1HopLimit
		}else if(vlink_mode==1){
			routing_alg=1;//RoutingImpl1HopFirst
		}else{
			routing_alg=-1;
		}
		add_hop_limit=0;
		max_w=vlink_w_width+rlink_w_width;
		rlink_w_start=vlink_w_width;
		
		nth_shortestpath=8;
		grid=Integer.parseInt(args[9]);
		seed=0;
		//nwset.traffic_mode=0;//use traffic random
		traffic_mode=1;//use traffic matrix
		routing_table_rr=0;//use uniform round-robin
		//nwset.routing_table_rr=1;//use weighted round-robin
		return true;
	}
	
	public void printSetting(){
		System.out.println("[Setting Param in OPS network]");
		System.out.println("topology_file:"+topology_file);
		System.out.println("total_packet:"+total_packet);
		System.out.println("lambda:"+lambda);
		System.out.println("vlink_w_width:"+vlink_w_width);
		System.out.println("rlink_w_width:"+rlink_w_width);
		System.out.println("wc:"+wc);
		System.out.println("link_cost_alpha:"+link_cost_alpha);
		System.out.println("link_cost_beta:"+link_cost_beta);
		System.out.println("vlink_mode:"+vlink_mode);
		System.out.println("routing_alg:"+routing_alg);
		System.out.println("add_hop_limit:"+add_hop_limit);
		System.out.println("max_w:"+max_w);
		System.out.println("rlink_w_start:"+rlink_w_start);
		System.out.println("nth_shortestpath:"+nth_shortestpath);
		System.out.println("grid:"+grid);
		System.out.println("seed:"+seed);
		System.out.println("traffic_mode:"+traffic_mode);
		System.out.println("routing_table_rr:"+routing_table_rr);
	}


}
