package main;

import repast.simphony.context.Context;
import repast.simphony.context.space.gis.GeographyFactoryFinder;
import repast.simphony.context.space.graph.NetworkBuilder;
import repast.simphony.dataLoader.ContextBuilder;
import repast.simphony.space.gis.Geography;
import repast.simphony.space.gis.GeographyParameters;
import repast.simphony.space.gis.SimpleAdder;
import repast.simphony.space.graph.Network;
import trafficInCity.AgentTraffi;
import trafficInCity.Car;
import trafficInCity.CarFactory;
import trafficInCity.Semaphore;
import trafficInCity.SemaphoreFactory;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Iterator;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

import context.AgentTraffiContext;
import context.JunctionContext;
import context.RoadContext;
import environment.GISFunctions;
import environment.Junction;
import environment.NetworkEdgeCreator;
import environment.Road;

public class ContextManager implements ContextBuilder<Object> {

	private static Context<Object> mainContext;

	public static Context<Road> roadContext;
	public static Geography<Road> roadProjection;

	/*public static Context<Car> carContext;
	public static Geography<Car> carProjectio;*/

	public static Context<AgentTraffi> agentTraffiContext;
	public static Geography<AgentTraffi> agentTraffiProjection;

	public static Context<Junction> junctionContext;
	public static Geography<Junction> junctionProjection;
	public static Network<Junction> streetNetwork;

	public static RoadTrafficIntensity carsInRoad;
	public static ArrayList<Junction> locationSemaphors;

	public static GeometryFactory GF = new GeometryFactory();

	public String mapLocation = "./data/gis_data/toy_city/";

	@Override
	public Context<Object> build(Context<Object> con) {

		mainContext = con;
		mainContext.setId("mainContext");

		try {
			createRoads();
			// create Road intersections
			createJunctions();
			createNetWork();

			// storage info for traffic intensity in network
			carsInRoad = new RoadTrafficIntensity();

			//createCarContext();
			createAgentsContext();

		} catch (MalformedURLException | FileNotFoundException e) {
			e.printStackTrace();
		}

		return mainContext;
	}

	private void createRoads() throws MalformedURLException, FileNotFoundException {
		roadContext = new RoadContext();
		roadProjection = GeographyFactoryFinder.createGeographyFactory(null).createGeography("roadGeography",
				roadContext, new GeographyParameters<Road>(new SimpleAdder<Road>()));

		String roadFile = mapLocation + "roads.shp";

		GISFunctions.readShapefile(Road.class, roadFile, roadProjection, roadContext);

		mainContext.addSubContext(roadContext);
	}

	private void createJunctions() {
		junctionContext = new JunctionContext();
		junctionProjection = GeographyFactoryFinder.createGeographyFactory(null).createGeography("junctionGeography",
				junctionContext, new GeographyParameters<Junction>(new SimpleAdder<Junction>()));

		mainContext.addSubContext(junctionContext);
	}

	private void createNetWork() {
		NetworkBuilder<Junction> builder = new NetworkBuilder<Junction>("streetNetwork", junctionContext, false);
		builder.setEdgeCreator(new NetworkEdgeCreator<Junction>());

		streetNetwork = builder.buildNetwork();

		GISFunctions.buildGISRoadNetwork(roadProjection, junctionContext, junctionProjection, streetNetwork);
	}

	
	private void createAgentsContext() {
		agentTraffiContext = new AgentTraffiContext();
		mainContext.addSubContext(agentTraffiContext);
		agentTraffiProjection = GeographyFactoryFinder.createGeographyFactory(null).createGeography("agentTraffiGeography",
				agentTraffiContext, new GeographyParameters<AgentTraffi>(new SimpleAdder<AgentTraffi>()));

		SemaphoreFactory semaphoreFactory = new SemaphoreFactory();
		semaphoreFactory.createAgents(agentTraffiContext, agentTraffiProjection);
		
		CarFactory carFactory = new CarFactory();
		carFactory.createAgents(agentTraffiContext, agentTraffiProjection);

		this.locationSemaphors = semaphoreFactory.getLocationSemaphors();
	}
	

	public static synchronized void addCarToContext(Car car) {
		ContextManager.agentTraffiContext.add(car);
	}

	public static synchronized void moveAgent(Car car, Point point) {
		ContextManager.agentTraffiProjection.move(car, point);
	}

	public static synchronized void moveAgentByVector(Car car, double dist, double ang) {
		ContextManager.agentTraffiProjection.moveByVector(car, dist, ang);
	}

	public static Junction getJunction(Coordinate coord) {

		Iterator<Junction> junctions = streetNetwork.getNodes().iterator();

		while (junctions.hasNext()) {
			Junction j = junctions.next();
			if (j.getCoords().equals(coord))
				return j;
		}
		return null;
	}

	public static synchronized void addSemaphoreToContext(Semaphore semaphore) {
		ContextManager.agentTraffiContext.add(semaphore);
	}

	public static synchronized void moveSemaphoreToPlace(Semaphore sem, Point point) {
		ContextManager.agentTraffiProjection.move(sem, point);
	}
}
