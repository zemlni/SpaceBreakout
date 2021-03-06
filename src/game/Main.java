package game;
/**
 * Main class to run the game. 
 * Takes care of status display, transition screens and gameplay.
 * @author Nikita Zemlevskiy
 * Code borrowed from in class example from Robert Duvall.
 * 
 */
import java.util.ArrayList;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
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
	public static final int KEY_INPUT_SPEED = 10;
	public static final int SCOREBOARD_HEIGHT = 30;
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
	private String status;

	/**
	 * Initialize what will be displayed and how it will be updated.
	 * @param stage the stage with which to start the game
	 */
	@Override
	public void start(Stage s) {
		// attach scene to the stage and display it
		stage = s;
		// attach "game loop" to timeline to play it
		KeyFrame frame = new KeyFrame(Duration.millis(MILLISECOND_DELAY), e -> gameController(SECOND_DELAY));
		animation = new Timeline();
		animation.setCycleCount(Timeline.INDEFINITE);
		animation.getKeyFrames().add(frame);

		Scene scene = setupGame(SIZE, SIZE, BACKGROUND);
		s.setScene(scene);
		s.setTitle(TITLE);
		s.show();

	}
	/**
	 * Set up the scene of the game. Includes setting up 
	 * the scoreboard, setting up the background, the paddle
	 * initializing the levels container and the bouncer.
	 * 
	 * @param width width of the game window
	 * @param height height of the game window
	 * @param background Color of background
	 * @return Scene in which gameplay takes place
	 */
	private Scene setupGame(int width, int height, Paint background) {
		root = new Group();

		myScene = new Scene(root, width, height, background);
		player = new Player();

		Image space = new Image(getClass().getClassLoader().getResourceAsStream("space.png"), SIZE, SIZE, false, false);
		ImagePattern pattern = new ImagePattern(space);
		myScene.setFill(pattern);

		scoreBoard = new Rectangle(SIZE, SCOREBOARD_HEIGHT);
		setXYAndAdd(scoreBoard, 0, 0);
		scoreBoard.setFill(Color.BLACK);

		level = new Text("Level: " + player.getLevel());
		level.setFill(Color.WHITE);
		setXYAndAdd(level, 10, 20);

		lives = new Text("Lives: " + player.getLives());
		lives.setFill(Color.WHITE);
		setXYAndAdd(lives, 170, 20);

		score = new Text("Score: " + player.getScore());
		score.setFill(Color.WHITE);
		setXYAndAdd(score, 330, 20);

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
		return myScene;
	}
	/**
	 * Set X and Y coordinates of some Node
	 * @param element node to place in the screen
	 * @param x x coordinate
	 * @param y y coordinate
	 */
	private void setXY(Node element, double x, double y) {
		if (element instanceof Rectangle) {
			Rectangle rect = (Rectangle) element;
			rect.setX(x);
			rect.setY(y);
		} else if (element instanceof ImageView) {
			ImageView im = (ImageView) element;
			im.setX(x);
			im.setY(y);
		} else if (element instanceof Text) {
			Text text = (Text) element;
			text.setX(x);
			text.setY(y);
		}
	}
	/**
	 * Set X and Y coordinates and add to root
	 * @param element node to place in the screen
	 * @param x x coordinate
	 * @param y y coordinate
	 */
	private void setXYAndAdd(Node element, double x, double y) {
		setXY(element, x, y);
		root.getChildren().add(element);
	}
	/**
	 * Reset paddle location at beginning of level 
	 * or at a loss of a life
	 */
	private void resetPaddle() {
		paddle.setFitHeight(10);
		paddle.setFitWidth(50);
		setXY(paddle, SIZE / 2 - paddle.getBoundsInLocal().getWidth() / 2,
				SIZE - paddle.getBoundsInLocal().getHeight());
		paddleLeft = null;
	}
	/**
	 * Set up a level at beginning of game or at loss of a life
	 * @param level level to be set up
	 */
	private void setUpLevel(int level) {
		clearLevel();
		resetPaddle();
		ArrayList<PlanetLoc> curLevel = LEVELS.getLevel(level);
		for (PlanetLoc loc : curLevel) {
			Planet temp = new Planet(loc.getName());
			planets.add(temp);
			setXYAndAdd(temp.getPlanet(), loc.getX(), loc.getY());
		}
		makeBouncer(-1, -1);
	}
	/**
	 *Generate a new bouncer with given coordinates. 
	 *if coordinates are both -1 then will generate a bouncer
	 *at the coordinates of the paddle
	 *@param x x coordinate
	 *@param y y coordinate
	 */
	private void makeBouncer(double x, double y) {
		Bouncer temp1 = new Bouncer();
		bouncers.add(temp1);
		if (x == -1 && y == -1) {
			x = paddle.getX() + paddle.getBoundsInLocal().getWidth() / 2
					- temp1.getBouncer().getBoundsInLocal().getWidth() / 2;
			y = paddle.getY() - temp1.getBouncer().getBoundsInLocal().getHeight();
		}
		setXYAndAdd(temp1.getBouncer(), x, y);
	}
	/**
	 * clear level
	 * */
	private void clearLevel() {
		clearScreen();
		planets.clear();
		bouncers.clear();
	}
	/**
	 * clear screen
	 */
	private void clearScreen() {
		root.getChildren().remove(screen);
		for (Planet planet : planets) {
			root.getChildren().remove(planet.getPlanet());
		}
		removeBouncersFromRoot();
	}

	private void removeBouncersFromRoot() {
		for (Bouncer bouncer : bouncers) {
			root.getChildren().remove(bouncer.getBouncer());
		}
	}

	private void removeBouncersFromRootAndClear() {
		removeBouncersFromRoot();
		bouncers.clear();
	}

	private void removePlanetsFromRoot() {
		for (Planet planet : planets) {
			root.getChildren().add(planet.getPlanet());
		}
	}
	/**
	 * reset screen but do not remove planets. 
	 * Happens when life is lost
	 */
	private void resetScreen() {
		root.getChildren().remove(screen);
		resetPaddle();
		removePlanetsFromRoot();
		removeBouncersFromRootAndClear();
		makeBouncer(-1, -1);
	}
	/**
	 * Handle displaying transition screens
	 * @param beginning string to tell which transition screen to display
	 */
	public void showTransitionScreen(String beginning) {
		clearScreen();
		if (beginning.equals("beginning")) {
			screen = new Text(
					"Welcome to Space Breakout!\nPress enter to view the level \nand enter again to start.\nUse left and right keys to move \nthe ship.\n"
							+ "Destroy all the planets to win!\nYou must destroy earth last for \nthe humans to escape safely!\n"
							+ "Good luck!");
		} else if (beginning.equals("life")) {
			screen = new Text("You lost a life\nBe careful!\nGood luck!\nPress enter to try again \nor q to quit.");
		} else if (beginning.equals("level")) {
			if (player.getHumans())
				screen = new Text(
						"You passed the level and saved \nthe Humans!\nGood job!\nPress enter to view the \nnext level and enter again to \nstart or q to quit.");
			else
				screen = new Text(
						"You passed the level but did \nnot save the Humans :(\nDo better!\nYou are guilty!\nPress enter to view the \nnext level and enter again to \nstart or q to quit.");
		} else if (beginning.equals("end")) {
			screen = new Text(
					"You lost\nBetter luck next time!\nPress enter to view the \nfirst level again \nor q to quit.");
		} else if (beginning.equals("quit")) {
			screen = new Text("See you later!\nPress enter to start again \nor q to quit.");
		} else {// (beginning.equals("win")){
			if (player.getHumans())
				screen = new Text(
						"You won and saved the \nHumans!\nCongratulations!\nPress enter to start again \nor q to quit.");
			else
				screen = new Text(
						"You won but did not save \nthe Humans. \nTry harder. \nPress enter to start again \nor q to quit.");
		}
		screen.setFont(Font.font("System Regular", FontWeight.BOLD, 20));
		screen.setFill(Color.WHITE);
		setXYAndAdd(screen, 30, 150);
	}
	/**
	 * Game loop. Check each step for game status
	 * @param elapsedTime time in between the steps
	 */
	private void gameController(double elapsedTime) {
		if (bouncers.size() == 0) {
			if (status.equals("play")) {
				if (player.getLives() == 0) {
					status = "end";
					showTransitionScreen(status);
				} else {
					player.decrementLives();
					status = "life";
					showTransitionScreen(status);
				}
			} else if (status.equals("wait")) {
				setUpLevel(player.getLevel());
			}
		}

		else if (planets.size() == 0) {
			if (status.equals("play")) {
				player.incrementLevel();
				if (player.getLevel() > Player.MAX_LEVELS) {
					status = "win";
					showTransitionScreen(status);

				} else {
					status = "level";
					showTransitionScreen(status);
				}
			} else if (status.equals("wait")) {
				setUpLevel(player.getLevel());
			}
		}
		step(elapsedTime);
	}
	/**
	 * Update positions of all elements in the game.
	 */
	private void step(double elapsedTime) {
		//move paddle up
		if (player.getStep() % (PADDLE_STEP / player.getLevel()) == 0 && status.equals("play"))
			paddle.setY(paddle.getY() - 1);
		player.step();

		for (Planet planet : planets) {
			if (planet.getPlanet().getBoundsInParent().intersects(paddle.getBoundsInParent())) {
				removeBouncersFromRootAndClear();
				return;
			}
		}
		//increase speed of all bouncers if more than half of planets cleared
		if (player.getLevel() <= Player.MAX_LEVELS && planets.size() <= LEVELS.getLevelSize(player.getLevel()) / 2) {
			for (Bouncer bouncer : bouncers) 
				bouncer.doubleSpeed();
		}
		
		for (int i = 0; i < bouncers.size(); i++) {
			for (int j = 0; j < planets.size(); j++) {
				//check if planets hit
				Point planetCenter = bouncers.get(i).checkPlanetHit(planets.get(j));
				
				//make planet blow up
				if (planets.get(j).getHits() >= planets.get(j).getMaxHits()) 
					planets.get(j).destroy();
				
				if (planets.get(j).isBlowingUp() && planets.get(j).isDestroyed()){
					root.getChildren().remove(planets.get(j).getPlanet());
					if (planets.get(j).getName().equals("earth")) {
						if (planets.size() == 1)
							player.setHumans(true);
					}
					if (planets.get(j).isBig())
						makeBouncer(planetCenter.getX(), planetCenter.getY());

					player.incrementScore();
					planets.remove(j);
					j--;
				}
			}

			bouncers.get(i).bounceOffPaddle(paddle, paddleLeft);
			//move and bounce bouncers off walls and remove if fell off bottom
			
			if (bouncers.get(i).step(elapsedTime)){
				root.getChildren().remove(bouncers.get(i).getBouncer());
				bouncers.remove(i);
				i--;
				continue;
			}
			//update scoreboard status
			score.setText("Score: " + player.getScore());
		}
		lives.setText("Lives: " + player.getLives());
		level.setText("Level: " + player.getLevel());

	}
	

	/**
	 * react to keys pressed on keyboard
	 * @param code Keycode of key pressed 
	 */
	private void handleKeyPressed(KeyCode code) {
		if (code == KeyCode.RIGHT ) {
			paddleLeft = false;
			//warp check
			if (paddle.getX() < (SIZE - paddle.getBoundsInLocal().getWidth() / 2))
				paddle.setX(paddle.getX() + KEY_INPUT_SPEED);
			else
				paddle.setX(-paddle.getBoundsInLocal().getWidth());
			if (status.equals("wait")) {
				bouncers.get(0).getBouncer().setX(paddle.getX() + paddle.getBoundsInLocal().getWidth() / 2
						- bouncers.get(0).getBouncer().getBoundsInLocal().getWidth() / 2);
			}
		} else if (code == KeyCode.LEFT) {
			paddleLeft = true;
			//warp check
			if (paddle.getX() > -paddle.getBoundsInLocal().getWidth() / 2)
				paddle.setX(paddle.getX() - KEY_INPUT_SPEED);
			else
				paddle.setX(SIZE);
			if (status.equals("wait")) {
				bouncers.get(0).getBouncer().setX(paddle.getX() + paddle.getBoundsInLocal().getWidth() / 2
						- bouncers.get(0).getBouncer().getBoundsInLocal().getWidth() / 2);
			}
		} else if (code == KeyCode.ENTER) {
			if (status.equals("wait")) {
				status = "play";
				animation.play();
			} else if (status == null || status.equals("level") || status.equals("beginning")) {
				//need to wait for playerin some cases
				pauseOnButton();
				setUpLevel(player.getLevel());
			} else if (status.equals("life")) {
				pauseOnButton();
				resetScreen();
			} else if (status.equals("play")) {
				pauseOnButton();
			} else if (status.equals("win") || status.equals("end") || status.equals("quit")) {
				animation.pause();
				player.reset();
				status = "beginning";
				showTransitionScreen(status);
			}
		} else if (code == KeyCode.DIGIT1) {
			changeLevelOnButton("beginning", 1);
		} else if (code == KeyCode.DIGIT2) {
			changeLevelOnButton("level", 2);
		} else if (code == KeyCode.DIGIT3) {
			changeLevelOnButton("level", 3);
		} else if (code == KeyCode.L) {
			player.incrementLives();
		} else if (code == KeyCode.N) {
			player.incrementLevel();
			if (player.getLevel() > Player.MAX_LEVELS) {
				status = "win";
				showTransitionScreen(status);
			} else {
				changeLevelOnButton("level", player.getLevel());
			}
		} else if (code == KeyCode.Q) {
			if (status.equals("quit"))
				stage.close();
			else {
				player.reset();
				statusScreen("quit");
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
	}

	private void statusScreen(String s) {
		status = s;
		showTransitionScreen(status);
	}

	private void changeLevelOnButton(String s, int level) {
		statusScreen(s);
		player.setLevel(level);

	}

	private void pauseOnButton() {
		animation.pause();
		status = "wait";
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
