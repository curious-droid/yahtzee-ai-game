
public class YahtzeeGame {
	private static final String BILL_WIN = ""
			+ "   ___________\n"
			+ "  '._==_==_=_.'\n"
			+ "  .-\\:      /-.\n"
			+ " | (|:.     |) |\n"
			+ " | (|:.BILL |) |\n"
			+ "  '-|:.     |-'\n"
			+ "    \\::.    /\n"
			+ "     '::. .'\n"
			+ "       ) (\n"
			+ "     _.' '._\n"
			+ "    `\"\"\"\"\"\"\"`";
	private static final String YOU_WIN = ""
			+ "   ___________\n"
			+ "  '._==_==_=_.'\n"
			+ "  .-\\:      /-.\n"
			+ " | (|:.     |) |\n"
			+ " | (|:.YOU  |) |\n"
			+ "  '-|:.     |-'\n"
			+ "    \\::.    /\n"
			+ "     '::. .'\n"
			+ "       ) (\n"
			+ "     _.' '._\n"
			+ "    `\"\"\"\"\"\"\"`";
	private static final String TIE = ""
			+ "   ___________\n"
			+ "  '._==_==_=_.'\n"
			+ "  .-\\:      /-.\n"
			+ " | (|:.     |) |\n"
			+ " | (|:.TIE  |) |\n"
			+ "  '-|:.     |-'\n"
			+ "    \\::.    /\n"
			+ "     '::. .'\n"
			+ "       ) (\n"
			+ "     _.' '._\n"
			+ "    `\"\"\"\"\"\"\"`";
	private YahtzeeDice gameDice;
	private YahtzeeScore gameScore;
	private YahtzeeScore AIScore;
	private DiceManager dice;
	private YahtzeeAI AI;

	public YahtzeeGame() {
		gameDice = new YahtzeeDice();
		gameScore = new YahtzeeScore(gameDice, false);
		dice = new DiceManager(gameDice);
		AIScore = new YahtzeeScore(gameDice, true);
		AI = new YahtzeeAI(AIScore, dice);
	}

	public void playGame() {
		System.out.println("   ****  Yahtzee Game  ****\n");
		
		while (!gameScore.end()) {
			playRound();
			gameScore.checkBonus();
			gameScore.checkGameEnd();
			playAIRound();
			AIScore.checkBonus();
			AIScore.checkGameEnd();
		}
		
		System.out.println("Game Over!");
		sleep();
		gameScore.printScore();
		sleep();
		AIScore.printScore();
		sleep();
		System.out.println("You scored " + gameScore.getScore(15) + " points.");
		sleep();
		System.out.println("Bill scored " + AIScore.getScore(15) + " points.");
		sleep();
		if (AIScore.getScore(15) > gameScore.getScore(15)) {
			System.out.println(BILL_WIN);
			System.out.println("    Bill wins!");
		} else if (AIScore.getScore(15) < gameScore.getScore(15)) {
			System.out.println(YOU_WIN);
			System.out.println("    You win!");
		} else if (AIScore.getScore(15) == gameScore.getScore(15)) {
			System.out.println(TIE);
			System.out.println("   Perfect tie!");
		}
	}

	public void playRound() {
		sleep();
		System.out.println("   ****   Your Turn   ****");
		sleep();
		boolean cont = true;
		for (int i = 0; i < 3 && cont; i++) {
			gameDice.roll();
			gameScore.setpScore();
			gameScore.printScore();
			System.out.println(gameDice.toString());
			if (i != 2) {
				cont = getInput();
			}
		}
		gameDice.setHold("rrrrr");
		score();
	}

	public void playAIRound() {
		sleep();
		System.out.println("   ****  Bill's Turn  ****");
		boolean cont = true;
		for (int i = 0; i < 3 && cont; i++) {
			gameDice.roll();
			AIScore.setpScore();
			sleep();
			AIScore.printScore();
			System.out.println(gameDice.toString());
			if (i != 2) {
				cont = AIgetInput();
			}
		}
		gameDice.setHold("rrrrr");
		AIscore();
	}

	public void score() {
		int choice;
		do {
			System.out.print("Choose your combination number (1-6, 9-15): ");
			choice = TextIO.getlnInt();
		} while (!gameScore.claimScore(choice));
		gameScore.claimJoker();
		gameScore.resetpScore();
		gameScore.printScore();
		System.out.println("You scored " + gameScore.getScore(choice - 1) + " points.\n");
	}

	public boolean getInput() {
		System.out.print("Roll or Hold? Enter a 5-character string of 'h' (hold) and 'r' (roll): ");
		String input = TextIO.getlnString();
		return gameDice.setHold(input);
	}

	public void AIscore() {
		int choice;
		do {
			choice = getAIChoice();
		} while (!AIScore.claimScore(choice));
		System.out.println("Bill chooses " + choice + ".");
		AIScore.claimJoker();
		sleep();
		AIScore.resetpScore();
		AIScore.printScore();
		System.out.println("Bill scored " + AIScore.getScore(choice - 1) + " points.\n");
	}

	public boolean AIgetInput() {
		String input = getAIMove();
		System.out.println("Bill chooses " + input + ".");
		return gameDice.setHold(input);
	}

	public String getAIMove() {
		sleep();
		dice.updateDice();
		boolean[] hold = AI.getBestMove(gameDice.getDiceValues());
		String holds = "";
		for (int i = 0; i < 5; i++) {
			if (hold[i]) {
				holds += "h";
			} else {
				holds += "r";
			}
		}
		return holds;
	}

	public int getAIChoice() {
		sleep();
		dice.updateDice();
		int choice = AI.getHighestChoice();
		if (choice < 6) {
			choice++;
		} else if (choice <= 7) {
			choice += 3;
		} else if (choice == 8) {
			choice += 6;
		} else if (choice <= 11) {
			choice += 2;
		} else {
			choice += 3;
		}
		return choice;
	}

	private void sleep() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
