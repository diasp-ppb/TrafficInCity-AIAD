package trafficInCity;


import main.ContextManager;

import repast.simphony.context.Context;
import repast.simphony.space.gis.Geography;

public class CarFactory {

	public void createAgents(Context<Car> context, Geography<Car> carProjection) {
		int numCars = 1;
		
		for(int i = 0; i < numCars; i++) {
			Car car = new Car(carProjection);
			ContextManager.addCarToContext(car);
			ContextManager.moveAgent(car);
		}
	}
	
}