package opsnetwork;

import java.util.ArrayList;

import simuframe.RankingTable;
import simuframe.Simu;
import simuframe.SimuResult;

public class PacketManagerResult extends SimuResult {
	public long total_pk_new=0;
	public long total_pk_delete_ok=0;
	public long total_pk_delete_loss=0;
	public double total_wating_time_sum=0;
	
	public long[] total_pk_loss_path;
	
	private Network net;
	public PacketManagerResult(Network net){
		SimuResult.addResultList(this);
		this.net=net;
		total_pk_loss_path = new long[net.getRtable().getNentry()];
	}
	public void printResult() {
		// TODO 自動生成されたメソッド・スタブ
		if(total_pk_delete_loss!=0){
			printLossPath();
		}
		System.out.println("total_pk_new:"+total_pk_new);
		System.out.println("total_pk_delete_ok:"+total_pk_delete_ok);
		System.out.println("total_pk_delete_loss:"+total_pk_delete_loss);
		System.out.println("total_pk_remain:"+(total_pk_new-total_pk_delete_ok-total_pk_delete_loss));
		double total_pk_delete = total_pk_delete_loss + total_pk_delete_ok;
		System.out.println("total_loss_ratio:"+total_pk_delete_loss/total_pk_delete);
		System.out.println("total_wating_time_av:"+total_wating_time_sum/total_pk_delete_ok);
		System.out.println("total_throughput:"+total_pk_delete_ok*PacketManager.fix_packet_size_bybit/(Simu.getTime()*1.0e3));//[Gbps]
	}
	
	private void printLossPath(){
		RoutingTable rtable = net.getRtable();
		ArrayList ri_list = rtable.getRi_list();

		RankingTable rank = new RankingTable();
		for(int i=0; i< ri_list.size(); i++){
			rank.add((double)total_pk_loss_path[i]/total_pk_delete_loss, ri_list.get(i));
		}
		rank.printNonZero();
		
	}

}
