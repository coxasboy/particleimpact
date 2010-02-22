package org.acabativa.impact.controller;

import org.acabativa.impact.model.ColisionModel;
import org.acabativa.impact.view.ParticlesView;

public class ColisionController {

	ColisionModel model;
	ParticlesView view;
	Thread t;
		
	public ColisionController(ColisionModel model) {
		super();
		this.model = model;
		this.view = new ParticlesView(this, model);
		start();	
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
