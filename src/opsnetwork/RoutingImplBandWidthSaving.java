package opsnetwork;

public class RoutingImplBandWidthSaving extends RoutingImpl1HopFirst {

	private TrafficMatrix traffic_matrix;
	protected RoutingImplBandWidthSaving(Network net, RoutingTable table) {
		super(net, table);
		// TODO 自動生成されたコンストラクター・スタブ
		this.traffic_matrix=net.getTraffic_matrix();
	}
	
	protected boolean isEnoughBandWidth(int src, int dst){
		double cur = currentEffectiveBandWidth(src, dst);
		double require = estimateRequiredBandWidth(src, dst);
		boolean result= (cur>=require); 
		//if(result){
		//	System.out.println("src:"+src+"\tdst:"+dst+"\tcur:"+cur+"\trequire:"+require+"\tresult:"+result);
		//}
		return result;
	}
	
	private double estimateRequiredBandWidth(int src, int dst){
		double network_traffic_bpus = net.getNwset().lambda*PacketManager.fix_packet_size_bybit;
		double traffic_ratio = traffic_matrix.getNormarilizedTraffic(src, dst);
		double traffic_per_SDpair = network_traffic_bpus * traffic_ratio;
		return traffic_per_SDpair;
	}
	
	private double currentEffectiveBandWidth(int src, int dst){
		double effective_width=0;
		int rr_max=table.getRr_max(src, dst);
		for(int i=0;i<rr_max;i++){
			RoutingInformation ri = table.getRoutingInformation(src, dst, i);
			int n_hop=ri.getN_hop();
			if(n_hop==1){
				Link[] link_list = ri.getLn_list();
				effective_width+= link_list[0].getW_width()*1.0;
			}else{
				effective_width+= ri.getMin_width()*0.3;
			}
		}
		return effective_width*OpsLink.channel_speed;
	}
}
