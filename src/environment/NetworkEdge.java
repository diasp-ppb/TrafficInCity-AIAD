package environment;

import org.apache.poi.ss.formula.functions.T;

import repast.simphony.space.graph.RepastEdge;
import environment.Road;

@SuppressWarnings("hiding")
public class NetworkEdge<T> extends RepastEdge<T> {
	
	private Road road;
	
	
	public NetworkEdge(T source, T target, boolean directed, double weight) {
		super(source, target, directed, weight);
	}
	
	@Override
	public double getWeight() {
		return super.getWeight() / this.getSpeed();
	}
    
	
	//TODO
	public double getSpeed() {
		return 1;
	}

	public Road getRoad() {
		return road;
	}
	
	public void setRoad(Road road) {
		this.road = road;
	}
	
	@Override
	public String toString() {
		return "Edge between " + this.getSource() + "->" + this.getTarget();
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
