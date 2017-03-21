import java.util.Random;
import java.util.ArrayList;

public class BlueCard {

	private Random randomGenerator = new Random();

	private String typeOfWater;
	private String depositSize;
	private String drillType;
	private double drillPrice;
	private double barrels;

	public BlueCard() {
		String[] typeOfWater = { "Shallow Water", "Reefs", "Deep Water" };
		// To store all the possibilities we can get for each data for our card.
		ArrayList<String> depositSize = new ArrayList<String>();
		ArrayList<String> drillType = new ArrayList<String>();
		ArrayList<Double> barrels = new ArrayList<Double>();

		this.setTypeOfWater(typeOfWater[random(typeOfWater.length)]);
		if (this.typeOfWater == "Shallow Water") {
			depositSize.add("No Oil");
			depositSize.add("Small Deposits");
			depositSize.add("Medium Deposits");
			drillType.add("Light Drill");
			barrels.add(20000.0);
			barrels.add(40000.0);
		} else if (this.typeOfWater == "Reefs") {
			depositSize.add("No Oil");
			depositSize.add("Small Deposits");
			depositSize.add("Medium Deposits");
			depositSize.add("Large Deposits");
			drillType.add("Special Drill");
			barrels.add(20000.0);
			barrels.add(40000.0);
			barrels.add(100000.0);
		} else {
			depositSize.add("No Oil");
			depositSize.add("Small Deposits");
			depositSize.add("Medium Deposits");
			depositSize.add("Large Deposits");
			drillType.add("Light Drill");
			drillType.add("Heavy Drill");
			barrels.add(20000.0);
			barrels.add(40000.0);
			barrels.add(100000.0);
		}

		this.setDepositSize(depositSize.get(random(depositSize.size())));
		this.setDrillType(drillType.get(random(drillType.size())));
		this.setDrillPrice();

		this.setBarrels(barrels.get(random(barrels.size())));
	}

	public String toString() {
		String str = "";

		if (this.getDepositSize() == "No Oil") {
			str = "\nDeposit Size: " + this.getDepositSize();
		} else {
			str = "\nType of Water: " + this.getTypeOfWater() + "\nDeposit Size: " + this.getDepositSize()
					+ "\nDrill Type: " + this.getDrillType() + " ($" + this.getDrillPrice() + ")\nBarrels: "
					+ this.getBarrels();
		}

		return str;
	}

	public String getTypeOfWater() {
		return this.typeOfWater;
	}

	public String getDepositSize() {
		return this.depositSize;
	}

	public String getDrillType() {
		return this.drillType;
	}

	public double getDrillPrice() {
		return this.drillPrice;
	}

	public double getBarrels() {
		return this.barrels;
	}

	public void setTypeOfWater(String typeOfWater) {
		this.typeOfWater = typeOfWater;
	}

	public void setDepositSize(String depositSize) {
		this.depositSize = depositSize;
	}

	public void setDrillType(String drillType) {
		this.drillType = drillType;
	}

	public void setDrillPrice() {
		double drillPrice;
		if (this.drillType == "Special Drill") {
			drillPrice = 250000;
		} else if (this.drillType == "Light Drill") {
			drillPrice = 100000;
		} else {
			drillPrice = 500000;
		}
		this.drillPrice = drillPrice;
	}

	public void setBarrels(double barrels) {
		this.barrels = barrels;
	}

	// Utility methods.
	private int random(int length) {
		return randomGenerator.nextInt(length);
	}
}