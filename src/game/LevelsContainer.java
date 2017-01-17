package game;

import java.util.ArrayList;
import java.util.HashMap;

class PlanetLoc{
	private String name;
	private int x;
	private int y;
	public PlanetLoc(String name, Point location){
		this.name = name;
		this.x = location.getX();
		this.y = location.getY();
	}
	public String getName(){
		return name;
	}
	public int getX(){
		return x;
	}
	public int getY(){
		return y;
	}
}

public class LevelsContainer {
	public static final int SCOREBOARD_HEIGHT = 30;
	private static HashMap<Integer, ArrayList<PlanetLoc>> levels;
	public LevelsContainer(){
		levels = new HashMap<Integer, ArrayList<PlanetLoc>>();
		//level1
		ArrayList<PlanetLoc> level1 = new ArrayList<PlanetLoc>();
		level1.add(new PlanetLoc("mercury", new Point(0, SCOREBOARD_HEIGHT)));
		level1.add(new PlanetLoc("venus", new Point(50, SCOREBOARD_HEIGHT)));
		level1.add(new PlanetLoc("earth", new Point(100, SCOREBOARD_HEIGHT)));
		level1.add(new PlanetLoc("mars", new Point(150, SCOREBOARD_HEIGHT)));
		level1.add(new PlanetLoc("mars", new Point(200, SCOREBOARD_HEIGHT)));
		level1.add(new PlanetLoc("venus", new Point(250, SCOREBOARD_HEIGHT)));
		level1.add(new PlanetLoc("mercury", new Point(300, SCOREBOARD_HEIGHT)));
		level1.add(new PlanetLoc("pluto", new Point(350, SCOREBOARD_HEIGHT)));
		levels.put(1, level1);
		levels.put(2, level1);
		
		//level2
		//ArrayList<PlanetLoc> level2 = new ArrayList<PlanetLoc>();
		
	}
	
	public ArrayList<PlanetLoc> getLevel(int level){
		return levels.get(level);
	}
}
