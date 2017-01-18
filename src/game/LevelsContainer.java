package game;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

class PlanetLoc {
	private String name;
	private int x;
	private int y;

	public PlanetLoc(String name, Point location) {
		this.name = name;
		this.x = location.getX();
		this.y = location.getY();
	}

	public String getName() {
		return name;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
}

public class LevelsContainer {
	public static final int SCOREBOARD_HEIGHT = 30;
	private static HashMap<Integer, ArrayList<PlanetLoc>> levels;

	public LevelsContainer() {
		levels = new HashMap<Integer, ArrayList<PlanetLoc>>();
		for (int i = 1; i <= Player.MAX_LEVELS; i++){
			levels.put(i, parseLevel(i));
		}
	}

	private ArrayList<PlanetLoc> parseLevel(int level) {
		ArrayList<PlanetLoc> locations = new ArrayList<PlanetLoc>();
		try {
			InputStream curLevel = getClass().getClassLoader()
					.getResourceAsStream("level" + level);
			BufferedReader reader = new BufferedReader(new InputStreamReader(curLevel));
			String line = reader.readLine();
			while (line != null) {

				String[] lineSplit = line.split(" ");

				locations.add(new PlanetLoc(lineSplit[0],
						new Point(Integer.parseInt(lineSplit[1]), Integer.parseInt(lineSplit[2] + SCOREBOARD_HEIGHT))));
				line = reader.readLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return locations;

	}

	public ArrayList<PlanetLoc> getLevel(int level) {
		return levels.get(level);
	}

	public int getLevelWeight(int level) {
		return levels.get(level).size();
	}
}
