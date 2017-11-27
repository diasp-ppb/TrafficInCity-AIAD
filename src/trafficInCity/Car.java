package trafficInCity;

import java.io.Serializable;

import com.vividsolutions.jts.geom.Point;

import repast.simphony.space.SpatialMath;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.space.gis.Geography;
import repast.simphony.space.grid.Grid;
import sajas.core.Agent;

public class Car extends Agent{
	protected Geography<? extends Car> space;
	protected Point finalPos;
	
	public Car(Geography<? extends Car> space) {
		this.space = space;
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
