package game;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

class Bouncer {
	private ImageView bouncer;
	private int speedx;
	private int speedy;
	private boolean left;
	private boolean up;
	private int size;

	public Bouncer() {
		Image image = new Image(getClass().getClassLoader().getResourceAsStream("asteroid.png"));
		bouncer = new ImageView(image);
		bouncer.setFitHeight(20);
		bouncer.setFitWidth(20);
		speedx = (int) (Math.random() * 500);
		speedy = (int) (Math.random() * 500);
		bouncer.setX(Math.random() * 400);
		bouncer.setY(Math.random() * 400);
		System.out.println("TEST");
		left = false;
		up = false;
		size = 20;
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
}