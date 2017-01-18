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
	private boolean big;

	public Bouncer() {
		Image image = new Image(getClass().getClassLoader().getResourceAsStream("asteroid.png"));
		bouncer = new ImageView(image);
		bouncer.setFitHeight(20);
		bouncer.setFitWidth(20);
		speedx = (int) (Math.random() * 100);
		speedy = (int) (Math.random() * 100);
		// bouncer.setX(Math.random() * 400);
		// bouncer.setY(Math.random() * 400);
		left = false;
		up = false;
		size = 20;
		big = false;
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

	public void destroy() {
		this.bouncer.setVisible(false);
	}

	public boolean getBig() {
		return big;
	}

	public void setBig() {
		if (big != true) {
			big = true;
			bouncer.setFitHeight(bouncer.getFitHeight() * 2);
			bouncer.setFitWidth(bouncer.getFitWidth() * 2);
		}
	}
}