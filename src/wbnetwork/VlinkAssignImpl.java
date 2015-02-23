package wbnetwork;

import java.util.ArrayList;

public abstract class VlinkAssignImpl {
	protected WbNetwork net;
	protected ArrayList vlinks;
	protected int grid;
	
	public VlinkAssignImpl(WbNetwork net, int grid){
		this.net=net;
		this.grid=grid;
		this.vlinks = new ArrayList();
	}
	public abstract ArrayList assign();

	public static VlinkAssignImpl getImpl(int impl_type, WbNetwork net) {
		VlinkAssignImpl result;
		switch(impl_type){
			case 0:
				result=new VlinkAssignImplSP(net,net.nwset.grid);
				break;
			case 1:
				result=new VlinkAssignImplSP2(net,net.nwset.grid);
				break;
			case 2:
				result=new VlinkAssignImplWidthFirst(net,net.nwset.grid);
				break;
			case 3:
				result=new VlinkAssignImplAlt(net,net.nwset.grid);
				break;
			default:
				throw new RuntimeException("Unknown vlink_assign_alg:"+impl_type);
		}
		return result;
	}


}
