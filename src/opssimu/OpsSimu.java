package opssimu;

import simuframe.*;
import opsnetwork.*;

public class OpsSimu extends Simu {
	private Network net;
	
	public OpsSimu(SimuSetting setting, SimuResult result){
		super(setting,result);
	}
	
	public void init(){
		super.init();
		
		OpsSimuSetting opsset = (OpsSimuSetting)setting;
		net = new OpsNetwork(this, opsset.nwset);
		
		net.init();
		System.out.println("Initilized OK.");
	}
}
