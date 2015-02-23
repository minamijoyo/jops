package opsnetwork;

import java.util.ArrayList;
import java.util.List;

import org._3pq.jgrapht.Edge;
import org._3pq.jgrapht.Graph;
import org._3pq.jgrapht.alg.DijkstraShortestPath;
import org._3pq.jgrapht.edge.DirectedWeightedEdge;
import org._3pq.jgrapht.graph.SimpleDirectedWeightedGraph;

public class RoutingImplNthSP extends RoutingImpl {

	protected Node[] nodes;
	protected Link[] links;
	protected ArrayList[][] linkmatrix;
	protected NetworkSetting nwset;
	protected int nth_shortestpath;
	protected int max_w;
	protected RoutingImplNthSPGraphState graph_state;
	protected RoutingImplNthSP(Network net, RoutingTable table) {
		// TODO 自動生成されたコンストラクター・スタブ
		super(net,table);
		init_common();
	}

	protected void init_common() {
		nodes = net.nodes;
		links = net.links;
		linkmatrix = net.linkmatrix;
		nwset = net.nwset;
		nth_shortestpath = nwset.nth_shortestpath;
		max_w = nwset.max_w;
	}
	
	public void createRoutingTable() {
		for(int i=0; i<nodes.length; i++){
			 for(int j=0; j<nodes.length; j++){
				if(i!=j){
					int rr=0;
					findNthShortestPath(i, j, rr);
				}
			 }
		 }
	}

	protected void findNthShortestPath(int src, int dst, int rr) {
		initGraphState(src, dst, rr);
		for( ; rr< nth_shortestpath ; rr++){
			ArrayList found_path = findDijkstraShortestPath(src, dst);
			if(found_path==null){
				break;//no more path between nodes
			}
			if(!checkHopLimit(src,dst,found_path)){
				break;//default implementation returns always true
						//Link's cost function must be hop-based one.
			}
			
			RoutingInformation ri = createRoutingInformation(found_path);
			table.setRoutingInformation(src, dst, rr, ri);
			
			updateGraphState(found_path);
			stat.addPath(found_path);
			
			if(isEnoughBandWidth(src,dst)){
				break;
			}
		}
	}
	
	protected boolean checkHopLimit(int src, int dst, ArrayList found_path){
		return true;
	}
	
	protected boolean isEnoughBandWidth(int src, int dst){
		return false;
	}

	protected void initGraphState(int src, int dst, int rr) {
		graph_state = new RoutingImplNthSPGraphState(net);
		graph_state.createMinLinkmatrix();
		graph_state.createActiveLinks();
	}

	protected void updateGraphState(ArrayList found_path) {
		graph_state.updateActiveLinks(found_path);
	}
	
	private ArrayList findDijkstraShortestPath(int src, int dst) {
		//create graph
		Graph g = constructGraph(graph_state.getActive_links());
		//find path by Dijkstra
		DijkstraShortestPath path = new DijkstraShortestPath(
				g,nodes[src],nodes[dst],Double.POSITIVE_INFINITY);
		
		return edgeListToLinkList(path.getPathEdgeList());
	}
	
	private Graph constructGraph(ArrayList active_links) {
		Graph g = new SimpleDirectedWeightedGraph();
		 
		for(int ii=0; ii<nodes.length; ii++){
			g.addVertex(nodes[ii]);
		}
		Edge[] edges = new DirectedWeightedEdge[active_links.size()];
		for(int ii=0; ii<active_links.size(); ii++){
			Link ln = (Link)active_links.get(ii);
			edges[ii] = g.addEdge(nodes[ln.getFrom()], nodes[ln.getTo()]);
			edges[ii].setWeight(ln.getVirtualCost());
		}
		return g;
	}


	private RoutingInformation createRoutingInformation(ArrayList found_path) {
		RoutingInformation ri = new RoutingInformation();
		for(int k=0; k<found_path.size(); k++){
			Link ln = (Link)(found_path.get(k));
			Node src = nodes[ln.getFrom()];
			//find outport from Link
			int outport = src.findOutportByNextLink(ln.getId());
			//set RoutingInformation Element
			ri.add(outport, ln.getW_start(), ln.getW_width(), ln);
		}
		return ri;
	}

	private ArrayList edgeListToLinkList(List edge_list){
		if(edge_list==null){
			return null;
		}
		
		ArrayList link_list = new ArrayList();
		for(int k=0; k<edge_list.size(); k++){
			Edge eg = (Edge) (edge_list.get(k));
			Link ln = graph_state.edgeToLink(eg);
			link_list.add(ln);
		}
		return link_list;
	}
}
