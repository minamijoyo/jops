package wbnetwork;

import java.util.ArrayList;

public class VlinkAssignImplSP extends VlinkAssignImpl {
	
	public VlinkAssignImplSP(WbNetwork net, int grid) {
		// TODO 自動生成されたコンストラクター・スタブ
		super(net,grid);
	}

	public ArrayList assign() {
		// TODO 自動生成されたメソッド・スタブ
		ArrayList pathlist = getPathList();//demand first
		int max_w = net.max_w;
		boolean found;
		int vlink_id=0;
		//next found blank on the path are reserved
		//select next path
		do{
			found=false;
			for(int i=0; i< pathlist.size(); i++){
				ArrayList path = (ArrayList)pathlist.get(i);
				//find first blank
				int w_start = findNextAvailableBlank(path,0, max_w);
				//available wavelength not found
				if(w_start==-1){
					continue;
				}
				//check consecutive blank
				int w_width = checkConsecutiveBlank(path, w_start, max_w);
				//assign
				set_used(path,w_start,w_width,vlink_id);
				vlinks.add(new Vlink(vlink_id++,path,w_start,w_width));
				
				found=true;
			}
		}while(found);
		
		return vlinks;
	}

	protected int checkConsecutiveBlank(ArrayList path, int w_start, int max_w) {
		int w_width=1;
		for(int j=w_start+1; j<max_w; j++){
			if(is_blank(path,j)){
				w_width++;
			}else{
				break;
			}
		}
		return w_width;
	}

	protected int findNextAvailableBlank(ArrayList path, int check_start, int max_w) {
		int w_start=-1;
		for(int j=check_start; j<max_w; j++){
			if(is_available(path, j)){
				w_start=j;
				break;
			}
		}
		return w_start;
	}
	protected int findNextPrimitiveBlank(ArrayList path, int check_start, int max_w) {
		int w_start=-1;
		for(int j=check_start; j<max_w; j++){
			if(is_blank(path, j)){
				w_start=j;
				break;
			}
		}
		return w_start;
	}
/*	
	private ArrayList getPathList(WbNetwork net){
		ArrayList pathlist = new ArrayList();
		ArrayList[][] shortestpath = net.shortestpath;
		for(int i=0; i< shortestpath.length; i++){
			for(int j=0; j< shortestpath[i].length; j++){
				if(shortestpath[i][j]!=null){
					pathlist.add(shortestpath[i][j]);
				}
			}
		}
		return pathlist;
	}
*/
	protected ArrayList getPathList(){
		return net.wbpath;
	}
	
	private boolean is_blank(ArrayList path, int w){
		//check blank
		for(int i=0; i<path.size(); i++){
			WbLink ln = (WbLink)path.get(i);
			if(ln.w_used[w]!=-1) return false;
		}
		return true;
	}
	protected boolean is_available(ArrayList path, int w){
		//check blank
		if(!is_blank(path,w)) return false;
		
		// check constraint for "WaveBand must not be devided"
		for(int i=0; i<path.size(); i++){
			WbLink ln = (WbLink)path.get(i);
			int grid_start= ((int)(w/grid))*grid;
			Integer last_check =null;//chache of check result
			for(int j=0; j<grid; j++){
				int pathno=ln.w_used[grid_start+j];
				if(last_check!=null && last_check.intValue()==pathno){
					continue;//same pathno
				}
				
				if(!is_include_path(path,pathno)){
					return false;
				}else{
					last_check = new Integer(pathno);
				}
			}
		}
		return true;
	}
	
	protected void set_used(ArrayList path, int start, int width, int index){
		for(int i=0; i< path.size(); i++){
			WbLink ln = (WbLink)path.get(i);
			for(int j=0; j<width; j++){
				set_vlink_id(index, ln, start+j);
			}
		}
	}
	
	protected void set_used(ArrayList path, int[] w_bit, int index){
		for(int i=0; i< path.size(); i++){
			WbLink ln = (WbLink)path.get(i);
			for(int j=0; j<w_bit.length; j++){
				if(w_bit[j]==1){
					set_vlink_id(index, ln, j);
				}
			}
		}
	}

	private void set_vlink_id(int index, WbLink ln, int j) {
		ln.w_used[j]=index*-1-100;
	}
	
	protected boolean is_include_path(ArrayList parent_path, int child_path_no){
		ArrayList child_path;
		if(child_path_no == -1){
			return true;//not used
		}else if(child_path_no >=0){//wbpath
			child_path = (ArrayList)net.wbpath.get(child_path_no);
		}else{//vlink
			child_path = ((Vlink)vlinks.get((child_path_no+100)*-1)).path;
		}
		
		if(parent_path.size() < child_path.size()){
			return false;
		}
		
		if(parent_path==child_path){//same object reference
			return true;
		}
		boolean ret=false;
		int i=0;
		int j=0;
		while(i<child_path.size()){
			if(((WbLink)child_path.get(i)).id==((WbLink)parent_path.get(j)).id){//一致中
				ret=true;
				i++;
			}else if(ret){//一致中に中断
				ret=false;
				break;
			}
			j++;
			if(j==parent_path.size() && i!=child_path.size()){//parentの終点でchildに続きが存在
				ret=false;
				break;
			}
		}
		return ret;
	}

}
