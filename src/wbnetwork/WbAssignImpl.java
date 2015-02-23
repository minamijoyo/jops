package wbnetwork;
import java.util.ArrayList;

import simuframe.RandomGenerator;
public abstract class WbAssignImpl {
	protected WbNetwork net;
	protected ArrayList traffic;
	protected int grid;
	protected RandomGenerator rnd;
	protected boolean use_wc;
	
	public WbAssignImpl(WbNetwork net, ArrayList traffic, int grid, long seed, boolean use_wc){
		this.net=net;
		this.traffic=traffic;
		this.grid=grid;
		rnd = new RandomGenerator(seed);
		//Note that use_wc option valids only this class
		//and VlinkAssign class has not supported yet.
		this.use_wc = use_wc;
	}
	public abstract void assign();
	
	public static WbAssignImpl getImpl(int impl_type,WbNetwork net) {
		WbAssignImpl result;
		switch(impl_type){
			case 0:
				result = new WbAssignImplFirstFit(net,net.traffic,net.nwset.grid,net.nwset.seed,false);
				break;
			case 1:
				result = new WbAssignImplFirstFitMultiFiber(net,net.traffic,net.nwset.grid,net.nwset.seed,false);
				break;
			default:
				throw new RuntimeException("Unknown wb_assign_alg:"+impl_type);
		}
		return result;
	}
}
