import java.util.Arrays;

public class YahtzeeJudger {

	private int[] score;
	private YahtzeeDice gameDice;
	private boolean isJoker;
	
	public boolean canJoker() {
		return isJoker && score[gameDice.getNum()-1] != -1;
	}

	public YahtzeeJudger(YahtzeeDice gameDice, int[] score) {
		this.score = score;
		this.gameDice = gameDice;
	}

	public boolean scoreNum(int choice) {
		if (score[choice - 1] != -1) {
			return false;
		}
		score[choice - 1] = gameDice.countNum(choice) * choice;
		return true;
	}

	public boolean scoreChance() {
		if (score[13] != -1) {
			return false;
		}
		score[13] = gameDice.chance();
		return true;
	}

	public boolean scoreLStraight() {
		if (score[12] != -1) {
			return false;
		}
		score[12] = countLStraight();
		return true;
	}

	public boolean scoreSStraight() {
		if (score[11] != -1) {
			return false;
		}
		score[11] = countSStraight();
		return true;
	}

	public boolean score3oak() {
		if (score[8] != -1) {
			return false;
		}
		score[8] = count3oak();
		return true;
	}

	public boolean score4oak() {
		if (score[9] != -1) {
			return false;
		}
		score[9] = count4oak();
		return true;
	}

	public boolean scorefh() {
		if (score[10] != -1) {
			return false;
		}
		score[10] = countfh();
		return true;
	}

	public boolean scoreYahtzee() {
		if (gameDice.checkYahtzee()) {
			if (score[14] == -1) {
				score[14] = 50;
				return true;
			} else {
				return false;
			}
		} else {
			score[14] = 0;
			return true;
		}
	}

	public boolean checkYahtzee() {
		if (gameDice.checkYahtzee()) {
			if (score[14] != -1) {
				isJoker = true;
				return true;
			} else {
				isJoker = false;
				return false;
			}
		} else {
			isJoker = false;
			return false;
		}
	}

	public int countNum(int choice) {
		if (score[choice - 1] != -1) {
			return 0;
		}
		if(isJoker && choice == gameDice.getNum()) {
			return gameDice.chance();
		}
		return gameDice.countNum(choice) * choice;
	}

	public int countChance() {
		if (score[13] != -1) {
			return 0;
		}
		return gameDice.chance();
	}

	public int countLStraight() {
		if (score[12] != -1) {
			return 0;
		}
		int[] dice = gameDice.getDiceValues();
		Arrays.sort(dice);
		for (int i = 1; i < dice.length; i++) {
			if (dice[i] != dice[i - 1] + 1) {
				return 0;
			}
		}
		return 40;
	}
	
	public boolean isJoker() {
		return isJoker;
	}

	public int countSStraight() {
		if (score[11] != -1) {
			return 0;
		}
		if(canJoker()) {
			return 30;
		}
		int[] dice = gameDice.getDiceValues();
		Arrays.sort(dice);
		boolean works = true;
		for (int i = 0; i < dice.length; i++) {
			for (int j = 1; j < dice.length; j++) {
				if(j == i) {
					continue;
				}
				if(j-1 == i) {
					if (j != 1 && dice[j] != dice[j - 2] + 1) {
						works = false;
						break;
					}
				}
				else if (dice[j] != dice[j - 1] + 1) {
					works = false;
					break;
				}
			}
			if(works) {
				return 30;
			}
			works = true;
		}
		return 0;
	}

	public int count3oak() {
		if (score[8] != -1) {
			return 0;
		}
		if(canJoker()) {
			return 40;
		}
		if(canJoker()) {
			return gameDice.chance();
		}
		for (int i = 1; i <= 6; i++) {
			if (gameDice.countNum(i) == 3 || gameDice.countNum(i) == 4) {
				return gameDice.chance();
			}
		}
		return 0;
	}

	public int count4oak() {
		if (score[9] != -1) {
			return 0;
		}
		if(canJoker()) {
			return gameDice.chance();
		}
		for (int i = 1; i <= 6; i++) {
			if (gameDice.countNum(i) == 4) {
				return gameDice.chance();
			}
		}
		return 0;
	}

	public int countfh() {
		if (score[10] != -1) {
			return 0;
		}
		if(canJoker()) {
			return 25;
		}
		boolean two = false, three = false;
		for (int i = 1; i <= 6; i++) {
			if (gameDice.countNum(i) == 5 && score[14] != -1) {
				return 25;
			}
			if (gameDice.countNum(i) == 3) {
				three = true;
			} else if (gameDice.countNum(i) == 2) {
				two = true;
			}
		}
		if (two && three) {
			return 25;
		}
		return 0;
	}

	public int countYahtzee() {
		if (gameDice.checkYahtzee()) {
			if (score[14] == -1) {
				return 50;
			} else {
				return score[14] + 100;
			}
		} else {
			return 0;
		}
	}
}
