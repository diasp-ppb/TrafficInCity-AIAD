package trafficInCity;

import main.ContextManager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Point;

import environment.Junction;
import environment.Road;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.space.gis.Geography;


public class ShortestPathCar extends Car {
	
	private int i;
	private List<Junction> route;

	public ShortestPathCar (Geography<? extends Car> space, Point finalPos) {
		super(space, finalPos);
		Coordinate f = new Coordinate(finalPos.getX(), finalPos.getY());
		i = 0;
		route = new ArrayList<Junction>();
		System.out.println("Final: " + f);
	}
	
	@ScheduledMethod (start = 1 , interval = 1)
	public void move() {
		if(i < route.size()) {
			ContextManager.moveAgent(this, ContextManager.junctionProjection.getGeometry(route.get(i)).getCentroid());
			i++;
			Coordinate a = new Coordinate(actualPos().getX(), actualPos().getY());
			System.out.println(a);
		}
	}
	
	public void moveAgentInStreet() {
		Coordinate i = new Coordinate(actualPos().getX(), actualPos().getY());
		Coordinate f = new Coordinate(finalPos.getX(), finalPos.getY());
		
		if (i.equals(f))
			return;
		
		Junction actJunction = ContextManager.getJunction(i);
		Junction finalJunction = ContextManager.getJunction(f);
		
		Iterator<Junction> successors = ContextManager.streetNetwork.getSuccessors(actJunction).iterator();
		
		if(!successors.hasNext())
			return;
			 
		while(successors.hasNext()) {
			Junction j = successors.next();
			if(j.getCoords().equals(f)) {
				ContextManager.moveAgent(this, ContextManager.junctionProjection.getGeometry(j).getCentroid());
				return;
			}
		}
		ContextManager.moveAgent(this, ContextManager.junctionProjection.getGeometry(ContextManager.streetNetwork.getRandomSuccessor(actJunction)).getCentroid());
		System.out.println("Actual: " + i);
	}
	
	public void runDFS() {
		Coordinate i = new Coordinate(actualPos().getX(), actualPos().getY());
		Coordinate f = new Coordinate(finalPos.getX(), finalPos.getY());
		
		if (i.equals(f))
			return;
		
		Junction actJunction = ContextManager.getJunction(i);
		Junction finalJunction = ContextManager.getJunction(f);
		
		List<Junction> visited = new ArrayList<Junction>();
		
		Stack<Junction> stack = new Stack<Junction>();
		stack.push(actJunction);
		visited.add(actJunction);
		
		while(!stack.isEmpty()) {
			Junction j = stack.peek();
			Iterator<Junction> successors = ContextManager.streetNetwork.getSuccessors(j).iterator();
	
			Junction unvisited = null;
			while(successors.hasNext()){
				unvisited = successors.next();
				if(!visited.contains(unvisited))
					break;
			}
			
			if(!visited.contains(unvisited)) {
				visited.add(unvisited);
				stack.push(unvisited);
				if(unvisited.equals(finalJunction)) {
					Stack<Junction> temp = new Stack<Junction>();
					while(!stack.isEmpty()) {
						temp.push(stack.peek());
						stack.pop();
					}
					
					while(!temp.isEmpty()) {
						route.add(temp.peek());
						temp.pop();
					}
					
					return;
				}
			} else {
				stack.pop();
			}
		}
	}
}
