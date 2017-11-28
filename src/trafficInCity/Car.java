package trafficInCity;

import com.vividsolutions.jts.geom.Point;

import repast.simphony.space.gis.Geography;
import sajas.core.Agent;

public class Car extends Agent{
	protected Geography<? extends Car> space;
	protected Point finalPos;
	
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
//	
//	public void moveInSpace(Point nextPos) {
//		Point actualPos = actualPos();
//		this.space.move(this, nextPos);;
//	}
}
