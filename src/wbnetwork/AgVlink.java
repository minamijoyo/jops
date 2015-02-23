package wbnetwork;

import java.util.ArrayList;

public class AgVlink extends Vlink{
	protected int[] w_bit;
	protected ArrayList vlinks = new ArrayList();
	public AgVlink(int id, Vlink ln, int max_w) {
		super(id,ln.path,-1,ln.w_width);
		w_bit= new int[max_w];
		setW_bit(ln.w_start,ln.w_width);
		vlinks.add(ln);
	}
	
	private void setW_bit(int w_start, int w_width){
		for(int i=0; i< w_width; i++){
			w_bit[w_start+i]=1;
		}
	}
	public void addVlink(Vlink ln){
		setW_bit(ln.w_start,ln.w_width);
		vlinks.add(ln);
		w_width+=ln.w_width;
	}
	public boolean isAddable(Vlink ln){
		if(this.from == ln.from && this.to == ln.to && this.path.size() == ln.path.size()){
			for(int i=0; i<this.path.size();i++){
				WbLink t = (WbLink)(this.path.get(i));
				WbLink l = (WbLink)(ln.path.get(i));
				if(t.id != l.id){
					return false;
				}
			}
		}else{
			return false;
		}
		return true;
	}
	
	

}
