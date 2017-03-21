import java.util.Random;

public class BrownCard {

	private Random randomGenerator = new Random();

	private String season;
	private String[] directions = new String[4]; // The different quadrants.
	/*
	 * directions[0] = North West | directions[1] = North East | directions[2] =
	 * South West, directions[3] = South East
	 */

	public BrownCard() {
		String[] seasons = { "Spring", "Summer", "Autumn", "Winter" };

		int r = random(100);
		if (r < 25) {
			this.setSeason(seasons[0]);
		} else if (r < 42) {
			this.setSeason(seasons[1]);
		} else if (r < 66) {
			this.setSeason(seasons[2]);
		} else {
			this.setSeason(seasons[3]);
		}

		if (this.season == seasons[0]) {
			this.directions[0] = changeWeather(33, 33, 33);
			this.directions[1] = changeWeather(33, 66, 0);
			this.directions[2] = changeWeather(66, 0, 33);
			this.directions[3] = changeWeather(66, 33, 0);
		} else if (this.season == seasons[1]) {
			this.directions[0] = changeWeather(50, 0, 50);
			this.directions[1] = changeWeather(50, 50, 0);
			this.directions[2] = changeWeather(50, 0, 50);
			this.directions[3] = changeWeather(100, 0, 0);
		} else if (this.season == seasons[2]) {
			this.directions[0] = changeWeather(66, 33, 0);
			this.directions[1] = changeWeather(66, 33, 0);
			this.directions[2] = changeWeather(100, 0, 0);
			this.directions[3] = changeWeather(100, 0, 0);
		} else {
			this.directions[0] = changeWeather(25, 25, 0); // 50% = Gale.
			this.directions[1] = changeWeather(25, 25, 0);
			this.directions[2] = changeWeather(25, 25, 25);
			this.directions[3] = changeWeather(25, 25, 25);
		}
	}

	public String toString() {
		String str = "\nNorth West: " + directions[0] + " | North East: " + directions[1] + "\nSouth West: "
				+ directions[2] + " | South East: " + directions[3];

		return str;
	}

	public String changeWeather(int r1, int r2, int r3) {
		String[] typeOfWeather = { "Good", "Rough", "Storm", "Gale" };
		int r = random(100);
		if (r < r1) {
			return typeOfWeather[0];
		} else if (r < r2 + r1) {
			return typeOfWeather[1];
		} else if (r < r3 + r2 + r1) {
			return typeOfWeather[2];
		} else {
			return typeOfWeather[3];
		}
	}

	public double getProductionMultiplier(Space space) {
		// Calculating the production multiplier for use in the Board class.
		String[] quadrants = { "North West", "North East", "South West", "South East" };
		String quadrant = space.getQuadrant();

		int quadrantIndex = -1;
		for (int i = 0; i < quadrants.length; i++) {
			if (quadrants[i] == quadrant) {
				quadrantIndex = i;
			}
		}

		double multiplier = 0;
		if (quadrantIndex != -1) {
			String weatherType = this.directions[quadrantIndex];
			if (weatherType == "Good") {
				multiplier = 1;
			} else if (weatherType == "Rough") {
				multiplier = 0.5;
			} else if (weatherType == "Storm" || weatherType == "Gale") {
				multiplier = 0;
			}
		}

		return multiplier;
	}

	public String getSeason() {
		return season;
	}

	public String[] getDirections() {
		return directions;
	}

	public void setSeason(String season) {
		this.season = season;
	}

	// public void setDirections(String[] directions) {
	// this.directions = directions;
	// }

	// Utility methods.
	private int random(int length) {
		return randomGenerator.nextInt(length);
	}
}