package trafficInCity;

import java.awt.Color;


import repast.simphony.visualizationOGL2D.DefaultStyleOGL2D;

public class SemaphoreColor extends DefaultStyleOGL2D{
	
	@Override
	public Color getColor(Object o) {
		if(o instanceof Semaphore) {
			Semaphore s = (Semaphore) o;
			
			if(s.isSemaphoreGreen())
				return Color.GREEN;
			else 
				return Color.RED;
		}
		else
			return Color.BLUE;
	}

}
