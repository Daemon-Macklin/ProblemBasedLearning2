import java.util.Random;

public class GreenCard {

	private Random randomGenerator = new Random();

	private String government;
	double capTax;
	double revTax;
	double interest;

	public GreenCard() {
		String[] government = { "Conservative", "Labour" };
		double[] CcapTax = { 0, 0, 0, 0, 0.25, 0.25 };
		double[] CrevTax = { 0.25, 0.5, 0, 0.25, 0.25, 0.5 };
		double[] Cinterest = { 0.2, 0.1, 0.2, 0.2, 0.1, 0.1 };
		double[] LcapTax = { 0.75, 0.50, 0.50, 0.75 };
		double[] Lrevtax = { 0.50, 0.50, 0.75, 0.25 };
		double[] Linterest = { 0.05, 0.05, 0.1, 0.05 };

		this.setGovernment(government[random(government.length)]);

		if (this.government == "Conservative") {
			this.setCapTax(CcapTax[random(CcapTax.length)]);
			this.setRevTax(CrevTax[random(CrevTax.length)]);
			this.setInterest(Cinterest[random(Cinterest.length)]);
		} else if (this.government == "Labour") {
			this.setCapTax(LcapTax[random(LcapTax.length)]);
			this.setRevTax(Lrevtax[random(Lrevtax.length)]);
			this.setInterest(Linterest[random(Linterest.length)]);
		}
	}

	public String toString() {
		String str = "\nParty in Power: " + this.getGovernment() + "\nCapital Tax: " + this.getCapTax()
				+ "%\nRevenue Tax: " + this.getRevTax() + "%\nInterest: " + this.getInterest() + "%";

		return str;
	}

	public String getGovernment() {
		return this.government;
	}

	public double getCapTax() {
		return this.capTax;
	}

	public double getRevTax() {
		return this.revTax;
	}

	public double getInterest() {
		return this.interest;
	}

	public void setGovernment(String government) {
		this.government = government;
	}

	public void setCapTax(double capTax) {
		this.capTax = capTax;
	}

	public void setRevTax(double revTax) {
		this.revTax = revTax;
	}

	public void setInterest(double interest) {
		this.interest = interest;
	}

	// Utility methods.
	private int random(int length) {
		return randomGenerator.nextInt(length);
	}
}
