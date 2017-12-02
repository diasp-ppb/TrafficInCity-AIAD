package trafficInCity;

import main.ContextManager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Point;

import environment.Junction;
import environment.Road;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.space.gis.Geography;


public class ShortestPathCar extends Car {
	
	private int i;
	private List<Coordinate> route;

	public ShortestPathCar (Geography<? extends Car> space, Point finalPos) {
		super(space, finalPos);
		Coordinate f = new Coordinate(finalPos.getX(), finalPos.getY());
		i = 0;
		route = new ArrayList<Coordinate>();
		System.out.println("Final: " + f);
	}
	
	@ScheduledMethod (start = 1 , interval = 1)
	public void move() {
		if(i < route.size()) {
			double lon = (route.get(i).y - actualPos().getY());
			double dx = Math.cos(actualPos().getX()) * Math.sin(route.get(i).x) - Math.sin(actualPos().getX()) * Math.cos(route.get(i).x) * Math.cos(lon);
			double dy = Math.sin(lon) * Math.cos(route.get(i).y);
			double ang = Math.atan2(dy, dx);
			
			ang = ((Math.PI * 2) + ang) % (Math.PI * 2);
			ang = (Math.PI * 2) - ang;
			
			System.out.println(ang);
			ContextManager.moveAgentByVector(this, 0.0001, ang);
			
			if(((int)(ang * 10000)) == 31415 || ((int)(ang * 10000)) == 62831 || ((int)(ang * 10000)) == 15707 || ((int)(ang * 10000)) == 47123)
				i++;
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
					
					List<Junction> juncts = new ArrayList<Junction>();
					while(!temp.isEmpty()) {
						juncts.add(temp.peek());
						temp.pop();
					}
					
					defineRoute(juncts);
					return;
				}
			} else {
				stack.pop();
			}
		}
	}
	
	public void runBFS() {
		Coordinate i = new Coordinate(actualPos().getX(), actualPos().getY());
		Coordinate f = new Coordinate(finalPos.getX(), finalPos.getY());
		
		if (i.equals(f))
			return;
		
		Junction actJunction = ContextManager.getJunction(i);
		Junction finalJunction = ContextManager.getJunction(f);
		
		Queue<Junction> queue = new LinkedList<Junction>();
		List<Junction> visited = new ArrayList<Junction>();
		Stack<Junction> pathStack = new Stack<Junction>();
		ArrayList<Junction> shortestPathList = new ArrayList<Junction>();

		queue.add(actJunction);
		visited.add(actJunction);
		pathStack.add(actJunction);
		
		while(! queue.isEmpty()) {
			Junction j = queue.poll();
			Iterator<Junction> successors = ContextManager.streetNetwork.getSuccessors(j).iterator();
			
			while(successors.hasNext()) {
				Junction n = successors.next();
				
				if(!visited.contains(n)) {
					queue.add(n);
					visited.add(n);
					pathStack.add(n);
					if(j.equals(finalJunction))
						break;
				}
			}
		}
		Junction node, currSrc = finalJunction;
		shortestPathList.add(finalJunction);
		
		while(!pathStack.isEmpty()) {
			node = pathStack.pop();
			Iterator<Junction> succ = ContextManager.streetNetwork.getSuccessors(currSrc).iterator();
			Boolean fin = false;
			
			while(succ.hasNext()) {
				Junction ao = succ.next();
				
				if(ao.equals(node)) {
					shortestPathList.add(0, node);
					currSrc = node;
					if(node.equals(actJunction)) {
						fin = true;
					}
					break;
				}
			}
			if(fin)
				break;
		}
		defineRoute(shortestPathList);
	}
	
	public void defineRoute(List<Junction> junctions) {
		for(int i = 1; i < junctions.size(); i++) {
			Iterator<Road> roads = ContextManager.roadContext.getObjects(Road.class).iterator();
			Road r = null;
			while(roads.hasNext()) {
				r = roads.next();
				if (r.getJunctions().contains(junctions.get(i-1)) && r.getJunctions().contains(junctions.get(i))) {
					break;
				}
			}
			Coordinate[] coords = ContextManager.roadProjection.getGeometry(r).getCoordinates();
			if(coords[0].equals(junctions.get(i-1).getCoords())) {
				for (int j = 0; j < coords.length; j++) {
					route.add(coords[j]);
				}
			} else {
				for (int j = coords.length-1; j >= 0; j--) {
					route.add(coords[j]);
				}
			}
		}
	}
}
