package main;

import repast.simphony.context.Context;
import repast.simphony.context.space.gis.GeographyFactoryFinder;
import repast.simphony.dataLoader.ContextBuilder;
import repast.simphony.space.gis.Geography;
import repast.simphony.space.gis.GeographyParameters;
import repast.simphony.space.gis.SimpleAdder;
import trafficInCity.Car;
import trafficInCity.CarFactory;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

import context.CarContext;
import context.RoadContext;
import environment.GISFunctions;
import environment.Road;

public class ContextManager  implements ContextBuilder <Object>{

	private static Context<Object> mainContext;
	
	public static Context<Road> roadContext;
	public static Geography<Road> roadProjection;
	
	public static Context<Car> carContext;
	public static Geography<Car> carProjection;

	static GeometryFactory GF = new GeometryFactory();
	
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
	
	public static synchronized void moveAgent(Car car) {
		Coordinate coord = new Coordinate(53.83, -1.52);
		Point p = GF.createPoint(coord);
		ContextManager.carProjection.move(car, p);
		System.out.println(ContextManager.carProjection.getGeometry(car).equals(p));
	}
}
