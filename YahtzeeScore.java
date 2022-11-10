import java.util.Arrays;

public class YahtzeeScore {
	private int[] score;
	private int[] pScore;
	private boolean gameEnd;
	private boolean bill;
	private YahtzeeJudger judge;

	public boolean end() {
		return gameEnd;
	}

	public int getScore(int i) {
		return score[i];
	}
	
	private String[] names = { "Ones", "Twos", "Threes", "Fours", "Fives", "Sixes", "SUM", "BONUS", "Three of a kind",
			"Four of a kind", "Full house", "Small Straight", "Large Straight", "Chance", "Yahtzee", "TOTAL SCORE" };
	private YahtzeeDice gameDice;

	public void printScore() {
		if(bill) {
			System.out.println("\n------ Bill's Score Card -----");
		}
		else {
			System.out.println("\n------- Your Score Card ------");
		}
		System.out.println("|----------------------------|");
		for (int i = 0; i < 16; i++) {
			System.out.printf("| %2d: %15s | %-5s|\n", i + 1, names[i],
					(score[i] < 0 ? (pScore[i] == 0 ? "--" : "(" + pScore[i] + ")") : score[i]));
		}
		System.out.println("------------------------------\n");
	}

	public void checkGameEnd() {
		int sum = 0;
		for (int i = 0; i < 15; i++) {
			if (score[i] == -1) {
				return;
			} else {
				sum += score[i];
			}
		}
		gameEnd = true;
		score[15] = sum;
	}

	public void checkBonus() {
		int sum = 0;
		for (int i = 0; i < 6; i++) {
			if (score[i] == -1) {
				return;
			} else {
				sum += score[i];
			}
		}
		score[6] = sum;
		score[7] = sum >= 63 ? 35 : 0;
	}

	public void setpScore() {
		for (int choice = 1; choice <= 16; choice++) {
			if (choice <= 6) {
				pScore[choice - 1] = judge.countNum(choice);
				continue;
			}
			if (judge.isJoker() && !judge.canJoker()) {
				pScore[choice - 1] = 0;
				continue;
			}
			switch (choice) {
			case 9:
				pScore[choice - 1] = judge.count3oak();
				break;
			case 10:
				pScore[choice - 1] = judge.count4oak();
				break;
			case 11:
				pScore[choice - 1] = judge.countfh();
				break;
			case 12:
				pScore[choice - 1] = judge.countSStraight();
				break;
			case 13:
				pScore[choice - 1] = judge.countLStraight();
				break;
			case 14:
				pScore[choice - 1] = judge.countChance();
				break;
			case 15:
				pScore[choice - 1] = judge.countYahtzee();
				break;
			default:
				pScore[choice - 1] = 0;
			}
		}
	}
	
	public void resetpScore() {

		for (int choice = 1; choice <= 16; choice++) {
			pScore[choice-1] = 0;
		}
	}

	public YahtzeeScore(YahtzeeDice gameDice, boolean bill) {
		gameEnd = false;
		score = new int[16];
		pScore = new int[16];
		this.bill = bill;
		for (int i = 0; i < 16; i++) {
			score[i] = -1;
		}
		judge = new YahtzeeJudger(gameDice, score);
		this.gameDice = gameDice;
	}

	public boolean claimScore(int choice) {
		judge.checkYahtzee();
		if (1 <= choice && choice <= 6) {
			return judge.scoreNum(choice);
		}
		if (judge.isJoker() && !judge.canJoker()) {
			return false;
		}
		switch (choice) {
		case 9:
			return judge.score3oak();
		case 10:
			return judge.score4oak();
		case 11:
			return judge.scorefh();
		case 12:
			return judge.scoreSStraight();
		case 13:
			return judge.scoreLStraight();
		case 14:
			return judge.scoreChance();
		case 15:
			return judge.scoreYahtzee();
		default:
			return false;
		}
	}

	public void claimJoker() {
		if(judge.isJoker()) {
			score[14]+= 100;
		}
	}
	
	public boolean isEligibleScore(int choice) {
		if(choice < 6) {
			choice++;
		}
		else if(choice <= 7) {
			choice += 3;
		}
		else if(choice == 8) {
			choice += 6;
		}
		else if(choice <= 11) {
			choice += 2;
		}
		else {
			choice += 3;
		}
		return score[choice-1] == -1;
	}
}
