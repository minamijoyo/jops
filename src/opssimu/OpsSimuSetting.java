package opssimu;

import simuframe.SimuSetting;
import opsnetwork.NetworkSetting;

public class OpsSimuSetting extends SimuSetting {
	public NetworkSetting nwset;
	
	public OpsSimuSetting(NetworkSetting nwset){
		this.nwset=nwset;
	}
	
	public boolean isValid() {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		return nwset.isValid();
	}

}
