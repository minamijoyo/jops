package simuframe;

public class EventDebug extends Event {

	private String str;
	
	public EventDebug(double time, String str) {
		super(time);
		// TODO Auto-generated constructor stub
		this.str=str;
	}

	public void onEvent() {
		// TODO Auto-generated method stub
		System.out.println(this.toString() + "\t:" + time + "\t:" + id + "\t:" + str);
		
	}

}
