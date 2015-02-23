package opsnetwork;

import java.util.ArrayList;
import java.util.List;

import org._3pq.jgrapht.Edge;
import org._3pq.jgrapht.Graph;
import org._3pq.jgrapht.alg.DijkstraShortestPath;
import org._3pq.jgrapht.edge.DirectedEdge;
import org._3pq.jgrapht.graph.SimpleDirectedWeightedGraph;

// Multi graph is not supported
public class RoutingImplSP extends RoutingImpl {

	protected RoutingImplSP(Network net, RoutingTable table){
		super(net,table);
		throw new RuntimeException("This Class has already not supported.");
	}
	
	public void createRoutingTable() {
		// TODO 自動生成されたメソッド・スタブ
		Node[] nodes = net.nodes;
		Link[] links = net.links;
		ArrayList[][] link_matrix = net.linkmatrix;
		
		 Graph g = new SimpleDirectedWeightedGraph();
		 
		 for(int i=0; i<nodes.length; i++){
			 g.addVertex(nodes[i]);
		 }
		 for(int i=0; i<links.length; i++){
			 Edge e = g.addEdge(nodes[links[i].getFrom()], nodes[links[i].getTo()]);
			 e.setWeight(links[i].getVirtualCost());
			 double w=e.getWeight();
			 System.out.println(w);
		 }
		 
		 for(int i=0; i<nodes.length; i++){
			 for(int j=0; j<nodes.length; j++){
				if(i!=j){ 
					 DijkstraShortestPath path = new DijkstraShortestPath(
							 g,nodes[i],nodes[j],Double.POSITIVE_INFINITY);
					 List list = path.getPathEdgeList();
					 RoutingInformation ri = new RoutingInformation();
					 
					 for(int k=0; k<list.size(); k++){
						 DirectedEdge eg = (DirectedEdge) (list.get(k));
						 Node src = (Node) eg.getSource();
						 Node dst = (Node) eg.getTarget();
						 int outport = src.findOutportByNextNode(dst.getId());
						 int max_w=net.getNwset().max_w;
						 Link ln = (Link) link_matrix[src.getId()][dst.getId()].get(0);
						 ri.add(outport, 0, max_w, ln);
					}
					 table.setRoutingInformation(i, j, 0, ri);
					 System.out.println(list);
				}
			 }
		 }
	}

}
