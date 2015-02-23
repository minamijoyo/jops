package wbnetwork;

import java.util.ArrayList;

public class VlinkAssignImplSP2 extends VlinkAssignImplSP {

	public VlinkAssignImplSP2(WbNetwork net, int grid) {
		// TODO Auto-generated constructor stub
		super(net,grid);
	}

	public ArrayList assign() {
		// TODO 自動生成されたメソッド・スタブ
		ArrayList pathlist = getPathList();//demand first
		int max_w = net.max_w;
		boolean found;
		int vlink_id=0;
	
		for(int i=0; i< pathlist.size(); i++){
			//all found blank on the path are reserved
			//select next path
			do{
				found=false;
				ArrayList path = (ArrayList)pathlist.get(i);
				//find first blank
				int w_start = findNextAvailableBlank(path,0, max_w);
				//available wavelength not found
				if(w_start==-1){
					break;
				}
				//check consecutive blank
				int w_width = checkConsecutiveBlank(path, w_start, max_w);
				//assign
				set_used(path,w_start,w_width,vlink_id);
				vlinks.add(new Vlink(vlink_id++,path,w_start,w_width));
				
				found=true;
			}while(found);
		}
	
	
		return vlinks;
	}

}
