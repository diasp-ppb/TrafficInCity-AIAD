package trafficInCity;

import jade.core.AID;
import jade.lang.acl.ACLMessage;
import sajas.core.Agent;

public class AgentTraffi extends Agent {

	public AgentTraffi() {

	}

	public void sendMessage(AID car, String message) {
		ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
		msg.setContent(message);
		msg.addReceiver(car);
		send(msg);
	}

}
