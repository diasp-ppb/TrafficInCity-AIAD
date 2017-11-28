package environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.vividsolutions.jts.geom.*;


import repast.simphony.context.Context;
import repast.simphony.context.DefaultContext;
import repast.simphony.context.space.gis.GeographyFactoryFinder;
import repast.simphony.context.space.graph.NetworkBuilder;
import repast.simphony.dataLoader.ContextBuilder;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.ISchedule;
import repast.simphony.engine.schedule.ScheduleParameters;
import repast.simphony.parameter.Parameters;
import repast.simphony.space.gis.Geography;
import repast.simphony.space.gis.GeographyParameters;
import repast.simphony.space.gis.ShapefileLoader;
import repast.simphony.space.gis.SimpleAdder;
import repast.simphony.space.graph.Network;
import repast.simphony.space.graph.RepastEdge;

import environment.GISFunctions;
import environment.Junction;
import environment.NetworkEdge;
import environment.NetworkEdgeCreator;
import environment.Road;
import context.JunctionContext;
import context.RoadContext;
import exceptions.NoIdentifierException;



public class GISFunctions {


	
	public static void buildGISRoadNetwork(Geography<Road> roadGeography, Context<Junction> junctionContext,
			Geography<Junction> junctionGeography, Network<Junction> roadNetwork) {

		GeometryFactory geomFac = new GeometryFactory();

		Map<Coordinate, Junction> coordMap = new HashMap<Coordinate, Junction>();

		Iterable<Road> roadIt = roadGeography.getAllObjects();
		for (Road road : roadIt) {
			Geometry roadGeom = roadGeography.getGeometry(road);
			Coordinate c1 = roadGeom.getCoordinates()[0]; // First coord
			Coordinate c2 = roadGeom.getCoordinates()[roadGeom.getNumPoints() - 1]; // Last coord

			Junction junc1, junc2;
			if (coordMap.containsKey(c1)) {
				junc1 = coordMap.get(c1);
			} else {
				junc1 = new Junction();
				junc1.setCoords(c1);
				junctionContext.add(junc1);
				coordMap.put(c1, junc1);
				Point p1 = geomFac.createPoint(c1);
				junctionGeography.move(junc1, p1);
			}
			if (coordMap.containsKey(c2)) {
				junc2 = coordMap.get(c2);
			} else { 
				junc2 = new Junction();
				junc2.setCoords(c2);
				junctionContext.add(junc2);
				coordMap.put(c2, junc2);
				Point p2 = geomFac.createPoint(c2);
				junctionGeography.move(junc2, p2);
			}
			road.addJunction(junc1);
			road.addJunction(junc2);
			junc1.addRoad(road);
			junc2.addRoad(road);

			NetworkEdge<Junction> edge = new NetworkEdge<Junction>(junc1, junc2, false, roadGeom.getLength(), road
					.getAccessibility());
			if (road.isMajorRoad())
				edge.setMajorRoad(true);

			road.setEdge(edge);
			edge.setRoad(road);

			if (!roadNetwork.containsEdge(edge)) {
				roadNetwork.addEdge(edge);
			} else {
				System.out.println("buildRoadNetwork: for some reason this edge that has just been created "
			                    	+ "already exists in the RoadNetwork.");
			}

		}
	}

	
	public static <T extends FixedGeography> void readShapefile(Class<T> cl, String shapefileLocation,
			Geography<T> geog, Context<T> context) throws MalformedURLException, FileNotFoundException {
		File shapefile = null;
		ShapefileLoader<T> loader = null;
		shapefile = new File(shapefileLocation);
		if (!shapefile.exists()) {
			throw new FileNotFoundException("Could not find the given shapefile: " + shapefile.getAbsolutePath());
		}
		loader = new ShapefileLoader<T>(cl, shapefile.toURI().toURL(), geog, context);
		while (loader.hasNext()) {
			loader.next();
		}
		for (T obj : context.getObjects(cl)) {
			obj.setCoords(geog.getGeometry(obj).getCentroid().getCoordinate());
		}
	}
}

  
