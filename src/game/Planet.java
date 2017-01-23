package game;
/**
 * Wrapper for imageview for purpose of displaying planets 
 * as blocks. Planet related actions live here
 * @author Nikita Zemlevskiy.
 */
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class Planet {
	public static final int LARGE_PLANET_SIZE = 75;
	public static final int SMALL_PLANET_SIZE = 50;
	private ImageView planet;
	private int maxHits;
	private int hits;
	private int size;
	private String name;
	private boolean blowingUp;
	private boolean destroyed;
	/**
	 * Make a new planet with the picture taken from name.
	 * @param name name of planet 
	 */
	public Planet(String name) {
		this.name = name;
		Image image = new Image(getClass().getClassLoader().getResourceAsStream(name + ".png"));
		planet = new ImageView(image);
		size = sizeDecider(name);
		planet.setFitHeight(this.size);
		planet.setFitWidth(this.size);
		maxHits = size == SMALL_PLANET_SIZE ? 2 : 3;
		hits = 0;
		blowingUp = false;
		destroyed = false;
	}
	public String getName(){
		return name;
	}
	/**
	 * Decide the size of a given planet
	 * @param name name of planet
	 * @return size of planet to be displayer
	 */
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
	
	public boolean isBig(){
		return size == LARGE_PLANET_SIZE;
	}

	public ImageView getPlanet() {
		return planet;
	}

	public int getMaxHits() {
		return maxHits;
	}

	public void setMaxHits(int maxHits) {
		this.maxHits = maxHits;
	}

	public int getHits() {
		return hits;
	}

	public void incrementHits(){
		this.hits++;
	}

	public int getRadius() {
		return size/2;
	}
	
	public int getSize(){
		return size;
	}
	public boolean isBlowingUp(){
		return blowingUp;
	}
	/**
	 * Display blowing up animation.
	 */
	public void destroy(){
		blowingUp = true;
		planet.setImage(new Image(getClass().getClassLoader().getResourceAsStream("explosion.png")));
		KeyFrame frame = new KeyFrame(Duration.millis(100), e -> destroyed = true);
		Timeline temp = new Timeline(frame);
		temp.play();
	}
	public boolean isDestroyed(){
		return destroyed;
	}
}
