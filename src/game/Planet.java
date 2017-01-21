package game;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Planet {
	public static final int LARGE_PLANET_SIZE = 75;
	public static final int SMALL_PLANET_SIZE = 50;
	private ImageView planet;
	private int maxHits;
	private int hits;
	private int size;
	private String name;

	public Planet(String name) {
		this.name = name;
		Image image = new Image(getClass().getClassLoader().getResourceAsStream(name + ".png"));
		planet = new ImageView(image);
		size = sizeDecider(name);
		planet.setFitHeight(this.size);
		planet.setFitWidth(this.size);
		maxHits = size == SMALL_PLANET_SIZE ? 2 : 3;
		hits = 0;
	}
	public String getName(){
		return name;
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
}
