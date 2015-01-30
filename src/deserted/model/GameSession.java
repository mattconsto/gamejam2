package deserted.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.joda.time.LocalDateTime;

public class GameSession {
	// 30 seconds = 1 hour
	// 1 second = 2 minutes
	private static final int MINS_PER_SEC = 2;
	private static final int NUMBER_AGENTS = 8;

	private static final float FOOD_PER_SEC_WALK = 0.5f;
	private static final float FOOD_PER_SEC_STAND = 0.25f;
	private static final float FOOD_PER_SEC_SLEEP = 0.2f;

	private static final float WATER_PER_SEC_WALK = 0.5f;
	private static final float WATER_PER_SEC_STAND = 0.25f;
	private static final float WATER_PER_SEC_SLEEP = 0.2f;

	private static final float HEALTH_PER_SEC = 0.25f;
	private static GameSession instance;

	//private boolean walking = false;
	// Play time in seconds
	private double gameTimer;
	// Game time in minutes
	private double timeSurvived;
	// When we 'crashed'
	private LocalDateTime crashDate;
	private boolean completed;
	private int completionType;
	private Inventory inventory;

	private ArrayList<Agent> agents;

	private GameSession() {
		this.setCompleted(false);
		this.setCompletionType(0);
		this.setInventory(new Inventory());
		this.gameTimer = 0;
		this.timeSurvived = 0;
		this.agents = new ArrayList<Agent>();
		int year = (int) (2010 + (Math.round(Math.random() * 5) - 10));
		int month = (int) Math.ceil(Math.random() * 12);
		int day = (int) Math.ceil(Math.random() * 27);
		int hour = (int) (Math.random() * 24);
		int minute = (int) (Math.random() * 60);
		this.crashDate = new LocalDateTime(year, month, day, hour, minute);

		for (int i = 0; i < NUMBER_AGENTS; i++) {
			getAgents().add(new Agent());
		}
		this.generateInventory();

	}

	public static GameSession getInstance() {
		if (instance == null) {
			instance = new GameSession();
		}
		return instance;
	}

	public void update(float delta) {
		this.gameTimer += delta;
		this.timeSurvived = gameTimer * MINS_PER_SEC;
		// (int) Math.floor(
		// Update agent stats
		for (Agent agent : agents) {

			if (agent.getState() == AgentState.WALKING) {
				agent.decFood(FOOD_PER_SEC_WALK * delta);
				agent.decWater(WATER_PER_SEC_WALK * delta);
			} else if (agent.getState() == AgentState.STANDING) {
				agent.decFood(FOOD_PER_SEC_STAND * delta);
				agent.decWater(WATER_PER_SEC_STAND * delta);
			} else if (agent.getState() == AgentState.SLEEPING) {
				// Only gain health if not thirsty or hungry
				if (agent.getFood() > 0 && agent.getWater() > 0) {
					if (agent.getHealth() < 90) {
						agent.incHealth(0.1f);
					}
				}
				agent.decFood(FOOD_PER_SEC_SLEEP * delta);
				agent.decWater(WATER_PER_SEC_SLEEP * delta);
			}

			if (agent.getFood() == 0 && agent.getWater() == 0) {
				agent.decHealth(HEALTH_PER_SEC * delta);
			}

			if (agent.getHealth() <= 0) {

				if (agent.getState() != AgentState.DEAD)
					agent.setExpiredTime(this.timeSurvived);
				agent.setState(AgentState.DEAD);

			}
		}

	}

	public double getTimeSurvived() {
		return this.timeSurvived;
	}

	public LocalDateTime getDate() {
		return this.crashDate.plusMinutes((int) Math.floor(this.timeSurvived));
	}

	public ArrayList<Agent> getAgents() {
		return agents;
	}

	

	private void generateInventory() {
		ItemType[] itemTypes = { ItemType.LIFEJACKET, ItemType.SNACK };
		for (int i = 0; i < NUMBER_AGENTS; i++) {
			for (ItemType itemType : itemTypes) {
				if (Math.random() > 0.8) {
					getInventory().addItem(itemType);
				}
			}
		}
	}

	public boolean isCompleted() {
		return completed;
	}

	public void setCompleted(boolean completed) {
		this.completed = completed;
	}

	public int getCompletionType() {
		return completionType;
	}

	public void setCompletionType(int completionType) {
		this.completionType = completionType;
	}

	public Inventory getInventory() {
		return inventory;
	}

	public void setInventory(Inventory inventory) {
		this.inventory = inventory;
	}
}