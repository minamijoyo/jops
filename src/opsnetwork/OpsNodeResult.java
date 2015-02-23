package opsnetwork;

import simuframe.SimuResult;

public class OpsNodeResult extends SimuResult {
	public long pk_ok;
	public long pk_loss;
	public long pk_loss_wc;
	public long use_wc;
	public long max_wc;
	
	private OpsNode node;
	public OpsNodeResult(OpsNode node){
		SimuResult.addResultList(this);
		this.node=node;
	}
	
	public void printResult() {
		// TODO 自動生成されたメソッド・スタブ
		System.out.println("Node_id:"+node.getId()+"\tnode_loss:"+loss_ratio()+"\twc_loss:"+wc_loss_ratio()+"\twc_util:"+wc_ratio()+"\twc_max:"+max_wc);
	}

	public double wc_ratio(){
		return use_wc/(double)pk_ok;
	}
	public double loss_ratio(){
		return pk_loss/(double)(pk_ok+pk_loss);
	}
	public double wc_loss_ratio(){
		return pk_loss_wc/(double)(pk_loss);
	}
}
