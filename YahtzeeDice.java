import java.util.Arrays;

public class YahtzeeDice {
	private static final boolean DEBUG = false;
	Die[] dice;
	boolean[] hold;

	private void buildDice(int numSides) {
		dice = new Die[5];
		for (int i = 0; i < dice.length; i++) {
			dice[i] = new Die(numSides);
		}
		hold = new boolean[5];
	}

	public YahtzeeDice() {
		buildDice(6);
	}

	public int getNum() {
		return dice[0].getCurrentValue();
	}

	public YahtzeeDice(int numSides) {
		buildDice(numSides);
	}

	public int roll() {
		int sum = 0;
		for (int i = 0; i < dice.length; i++) {
			if (!hold[i]) {
				sum += dice[i].roll();
			} else {
				sum += dice[i].getCurrentValue();
			}
			if (DEBUG) {
				dice[i].setCurrentValue(6);
			}
		}
		Arrays.sort(dice);
		return sum;
	}

	public boolean setHold(String s) {
		boolean cont = false;
		for (int i = 0; i < hold.length; i++) {
			hold[i] = true;
		}
		for (int i = 0; i < Math.min(hold.length, s.length()); i++) {
			hold[i] = s.charAt(i) == 'h' ? true : false;
			if (s.charAt(i) == 'r') {
				cont = true;
			}
		}
		return cont;
	}

	public Die[] getDice() {
		return dice;
	}

	public int[] getDiceValues() {
		int[] values = new int[dice.length];
		for (int i = 0; i < dice.length; i++) {
			values[i] = dice[i].getCurrentValue();
		}
		return values;
	}

	@Override
	public String toString() {
		String yahtzee = "The " + dice.length + " dice read: ";
		for (int i = 0; i < dice.length; i++) {
			if (i < dice.length - 1)
				yahtzee += dice[i].getCurrentValue() + ", ";
			else
				yahtzee += dice[i].getCurrentValue() + ".";
		}
		return yahtzee;
	}

	public int countNum(int num) {
		int count = 0;
		for (int i = 0; i < dice.length; i++) {
			if (dice[i].getCurrentValue() == num) {
				count++;
			}
		}
		return count;
	}

	public int chance() {
		int count = 0;
		for (int i = 0; i < dice.length; i++) {
			count += dice[i].getCurrentValue();
		}
		return count;
	}

	public boolean checkYahtzee() {
		int same = getNum();

		for (int i = 0; i < dice.length; i++) {
			if (dice[i].getCurrentValue() != same) {
				return false;
			}
		}
		return true;
	}
}
