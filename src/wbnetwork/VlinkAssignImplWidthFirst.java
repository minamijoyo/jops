package wbnetwork;

import java.util.ArrayList;

public class VlinkAssignImplWidthFirst extends VlinkAssignImplSP {

	public VlinkAssignImplWidthFirst(WbNetwork net, int grid) {
		// TODO Auto-generated constructor stub
		super(net,grid);
	}

	public ArrayList assign() {
		// TODO 自動生成されたメソッド・スタブ
		ArrayList pathlist = getPathList();
		int max_w = net.max_w;
		boolean found;
		int vlink_id=0;
		//found largest blank on the path are reserved
		//select next path
		do{
			found=false;
			for(int i=0; i< pathlist.size(); i++){
				ArrayList path = (ArrayList)pathlist.get(i);
				int cur=0;
				int widest_s=-1;//w_start of widest waveband
				int widest_w=-1;//w_width of widest waveband
				while(cur<max_w){
					//find next blank
					int w_start = findNextAvailableBlank(path,cur,max_w);
					//next wavelength not found
					if(w_start==-1){
						break;
					}
					//check consecutive blank
					int w_width = checkConsecutiveBlank(path, w_start, max_w);
					if(widest_w < w_width){//widest_w update
						widest_w=w_width;
						widest_s=w_start;
					}
					cur=w_start+w_width;//goto next w;
				}
				//check widest_s is valid value
				if(widest_s==-1){
					//this path is not available
					//goto next path
					continue;
				}
				//assign
				set_used(path,widest_s,widest_w,vlink_id);
				vlinks.add(new Vlink(vlink_id++,path,widest_s,widest_w));
				
				found=true;
			}
		}while(found);
		
		return vlinks;
	}

}
