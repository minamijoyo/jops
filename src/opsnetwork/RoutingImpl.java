package opsnetwork;

public abstract class RoutingImpl {
	protected RoutingResult stat;//This object is used for statistics
	protected Network net;
	protected RoutingTable table;
	
	protected RoutingImpl(Network net, RoutingTable table){
		this.net = net;
		this.table = table;
		stat = new RoutingResult(net);

	}
	public abstract void createRoutingTable();
	
	public static RoutingImpl getImpl(int impl_type, Network net, RoutingTable rtable) {
		RoutingImpl impl=null;
		switch(impl_type){
			case 0:
				impl = new RoutingImplNthSP(net,rtable);
				break;
			case 1:
				impl = new RoutingImpl1HopFirst(net,rtable);
				break;
			case 2:
				impl = new RoutingImplBandWidthSaving(net,rtable);
				break;
			case 3:
				impl = new RoutingImplHopLimit(net,rtable);
				break;
			default:
				throw new RuntimeException("Unknown routing_alg:"+impl_type);
		}
		return impl;
	}
}
