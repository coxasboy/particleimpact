package org.acabativa.impact.controller;

import org.acabativa.impact.model.ColisionModel;
import org.acabativa.impact.view.EnergyView;
import org.acabativa.impact.view.ParticlesView;

public class ColisionController {

	ColisionModel model;
	ParticlesView view;
	EnergyView eView;
	Thread t;
		
	public ColisionController(ColisionModel model) {
		super();
		this.model = model;
		this.view = new ParticlesView(this, model);
		this.eView = new EnergyView(this, model);
		start();	
	}	
	
	public void addEnergy(){
		this.model.addEnergy();
	}
	
	public void removeEnergy(){
		this.model.removeEnergy();
	}

	public void start() {
		t = new Thread(model);
		t.start();
	}
	
	public boolean isAlive(){
		return this.t.isAlive();
	}

	public void stop() {
		model.stop();
	}
	
}
