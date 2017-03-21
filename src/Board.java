public class Board {

	private Space[] spaces = new Space[28]; // An array of Spaces. Each spaces
											// contain a BlueCard, imitating a
											// collection of BlueCards.

	private GreenCard taxesData;
	private RedCard oilPriceData;
	private BrownCard weatherData;

	public Board() {
		for (int i = 0; i < spaces.length; i++) {
			spaces[i] = new Space(i);
		}
		taxesData = new GreenCard();
		oilPriceData = new RedCard();
		weatherData = new BrownCard();
	}

	public void surveyPlot(Player player, int index) {
		String name = player.getName();
		Space space = spaces[index];

		boolean canSurvey = true;
		boolean mustPay = true;
		if (space.getOwner() == "") {
			// Checking if the player has seen the yield before.
			for (int i = 0; i < space.getSeen().size(); i++) {
				if (name == space.getSeen().get(i)) {
					mustPay = false;
					break;
				}
			}
			if (mustPay) {
				if (player.getCash() < 100000) {
					print("\nYou cannot survey the space. You require atleast $100000 to survey the Space.");
					canSurvey = false;
				}
			}
		}
		if (canSurvey) {
			if (mustPay) {
				player.setCash(player.getCash() - 100000);
				print("\n" + name + " has surveyed Space " + index + " (- $100000).");
			} else {
				print("\n" + name + " has surveyed Space " + index + ".");
			}
			player.setSitesTested(player.getSitesTested() + 1);
			String spaceData = space.toString(name); // Data from BlueCard.
			print(spaceData);
		}
	}

	public void buyPlot(Player player, int index, double bid) {
		String name = player.getName();
		Space space = spaces[index];

		boolean allowBuying = true;
		if (space.getOwner() == "") {
			// Checking if someone has seen the space.
			if (space.getSeen().size() == 0) {
				allowBuying = false;
				print("\n" + name + ", the space has to be surveyed to have a drilling concession.");
			}
		} else {
			allowBuying = false;
			print("\n" + name + ", you cannot buy a drilling concession for Space " + index
					+ " as somebody else already owns the drilling concession rights.");
		}
		if (allowBuying) {
			space.giveSpaceTo(name);
			player.setCash(player.getCash() - bid);
			player.setConcessionsBought(player.getConcessionsBought() + 1);
			print("\n" + name + " has bought a drilling concession for Space " + index + " (- $" + bid + ").");
		}
	}

	public void sellPlot(Player player, int index) {
		String name = player.getName();
		String bank = "";
		Space space = spaces[index];

		if (this.playerOwn(player, index)) {
			space.setOwner(bank);

			double profit = 0;
			String size = space.getCard().getDepositSize();
			String type = space.getCard().getTypeOfWater();
			String drill = space.getCard().getDrillType();

			if (!space.getRig()) {
				if (type == "Reefs") {
					if (size == "No Oil") {
						profit = 0;
					} else if (size == "Small Deposits") {
						profit = 250000;
					} else if (size == "Medium Deposits") {
						profit = 650000;
					} else {
						profit = 1850000;
					}
				} else if (type == "Shallow Water") {
					if (size == "No Oil") {
						profit = 0;
					} else if (size == "Small Deposits") {
						profit = 350000;
					} else if (size == "Medium Deposits") {
						profit = 750000;
					} else {
						profit = 1950000;
					}
				} else {
					if (size == "No Oil") {
						profit = 0;
					} else if (size == "Small Deposits") {
						profit = 150000;
					} else if (size == "Medium Deposits") {
						profit = 500000;
					} else {
						profit = 1700000;
					}
				}
			} else {
				if (drill == "Light Drill") {
					if (size == "No Oil") {
						profit = 0;
					} else if (size == "Small Deposits") {
						profit = 400000;
					} else if (size == "Medium Deposits") {
						profit = 800000;
					} else {
						profit = 2000000;
					}
				} else if (type == "Heavy Drill") {
					if (size == "No Oil") {
						profit = 300000;
					} else if (size == "Small Deposits") {
						profit = 300000;
					} else if (size == "Medium Deposits") {
						profit = 700000;
					} else {
						profit = 1900000;
					}
				} else {
					if (size == "No Oil") {
						profit = 150000;
					} else if (size == "Small Deposits") {
						profit = 350000;
					} else if (size == "Medium Deposits") {
						profit = 750000;
					} else {
						profit = 1950000;
					}
				}
				space.destroyRig();
			}
			player.setCash(player.getCash() + profit);
			print("\n" + name + " has sold their drilling concession rights for Space " + index + " (+ $" + profit
					+ ").");
		} else {
			print("\n" + name + ", you cannot sell the drilling concession to Space " + index
					+ " as you do not own that space.");
		}
	}

	public void buyDrill(Player player, int index) {
		String name = player.getName();
		Space space = spaces[index];

		boolean wantToBuy = true;
		if (this.playerOwn(player, index)) {
			String drillType = space.getCard().getDrillType();
			double drillPrice = space.getCard().getDrillPrice();
			if (player.getCash() < drillPrice) {
				wantToBuy = false;
			} else {
				print("\n" + name + ", you cannot buy a drill rig for Space " + index + " as you require $" + drillPrice
						+ " to buy one.");
			}
			if (wantToBuy) {
				space.newRig();
				boolean mustPay = true;
				for (int i = 0; i < player.getOffBoardDrills().size(); i++) {
					if (player.getOffBoardDrills().get(i) == drillType) {
						player.getOffBoardDrills().remove(i);
						mustPay = false;
						break;
					}
				}
				if (mustPay) {
					player.setCash(player.getCash() - drillPrice);
					print("\n" + name + " has bought a drill rig for Space " + index + " (- $" + drillPrice + ").");
				} else {
					print("\n" + name + " has installed an off-board drill rig to Space " + index + ".");
				}
			}
		} else {
			print("\n" + name + ", you cannot buy a drill for Space " + index + " as you do not own the space.");
		}
	}

	public void sellDrill(Player player, int index) {
		String name = player.getName();
		Space space = spaces[index];

		if (this.playerOwn(player, index)) {
			if (space.getRig()) {
				space.destroyRig();

				double profit = space.getCard().getDrillPrice();
				player.setCash(player.getCash() + profit);
				print("\n" + name + " has sold their drill rig for Space " + index + " (+ $" + profit + ").");
			} else {
				print("\n" + name + ", you cannot sell the drill rig in Space " + index
						+ " as there is none installed.");
			}
		} else {
			print("\n" + name + ", you cannot sell a drill from Space " + index + " as you do not own the space.");
		}
	}

	public void transferDrill(Player player, int index1, int index2) {
		String name = player.getName();
		// To Do; Check if the drill transferred can be transferred (due to
		// requirement of having a drill suitable to the space).
		Space spaceToTransferFrom = spaces[index1];
		Space spaceToTransferTo = spaces[index2];

		boolean allowDrillTransfer = true;
		if (player.getCash() < 20000) {
			allowDrillTransfer = false;
			print("\n" + name + ", you cannot transfer a drill rig from Space " + index1
					+ " as you require $20000 to transfer a drill.");
		} else {
			if (this.playerOwn(player, index1)) {
				if (!spaceToTransferFrom.getRig()) {
					allowDrillTransfer = false;
					print("\n" + name + ", you cannot transfer a drill rig as there is none installed in Space "
							+ index1 + ".");
				}
			} else {
				allowDrillTransfer = false;
				print("\n" + name + ", you cannot transfer a drill rig as you do not own Space " + index1 + ".");
			}
		}

		boolean drillTransferred = true;
		if (allowDrillTransfer) {
			if (this.playerOwn(player, index2)) {
				if (spaceToTransferTo.getRig()) {
					drillTransferred = false;
					print("\n" + name + ", you cannot transfer a drill rig to Space " + index2
							+ " as there already is one installed.");
				}
			} else {
				drillTransferred = false;
				print("\n" + name + ", you cannot transfer a drill rig to Space " + index2 + " as you do not own it.");
			}
		}

		if (allowDrillTransfer && drillTransferred) {
			spaceToTransferFrom.destroyRig();
			spaceToTransferTo.newRig();
			player.setCash(player.getCash() - 20000);
			print("\n" + name + " has transferred a drill rig from Space " + index1 + " to Space " + index2
					+ " (- $20000).");
		}
	}

	public void transferDrillOffBoard(Player player, int index) {
		String name = player.getName();
		Space spaceToTransferFrom = spaces[index];

		boolean allowDrillTransfer = true;
		if (player.getCash() < 20000) {
			allowDrillTransfer = false;
			print("\n" + name + ", you cannot transfer a drill rig from Space " + index
					+ " as you require $20000 to transfer a drill.");
		} else {
			if (this.playerOwn(player, index)) {
				if (!spaceToTransferFrom.getRig()) {
					allowDrillTransfer = false;
					print("\n" + name + ", you cannot transfer a drill rig as there is none installed in Space " + index
							+ ".");
				}
			} else {
				allowDrillTransfer = false;
				print("\n" + name + ", you cannot transfer the drill off-board from Space " + index
						+ " as you do not own the space.");
			}
		}
		if (allowDrillTransfer) {
			spaceToTransferFrom.destroyRig();
			player.addOffBoardDrill(spaceToTransferFrom.getCard().getDrillType());
			print("\n" + name + " has transferred a drill rig from Space " + index + " to off-board (- $20000).");
			print("\nWhen required to buy a drill rig for a Space with a "
					+ spaceToTransferFrom.getCard().getTypeOfWater() + " yield, " + name
					+ " will automatically use the off-board drill rig.");
		}
	}

	public void viewActSpaces() {
		String str = "\nAll active spaces on the board (Bought): ";

		for (int i = 0; i < spaces.length; i++) {
			if (spaces[i].getOwner() != "") {
				str = str + i + " (Owned by " + spaces[i].getOwner() + ")";
				Space space = spaces[i];
				if (space.getRig()) {
					str = str + " (Drill installed)";
				}
				str = str + ", ";
			}
		}
		print(str);
	}

	public void viewMySpaces(Player player) {
		String str = "\nAll spaces bought / owned by " + player.getName() + " on the board: ";

		for (int i = 0; i < spaces.length; i++) {
			if (this.playerOwn(player, i)) {
				str = str + i;

				Space space = spaces[i];
				if (space.getRig()) {
					str = str + " (Drill installed)";
				}
				str = str + ", ";
			}
		}
		print(str);
	}

	public void showTaxesData(int round) {
		String str = "\n-- Taxes Details for Round " + round + " --\n" + this.taxesData.toString();

		print(str);
	}

	public double calcRevenue(Player player) {
		double profit = 0;
		for (int i = 0; i < spaces.length; i++) {
			if (this.playerOwn(player, i)) {
				Space space = spaces[i];
				profit += (space.getCard().getBarrels() * this.getWeatherData().getProductionMultiplier(space)
						* this.getOilPriceData().getPrice());
			}
		}
		double revenue = profit - (profit * this.getTaxesData().getRevTax());
		return revenue;
	}

	public boolean playerOwn(Player player, int index) {
		// Checking if the player owns a space.
		Space space = spaces[index];

		boolean ownDrill = false;
		if (player.getName() == space.getOwner()) {
			ownDrill = true;
		}
		return ownDrill;
	}

	public Space[] getSpaces() {
		return this.spaces;
	}

	public GreenCard getTaxesData() {
		return this.taxesData;
	}

	public RedCard getOilPriceData() {
		return this.oilPriceData;
	}

	public BrownCard getWeatherData() {
		return this.weatherData;
	}

	public void setNewTaxes() {
		// Setting new taxes and interest rate at the end of each round.
		this.taxesData = new GreenCard();
	}

	public void setNewWeatherData() {
		this.weatherData = new BrownCard();
	}

	public void setNewOilPriceData() {
		this.oilPriceData = new RedCard();
	}

	// Utility methods.
	private void print(String str) {
		System.out.println(str);
	}
}