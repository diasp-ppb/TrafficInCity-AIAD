package environment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.vividsolutions.jts.geom.Coordinate;

import environment.FixedGeography;
import environment.Identified;

import exceptions.DuplicateIdentifierException;
import exceptions.NoIdentifierException;
/*import repastcity3.main.GlobalVars;*/
import environment.Junction;
/*import repastcity3.environment.NetworkEdge;*/

public class Road implements FixedGeography, Identified {
	
	private String identifier;
	private static Map<String, Object> idMap = new HashMap<String, Object>();
	
	/*transient*/
	private ArrayList<Junction> junctions;

	private Coordinate coord;
	
	/*transient*/
	//private NetworkEdge<Junction> edge;
	
	private String access;
	private List<String> accessibility;
	
	private String name;
	private boolean majorRoad = false;
	
	public static Road nullRoad;
	static {
		Road.nullRoad = new Road();
		try {
			Road.nullRoad.setIdentifier("NULLROAD");
			Road.nullRoad.setCoords(new Coordinate());
		} catch (DuplicateIdentifierException e) { // This should never happen
			//LOGGER.log(Level.SEVERE, "", e);
		}
	}
	
	public Road() {
		this.junctions = new ArrayList<Junction>();
	}
	
	public void initialise() throws NoIdentifierException {
		if (this.identifier == null || this.identifier == "") {
			throw new NoIdentifierException("This road has no identifier.");
		}
		// Parse the access string and work out which accessibility methods can be used to travel this Road
		if (this.access != null) { // Could be null because not using accessibility in GRID environment for example
			this.accessibility = new ArrayList<String>();
			for (String word : this.access.split(" ")) {
				/*	if (word.equals(GlobalVars.TRANSPORT_PARAMS.MAJOR_ROAD)) {
						// Special case: 'majorRoad' isn't a type of access, means the road is quick for car drivers
						this.majorRoad = true;
					} else {
						// Otherwise just add the accessibility type to the list
						this.accessibility.add(word);
					}
				}*/
			}
		}
	}
	
	public void setAccess(String access) {
		this.access = access;
	}

	public boolean isMajorRoad() {
		return this.majorRoad;
	}
	
	public List<String> getAccessibility() {
		return this.accessibility;
	}
	
	
	@Override
	public String toString() {
		return "road: " + this.identifier + (this.name == null ? "(no name)" : "(" + this.name + ")");
	}
	
	@Override
	public String getIdentifier() throws NoIdentifierException {
		if (this.identifier == null) {
			throw new NoIdentifierException("This road has no identifier.");
		} else {
			return identifier;
		}
	}
	@Override
	public void setIdentifier(String identifier) throws DuplicateIdentifierException {
		if (Road.idMap.containsKey(identifier)) {
			throw new DuplicateIdentifierException("A road with identifier '" + identifier + "' has already "
					+ "been created - cannot have two roads with the same unique ID.");
		}
		this.identifier = identifier;
	}
	
	public void addJunction(Junction j) {
		if (this.junctions.size() == 2) {
			System.err.println("Road: Error: this Road object already has two Junctions.");
		}
		this.junctions.add(j);
	}
	
	public ArrayList<Junction> getJunctions() {
		if (this.junctions.size() != 2) {
			System.err.println("Road: Error: This Road does not have two Junctions");
		}
		return this.junctions;
	}
	
	@Override
	public Coordinate getCoords() {
		return coord;
	}
	
	@Override
	public void setCoords(Coordinate coord) {
		this.coord = coord;
	}
	
	/*
	public NetworkEdge<Junction> getEdge() {
		return edge;
	}
	 */
	/*
	public void setEdge(NetworkEdge<Junction> edge) {
		this.edge = edge;
	}
	*/
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Road))
			return false;
		Road r = (Road) obj;
		return this.identifier.equals(r.identifier);
	}
	
	
	@Override
	public int hashCode() {
		if (this.identifier==null) {
			//LOGGER.severe("hashCode called but this object's identifier has not been set. It is likely that you're " +
			//		"reading a shapefile that doesn't have a string column called 'identifier'");
		}
		return this.identifier.hashCode();
	}
	


}
