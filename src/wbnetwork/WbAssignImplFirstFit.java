package wbnetwork;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import wbnetwork.TrafficDemand.CompByDemand;

public class WbAssignImplFirstFit extends WbAssignImpl{

	
	public WbAssignImplFirstFit(WbNetwork net, ArrayList traffic, int grid, long seed, boolean use_wc) {
		// TODO 自動生成されたコンストラクター・スタブ
		super(net,traffic,grid,seed,use_wc);
	}

	public void assign() {
		//This implementation does not support multi fiber mode.
		//if you need it, use WbAssignImplFirstFitMultiFiber class.
		
		// TODO 自動生成されたメソッド・スタブ
		ArrayList unassigned = new ArrayList(traffic);
		Collections.sort(unassigned, new CompByDemand());
		
		int max_w = net.max_w;
		net.wbpath = new ArrayList();
		int loop_count=0;
		int path_id=0;
		while(!unassigned.isEmpty()){
			loop_count++;
			//selected demand by Random
			//TrafficDemand target = (TrafficDemand)unassigned.get(rnd.nextInt(unassigned.size()));
			
			//selected demand by demand size
			TrafficDemand target = (TrafficDemand)unassigned.get(0);
			
			int found=-1;
			//ArrayList path = net.shortestpath[target.from][target.to];
			ArrayList path = WbRoutingImplSPonDemand.getRoute(net, target.from, target.to, target.demand, grid);
			int[] founds = new int[path.size()];
			Arrays.fill(founds, -1);
			
			//find first blank path without WC
			for(int i=0; i<max_w; i+=grid){
				if(is_available_path(path,i,target.demand)){
					found=i;
					Arrays.fill(founds, found);
					break;
				}
			}
			//not found without WC and
			//use wavelength convertor if use_wc = true
			if(found==-1 && use_wc){
				int last_wave=0;
				for(int i=0; i< path.size(); i++){
					WbLink ln = (WbLink) path.get(i);
					//try to consecutive wave
					if(ln.is_available_wb(last_wave,target.demand)){
						found=last_wave;
					}else{
						//wavelength conversion
						int new_wave = ln.find_free_wb(target.demand);
						if(new_wave!=-1){//found
							found=new_wave;
						}else{
							//not found
							found=-1;
							break;
						}
					}
					//update state and continue
					founds[i]=found;
					last_wave=found;
				}
			}
			//not found
			if(found==-1){
				//System.out.println("WbAssignError: Path:"+target.from+"->"+target.to+"@demand="+target.demand);
				if(loop_count==1000){
					throw new WaveAssignException(unassigned.toString());
				}
				//unassigned.remove(target);
			}else{
				//assign
				//int index=traffic.indexOf(target);
				int index=path_id++;
				set_used(path,founds,target.demand,index);
				net.wbpath.add(path);
				//remove target from unassigned
				unassigned.remove(target);
			}
		}
	}
	
	protected boolean is_available_path(ArrayList path, int start, int demand){
		for(int i=0; i< path.size(); i++){
			WbLink ln = (WbLink)path.get(i);
			for(int j=0; j<demand; j++){
				if(start+j>=ln.max_w) return false;
				if(ln.w_used[start+j]!=-1) return false;
			}
		}
		return true;
	}

	
	protected void set_used(ArrayList path, int[] start, int width, int index){
		for(int i=0; i< path.size(); i++){
			//for Debug
			if(start[i]==-1){
				throw new RuntimeException("Logical Error:"+"path="+path+",start="+start+",width="+width+",index="+index);
			}
			
			WbLink ln = (WbLink)path.get(i);
			for(int j=0; j<width; j++){
				ln.w_used[start[i]+j]=index;
			}
		}
	}
	
}
