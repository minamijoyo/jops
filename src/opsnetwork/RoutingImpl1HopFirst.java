package opsnetwork;

import java.util.ArrayList;
import java.util.Collections;

public class RoutingImpl1HopFirst extends RoutingImplNthSP {

	protected RoutingImpl1HopFirst(Network net, RoutingTable table) {
		// TODO 自動生成されたコンストラクター・スタブ
		super(net,table);
	}

	public void createRoutingTable() {
		// TODO 自動生成されたメソッド・スタブ
		for(int i=0; i<nodes.length; i++){
			 for(int j=0; j<nodes.length; j++){
				if(i!=j){
					int rr=0;
					if(linkmatrix[i][j]!=null){
						rr = find1HopPath(i, j, rr);
					}
					if(!isEnoughBandWidth(i, j)){
						findNthShortestPath(i, j, rr);
					}
				}
			 }
		 }
	}

	private int find1HopPath(int src, int dst, int rr) {
		ArrayList onehop = getSorted1HopLinkList(src, dst);
		while(rr<onehop.size() && rr< nth_shortestpath ){
			//check 1Hop path
			Link ln = (Link)onehop.get(rr);
			RoutingInformation ri = createRoutingInformation1Hop(ln);
			//set RoutingInformation to table
			table.setRoutingInformation(src, dst, rr, ri);
			
			ArrayList found_path = new ArrayList();
			found_path.add(ln);
			stat.addPath(found_path);
			rr++;
		}
		return rr;
	}

	private ArrayList getSorted1HopLinkList(int src, int dst) {
		ArrayList result = (ArrayList)linkmatrix[src][dst].clone();
		Collections.sort(result,new Link.CompByWidth());
		return result;
	}

	private RoutingInformation createRoutingInformation1Hop(Link ln) {
		RoutingInformation ri = new RoutingInformation();
		Node src = nodes[ln.getFrom()];
		//find outport from Link
		int outport = src.findOutportByNextLink(ln.getId());
		//set RoutingInformation Element
		ri.add(outport, ln.getW_start(), ln.getW_width(), ln);
		return ri;
	}
	
	protected void initGraphState(int src, int dst, int rr) {
		graph_state = new RoutingImplNthSPGraphState(net);
		graph_state.createConditionalMinLinkmatrix(src,dst,nwset.grid);
		graph_state.createActiveLinks();
	}

	protected void updateGraphState(ArrayList found_path) {
		// TODO 自動生成されたメソッド・スタブ
		super.updateGraphState(found_path);
		addTrivialCost(found_path);
	}
	
	private void addTrivialCost(ArrayList found_path){
		for(int i=0; i<found_path.size(); i++){
			((Link)found_path.get(i)).addTrivialCost();
		}
	}


}
