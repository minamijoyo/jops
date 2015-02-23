package wbnetwork;

public class WbMain {

	/**
	 * @param args
	 */
	
	public static void main(String[] args) {
		// TODO �����������ꂽ���\�b�h�E�X�^�u

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
