package org.acabativa.impact.view;

import java.awt.Graphics;
import java.awt.Graphics2D;

import org.acabativa.impact.controller.ColisionController;
import org.acabativa.impact.model.ColisionModel;
import org.acabativa.impact.view.drawer.EnergyDrawer;
import org.acabativa.impact.view.drawer.FPSDrawer;


public class EnergyView extends AbstractBasicView {

	EnergyDrawer drawer = new EnergyDrawer();
	FPSDrawer drawerfps = new FPSDrawer();
	
	public EnergyView(ColisionController controller, ColisionModel model) {
		super(controller, model);
		frame.setLocation(534,600);
		frame.setSize(150,160);
		frame.setVisible(true);
	}
	
	public void paint(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		try {
			drawer.draw(g2d, model.getAllEnergy());
			drawerfps.draw(g2d, model.getFramesPerSecond());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected int getHeightFrame() {
		return 500;
	}

	@Override
	protected int getWidthFrame() {
		return 500;
	}


}

