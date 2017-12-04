package main;

import java.util.HashMap;
import java.util.Iterator;

import environment.Junction;
import javafx.util.Pair;
import repast.simphony.space.graph.RepastEdge;

public class RoadTrafficIntensity {

	public  HashMap<Pair<Junction,Junction>, Integer> carsInRoad = new HashMap<Pair<Junction,Junction>, Integer>();
	
	
	public RoadTrafficIntensity () {
			Iterator<RepastEdge<Junction>> junctions = ContextManager.streetNetwork.getEdges().iterator();
		
		while(junctions.hasNext()) {
			RepastEdge<Junction> junction = junctions.next();
			
			Junction source = junction.getSource();
			Junction target = junction.getTarget();
						
			carsInRoad.put(new Pair<Junction, Junction>(source, target), 0);
			carsInRoad.put(new Pair<Junction, Junction>(target, source), 0);
		}	
	}
	
	
	public synchronized void addIndexjunctionsCars(Pair<Junction,Junction> road) {	
		carsInRoad.merge(road, 1, Integer::sum);		
	}
	
	public synchronized void subIndexjunctionsCars(Pair<Junction,Junction> road) {
		carsInRoad.merge(road, -1, Integer::sum);		
	}
}
