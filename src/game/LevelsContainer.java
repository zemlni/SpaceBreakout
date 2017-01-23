package game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
/**
 * contains a planet location and its name
 * @author Nikita Zemlevskiy.
 */
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
/**
 * Loads levels from files. Stores levels and their initial sizes.
 *@author Nikita Zemlevskiy.
 */
public class LevelsContainer {
	private static HashMap<Integer, ArrayList<PlanetLoc>> levels;
	private static HashMap<Integer, Integer> sizes;

	public LevelsContainer() {
		levels = new HashMap<Integer, ArrayList<PlanetLoc>>();
		sizes = new HashMap<Integer, Integer>();
		for (int i = 1; i <= Player.MAX_LEVELS; i++){
			ArrayList<PlanetLoc> result = parseLevel(i);
			levels.put(i, result);
			sizes.put(i, result.size());
		}
	}
	/**
	 *Parse level files and populate levels map. 
	 */
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
						new Point(Integer.parseInt(lineSplit[1]), Integer.parseInt(lineSplit[2]) + Main.SCOREBOARD_HEIGHT)));
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
	
	public int getLevelSize(int level){
		return sizes.get(level);
	}
}
