package opsnetwork;

public class OpsLink extends Link {
	public static final double channel_speed = 10000;//10Gbps=10000bit/us
	private static final double propagation_speed = 0.2;//200000km/s=0.2km/us
	private static final double ave_link_length = 100;//100km
	private static double ave_link_cost;
	public OpsLinkResult result = new OpsLinkResult(this);
	
	public OpsLink(Network net, int id, int from, int to, int w_start, int w_width, double cost, int group) {
		super(net, id, from, to, w_start, w_width, cost, group);
		// TODO Auto-generated constructor stub
	}
	public OpsLink(Network net, int id, int from, int to, int[] w_bit, int w_width, double cost, int group) {
		super(net, id, from, to, w_bit, w_width, cost, group);
		// TODO Auto-generated constructor stub
	}
	
	public void pushPacket(Packet p, int wavelength){
	}
	
	public void popPacket(int wavelength){
		result.pk_ok++;
	}
	
	public void informPacketLoss(Packet p){
		result.pk_loss++;
	}
	
	public double getTransDelay(){
		return cost* (ave_link_length / ave_link_cost) /propagation_speed;
	}
	
	public double getVlinkOverhead(){
		return link_cost_alpha;
	}
	
	public static void setAveLinkCost(double cost){
		ave_link_cost=cost;
	}
	
	
}
