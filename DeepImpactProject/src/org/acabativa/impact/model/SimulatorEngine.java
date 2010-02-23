package org.acabativa.impact.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

public class SimulatorEngine {
	
	int MAX_WITH = 0;
	int MAX_HEIGHT = 0;

	Logger logger = Logger.getLogger(SimulatorEngine.class);
	double increaseEnergy = 1;
	double pass = 0.001;
	
	boolean modelOn = true;
	List<Particle> particles = new ArrayList<Particle>();
	Set<Particle> conflictList = new HashSet<Particle>();
	

	
	
	public SimulatorEngine(int mAXWITH, int mAXHEIGHT) {
		super();
		MAX_WITH = mAXWITH;
		MAX_HEIGHT = mAXHEIGHT;
	}

	public void addParticles(Particle particle) {
		this.particles.add(particle);
	}

	public void removeParticles(Particle particle) {
		this.particles.remove(particle);
	}
	
	private void increaseEnergy(){
		increaseEnergy = increaseEnergy + pass;
	}
	
	private void decreaseEnergy(){
		increaseEnergy = increaseEnergy - pass;
	}
	
	public void removeEnergyFromTheSystem(){
		decreaseEnergy();
		refreshEnergy();
	}
	
	public void addEnergyToTheSystem(){
		increaseEnergy();
		refreshEnergy();
	}
	
	public double getAllEnergy(){
		double energy = 0;
		Iterator<Particle> iterator = particles.iterator();
		while (iterator.hasNext()) {
			Particle particleElement = iterator.next();
			energy += getEnergy(particleElement);
		}
		return energy;
	}
	
	public double getEnergy(Particle particle){
		return particle.getMass()*Math.pow(particle.getVelocity().getNorm(), 2)/2;
	}
	
	public void refreshEnergy(){
		synchronized(particles){
			Iterator<Particle> iterator = particles.iterator();
			while (iterator.hasNext()) {
				Particle particleElement = iterator.next();
				System.out.println(increaseEnergy);
				particleElement.setVelocity(particleElement.getVelocity().scalarMultiply(increaseEnergy));
			}
		}
	}

	public void changeStatesAll() {
		Iterator<Particle> iterator = particles.iterator();
		while (iterator.hasNext()) {
			Particle particleElement = iterator.next();
			int index = particles.indexOf(particleElement);
			changeStatesParticle(particleElement,index);
		}
	}

	private void changeStatesParticle(Particle particle) {
		Iterator<Particle> iterator = particles.iterator();
		while (iterator.hasNext()) {
			Particle particleElement = iterator.next();
			if (!particle.equals(particleElement)) {
				if (particle.conflicts(particleElement)) {
					Vector2D velocityParticle = getFinalNewSpeedReference(
							particle, particleElement);
					Vector2D velocityElement = getFinalNewSpeedReference(
							particleElement, particle);
					particle.setVelocity(velocityParticle);
					particleElement.setVelocity(velocityElement);
					while (particle.conflicts(particleElement)) {
						if (particle.getVelocity().getNorm() > particleElement
								.getVelocity().getNorm()) {
							iterate(particle);
						} else {
							iterate(particleElement);
						}
					}
				}
			}
		}
	}

	private void changeStatesParticle(Particle particle, int begin) {
		int cont = 0;
		Iterator<Particle> iterator = particles.iterator();

		while (iterator.hasNext()) {
			Particle particleElement = iterator.next();
			if (cont >= begin) {
				if (!particle.equals(particleElement)) {
					if (particle.conflicts(particleElement)) {
						Vector2D velocityParticle = getFinalNewSpeedReference(
								particle, particleElement);
						Vector2D velocityElement = getFinalNewSpeedReference(
								particleElement, particle);
						particle.setVelocity(velocityParticle);
						particleElement.setVelocity(velocityElement);
						while (particle.conflicts(particleElement)) {
							if (particle.getVelocity().getNorm() > particleElement
									.getVelocity().getNorm()) {
								iterate(particle);
							} else {
								iterate(particleElement);
							}
						}
					}
				}
			}
			cont++;
		}
	}

	public void iterateAll() {
		Iterator<Particle> iterator = particles.iterator();
		while (iterator.hasNext()) {
			Particle particleElement = iterator.next();
			iterate(particleElement);
		}
	}

	private void iterate(Particle particle) {
		Point point = getNextPoint(particle);
		
		if(point.getY()>MAX_HEIGHT || point.getY()<0){
			if(point.getY()<0){
				point = new Point(point.getX(), 0);
			}
			if(point.getY()>MAX_HEIGHT){
				point = new Point(point.getX(), MAX_HEIGHT);
			}
			invertY(particle);
		}
		
		if(point.getX()>MAX_WITH || point.getX()<0){
			if(point.getX()<0){
				point = new Point(0, point.getY());
			}
			if(point.getX()>MAX_WITH){
				point = new Point(MAX_WITH, point.getY());
			}
			invertX(particle);
		}
				
		particle.setPosition(point);
	}

	private Point getNextPoint(Particle particle) {
		Vector2D vector2d = particle.getVelocity();
		Point point = particle.getPosition();
		return new Point((vector2d.getX() + point.getX()),
				(vector2d.getY() + point.getY()));
	}
	
	private void invertX(Particle particle){
		particle.setVelocity(new Vector2D(particle.getVelocity().getX()*(-1), particle.getVelocity().getY()));
	}
	
	private void invertY(Particle particle){
		particle.setVelocity(new Vector2D(particle.getVelocity().getX(), particle.getVelocity().getY()*(-1)));
	}

	private Point getLastPoint(Particle particle) {
		Vector2D vector2d = particle.getVelocity();
		Point point = particle.getPosition();
		return new Point((-vector2d.getX() + point.getX()),
				(-vector2d.getY() + point.getY()));
	}

	public List<Particle> getParticles() {
		return particles;
	}

	private Vector2D getNewVelocityOne(double massOne, Vector2D velocityOne,
			double massTwo, Vector2D velocityTwo) {
		double totalMass = massOne + massTwo;
		double difMass = massOne - massTwo;

		Vector2D coeficientOne = velocityOne
				.scalarMultiply((difMass / totalMass));
		Vector2D coeficientTwo = velocityTwo
				.scalarMultiply((2 * massTwo / totalMass));

		return coeficientOne.add(coeficientTwo);
	}

	private Vector2D getNewVelocityTwo(double massOne, Vector2D velocityOne,
			double massTwo, Vector2D velocityTwo) {
		double totalMass = massOne + massTwo;
		double difMass = massTwo - massOne;

		Vector2D coeficientOne = velocityTwo
				.scalarMultiply((difMass / totalMass));
		Vector2D coeficientTwo = velocityOne
				.scalarMultiply((2 * massOne / totalMass));

		return coeficientOne.add(coeficientTwo);
	}

	private Vector2D getFinalNewSpeedReference(Particle reference,
			Particle particle) {
		Vector2D speedRef = getVelocityAlignRef(reference, particle);
		Vector2D speedPart = getVelocityAlignRef(particle, reference);
		// logger.info("Speed A: " + getNewVelocityOne(reference.getMass(),
		// speedRef, particle.getMass(), speedPart));
		Vector2D speedObliqueRef = getVelocityObliqueRef(reference, particle);
		// logger.info("Speed O: " + speedObliqueRef);
		return getNewVelocityOne(reference.getMass(), speedRef,
				particle.getMass(), speedPart).add(speedObliqueRef);
	}

	private Vector2D getVelocityAlignRef(Particle reference, Particle particle)
			throws IllegalArgumentException {
		Vector2D velocity = reference.getVelocity();
		Vector2D impactVector = getImpactVector(reference, particle);
		return velocity.getProjection(impactVector);
	}

	private Vector2D getVelocityObliqueRef(Particle reference, Particle particle)
			throws IllegalArgumentException {
		Vector2D velocity = reference.getVelocity();
		Vector2D impactVector = getImpactVector(reference, particle);
		Vector2D obliqueImpactVector = impactVector.getOrtogonalToThis();
		return velocity.getProjection(obliqueImpactVector);
	}

	public Vector2D getImpactVector(Particle reference, Particle particle) {
		Point ref = reference.getPosition();
		Point point = particle.getPosition();
		Vector2D vector = new Vector2D(ref, point);
		return vector;
	}
	

}
