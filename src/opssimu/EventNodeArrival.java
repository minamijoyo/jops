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
		// TODO �����������ꂽ�R���X�g���N�^�[�E�X�^�u
		this.node = node;
		this.link = link;
		this.w = w;
		this.packet = packet;
	}

	public void onEvent() {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
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
