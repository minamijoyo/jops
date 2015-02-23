package wbnetwork;

public class WbMain {

	/**
	 * @param args
	 */
	
	public static void main(String[] args) {
		// TODO 自動生成されたメソッド・スタブ

		WbNetworkSetting nwset = new WbNetworkSetting(args);
		
		WbNetwork net = new WbNetwork(nwset);
		nwset.printSetting();
		System.out.println("\n[Result of WBS network]\n");
		
		net.init();
		
		net.printUsedLinkMatrix();
		net.printBlankHistrgam();
		System.out.println("\n[Output Topology Data for OPS network]\n");
		net.printVlinkTopology();
		net.printTrafficMatrix();

	}

}
