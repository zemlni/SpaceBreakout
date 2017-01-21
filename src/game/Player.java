package game;

public class Player {
	public static final int MAX_LEVELS = 3;
	private int level;
	private int score;
	private int lives;
	private int step;
	private boolean fast;
	private boolean humans;

	public Player() {
		reset();
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
		fast = false;
		humans = false;
	}
	public boolean getHumans(){
		return humans;
	}
	public void setHumans(boolean humans){
		this.humans = humans;
	}
	public boolean isFast(){
		return fast;
	}
	public void setFast(boolean fast){
		this.fast = fast;
	}
	public int getStep(){
		return step;
	}
	public void step(){
		step++;
	}
}
