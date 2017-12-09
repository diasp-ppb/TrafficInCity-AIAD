package trafficInCity;

import java.util.Iterator;
import java.util.Random;

import environment.Junction;
import jade.wrapper.StaleProxyException;
import main.ContextManager;

import repast.simphony.context.Context;
import repast.simphony.space.gis.Geography;

public class CarFactory {
	public void createAgents(Context<AgentTraffi> carContext, Geography<AgentTraffi> carProjection) {
		int numShortestPathCars = 30;
		int numLowesttrafficCars = 1;

		Random rand = new Random();

		Junction finalJunction = ContextManager.junctionContext.getRandomObject();
		
		Iterator<Junction> junctions = ContextManager.junctionContext.getRandomObjects(Junction.class, numShortestPathCars + numLowesttrafficCars).iterator();
		
		for (int i = 0; i < numShortestPathCars; i++) {

			ShortestPathCar car = new ShortestPathCar(carProjection,
					ContextManager.junctionProjection.getGeometry(finalJunction).getCentroid());
			ContextManager.addCarToContext(car);
			ContextManager.moveAgent(car, ContextManager.junctionProjection.getGeometry(junctions.next()).getCentroid());
			car.runDiskj();

			try {
				ContextManager.mainContainer.acceptNewAgent("SPCar" + rand.nextInt(Integer.MAX_VALUE), car).start();
			} catch (StaleProxyException e) {
				e.printStackTrace();
			}
		}


		for (int i = 0; i < numLowesttrafficCars; i++) {

			LowestTrafficCar car = new LowestTrafficCar(carProjection,
					ContextManager.junctionProjection.getGeometry(finalJunction).getCentroid());
			ContextManager.addCarToContext(car);
			ContextManager.moveAgent(car, ContextManager.junctionProjection.getGeometry(junctions.next()).getCentroid());
			car.runDiskj();

			try {
				ContextManager.mainContainer.acceptNewAgent("SPCar" + rand.nextInt(Integer.MAX_VALUE), car).start();
			} catch (StaleProxyException e) {
				e.printStackTrace();
			}
		}
	}
}