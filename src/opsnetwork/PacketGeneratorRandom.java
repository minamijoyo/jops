package opsnetwork;

public class PacketGeneratorRandom extends PacketGenerator {

	public PacketGeneratorRandom(int n_nodes, long seed) {
		super(n_nodes, seed);
		// TODO 自動生成されたコンストラクター・スタブ
	}

	public Packet packetNew(long id, double time, int size_bybit) {
		// TODO 自動生成されたメソッド・スタブ
		int src = rnd_node.nextInt(n_nodes);
		int dst = rnd_node.nextInt(n_nodes);
		while(src==dst){//src must not equal dst
			dst = rnd_node.nextInt(n_nodes);
		}
		
		return new Packet(id,time,size_bybit,src,dst);
	
	}

}
