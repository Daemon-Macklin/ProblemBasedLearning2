import java.util.Random;

public class RedCard {

	private Random randomGenerator = new Random();

	private double price;

	public RedCard() {
		int result = randomGenerator.nextInt(100);

		if (result < 50) {
			this.setPrice(4);
		} else if (result < 80) {
			this.setPrice(20);
		} else {
			this.setPrice(50);
		}
	}

	public String toString() {
		String str = "\nPrice of Oil: " + this.getPrice();

		return str;
	}

	public double getPrice() {
		return this.price;
	}

	public void setPrice(double price) {
		this.price = price;
	}
}