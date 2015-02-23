package opssimu;

import simuframe.Event;
import opsnetwork.OpsNode;
import opsnetwork.Packet;

public class EventNodeDeparture extends Event {
	private OpsNode node;
	private Packet packet;
	private int outport;
	private boolean wc;
	public EventNodeDeparture(double time, OpsNode node, Packet packet, int outport, boolean wc) {
		super(time);
		// TODO 自動生成されたコンストラクター・スタブ
		this.node = node;
		this.packet = packet;
		this.outport = outport;
		this.wc = wc;
	}

	public void onEvent() {
		// TODO 自動生成されたメソッド・スタブ
		node.onDeparture(this);
	}
	
	public Packet getPacket(){
		return packet;
	}
	
	public int getOutport(){
		return outport;
	}
	
	public boolean getWc(){
		return wc;
	}

}
