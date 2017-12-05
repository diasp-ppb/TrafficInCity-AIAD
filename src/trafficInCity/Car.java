package trafficInCity;



import com.vividsolutions.jts.geom.Point;

import environment.Junction;
import javafx.util.Pair;
import repast.simphony.space.gis.Geography;
import sajas.core.Agent;

public class Car extends Agent{
	protected Geography<? extends Car> space;
	protected Point finalPos;
	protected Pair<Junction, Junction> roadDirection;
	
	public Car(Geography<? extends Car> space, Point finalPos) {
		this.space = space;
		this.finalPos = finalPos;
	}
	
	public Point getFinalPos() {
		return finalPos;
	}
	
	public Point actualPos() {
		return space.getGeometry(this).getCentroid();
	}

}
