package org.acabativa.impact.view.drawer;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;

import org.acabativa.impact.model.Particle;
import org.acabativa.impact.model.Point;

public class ParticlesDrawer {
	Color blue = Color.lightGray;
	Color red = Color.red;
	Color green = Color.green;
	Color cyan = Color.CYAN;
		
	public void draw(Graphics2D g2d, Particle particle) throws IllegalArgumentException{
		createAndDrawShape(g2d, particle);
	}
			
	protected void createAndDrawShape(Graphics2D g2d, Particle particle){
		g2d.setColor(getColor(particle));
		Point point = particle.getPosition();
		int shapeSize = (int)particle.getRadius()*2;
		Shape shape = new Ellipse2D.Double((int)point.getX()-(shapeSize/2),(int)point.getY()-(shapeSize/2),shapeSize,shapeSize);
		g2d.fill(shape);
		g2d.setColor(Color.BLACK);
	}
	
	protected Color getColor(Particle particle){
		if(particle.getRadius()==5){
			return Color.BLACK;
		}
		if(particle.getRadius()==6){
			return blue;
		}
		if(particle.getRadius()==7){
			return red;
		}
		if(particle.getRadius()==8){
			return green;
		}
		return Color.cyan;
	}
	
}
