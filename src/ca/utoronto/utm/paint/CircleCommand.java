package ca.utoronto.utm.paint;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.io.PrintWriter;

public class CircleCommand implements PaintCommand {
	//instances of this
	private Circle circle;
	/**
	 * Initializes the instance variables of this class
	 * @param circle 
	 */
	public CircleCommand(Circle circle){
		this.circle=circle;
	}
	/**
	 * creates the circle shape onto the canvas with specified features
	 */
	public void execute(Graphics2D g2d){
		g2d.setColor(circle.getColor());
		int x = this.circle.getCentre().x;
		int y = this.circle.getCentre().y;
		int radius = this.circle.getRadius();
		if(circle.isFill()){
			g2d.fillOval(x-radius, y-radius, 2*radius, 2*radius);
		} else {
			g2d.drawOval(x-radius, y-radius, 2*radius, 2*radius);
		}
	}
	/**
	 * Returns the circle object of the the current command
	 * @return this.circle, Circle object
	 */
	public Shape getShape(){
		return this.circle;
	}
	
	
}
