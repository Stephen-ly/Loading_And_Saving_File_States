package ca.utoronto.utm.paint;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * Parse a file in Version 1.0 PaintSaveFile format. An instance of this class
 * understands the paint save file format, storing information about
 * its effort to parse a file. After a successful parse, an instance
 * will have an ArrayList of PaintCommand suitable for rendering.
 * If there is an error in the parse, the instance stores information
 * about the error. For more on the format of Version 1.0 of the paint 
 * save file format, see the associated documentation.
 * 
 * @author 
 *
 */
public class PaintSaveFileParser {
	
	private int lineNumber = 0; // the current line being parsed
	private String errorMessage =""; // error encountered during parse
	private ArrayList<PaintCommand> commands; // created as a result of the parse
	private String shape = null; // used to match the correct shape with the proper state
	//Used to match the shape with the correct state
	private static final String CIRCLE = "Circle"; //The Circle constant that is used to match
	private static final String RECTANGLE = "Rectangle"; //Rectangle constant
	private static final String SQUIGGLE = "Squiggle"; //Squiggle constant
	/**
	 * Patterns that are used to parse the text in the save file
	 */
	private Pattern pFileStart=Pattern.compile("^PaintSaveFileVersion1.0$"); //Used to check whether the text is the tag used to start the file
	private Pattern pFileEnd=Pattern.compile("^EndPaintSaveFile$"); //check text file if the file is ending

	//Start and End Shape patterns
	private Pattern pCircleStart=Pattern.compile("^Circle$"); //check the text file to see if the circle starts
	private Pattern pCircleEnd=Pattern.compile("^EndCircle$"); //check the text file to see if the circle ends

	private Pattern pRectangleStart= Pattern.compile("^Rectangle$");//check the text file to see if th rectangle starts
	private Pattern pRectangleEnd= Pattern.compile("^EndRectangle$");//check the text file tp see if the rectangle ends

	private Pattern pSquiggleStart=Pattern.compile("^Squiggle$");//check for squiggle start 
	private Pattern pSquiggleEnd=Pattern.compile("^EndSquiggle$");//check for squiggle end

	//The regular expression to match each "0-255" value in the string. This is because the color string should have 3 values from 0-255 to encode RGB
	//values
	private Pattern pColor= Pattern.compile("^color:([0-9]{1,2}|1[0-9]{1,2}|2[0-4][0-9]|25[0-5]),"
			+ "([0-9]{1,2}|1[0-9]{1,2}|2[0-4][0-9]|25[0-5]),"
			+ "([0-9]{1,2}|1[0-9]{1,2}|2[0-4][0-9]|25[0-5])$");

	private Pattern pCenter= Pattern.compile("^center:\\(-?\\d+,-?\\d+\\)$"); //check for the center string 
	private Pattern pRadius= Pattern.compile("^radius:\\d+$"); //check for radius string
	
	private Pattern pPointStart= Pattern.compile("^points$"); //check for point start
	private Pattern pPoint= Pattern.compile("^point:\\(-?\\d+,-?\\d+\\)$"); //check point format
	private Pattern pPointEnd= Pattern.compile("^endpoints$"); //check for point end
	
	/*//Unfortunately this is needed as the order of RECTANGLE p1, and RECTANGLE p2 may be reversed, which is something 
	 * that needs to be detected. Although they are very similar, they must use different patterns
	 * because of this.
	 */
	private Pattern pRectanglePoint1= Pattern.compile("^p1:\\(-?\\d+,-?\\d+\\)$"); //rectangle point 1
	private Pattern pRectanglePoint2= Pattern.compile("^p2:\\(-?\\d+,-?\\d+\\)$"); //rectangle point 2

	private Pattern pIsFilled = Pattern.compile("^filled:(true|false)$"); //checked if filled or not

	// *--------------------------------------------------------------------------------------------------------------------------------------------------*//
	
	/**
	 * Store an appropriate error message in this, including 
	 * lineNumber where the error occurred.
	 * @param mesg
	 */
	private void error(String mesg){
		this.errorMessage = "Error in line "+lineNumber+" "+mesg;
	}
	
	/**
	 * 
	 * @return the PaintCommands resulting from the parse
	 */
	public ArrayList<PaintCommand> getCommands(){
		return this.commands;
	}
	/**
	 * 
	 * @return the error message resulting from an unsuccessful parse
	 */
	public String getErrorMessage(){
		return this.errorMessage;
	}

	/**
	 * Parse the inputStream as a Paint Save File Format file.
	 * The result of the parse is stored as an ArrayList of Paint command.
	 * If the parse was not successful, this.errorMessage is appropriately
	 * set, with a useful error message.
	 * 
	 * @param inputStream the open file to parse
	 * @return whether the complete file was successfully parsed
	 */
	public boolean parse(BufferedReader inputStream) {
		this.commands = new ArrayList<PaintCommand>();
		this.errorMessage="";

		// During the parse, we will be building one of the 
		// following shapes. As we parse the file, we modify 
		// the appropriate shape.

		//
		Circle circle = null;  //Stores the current circle being worked on
		Rectangle rectangle = null; //Stores the current rectangle being worked on
		Squiggle squiggle = null; //Stores the current squiggle being worked on

		// Prevents the crash with an exception
		try {	
			
			int state=0; Matcher m; String l; //state of the program, the matcher with the regex, and string being parsed
			
			this.lineNumber=0; //the line number being worked on
			
			while ((l = inputStream.readLine()) != null) {
				//prevents the parsing of empty stings
				l = l.replaceAll("\\s+",""); //removes empty lines and spaces before the lines
				if (l.equals("")){ // if there is an empty line the skip the line
					break;
				}
				
				this.lineNumber++;
				//Changed this line so that the line number and the state is less cryptic and easier to work with
				System.out.println("The line number is: " + lineNumber +". The string being read is: "
						+ "" + l +". The state is : " + state);
				
				switch(state){
				case 0:
					//checks for the file start string
					m=pFileStart.matcher(l);
					if(m.matches()){
						state=1; // goto the next state if it is correct
						break; 
					}
					else{ //report the appropriate error
						error("Expected Start of Paint Save File");
						return false;
					}
				case 1: // Looking for the start of a new object or end of the save file
					m=pCircleStart.matcher(l);
					if(m.matches()){
						state=2; //goto the next state
						circle = new Circle(); //add the circle to the local circle
						this.shape = CIRCLE; //set the current shape being worked to circle
						break;
					}else{
						m=pRectangleStart.matcher(l); //check for rectangle start
						if (m.matches()){
							state=2; //next state
							rectangle = new Rectangle(); //add the rectangle to the local rectangle
							this.shape = RECTANGLE; //set the current shape being worked to rectangle
							break;
						}else{
							m=pSquiggleStart.matcher(l); //check for squiggle start
							if (m.matches()){
								state=2; //next state
								squiggle = new Squiggle(); //add the squiggle to the squiggle being worked on
								this.shape = SQUIGGLE; // set the current shape being worked on to squiggle
								break;

							}else{
								m=pFileEnd.matcher(l); //end the file
								if (m.matches()){
									break;
								}

								else{
									error("Expected Shape Start or End of file"); //report the error of a shape start, or missing end of file
									return false; 
								}
							}
						}
					}
				case 2:
					m=pColor.matcher(l); //color start
					if(m.matches()){
						//removing the apropriate matched format and retreiving information
						String[] removingColorWord = l.split(":");
						String[] splitIntoValues = removingColorWord[1].split(",");
						state=3;
						
						//placing the data into the appropriate place
						if (this.shape == CIRCLE){ // set circle color
							circle.setColor(new Color(Integer.parseInt(splitIntoValues[0]),Integer.parseInt(splitIntoValues[1]),
									Integer.parseInt(splitIntoValues[2])));	
							break;
						}else if(this.shape == RECTANGLE){ // set rectangle color
							rectangle.setColor(new Color(Integer.parseInt(splitIntoValues[0]),Integer.parseInt(splitIntoValues[1]),
									Integer.parseInt(splitIntoValues[2])));
							break;
						}else if(this.shape == SQUIGGLE){ //set squiggle color
							squiggle.setColor(new Color(Integer.parseInt(splitIntoValues[0]),Integer.parseInt(splitIntoValues[1]),
									Integer.parseInt(splitIntoValues[2])));
							break;
						}
					}else{
						error("The Color String is expected"); //report that color string is missing
						return false;
					}

				case 3:
					m = pIsFilled.matcher(l); //isfilled start
					if(m.matches()){
						//remove he filled word, that matches the regex, and takes the info and places it into the right place
						String[] removingFilledWord = l.split(":");
						state=4;
						if (this.shape == CIRCLE){//sets the circle fill
							circle.setFill(Boolean.valueOf(removingFilledWord[1]));
							break;
						}else if (this.shape == RECTANGLE){//sets the rectangle fill
							rectangle.setFill(Boolean.valueOf(removingFilledWord[1]));
							break;
						}else if (this.shape == SQUIGGLE){//sets the squiggle fill
							squiggle.setFill(Boolean.valueOf(removingFilledWord[1]));
							break;
						}
					}else{
						error("The is fill string is expected"); //report that the fill string is missing
						return false;
					}

				case 4:
					if (this.shape == CIRCLE){ //circle center
						m = pCenter.matcher(l); //checks for the center string
						if(m.matches()){
							//removes unnecessary string and retrieves data
							String[] removingCenterWord = l.split(":");
							String removeBrackets = removingCenterWord[1].replaceAll("[\\()]","");
							String [] removeComma = removeBrackets.split(",");
							state=5;
							circle.setCentre(new Point(Integer.parseInt(removeComma[0]),Integer.parseInt(removeComma[1]))); //set the center for the circle
							break;
						}
						else{
							error("The centre string is expected"); //report the error with the center string
							return false;
						}

					} else if (this.shape == RECTANGLE){ //Rectangle point 1
						m = pRectanglePoint1.matcher(l); //checks for the rectangle point 1 string
						if(m.matches()){
							//retrieves the point data from the string
							String[] removingP1 = l.split(":");
							String removeBrackets = removingP1[1].replaceAll("[\\()]","");
							String [] removeComma = removeBrackets.split(",");
							rectangle.setP1(new Point(Integer.parseInt(removeComma[0]),Integer.parseInt(removeComma[1]))); // set the P1 of local rectangle
							state=5;
							break;
						}
						else{
							error("The point1 string is expected"); // report the error with the p1 string
							return false;
						}

					}else if (this.shape == SQUIGGLE){ //Squiggle start
						m = pPointStart.matcher(l); //checks for the points starting
						if(m.matches()){
							state=5;
							break;
						}
						else{
							error("The point start string is expected"); // Report that the pointstart string is wrong
							return false;
						}
					}
					case 5:
						if (this.shape == CIRCLE){ //checks for the radius
							m = pRadius.matcher(l);
							if(m.matches()){ //retrives the data from the string
								String[] removingRadiusWord = l.split(":");
								circle.setRadius(Integer.parseInt(removingRadiusWord[1])); //set the radius to the value
								state=6;
								break;
							}
							else{
								error("The radius string is expected"); //report the radius string is wrong
								return false;
							}
						} else if(this.shape == RECTANGLE){ //remember to fix this cause you might have errors with the p1/p2 order
							m = pRectanglePoint2.matcher(l); //Checks for the rectangle point 2
							if(m.matches()){
								//retrieves the information from the string
								String[] removingP2 = l.split(":");
								String removeBrackets = removingP2[1].replaceAll("[\\()]","");
								String [] removeComma = removeBrackets.split(",");
								rectangle.setP2(new Point(Integer.parseInt(removeComma[0]),Integer.parseInt(removeComma[1]))); //sets the rectangle point 2
								state=6;
								break;
							}
							else{
								error("The point2 string is expected"); //report the point2 string error
								return false;
							}
							
						} else if(this.shape == SQUIGGLE){ //checks for the points and end points
							m = pPoint.matcher(l); //checking for point coordinates
							if(m.matches()){
								//retrieves the data from the string
								String[] removingPoint = l.split(":");
								String removeBrackets = removingPoint[1].replaceAll("[\\()]","");
								String [] removeComma = removeBrackets.split(",");
								squiggle.add(new Point(Integer.parseInt(removeComma[0]),Integer.parseInt(removeComma[1]))); //add the point to the arraylist
								state=5;
								break;
							}else{
								m = pPointEnd.matcher(l); //if the pointEnd string is matched then end and move next state
								if(m.matches()){
									state=6;
									break;
								}
								else{
									error("The point/point end string is expected"); //report that the point/point end string is wrong
									return false;
								}
							}
						}
					case 6:
						if (this.shape == CIRCLE){ //add the apropriate shapecommand to the command list
							m = pCircleEnd.matcher(l); //checks for circle end
							if(m.matches()){ //add circle
								state=1;//go back to state one
								commands.add(new CircleCommand(circle));
								this.shape = null; //resets the current shape
								circle = null; //set the local circle to the null value
								break;
							}
							else{
								//error("") changed into println for development purposes.
								error("Expected Circle End"); //report something wrong with circle end
								return false;
							}
						}
						else if (this.shape == RECTANGLE){ 
							m = pRectangleEnd.matcher(l);//checks for rectangle end
							if(m.matches()){
								state=1;//go back to state one
								commands.add(new RectangleCommand(rectangle)); //add the rectangle
								this.shape = null; //resets the current shape
								rectangle = null;//set the local rectangle to the null value
								break;
							}
							else{
								//error("") changed into println for development purposes.
								error("Expected Rectangle End"); //report something wrong with the rectangle end
								return false;
							}

						}
						else if (this.shape == SQUIGGLE){
							m =  pSquiggleEnd.matcher(l); //checks for squiggle end
							if(m.matches()){
								state=1; 
								commands.add(new SquiggleCommand(squiggle)); //add the squiggle command
								this.shape = null; //resets the current shape
								squiggle = null; //set the local squiggle to the null valkue
								break;
							}

							else{
								error("The Squiggle end is expected"); //report the squiggle end error
								return false;
							}
						}
					}

				}
			}  catch (Exception e){

			} 
			return true;
		}
	}
