package environment;


import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.formula.functions.T;

import repast.simphony.space.graph.RepastEdge;
import environment.Road;

public class NetworkEdge<T> extends RepastEdge<T> {
	
	private List<String> access = new ArrayList<String>();
	
	private boolean majorRoad = false;
	
	private Road road;
	
	
	public NetworkEdge(T source, T target, boolean directed, double weight, List<String> initialAccess) {
		super(source, target, directed, weight);
		if (initialAccess != null) {
			this.access.addAll(initialAccess);
		}
	}
	
	@Override
	public double getWeight() {
		return super.getWeight() / this.getSpeed();
	}

	//TODO
	public double getSpeed() {
		return 1;
	}
	
	public List<String> getTypes() {
		return this.access;
	}
	
	public void addType(String type) {
		this.access.add(type);
	}
	
	public void setMajorRoad(boolean majorRoad) {
		this.majorRoad = majorRoad;
	}
	
	public Road getRoad() {
		return road;
	}
	
	public void setRoad(Road road) {
		this.road = road;
	}
	
	@Override
	public String toString() {
		return "Edge between " + this.getSource() + "->" + this.getTarget() + " accessible by "
				+ this.access.toString() + (this.majorRoad ? " (is major road)" : "");
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof NetworkEdge))
			return false;
		NetworkEdge<?> e = (NetworkEdge<?>) obj;
		return e.source.equals(this.source) && e.target.equals(this.target);
	}
	
	@Override
	public int hashCode() {
		return this.road.hashCode(); 
	}

}
