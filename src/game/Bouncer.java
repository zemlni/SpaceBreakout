package game;
/* This entire file is part of my masterpiece.
 * Nikita Zemlevskiy
 * The purpose of this class is to store all bouncer related information and
 * tasks. The bouncer instance variables are here and decisions on how to 
 * change position on each step of the game are also made here. I think that
 * this class is well designed because there is little code stinks in here.
 * I wrote a lot in my ANALYSIS.md about how I did not give enough power to
 * the bouncer and planet classes as I should have, and instead put all of 
 * the decision making in main. With this refactor, I have diverted some of 
 * that control to here. In doing so, I was also able to get rid of a lot of 
 * data dependencies and if statements. Additionally, by putting the control
 * in here instead of in main, this fixes the problem of the bouncer object 
 * being told what to do, instead of just living on its own like objects are
 * supposed to do.
 */

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
public class Bouncer {
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
			setUp(false);
			if (getHits()) {
				hit();
				planet.incrementHits();
			}
		}
		return planetCenter;
	}
	/**
	 * Gets center of ImageView object. Useful for planets and bouncers.
	 * used in determining when something is hit
	 * @param im ImageView object to get center of
	 * @return the center point of the ImageView object.
	 */
	private Point getCenter(ImageView im) {
		int x = (int) (im.getX() + im.getBoundsInLocal().getWidth() / 2);
		int y = (int) (im.getY() + im.getBoundsInLocal().getHeight() / 2);
		return new Point(x, y);
	}

	/**Handles bouncing off of the paddle on striking it.
	 * First test for contact and if contact 
	 * exists then a bounce is made, speeding up
	 * or slowing down the bouncer depending on whether
	 * the direction of motion of the paddle and bouncer are the same.
	 * 
	 * @param paddle the paddle ImageView from main.
	 * @param paddleLeft the paddleLeft variable that tells whether the paddle
	 * 		is moving left or right or stationary.
	 */
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