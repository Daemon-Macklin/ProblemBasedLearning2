import java.util.ArrayList;

public class Space {

	// Quadrant data from the Board class.
	private String quadrant;
	private int index;

	private String owner;
	private ArrayList<String> seen; // To check and store who can look at the
									// BlueCard freely.
	private BlueCard card; // Each Space will have an assigned BlueCard to them.
	private boolean bought; // Checking if a drill concession is bought aka an
							// ownership token.
	private boolean rig; // Checking if a drill rig is installed.

	public Space(int index) {
		this.setQuadrant();
		this.setIndex(index);

		this.setOwner("");
		this.seen = new ArrayList<String>();
		this.setCard();
		this.setBought(false);
		this.destroyRig();
	}

	public void giveSpaceTo(String owner) {
		this.setOwner(owner);
		this.setBought(true);
	}

	public String toString(String player) {
		this.seen.add(player);
		String str = "\n-- Details for Space " + this.getIndex() + " --" + this.getCard().toString();

		return str;
	}

	public void newRig() {
		this.rig = true;
	}

	public void destroyRig() {
		this.rig = false;
	}

	public String getQuadrant() {
		return this.quadrant;
	}

	public int getIndex() {
		return this.index;
	}

	public String getOwner() {
		return this.owner;
	}

	public ArrayList<String> getSeen() {
		return this.seen;
	}

	public BlueCard getCard() {
		return this.card;
	}

	public boolean getBought() {
		return this.bought;
	}

	public boolean getRig() {
		return this.rig;
	}

	public void setQuadrant() {
		// Automatically determining the quadrant of the Space.
		int index = this.getIndex();
		if (index < 7) {
			this.setQuadrant("North West");
		} else if (index < 14) {
			this.setQuadrant("North East");
		} else if (index < 21) {
			this.setQuadrant("South West");
		} else {
			this.setQuadrant("South East");
		}
	}

	public void setQuadrant(String quadrant) {
		this.quadrant = quadrant;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public void setCard() {
		// Resetting the Space with a new BlueCard.
		this.card = new BlueCard();
	}

	public void setBought(boolean bought) {
		this.bought = bought;
	}
}
