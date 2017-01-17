package game;

import java.util.ArrayList;
import java.util.HashMap;

import javafx.animation.Animation.Status;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Main extends Application {
	public static final String TITLE = "Space Breakout";
	public static final int SIZE = 400;
	public static final Paint BACKGROUND = Color.WHITE;
	public static final int FRAMES_PER_SECOND = 60;
	public static final int MILLISECOND_DELAY = 1000 / FRAMES_PER_SECOND;
	public static final double SECOND_DELAY = 1.0 / FRAMES_PER_SECOND;
	public static final int KEY_INPUT_SPEED = 5;
	public static final int SCOREBOARD_HEIGHT = 30;
	public static final int LARGE_PLANET_SIZE = 75;
	public static final int SMALL_PLANET_SIZE = 50;
	public static final LevelsContainer LEVELS = new LevelsContainer();
	private Timeline animation;
	private Boolean paddleLeft;
	private ArrayList<Bouncer> bouncers;
	private ArrayList<Planet> planets;
	private Rectangle scoreBoard;
	private Group root;
	private Text level;
	private Text lives;
	private Text score;
	private Text screen;
	private Scene myScene;
	private ImageView paddle;
	private Player player;
	private Stage stage;
	private KeyCode previousCode;

	/**
	 * Initialize what will be displayed and how it will be updated.
	 */
	@Override
	public void start(Stage s) {
		// attach scene to the stage and display it
		stage = s;
		// attach "game loop" to timeline to play it
		// KeyFrame frame = new KeyFrame(Duration.millis(MILLISECOND_DELAY), e
		// -> step(SECOND_DELAY));
		KeyFrame frame = new KeyFrame(Duration.millis(MILLISECOND_DELAY), e -> gameController(SECOND_DELAY));
		animation = new Timeline();
		animation.setCycleCount(Timeline.INDEFINITE);
		animation.getKeyFrames().add(frame);
		animation.play();

		Scene scene = setupGame(SIZE, SIZE, BACKGROUND);
		s.setScene(scene);
		s.setTitle(TITLE);
		s.show();

	}

	// Create the game's "scene": what shapes will be in the game and their
	// starting properties
	private Scene setupGame(int width, int height, Paint background) {
		// create one top level collection to organize the things in the scene
		root = new Group();
		// create a place to see the shapes
		myScene = new Scene(root, width, height, background);
		player = new Player();

		// set background
		Image space = new Image(getClass().getClassLoader().getResourceAsStream("space.png"), SIZE, SIZE, false, false);
		ImagePattern pattern = new ImagePattern(space);
		myScene.setFill(pattern);

		// set up scoreboard
		scoreBoard = new Rectangle(SIZE, SCOREBOARD_HEIGHT);
		root.getChildren().add(scoreBoard);
		scoreBoard.setX(0);
		scoreBoard.setY(0);
		scoreBoard.setFill(Color.BLACK);

		level = new Text("Level: 1");
		level.setFill(Color.WHITE);
		root.getChildren().add(level);
		level.setX(10);
		level.setY(20);

		lives = new Text("Lives: 3");
		lives.setFill(Color.WHITE);
		root.getChildren().add(lives);
		lives.setX(150);
		lives.setY(20);

		score = new Text("Score: 0");
		score.setFill(Color.WHITE);
		root.getChildren().add(score);
		score.setX(300);
		score.setY(20);

		// set up paddle
		Image ship = new Image(getClass().getClassLoader().getResourceAsStream("ship.png"));
		paddle = new ImageView(ship);
		paddle.setFitHeight(10);
		paddle.setFitWidth(50);
		root.getChildren().add(paddle);
		paddle.setX(SIZE / 2 - paddle.getBoundsInLocal().getWidth() / 2);
		paddle.setY(SIZE - paddle.getBoundsInLocal().getHeight());
		paddleLeft = null;

		bouncers = new ArrayList<Bouncer>();
		planets = new ArrayList<Planet>();
		showTransitionScreen("beginning");
		setUpLevel(1);
		System.out.println("setup");

		// respond to input
		myScene.setOnKeyPressed(e -> handleKeyPressed(e.getCode()));
		myScene.setOnKeyReleased(e -> handleKeyReleased(e.getCode()));
		// myScene.setOnMouseClicked(e -> handleMouseInput(e.getX(), e.getY()));
		return myScene;
	}

	private void setUpLevel(int level) {
		ArrayList<PlanetLoc> curLevel = LEVELS.getLevel(level);
		for (PlanetLoc loc : curLevel) {
			Planet temp = new Planet(loc.getName() + ".png", sizeDecider(loc.getName()));
			planets.add(temp);
			root.getChildren().add(temp.getPlanet());
			temp.getPlanet().setX(loc.getX());
			temp.getPlanet().setY(loc.getY());
		}

		// setup bouncer
		Bouncer asteroid = new Bouncer();
		bouncers.add(asteroid);
		root.getChildren().add(asteroid.getBouncer());
		asteroid.getBouncer().setX(paddle.getX() + paddle.getBoundsInLocal().getWidth() / 2);
		asteroid.getBouncer().setY(paddle.getY() - asteroid.getBouncer().getBoundsInLocal().getHeight());
	}

	public void clearLevel() {
		for (Planet planet : planets) {
			root.getChildren().remove(planet.getPlanet());
		}
		for (Bouncer bouncer : bouncers) {
			root.getChildren().remove(bouncer.getBouncer());
		}
		planets.clear();
		bouncers.clear();
	}

	public void showTransitionScreen(String beginning) {
		clearLevel();
		if (beginning.equals("beginning")) {
			screen = new Text(
					"Welcome to Space Breakout!\nPress enter to begin.\nUse left and right keys to move \nthe ship.\n"
							+ "Destroy all the planets to win!\nYou must destroy earth last for \nthe humans to escape safely!\n"
							+ "Good luck!");
		} else if (beginning.equals("life")) {
			screen = new Text("You lost a life\nBe careful!\nGood luck!\nPress enter to try again \nor q to quit.");
		} else if (beginning.equals("level")) {
			screen = new Text("You passed the level\nGood job!\nPress enter to begin the \nnext level or q to quit.");
		} else if (beginning.equals("end")) {
			screen = new Text("You lost\nBetter luck next time!\nPress enter to start again \nor q to quit.");
		} else if (beginning.equals("quit")) {
			screen = new Text("See you later!\nPress enter to start again or q to quit.");
		} else {// (beginning.equals("win")){
			screen = new Text("You won!\nCongratulations!\nPress enter to start again \nor q to quit.");
		}
		System.out.println("transition");
		// root.getChildren().add(screen);
		screen.setFont(Font.font("System Regular", FontWeight.BOLD, 20));
		screen.setFill(Color.WHITE);
		root.getChildren().add(screen);
		screen.setX(40);
		screen.setY(150);
		// if (animation.getStatus() == Status.RUNNING)
		animation.pause();
	}

	private int sizeDecider(String name) {
		switch (name) {
		case "mercury":
		case "venus":
		case "mars":
		case "earth":
		case "pluto":
			return SMALL_PLANET_SIZE;
		case "jupiter":
		case "saturn":
		case "neptune":
		case "uranus":
			return LARGE_PLANET_SIZE;
		default:
			return -1;
		}
	}

	private void gameController(double elapsedTime) {
		// System.out.println("gamecontroller");
		if (bouncers.size() == 0) {
			if (player.getLives() == 0) {
				showTransitionScreen("end");
			} else {
				// resetup current level
				System.out.println(player.getLevel());
				player.decrementLives();
				player.resetScore();
				showTransitionScreen("life");
				setUpLevel(player.getLevel());

				// show transition window
				// wait till enter key is pressed;
			}
		}

		if (planets.size() == 0) {
			player.incrementLevel();
			System.out.println("passlevel");
			if (player.getLevel() >= Player.MAX_LEVELS) {
				showTransitionScreen("win");
				player.setLevel(1);
				setUpLevel(player.getLevel());

			} else {
				showTransitionScreen("level");
				setUpLevel(player.getLevel());
			}
		}

		step(elapsedTime);
	}

	// Change properties of shapes to animate them
	// Note, there are more sophisticated ways to animate shapes, but these
	// simple ways work fine to start.
	private void step(double elapsedTime) {
		// update attributes
		for (int i = 0; i < bouncers.size(); i++) {

			// check if any planets have been hit
			// for (Planet planet: planets){
			for (int j = 0; j < planets.size(); j++) {
				Point planetCenter = getCenter(planets.get(j).getPlanet());
				Point bouncerCenter = getCenter(bouncers.get(i).getBouncer());
				int distance = Point.distance(planetCenter, bouncerCenter);
				int sum = bouncers.get(i).getRadius() + planets.get(j).getRadius();
				if (distance <= sum) {
					bouncers.get(i).setUp(false);
					// bouncers.get(i).setLeft(!bouncers.get(i).getLeft());
					planets.get(j).incrementHits();
					System.out.println("HIT");
				}
				if (planets.get(j).getHits() >= planets.get(j).getMaxHits()) {
					// System.out.println(planet.getHits() + " " +
					// planet.getMaxHits());
					planets.get(j).destroy();
					root.getChildren().remove(planets.get(j).getPlanet());
					Bouncer temp = new Bouncer();
					root.getChildren().add(temp.getBouncer());
					bouncers.add(temp);
					temp.getBouncer().setX(planetCenter.getX());
					temp.getBouncer().setY(planetCenter.getY());
					if (planets.get(j).getSize() == 70) {
						Bouncer temp1 = new Bouncer();
						root.getChildren().add(temp1.getBouncer());
						bouncers.add(temp1);
						temp1.getBouncer().setX(planetCenter.getX());
						temp1.getBouncer().setY(planetCenter.getY());
					}
					player.incrementScore();
					planets.remove(j);
					j--;
				}
				// two circles intersect when distance between centers <= sum of
				// radii
			}

			// check if bouncer is hitting any other bouncers
			for (int b = 0; b < bouncers.size(); b++) {
				if (b != i) {
					if (bouncers.get(i).getBouncer().getBoundsInParent()
							.intersects(bouncers.get(b).getBouncer().getBoundsInParent())) {
						bouncers.get(i).setLeft(!bouncers.get(i).getLeft());
						bouncers.get(b).setLeft(!bouncers.get(b).getLeft());
					}
				}
			}

			if (paddle.getBoundsInParent().intersects(bouncers.get(i).getBouncer().getBoundsInParent())) {
				// bouncer.setLeft(!bouncer.getLeft());
				bouncers.get(i).setUp(true);
				if (paddleLeft != null) {
					if (paddleLeft.equals(bouncers.get(i).getLeft()))
						bouncers.get(i).setSpeedX(bouncers.get(i).getSpeedX() + KEY_INPUT_SPEED);
					else
						bouncers.get(i).setSpeedX(bouncers.get(i).getSpeedX() - KEY_INPUT_SPEED);
					System.out.println(bouncers.get(i).getSpeedX());
				}
			}

			double locationx = bouncers.get(i).getBouncer().getX();

			if (locationx >= SIZE - bouncers.get(i).getBouncer().getBoundsInLocal().getWidth()) {
				bouncers.get(i).setLeft(true);
			}
			if (locationx <= 0) {
				bouncers.get(i).setLeft(false);
			}
			if (bouncers.get(i).getLeft() == true) {
				bouncers.get(i).getBouncer().setX(locationx - bouncers.get(i).getSpeedX() * elapsedTime);
			}
			if (bouncers.get(i).getLeft() == false) {
				bouncers.get(i).getBouncer().setX(locationx + bouncers.get(i).getSpeedX() * elapsedTime);
			}

			double locationy = bouncers.get(i).getBouncer().getY();

			if (locationy >= SIZE - bouncers.get(i).getBouncer().getBoundsInLocal().getWidth()) {
				// bouncer.setUp(true);
				bouncers.get(i).destroy();
				root.getChildren().remove(bouncers.get(i).getBouncer());
				bouncers.remove(i);
				i--;
				continue;
			}
			if (locationy <= SCOREBOARD_HEIGHT) {
				bouncers.get(i).setUp(false);
			}
			if (bouncers.get(i).getUp() == true) {
				bouncers.get(i).getBouncer().setY(locationy - bouncers.get(i).getSpeedY() * elapsedTime);
			}
			if (bouncers.get(i).getUp() == false) {
				bouncers.get(i).getBouncer().setY(locationy + bouncers.get(i).getSpeedY() * elapsedTime);
			}
			score.setText("Score: " + player.getScore());
		}
		lives.setText("Lives: " + player.getLives());
		level.setText("Level: " + player.getLevel());

	}

	private Point getCenter(ImageView im) {
		int x = (int) (im.getX() + im.getBoundsInLocal().getWidth() / 2);
		int y = (int) (im.getY() + im.getBoundsInLocal().getHeight() / 2);
		return new Point(x, y);
	}

	// What to do each time a key is pressed
	private void handleKeyPressed(KeyCode code) {
		root.getChildren().remove(screen);
		if (code == KeyCode.RIGHT && paddle.getX() < (SIZE - paddle.getBoundsInLocal().getWidth())) {
			paddleLeft = false;
			paddle.setX(paddle.getX() + KEY_INPUT_SPEED);
		} else if (code == KeyCode.LEFT && paddle.getX() > 0) {
			paddleLeft = true;
			paddle.setX(paddle.getX() - KEY_INPUT_SPEED);
		} else if (code == KeyCode.ENTER) {
			// root.getChildren().remove(screen);
			if (previousCode == KeyCode.Q) {
				player.reset();
				showTransitionScreen("beginning");
			} else
				animation.play();
		} else if (code == KeyCode.DIGIT1) {
			player.setLevel(1);
			showTransitionScreen("beginning");
			setUpLevel(1);
		} else if (code == KeyCode.DIGIT2) {
			player.setLevel(2);
			showTransitionScreen("level");
			setUpLevel(2);
		} else if (code == KeyCode.L) {
			player.incrementLives();
		} else if (code == KeyCode.N) {
			player.incrementLevel();
			if (player.getLevel() > Player.MAX_LEVELS) {
				showTransitionScreen("win");
			} else {
				showTransitionScreen("level");
				setUpLevel(player.getLevel());
			}
		} else if (code == KeyCode.Q) {
			if (previousCode == KeyCode.Q)
				stage.close();
			else
				showTransitionScreen("quit");

		} else if (code == KeyCode.D) {
			paddle.setFitWidth(paddle.getFitWidth() * 2);
		}
		previousCode = code;

	}

	private void handleKeyReleased(KeyCode code) {
		if (code == KeyCode.RIGHT || code == KeyCode.LEFT)
			paddleLeft = null;
	}

	/**
	 * Start the program.
	 */
	public static void main(String[] args) {
		launch(args);
	}
}
