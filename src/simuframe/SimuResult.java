package simuframe;
import java.util.ArrayList;

public abstract class SimuResult {

	protected static ArrayList resultlist = new ArrayList();
	
	public static void addResultList(SimuResult r){
		resultlist.add(r);
	}
	public static void printResultList() {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		for(int i=0; i< resultlist.size(); i++){
			((SimuResult)resultlist.get(i)).printResult();
		}
	}
	public abstract void printResult();	
}
