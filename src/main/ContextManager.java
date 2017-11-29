package main;

import repast.simphony.context.Context;
import repast.simphony.context.space.gis.GeographyFactoryFinder;
import repast.simphony.context.space.graph.NetworkBuilder;
import repast.simphony.dataLoader.ContextBuilder;
import repast.simphony.space.gis.Geography;
import repast.simphony.space.gis.GeographyParameters;
import repast.simphony.space.gis.SimpleAdder;
import repast.simphony.space.graph.Network;
import trafficInCity.Car;
import trafficInCity.CarFactory;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

import context.CarContext;
import context.JunctionContext;
import context.RoadContext;
import environment.GISFunctions;
import environment.Junction;
import environment.NetworkEdgeCreator;
import environment.Road;

public class ContextManager  implements ContextBuilder <Object>{

	private static Context<Object> mainContext;
	
	public static Context<Road> roadContext;
	public static Geography<Road> roadProjection;
	
	public static Context<Car> carContext;
	public static Geography<Car> carProjection;
	
	public static Context<Junction> junctionContext;
	public static Geography<Junction> junctionProjection;
	public static Network<Junction> streetNetwork;

	public static GeometryFactory GF = new GeometryFactory();
	
	public String mapLocation = "./data/gis_data/toy_city/";
	
	@Override
	public Context build(Context<Object> con) {
		
		mainContext = con;
		mainContext.setId("mainContext");
		
			
		try {
			//Roads
	   		roadContext = new RoadContext();
	   		roadProjection = GeographyFactoryFinder
	   				.createGeographyFactory(null)
	   				.createGeography("roadGeography", roadContext, new GeographyParameters<Road>(new SimpleAdder<Road>()));
	   		
	   		String roadFile = mapLocation + "roads.shp"; 
	   		
			GISFunctions.readShapefile(Road.class, roadFile , roadProjection, roadContext);
			
			mainContext.addSubContext(roadContext);
			
			//Junctions
			junctionContext = new JunctionContext();
			junctionProjection = GeographyFactoryFinder
					.createGeographyFactory(null)
					.createGeography("junctionGeography", junctionContext, new GeographyParameters<Junction>(new SimpleAdder<Junction>()));
			
			mainContext.addSubContext(junctionContext);
			
			// create network 
			NetworkBuilder<Junction> builder = new NetworkBuilder<Junction>("streetNetwork",junctionContext,false);
			builder.setEdgeCreator(new NetworkEdgeCreator<Junction>());
			
			streetNetwork = builder.buildNetwork();
			
			GISFunctions.buildGISRoadNetwork(roadProjection, junctionContext, junctionProjection, streetNetwork);
			
			
			//Car Agents
			carContext = new CarContext();
			mainContext.addSubContext(carContext);
			carProjection = GeographyFactoryFinder
	   				.createGeographyFactory(null)
	   				.createGeography("carGeography", carContext, new GeographyParameters<Car>(new SimpleAdder<Car>()));
	   		
			
			CarFactory carFactory = new CarFactory();
			carFactory.createAgents(carContext, carProjection);
	   					
			} catch (MalformedURLException | FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		return mainContext;
	}
	
	public static synchronized void addCarToContext(Car car) {
		ContextManager.carContext.add(car);
	}
	
	public static synchronized void moveAgent(Car car, Point point ) {
		ContextManager.carProjection.move(car, point);
		//System.out.println(ContextManager.carProjection.getGeometry(car).equals(point));
	}
	
	public static synchronized void moveAgentByVector(Car car, double dist, double ang) {
		ContextManager.carProjection.moveByVector(car, dist, ang);
		//System.out.println(ContextManager.carProjection.getGeometry(car).equals(point));
	}
	
	public static Junction getJunction(Coordinate coord) {
		
		Iterator<Junction> junctions = streetNetwork.getNodes().iterator();
		
		while(junctions.hasNext()) {
			Junction j = junctions.next();
			if(j.getCoords().equals(coord))
				return j;
		}
		return null;
	}
	
//	public static synchronized List<Junction> getShortestPath(Point initialPoint, Point finalPoint){
//		
//		Junction initialJ = getJunction(new Coordinate(initialPoint.getX(), initialPoint.getY()));
//		Junction finalJ = getJunction(new Coordinate(finalPoint.getX(), finalPoint.getY()));
//		
//		final int size = streetNetwork.size();
//		
//		final List<Junction> closedSet = new ArrayList<Junction>(size);
//		final List<Junction> openSet = new ArrayList<Junction>(size);
//		
//		openSet.add(initialJ);
//		streetNetwork.
//		
//		final Map<>
//		
//		return null;
//	}
}
