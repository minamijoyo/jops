package wbnetwork;

import java.util.ArrayList;

public class Vlink {
	protected int id;
	protected ArrayList path;
	protected int w_start;
	protected int w_width;
	
	protected int from;
	protected int to;
	protected double cost;
	
	public Vlink(int id, ArrayList path, int w_start, int w_width) {
		this.id = id;
		this.path = path;
		this.w_start = w_start;
		this.w_width = w_width;
		this.from = ((WbLink)path.get(0)).from;
		this.to = ((WbLink)path.get(path.size()-1)).to;
		this.cost=0;
		for(int i=0; i< path.size(); i++){
			this.cost += ((WbLink)path.get(i)).cost;
		}
	}		
}
