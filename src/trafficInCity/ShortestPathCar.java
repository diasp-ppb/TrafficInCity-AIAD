package trafficInCity;

import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.space.SpatialMath;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;


public class ShortestPathCar {

	private ContinuousSpace < Object > space ;
	private Grid < Object > grid ;

	public ShortestPathCar ( ContinuousSpace < Object > space , Grid < Object > grid ) {
		this.space = space ;
		this.grid = grid ;
	}
	
	@ScheduledMethod ( start = 1 , interval = 1)
	public void step () {
		// get the grid location of this Zombie
		GridPoint pt = grid.getLocation ( this );
		System.out.println(pt);
		
		GridPoint moveCar = new GridPoint(pt.getX()+1, pt.getY());
		
		moveTowards (moveCar);
	}
	
	public void moveTowards ( GridPoint pt ) {
		
			NdPoint myPoint = this.space.getLocation (this );
			NdPoint otherPoint = new NdPoint ( pt.getX() , pt.getY());
			double angle = SpatialMath.calcAngleFor2DMovement(this.space ,myPoint , otherPoint );
			this.space.moveByVector ( this , 1 , angle , 0);
			myPoint = this.space.getLocation ( this );
			this.grid.moveTo( this , ( int ) myPoint . getX () , ( int ) myPoint . getY ());
		}
}
