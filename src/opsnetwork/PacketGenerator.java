package opsnetwork;

import simuframe.RandomGenerator;

public abstract class PacketGenerator {
	protected int n_nodes;
	protected RandomGenerator rnd_node;
	
	public PacketGenerator(int n_nodes, long seed){
		this.n_nodes = n_nodes;
		rnd_node = new RandomGenerator(seed);
	}
	public abstract Packet packetNew(long id, double time, int size_bybit);

	public static PacketGenerator getGenerator(int traffic_mode, OpsNetwork net, int n_nodes, long seed) {
		PacketGenerator pgen;
		switch(traffic_mode){
			case 0:
				pgen = new PacketGeneratorRandom(n_nodes,seed);
				break;
			case 1:
				pgen = new PacketGeneratorTrafficMatrix(n_nodes,seed,net.getTraffic_matrix());
				break;
			default:
				throw new NetworkInitilizeException("unknown traffic_mode"+traffic_mode);
		}
		return pgen;
	}

}
