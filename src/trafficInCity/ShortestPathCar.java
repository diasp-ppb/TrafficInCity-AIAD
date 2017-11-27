package trafficInCity;

import com.vividsolutions.jts.geom.Point;

import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.space.gis.Geography;


public class ShortestPathCar extends Car {


	public ShortestPathCar (Geography<? extends Car> space) {
		super(space);
	}
	
	@ScheduledMethod (start = 1 , interval = 1)
	public void move() {
		System.out.println("oi");
//		int x = (int)finalPos.getX() - (int)actualPos().getX();
//		int y = (int)finalPos.getY() - (int)actualPos().getY();
//		
//		if(Math.abs(x)+Math.abs(y) != 0) {
//			NdPoint newPos = defineMovement(x, y);
//			updatePos(newPos);
//			moveInSpace();
//		}
	}
	
//	public NdPoint defineMovement(int x, int y) {
//		System.out.println("Actual: " + (int)actualPos().getX() + ", " + (int)actualPos().getY());
//		
//		if(Math.abs(x) > Math.abs(y))
//			return new NdPoint(grid.getLocation(this).getX()+(x/Math.abs(x)), grid.getLocation(this).getY());
//		else
//			return new NdPoint(grid.getLocation(this).getX(), grid.getLocation(this).getY()+(y/Math.abs(y)));
//		
//	}
}
