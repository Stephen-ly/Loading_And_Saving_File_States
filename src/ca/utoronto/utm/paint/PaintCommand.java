package ca.utoronto.utm.paint;
import java.awt.Graphics2D;
import java.io.PrintWriter;

public interface PaintCommand {
	/**
	 * Executes the shape onto the canvas based on the user's shape commands
	 * @param g2d
	 */
	public void execute(Graphics2D g2d);
	/**
	 * Returns the shape used in the command for drawing
	 * @return Shape shape object that is drawn
	 */
	public Shape getShape();
}
