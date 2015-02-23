package opsnetwork;

public class Packet {
	private long id;
	private double time;
	private int size_bybit;
	private int src;
	private int dst;
	private int group=0;
	private int w;
	private int hop=0;
	private RoutingInformation ri;
	
	public Packet(long id, double time, int size_bybit, int src, int dst) {
		this.id = id;
		this.time = time;
		this.size_bybit = size_bybit;
		this.src = src;
		this.dst = dst;
	}
	public int getDst() {
		return dst;
	}
	public long getId() {
		return id;
	}
	public int getSizeByBit() {
		return size_bybit;
	}
	public int getSrc() {
		return src;
	}
	public double getTime() {
		return time;
	}
	public int getGroup() {
		return group;
	}
	public void setGroup(int group) {
		this.group = group;
	}
	public int getW() {
		return w;
	}
	public void setW(int w) {
		this.w = w;
	}
	public int incHop(){
		return ++hop;
	}
	public int getHop(){
		return hop;
	}
	public RoutingInformation getRi() {
		return ri;
	}
	public void setRi(RoutingInformation ri) {
		this.ri = ri;
	}
	

}
