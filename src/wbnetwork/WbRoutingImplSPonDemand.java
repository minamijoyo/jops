package wbnetwork;

import java.util.ArrayList;
import java.util.List;

import org._3pq.jgrapht.Edge;
import org._3pq.jgrapht.Graph;
import org._3pq.jgrapht.alg.DijkstraShortestPath;
import org._3pq.jgrapht.edge.DirectedEdge;
import org._3pq.jgrapht.graph.SimpleDirectedWeightedGraph;

public class WbRoutingImplSPonDemand{
	//static use only
	//note that this implementation does not support multi fiber mode.
	private WbRoutingImplSPonDemand() {
	}

	public static ArrayList getRoute(WbNetwork net, int src_id, int dst_id, int demand, int grid) {
		// TODO 自動生成されたメソッド・スタブ
		WbNode[] nodes = net.nodes;
		WbLink[] links = net.links;
		
		 Graph g = new SimpleDirectedWeightedGraph();
		 
		 for(int i=0; i<nodes.length; i++){
			 g.addVertex(nodes[i]);
		 }
		 for(int i=0; i<links.length; i++){
			 if(links[i].find_free_wb(demand)!=-1){
				 Edge e = g.addEdge(nodes[links[i].from], nodes[links[i].to]);
				 e.setWeight(links[i].getVirtualCost());
			 }
		 }
		 
		 DijkstraShortestPath path = new DijkstraShortestPath(
				 g,nodes[src_id],nodes[dst_id],Double.POSITIVE_INFINITY);
		 
		 List list = path.getPathEdgeList();
		 if(list==null){
			 throw new WbRouteNotFoundException("src:"+src_id+",dst:"+dst_id);
		 }
		 ArrayList ret = new ArrayList();
		 for(int k=0; k<list.size(); k++){
			 DirectedEdge eg = (DirectedEdge) (list.get(k));
			 WbNode src = (WbNode) eg.getSource();
			 WbNode dst = (WbNode) eg.getTarget();
			 WbLink ln = (WbLink)net.link_matrix[src.id][dst.id].get(0);
			 ret.add(ln);
		}
		return ret;
	}

}
