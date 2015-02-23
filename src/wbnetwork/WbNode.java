package wbnetwork;

import java.util.ArrayList;

public class WbNode {
	protected WbNetwork net;
	protected int id;
	protected ArrayList outlinks;
	protected ArrayList inlinks;
	public WbNode(WbNetwork net, int id) {
		this.net = net;
		this.id = id;
		outlinks = new ArrayList();
		inlinks = new ArrayList();
	}
	
	public void addOutlinks(WbLink ln){
		outlinks.add(ln);
	}
	
	public void addInlinks(WbLink ln){
		inlinks.add(ln);
	}
	public String toString() {
		// TODO 自動生成されたメソッド・スタブ
		return "node:"+id;
	}

}
