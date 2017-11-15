package trafficInCity;

import java.io.Serializable;

import repast.simphony.space.SpatialMath;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.space.grid.Grid;
import sajas.core.Agent;

public class Car extends Agent{
	protected ContinuousSpace<Object> space;
	protected Grid<Object> grid;
	protected NdPoint initialPos;
	protected NdPoint finalPos;
	
	public Car(ContinuousSpace<Object> space, Grid<Object> grid, NdPoint finalPos) {
		this.space = space;
		this.grid = grid;
		this.finalPos = finalPos;
	}
	
	public void initiatePos(NdPoint initialPos) {
		this.initialPos = initialPos;
	}
	
	public NdPoint getInitialPos() {
		return initialPos;
	}
	
	public NdPoint getFinalPos() {
		return initialPos;
	}
	
	public NdPoint actualPos() {
		return space.getLocation(this);
	}
	
	public void updatePos(NdPoint newPos) {
		int[] pos = {(int) newPos.getX(), (int) newPos.getY()};
		grid.moveTo(this, pos);
	}
	
	public void moveInSpace() {
		NdPoint actualPos = space.getLocation(this);
		NdPoint nextPos = new NdPoint(grid.getLocation(this).getX(), grid.getLocation(this).getY());
		double angle = SpatialMath.calcAngleFor2DMovement(this.space, actualPos, nextPos);
		this.space.moveByVector(this, 1, angle, 0);
	}
}
