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
		if (big != true) {
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
		KeyFrame frame = new KeyFrame(Duration.millis(5), e -> hits = true);
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
}