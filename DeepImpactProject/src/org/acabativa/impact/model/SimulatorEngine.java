package org.acabativa.impact.model;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

public class SimulatorEngine {
	
	Logger logger = Logger.getLogger(SimulatorEngine.class);

	boolean modelOn = true;
	List<Particle> particles = new ArrayList<Particle>();
	Set<Particle> conflictList = new HashSet<Particle>();
	
	public void addParticles(Particle particle){
		this.particles.add(particle);
	}
	
	public void removeParticles(Particle particle){
		this.particles.remove(particle);
	}
	
	public void changeStatesAll(){
		Iterator<Particle> iterator = particles.iterator();
		while(iterator.hasNext()){
			Particle particleElement = iterator.next();
			changeStatesParticle(particleElement);
		}
	}
	
	private void changeStatesParticle(Particle particle){
		Iterator<Particle> iterator = particles.iterator();
		while(iterator.hasNext()){
			Particle particleElement = iterator.next();
			if(!particle.equals(particleElement)){
				if(particle.conflicts(particleElement)){
					Vector2D velocityParticle = getFinalNewSpeedReference(particle, particleElement);
					Vector2D velocityElement = getFinalNewSpeedReference(particleElement, particle);
					particle.setVelocity(velocityParticle);
					particleElement.setVelocity(velocityElement);
					while(particle.conflicts(particleElement)){
						iterateAll();
					}
				}
			}
		}
	}
	
	public void iterateAll(){
		Iterator<Particle> iterator = particles.iterator();
		while(iterator.hasNext()){
			Particle particleElement = iterator.next();
			iterate(particleElement);
		}
	}
	
	private void iterate(Particle particle){
		Point point = getNextPoint(particle);
		particle.setPosition(point);
	}
	
	private Point getNextPoint(Particle particle){
		Vector2D vector2d = particle.getVelocity();
		Point point = particle.getPosition();
		return new Point((vector2d.getX()+point.getX()), (vector2d.getY()+point.getY()));
	}
	
	private Point getLastPoint(Particle particle){
		Vector2D vector2d = particle.getVelocity();
		Point point = particle.getPosition();
		return new Point((-vector2d.getX()+point.getX()), (-vector2d.getY()+point.getY()));
	}

	public List<Particle> getParticles() {
		return particles;
	}
		
	private Vector2D getNewVelocityOne(double massOne, Vector2D velocityOne, double massTwo, Vector2D velocityTwo){
		double totalMass = massOne + massTwo; 
		double difMass = massOne - massTwo;
		
		Vector2D coeficientOne = velocityOne.scalarMultiply((difMass/totalMass));
		Vector2D coeficientTwo = velocityTwo.scalarMultiply((2*massTwo/totalMass));
		
		return coeficientOne.add(coeficientTwo);
	}
	
	private Vector2D getNewVelocityTwo(double massOne, Vector2D velocityOne, double massTwo, Vector2D velocityTwo){
		double totalMass = massOne + massTwo; 
		double difMass = massTwo - massOne;
		
		Vector2D coeficientOne = velocityTwo.scalarMultiply((difMass/totalMass));
		Vector2D coeficientTwo = velocityOne.scalarMultiply((2*massOne/totalMass));
		
		return coeficientOne.add(coeficientTwo);
	}
	
	private Vector2D getFinalNewSpeedReference(Particle reference, Particle particle){
		Vector2D speedRef = getVelocityAlignRef(reference, particle);
		Vector2D speedPart = getVelocityAlignRef(particle, reference);
//		logger.info("Speed A: " + getNewVelocityOne(reference.getMass(), speedRef, particle.getMass(), speedPart));
		Vector2D speedObliqueRef = getVelocityObliqueRef(reference, particle);
//		logger.info("Speed O: " + speedObliqueRef);
		return getNewVelocityOne(reference.getMass(), speedRef, particle.getMass(), speedPart).add(speedObliqueRef);
	}
	
	private Vector2D getVelocityAlignRef(Particle reference, Particle particle) throws IllegalArgumentException{
		Vector2D velocity = reference.getVelocity();
		Vector2D impactVector = getImpactVector(reference, particle);
		return velocity.getProjection(impactVector);
	}
		
	private Vector2D getVelocityObliqueRef(Particle reference, Particle particle) throws IllegalArgumentException{
		Vector2D velocity = reference.getVelocity();
		Vector2D impactVector = getImpactVector(reference, particle);
		Vector2D obliqueImpactVector = impactVector.getOrtogonalToThis();
		return velocity.getProjection(obliqueImpactVector);
	}
	
	public Vector2D getImpactVector(Particle reference, Particle particle){
		Point ref = reference.getPosition();
		Point point = particle.getPosition();
		Vector2D vector = new Vector2D(ref, point);
		return vector;
	}
	
	public static void main(String[] args) {
		BasicConfigurator.configure();
		Vector2D vector1 = new Vector2D(3, 3);
		Vector2D vector2 = new Vector2D(-1, -1);
		
		Particle p1 = new Particle(vector1, 10, 1);
		Particle p2 = new Particle(vector2, 10, 1);
		
		p1.setPosition(new Point(0,0));
		p2.setPosition(new Point(10,20));
		
		SimulatorEngine se = new SimulatorEngine();
				
		System.out.println(vector1);
		System.out.println(vector2);
		
		System.out.println("********");
//		
//		
//		
//		System.out.println(velocityOne);
//		System.out.println(velocityTwo);
	}
	
}
