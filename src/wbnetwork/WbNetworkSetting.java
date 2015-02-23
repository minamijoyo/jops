package wbnetwork;

import simuframe.SimuSetting;

public class WbNetworkSetting extends SimuSetting {
	protected String topology_file;
	protected int max_w;
	protected int total_traffic;
	protected int grid;
	protected long seed;
	protected int vlink_assign_alg;
	protected int wb_assign_alg;
	protected int agvlink_th;
	protected int multi_fiber;
	protected int fiber_margin;
	protected int vlink_add_hop_limit;
	
	public WbNetworkSetting(String[] args) {
		// TODO 自動生成されたコンストラクター・スタブ
		analyzeCmdParam(args);
	}
	
	public WbNetworkSetting() {
		// TODO 自動生成されたコンストラクター・スタブ
		String[] dummy=new String[1];
		analyzeCmdParam(dummy);
	}
	public boolean isValid() {
		// TODO 自動生成されたメソッド・スタブ
		if(!topology_file.endsWith(".txt")) return false;
		if(max_w<=0) return false;
		if(total_traffic<=0) return false;
		if(agvlink_th<=0) return false;
		if(grid<=0) return false;
		if(wb_assign_alg<0 || wb_assign_alg>1) return false;
		if(multi_fiber<=0) return false;
		if(vlink_assign_alg<0 || vlink_assign_alg>3) return false;
		if(vlink_add_hop_limit<0) return false;
		
		if(isMultiFiber()){
			if(vlink_assign_alg!=3) return false;
			if(wb_assign_alg!=1) return false;
		}
		
		return true;
	}
	
	public boolean isMultiFiber(){
		return (multi_fiber > 1);
	}

	private void analyzeCmdParam(String[] args){
		if(args.length!=7){
			System.out.println("Invalid args:"+args.length);
			System.out.println("Usage: topology_file total_traffic max_w grid fiber_margin seed vlink_add_hop_limit");
			throw new simuframe.InvalidSimuSettingException();
		}
	
		topology_file=args[0];
		
		total_traffic=Integer.parseInt(args[1]);
		
		max_w=Integer.parseInt(args[2]);//number of wavelength per fiber
		grid=Integer.parseInt(args[3]);//number of wavelength per grid
		multi_fiber=16;// number of fiber; multifiber mode must be larger than 1
		fiber_margin=Integer.parseInt(args[4]);//margin of making multi fiber
						//margin=0 offen fail WavabandAssign
		
		seed=Integer.parseInt(args[5]);
		if(multi_fiber==1){
			wb_assign_alg=0;//SP on Demand
		}else if(multi_fiber > 1){
			wb_assign_alg=1;//MultiFiber
		}else{
			wb_assign_alg=-1;//undifined
		}
//		nwset.vlink_assign_alg=0;//default
//		nwset.vlink_assign_alg=1;//explicit demand first
//		nwset.vlink_assign_alg=2;//demand->width
		vlink_assign_alg=3;//alt
		agvlink_th=grid;
		
		if(vlink_assign_alg==3){
			vlink_add_hop_limit=Integer.parseInt(args[6]);//enable alt mode only
		}
		
		if(!isValid()){
			throw new simuframe.InvalidSimuSettingException();
		}
	}
	
	public void printSetting(){
		System.out.println("[Setting Param in WBS network]");
		System.out.println("topology_file:"+topology_file);
		System.out.println("totoal_traffic:"+total_traffic);
		System.out.println("max_w:"+max_w);
		System.out.println("grid:"+grid);
		System.out.println("multi_fiber:"+multi_fiber);
		System.out.println("fiber_margin:"+fiber_margin);
		System.out.println("seed:"+seed);
		System.out.println("wb_assign_alg:"+wb_assign_alg);
		System.out.println("vlink_assign_alg:"+vlink_assign_alg);
		System.out.println("vlink_add_hop_limit:"+vlink_add_hop_limit);
		System.out.println("agvlink_th:"+agvlink_th);
	}

}
