package wbnetwork;

import simuframe.RandomGenerator;
import wbnetwork.MakeTrafficImpl;

import java.util.ArrayList;
import java.util.Arrays;

public class MakeTrafficImplRandom extends MakeTrafficImpl {

	private RandomGenerator rnd;
	private int total_traffic;
	
	public MakeTrafficImplRandom(int total_traffic, long seed){
		this.total_traffic = total_traffic;
		rnd = new RandomGenerator(seed);
	}
	public ArrayList makeTraffic(int n_nodes) {
		// TODO 自動生成されたメソッド・スタブ
		int [][] demand = new int[n_nodes][n_nodes];
		int data_length = n_nodes*(n_nodes-1);
		int [] rnd_data = new int[data_length];
		ArrayList ret = new ArrayList();
		
		//generate random partition
		for(int i=0; i< data_length-1; i++){
			rnd_data[i]= rnd.nextInt(total_traffic);
		}
		rnd_data[data_length-1]=total_traffic;
		
		//sort
		Arrays.sort(rnd_data);
		
		//diff
		int[] diff = new int[data_length];
		diff[0]=rnd_data[0];
		for(int i=1; i< data_length; i++){
			diff[i]=rnd_data[i]-rnd_data[i-1];
		}
		
		//set traffic matrix
		int count=0;
		int check=0;
		for(int i=0; i< n_nodes; i++){
			for(int j=0; j< n_nodes; j++){
				if(i!=j){
					demand[i][j]=diff[count++];
					if(demand[i][j]!=0){
						ret.add(new TrafficDemand(i,j,demand[i][j]));
						check+=demand[i][j];
					}
				}else{
					demand[i][j]=0;
				}
			}
		}
		//for Debug
		if(check!=total_traffic){
			System.out.println("Debug:LogicalError@MakeTrafficImplRandom");
		}
		return ret;
	}

}
