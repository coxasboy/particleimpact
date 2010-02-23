package org.acabativa.impact.view;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;

import org.acabativa.impact.controller.ColisionController;
import org.acabativa.impact.model.ColisionModel;
import org.acabativa.impact.model.Particle;
import org.acabativa.impact.view.drawer.ParticlesDrawer;

public class ParticlesView extends AbstractBasicView implements KeyListener{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	ParticlesDrawer drawer = new ParticlesDrawer();
	
	
	public ParticlesView(ColisionController controller, ColisionModel model) {
		super(controller, model);
		frame.addKeyListener(this);
		frame.setLocation(0, 0);
		frame.setVisible(true);
		
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
			
			for (Particle particle : particles) {
				drawer.draw(g2d, particle);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
//		System.out.println(e.getKeyChar());
		if(e.getKeyChar()=='+'){
			this.controller.addEnergy();
		}
		if(e.getKeyChar()=='-'){
			this.controller.removeEnergy();
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	
}
