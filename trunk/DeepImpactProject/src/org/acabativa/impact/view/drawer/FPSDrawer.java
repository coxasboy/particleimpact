package org.acabativa.impact.view.drawer;

import java.awt.Graphics2D;
import java.text.DecimalFormat;

public class FPSDrawer {

	private static final int SHAPE_SIZE = 6;
	DecimalFormat df = new DecimalFormat("0000.000");
		
	public void draw(Graphics2D g2d, int frame){
		g2d.drawString("Frames: " + df.format(frame), 0, 80);
		
	}
	
	
}