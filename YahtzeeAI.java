import java.util.ArrayList;

public class YahtzeeAI {

	private DiceManager dice;
	private AIUtils utils;
	private YahtzeeScore score;
	private double[] categoryWeights = {0.3, 0.5, 1, 1, 1.25, 1.5, 1, 1, 0.5, 1, 1, 1, 1};// must be length 13
	
	public YahtzeeAI(YahtzeeScore score, DiceManager dice) {
		this.dice = dice;
		utils = new AIUtils();
		this.score = score;
	}
	
	public boolean[] getBestMove(int[] dice){
		
		boolean[] bestHold = null;
		double bestValue = -1;
		
		for(int a = 0; a < 2; a++){
			for(int b = 0; b < 2; b++){
				for(int c = 0; c < 2; c++){
					for(int d = 0; d < 2; d++){
						for(int e = 0; e < 2; e++){
							
							boolean[] hold = {a == 1, b == 1, c == 1, d == 1, e == 1};
							
							double value = getExpectedValue(hold, dice);
							
							if(value > bestValue){
								bestValue = value;
								bestHold = hold;
							}
						}
					}
				}
			}
		}
		
		return bestHold;
	}
	
	private double getWeightedValue(double value, int scoreIndex){
		return value * categoryWeights[scoreIndex];
	}
	
	private double getExpectedValue(boolean[] hold, int[] dice){
		
		int value = 0;
		double probability;
		
		ArrayList<Integer> rerollIndices = new ArrayList<Integer>();
		
		for(int i = 0; i < hold.length; i++){
			if(!hold[i]){
				rerollIndices.add(i);
			}
		}
		
		int numReroll = rerollIndices.size();
		probability = 1.0/(Math.pow(6, numReroll));

		if(numReroll == 0){
			value = calculateZeroRerollExpectation(dice);
		}else if(numReroll == 1){// one reroll
			value = calculateOneRerollExpectation(rerollIndices, dice);
		}else if(numReroll == 2){ // two rerolls
			value = calculateTwoRerollExpectation(rerollIndices, dice);
		}else if(numReroll == 3){ // three rerolls
			value = calculateThreeRerollExpectation(rerollIndices, dice);
		}else if(numReroll == 4){ // four rerolls
			value = calculateFourRerollExpectation(rerollIndices, dice);
		}else if(numReroll == 5){ // five rerolls
			value = calculateFiveRerollExpectation(rerollIndices, dice);
		}
		
		return value * probability;
	}
	
	private int calculateZeroRerollExpectation(int[] dice){
		
		int value = 0;
		
		int[] tempDice = (int[])dice.clone();
		
		tempDice = convertToDiceContents(tempDice);
		
		for(int a = 0; a < 13; a++){
			if(score.isEligibleScore(a))// only add eligible categories
				value += getWeightedValue(utils.get(a, tempDice), a);
			else if(a == 12)
				value += getWeightedValue(utils.get(a, tempDice), a);// always add yahtzee chance	
		}
		
		return value;
	}
	
	private int calculateOneRerollExpectation(ArrayList<Integer> rerollIndices, int[] dice){
		
		int value = 0;
		
		for(int i = 1; i <= 6; i++){// six possible values to reroll so i = 1 through 6
			for(int a = 0; a < 13; a++){
				if(score.isEligibleScore(a)){// only add eligible categories
					int[] tempDice = (int[])dice.clone();
					
					tempDice[rerollIndices.get(0)] = i;
					
					tempDice = convertToDiceContents(tempDice);
					
					value += getWeightedValue(utils.get(a, tempDice), a);
				}else if(a == 12){// always add yahtzee chance
					int[] tempDice = (int[])dice.clone();
					
					tempDice[rerollIndices.get(0)] = i;
					
					tempDice = convertToDiceContents(tempDice);
					
					value += getWeightedValue(utils.get(a, tempDice), a);
				}
			}
		}
		
		return value;
	}
	
	private int calculateTwoRerollExpectation(ArrayList<Integer> rerollIndices, int[] dice){
		
		int value = 0;
		
		for(int i = 1; i <= 6; i++){// six possible values to reroll so i = 1 through 6
			for(int j = 1; j <= 6; j++){
				for(int a = 0; a < 13; a++){
					if(score.isEligibleScore(a)){// only add eligible categories
						int[] tempDice = (int[])dice.clone();
						
						tempDice[rerollIndices.get(0)] = i;
						tempDice[rerollIndices.get(1)] = j;
						
						tempDice = convertToDiceContents(tempDice);
						
						value += getWeightedValue(utils.get(a, tempDice), a);
					}else if(a == 12){// always add yahtzee chance
						int[] tempDice = (int[])dice.clone();
						
						tempDice[rerollIndices.get(0)] = i;
						tempDice[rerollIndices.get(1)] = j;
						
						tempDice = convertToDiceContents(tempDice);
						
						value += getWeightedValue(utils.get(a, tempDice), a);
					}
				}
			}
		}
		
		return value;
	}
	
	private int calculateThreeRerollExpectation(ArrayList<Integer> rerollIndices, int[] dice){
		
		int value = 0;
		
		for(int i = 1; i <= 6; i++){// six possible values to reroll so i = 1 through 6
			for(int j = 1; j <= 6; j++){
				for(int k = 1; k <= 6; k++){
					for(int a = 0; a < 13; a++){
						if(score.isEligibleScore(a)){// only add eligible categories
							int[] tempDice = (int[])dice.clone();
							
							tempDice[rerollIndices.get(0)] = i;
							tempDice[rerollIndices.get(1)] = j;
							tempDice[rerollIndices.get(2)] = k;
							
							tempDice = convertToDiceContents(tempDice);
							
							value += getWeightedValue(utils.get(a, tempDice), a);
						}else if(a == 12){// always add yahtzee chance
							int[] tempDice = (int[])dice.clone();
							
							tempDice[rerollIndices.get(0)] = i;
							tempDice[rerollIndices.get(1)] = j;
							tempDice[rerollIndices.get(2)] = k;
							
							tempDice = convertToDiceContents(tempDice);
							
							value += getWeightedValue(utils.get(a, tempDice), a);
						}
					}
				}
			}
		}
		
		return value;
	}
	
	private int calculateFourRerollExpectation(ArrayList<Integer> rerollIndices, int[] dice){
		
		int value = 0;
		
		for(int i = 1; i <= 6; i++){// six possible values to reroll so i = 1 through 6
			for(int j = 1; j <= 6; j++){
				for(int k = 1; k <= 6; k++){
					for(int l = 1; l <= 6; l++){
						for(int a = 0; a < 13; a++){
							if(score.isEligibleScore(a)){// only add eligible categories
								int[] tempDice = (int[])dice.clone();
								
								tempDice[rerollIndices.get(0)] = i;
								tempDice[rerollIndices.get(1)] = j;
								tempDice[rerollIndices.get(2)] = k;
								tempDice[rerollIndices.get(3)] = l;
								
								tempDice = convertToDiceContents(tempDice);
								
								value += getWeightedValue(utils.get(a, tempDice), a);
							}else if(a == 12){// always add yahtzee chance
								int[] tempDice = (int[])dice.clone();
								
								tempDice[rerollIndices.get(0)] = i;
								tempDice[rerollIndices.get(1)] = j;
								tempDice[rerollIndices.get(2)] = k;
								tempDice[rerollIndices.get(3)] = l;
								
								tempDice = convertToDiceContents(tempDice);
								
								value += getWeightedValue(utils.get(a, tempDice), a);
							}
						}
					}
				}
			}
		}
		
		return value;
	}
	
	private int calculateFiveRerollExpectation(ArrayList<Integer> rerollIndices, int[] dice){
		
		int value = 0;
		
		for(int i = 1; i <= 6; i++){// six possible values to reroll so i = 1 through 6
			for(int j = 1; j <= 6; j++){
				for(int k = 1; k <= 6; k++){
					for(int l = 1; l <= 6; l++){
						for(int m = 1; m <= 6; m++){
							for(int a = 0; a < 13; a++){
								if(score.isEligibleScore(a)){// only add eligible categories
									int[] tempDice = (int[])dice.clone();
									
									tempDice[rerollIndices.get(0)] = i;
									tempDice[rerollIndices.get(1)] = j;
									tempDice[rerollIndices.get(2)] = k;
									tempDice[rerollIndices.get(3)] = l;
									tempDice[rerollIndices.get(4)] = m;
									
									tempDice = convertToDiceContents(tempDice);
									
									value += getWeightedValue(utils.get(a, tempDice), a);
								}else if(a == 12){// always add yahtzee chance
									int[] tempDice = (int[])dice.clone();
									
									tempDice[rerollIndices.get(0)] = i;
									tempDice[rerollIndices.get(1)] = j;
									tempDice[rerollIndices.get(2)] = k;
									tempDice[rerollIndices.get(3)] = l;
									tempDice[rerollIndices.get(4)] = m;
									
									tempDice = convertToDiceContents(tempDice);
									
									value += getWeightedValue(utils.get(a, tempDice), a);
								}
							}
						}
					}
				}
			}
		}
		
		return value;
	}
	
	private int[] convertToDiceContents(int[] dice){//converts to a length-6 array which is sorted by {# ones, # twos, # threes, # fours, # fives, # sixes}
		int[] diceContents = new int[6];
		
		for(int i = 0; i < dice.length; i++){
			diceContents[dice[i] - 1]++;
		}
		
		return diceContents;
	}
	
	public int getHighestChoice(){

		int highestValue = -1;
		int highestChoice = -1;
		
		for(int i = 3; i <= 6; i++){// threes through sixes
			if(dice.hasNumber(i) && score.isEligibleScore(i - 1)){
				if(dice.determineScore(i - 1) > highestValue){
					highestValue = dice.determineScore(i - 1);
					highestChoice = i - 1;
				}
			}
		}
		
		if(dice.isFourKind() && score.isEligibleScore(7)){// four of a kind
			if(dice.determineScore(7) > highestValue){
				highestValue = dice.determineScore(7);
				highestChoice = 7;
			}
		}
		
		if(dice.isThreeKind() && score.isEligibleScore(6)){// three of a kind
			if(dice.determineScore(6) > highestValue){
				highestValue = dice.determineScore(6);
				highestChoice = 6;
			}
		}
		
		if(dice.isFullHouse() && score.isEligibleScore(9)){// full house
			if(dice.determineScore(9) > highestValue){
				highestValue = dice.determineScore(9);
				highestChoice = 9;
			}
		}
		
		if(dice.isSmallStraight() && score.isEligibleScore(10)){// small straight
			if(dice.determineScore(10) > highestValue){
				highestValue = dice.determineScore(10);
				highestChoice = 10;
			}
		}
		
		if(dice.isLargeStraight() && score.isEligibleScore(11)){// large straight
			if(dice.determineScore(11) > highestValue){
				highestValue = dice.determineScore(11);
				highestChoice = 11;
			}
		}
		
		if(dice.isYahtzee()){// yahtzee
			if(score.isEligibleScore(12)){
				if(dice.determineScore(12) > highestValue){
					highestValue = dice.determineScore(12);
					highestChoice = 12;
				}
			}
		}
		
		if(highestChoice == -1){// if there has not been a score chosen, pick chance; or pick a random value if chance has already been chosen
			
			for(int i = 1; i <= 2; i++){// ones and twos
				if(dice.hasNumber(i) && score.isEligibleScore(i - 1)){
					highestChoice = i - 1;
				}
			}
			
			if(highestChoice == -1){// if there still hasn't been a choosen score...
				if(score.isEligibleScore(8)){//chance
					if(dice.determineScore(8) > highestValue){
						highestValue = dice.determineScore(8);
						highestChoice = 8;
					}
				}else{
					for(int i = 0; i < 13; i++){
						if(score.isEligibleScore(i)){
							highestChoice = i;
							break;
						}
					}
				}
			}
		}

		return highestChoice;
	}
	
}
