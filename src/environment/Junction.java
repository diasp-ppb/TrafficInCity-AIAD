package environment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.vividsolutions.jts.geom.Coordinate;
import trafficInCity.Car;


public class Junction implements Comparable<Junction> {
	public static int UniqueID = 0;
	private int id ;
	private Coordinate coord;
	private List<Road> roads; // The Roads connected to this Junction, used in GIS road network
    private double nodeWeight;
    private Junction previousNode;
	
	public Junction () {
		roads = new ArrayList<Road>();
		id = UniqueID;
		UniqueID++;
		nodeWeight = Double.MAX_VALUE;
	}
	
	public int getId() {
		return id;
	}
    /*
	@Override
	public String toString() {
		return "Junction "+this.id+": ("+this.coord.x+","+this.coord.y+")";
	}*/
	
	@Override
	public String toString() {
		return "Junction "+this.id+"";
	}
	
	
	public List<Road> getRoads() {
		return this.roads;
	}
	
	public void addRoad(Road road) {
		this.roads.add(road);
	}
	
	
	public Coordinate getCoords() {
		return coord;
	}
	
	public void setCoords(Coordinate c) {
		this.coord = c;	
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Junction)) {
			return false;
		}
		Junction j = (Junction) obj;
		return this.getCoords().equals(j.getCoords());
	}
	
	
	public void setNodeWeight(double weight) {
		nodeWeight = weight;
	}
	
	public double getNodeWeight() {
		return nodeWeight;
	}
	
	@Override
	public int compareTo(Junction o) {
		if (nodeWeight  < o.getNodeWeight())
			return -1;
		else if (nodeWeight > o.getNodeWeight())
			return 1;
		return 0;
	}
	
	
	public void setPreviousNode(Junction  node) {
		previousNode = node;
		System.out.println(previousNode);
	}
	
	public Junction getPreviousNode() {
		return previousNode;
	}

	
	
}
