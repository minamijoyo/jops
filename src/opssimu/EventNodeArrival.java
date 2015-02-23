package opssimu;

import simuframe.Event;
import opsnetwork.*;

public class EventNodeArrival extends Event {
	private OpsNode node;
	private OpsLink link;
	private int w;
	private Packet packet;
	public EventNodeArrival(double time, OpsNode node, OpsLink link, int w, Packet packet) {
		super(time);
		// TODO 自動生成されたコンストラクター・スタブ
		this.node = node;
		this.link = link;
		this.w = w;
		this.packet = packet;
	}

	public void onEvent() {
		// TODO 自動生成されたメソッド・スタブ
		node.onArrival(this);
	}

	public OpsLink getLink() {
		return link;
	}
	
	public int getW(){
		return w;
	}
	public Packet getPacket(){
		return packet;
	}

}
