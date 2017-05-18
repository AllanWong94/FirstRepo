
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;



public class Editor extends Application {
	private static FileHandler fh;
    private final Rectangle textBoundingBox = new Rectangle(1, 0);
    private double winWidth=500;
    private double winHeight=500;
    private KeyEventHandler keh;
    private static String[] argu;
    private static final String MESSAGE_PREFIX =
            "User pressed the shortcut key (command or control, depending on the OS)";
    private int fontSize = STARTING_FONT_SIZE;
    private static final int STARTING_FONT_SIZE = 20;

    /** The Text to display on the screen. */
    private String fontName = "Verdana";




    private class KeyEventHandler implements EventHandler<KeyEvent> {
    	public DataHolder dh;


        /** An EventHandler to handle keys that get pressed. */
        KeyEventHandler(final Group root, int windowWidth, int windowHeight) {
            keh=this;
            dh=new DataHolder(textBoundingBox);
           // Initialize some empty text and add it to root so that it will be displayed.
            // Always set the text origin to be VPos.TOP! Setting the origin to be VPos.TOP means
            // that when the text is assigned a y-position, that position corresponds to the
            // highest position across all letters (for example, the top of a letter like "I", as
            // opposed to the top of a letter like "e"), which makes calculating positions much
            // simpler!
        }


        /*Return a string that is ready to be displayed by the displayText with the window width
        * Input param: The window width in double
        * Modifies: cursorPosX to the x-coordinate current position of the cursor
        *           cursorPosY to the y-coordinate current position of the cursor
        *           charwidth
        *           charheight
        *           spacing
        *           rawData append the newly input data to the String
        *           cursorPos to add the new position of the cursor.
        *
        *           Calls UpdateBoundingBox() that modifies:
        *           textBoundingBox*/



        /*  Helper function that prints out the contents of cursorPos
            and the current cursor position.
        * */





        /*public void updateCharSize(String s){
            displayText.setText(s);
            charwidth=displayText.getLayoutBounds().getWidth();
            charheight=displayText.getLayoutBounds().getHeight();
        }*/
        @Override
        public void handle(KeyEvent keyEvent) {
            if (keyEvent.isShortcutDown()) {
                if (keyEvent.getCode() == KeyCode.S) {
                    fh.Save(keh.dh.returnRawData());
                } else if (keyEvent.getCode() == KeyCode.Z) {
                    System.out.println(MESSAGE_PREFIX + " in addition to \"z\"");
                }
            }else {
                if (keyEvent.getEventType() == KeyEvent.KEY_TYPED) {
                    // Use the KEY_TYPED event rather than KEY_PRESSED for letter keys, because with
                    // the KEY_TYPED event, javafx handles the "Shift" key and associated
                    // capitalization.
                    String characterTyped = keyEvent.getCharacter();
                    if (characterTyped.length() > 0 && characterTyped.charAt(0) != 8) {
                        // Ignore control keys, which have non-zero length, as well as the backspace
                        // key, which is represented as a character of value = 8 on Windows.
                        if (characterTyped.charAt(0) == '\r'){
                            characterTyped=String.valueOf('\n');
                        }
                        dh.append(characterTyped);
                        dh.render();
                        int i=dh.getCursorIndex();
                        dh.setCursor(i);
                        keyEvent.consume();
                    }

                } else if (keyEvent.getEventType() == KeyEvent.KEY_PRESSED) {
                    // Arrow keys should be processed using the KEY_PRESSED event, because KEY_PRESSED
                    // events have a code that we can check (KEY_TYPED events don't have an associated
                    // KeyCode).
                    KeyCode code = keyEvent.getCode();
                    if (code == KeyCode.UP) {
                        dh.cursorUp();
                    /*fontSize += 5;
                    displayText.setFont(Font.font(fontName, fontSize));
                    displayText.setText(print(windWidth));
                    UpdateBoundingBox();*/
                    } else if (code == KeyCode.DOWN) {
                        dh.cursorDown();
                    /*fontSize = Math.max(0, fontSize - 5);
                    displayText.setFont(Font.font(fontName, fontSize));
                    displayText.setText(print(windWidth));
                    UpdateBoundingBox();*/
                    } else if (code == KeyCode.BACK_SPACE && dh.getSize()>0){
                    	dh.delete();
                        dh.render();
                    } else if (code == KeyCode.LEFT){
                        dh.cursorLeft();
                    } else if (code == KeyCode.RIGHT){
                        dh.cursorRight();
                    }
                }
            }
        }
    }


        

    public void editFile(){
    	if(argu.length>1){
	    	if (argu[0].equals("-edit")){
	    		if (argu.length<3){
	    			System.out.println("Expected input: '-edit InputFileName OutputFileName'");
	    		}else{
	    			fh=new FileHandler(argu[1], argu[2]);
		    		String read=fh.Read();
		    		keh.dh.setRawData(read);
		    		keh.dh.setCursorBack();
		    		keh.dh.updateCursorPos();
		    		keh.dh.render();
	    		}
	    	}
    		
    	}
    }



    private class RectangleBlinkEventHandler implements EventHandler<ActionEvent> {
        private int currentColorIndex = 0;
        private Color[] boxColors ={Color.BLACK, Color.WHITE};
        //{Color.LIGHTPINK, Color.ORANGE, Color.YELLOW,
        //      Color.GREEN, Color.LIGHTBLUE, Color.PURPLE};

        RectangleBlinkEventHandler() {
            // Set the color to be the first color in the list.
            changeColor();
        }

        private void changeColor() {
            textBoundingBox.setFill(boxColors[currentColorIndex]);
            currentColorIndex = (currentColorIndex + 1) % boxColors.length;
        }

        @Override
        public void handle(ActionEvent event) {
            changeColor();
        }
    }


    /** Makes the text bounding box change color periodically. */
    public void makeRectangleColorChange() {
        // Create a Timeline that will call the "handle" function of RectangleBlinkEventHandler
        // every 1 second.
        final Timeline timeline = new Timeline();
        // The rectangle should continue blinking forever.
        timeline.setCycleCount(Timeline.INDEFINITE);
        Editor.RectangleBlinkEventHandler cursorChange = new Editor.RectangleBlinkEventHandler();
        KeyFrame keyFrame = new KeyFrame(Duration.seconds(0.5), cursorChange);
        timeline.getKeyFrames().add(keyFrame);
        timeline.play();
    }
    @Override
    public void start(Stage primaryStage) {
        // Create a Node that will be the parent of all things displayed on the screen.
        Group root = new Group();
        // The Scene represents the window: its height and width will be the height and width
        // of the window displayed.
        Scene scene = new Scene(root, winWidth, winHeight, Color.WHITE);
        // To get information about what keys the user is pressing, create an EventHandler.
        // EventHandler subclasses must override the "handle" function, which will be called
        // by javafx.

        EventHandler<KeyEvent> keyEventHandler =
                new Editor.KeyEventHandler(root, (int)winWidth, (int)winHeight);
        // Register the event handler to be called for all KEY_PRESSED and KEY_TYPED events.
        scene.setOnKeyTyped(keyEventHandler);
        scene.setOnKeyPressed(keyEventHandler);


        root.getChildren().add(textBoundingBox);
        makeRectangleColorChange();

        primaryStage.setTitle("Allan's Simple Editor");

    	String s;
        keh.dh.displayText.setTextOrigin(VPos.TOP);
        keh.dh.displayText.setFont(Font.font(fontName, fontSize));
        // All new Nodes need to be added to the root in order to be displayed.
        root.getChildren().add(keh.dh.displayText);
    	editFile();
        // This is boilerplate, necessary to setup the window where things are displayed.
        primaryStage.setScene(scene);
        primaryStage.show();
        scene.widthProperty().addListener(new ChangeListener<Number>() {
            @Override public void changed(
                    ObservableValue<? extends Number> observableValue,
                    Number oldScreenWidth,
                    Number newScreenWidth) {
                // Re-compute Allen's width.
                winWidth =(double) newScreenWidth;
                keh.dh.setWindWidth(winWidth);
                keh.dh.displayText.setText(keh.dh.print());
            }
        });
        /*scene.heightProperty().addListener(new ChangeListener<Number>() {
            @Override public void changed(
                    ObservableValue<? extends Number> observableValue,
                    Number oldScreenHeight,
                    Number newScreenHeight) {
                int height = getDimensionInsideMargin(newScreenHeight.intValue());
                allenView.setFitHeight(newAllenHeight);
            }
        }*/

    }

    public static void main(String[] args) {
    	argu=args;
    	launch(args);
    }
}