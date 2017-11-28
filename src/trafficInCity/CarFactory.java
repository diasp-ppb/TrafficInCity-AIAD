package trafficInCity;

import java.util.Iterator;
import environment.Road;
import main.ContextManager;

import repast.simphony.context.Context;
import repast.simphony.space.gis.Geography;

public class CarFactory {

	public void createAgents(Context<Car> context, Geography<Car> carProjection) {
		int numCars = 15;
	
		for(int i = 0; i < numCars; i++) {
			Iterator<Road> road = ContextManager.roadContext.getRandomObjects(Road.class,numCars).iterator();
			Iterator<Road> roadF = ContextManager.roadContext.getRandomObjects(Road.class,numCars).iterator();
			
			while(road.hasNext() && i < numCars) {
				Road finalRoad = roadF.next();
				ShortestPathCar car = new ShortestPathCar(carProjection, ContextManager.roadProjection.getGeometry(finalRoad).getCentroid());
				ContextManager.addCarToContext(car);
				Road nextRoad = road.next();
				ContextManager.moveAgent(car, ContextManager.roadProjection.getGeometry(nextRoad).getCentroid());
				i++;
			}
		}
	}
}