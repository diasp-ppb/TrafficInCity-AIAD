package trafficInCity;

import java.util.Iterator;

import environment.Junction;
import environment.Road;
import main.ContextManager;

import repast.simphony.context.Context;
import repast.simphony.space.gis.Geography;

public class CarFactory {

	public void createAgents(Context<Car> context, Geography<Car> carProjection) {
		int numCars = 50;
	
		for(int i = 0; i < numCars; i++) {
			Iterator<Junction> junction = ContextManager.junctionContext.getRandomObjects(Junction.class,numCars).iterator();
			Iterator<Junction> junctionF = ContextManager.junctionContext.getRandomObjects(Junction.class,numCars).iterator();
			
			while(junction.hasNext() && i < numCars) {
				Junction finalJunction = junctionF.next();
				ShortestPathCar car = new ShortestPathCar(carProjection, ContextManager.junctionProjection.getGeometry(finalJunction).getCentroid());
				ContextManager.addCarToContext(car);
				Junction nextRoad = junction.next();
				ContextManager.moveAgent(car, ContextManager.junctionProjection.getGeometry(nextRoad).getCentroid());
				car.runDiskj();
				i++;
			}
		}
	}
}