package simuframe;

/**
 * @author m-morita
 *	This class is an abstract class for several Events
 */
public abstract class Event implements Comparable {
	protected static long id_counter=0;
	protected long id;
	protected double time;
	
	public Event(double time){
		this.id=id_counter++;
		this.time=time;
	}
	
	public long getId(){
		return id;
	}
	public double getTime(){
		return time;
	}
	
	//this abstract method is called when Event occurs.
	public abstract void onEvent();
	
	//Compare with other Event using time.
	//If both events have same time, then compare by id
	public int compareTo(Object o){
		double diff = this.time - ((Event)o).getTime();
		if(diff < 0) return -1;
		else if(diff > 0) return 1;
		else{
			long i=((Event)o).getId();
			if(this.id < i) return -1;
			else if(this.id > i) return 1;
			else return 0;
		}
	}

}
