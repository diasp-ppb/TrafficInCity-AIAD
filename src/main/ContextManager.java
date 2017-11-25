package main;

import repast.simphony.context.Context;
import repast.simphony.context.space.gis.GeographyFactoryFinder;
import repast.simphony.dataLoader.ContextBuilder;
import repast.simphony.space.gis.Geography;
import repast.simphony.space.gis.GeographyParameters;
import repast.simphony.space.gis.SimpleAdder;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;

import context.RoadContext;
import environment.GISFunctions;
import environment.Road;

public class ContextManager  implements ContextBuilder <Object>{

	private static Context<Object> mainContext;
	
	public static Context<Road> roadContext;
	public static Geography<Road> roadProjection;
	
	
	public String mapLocation = "./data/gis_data/toy_city/";
	
	@Override
	public Context build(Context<Object> con) {
		
		mainContext = con;
		mainContext.setId("mainContext");
		
			
		try {
	   		roadContext = new RoadContext();
	   		
	   		roadProjection = GeographyFactoryFinder
	   				.createGeographyFactory(null)
	   				.createGeography("roadGeography", roadContext, new GeographyParameters<Road>(new SimpleAdder<Road>()));
	   		
	   		String roadFile = mapLocation + "roads.shp"; 
	   		
			GISFunctions.readShapefile(Road.class, roadFile , roadProjection, roadContext);
			
			mainContext.addSubContext(roadContext);
	   					
			} catch (MalformedURLException | FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	   		
	   	
		
		return mainContext;
	}

	
}
