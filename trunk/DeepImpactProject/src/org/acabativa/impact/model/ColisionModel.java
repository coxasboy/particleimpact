package org.acabativa.impact.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.acabativa.impact.util.Observable;
import org.acabativa.impact.util.Observer;
import org.apache.log4j.Logger;

public class ColisionModel implements Runnable, Observable{

	protected int MAX_WIDTH = 1200;
	protected int MAX_HEIGHT = 500;
	int clock = 0;
	boolean modelOn = true;
	SimulatorEngine engine;
	int fps = 400;
	int auxFps = 120;
	int pass = 1000/fps;
	int waitTime = 0;
	
	List<Observer> observers = new ArrayList<Observer>(); 
	
	Logger logger = Logger.getLogger(ColisionModel.class);
	
	public ColisionModel() {
		engine = new SimulatorEngine(MAX_WIDTH, MAX_HEIGHT);
		populateModel();
	}
	
	public void populateModel(){
		
		for (int i = 0; i < 200; i++) {
			addNewParticle();
		}
		
//		for (int i = 10; i < 480; i = i+21) {
//			engine.addParticles(getExtraHeavyParticle(0,i));
//		}
//
//		for (int i = 20; i < 500; i = i+21) {
//			engine.addParticles(getExtraHeavyParticle(500,i));
//		}
//		
//		for (int i = 20; i < 500; i = i+21) {
//			engine.addParticles(getExtraHeavyParticle(i,0));
//		}
//		for (int i = 0; i < 500; i = i+21) {
//			engine.addParticles(getExtraHeavyParticle(i,500));
//		}
		
	}	
	
	public double getAllEnergy(){
		return engine.getAllEnergy();
	}
	
	public void addEnergy(){
		engine.addEnergyToTheSystem();
		this.notifyAll("Change Energy");
	}
	
	public void removeEnergy(){
		engine.removeEnergyFromTheSystem();
		this.notifyAll("Change Energy");
	}
	
	private void addNewParticle(){
		Random r = new Random();
		int addSize = r.nextInt(3);
		Particle particle = new Particle(
				new Vector2D(((double)r.nextInt(4))/10d, ((double)r.nextInt(4))/10d), 
				new Point(r.nextInt(450)+20,r.nextInt(450)+20),
				5+addSize, 
				5-addSize);	
		engine.addParticles(particle);
	}
	
	private Particle getExtraHeavyParticle(int posx, int posY){
		return  new Particle(
				new Vector2D(0, 0), 
				new Point(posx,posY),
				10, 
				Integer.MAX_VALUE);		
	}	
	
	@Override
	public void run() {
		while(modelOn){
			try {
				long startProcessAt = System.currentTimeMillis();
				engine.iterateAll();
				engine.changeStatesAll();
				clock++;
				notifyAll("Clock change");
				long endProcessAt = System.currentTimeMillis();
				long diff = endProcessAt - startProcessAt;
				if(diff>pass){
					auxFps = (int)diff;
//					System.out.println(auxFps);
					waitTime = 1000/(int)diff;
					continue;
				}
				else{
					auxFps = fps;
//					System.out.println(auxFps);
					waitTime = pass-(int)diff;
					Thread.sleep(pass);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}		
	}
	
	public int getFramesPerSecond(){
		return auxFps;
	}
	
	public int getWaitTime(){
		return waitTime;
	}
	
	public List<Particle> getAllParticles(){
		return this.engine.getParticles();
	}
	
	public Vector2D getNormAngle(Particle reference, Particle particle){
		return this.engine.getImpactVector(reference, particle);
	}
				
	private void notifyAll(String event){
		synchronized(observers){
			for (Observer observer : observers) {
				observer.notifyEvent(event);
			}
		}
	}
	
	public void addObserver(Observer observer) {
		synchronized(observers){
			observers.add(observer);		
		}
	}

	public void removeObserver(Observer observer) {
		synchronized(observers){
			observers.remove(observer);
		}
	}
	
	public int getWidth(){
		return MAX_WIDTH;
	}
	
	public int getHeight(){
		return MAX_HEIGHT;
	}

	public void stop() {
		this.modelOn = false;		
	}
	
	
}
