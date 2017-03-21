import java.util.ArrayList;

public class Player {

	private String name;
	private double cash;
	private double debt;

	private ArrayList<String> offBoardDrills;
	// These fields determine the amount of times a player has done x per turn.
	// Resets at the end of each turn.
	private int sitesTested;
	private int concessionsBought;

	public Player(String name) {
		this.setName(name);
		this.setCash(0);
		this.setDebt(0);

		this.offBoardDrills = new ArrayList<String>();

		this.setSitesTested(0);
		this.setConcessionsBought(0);
	}

	public String getName() {
		return this.name;
	}

	public double getCash() {
		return this.cash;
	}

	public double getDebt() {
		return this.debt;
	}

	public int getSitesTested() {
		return this.sitesTested;
	}

	public int getConcessionsBought() {
		return this.concessionsBought;
	}

	public ArrayList<String> getOffBoardDrills() {
		return this.offBoardDrills;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setCash(double cash) {
		this.cash = cash;
	}

	public void setDebt(double debt) {
		this.debt = debt;
	}

	public void setSitesTested(int sitesTested) {
		this.sitesTested = sitesTested;
	}

	public void setConcessionsBought(int concessionsBought) {
		this.concessionsBought = concessionsBought;
	}

	public void addOffBoardDrill(String offBoardDrills) {
		this.offBoardDrills.add(offBoardDrills);
	}
}