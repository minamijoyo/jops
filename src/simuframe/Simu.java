package simuframe;

/**
 * @author m-morita
 * This class controls an event simulation mechanism.
 * Users must call init() method before calling start() method.
 */
public class Simu {
	protected boolean running = false;
	protected EventManager event_manager = EventManager.getInstance();
	protected SimuSetting setting;
	protected SimuResult result;
	
	protected static double time = 0;
	
	public Simu(SimuSetting setting, SimuResult result){
		this.setting = setting;
		this.result = result;
	}
	
	public SimuSetting getSetting(){
		return setting;
	}
	
	public static double getTime(){
		return time;
	}

	public void init(){
		if(!setting.isValid()) throw new InvalidSimuSettingException();
		time = 0;
		running = false;
	}
	
	/**
	 * This method is a main loop of the simulation.
	 */
	public void start(){
		running = true;
		while(running){
			if(!event_manager.isEmpty()){
				//get next event
				Event e = event_manager.getEvent();
				time = e.getTime();
				//System.out.println(e);
				
				//invoke its event hadler
				e.onEvent();
			}else{
				System.out.println("Simu.start():Event table is empty.");
				end();
			}
		}
	}
	public void end(){
		running = false;
	}
	
	/**
	 * Print simulation results to stdin.
	 */
	public void printResult(){
		result.printResult();
	}
	
	public void cleanup(){
		time = 0;
		running = false;
	}
}
