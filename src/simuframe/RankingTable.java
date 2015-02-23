package simuframe;
import java.util.Collections;

import java.util.ArrayList;

public class RankingTable {

	private ArrayList table = new ArrayList();
	
	public void add(double score, Object obj){
		table.add(new ScoredObject(score,obj));
	}
	
	public void printNonZero(){
		Collections.sort(table);
		for(int i=0; i< table.size(); i++){
			ScoredObject sobj = (ScoredObject)table.get(i);
			if(sobj.score!=0){
				System.out.println(sobj.score+":\t"+sobj.obj);
			}
		}
	}
}
