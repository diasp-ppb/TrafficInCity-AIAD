package trafficInCity;

import java.util.Iterator;

import environment.Junction;
import main.ContextManager;

import repast.simphony.context.Context;
import repast.simphony.space.gis.Geography;

public class RadioFactory {
	public void createAgents(Context<AgentTraffi> carContext, Geography<AgentTraffi> carProjection) {
		int numRadios = 1;
	
		for(int i = 0; i < numRadios; i++) {
			Iterator<Junction> junction = ContextManager.junctionContext.getRandomObjects(Junction.class,numRadios).iterator();
			
			while(junction.hasNext() && i < numRadios) {
				Radio radio = new Radio();
				ContextManager.addRadioToContext(radio);
				Junction nextRoad = junction.next();
				ContextManager.moveAgent(radio, ContextManager.junctionProjection.getGeometry(nextRoad).getCentroid());
				i++;
			}
		}
		
	}

}