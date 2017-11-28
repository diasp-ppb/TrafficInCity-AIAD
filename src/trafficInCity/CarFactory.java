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
			
			while(road.hasNext() && i < numCars) {
				Road nextRoad = road.next();
				Car car = new Car(carProjection);
				ContextManager.addCarToContext(car);
				ContextManager.moveAgent(car, ContextManager.roadProjection.getGeometry(nextRoad).getCentroid());
				i++;
			}
		}
	}
}