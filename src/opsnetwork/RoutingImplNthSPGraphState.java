package opsnetwork;

import java.util.ArrayList;

import org._3pq.jgrapht.Edge;

import edu.emory.mathcs.backport.java.util.PriorityQueue;

public class RoutingImplNthSPGraphState {
	private Node[] nodes;
	private ArrayList[][] linkmatrix;
	private PriorityQueue[][] min_linkmatrix;
	private ArrayList active_links;
	
	public RoutingImplNthSPGraphState(Network net){
		this.nodes = net.nodes;
		this.linkmatrix = net.linkmatrix;
	}
	public PriorityQueue[][] createMinLinkmatrix() {
		min_linkmatrix = new PriorityQueue[nodes.length][nodes.length];
		for(int i=0; i< nodes.length; i++){
			for(int j=0; j< nodes.length; j++){
				if(!linkmatrix[i][j].isEmpty()){
					min_linkmatrix[i][j] = new PriorityQueue();
					min_linkmatrix[i][j].addAll(linkmatrix[i][j]);
				}
			}
		}
		return min_linkmatrix;
	}
	
	public PriorityQueue[][] createConditionalMinLinkmatrix(int src, int dst, int width_th) {
		min_linkmatrix = new PriorityQueue[nodes.length][nodes.length];
		for(int i=0; i< nodes.length; i++){
			for(int j=0; j< nodes.length; j++){
				if(!linkmatrix[i][j].isEmpty() && !(i==src && j==dst)){
					min_linkmatrix[i][j] = new PriorityQueue();
					for(int k=0; k<linkmatrix[i][j].size(); k++){
						Link ln = (Link)linkmatrix[i][j].get(k);
						if(ln.getW_width()>=width_th){
							min_linkmatrix[i][j].add(ln);
						}
					}
				}
				
			}
		}
		return min_linkmatrix;
	}
	
	public ArrayList createActiveLinks() {
		active_links = new ArrayList();
		for(int i=0; i< nodes.length; i++){
			for(int j=0; j< nodes.length; j++){
				if(min_linkmatrix[i][j]!=null && !min_linkmatrix[i][j].isEmpty()){
					active_links.add(min_linkmatrix[i][j].peek());
				}
				
			}
		}
		return active_links;
	}
	
	public void updateActiveLinks(ArrayList found_path) {
		for(int k=0; k<found_path.size(); k++){
			Link ln = (Link) found_path.get(k);
			//del used link
			active_links.remove(ln);
			//add link which has next min cost
			PriorityQueue q=min_linkmatrix[ln.getFrom()][ln.getTo()];
			q.remove();
			if(!q.isEmpty()){
				active_links.add(q.peek());
			}
		}
	}
	
	public Link edgeToLink(Edge eg) {
		Node src = (Node) eg.getSource();
		Node dst = (Node) eg.getTarget();
		return (Link)(min_linkmatrix[src.getId()][dst.getId()]).peek();
	}
	
	public ArrayList getActive_links() {
		return active_links;
	}
}
