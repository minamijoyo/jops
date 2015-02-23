package opsnetwork;

public class TrafficMatrix {
	private int n_nodes;
	private int [][] traffic_matrix;
	private int total_traffic;
	
	public TrafficMatrix(int[][] traffic_matrix){
		this.traffic_matrix = traffic_matrix;
		this.n_nodes = traffic_matrix.length;
		total_traffic = sumTraffic();
	}
	
	private int sumTraffic() {
		int result=0;
		for(int i=0; i< n_nodes; i++){
			for(int j=0; j< n_nodes; j++){
				result+=traffic_matrix[i][j];
			}
		}
		return result;
	}
	
	public int[][] getTraffic_matrix() {
		return traffic_matrix;
	}
	
	public int getTotalTraffic(){
		return total_traffic;
	}
	
	public double getNormarilizedTraffic(int src, int dst){
		return (double)traffic_matrix[src][dst]/total_traffic;
	}

}
