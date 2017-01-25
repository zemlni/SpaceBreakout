package game;


import javafx.scene.image.Image;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;

import javafx.scene.image.ImageView;
import javafx.util.Duration;
/**
 * Wrapper for imageview for purpose of maintaining bouncers.
 * All things bouncer related live here
 * @author Nikita Zemlevskiy.
 */
class Bouncer {
	public static final int BOUNCER_BASE_SPEED = 200;
	public static final int BOUNCER_SIZE = 20;
	private ImageView bouncer;
	private int speedx;
	private int speedy;
	private boolean left;
	private boolean up;
	private int size;
	private boolean big;
	private boolean hits;
	private boolean fast;

	public Bouncer() {
		Image image = new Image(getClass().getClassLoader().getResourceAsStream("asteroid.png"));
		bouncer = new ImageView(image);
		bouncer.setFitHeight(BOUNCER_SIZE);
		bouncer.setFitWidth(BOUNCER_SIZE);
		speedx = (int) (Math.random() * BOUNCER_BASE_SPEED);
		speedy = (int) (Math.random() * BOUNCER_BASE_SPEED);
		left = false;
		up = false;
		size = BOUNCER_SIZE;
		big = false;
		hits = true;
	}

	public ImageView getBouncer() {
		return bouncer;
	}

	public int getSpeedX() {
		return speedx;
	}

	public void setSpeedX(int speedx) {
		this.speedx = speedx;
	}

	public int getSpeedY() {
		return speedy;
	}

	public void setSpeedY(int speedy) {
		this.speedy = speedy;
	}

	public boolean getLeft() {
		return left;
	}

	public boolean getUp() {
		return up;
	}

	public void setLeft(boolean left) {
		this.left = left;
	}

	public void setUp(boolean up) {
		this.up = up;
	}

	public int getRadius() {
		return size / 2;
	}

	public boolean getBig() {
		return big;
	}
	/**
	 * double size of bouncer. called on keypress a
	 */
	public void setBig() {
		if (!big) {
			big = true;
			bouncer.setFitHeight(bouncer.getFitHeight() * 2);
			bouncer.setFitWidth(bouncer.getFitWidth() * 2);
		}
	}
	public boolean getHits(){
		return hits;
	}
	/**
	 * disable hits for 5 ms after bouncer has hit a planet.
	 * fixes double hit issue.
	 */
	public void hit(){
		hits = false;
		KeyFrame frame = new KeyFrame(Duration.millis(10), e -> hits = true);
		Timeline temp = new Timeline(frame);
		temp.play();
	}
	/**
	 * double speed of bouncer.
	 */
	public void doubleSpeed(){
		if (!fast){
			speedx *= 2;
			speedy *= 2;
			fast = true;
		}
	}
	
	/**
	 * Move the bouncer and bounce off of walls. 
	 * @param elapsedTime
	 * @return
	 */
	public boolean step(double elapsedTime){
		double locationx = bouncer.getX();

		if (locationx >= Main.SIZE - bouncer.getBoundsInLocal().getWidth())
			setLeft(true);
		if (locationx <= 0) 
			setLeft(false);
		
		int sign = getLeft() == true ? -1 : 1;	
		getBouncer().setX(locationx + sign * getSpeedX() * elapsedTime);

		double locationy = getBouncer().getY();

		if (locationy >= Main.SIZE - getBouncer().getBoundsInLocal().getWidth()) 
			return true;
		
		if (locationy <= Main.SCOREBOARD_HEIGHT)
			setUp(false);
		
		sign = getUp() == true ? -1 : 1;
		getBouncer().setY(locationy + sign * getSpeedY() * elapsedTime);
		return false;
	}

	/**
	 * Check presence of collision with a given planet. 
	 * If there is a collision, the planet's hits are incremented
	 * and the bouncers hits are turned off for 10 ms.
	 * 
	 * @param planet planet with which to check collision
	 * @return the center point of the planet, to use further in main.
	 */
	public Point checkPlanetHit(Planet planet) {
		Point planetCenter = getCenter(planet.getPlanet());
		Point bouncerCenter = getCenter(getBouncer());
		int distance = planetCenter.distance(bouncerCenter);
		int sum = getRadius() + planet.getRadius();
		
		if (distance <= sum) {
			//bounce bouncers and hit planet
			setUp(false);
			if (getHits()) {
				hit();
				planet.incrementHits();
			}
		}
		return planetCenter;
	}
	/**
	 * Gets center of imageview object. Useful for planets
	 * @param im imageview object to get center of
	 */
	private Point getCenter(ImageView im) {
		int x = (int) (im.getX() + im.getBoundsInLocal().getWidth() / 2);
		int y = (int) (im.getY() + im.getBoundsInLocal().getHeight() / 2);
		return new Point(x, y);
	}

	public void bounceOffPaddle(ImageView paddle, Boolean paddleLeft) {
		if (paddle.getBoundsInParent().intersects(getBouncer().getBoundsInParent())) {
			setUp(true);
			if (paddleLeft != null) {
				if (paddleLeft.equals(getLeft()))
					setSpeedX(getSpeedX() + Main.KEY_INPUT_SPEED);
				else
					setSpeedX(getSpeedX() - Main.KEY_INPUT_SPEED);
			}
		}
		
	}
	
}