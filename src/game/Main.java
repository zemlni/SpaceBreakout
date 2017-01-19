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
	public static final int PADDLE_STEP = 100;
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
	private String status;

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
		// animation.play();

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

		level = new Text("Level: " + player.getLevel());
		level.setFill(Color.WHITE);
		root.getChildren().add(level);
		level.setX(10);
		level.setY(20);

		lives = new Text("Lives: " + player.getLives());
		lives.setFill(Color.WHITE);
		root.getChildren().add(lives);
		lives.setX(150);
		lives.setY(20);

		score = new Text("Score: " + player.getScore());
		score.setFill(Color.WHITE);
		root.getChildren().add(score);
		score.setX(300);
		score.setY(20);

		// set up paddle
		Image ship = new Image(getClass().getClassLoader().getResourceAsStream("ship.png"));
		paddle = new ImageView(ship);
		root.getChildren().add(paddle);
		resetPaddle();

		bouncers = new ArrayList<Bouncer>();
		planets = new ArrayList<Planet>();
		status = "beginning";
		showTransitionScreen("beginning");

		// respond to input
		myScene.setOnKeyPressed(e -> handleKeyPressed(e.getCode()));
		myScene.setOnKeyReleased(e -> handleKeyReleased(e.getCode()));
		// myScene.setOnMouseClicked(e -> handleMouseInput(e.getX(), e.getY()));
		return myScene;
	}

	private void resetPaddle() {
		paddle.setFitHeight(10);
		paddle.setFitWidth(50);
		paddle.setX(SIZE / 2 - paddle.getBoundsInLocal().getWidth() / 2);
		paddle.setY(SIZE - paddle.getBoundsInLocal().getHeight());
		paddleLeft = null;
	}

	private void setUpLevel(int level) {
		clearLevel();
		resetPaddle();
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
		asteroid.getBouncer().setX(paddle.getX() + paddle.getBoundsInLocal().getWidth() / 2
				- asteroid.getBouncer().getBoundsInLocal().getWidth() / 2);
		asteroid.getBouncer().setY(paddle.getY() - asteroid.getBouncer().getBoundsInLocal().getHeight());
	}

	private void clearLevel() {
		root.getChildren().remove(screen);
		for (Planet planet : planets) {
			root.getChildren().remove(planet.getPlanet());
		}
		for (Bouncer bouncer : bouncers) {
			root.getChildren().remove(bouncer.getBouncer());
		}
		planets.clear();
		bouncers.clear();
	}

	private void clearScreen() {
		root.getChildren().remove(screen);
		for (Planet planet : planets) {
			root.getChildren().remove(planet.getPlanet());
		}
		for (Bouncer bouncer : bouncers) {
			root.getChildren().remove(bouncer.getBouncer());
		}
	}

	private void resetScreen() {
		root.getChildren().remove(screen);
		resetPaddle();
		for (Planet planet : planets) {
			root.getChildren().add(planet.getPlanet());
		}
		Bouncer asteroid = new Bouncer();

		asteroid.getBouncer().setX(paddle.getX() + paddle.getBoundsInLocal().getWidth() / 2
				- asteroid.getBouncer().getBoundsInLocal().getWidth() / 2);
		asteroid.getBouncer().setY(paddle.getY() - asteroid.getBouncer().getBoundsInLocal().getHeight());
		for (Bouncer bouncer : bouncers) {
			root.getChildren().remove(bouncer.getBouncer());
		}
		bouncers.clear();
		bouncers.add(asteroid);
		root.getChildren().add(asteroid.getBouncer());
	}

	public void showTransitionScreen(String beginning) {
		// animation.pause();
		clearScreen();
		if (beginning.equals("beginning")) {
			screen = new Text(
					"Welcome to Space Breakout!\nPress enter to view the level \nand enter again to start.\nUse left and right keys to move \nthe ship.\n"
							+ "Destroy all the planets to win!\nYou must destroy earth last for \nthe humans to escape safely!\n"
							+ "Good luck!");
		} else if (beginning.equals("life")) {
			screen = new Text("You lost a life\nBe careful!\nGood luck!\nPress enter to try again \nor q to quit.");
		} else if (beginning.equals("level")) {
			screen = new Text(
					"You passed the level\nGood job!\nPress enter to view the \nnext level and enter again to \nstart or q to quit.");
		} else if (beginning.equals("end")) {
			screen = new Text(
					"You lost\nBetter luck next time!\nPress enter to view the \nfirst level again and enter again \nto start or q to quit.");
		} else if (beginning.equals("quit")) {
			screen = new Text("See you later!\nPress enter to start again \nor q to quit.");
		} else {// (beginning.equals("win")){
			screen = new Text("You won!\nCongratulations!\nPress enter to start again \nor q to quit.");
		}
		screen.setFont(Font.font("System Regular", FontWeight.BOLD, 20));
		screen.setFill(Color.WHITE);
		root.getChildren().add(screen);
		screen.setX(40);
		screen.setY(150);
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
		if (bouncers.size() == 0) {
			if (status.equals("play")) {
				if (player.getLives() == 0) {
					status = "end";
					// player.reset();
					showTransitionScreen(status);
				} else {
					player.decrementLives();
					// player.resetScore();
					status = "life";
					showTransitionScreen(status);
				}
			} else if (status.equals("wait")) {
				setUpLevel(player.getLevel());
				System.out.println("up here");
			}
		}

		else if (planets.size() == 0) {
			if (status.equals("play")) {
				player.incrementLevel();
				System.out.println("passlevel" + player.getLevel());
				if (player.getLevel() > Player.MAX_LEVELS) {
					status = "win";
					showTransitionScreen(status);
					// player.reset();

				} else {
					System.out.println("In here");
					status = "level";
					showTransitionScreen(status);
				}
			} else if (status.equals("wait")) {
				setUpLevel(player.getLevel());
				System.out.println("up here 2");
			}
		}
		step(elapsedTime);
	}

	private void step(double elapsedTime) {
		if (player.getStep() % (PADDLE_STEP / player.getLevel()) == 0 && status.equals("play"))
			paddle.setY(paddle.getY() - 1);
		player.step();
		for (Planet planet : planets) {
			if (planet.getPlanet().getBoundsInParent().intersects(paddle.getBoundsInParent())) {
				for (Bouncer bouncer : bouncers) {
					root.getChildren().remove(bouncer.getBouncer());
				}
				bouncers.clear();
				return;
			}

		}
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
					if (planets.get(j).getSize() == LARGE_PLANET_SIZE) {
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
			/*
			 * for (int b = 0; b < bouncers.size(); b++) { if (b != i) { if
			 * (bouncers.get(i).getBouncer().getBoundsInParent()
			 * .intersects(bouncers.get(b).getBouncer().getBoundsInParent())) {
			 * bouncers.get(i).setLeft(!bouncers.get(i).getLeft());
			 * bouncers.get(b).setLeft(!bouncers.get(b).getLeft()); } } }
			 */

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
		// transitionRoot.getChildren().remove(screen);
		if (code == KeyCode.RIGHT && paddle.getX() < (SIZE - paddle.getBoundsInLocal().getWidth())) {
			paddleLeft = false;
			paddle.setX(paddle.getX() + KEY_INPUT_SPEED);
			if (status.equals("wait")) {
				bouncers.get(0).getBouncer().setX(paddle.getX() + paddle.getBoundsInLocal().getWidth() / 2
						- bouncers.get(0).getBouncer().getBoundsInLocal().getWidth() / 2);
			}
		} else if (code == KeyCode.LEFT && paddle.getX() > 0) {
			paddleLeft = true;
			paddle.setX(paddle.getX() - KEY_INPUT_SPEED);
			if (status.equals("wait")) {
				bouncers.get(0).getBouncer().setX(paddle.getX() + paddle.getBoundsInLocal().getWidth() / 2
						- bouncers.get(0).getBouncer().getBoundsInLocal().getWidth() / 2);
			}
			System.out.println(status);
		} else if (code == KeyCode.ENTER) {
			if (status.equals("wait")) {
				System.out.println("here instead");
				status = "play";
				animation.play();
			} else if (status == null || status.equals("level") || status.equals("beginning")) {
				System.out.println("wait here");
				status = "wait";
				animation.pause();
				setUpLevel(player.getLevel());
			} else if (status.equals("life")) {
				System.out.println("in life");
				status = "wait";
				animation.pause();
				resetScreen();
			} else if (status.equals("play")) {
				animation.pause();
				status = "wait";
			} else if (status.equals("win") || status.equals("end") || status.equals("quit")) {
				animation.pause();
				System.out.println("here");
				player.reset();
				status = "beginning";
				showTransitionScreen(status);
			}
		} else if (code == KeyCode.DIGIT1) {
			status = "level";
			player.setLevel(1);
			showTransitionScreen("beginning");
		} else if (code == KeyCode.DIGIT2) {
			player.setLevel(2);
			status = "level";
			showTransitionScreen("level");
		} else if (code == KeyCode.DIGIT3) {
			status = "level";
			player.setLevel(3);
			showTransitionScreen("level");
		} else if (code == KeyCode.L) {
			player.incrementLives();
		} else if (code == KeyCode.N) {
			player.incrementLevel();
			if (player.getLevel() > Player.MAX_LEVELS) {
				status = "win";
				showTransitionScreen(status);
			} else {
				status = "level";
				showTransitionScreen(status);
				setUpLevel(player.getLevel());
			}
		} else if (code == KeyCode.Q) {
			if (status.equals("quit"))
				stage.close();
			else {
				player.reset();
				status = "quit";
				showTransitionScreen("quit");
			}

		} else if (code == KeyCode.D) {
			if (paddle.getFitWidth() <= SIZE / 2)
				paddle.setFitWidth(paddle.getFitWidth() * 2);
		} else if (code == KeyCode.A) {
			for (Bouncer bouncer : bouncers) {
				if (!bouncer.getBig())
					bouncer.setBig();
			}
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
