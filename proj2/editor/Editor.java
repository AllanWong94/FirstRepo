//package editor;

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
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import java.util.ArrayList;


public class Editor extends Application {
    private final Rectangle textBoundingBox = new Rectangle(0, 0);
    private static final int WINDOW_MARGIN = 5;
    private static final int WINDOW_HEIGHT = 500;
    private double winWidth=500;
    private double winHeight=500;
    private double charwidth;
    private double charheight;
    private double spacing;
    private double cursorPosY;
    private double cursorPosX;
    private KeyEventHandler keh;



    private class KeyEventHandler implements EventHandler<KeyEvent> {
        private int textX;
        private int textY;
        private int windWidth;
        private int windHeight;
        private static final int STARTING_FONT_SIZE = 20;
        private ArrayList<double[]> cursorPos;

        /** The Text to display on the screen. */
        private Text displayText = new Text(textX, textY, "");
        private int fontSize = STARTING_FONT_SIZE;
        private String fontName = "Verdana";
        private String rawData;
        int size;


        /** An EventHandler to handle keys that get pressed. */
        KeyEventHandler(final Group root, int windowWidth, int windowHeight) {
            keh=this;
            textX = WINDOW_MARGIN;
            textY = 0;
            windWidth=windowWidth;
            windHeight=windowHeight;
            cursorPos=new ArrayList<double[]>();
            double[] k=new double[]{cursorPosX, cursorPosY};
            cursorPos.add(k);
            // Initialize some empty text and add it to root so that it will be displayed.
            displayText = new Text(textX, textY, "");
            // Always set the text origin to be VPos.TOP! Setting the origin to be VPos.TOP means
            // that when the text is assigned a y-position, that position corresponds to the
            // highest position across all letters (for example, the top of a letter like "I", as
            // opposed to the top of a letter like "e"), which makes calculating positions much
            // simpler!
            displayText.setTextOrigin(VPos.TOP);
            displayText.setFont(Font.font(fontName, fontSize));
            size=0;
            // All new Nodes need to be added to the root in order to be displayed.
            root.getChildren().add(displayText);
        }


        /*Return a string that is ready to be displayed by the displayText with the window width*/
        public String print(double width){
            String temp=null;
            char c;
            double linewidth;
            int lastIndex=0;
            cursorPosX=WINDOW_MARGIN;
            cursorPosY=0;
            if (size==0)
                return null;
            for (int i=0; i<rawData.length();i++) {
                c = rawData.charAt(i);
                if (i == 0){
                    temp = String.valueOf(c);
                }
                else{
                    temp=temp+c;
                }
                charwidth=measureCharWidth(c);
                charheight=measureCharHeight(c);
                spacing=displayText.getLineSpacing();
                if (c=='\n'){
                    nextLine(spacing, charheight/2);
                }else {
                    cursorPosX += charwidth;
                }
                displayText.setText(temp);
                linewidth=displayText.getLayoutBounds().getWidth();
                if (linewidth>width-2*WINDOW_MARGIN){
                    lastIndex=findLastBlank(temp);
                    if (lastIndex<0){
                        temp=temp.substring(0,temp.length()-1)+"\r\n"+rawData.charAt(i);
                        nextLine(spacing, charheight);
                        cursorPosX+=charwidth;
                    }else{
                        String latter=temp.substring(lastIndex+1, temp.length());
                        temp=temp.substring(0,lastIndex)+"\r\n"+latter;
                        nextLine(spacing, charheight);
                        cursorPosX+=measureStringWidth(latter);
                    }

                    UpdateBoundingBox();
                }

                if (i==rawData.length()-1){
                    double[] pos=new double[2];
                    pos[0]=cursorPosX;
                    pos[1]=cursorPosY;
                    cursorPos.add(pos);
                }
            }
            printArray(cursorPos);
            return temp;
        }

        public void printArray(ArrayList<double[]> a){
            System.out.println("\nPrinting array:");
            for (int i=0;i<a.size();i++){
                double[] temp=a.get(i);
                System.out.println("Cursor #"+i+":");
                System.out.println(temp[0]);
                System.out.println(temp[1]);
            }
        }

        public double measureStringWidth(String s){
            double l=0;
            displayText.setText(s);
            l=displayText.getLayoutBounds().getWidth();
            return l;
        }

        public double measureCharHeight(char c){
            displayText.setText(String.valueOf(c));
            return displayText.getLayoutBounds().getHeight();
        }

        public double measureCharWidth(char c){
            displayText.setText(String.valueOf(c));
            return displayText.getLayoutBounds().getWidth();
        }

        public void render() {
            displayText.setText(print(winWidth));
        }
        /*public void updateCharSize(String s){
            displayText.setText(s);
            charwidth=displayText.getLayoutBounds().getWidth();
            charheight=displayText.getLayoutBounds().getHeight();
        }*/
        @Override
        public void handle(KeyEvent keyEvent) {
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
                    if (size == 0) {
                        rawData = characterTyped;
                    } else {
                        rawData = rawData + characterTyped;
                    }
                    size += 1;
                    render();
                    UpdateBoundingBox();
                    keyEvent.consume();
                }

            } else if (keyEvent.getEventType() == KeyEvent.KEY_PRESSED) {
                // Arrow keys should be processed using the KEY_PRESSED event, because KEY_PRESSED
                // events have a code that we can check (KEY_TYPED events don't have an associated
                // KeyCode).
                KeyCode code = keyEvent.getCode();
                if (code == KeyCode.UP) {
                    fontSize += 5;
                    displayText.setFont(Font.font(fontName, fontSize));
                    displayText.setText(print(windWidth));
                    UpdateBoundingBox();
                } else if (code == KeyCode.DOWN) {
                    fontSize = Math.max(0, fontSize - 5);
                    displayText.setFont(Font.font(fontName, fontSize));
                    displayText.setText(print(windWidth));
                    UpdateBoundingBox();
                } else if (code == KeyCode.BACK_SPACE && size>0){
                    rawData=rawData.substring(0, size-1);
                    size-=1;
                    displayText.setText(print(windWidth));
                    UpdateBoundingBox();
                } /*else if (code == KeyCode.ENTER){
                    nextLine(spacing, charheight);
                    UpdateBoundingBox();
                }*/
            }
        }

        public void nextLine(double spacing, double charh){
            cursorPosY+=(spacing+charh);
            cursorPosX=WINDOW_MARGIN;
        }

        public void setCursor(){
        }


        public void UpdateBoundingBox() {
            textBoundingBox.setHeight(charheight);
            textBoundingBox.setWidth(1);
            textBoundingBox.setX(cursorPosX);
            textBoundingBox.setY(cursorPosY);
            displayText.toFront();
        }

        public int findLastBlank(String s){
            return s.lastIndexOf(" ");
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

        primaryStage.setTitle("Single Letter Display Simple");

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
                keh.displayText.setText(keh.print(winWidth));
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
        launch(args);
    }
}