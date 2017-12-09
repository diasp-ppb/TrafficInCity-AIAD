package environment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.vividsolutions.jts.geom.Coordinate;

import environment.FixedGeography;
import environment.Identified;

import exceptions.DuplicateIdentifierException;
import exceptions.NoIdentifierException;
import environment.Junction;

public class Road implements FixedGeography, Identified {

	private String identifier;
	private static Map<String, Object> idMap = new HashMap<String, Object>();
	private ArrayList<Junction> junctions;
	private Coordinate coord;
	private NetworkEdge<Junction> edge;
	private String name;
	private int load;
	private boolean majorRoad = false;
	public static Road nullRoad;
	static {
		Road.nullRoad = new Road();
		try {
			Road.nullRoad.setIdentifier("NULLROAD");
			Road.nullRoad.setCoords(new Coordinate());
		} catch (DuplicateIdentifierException e) { // This should never happen
			// LOGGER.log(Level.SEVERE, "", e);
		}
	}

	public Road() {
		this.junctions = new ArrayList<Junction>();
		this.load = 0;
	}

	public void initialise() throws NoIdentifierException {
		if (this.identifier == null || this.identifier == "") {
			throw new NoIdentifierException("This road has no identifier.");
		}
	}

	public int getLoad() {
		return load;
	}

	public void setLoad(int load) {
		this.load = load;
	}

	public boolean isMajorRoad() {
		return this.majorRoad;
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

	public NetworkEdge<Junction> getEdge() {
		return edge;
	}

	public void setEdge(NetworkEdge<Junction> edge) {
		this.edge = edge;
	}

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
		if (this.identifier == null) {
			// LOGGER.severe("hashCode called but this object's identifier has not been set.
			// It is likely that you're " +
			// "reading a shapefile that doesn't have a string column called 'identifier'");
		}
		return this.identifier.hashCode();
	}
}
