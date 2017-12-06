package trafficInCity;

import java.util.Iterator;

import environment.Junction;
import environment.Road;
import javafx.util.Pair;
import main.ContextManager;
import repast.simphony.engine.schedule.ScheduledMethod;

public class Radio extends AgentTraffi{
	private RoadTrafficIntensity carTrafficInfo;
	
	public Radio () {
		carTrafficInfo = new RoadTrafficIntensity();
	}
	
	@ScheduledMethod(start = 20, interval = 50)
	void updateAllRoadWeight() {
		Iterator<Road> roads = ContextManager.roadContext.getObjects(Road.class).iterator();
		
		int load;
		
		while(roads.hasNext()) {
			load = 0;
			
			Road current =  roads.next();
			Junction source = current.getJunctions().get(0);
			Junction target = current.getJunctions().get(1);
				
		    load += carTrafficInfo.getRoadLoad(new Pair<Junction, Junction>(source, target));
		    load += carTrafficInfo.getRoadLoad(new Pair<Junction, Junction>(source, target));
		
			current.setLoad(load);
		}
	}
}
