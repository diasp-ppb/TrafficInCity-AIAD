package trafficInCity;

import sajas.core.Agent;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.space.continuous.ContinuousSpace;

public class Semaphore extends Agent{
	protected ContinuousSpace<Object> space;
	protected boolean isGreen;
	protected int tickChange;
	
	protected int actualTick;
	
	public Semaphore(ContinuousSpace<Object> space, boolean isGreen, int tickChange) {
		this.space = space;
		this.isGreen = isGreen;
		this.tickChange = tickChange;
		this.actualTick = 0;
	}
	
	@Override
	public void setup() {
		System.out.println("Oi, sou o semáforo");
	}
	
	public boolean isSemaphoreGreen() {
		return isGreen;
	}
	
	@ScheduledMethod(start = 1, interval = 1)
	public void verifySemaphoreColor() {
		if (actualTick < tickChange)
			actualTick++;
		else {
			actualTick = 0;
			isGreen = !isGreen;
		}
		
		RunEnvironment.getInstance().setScheduleTickDelay(20);
	}
}
