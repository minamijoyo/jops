package opsnetwork;

import java.util.ArrayList;

public class RoutingImplHopLimit extends RoutingImpl1HopFirst {

	private int add_hop_limit;
	protected RoutingImplHopLimit(Network net, RoutingTable table) {
		super(net, table);
		// TODO �����������ꂽ�R���X�g���N�^�[�E�X�^�u
		this.add_hop_limit=nwset.add_hop_limit;
	}

	protected boolean checkHopLimit(int src, int dst, ArrayList found_path){
		if(table.isEmptyEntry(src, dst)){
			return true;
		}
		
		RoutingInformation ri=table.getRoutingInformation(src, dst, 0);
		int shortest_hop=ri.getN_hop();
		return found_path.size() <= (shortest_hop+add_hop_limit);
	}
	
}
