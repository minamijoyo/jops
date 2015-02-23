package opsnetwork;

import simuframe.SimuResult;
import simuframe.Simu;
public class OpsLinkResult extends SimuResult {

	public long pk_ok;
	public long pk_loss;
	private OpsLink ln;
	public OpsLinkResult(OpsLink ln){
		SimuResult.addResultList(this);
		this.ln=ln;
	}
	
	public void printResult() {
		// TODO 自動生成されたメソッド・スタブ
		System.out.println("Link_id:"+ln.getId()+"\tutil:"+utilization()+"\tlink_loss:"+loss_ratio()+"\tLink_info:"+ln);
	}
	
	public double loss_ratio(){
		return pk_loss/(double)(pk_ok+pk_loss);
	}
	public double utilization(){
		return pk_ok*PacketManager.fix_packet_size_bybit/((double)Simu.getTime()*OpsLink.channel_speed*ln.getW_width());
	}

}
