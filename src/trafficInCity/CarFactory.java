package trafficInCity;

import java.util.Random;

import environment.Junction;
import jade.wrapper.StaleProxyException;
import main.ContextManager;

import repast.simphony.context.Context;
import repast.simphony.space.gis.Geography;

public class CarFactory {
	public void createAgents(Context<AgentTraffi> carContext, Geography<AgentTraffi> carProjection) {
		int numShortestPathCars = 1;
		Random rand = new Random();

		for(int i = 0; i < numShortestPathCars; i++) {

			Junction finalJunction = ContextManager.junctionContext.getRandomObject();
			ShortestPathCar car = new ShortestPathCar(carProjection, ContextManager.junctionProjection.getGeometry(finalJunction).getCentroid());
			ContextManager.addCarToContext(car);
			Junction nextRoad = ContextManager.junctionContext.getRandomObject();
			ContextManager.moveAgent(car, ContextManager.junctionProjection.getGeometry(nextRoad).getCentroid());
			car.runDiskj();

			try {
				ContextManager.mainContainer.acceptNewAgent("SPCar"+rand.nextInt(Integer.MAX_VALUE), car).start();
			} catch (StaleProxyException e) {
				e.printStackTrace();
			}
		}
		
		int numLowesttrafficCars = 0;

		for(int i = 0; i < numLowesttrafficCars; i++) {

			Junction finalJunction = ContextManager.junctionContext.getRandomObject();
			LowestTrafficCar car = new LowestTrafficCar(carProjection, ContextManager.junctionProjection.getGeometry(finalJunction).getCentroid());
			ContextManager.addCarToContext(car);
			Junction nextRoad = ContextManager.junctionContext.getRandomObject();
			ContextManager.moveAgent(car, ContextManager.junctionProjection.getGeometry(nextRoad).getCentroid());
			car.runDiskj();

			try {
				ContextManager.mainContainer.acceptNewAgent("SPCar"+rand.nextInt(Integer.MAX_VALUE), car).start();
			} catch (StaleProxyException e) {
				e.printStackTrace();
			}
		}
	}
}