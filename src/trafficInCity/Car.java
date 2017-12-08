package trafficInCity;

import java.util.Iterator;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Point;

import environment.Junction;
import jade.core.AID;
import javafx.util.Pair;
import main.ContextManager;
import repast.simphony.space.gis.Geography;

public class Car extends AgentTraffi {
	protected Geography<? extends AgentTraffi> space;
	protected Point finalPos;
	protected Pair<Junction, Junction> roadDirection;

	public Car(Geography<? extends AgentTraffi> space, Point finalPos) {
		super();
		this.space = space;
		this.finalPos = finalPos;
	}

	public Point getFinalPos() {
		return finalPos;
	}

	public Point actualPos() {
		return space.getGeometry(this).getCentroid();
	}

	public void moveAgentInStreet() {
		Coordinate i = new Coordinate(actualPos().getX(), actualPos().getY());
		Coordinate f = new Coordinate(finalPos.getX(), finalPos.getY());
		System.out.println("Actual: " + i);
		System.out.println("Final: " + f);

		if (i.equals(f)) {
			return;
		}

		Junction actJunction = ContextManager.getJunction(i);
		// Junction finalJunction = ContextManager.getJunction(f);

		Iterator<Junction> successors = ContextManager.streetNetwork.getSuccessors(actJunction).iterator();

		// reach destination
		if (!successors.hasNext()) {
			System.out.println("Exit");
			return;
		}

		while (successors.hasNext()) {
			Junction j = successors.next();
			if (j.getCoords().equals(f)) {
				ContextManager.moveAgent(this, ContextManager.junctionProjection.getGeometry(j).getCentroid());
				System.out.println("Final---: " + this.actualPos());
				return;
			}

			ContextManager.moveAgent(this, ContextManager.junctionProjection.getGeometry(j).getCentroid());
			System.out.println("Actual: " + this.actualPos());
		}

		System.out.println("fim do ciclo");
	}

	public boolean isSemaphoreAgent(AID senderAID) {
		Iterator<AgentTraffi> semaphores = ContextManager.agentTraffiContext.getObjects(Semaphore.class).iterator();

		while (semaphores.hasNext()) {
			Semaphore s = (Semaphore) semaphores.next();

			if (s.getAID().equals(senderAID))
				return true;
		}
		return false;
	}

	public boolean isRadioAgent(AID senderAID) {
		Iterator<AgentTraffi> radios = ContextManager.agentTraffiContext.getObjects(Radio.class).iterator();

		while (radios.hasNext()) {
			Radio r = (Radio) radios.next();

			if (r.getAID().equals(senderAID))
				return true;
		}
		return false;
	}

}
