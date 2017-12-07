package trafficInCity;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import environment.Junction;
import javafx.util.Pair;
import main.ContextManager;
import repast.simphony.space.graph.RepastEdge;

public class RoadTrafficIntensity {

	public HashMap<Pair<Junction, Junction>, Integer> carsInRoad = new HashMap<Pair<Junction, Junction>, Integer>();

	public RoadTrafficIntensity() {
		Iterator<RepastEdge<Junction>> junctions = ContextManager.streetNetwork.getEdges().iterator();

		while (junctions.hasNext()) {
			RepastEdge<Junction> junction = junctions.next();

			Junction source = junction.getSource();
			Junction target = junction.getTarget();

			carsInRoad.put(new Pair<Junction, Junction>(source, target), 0);
			carsInRoad.put(new Pair<Junction, Junction>(target, source), 0);
		}
	}

	public synchronized void addIndexjunctionsCars(Pair<Junction, Junction> road) {
		carsInRoad.put(road, carsInRoad.get(road)+ 1);
		//System.out.println(road + " +1" );

	}

	public synchronized void subIndexjunctionsCars(Pair<Junction, Junction> road) {
		carsInRoad.put(road, carsInRoad.get(road)- 1);
		//System.out.println(road + " -1" );
	}

	public HashMap<Pair<Junction, Junction>, Integer> getCarsInRoad() {
		return carsInRoad;
	}

	public int numberCars() {
		int number = 0;
		for (HashMap.Entry<Pair<Junction, Junction>, Integer> entry : carsInRoad.entrySet()) {
			number += entry.getValue();
		}
		return number;
	}
	
	public int getRoadLoad(Pair<Junction,Junction> road) {
		if(carsInRoad.containsKey(road))
		return carsInRoad.get(road);
		
		return 0;
	}
	
	public String data() {
		String data = "TRAFFIC INFO: ";
		
		Iterator<Entry<Pair<Junction, Junction>, Integer>> dataStorage = carsInRoad.entrySet().iterator();
		
		while(dataStorage.hasNext()) {
			Entry<Pair<Junction,Junction>, Integer> dataStatus = dataStorage.next();
			data += "<" + dataStatus.getKey().getKey() +","+ dataStatus.getKey().getValue() +","+ dataStatus.getValue() +">";
		}
		return data;
	}
}
