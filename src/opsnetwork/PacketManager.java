package opsnetwork;
import simuframe.Simu;

public class PacketManager {
	
	public PacketManagerResult result;
	public static final int fix_packet_size_bybit = 15000*8;

	//traffic_matrix mode=1
//	private Network net;
	private PacketGenerator pgen;
	
	public PacketManager(Network net, PacketGenerator pgen){
//		this.net =net;
		this.pgen=pgen;
		result = new PacketManagerResult(net);
	}
			
	public Packet packetNew(){
		return pgen.packetNew(++result.total_pk_new,Simu.getTime(),fix_packet_size_bybit);
	}
	public void packetDeleteOk(Packet p){
		result.total_pk_delete_ok++;
		double wating_time = Simu.getTime() - p.getTime();
		result.total_wating_time_sum += wating_time;
	}
	
	public void packetDeleteLoss(Packet p){
		result.total_pk_delete_loss++;
		result.total_pk_loss_path[p.getRi().getId()]++;
	}
}
