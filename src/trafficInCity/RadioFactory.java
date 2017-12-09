package trafficInCity;

import java.util.Random;

import environment.Junction;
import jade.wrapper.StaleProxyException;
import main.ContextManager;

import repast.simphony.context.Context;
import repast.simphony.space.gis.Geography;

public class RadioFactory {
	public void createAgents(Context<AgentTraffi> carContext, Geography<AgentTraffi> carProjection) {
		Junction junction = ContextManager.junctionContext.getRandomObject();
		Random rand = new Random();

		Radio radio = new Radio();
		ContextManager.addRadioToContext(radio);
		ContextManager.moveAgent(radio, ContextManager.junctionProjection.getGeometry(junction).getCentroid());

		try {
			ContextManager.mainContainer.acceptNewAgent("SPRadio" + rand.nextInt(Integer.MAX_VALUE), radio).start();
		} catch (StaleProxyException e) {
			e.printStackTrace();
		}
	}
}