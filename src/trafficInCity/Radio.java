package trafficInCity;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

import environment.Junction;
import environment.Road;
import jade.core.AID;
import jade.lang.acl.ACLMessage;
import javafx.util.Pair;
import main.ContextManager;
import repast.simphony.engine.schedule.ScheduledMethod;


public class Radio extends AgentTraffi{
	private RoadTrafficIntensity carTrafficInfo;
	private boolean a;

	public Radio () {
		carTrafficInfo = new RoadTrafficIntensity();
		a = true;
	}

	@ScheduledMethod(start = 1, interval = 1)
	public void updateAllRoadWeight() {
		Iterator<Road> roads = ContextManager.roadContext.getObjects(Road.class).iterator();

		int load;

		while(roads.hasNext()) {
			load = 0;

			Road current =  roads.next();
			Junction source = current.getJunctions().get(0);
			Junction target = current.getJunctions().get(1);

			
		    load = carTrafficInfo.getRoadLoad(new Pair<Junction, Junction>(source, target));
		    load = carTrafficInfo.getRoadLoad(new Pair<Junction, Junction>(source, target));
			 
			current.setLoad(load);
		}
		
		sendInfoToAllCars();
	}

	public void sendMessage(AID car, String message) {
		ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
		msg.setContent(message);
		msg.addReceiver(car);
		send(msg);
	}
	
	public synchronized void addIndexjunctionsCars(Pair<Junction,Junction> road) {
		carTrafficInfo.addIndexjunctionsCars(road);
	}
	
	public synchronized void subIndexjunctionsCars(Pair<Junction,Junction> road) {
		carTrafficInfo.subIndexjunctionsCars(road);
	}
	
	public void sendInfoToAllCars() {
		Iterator<AgentTraffi> cars = ContextManager.agentTraffiContext.getObjects(LowestTrafficCar.class).iterator();
		
		String msg = generateMessage();
		System.out.println("Radio: " + msg);
		
		while(cars.hasNext()) {
			LowestTrafficCar current = (LowestTrafficCar) cars.next();
			
			while(cars.hasNext()) {
				AID receiver = (AID) current.getAID();
				sendMessage(receiver, msg);
			}
		}
	}
	
	
	private String generateMessage() {
		HashMap<Pair<Junction,Junction>,Integer> carsInRoad = carTrafficInfo.getCarsInRoad();
		
		Set<Entry<Pair<Junction, Junction>, Integer>>  data = carsInRoad.entrySet();
		String message = "STATUS:";
		
		Iterator<Entry<Pair<Junction, Junction>, Integer>> iterator = data.iterator();
		
		while(iterator.hasNext()) {
			Entry<Pair<Junction,Junction>, Integer> roadLoad = iterator.next();
			
			Pair<Junction,Junction> road = roadLoad.getKey();
			Integer load = roadLoad.getValue();
			
			message +=" <Junction "+ road.getKey().getId() + 
					  "," 
				  	 +"Junction " + road.getValue().getId()+
				  	  ","
				  	 +load+">";
		}
		return message;
	}
}
