package org.acabativa.impact.view.drawer;

import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

import org.acabativa.impact.model.Point;
import org.acabativa.impact.model.Vector2D;

public class VectorDrawer {

	public void draw(Graphics2D g2d, Vector2D vector) throws IllegalArgumentException{
		createAndDrawShape(g2d, vector);
	}
			
	private void createAndDrawShape(Graphics2D g2d, Vector2D vector){
//		Point ref = new Point(250,250);
//		Point vec = new Point(vector.getX(),vector.getY());
//		g2d.draw(new Line2D.Double(
//				new Point2D.Double(ref.getX(), ref.getY()),
//				new Point2D.Double(vec.getX(), vec.getY())
//		));	
	}
	
}
