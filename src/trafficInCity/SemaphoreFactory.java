package trafficInCity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import environment.Junction;
import jade.wrapper.StaleProxyException;
import main.ContextManager;

import repast.simphony.context.Context;
import repast.simphony.space.gis.Geography;

public class SemaphoreFactory {
	public ArrayList<Junction> locationSemaphors = new ArrayList<Junction>();

	public void createAgents(Context<AgentTraffi> semaphoreContext, Geography<AgentTraffi> semaphoreProjection) {
		int numSemaphors = 3;
		Random rand = new Random();

		for(int i = 0; i < numSemaphors; i++) {
			
			Junction junction = ContextManager.junctionContext.getRandomObject();
			Semaphore semaphore = new Semaphore(semaphoreProjection, ContextManager.junctionProjection.getGeometry(junction).getCentroid(), true, 50000);
			ContextManager.addSemaphoreToContext(semaphore);
			ContextManager.moveSemaphoreToPlace(semaphore, ContextManager.junctionProjection.getGeometry(junction).getCentroid());	
			locationSemaphors.add(junction);

			try {
				ContextManager.mainContainer.acceptNewAgent("SPSemaphore"+rand.nextInt(Integer.MAX_VALUE), semaphore).start();
			} catch (StaleProxyException e) {
				e.printStackTrace();
			}
		}

	}


	public ArrayList<Junction> getLocationSemaphors() {
		return locationSemaphors;
	}

}