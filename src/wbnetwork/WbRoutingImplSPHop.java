package wbnetwork;

import java.util.ArrayList;

public class WbRoutingImplSPHop extends WbRoutingImplSP {
	public WbRoutingImplSPHop(ArrayList[][] shortestpath) {
		super(shortestpath);
	}
	
	protected double getLinkCost(WbLink ln){
		return 1000+ln.cost;
	}
}
