package opsnetwork;

public class PacketGeneratorTrafficMatrix extends PacketGenerator {
	private int[] src_vec;
	private int[] dst_vec;
	private int traffic_matrix_sum;
	private TrafficMatrix traffic_matrix;
	public PacketGeneratorTrafficMatrix(int n_nodes, long seed, TrafficMatrix traffic_matrix) {
		super(n_nodes, seed);
		// TODO 自動生成されたコンストラクター・スタブ
		this.traffic_matrix=traffic_matrix;
		createWeightedRandomVector();
	}

	public Packet packetNew(long id, double time, int size_bybit) {
		// TODO 自動生成されたメソッド・スタブ
		int rnd = rnd_node.nextInt(traffic_matrix_sum);
		int src = src_vec[rnd];
		int dst = dst_vec[rnd];
		return new Packet(id,time,size_bybit,src,dst);
	}
	private void createWeightedRandomVector(){
		traffic_matrix_sum=traffic_matrix.getTotalTraffic();
		src_vec = new int[traffic_matrix_sum];
		dst_vec = new int[traffic_matrix_sum];
		int[][] tm=traffic_matrix.getTraffic_matrix();
		int cur=0;
		for(int i=0; i< n_nodes; i++){
			for(int j=0; j< n_nodes; j++){
				for(int k=0; k< tm[i][j]; k++){
					src_vec[cur]=i;
					dst_vec[cur]=j;
					cur++;
				}
			}
		}
		if(cur!=traffic_matrix_sum){
			throw new RuntimeException("Logical Error:TrafficMatrixInit"+traffic_matrix_sum+"!="+cur);
		}
	}
}
