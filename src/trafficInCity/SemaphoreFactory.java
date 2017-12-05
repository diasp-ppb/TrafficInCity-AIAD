package trafficInCity;

import java.util.ArrayList;
import java.util.Iterator;

import environment.Junction;
import main.ContextManager;

import repast.simphony.context.Context;
import repast.simphony.space.gis.Geography;

public class SemaphoreFactory {
	public ArrayList<Junction> locationSemaphors = new ArrayList<Junction>();

	public void createAgents(Context<AgentTraffi> semaphoreContext, Geography<AgentTraffi> semaphoreProjection) {
		int numSemaphors = 3;
	
		for(int i = 0; i < numSemaphors; i++) {
			//Iterator<Road> road = ContextManager.roadContext.getRandomObjects(Road.class,numSemaphors).iterator();
			Iterator<Junction> junctionIterators = ContextManager.junctionContext.getRandomObjects(Junction.class,numSemaphors).iterator();
			
			while(junctionIterators.hasNext() && i < numSemaphors) {
				Junction junction = junctionIterators.next();
				Semaphore semaphore = new Semaphore(semaphoreProjection, ContextManager.junctionProjection.getGeometry(junction).getCentroid(), true, 20);
				ContextManager.addSemaphoreToContext(semaphore);
				ContextManager.moveSemaphoreToPlace(semaphore, ContextManager.junctionProjection.getGeometry(junction).getCentroid());	
				locationSemaphors.add(junction);
				i++;
			}
		}
		
	}

	
	public ArrayList<Junction> getLocationSemaphors() {
		return locationSemaphors;
	}

}