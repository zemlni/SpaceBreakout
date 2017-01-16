package game;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Planet {
	private ImageView planet;
	private int maxHits;
	private int hits;
	private int size;

	public Planet(String src, int maxHits, int size) {
		Image image = new Image(getClass().getClassLoader().getResourceAsStream(src));
		planet = new ImageView(image);
		this.size = size == 0 ? 50 : 75;
		planet.setFitHeight(this.size);
		planet.setFitWidth(this.size);
		this.maxHits = maxHits;
		this.hits = 0;
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
	
	public void destroy(){
		this.planet.setVisible(false);
	}
	
	public int getSize(){
		return size;
	}
}
