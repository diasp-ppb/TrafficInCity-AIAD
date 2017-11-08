package trafficInCity;


import repast.simphony.context.Context;
import repast.simphony.context.space.continuous.ContinuousSpaceFactory;
import repast.simphony.context.space.continuous.ContinuousSpaceFactoryFinder;
import repast.simphony.context.space.grid.GridFactory;
import repast.simphony.context.space.grid.GridFactoryFinder;
import repast.simphony.dataLoader.ContextBuilder;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.space.continuous.RandomCartesianAdder;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridBuilderParameters;
import repast.simphony.space.grid.SimpleGridAdder;
import repast.simphony.space.grid.WrapAroundBorders;

public class CarFactory implements ContextBuilder < Object > {
	/* ( non - Javadoc )
	 * @see repast . simphony . dataLoader . ContextBuilder
# build ( repast . simphony . context . Context )
	 */

	public Context build ( Context < Object > context ) {
		context.setId ("jcars");
		ContinuousSpaceFactory spaceFactory =
				ContinuousSpaceFactoryFinder.createContinuousSpaceFactory( null );
		ContinuousSpace < Object > space =
				spaceFactory.createContinuousSpace("space", context ,new RandomCartesianAdder < Object >() ,new repast.simphony.space.continuous.WrapAroundBorders() ,50 , 50);
		GridFactory gridFactory = GridFactoryFinder.createGridFactory ( null );
		// Correct import : import repast . simphony . space . grid . Wr apArou ndBord ers ;
		Grid<Object> grid = gridFactory.createGrid( "grid" , context ,
				new GridBuilderParameters < Object >( new WrapAroundBorders() ,new SimpleGridAdder < Object >() ,true , 50 , 50)
				);

		int  carCount = 5;

		for (int i = 0; i < carCount; i++) {			
			context.add(new  ShortestPathCar(space , grid ));
		}

		for (Object  obj : context) {

			NdPoint  pt = space.getLocation(obj);

			grid.moveTo(obj , (int)pt.getX(), (int)pt.getY ());

		}

		return context ;
	}
}