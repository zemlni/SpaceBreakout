package game;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Planet {
	private ImageView planet;
	private int maxHits;
	private int hits;

	public Planet(String src, int maxHits, int size) {
		Image image = new Image(getClass().getClassLoader().getResourceAsStream(src));
		planet = new ImageView(image);
		int s = size == 0 ? 25 : 50;
		planet.setFitHeight(s);
		planet.setFitWidth(s);
		this.maxHits = maxHits;
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
}
