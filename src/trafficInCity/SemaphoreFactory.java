package trafficInCity;

import java.util.Iterator;
import environment.Road;
import main.ContextManager;

import repast.simphony.context.Context;
import repast.simphony.space.gis.Geography;

public class SemaphoreFactory {

	public void createAgents(Context<Semaphore> context, Geography<Semaphore> semaphoreProjection) {
		int numSemaphors = 15;
	
		for(int i = 0; i < numSemaphors; i++) {
			Iterator<Road> road = ContextManager.roadContext.getRandomObjects(Road.class,numSemaphors).iterator();
			
			while(road.hasNext() && i < numSemaphors) {
				Road roadNext = road.next();
				Semaphore semaphore = new Semaphore(semaphoreProjection, ContextManager.roadProjection.getGeometry(roadNext).getCentroid(), true, 20);
				ContextManager.addSemaphoreToContext(semaphore);
				i++;
			}
		}
	}
}