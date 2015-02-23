package wbnetwork;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;

public class VlinkAssignImplAlt extends VlinkAssignImplSP {
	public VlinkAssignImplAlt(WbNetwork net, int grid) {
		// TODO 自動生成されたコンストラクター・スタブ
		super(net,grid);
	}

//ver 3.0	
	public ArrayList assign() {
		// TODO 自動生成されたメソッド・スタブ
		
		ArrayList sorted_traffic_list = getSortedTrafficList();
		boolean last_founds_cache[] = new boolean[sorted_traffic_list.size()];
		Arrays.fill(last_founds_cache, true);
		int vlink_id=0;
		int loop=0;
		do{
			loop++;
			for(int i=0; i< sorted_traffic_list.size(); i++){
				if(!last_founds_cache[i]){
					continue;
				}			
				TrafficDemand td=(TrafficDemand)sorted_traffic_list.get(i);
				VlinkAssignImplAltState best_st = findPath(td.from,td.to,i);
				if(best_st!=null){
					vlink_id = addVlink(vlink_id, best_st);
					last_founds_cache[i]=true;
				}else{
					last_founds_cache[i]=false;
				}
			}
			//System.out.println(""+loop+"loop end.");
			
		}while(isContainFound(last_founds_cache));
		
		//System.out.println("search end.");
		return vlinks;
		
	}
	
	private ArrayList getSortedTrafficList() {
		ArrayList traffic_list = new ArrayList(net.traffic);
		Collections.sort(traffic_list, new TrafficDemand.CompByDemand());
		return traffic_list;
	}
	
	private boolean isContainFound(boolean[] last_founds_cache){
		for(int i=0; i<last_founds_cache.length; i++){
			if(last_founds_cache[i]==true){
				return true;
			}
		}
		return false;
	}

	private VlinkAssignImplAltState findPath(int src, int dst, int no){
		ArrayList success = new ArrayList();
		int depth_limit = net.shortestpath[src][dst].size()+net.getNwset().vlink_add_hop_limit;
		
		VlinkAssignImplAltState.static_init();
		VlinkAssignImplAltState initial_state = new VlinkAssignImplAltState(src,net.max_w,net.nodes.length); 

		//System.out.println("demand:"+no+"src:"+src+" dst:"+dst+" limit:"+depth_limit);
		searchNext(initial_state,dst,depth_limit,success);
		//System.out.println("search result:"+(!success.isEmpty()));

		VlinkAssignImplAltState best_st=null;
		if(success.size()!=0){
			Collections.sort(success,new VlinkAssignImplAltState.CompByScore());
			best_st = (VlinkAssignImplAltState)success.get(0);
		}
		return best_st;
	}

	private int addVlink(int vlink_id, VlinkAssignImplAltState best_st) {
		ArrayList path = best_st.getPath();
		int[] w_bit = best_st.getCloneW_bit();
		
		//split to vlink
		ArrayList w_starts = new ArrayList();
		ArrayList w_widths = new ArrayList();
		splitW(w_starts,w_widths,w_bit);
		for(int j=0; j<w_starts.size(); j++){
			int w_start = ((Integer)w_starts.get(j)).intValue();
			int w_width = ((Integer)w_widths.get(j)).intValue();
//						//assign
			set_used(path,w_start,w_width,vlink_id);
			vlinks.add(new Vlink(vlink_id++,path,w_start,w_width));
		}
		return vlink_id;
	}

/*
	private int addVlink(int vlink_id, VlinkAssignImplAltState best_st) {
		ArrayList path = best_st.getPath();
		int w_start = best_st.getConsecutiveMaxStart();
		int w_width = best_st.getConsecutiveMaxWidth();
		//assign
		set_used(path,w_start,w_width,vlink_id);
		vlinks.add(new Vlink(vlink_id++,path,w_start,w_width));

		return vlink_id;
	}

*/
	//depth first search with sort and expand
	private void searchNext(VlinkAssignImplAltState st, int dst, int depth_limit, ArrayList result){
		//System.out.println("Dst:"+dst+" Cur:"+st.getCur_node()+" Depth:"+st.getDepth());
		
		//check depth
		if(st.getDepth()>net.nodes.length){
			//logical error: this path may be looped route
			throw new RuntimeException("Logical Error:Depth"+st.getDepth());
		}
		int cur_node=st.getCur_node();
		//enumerate all output node
		ArrayList outlinks = net.nodes[cur_node].outlinks;
		LinkedList expand_list = new LinkedList();
		for(int i=0; i<outlinks.size(); i++){
			WbLink ln = (WbLink)outlinks.get(i);
			int next_node=ln.to;
			//check duplicate node on the path
			if(!st.containNode(next_node)){
				//clone state and update
				VlinkAssignImplAltState clone_state = (VlinkAssignImplAltState)st.clone();
				clone_state.gotoNextNode(ln);
				clone_state.updateMask(ln);
				//cut branch if its score smaller than current best 
				if(clone_state.getScore() < VlinkAssignImplAltState.best_score){
					continue;
				}else{
					//check destination
					if(next_node==dst){
						//check consraint for WB
						if(checkConstraint(clone_state)){
							result.add(clone_state);//success
							//update best
							VlinkAssignImplAltState.best_score = clone_state.getScore();
						}
						continue;
					}else{
						//recurrence
						if(clone_state.getDepth() + getShortestHop(next_node, dst) -1 < depth_limit){
							expand_list.addLast(clone_state);
						}
					}
				}
			}
		}
		//sort and expand branch
		if(!expand_list.isEmpty()){
			Collections.sort(expand_list, new VlinkAssignImplAltState.CompByScore());
			while(!expand_list.isEmpty()){
				VlinkAssignImplAltState clone_state = (VlinkAssignImplAltState)expand_list.removeFirst();
				if(clone_state.getScore() < VlinkAssignImplAltState.best_score){
					continue;
				}else{
					searchNext(clone_state,dst,depth_limit,result);
				}
			}
		}
	}
	

	private boolean checkConstraint(VlinkAssignImplAltState st){
		ArrayList path = st.getPath();
		int[] w_bit_tmp = st.getCloneW_bit();
		int j=0;
		Integer last_check = null;
		while(j<w_bit_tmp.length){
			if(j%grid==0){
				last_check=null;//reset chache per grid
			}
			
			if(w_bit_tmp[j]==1){
				if(last_check != null){
					w_bit_tmp[j]=last_check.intValue();
				}else{
					if(is_available(path,j)){
						last_check = new Integer(1);
					}else{
						w_bit_tmp[j]=0;
						last_check = new Integer(0);
					}
				}
			}else{
				last_check=null;//reset chache
			}
			j++;
		}
		//update original data
		st.setW_bit(w_bit_tmp);
		return (st.getW_width()!=0);
	}
	
	private void splitW(ArrayList w_starts, ArrayList w_widths, int[] w_bit){
		int next_start=0;
		while(true){
			int start=-1;
			int width=1;
			for(int i=next_start; i<w_bit.length; i++){
				if(w_bit[i]==1){
					start=i;
					break;
				}
			}
			if(start==-1){
				return;
			}
			
			for(int i=start+1;i<w_bit.length;i++){
				if(w_bit[i]==1){
					width++;
				}else{
					break;
				}
			}
			w_starts.add(new Integer(start));
			w_widths.add(new Integer(width));
			next_start=start+width;
		}
	}
	
	private int getShortestHop(int src, int dst){
		return net.shortesthoppath[src][dst].size();
	}

}
