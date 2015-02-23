package opsnetwork;

public class Node {
	protected int id;
	protected Link[] inlinks;
	protected Link[] outlinks;
	protected int n_inlinks = 0;
	protected int n_outlinks = 0;
	
	protected Network net;
	protected RoutingTable rtable;
	
	public Node(Network net, int id){
		this.net=net;
		this.id=id;
		inlinks = new Link[net.links.length];
		outlinks = new Link[net.links.length];
	}
	
	public String toString(){
		return new String("Node_id:" + id);
	}
	
	public int getId(){
		return id;
	}
	public void addInlink(Link ln){
		inlinks[n_inlinks++] = ln;
	}
	
	public void addOutlink(Link ln){
		outlinks[n_outlinks++] = ln;
	}

	public int getN_inlinks() {
		return n_inlinks;
	}

	public int getN_outlinks() {
		return n_outlinks;
	}
	
	public int findOutportByNextNode(int node_id){
		for(int i=0; i<n_outlinks; i++){
			if(node_id == outlinks[i].getTo()){
				return i;
			}
		}
		throw new RuntimeException("Requested node_id=" + node_id + " not found at Node" + this.id);
	}
	
	public int findOutportByNextLink(int link_id){
		for(int i=0; i<n_outlinks; i++){
			if(link_id == outlinks[i].id){
				return i;
			}
		}
		throw new RuntimeException("Requested link_id=" + link_id + " not found at Node" + this.id);
	}

	public void setRtable(RoutingTable rtable) {
		this.rtable = rtable;
	}
}
