package trafficInCity;

import main.ContextManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Stack;
import java.util.Vector;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Point;

import environment.Junction;
import environment.Road;
import javafx.util.Pair;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.space.gis.Geography;


public class ShortestPathCar extends Car {
	
	private int atualIndexInJunction;
	private int atualIndex;
	private List<Pair<Junction, Vector<Coordinate>>> route;

	public ShortestPathCar (Geography<? extends Car> space, Point finalPos) {
		super(space, finalPos);
		Coordinate f = new Coordinate(finalPos.getX(), finalPos.getY());
		atualIndexInJunction = 0;
		atualIndex = 0;
		route = new ArrayList<Pair<Junction, Vector<Coordinate>>>();
	}
	
	@ScheduledMethod (start = 1 , interval = 1)
	public void move() {
		
		if(atualIndex < route.size()-1) {
			
			Vector<Coordinate> coordsJuntion = route.get(atualIndex).getValue();	
			
			if(atualIndexInJunction < coordsJuntion.size()) {
				
				Coordinate point = coordsJuntion.get(atualIndexInJunction);
			
			
			double lon = (point.y - actualPos().getY());
			double dx = Math.cos(actualPos().getX()) * Math.sin(point.x) - Math.sin(actualPos().getX()) * Math.cos(point.x) * Math.cos(lon);
			double dy = Math.sin(lon) * Math.cos(point.y);
			double ang = Math.atan2(dy, dx);
			
			ang = ((Math.PI * 2) + ang) % (Math.PI * 2);
			ang = (Math.PI * 2) - ang;
			
			//System.out.println(ang);
			ContextManager.moveAgentByVector(this, 0.0002, ang);
			
			int angl = (int) (ang * 10000);
			int pi = (int) (Math.PI *10000);
			
			if((angl == pi) || angl == 2*pi || angl == (int)(pi/2) || angl== (int)(3*pi/2)) {
				
				Junction newsourceJunction = route.get(atualIndex).getKey();
				Junction newtargetJunction =  route.get(atualIndex + 1).getKey();
				ContextManager.carsInRoad.addIndexjunctionsCars(new Pair<Junction,Junction>(newsourceJunction, newtargetJunction ));
				
				if(atualIndex > 0) {
					Junction oldsourceJunction = route.get(atualIndex - 1).getKey(); 
					Junction oldtargetJunction = newsourceJunction;
					
					ContextManager.carsInRoad.subIndexjunctionsCars(new Pair<Junction,Junction>(oldsourceJunction, oldtargetJunction ));
					
				}
				
				if(route.get(atualIndex).getValue().size() == atualIndexInJunction+1) {
					atualIndex++;
					atualIndexInJunction = 0;
				}
				else
					atualIndexInJunction++;
				
			}
		}
		}
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
		//Junction finalJunction = ContextManager.getJunction(f);
		
		Iterator<Junction> successors = ContextManager.streetNetwork.getSuccessors(actJunction).iterator();
		
		// reach destination
		if(!successors.hasNext()) {
			System.out.println("Exit");
			return;
		}
			 
		while(successors.hasNext()) {
			Junction j = successors.next();
			if(j.getCoords().equals(f)) {
				ContextManager.moveAgent(this, ContextManager.junctionProjection.getGeometry(j).getCentroid());
				System.out.println("Final---: " + this.actualPos());
				return;
			}
		
		ContextManager.moveAgent(this, ContextManager.junctionProjection.getGeometry(j).getCentroid());
		System.out.println("Actual: " + this.actualPos());
		}
		
		System.out.println("fim do ciclo");
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
	
	public void runDiskj() {
		Coordinate i = new Coordinate(actualPos().getX(), actualPos().getY());
		Coordinate f = new Coordinate(finalPos.getX(), finalPos.getY());
		
		if (i.equals(f))
			return;
		
		Junction actJunction = ContextManager.getJunction(i);
		Junction finalJunction = ContextManager.getJunction(f);
		
		
		
		
	}
	
	public void defineRoute(List<Junction> junctions) {
	
		
		for(int i = 1; i < junctions.size(); i++) {
			Vector<Coordinate> coordsRoad = new Vector<Coordinate>();
			Iterator<Road> roads = ContextManager.roadContext.getObjects(Road.class).iterator();
			Road r = null;
			Junction sourceJunction = junctions.get(i-1);
			Junction targetJunction = junctions.get(i);
			
			while(roads.hasNext()) {
				r = roads.next();
				if (r.getJunctions().contains(sourceJunction) && r.getJunctions().contains(targetJunction)) {
					break;
				}
			}
			Coordinate[] coords = ContextManager.roadProjection.getGeometry(r).getCoordinates();
			if(coords[0].equals(junctions.get(i-1).getCoords())) {
				for (int j = 0; j < coords.length; j++) {
					coordsRoad.addElement(coords[j]);
				}
			} else {
				for (int j = coords.length-1; j >= 0; j--) {
					coordsRoad.addElement(coords[j]);
				}
			}
			route.add(new Pair<Junction, Vector<Coordinate>>(sourceJunction, coordsRoad));
		}
		route.add(new Pair<Junction, Vector<Coordinate>>(junctions.get(junctions.size()-1), new Vector<Coordinate>() ));
	}
}
