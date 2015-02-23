package opssimu;

import simuframe.Event;
import opsnetwork.OpsNetwork;

public class EventPacketNew extends Event {
	private OpsNetwork net;
	public EventPacketNew(double time, OpsNetwork net) {
		super(time);
		// TODO 自動生成されたコンストラクター・スタブ
		this.net = net;
	}

	public void onEvent() {
		// TODO 自動生成されたメソッド・スタブ
		net.onPacketNew(this);
	}

}
