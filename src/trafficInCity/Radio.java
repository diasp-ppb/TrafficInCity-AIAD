package trafficInCity;

import java.util.Iterator;

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
	}

	@ScheduledMethod(start = 1, interval = 1000)
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
		
			Iterator<AgentTraffi> cars = ContextManager.agentTraffiContext.getObjects(Car.class).iterator();
			String msg = generateMessage();
			System.out.println("Radio: " + msg);
			
			while(cars.hasNext()) {
				Car c = (Car) cars.next();
				
				AID receiver = (AID) c.getAID();
				sendMessage(receiver, msg);
			}
	}
	
	private String generateMessage() {
		return carTrafficInfo.data();
	}
	
	
	public synchronized void addIndexjunctionsCars(Pair<Junction,Junction> road) {
		carTrafficInfo.addIndexjunctionsCars(road);
	}
	
	public synchronized void subIndexjunctionsCars(Pair<Junction,Junction> road) {
		carTrafficInfo.subIndexjunctionsCars(road);
	}
}
