package wbnetwork;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import opsnetwork.NetworkInitilizeException;
import simuframe.InvalidSimuSettingException;

public class WbNetwork {
	protected WbNode[] nodes;
	protected WbLink[] links;
	protected ArrayList[][] link_matrix;
	protected ArrayList[][] shortestpath;
	protected ArrayList[][] shortesthoppath;
	protected WbNetworkSetting nwset;
	protected int max_w;
	protected ArrayList traffic;
	protected ArrayList vlinks;
	protected ArrayList wbpath;
	protected ArrayList agvlinks;
	protected int[] n_fibers;
	
	public WbNetwork(WbNetworkSetting nwset){
		this.nwset = nwset;
		max_w= nwset.max_w;
	}
	
	public void init(){
		createFromFile(nwset.topology_file);
		setupConnection();
		makeTraffic();
		findShortestPath();
		findShortestHopPath();
		
		//setup for multi fiber mode
		//this process modifies links, link_matrix, and in/out links of nodes.
		if(nwset.multi_fiber>1){
			makeMultiFiber();
		}

		wavebandAssign();
		vlinkAssign();
		
		//if needed
		makeAgregateVlinks(nwset.agvlink_th);
		//if needed
		//vlinks=splitVlinks(vlinks);
	}

	private void makeTraffic() {
		MakeTrafficImpl mk_traffic = new MakeTrafficImplRandom(nwset.total_traffic,nwset.seed);
		traffic = mk_traffic.makeTraffic(nodes.length);
	}

	private void findShortestPath() {
		shortestpath = new ArrayList[nodes.length][nodes.length];
		WbRoutingImpl routing = new WbRoutingImplSPHop(shortestpath);
		routing.createRoutingTable(this);
	}

	private void findShortestHopPath() {
		shortesthoppath = new ArrayList[nodes.length][nodes.length];
		WbRoutingImpl hop_routing = new WbRoutingImplSPHop(shortesthoppath);
		hop_routing.createRoutingTable(this);
	}

	private void wavebandAssign() {
		WbAssignImpl wave = WbAssignImpl.getImpl(nwset.wb_assign_alg,this);
		wave.assign();
		//System.out.println("WB assign end");
	}

	private void vlinkAssign() {
		VlinkAssignImpl vl = VlinkAssignImpl.getImpl(nwset.vlink_assign_alg,this);
		vlinks = vl.assign();
		//System.out.println("VL assign end");
	}

	private void createFromFile(String filename){
		BufferedReader r = null;
	    try {
	        r = new BufferedReader(new FileReader(filename));
	        skipLineTo(r,"%node_size");
	        int n_nodes=Integer.parseInt(r.readLine());
	        skipLineTo(r,"%rlink_size");
	        int n_rlinks=Integer.parseInt(r.readLine());
	        
	        nodes = new WbNode[n_nodes];
	        links = new WbLink[n_rlinks];
	        
	        skipLineTo(r,"%node_setting");
	        r.readLine();
	        for(int i=0;i<n_nodes;i++){
	        	int id=Integer.parseInt(r.readLine());
	        	nodes[id]=new WbNode(this,id);
	        }
	        
	        skipLineTo(r,"%rlink_setting");
	        r.readLine();
	        for(int i=0;i<n_rlinks;i++){
	        	String line=r.readLine();
	        	String[] p = line.split("\t");
	        	int id = Integer.parseInt(p[0]);
	        	int from = Integer.parseInt(p[1]);
	        	int to = Integer.parseInt(p[2]);
	        	double cost = Double.parseDouble(p[3]);
	        	links[id]=new WbLink(this,id,from,to,cost,max_w,nwset.grid);
	        }
	        
	        link_matrix = new ArrayList[n_nodes][n_nodes];
	        for(int i=0; i<n_rlinks; i++){
	        	WbLink ln = links[i];
	        	link_matrix[ln.from][ln.to] = new ArrayList();
	        	link_matrix[ln.from][ln.to].add(ln);
	        }
	        
	    } catch (Exception e) {
	        throw new NetworkInitilizeException(e);
	    } finally {
	        try {
	            if (r != null) {
	                r.close();
	            }
	        } catch (Exception e) {
	        	throw new NetworkInitilizeException(e);
	        }
	    }
	}
	
	private void skipLineTo(BufferedReader r, String str)throws IOException{
		String line;
		while((line = r.readLine())!=null){
			if(line.compareTo(str)==0) return;
		}
		//str not found.
		throw new InvalidSimuSettingException(str+" not found in setting file.");
	}
	
	private void setupConnection(){
		for(int i=0; i<links.length; i++){
			WbNode src = nodes[links[i].from];
			WbNode dst = nodes[links[i].to];
			src.addOutlinks(links[i]);
			dst.addInlinks(links[i]);
		}
			
	}
	public void printUsedLinkMatrix(){
		int sum=0;
		for(int i=0; i<max_w; i++){
			for(int j=0; j<links.length; j++){
				System.out.print(links[j].w_used[i]+",");
				if(links[j].w_used[i]>=0){
					sum++;
				}
			}
			System.out.println("");
		}
		for(int i=0; i<links.length; i++){
			System.out.print(links[i].getUtilWithoutVlink()+",");
		}
		System.out.println("");
		for(int i=0; i<links.length; i++){
			System.out.print(links[i].getUtilWithVlink()+",");
		}
		System.out.println("");
		
		double util=(double)sum /(links.length*max_w);
		System.out.println("wave_utilization="+util);
		
		int sum_grid=0;
		int grid=nwset.grid;
		for(int j=0; j<links.length; j++){
			for(int i=0; i<max_w; i+=grid){
				for(int k=0; k<grid; k++){
					if(links[j].w_used[i+k]>=0){
						sum_grid++;
						break;
					}
				}
			}
		}
		double util_grid=(double)sum_grid/((double)links.length*max_w/grid);
		System.out.println("grid_utilization="+util_grid);
	}
	
	public void printBlankHistrgam(){
		int[] hist = new int[max_w+1];
		int width=0;
		for(int i=0; i<links.length; i++){
			for(int j=0; j<max_w; j++){
				if(links[i].w_used[j]>-1){
					if(width!=0){
						hist[width]++;
					}
					width=0;
				}else{
					width++;
				}
			}
			if(width!=0){
				hist[width]++;
			}
			if(width==max_w){
				//System.out.println(links[i]);
			}
			width=0;
		}
		System.out.println("Balnk Width Histgram");
		for(int i=0; i<hist.length; i++){
			System.out.println(hist[i]);
		}
	}
/*	
	public void printNormalizedTrafficMatrix(){
		double[][] matrix = new double[nodes.length][nodes.length];
		double sum=0;
		//sum total demand
		for(int i=0; i< traffic.size(); i++){
			sum+= ((TrafficDemand) traffic.get(i)).demand;
		}
		//normalize as "sum= 1.0"
		for(int i=0; i< traffic.size(); i++){
			TrafficDemand d = ((TrafficDemand) traffic.get(i));
			matrix[d.from][d.to]= d.demand / sum;
		}
		//print
		System.out.println("%traffic_matrix");
		for(int i=0; i<nodes.length; i++){
			for(int j=0; j<nodes.length; j++){
				System.out.print(matrix[i][j]+"\t");
			}
			System.out.println("");
		}
	}
*/	
	public void printTrafficMatrix(){
		int[][] matrix = new int[nodes.length][nodes.length];
		for(int i=0; i< traffic.size(); i++){
			TrafficDemand d = ((TrafficDemand) traffic.get(i));
			matrix[d.from][d.to]= d.demand;
		}
		//print
		System.out.println("%traffic_matrix");
		for(int i=0; i<nodes.length; i++){
			for(int j=0; j<nodes.length; j++){
				System.out.print(matrix[i][j]+"\t");
			}
			System.out.println("");
		}
	}
	
	public void printVlinkTopology(){
		//output virtual topology
		//this format is an input setting file for the packet simulation.
		System.out.println("%node_size");
		System.out.println(nodes.length);
		System.out.println("%rlink_size");
		System.out.println(links.length);
		System.out.println("%vlink_size");
		System.out.println(vlinks.size());
		System.out.println("%agvlink_size");
		System.out.println(agvlinks.size());
		System.out.println("");
		
		System.out.println("%node_setting");
		System.out.println("%index");
		for(int i=0; i< nodes.length; i++){
			System.out.println(nodes[i].id);
		}
		System.out.println("");
		
		System.out.println("%rlink_setting");
		System.out.println("%index\tfrom\tto\tcost");
		for(int i=0; i< links.length; i++){
			System.out.println(""+links[i].id+"\t"+links[i].from+"\t"+links[i].to+"\t"+links[i].cost);
		}
		System.out.println("");
		
		System.out.println("%vlink_setting");
		System.out.println("%index\tfrom\tto\tcost\tw_start\tw_width");
		for(int i=0; i< vlinks.size(); i++){
			Vlink ln = (Vlink)vlinks.get(i);
			System.out.println(""+ln.id+"\t"+ln.from+"\t"+ln.to+"\t"+ln.cost+"\t"+ln.w_start+"\t"+ln.w_width);
		}
		System.out.println("");
		
		System.out.println("%agvlink_setting");
		System.out.println("%index\tfrom\tto\tcost\tw_start\tw_width\tw_bit");
		for(int i=0; i< agvlinks.size(); i++){
			AgVlink ln = (AgVlink)agvlinks.get(i);
			System.out.print(""+ln.id+"\t"+ln.from+"\t"+ln.to+"\t"+ln.cost+"\t"+ln.w_start+"\t"+ln.w_width+"\t");
			for(int j=0; j<ln.w_bit.length; j++){
				System.out.print(""+ln.w_bit[j]+"\t");
			}
			System.out.println("");
		}
		System.out.println("");
	}
	
	private void makeAgregateVlinks(int min_width){
		agvlinks = new ArrayList();
		int counter =0;
		for(int i=0; i< vlinks.size(); i++){
			Vlink ln = (Vlink)vlinks.get(i);
			boolean found =false;
			for(int j=0; j<agvlinks.size(); j++){
				AgVlink ag = (AgVlink)agvlinks.get(j);
				if(ag.isAddable(ln) && (ln.w_width < min_width || ag.w_width < min_width)){
					ag.addVlink(ln);
					found =true;
					break;
				}
			}
			if(!found){
				agvlinks.add(new AgVlink(counter++,ln,max_w));
			}
		}
	}
	
	private void makeMultiFiber(){
		//sum traffic demand on the link
		int[] link_traffic = new int [links.length];
		int grid = nwset.grid;
		for(int i=0; i< traffic.size(); i++){
			TrafficDemand d = (TrafficDemand)traffic.get(i);
			ArrayList path = shortestpath[d.from][d.to];
			for(int j=0; j<path.size(); j++){
				WbLink ln = (WbLink)path.get(j);
				link_traffic[ln.id]+=(d.demand/grid+1)*grid;
			}
		}
		
		//number of fibers
		n_fibers = new int[links.length];
		int count=0;
		int max_fibers=0;
		for(int i=0; i<n_fibers.length; i++){
			n_fibers[i]= (int)(link_traffic[i]/nwset.max_w+1)+nwset.fiber_margin;
			count+=n_fibers[i];
			if(n_fibers[i]>max_fibers){
				max_fibers=n_fibers[i];
			}
			//if(n_fibers[i]>nwset.multi_fiber){
			//	throw new RuntimeException("multi fiber overflow:"+n_fibers[i]);
			//}
		}
		int id=links.length;
		WbLink[] newlinks = new WbLink[count];// new links array
		System.arraycopy(links,0,newlinks,0,links.length);// copy from old one
		//create new link
		for(int i=0; i<n_fibers.length; i++){
			WbLink p = links[i];//original link
			for(int j=1; j<n_fibers[i]; j++){
				WbLink ln = new WbLink(p.net,id,p.from,p.to,p.cost,p.max_w,p.grid);
				newlinks[id]=ln;
				//update connection
				link_matrix[ln.from][ln.to].add(ln);
				nodes[ln.from].addOutlinks(ln);
				nodes[ln.to].addInlinks(ln);
				id++;     
			}
		}
		//replace to new one
		links = newlinks;
		
		System.out.println("max_fibers:"+max_fibers);
	}

	public WbNetworkSetting getNwset() {
		return nwset;
	}
	
	public ArrayList getMultiFibers(WbLink ln){
		return link_matrix[ln.from][ln.to];
	}
	
	private ArrayList splitVlinks(ArrayList vlinks){
		int count=0;
		ArrayList split_vlinks=new ArrayList();
		for(int i=0; i<vlinks.size(); i++){
			Vlink vl =(Vlink)vlinks.get(i);
			for(int j=0; j<vl.path.size(); j++){
				WbLink ln =(WbLink)vl.path.get(j);
				ArrayList new_path = new ArrayList();
				new_path.add(ln);
				split_vlinks.add(new Vlink(count++,new_path,vl.w_start,vl.w_width));
			}
		}
		return split_vlinks;
	}
}
