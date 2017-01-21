package ca.utoronto.utm.paint;
import java.awt.Graphics2D;
import java.io.PrintWriter;
import java.util.ArrayList;

public class SquiggleCommand implements PaintCommand {
	//instances of this
	private Squiggle squiggle;
	/**
	 * Initializes the instance variables of this class
	 * @param squiggle 
	 */
	public SquiggleCommand(Squiggle squiggle){
		this.squiggle = squiggle;
	}
	/**
	 * creates the squiggle shape onto the canvas with specified features by drawing each point onto the canvas
	 */
	public void execute(Graphics2D g2d){
		ArrayList<Point> points = this.squiggle.getPoints();
		g2d.setColor(squiggle.getColor());
		for(int i=0;i<points.size()-1;i++){
			Point p1 = points.get(i);
			Point p2 = points.get(i+1);
			g2d.drawLine(p1.x, p1.y, p2.x, p2.y);
		}
	}
	/**
	 * Returns the squiggle object of the the current command
	 * @return this.squiggle, Squiggle object
	 */
	@Override
	public Shape getShape() {
		// TODO Auto-generated method stub
		return this.squiggle;
	}
}
