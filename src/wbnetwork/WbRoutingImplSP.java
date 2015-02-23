package wbnetwork;

import java.util.ArrayList;
import java.util.List;

import org._3pq.jgrapht.Edge;
import org._3pq.jgrapht.Graph;
import org._3pq.jgrapht.alg.DijkstraShortestPath;
import org._3pq.jgrapht.edge.DirectedEdge;
import org._3pq.jgrapht.graph.SimpleDirectedWeightedGraph;

public class WbRoutingImplSP extends WbRoutingImpl{

	protected ArrayList[][] shortestpath;
	public WbRoutingImplSP(ArrayList[][] shortestpath) {
		// TODO 自動生成されたコンストラクター・スタブ
		this.shortestpath = shortestpath;
	}

	public void createRoutingTable(WbNetwork net) {
		// TODO 自動生成されたメソッド・スタブ
		WbNode[] nodes = net.nodes;
		WbLink[] links = net.links;
		
		 Graph g = constructGraph(nodes, links);
		 
		 int n_route=0;
		 int hop_sum=0;
		 for(int i=0; i<nodes.length; i++){
			 for(int j=0; j<nodes.length; j++){
				if(i!=j){ 
					 DijkstraShortestPath path = new DijkstraShortestPath(
							 g,nodes[i],nodes[j],Double.POSITIVE_INFINITY);
					 List list = path.getPathEdgeList();
					 shortestpath[i][j]= new ArrayList();
					 for(int k=0; k<list.size(); k++){
						 DirectedEdge eg = (DirectedEdge) (list.get(k));
						 WbNode src = (WbNode) eg.getSource();
						 WbNode dst = (WbNode) eg.getTarget();
						 WbLink ln = (WbLink)net.link_matrix[src.id][dst.id].get(0);
						 shortestpath[i][j].add(ln);
					}
					 hop_sum+=list.size();
					 n_route++;
					 //System.out.println(list);
				}
			 }
		 }
		 //System.out.println("average_hop:" +(hop_sum/(double)n_route));

	}

	private Graph constructGraph(WbNode[] nodes, WbLink[] links) {
		Graph g = new SimpleDirectedWeightedGraph();
		 
		 for(int i=0; i<nodes.length; i++){
			 g.addVertex(nodes[i]);
		 }
		 for(int i=0; i<links.length; i++){
			 Edge e = g.addEdge(nodes[links[i].from], nodes[links[i].to]);
			 e.setWeight(getLinkCost(links[i]));
			 
		 }
		return g;
	}
	
	protected double getLinkCost(WbLink ln){
		return ln.cost;
	}
	

}
