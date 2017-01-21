package ca.utoronto.utm.paint;
import java.awt.Graphics2D;
import java.io.PrintWriter;
import java.util.ArrayList;

public class RectangleCommand implements PaintCommand {
	//instances of this
	private Rectangle rectangle;
	/**
	 * Initializes the instance variables of this class
	 * @param rectangle
	 */
	public RectangleCommand(Rectangle rectangle){
		this.rectangle = rectangle;
	}
	/**
	 * creates the rectangle shape onto the canvas with specified features
	 */
	public void execute(Graphics2D g2d){
		g2d.setColor(rectangle.getColor());
		Point topLeft = this.rectangle.getTopLeft();
		Point dimensions = this.rectangle.getDimensions();
		if(rectangle.isFill()){
			g2d.fillRect(topLeft.x, topLeft.y, dimensions.x, dimensions.y);
		} else {
			g2d.drawRect(topLeft.x, topLeft.y, dimensions.x, dimensions.y);
		}
	}
	/**
	 * Returns the rectangle object of the the current command
	 * @return this.rectangle, Rectangle object
	 */
	@Override
	public Shape getShape() {
		// TODO Auto-generated method stub
		return this.rectangle;
	}
}
