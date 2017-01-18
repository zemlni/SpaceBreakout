package game;

public class Player {
	public static final int MAX_LEVELS = 2;
	private int level;
	private int score;
	private int lives;
	private int step;

	public Player() {
		level = 1;
		score = 0;
		lives = 3;
		step = 0;
	}

	public int getLevel() {
		return level;
	}

	public int getScore() {
		return score;
	}

	public int getLives() {
		return lives;
	}

	public void decrementLives() {
		lives--;
	}

	public void incrementScore() {
		score++;
	}

	public void incrementLevel() {
		level++;
	}
	
	public void resetScore(){
		score = 0;
	}
	public void setLevel(int level){
		this.level = level;
	}

	public void incrementLives() {
		lives++;
	}
	public void reset(){
		level = 1;
		score = 0;
		lives = 3;
	}
	public int getStep(){
		return step;
	}
	public void step(){
		step++;
	}
}
