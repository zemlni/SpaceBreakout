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
	private ArrayList<Bouncer> bouncers;
	private Planet[] planets;

	// some things we need to remember during our game
	private Scene myScene;

	private Rectangle myTopBlock;
	private Rectangle myBottomBlock;
	

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
		Group root = new Group();
		// create a place to see the shapes
		myScene = new Scene(root, width, height, background);
		// make some shapes and set their properties		
		HashMap<String, Integer> map = new HashMap<String, Integer>();
        bouncers = new ArrayList<Bouncer>();
		for (int i = 0; i < 1; i++){
			bouncers.set(i, new Bouncer());
			System.out.println("TEST1");
			root.getChildren().add(bouncers.get(i).getBouncer());
		}
		
		planets = new Planet[2];
		int separation = SIZE/(planets.length + 1);
		int cur = separation;
		
		for (int i = 0; i < planets.length; i++){
			planets[i] = new Planet("earth.png", 3, 0);
			root.getChildren().add(planets[i].getPlanet());
			planets[i].getPlanet().setX(separation);
			planets[i].getPlanet().setY(0);
			cur += separation;
		}	
		
		// respond to input
		myScene.setOnKeyPressed(e -> handleKeyInput(e.getCode()));
		myScene.setOnMouseClicked(e -> handleMouseInput(e.getX(), e.getY()));
		return myScene;
	}

	// Change properties of shapes to animate them
	// Note, there are more sophisticated ways to animate shapes, but these
	// simple ways work fine to start.
	private void step(double elapsedTime) {
		// update attributes
		for (Bouncer bouncer : bouncers) {
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
				bouncer.setUp(true);
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

	// What to do each time a key is pressed
	private void handleKeyInput(KeyCode code) {
		if (code == KeyCode.RIGHT) {
			myTopBlock.setX(myTopBlock.getX() + KEY_INPUT_SPEED);
		} else if (code == KeyCode.LEFT) {
			myTopBlock.setX(myTopBlock.getX() - KEY_INPUT_SPEED);
		} else if (code == KeyCode.UP) {
			myTopBlock.setY(myTopBlock.getY() - KEY_INPUT_SPEED);
		} else if (code == KeyCode.DOWN) {
			myTopBlock.setY(myTopBlock.getY() + KEY_INPUT_SPEED);
		}
	}

	// What to do each time a key is pressed
	private void handleMouseInput(double x, double y) {
		if (myBottomBlock.contains(x, y)) {
			myBottomBlock.setScaleX(myBottomBlock.getScaleX() * GROWTH_RATE);
			myBottomBlock.setScaleY(myBottomBlock.getScaleY() * GROWTH_RATE);
		}
	}

	/**
	 * Start the program.
	 */
	public static void main(String[] args) {
		launch(args);
	}
}
