package opsnetwork;

import simuframe.*;
import opssimu.*;

public class OpsNode extends Node {
	//casted object for better performance
	private OpsLink[] opsinlinks;
	private OpsLink[] opsoutlinks;
	private OpsNetwork opsnet;
	
	private boolean[][] outch_used;
	private int wc_used=0;
	private int wc_used_max;
	private int max_w;
	private RandomGenerator rnd_wave;
	private final double const_node_delay=0;//node delay = 0us
	
	private EventManager ev_manager = EventManager.getInstance();

	public OpsNodeResult result = new OpsNodeResult(this);

	public OpsNode(Network net, int id) {
		super(net, id);
		// TODO Auto-generated constructor stub
		max_w = net.getNwset().max_w;
		outch_used = new boolean[outlinks.length][max_w];
		rnd_wave = new RandomGenerator(net.getNwset().seed);
		
		//casted object for better performance
		opsinlinks = new OpsLink[inlinks.length];
		opsoutlinks = new OpsLink[outlinks.length];
		opsnet = (OpsNetwork)net;
		
		//number of wavelength converter
		wc_used_max=net.getNwset().wc;
		
	}
	
	public void addInlink(Link ln) {
		// TODO 自動生成されたメソッド・スタブ
		opsinlinks[n_inlinks] = (OpsLink) ln;
		super.addInlink(ln);
	}

	public void addOutlink(Link ln) {
		// TODO 自動生成されたメソッド・スタブ
		opsoutlinks[n_outlinks] = (OpsLink) ln;
		super.addOutlink(ln);
	}

	public void onArrival(Event e){
		//get packet from link
		EventNodeArrival ae = (EventNodeArrival)e;
		OpsLink link = ae.getLink();
		link.popPacket(ae.getW());
		
		//go to service
		service(ae.getPacket());
	}
	
	private void service(Packet p){
		//update hop field
		p.incHop();
		
		//check destination of packet
		if(p.getDst()==id){
			packetDeleteOk(p);//transmission success
			return;
		}
		//goto routing
		routing(p);
	}
	
	private void routing(Packet p){
		RoutingInformation ri = p.getRi();
		int hop = p.getHop();
		//select outport
		int outport=ri.getOutport(hop);
		
		//select wavelength
		int cur_w=p.getW();
		int[] w_list=ri.getW_list(hop);
		int[] w_bit=ri.getW_bit(hop);
		int w_width=ri.getW_width(hop);
		
		int next_w;
		boolean is_wc_used=false;
		
		if(!outch_used[outport][cur_w] && w_bit[cur_w]==1){
			//transmit same wavelength
			next_w = cur_w;
		}else{
			//wavelength coversion
			
			//check available wavelength convertor
			if(wc_used>=wc_used_max){
				//wavelength convertor is full
				result.pk_loss_wc++;
				packetDeleteLoss(p,opsoutlinks[outport]);//packet loss
				return;
			}
			//convert wavelength by RRR(Random Round Robin)
			//set start point by random
			int i= rnd_wave.nextInt(w_width);
			int j=0;
			for(;j<w_width;j++){
				if(!outch_used[outport][w_list[(j+i)%w_width]]) break;
			}
			if(j==w_width){
				//out channel is full
				packetDeleteLoss(p,opsoutlinks[outport]);//packet loss
				return;
			}
			next_w= w_list[(j+i)%w_width];			
			wc_used++;
			is_wc_used=true;
			if(result.max_wc < wc_used){
				result.max_wc = wc_used;
			}
		}
		p.setW(next_w);
		outch_used[outport][next_w]=true;
	
		//set departure event
		
		double departure_time = Simu.getTime()+ const_node_delay + p.getSizeByBit() / OpsLink.channel_speed	;
		ev_manager.setEvent(new EventNodeDeparture(departure_time,this,p,outport,is_wc_used));
	}
/*	Old version
	private void routing(Packet p){
		RoutingInformation ri = p.getRi();
		int hop = p.getHop();
		//select outport
		int outport=ri.getOutport(hop);
		
		//select wavelength
		int cur_w=p.getW();
		int w_start=ri.getW_start(hop);
		int w_width=ri.getW_width(hop);
		
		int next_w;
		boolean is_wc_used=false;
		
		if(!outch_used[outport][cur_w] && w_start <= cur_w && cur_w < w_start+w_width){
			//transmit same wavelength
			next_w = cur_w;
		}else{
			//wavelength coversion
			
			//check available wavelength convertor
			if(wc_used>=wc_used_max){
				//wavelength convertor is full
				result.pk_loss_wc++;
				packetDeleteLoss(p,opsoutlinks[outport]);//packet loss
				return;
			}
			//convert wavelength by RRR(Random Round Robin)
			//set start point by random
			int i= rnd_wave.nextInt(w_width);
			int j=0;
			for(;j<w_width;j++){
				if(!outch_used[outport][w_start+(j+i)%w_width]) break;
			}
			if(j==w_width){
				//out channel is full
				packetDeleteLoss(p,opsoutlinks[outport]);//packet loss
				return;
			}
			next_w=w_start+(j+i)%w_width;			
			wc_used++;
			is_wc_used=true;
			if(result.max_wc < wc_used){
				result.max_wc = wc_used;
			}
		}
		p.setW(next_w);
		outch_used[outport][next_w]=true;
	
		//set departure event
		
		double departure_time = Simu.getTime()+ const_node_delay + p.getSizeByBit() / OpsLink.channel_speed	+ opsoutlinks[outport].getVlinkOverhead();
		ev_manager.setEvent(new EventNodeDeparture(departure_time,this,p,outport,is_wc_used));
	}
*/	
	public void onDeparture(Event e){
		EventNodeDeparture de = (EventNodeDeparture)e;
		//set arrival event at adjacent node
		int outport = de.getOutport();
		OpsLink next_link = opsoutlinks[outport];
		OpsNode next_node = opsnet.getOpsNode(next_link.getTo());
		double arrival_time = e.getTime()+next_link.getTransDelay()+ opsoutlinks[outport].getVlinkOverhead();
		Packet p = de.getPacket();
		int w = p.getW();
		ev_manager.setEvent(new EventNodeArrival(arrival_time,next_node,next_link,w,p));
		
		//remove packet from out channel
		outch_used[outport][w]=false;
		if(de.getWc()){
			wc_used--;//current available number of wc
			result.use_wc++;//total number of used wc
		}
		result.pk_ok++;
		
		//push packet to link for statistics
		//Note that the packet instance is fowarded by EvenNodeArrival
		//in this implimentation because of the simulation performance.
		next_link.pushPacket(p,w);
	}
	
	
	public void packetNew(Packet p){
		boolean initilized=false;
		int loop=0;
		do{
			//set routing information to header
			RoutingInformation ri = rtable.getRoutingInformation(p.getSrc(), p.getDst());
			p.setRi(ri);
			
			int[] w_list=ri.getW_list(0);
			int w_width =ri.getW_width(0);
			int outport = ri.getOutport(0);
			
			//set wavelength by RRR(Random Round Robin)
			//set start point by random
			int i= rnd_wave.nextInt(w_width);
			int j=0;
			for(;j<w_width;j++){
				if(!outch_used[outport][w_list[(j+i)%w_width]]) break;
			}
			if(j==w_width){
				//out channel is full
				if(++loop > rtable.getRr_max(p.getSrc(), p.getDst())){
//				if(++loop > RoutingTable.wrr_max){
//				if(true){
					packetDeleteLoss(p,opsoutlinks[outport]);//packet loss
					return;
				}else{
					continue;
				}
			}
			int w=w_list[(j+i)%w_width];			
			
			p.setW(w);
			initilized=true;
		}while(!initilized);
		
		//go to routing
		routing(p);
	}
	
	private void packetDeleteOk(Packet p){
		opsnet.packetDeleteOk(p);
	}
	
	private void packetDeleteLoss(Packet p, OpsLink ln){
		result.pk_loss++;
		ln.informPacketLoss(p);
		opsnet.packetDeleteLoss(p);
	}
}
