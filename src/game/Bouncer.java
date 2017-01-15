package game;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

class Bouncer{
	private ImageView bouncer;
	private int speedx;
	private int speedy;
	private boolean left;
	private boolean up;
	
	public Bouncer(){
		Image image = new Image(getClass().getClassLoader().getResourceAsStream("asteroid.png"));
		bouncer = new ImageView(image);
		speedx = (int)( Math.random()*100);
		speedy = (int)( Math.random()*100);
		bouncer.setX(Math.random()*400);
		bouncer.setY(Math.random()*400);
		System.out.println("TEST");
		left = false;
		up = false;
	}
	public ImageView getBouncer(){
		return bouncer;
	}
	public int getSpeedX(){
		return speedx;
	}
	public int getSpeedY(){
		return speedy;
	}
	public boolean getLeft(){
		return left;
	}
	public boolean getUp(){
		return up;
	}
	public void setLeft(boolean left){
		this.left = left;
	}
	public void setUp(boolean up){
		this.up = up;
	}
}