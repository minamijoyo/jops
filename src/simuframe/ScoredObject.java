package simuframe;

public class ScoredObject implements Comparable{
	public double score;
	public Object obj;
	
	public ScoredObject(double score, Object obj) {
		this.score = score;
		this.obj = obj;
	}
	
	public int compareTo(Object obj){
		ScoredObject sobj = (ScoredObject)obj;
		if(this.score < sobj.score){
			return -1;
		}else if(this.score > sobj.score){
			return 1;
		}else{
			return 0;
		}
	}
}
