package game;

import java.util.ArrayList;
import java.util.HashMap;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;
import javafx.util.Duration;



public class Main extends Application {
	public static final String TITLE = "Example JavaFX";
	public static final String BALL_IMAGE = "ball.gif";
	public static final int SIZE = 400;
	public static final Paint BACKGROUND = Color.WHITE;
	public static final int FRAMES_PER_SECOND = 60;
	public static final int MILLISECOND_DELAY = 1000 / FRAMES_PER_SECOND;
	public static final double SECOND_DELAY = 1.0 / FRAMES_PER_SECOND;
	public static final int KEY_INPUT_SPEED = 5;
	public static final double GROWTH_RATE = 1.1;
	private boolean paddleLeft;
	private ArrayList<Bouncer> bouncers;
	private Planet[] planets;
	private Rectangle scoreBoard;
	private Group root;

	// some things we need to remember during our game
	private Scene myScene;

    private ImageView paddle;
	

	/**
	 * Initialize what will be displayed and how it will be updated.
	 */
	@Override
	public void start(Stage s) {
		// attach scene to the stage and display it
		Scene scene = setupGame(SIZE, SIZE, BACKGROUND);
		s.setScene(scene);
		s.setTitle(TITLE);
		s.show();
		// attach "game loop" to timeline to play it
		KeyFrame frame = new KeyFrame(Duration.millis(MILLISECOND_DELAY), e -> step(SECOND_DELAY));
		Timeline animation = new Timeline();
		animation.setCycleCount(Timeline.INDEFINITE);
		animation.getKeyFrames().add(frame);
		animation.play();
	}

	// Create the game's "scene": what shapes will be in the game and their
	// starting properties
	private Scene setupGame(int width, int height, Paint background) {
		// create one top level collection to organize the things in the scene
		root = new Group();
		// create a place to see the shapes
		myScene = new Scene(root, width, height, background);
		
		scoreBoard = new Rectangle(SIZE, 80);
		root.getChildren().add(scoreBoard);
		scoreBoard.setX(0);
		scoreBoard.setY(0);
		
		
		//set up paddle
		Image ship = new Image(getClass().getClassLoader().getResourceAsStream("ship.png"));
		paddle = new ImageView(ship);
		paddle.setFitHeight(10);
		paddle.setFitWidth(50);
		root.getChildren().add(paddle);
		paddle.setX(SIZE / 2 - paddle.getBoundsInLocal().getWidth() / 2);
		paddle.setY(SIZE - paddle.getBoundsInLocal().getHeight());
		
		//set up bouncer
        bouncers = new ArrayList<Bouncer>();
		for (int i = 0; i < 1; i++){
			Bouncer cur = new Bouncer();
			bouncers.add(cur);
			System.out.println("TEST1");
			root.getChildren().add(cur.getBouncer());
		}
		
		//set up planets (need to adjust for each level)
		planets = new Planet[2];
		int separation = SIZE/(planets.length + 1);
		int cur = separation;
		
		for (int i = 0; i < planets.length; i++){
			planets[i] = new Planet("earth.png", 3, 0);
			root.getChildren().add(planets[i].getPlanet());
			planets[i].getPlanet().setX(cur);
			planets[i].getPlanet().setY(0);
			cur += separation;
		}	
		
		// respond to input
		myScene.setOnKeyPressed(e -> handleKeyInput(e.getCode()));
		//myScene.setOnMouseClicked(e -> handleMouseInput(e.getX(), e.getY()));
		return myScene;
	}

	// Change properties of shapes to animate them
	// Note, there are more sophisticated ways to animate shapes, but these
	// simple ways work fine to start.
	private void step(double elapsedTime) {
		// update attributes
		for (Bouncer bouncer : bouncers) {
			if (bouncers.size() == 0)
				break;

			
			//check if any planets have been hit
			for (Planet planet: planets){
				Point planetCenter = getCenter(planet.getPlanet());
				Point bouncerCenter = getCenter(bouncer.getBouncer());
				int distance = Point.distance(planetCenter, bouncerCenter);
				int sum = bouncer.getRadius() + planet.getRadius();
				if (distance <= sum){
					bouncer.setUp(false);
					planet.incrementHits();
					System.out.println("HIT");
				}
				if (planet.getHits() >= planet.getMaxHits()){
					//System.out.println(planet.getHits() + " " + planet.getMaxHits());
					planet.destroy();
					root.getChildren().remove(planet.getPlanet());
					Bouncer temp = new Bouncer();
					root.getChildren().add(temp.getBouncer());
					temp.getBouncer().setX(planetCenter.getX());
					temp.getBouncer().setY(planetCenter.getY());
					if (planet.getSize() == 70){
						Bouncer temp1 = new Bouncer();
						root.getChildren().add(temp1.getBouncer());
						temp1.getBouncer().setX(planetCenter.getX());
						temp1.getBouncer().setY(planetCenter.getY());
					}
				}
				//two circles intersect when distance between centers <= sum of radii
			}
			
			if (paddle.getBoundsInParent().intersects(bouncer.getBouncer().getBoundsInParent())) {
				//bouncer.setLeft(!bouncer.getLeft());
				bouncer.setUp(true);
				if (bouncer.getLeft() == paddleLeft)
					bouncer.setSpeedX(bouncer.getSpeedX() + KEY_INPUT_SPEED);
				else
					bouncer.setSpeedX(bouncer.getSpeedX() - KEY_INPUT_SPEED);
				//System.out.println(bouncer.getSpeedX());
			}
			
			double locationx = bouncer.getBouncer().getX();

			if (locationx >= SIZE - bouncer.getBouncer().getBoundsInLocal().getWidth()) {
				bouncer.setLeft(true);
			}
			if (locationx <= 0) {
				bouncer.setLeft(false);
			}
			if (bouncer.getLeft() == true) {
				bouncer.getBouncer().setX(locationx - bouncer.getSpeedX() * elapsedTime);
			}
			if (bouncer.getLeft() == false) {
				bouncer.getBouncer().setX(locationx + bouncer.getSpeedX() * elapsedTime);
			}

			double locationy = bouncer.getBouncer().getY();

			if (locationy >= SIZE - bouncer.getBouncer().getBoundsInLocal().getWidth()) {
				//bouncer.setUp(true);
				bouncers.remove(bouncer);
				continue;
			}
			if (locationy <= 0) {
				bouncer.setUp(false);
			}
			if (bouncer.getUp() == true) {
				bouncer.getBouncer().setY(locationy - bouncer.getSpeedY() * elapsedTime);
			}
			if (bouncer.getUp() == false) {
				bouncer.getBouncer().setY(locationy + bouncer.getSpeedY() * elapsedTime);
			}
		}

	}
	
	private Point getCenter(ImageView im){
		int x = (int) (im.getX() +  im.getBoundsInLocal().getWidth()/2);
		int y = (int) (im.getY() +  im.getBoundsInLocal().getHeight()/2);
		return new Point(x, y);
	}
	
	// What to do each time a key is pressed
	private void handleKeyInput(KeyCode code) {
		if (code == KeyCode.RIGHT && paddle.getX() < (SIZE - paddle.getBoundsInLocal().getWidth())) {
			paddle.setX(paddle.getX() + KEY_INPUT_SPEED);
			paddleLeft = false;
		} else if (code == KeyCode.LEFT && paddle.getX() > 0) {
			paddle.setX(paddle.getX() - KEY_INPUT_SPEED);
			paddleLeft = true;
		}
	}


	/**
	 * Start the program.
	 */
	public static void main(String[] args) {
		launch(args);
	}
}
