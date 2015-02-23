package simuframe;

import edu.emory.mathcs.backport.java.util.*;

/**
 * @author m-morita
 * This class maneges an event table.
 * The instance of EventManeger is sigleton.
 * getInstance() method returns its instance.
 */
public class EventManager {
	private PriorityQueue table = new PriorityQueue();
	
	/**
	 * instance of EventManager
	 */
	private static EventManager instance= new EventManager();
	
	private EventManager(){}
	
	public static EventManager getInstance(){
		return instance;
	}
	public void setEvent(Event e){
		table.add(e);
	}
	
	public Event getEvent(){
		return (Event)(table.poll());
	}
	
	public boolean isEmpty(){
		return table.isEmpty();
	}
	

}
