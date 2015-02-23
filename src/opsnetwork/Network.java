package opsnetwork;

import java.io.*;
import java.util.ArrayList;

import simuframe.InvalidSimuSettingException;

/**
 * @author m-morita
 * This class provides link connectivity between nodes
 * It is used for routing and packet fowarding.
 * The routing algorithms are implimented by sub classes of RoutingImpl class.
 */
public class Network {
	protected Link[] links;
	protected Node[] nodes;
	protected Link[] rlinks;
	protected Link[] vlinks;
	protected ArrayList[][] linkmatrix;
	protected ArrayList[][] rlinkmatrix;
	protected ArrayList[][] vlinkmatrix;
	protected TrafficMatrix traffic_matrix;
	
	protected NetworkSetting nwset;
	protected RoutingTable rtable;
	protected int max_w;
	
	public Network(NetworkSetting nwset){
		this.nwset=nwset;
		max_w=nwset.max_w;
	}
	
	public Node getNode(int index){
		return nodes[index];
	}
	
	public Link getLink(int index){
		return links[index];
	}
	
	public void init(){
		createFromFile(nwset.topology_file);
		setupConnection();
		initRoutingTable();
	}

	private void initRoutingTable() {
		rtable = new RoutingTable(this);
		RoutingImpl routing = RoutingImpl.getImpl(nwset.routing_alg,this,rtable);
		routing.createRoutingTable();
		rtable.setupWrr(nwset.routing_table_rr);
		for(int i=0; i<nodes.length; i++){
			nodes[i].setRtable(rtable);
		}
	}
	
	private void createFromFile(String filename){
		BufferedReader r = null;
	    try {
	        r = new BufferedReader(new FileReader(filename));
	        skipLineTo(r,"%node_size");
	        int n_nodes=Integer.parseInt(r.readLine());
	        skipLineTo(r,"%rlink_size");
	        int n_rlinks=Integer.parseInt(r.readLine());
	        skipLineTo(r,"%vlink_size");
	        int n_vlinks=Integer.parseInt(r.readLine());
	        skipLineTo(r,"%agvlink_size");
	        int n_agvlinks=Integer.parseInt(r.readLine());
	        
	        switch(nwset.vlink_mode){
	        	case 0:
	        		n_vlinks=0;
	        		break;
	        	case 1:
	        		break;
	        	case 2:
	        		n_vlinks=n_agvlinks;
	        		break;
	        	default:
	        		throw new RuntimeException("Unknown simulation mode:"+nwset.vlink_mode);
	        }
	        nodes = new Node[n_nodes];
	        links = new Link[n_rlinks+n_vlinks];
	        rlinks = new Link[n_rlinks];
	        vlinks = new Link[n_vlinks];
	        
	        skipLineTo(r,"%node_setting");
	        r.readLine();
	        for(int i=0;i<n_nodes;i++){
	        	int id=Integer.parseInt(r.readLine());
	        	nodes[id]=newNode(this,id);
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
	        	int w_start = nwset.rlink_w_start;
	        	int w_width = nwset.rlink_w_width;
	        	links[id]=newLink(this,id,from,to,w_start,w_width,cost,0);
	        	rlinks[id]=links[id];
	        }
	        
	        if(nwset.vlink_mode==1){
		        skipLineTo(r,"%vlink_setting");
		        r.readLine();
		        for(int i=0;i<n_vlinks;i++){
		        	String line=r.readLine();
		        	String[] p = line.split("\t");
		        	int id = Integer.parseInt(p[0]);
		        	int from = Integer.parseInt(p[1]);
		        	int to = Integer.parseInt(p[2]);
		        	double cost = Double.parseDouble(p[3]);
		        	int w_start = Integer.parseInt(p[4]);
		        	int w_width = Integer.parseInt(p[5]);
		        	links[id+n_rlinks]=newLink(this,id+n_rlinks,from,to,w_start,w_width,cost,1);
		        	vlinks[id]=links[id+n_rlinks];
		        }
	        }else if(nwset.vlink_mode==2){
	        	skipLineTo(r,"%agvlink_setting");
		        r.readLine();
		        for(int i=0;i<n_vlinks;i++){
		        	String line=r.readLine();
		        	String[] p = line.split("\t");
		        	int id = Integer.parseInt(p[0]);
		        	int from = Integer.parseInt(p[1]);
		        	int to = Integer.parseInt(p[2]);
		        	double cost = Double.parseDouble(p[3]);
		        	//int w_start = Integer.parseInt(p[4]);
		        	int w_width = Integer.parseInt(p[5]);
		        	int w_bit[] = new int[nwset.max_w];
		        	for(int j=0; j< nwset.vlink_w_width; j++){
		        		w_bit[j]=Integer.parseInt(p[6+j]);
		        	}
		        	links[id+n_rlinks]=newLink(this,id+n_rlinks,from,to,w_bit,w_width,cost,1);
		        	vlinks[id]=links[id+n_rlinks];
		        }
	        }
	        if(nwset.traffic_mode==1){
	        	int[][] traffic = new int[n_nodes][n_nodes]; 
	        	skipLineTo(r,"%traffic_matrix");
		        for(int i=0;i<n_nodes;i++){
		        	String line=r.readLine();
		        	String[] p = line.split("\t");
		        	for(int j=0;j<n_nodes;j++){
		        		traffic[i][j]=Integer.parseInt(p[j]);
		        	}
		        }
		        traffic_matrix = new TrafficMatrix(traffic);
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
	
	protected Node newNode(Network net, int id){
		return new Node(net,id);
	}
	
	protected Link newLink(Network net, int id, int from, int to, int w_start, int w_width, double cost, int group){
		return new Link(net,id,from,to,w_start,w_width,cost,group);
	}
	
	protected Link newLink(Network net, int id, int from, int to, int[] w_bit, int w_width, double cost, int group){
		return new Link(net,id,from,to,w_bit,w_width,cost,group);
	}
	
	private void setupConnection(){
		//set link to node
		for(int i=0; i<links.length; i++){
			Link ln = links[i];
			Node from = nodes[ln.getFrom()];
			Node to = nodes[ln.getTo()];
			from.addOutlink(ln);
			to.addInlink(ln);
		}
		
		//create linkmatrix, which provides link reference from Src.-Dst. pair
		//multilinks are stored in ArrayList
		linkmatrix = new ArrayList[nodes.length][nodes.length];
		rlinkmatrix =  new ArrayList[nodes.length][nodes.length];
		vlinkmatrix =  new ArrayList[nodes.length][nodes.length];
		for(int i=0; i< nodes.length; i++){
			for(int j=0; j< nodes.length; j++){
				linkmatrix[i][j] = new ArrayList();
				rlinkmatrix[i][j] = new ArrayList();
				vlinkmatrix[i][j] = new ArrayList();
			}
		}
		for(int i=0; i< links.length; i++){
			Link ln = links[i];
			linkmatrix[ln.getFrom()][ln.getTo()].add(ln);
		}
		for(int i=0; i< rlinks.length; i++){
			Link ln = rlinks[i];
			rlinkmatrix[ln.getFrom()][ln.getTo()].add(ln);
		}
		for(int i=0; i< vlinks.length; i++){
			Link ln = vlinks[i];
			vlinkmatrix[ln.getFrom()][ln.getTo()].add(ln);
		}
		
		OpsLink.setAveLinkCost(calcAveLinkCost());
		
	}
	
	private double calcAveLinkCost(){
		double sum=0;
		int count=0;
		for(int i=0; i< nodes.length; i++){
			for(int j=0; j< nodes.length; j++){
				if(!rlinkmatrix[i][j].isEmpty()){
					sum+= ((Link)rlinkmatrix[i][j].get(0)).cost;
					count++;
				}
			}
		}
		return sum/count;
	}

	public RoutingTable getRtable() {
		return rtable;
	}

	public NetworkSetting getNwset() {
		return nwset;
	}

	public TrafficMatrix getTraffic_matrix() {
		return traffic_matrix;
	}
	
	public int getN_nodes(){
		return nodes.length;
	}

}
