package opssimu;

import simuframe.SimuSetting;
import opsnetwork.NetworkSetting;

public class OpsSimuSetting extends SimuSetting {
	public NetworkSetting nwset;
	
	public OpsSimuSetting(NetworkSetting nwset){
		this.nwset=nwset;
	}
	
	public boolean isValid() {
		// TODO 自動生成されたメソッド・スタブ
		return nwset.isValid();
	}

}
