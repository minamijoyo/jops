package opssimu;

import simuframe.Event;
import opsnetwork.OpsNetwork;

public class EventPacketNew extends Event {
	private OpsNetwork net;
	public EventPacketNew(double time, OpsNetwork net) {
		super(time);
		// TODO �����������ꂽ�R���X�g���N�^�[�E�X�^�u
		this.net = net;
	}

	public void onEvent() {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		net.onPacketNew(this);
	}

}
