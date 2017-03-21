import java.util.Scanner;
import java.util.ArrayList;
import java.io.*;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class Driver {

	public Scanner input;

	public ArrayList<Player> players;
	public Board board;

	public int turns;
	public int rounds;

	public static void main(String[] args) {
		Driver game = new Driver();
			
		boolean loaded = game.runStartMenu();
		
		do {
			// Phase 1.
			if (game.rounds == 0 && game.turns == 1) {
				if (!loaded) {
					game.getNumOfPlayers();
				
					for (int i = 0; i < game.players.size(); i++) {
						game.takeLoan(game.players.get(i));
					}
				}
			}
			// Phase 2.
			for (int i = 0; i < game.players.size(); i++) {
				game.runMenu(game.players.get(i));
			}

			// Phase 3.
			for (int j = 0; j < game.players.size(); j++) {
				game.payLoan(game.players.get(j));
			}

			// End of each turn.
			game.turns++;

			for (int i = 0; i < game.players.size(); i++) {
				Player currentPlayer = game.players.get(i);
				currentPlayer.setSitesTested(0);
				currentPlayer.setConcessionsBought(0);
			}

			if (game.turns > 3) {
				game.rounds++;
				game.turns = 1;
			}
			game.print("\nIt is now Round " + game.rounds + ", the current Turn is " + game.turns + ".");
			try{
			game.save();
			}
			catch(Exception e){
				
			}
		} while (game.players.size() != 1);

		// When a winner is found;
		Player winner = game.players.get(0);
		game.print(winner + " has won the game. He is the sole player on the board and has $" + winner.getCash()
				+ " with a debt of $" + winner.getDebt() + "!");
	}

	public Driver() {
		this.input = new Scanner(System.in);

		this.players = new ArrayList<Player>();
		this.board = new Board();

		this.turns = 1;
		this.rounds = 0;
	}
	
	public int startMenu(){
		System.out.println("Would you like to Start a new or Load a game?");
		System.out.println(" 1) New Game");
		System.out.println(" 2) Load Game");
		
		int option = input.nextInt();
		return option;
	}
	
	public boolean runStartMenu(){
		 int option = startMenu();
		 boolean loaded = false;
		 
		 switch (option) {
			case 1:
				//New Game
				loaded = false;
				break;
			case 2:
				// Load game
				try{
				load();
				}
				catch (Exception e){
				
				}
				loaded = true;
				break;
		 }
		 return loaded;
		 
	}

	// Andy's methods.
	public void getNumOfPlayers() {
		boolean goodInput = false;
		do {
			// Repeating this block of code until valid inputs / numbers are
			// given.
			try {
				print("How many players are playing? (Between 2 and 6 inclusive)");

				int num = getIntOption();
				while (num < 2 || num > 6) {
					print("\nPlease type a number between 2 and 6 inclusive!");
					num = getIntOption();
				}
				input.nextLine();

				for (int i = 0; i < num; i++) {
					print("\nPlayer " + (i + 1) + ", what do you want your name to be?");

					String name = "";
					boolean badInput = true;
					while (badInput) {
						name = getStringOption();
						badInput = false;
						for (int j = 0; j < players.size(); j++) {
							if (players.get(j).getName() == name) {
								badInput = true;
							}
						}
					}
					players.add(new Player(name));
				}
				goodInput = true;
			} catch (Exception e) {
				// Error handling.
				errMessage();
				break;
			}
		} while (!goodInput);
	}

	public void takeLoan(Player player) {
		String name = player.getName();

		print("\n" + name + ", how much loan do you want to take out?");
		for (int i = 0; i < 8; i++) {
			// a + (n - 1) * d Formula to calculate the loan (0.5 million to 4
			// million).
			double sum = 0.5 + (i) * 0.5;
			print(i + ") Take out " + sum + " million.");
		}
		boolean goodInput = false;
		do {
			try {
				int option = getIntOption();
				while ((option < 0 || option > 8) || (rounds == 0 && option > 1)) {
					int max = 8;
					if (rounds == 0) {
						print("\n" + name
								+ ", since it's the first round, you can only take a sum of 0.5 or 1 million.");
						max = 1;
					}
					print("\nPlease put a valid number request between 0 and " + max + " inclusive.");
					option = getIntOption();
				}
				double sum = (0.5 + (option) * 0.5) * 1000000;
				double debt = player.getDebt();

				boolean allowLoan = true;
				if (debt + sum > 4000000) {
					allowLoan = false;
					print("\n" + name + ", you cannot take a loan as you will exceed 4 million in debt.");
				}
				if (allowLoan) {
					player.setCash(player.getCash() + sum);
					player.setDebt(debt + sum);
					print("\n" + name + " has taken a loan from the bank (+ $" + sum + ").");
				}
				goodInput = true;
			} catch (Exception e) {
				errMessage();
				break;
			}
		} while (!goodInput);
	}

	public void auctionForDrillingConcession(Player player) {
		String name = player.getName();

		boolean goodInput = false;
		while (!goodInput) {
			try {
				print("\n" + name + ", which space do you want to buy / auction? (Between 0 and 27 inclusive)");

				int index = getIntOption();
				while (index < 0 || index > 27) {
					print("\nPlease put a valid number request between 0 and 27 inclusive!");
					index = getIntOption();
				}
				;
				print("\n" + name + ", how much do you want to bid for Space " + index + "?");

				double currentBid = getDoubleOption();
				while (player.getCash() < currentBid || currentBid < 100000) {
					if (currentBid < 100000) {
						print("\n" + name + ", you must bid higher than $100000.");
					} else {
						print("\n" + name
								+ ", you do not have enough cash to afford the bid! Please bid a lower amount.");
					}
					currentBid = getDoubleOption();
				}

				// Storing all the players into an ArrayList to eventually check
				// who stops bidding.
				ArrayList<Player> playersOnBid = new ArrayList<Player>();
				for (int i = 0; i < players.size(); i++) {
					Player currentPlayer = players.get(i);
					if (currentPlayer.getName() != player.getName()) { // Making
																		// sure
																		// the
																		// bid
																		// starter
																		// goes
																		// last.
						playersOnBid.add(players.get(i));
					}
				}
				playersOnBid.add(player);

				// Looping until there's only one bidder left.
				while (playersOnBid.size() != 1) {
					for (int j = 0; j < playersOnBid.size(); j++) {
						Player currentPlayer = playersOnBid.get(j);
						if (player.getCash() >= currentBid) {
							print("\n" + currentPlayer.getName() + ", do you want to bid higher than $" + currentBid
									+ " for Space " + index + "? (y for Yes / Anything else for No)");

							char bool = getStringOption().charAt(0);
							boolean wantToBid = false;
							if (bool == 'y') {
								wantToBid = true;
							} else {
								wantToBid = false;
							}
							if (wantToBid) {
								print("\n" + currentPlayer.getName()
										+ ", how much do you want to bid (must be higher than $" + currentBid
										+ " and less than $" + currentPlayer.getCash() + ") for Space " + index + "?");

								double toBid = getDoubleOption();
								while ((toBid < currentBid) || (currentPlayer.getCash() < toBid)) {
									print("\nPlease bid higher than $" + currentBid + " and less than $"
											+ currentPlayer.getCash() + ".");
									toBid = getDoubleOption();
								}
								currentBid = toBid;
							} else {
								playersOnBid.remove(j);
								print("\n" + currentPlayer.getName() + " has stopped bidding.");
							}
						} else {
							playersOnBid.remove(j);
							print("\n" + currentPlayer.getName()
									+ " cannot afford the bid and thus has been removed from the auction.");
						}
					}
				}
				if (playersOnBid.get(0).getName() != "") {
					Player newOwner = playersOnBid.get(0);
					print("\n" + newOwner.getName()
							+ " has won the auction for buying the drilling concession rights for Space " + index
							+ ".");

					board.buyPlot(newOwner, index, currentBid);
				}
				goodInput = true;
			} catch (Exception e) {
				errMessage();
				break;
			}
		}
	}

	// Daemon's methods.
	public int mainMenu(Player player) {
		print("\n" + player.getName() + ", what do you want to do? (Cash: $" + player.getCash() + " | Debt: $"
				+ player.getDebt() + ")");
		print("0) Take out a loan.");
		print("1) Survey one or more spaces (site-test).");
		print("2) Purchase a drilling concession for a surveyed space (via auction).");
		print("3) Sell a drilling concession from an owned space.");
		print("4) Purchase an oil drill for an owned space.");
		print("5) Sell an oil drill from an owned space.");
		print("6) Transfer an oil drill from an owned space.");
		print("7) End turn.");

		boolean goodInput = false;
		int option = 0;
		while (!goodInput) {
			try {
				option = getIntOption();
				while (option < 0 || option > 7) {
					print("Invalid option selected. Please select a valid number request between 0 and 7 inclusive.");
					option = getIntOption();
				}
				goodInput = true;
			} catch (Exception e) {
				errMessage();
				break;
			}
		}
		return option;
	}

	public void runMenu(Player player) {
		boolean endTurn = false;

		while (!endTurn) {
			int option = mainMenu(player);
			switch (option) {
			case 0:
				// Take a loan from the bank.
				print("\nYou have decided to take another loan from the bank.");
				if (this.rounds == 0 && this.turns == 1) {
					print("\nYou cannot take out another loan immediately after your first turn.");
				} else {
					this.takeLoan(player);
				}
				break;
			case 1:
				// Survey a space (max: 3 times).
				print("\nYou have decided to survey a or all spaces.");
				runViewSpacesMenu(player);
				break;
			case 2:
				// Buy a drilling concession (max: 3 times).
				print("\nYou have decided to buy a drilling concession.");
				if (player.getConcessionsBought() > 3) {
					print("\nTou cannot buy another drilling concession as you have already bought three concessions this turn.");
				} else {
					auctionForDrillingConcession(player);
				}
				break;
			case 3:
				// Sell a drilling concession.
				print("\nYou have decided to sell one of your drilling concessions.");
				print("\nWhich Space's drilling concession would you like to sell?");
				board.viewMySpaces(player);

				boolean goodInput = false;
				int index = 0;
				while (!goodInput) {
					try {
						index = getIntOption();
						while (index < 0 || index > 27) {
							print("\nPlease put a valid number request between 0 and 27 inclusive.");
							index = getIntOption();
						}
						goodInput = true;
					} catch (Exception e) {
						errMessage();
						break;
					}
				}
				board.sellPlot(player, index);
				break;
			case 4:
				// Buy an oil drill.
				print("\nYou have decided to buy an oil drill rig for your drilling concessions.");
				print("\nWhich Space would you like to buy an oil drill rig for?");
				board.viewMySpaces(player);

				boolean goodInput1 = false;
				int index1 = 0;
				while (!goodInput1) {
					try {
						index1 = getIntOption();
						while (index1 < 0 || index1 > 27) {
							print("\nPlease put a valid number request between 0 and 27 inclusive.");
							index1 = getIntOption();
						}
						;
						goodInput1 = true;
					} catch (Exception e) {
						errMessage();
						break;
					}
				}
				board.buyDrill(player, index1);
				break;
			case 5:
				// Sell an oil drill.
				print("\nYou have decided to sell an oil drill.");
				print("\nWhich Space's drill rig would you like to sell?");
				board.viewMySpaces(player);

				boolean goodInput2 = false;
				int index2 = getIntOption();
				while (!goodInput2) {
					try {

						index2 = getIntOption();
						while (index2 < 0 || index2 > 27) {
							print("\nPlease put a valid number request between 0 and 27 inclusive.");
							index2 = getIntOption();
						}
						;
						goodInput2 = true;
					} catch (Exception e) {
						errMessage();
						break;
					}
				}
				board.sellDrill(player, index2);
				break;
			case 6:
				// Transfer an oil drill.
				print("\nYou have decided to transfer an oil drill.");
				runTransferDrillMenu(player);
				break;
			case 7:
				//End turn
				endTurn = true;
				break;
			default:
				print("\nInvalid option entered.");
				break;
			}
		}
	}

	public int viewSpacesMenu() {
		print("0) Survey a Space (Costs $100000).");
		print("1) View all active Spaces (Index number only).");
		print("2) View your active Spaces (Index number only).");

		boolean goodInput = false;
		int option = 0;
		while (!goodInput) {
			try {
				option = getIntOption();
				while (option < 0 || option > 2) {
					print("\nInvalid option selected. Please select a number between 0 and 2 inclusive.");
					option = getIntOption();
				}
				goodInput = true;
			} catch (Exception e) {
				errMessage();
				break;
			}
		}
		return option;
	}

	public void runViewSpacesMenu(Player player) {
		int option = viewSpacesMenu();

		switch (option) {
		case 0:
			print("\nYou have decided to survey a Space.");

			if (player.getSitesTested() > 3) {
				print("\nYou cannot survey another space as you have already surveyed thrice this turn.");
			} else {
				print("\nWhich Space would you like to survey? Please provide an index number for the Space between 0 and 27 inclusive.");

				boolean goodInput = false;
				int index = 0;
				while (!goodInput) {
					try {
						index = getIntOption();
						while (index < 0 || index > 27) {
							print("\nPlease enter a valid number request between 0 and 27 inclusive.");
							index = getIntOption();
						}
						;
						goodInput = true;
					} catch (Exception e) {
						errMessage();
					}
				}
				board.surveyPlot(player, index);
			}
			break;
		case 1:
			print("\nYou have decided to view all active Spaces.");
			board.viewActSpaces();
			break;
		case 2:
			print("\nYou have decided to view all your owned Spaces.");
			board.viewMySpaces(player);
			break;
		default:
			print("\nInvalid option selected. Please select a number between 0 and 1 inclusive.");
		}
	}

	public int transferDrillMenu() {
		print("0) Transfer a drill rig to another drilling concession (Costs $20000).");
		print("1) Transfer a drill rig off the board (Can be used later) (Costs $20000).");

		boolean goodInput = false;
		int option = 0;
		while (!goodInput) {
			try {
				option = getIntOption();
				while (option < 0 || option > 1) {
					print("\nInvalid option selected. Please select a number between 0 and 1 inclusive.");
					option = getIntOption();
				}
				goodInput = true;
			} catch (Exception e) {
				errMessage();
				break;
			}
		}
		return option;
	}

	public void runTransferDrillMenu(Player player) {
		int option = transferDrillMenu();

		switch (option) {
		case 0:
			boolean goodInput = false;
			while (!goodInput) {
				try {
					print("\nYou have decided to transfer an oil drill to another drilling concession.");
					print("\nWhich space would you like to transfer an oil drill from?");

					int index = getIntOption();
					while (index < 0 || index > 27) {
						print("\nPlease enter a number between 0 and 27 inclusive!");
						index = getIntOption();
					}
					;
					print("\nWhich space would you like to transfer an oil drill to?");
					int index2 = getIntOption();
					while (index2 < 0 || index2 > 27) {
						print("\nPlease enter a number between 0 and 27 inclusive!");
						index2 = getIntOption();
					}
					;
					board.transferDrill(player, index, index2);
					goodInput = true;
				} catch (Exception e) {
					errMessage();
					break;
				}
			}
			break;
		case 1:
			boolean goodInput1 = false;
			while (!goodInput1) {
				try {
					print("\nYou have decided to transfer an oil drill off the board.");
					print("\nWhich space would you like to transfer an oil drill from?");

					int index = getIntOption();
					while (index < 0 || index > 27) {
						print("\nPlease enter a number between 0 and 27 inclusive!");
						index = getIntOption();
					}
					;
					board.transferDrillOffBoard(player, index);
					goodInput1 = true;
				} catch (Exception e) {
					errMessage();
					break;
				}
			}
			break;
		default:
			print("\nInvalid option selected. Please select a number between 0 and 1 inclusive.");
		}
	}

	public void doInterestPayment() {
		double interest = board.getTaxesData().getInterest();

		for (int i = 0; i < players.size(); i++) {
			Player currentPlayer = players.get(i);
			String name = currentPlayer.getName();
			double cash = currentPlayer.getCash();
			double debt = currentPlayer.getDebt();
			double interestOwed = debt * interest;

			boolean canPay = true;
			if (cash < interestOwed) {
				canPay = false;
			}

			if (!canPay) {
				if (debt >= 4000000) {
					print("\n" + name + ", you have a debt of over $4000000.");
					players.remove(i);
				} else {
					print("\n" + name + ", you need to take out another loan in order to pay off your interest.");
					this.takeLoan(currentPlayer);
					canPay = true;
				}
			}
			if (canPay) {
				print("\n" + name + ", you have paid off your loan for this round (- $" + interestOwed + ").");
				currentPlayer.setCash(cash - interestOwed);
			}
		}
	}

	// To Do: When should we call this?
	public void payLoan(Player player) {
		String name = player.getName();
		double cash = player.getCash();
		double debt = player.getDebt();
		print("\n" + name + ", how much would you like to pay off your loan?\nCash: $" + cash + " | Debt: " + debt);

		for (int i = 0; i < 8; i++) {
			double sum = 0.5 + (i) * 0.5;
			print(i + ") Pay off " + sum + " million.");
		}
		print("8) Don't pay.");

		boolean goodInput = false;
		do {
			try {
				int option = getIntOption();
				while (option < 0 || option > 8) {
					print("\nPlease put a valid number request between 0 and 8 inclusive.");
					option = getIntOption();
				}
				if (option < 8) {
					boolean canPay = true;

					double sum = (0.5 + (option) * 0.5) * 1000000;
					if (cash < sum) {
						print("\n" + name + ", you can't afford to pay off your loan, you only have $" + cash + ".");
						canPay = false;
					}
					if (canPay) {
						player.setCash(cash - sum);
						player.setDebt(debt - sum);
						print("\n" + name + " has paid off " + sum + " off their debt (- $" + sum + ").");
					}
				}
				goodInput = true;
			} catch (Exception e) {
				errMessage();
				break;
			}
		} while (!goodInput);
	}
	@SuppressWarnings("unchecked")
	
	public void save() throws Exception {
		XStream xstream = new XStream(new DomDriver());
		ObjectOutputStream out = xstream.createObjectOutputStream(new FileWriter("saveData.xml"));
		out.writeObject(players);
		out.writeObject(board);
		out.close();
	}
	
	public void load() throws Exception {
		XStream xstream = new XStream(new DomDriver());
		ObjectInputStream is = xstream.createObjectInputStream(new FileReader("saveData.xml"));
		players = (ArrayList<Player>) is.readObject();
		board = (Board) is.readObject();
		is.close();
	}
	

	private void errMessage() {
		print("\nError caused; Please enter a valid integer.");
	}

	// Utility methods.
	private void print(String str) {
		System.out.println(str);
	}

	// Simplifying getting I/O.
	private int getIntOption() {
		System.out.print("> ");
		int option = input.nextInt();
		return option;
	}

	private String getStringOption() {
		System.out.print("> ");
		String text = input.nextLine();
		return text;
	}

	private double getDoubleOption() {
		System.out.print("> ");
		Double num = input.nextDouble();
		return num;
	}
}