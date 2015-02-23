package simuframe;


public class WeightedRoundRobin {

	private int[] w;
	private int max_len;
	private int[] wrr;
	private int counter=0;
//	private RandomGenerator rnd = new RandomGenerator(0);
	public WeightedRoundRobin(int[] w, int max_len){
		this.w=w;
		this.max_len=max_len;
		wrr = new int[max_len];
		init();
	}

	public int getNext(){
		int r=counter++;
		if(counter==max_len){
			counter=0;
		}
		return wrr[r];
	}

/*
	public int getNext(){
		return wrr[rnd.nextInt(max_len)];
	}

	private void init2(){
		int sum=0;
		for(int i=0; i<w.length; i++){
			sum+=w[i];
		}
		max_len=sum;
		int count=0;
		for(int i=0; i<w.length; i++){
			for(int j=0; j<w[i]; j++){
				wrr[count++]=i;
			}
		}
	}
	*/
	private void init(){
		int max_weight = maxWeight(w);
		int gcd_weight = gcdAll(w);
		int x = max_weight;
		int i=0;
		while(i<max_len){
			for(int j=0; j<w.length; j++){
				if(w[j]>=x){
					wrr[i++]=j;
					if(i==max_len){
						break;
					}
				}
			}
			x-=gcd_weight;
			if(x<=0){
				x=max_weight;
				max_len=i;
				break;
			}
		}
	}
	
	private int gcd2(int a, int b)
	{
		int c;
		while ((c = a % b)!=0) {
			a = b;
			b = c;
		}
		return b;
	}
	
	private int gcdAll(int[] w){
		int g=w[0];
		for(int i=1; i< w.length; i++){
			g=gcd2(w[i],g);
		}
		return g;
	}
	
	private int maxWeight(int[] w){
		int max=0;
		for(int i=0; i< w.length; i++){
			if(w[i]>max){
				max=w[i];
			}
		}
		return max;
	}
	
}
