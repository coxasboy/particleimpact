package org.acabativa.impact.view;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.List;

import org.acabativa.impact.controller.ColisionController;
import org.acabativa.impact.model.ColisionModel;
import org.acabativa.impact.model.Particle;
import org.acabativa.impact.view.drawer.ParticlesDrawer;
import org.acabativa.impact.view.drawer.VectorDrawer;

public class ParticlesView extends AbstractBasicView{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	ParticlesDrawer drawer = new ParticlesDrawer();
	VectorDrawer vecDrawer = new VectorDrawer();
	
	public ParticlesView(ColisionController controller, ColisionModel model) {
		super(controller, model);
	}

	protected int getHeightFrame() {
		return model.getHeight();
	}

	protected int getWidthFrame() {
		return model.getWidth();
	}
	
	public void paint(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		try {
			List<Particle> particles = model.getAllParticles();
			synchronized(particles){
				Particle lastOne = null;
				for (Particle particle : particles) {
					drawer.draw(g2d, particle);
					if(lastOne!=null){
						vecDrawer.draw(g2d, model.getNormAngle(lastOne, particle));
					}
					lastOne=particle;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
