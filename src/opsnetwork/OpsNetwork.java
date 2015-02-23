package opsnetwork;
import opssimu.EventPacketNew;
import opssimu.OpsSimu;
import simuframe.*;

public class OpsNetwork extends Network {
	private OpsSimu simu;
	private PacketManager pk_manager;
	private EventManager ev_manager;
	
	private RandomGenerator rnd_pknew_time;
	
	//casted object for better performance
	private OpsNode[] opsnodes;
	private OpsLink[] opslinks;
	
	public OpsNetwork(OpsSimu simu, NetworkSetting nwset) {
		super(nwset);
		// TODO Auto-generated constructor stub
		this.simu = simu;
	}
	
	public void init() {
		// TODO Auto-generated method stub
		super.init();//Initilize Network
		
		//casted object for better performance
		setupCastedObject();
		
		//Create RandomGenerator
		rnd_pknew_time = new RandomGenerator(nwset.seed);
		
		//Create PacketManager
		PacketGenerator pgen = PacketGenerator.getGenerator(nwset.traffic_mode,this,nodes.length,nwset.seed);
		pk_manager = new PacketManager(this,pgen);
		//Get EventManager
		ev_manager = EventManager.getInstance();
		//Create first event
		ev_manager.setEvent(new EventPacketNew(Simu.getTime(),this));
	}

	private void setupCastedObject() {
		opsnodes = new OpsNode[nodes.length];
		for(int i=0; i<nodes.length; i++){
			opsnodes[i] = (OpsNode) nodes[i];
		}
		opslinks = new OpsLink[links.length];
		for(int i=0; i<links.length; i++){
			opslinks[i] = (OpsLink) links[i];
		}
	}

	protected Link newLink(Network net, int id, int from, int to, int w_start, int w_width,
			double cost, int group) {
		// TODO Auto-generated method stub
		return new OpsLink(net, id, from, to, w_start, w_width, cost, group);
	}
	protected Link newLink(Network net, int id, int from, int to, int[] w_bit, int w_width,
			double cost, int group) {
		// TODO Auto-generated method stub
		return new OpsLink(net, id, from, to, w_bit, w_width, cost, group);
	}

	protected Node newNode(Network net, int id) {
		// TODO Auto-generated method stub
		return new OpsNode(net, id);
	}
	
	public void onPacketNew(Event e){
		//Generate Packet
		Packet p = pk_manager.packetNew();
		/*
		if(p.getId()%1000000==0){
			System.out.println(p.getId());
		}*/
		if(p.getId()==nwset.total_packet){
			simu.end();
			return;
		}
		
		//Initilize at source node
		OpsNode n = opsnodes[p.getSrc()];
		n.packetNew(p);
		
		//Set next packet new event
		double next_gen_time = e.getTime()+rnd_pknew_time.nextExp(nwset.lambda);
		ev_manager.setEvent(new EventPacketNew(next_gen_time,this));
	}
	
	public void packetDeleteOk(Packet p){
		pk_manager.packetDeleteOk(p);
	}
	
	public void packetDeleteLoss(Packet p){
		pk_manager.packetDeleteLoss(p);
	}

	public OpsLink getOpsLink(int index) {
		return opslinks[index];
	}

	public OpsNode getOpsNode(int index) {
		return opsnodes[index];
	}

}
