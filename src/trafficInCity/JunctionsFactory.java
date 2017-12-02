package trafficInCity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import environment.Junction;
import main.ContextManager;
import repast.simphony.context.Context;
import repast.simphony.space.gis.Geography;
import repast.simphony.space.graph.RepastEdge;
import repast.simphony.util.collections.IndexedIterable;

public class JunctionsFactory {

	public void createjunctionsCars() {

		Iterator<RepastEdge<Junction>> junctions = ContextManager.streetNetwork.getEdges().iterator();
		
		while(junctions.hasNext()) {
			RepastEdge<Junction> junction = junctions.next();
			
			Junction source = junction.getSource();
			Junction target = junction.getTarget();
			
			HashMap<Junction,Junction> pair= new HashMap <Junction,Junction>();
			pair.put(source, target);
			
			ContextManager.JunctionCars.put(pair, 0);
			
			//System.out.println(junction.toString());
			//System.out.println(pair.values());
			
			if(junctions.next().isDirected()) {
				
				HashMap<Junction,Junction> pair2 = new HashMap <Junction,Junction>();
				pair2.put(target, source);
				
				ContextManager.JunctionCars.put(pair2, 0);
				//System.out.println(pair2.values());
			}	
			//System.out.println();
		}
		//System.out.println();
		//System.out.println();
		//System.out.println(ContextManager.JunctionCars);
		
	}
}
