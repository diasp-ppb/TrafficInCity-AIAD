package trafficInCity;

import java.util.Random;

import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.space.SpatialMath;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;


public class ShortestPathCar extends Car {


	public ShortestPathCar (ContinuousSpace<Object> space, Grid<Object> grid, NdPoint finalPos) {
		super(space, grid, finalPos);
	}
	
	@ScheduledMethod (start = 1 , interval = 1)
	public void move() {
		int x = (int)finalPos.getX() - (int)actualPos().getX();
		int y = (int)finalPos.getY() - (int)actualPos().getY();
		
		if(Math.abs(x)+Math.abs(y) != 0) {
			NdPoint newPos = defineMovement(x, y);
			updatePos(newPos);
			moveInSpace();
		}
		
		RunEnvironment.getInstance().setScheduleTickDelay(20);
	}
	
	public NdPoint defineMovement(int x, int y) {
		System.out.println("Actual: " + (int)actualPos().getX() + ", " + (int)actualPos().getY());
		
		if(Math.abs(x) > Math.abs(y))
			return new NdPoint(grid.getLocation(this).getX()+(x/Math.abs(x)), grid.getLocation(this).getY());
		else
			return new NdPoint(grid.getLocation(this).getX(), grid.getLocation(this).getY()+(y/Math.abs(y)));
		
	}
}
