package trafficInCity;

import java.util.ArrayList;
import java.util.List;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Point;

import repast.simphony.space.gis.Geography;

public class LowestTrafficCar extends Car {
	
	private int i;
	private List<Coordinate> route;

	public LowestTrafficCar (Geography<? extends Car> space, Point finalPos) {
		super(space, finalPos);
		Coordinate f = new Coordinate(finalPos.getX(), finalPos.getY());
		i = 0;
		route = new ArrayList<Coordinate>();
	}

}
