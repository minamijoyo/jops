package opssimu;

import simuframe.*;
import opsnetwork.NetworkSetting;

/**
 * @author m-morita
 * This class is entry point of the Java application.
 */
public class Main {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		NetworkSetting nwset=new NetworkSetting(args);
		SimuSetting setting = new OpsSimuSetting(nwset);
		SimuResult result = new OpsSimuResult();
		Simu sim = new OpsSimu(setting,result);
		
		nwset.printSetting();
		
		sim.init();
		sim.start();
		
		System.out.println("\n[Simulation Result in OPS network]\n");
		sim.printResult();
		
		System.out.println("Simulation finished:"+Simu.getTime());
		sim.cleanup();
	}
	

}
