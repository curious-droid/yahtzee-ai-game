public class DiceManager{
	
	private int[] diceContents = new int[6];
	private int numConsecutive = 0;
	
	private int straightStartIndex;//exclusive
	private int straightEndIndex;//inclusive
	
	private boolean moveDone;
	
	private int maxRolls = 3;
	
	private boolean rollingEnabled = true;
	private Dice[] diceRolls = new Dice[5];
	private YahtzeeDice gameDice;

	private int numRolls;
	
	public DiceManager(){
		resetDice();
	}
	public DiceManager(YahtzeeDice gameDice){
		resetDice();
		this.gameDice = gameDice;
	}
	
	public void updateDice() {
		for(int i = 0; i < 5; i++) {
			diceRolls[i]=new Dice(gameDice.getDiceValues()[i]);
		}
		createDiceContentArray();
	}
	
	public void enableRolling(boolean value){
		rollingEnabled = value;
	}
	
	public int getNumRolls(){
		return numRolls;
	}
	
	public boolean getMoveDone(){
		return moveDone;
	}
	
	public Dice[] getDice(){
		return diceRolls;
	}
	
	public void resetDice(){
		
		initDice();
		
		for(int i = 0; i < diceRolls.length; i++){
			diceRolls[i].unhold();
		}
		
		numRolls = 0;
		moveDone = true;
		
	}
	
	public void initDice(){
		
		for(int i = 0; i < diceRolls.length; i++){
			diceRolls[i] = new Dice(0);
		}
	}
	
	public void rollDice(){
		if(moveDone){
			if(numRolls < maxRolls){
		
				moveDone = false;
				
				for(int i = 0; i < diceRolls.length; i++){
					
					Dice oldDice = diceRolls[i];
					
					if(!oldDice.isHeld()){
						int num = (int) (6*Math.random())+1;
						diceRolls[i] = new Dice(num);
					}
				}
				
				numRolls++;
				createDiceContentArray();
			}
		}
	}
	
	public void setRolls(int value){
		numRolls = value;
	}
	
	public int getMaxRolls(){
		return maxRolls;
	}

	public void createDiceContentArray(){
		for(int i = 0; i < diceContents.length; i++){
			diceContents[i] = 0;
		}
	
		for(int i = 0; i < diceRolls.length; i++){
			diceContents[diceRolls[i].getValue() - 1]++;
		}
	
		handleStraights();
	}
	
	public void handleStraights(){
		//for straights
		numConsecutive = 0;
		
		int tempStartIndex = 0;
		straightStartIndex = 0;
		straightEndIndex = 0;
		
		int tempNumConsecutive = 0;
		for(int i = 0; i < diceContents.length; i++){
			if(diceContents[i] >= 1){
				tempNumConsecutive++;
				
				if(tempNumConsecutive > numConsecutive){
					numConsecutive = tempNumConsecutive;
					straightStartIndex = tempStartIndex;
					straightEndIndex = i;
				}
			}else{
				tempNumConsecutive = 0;
				tempStartIndex = i;
			}
		}
	}
	
	public int getStraightStartIndex(){//exclusive
		return straightStartIndex;
	}
	
	public int getStraightEndIndex(){//inclusive
		return straightEndIndex;
	}
	
	public boolean hasNumber(int num){
		if(diceContents[num - 1] >= 1)
			return true;

		return false;
	}
	
	public boolean isThreeKind(){
		for(int i = 0; i < diceContents.length; i++){
			if(diceContents[i] >= 3)
				return true;
		}
		
		return false;
	}

	public boolean isFourKind(){
		for(int i = 0; i < diceContents.length; i++){
			if(diceContents[i] >= 4)
				return true;
		}
		
		return false;
	}
	
	public boolean isFullHouse(){
		boolean threeKind = false;
		boolean twoKind = false;
		
		for(int i = 0; i < diceContents.length; i++){
			if(diceContents[i] == 3){
				threeKind = true;
			}else if(diceContents[i] == 2){
				twoKind = true;
			}
		}
		
		if(threeKind && twoKind){
			return true;
		}
		
		return false;
	}
	
	public boolean isSmallStraight(){
		if(numConsecutive >= 4){
			return true;
		}
		
		return false;
	}
	
	public boolean isLargeStraight(){
		if(numConsecutive >= 5){
			return true;
		}
		
		return false;
	}
	
	public boolean isYahtzee(){
		for(int i = 0; i < diceContents.length; i++){
			if(diceContents[i] == 5)
				return true;
		}
		
		return false;
	}

	public int determineScore(int scoreIndex){
		int score = 0;
		
		if(scoreIndex <= 5){// ones through sixes
			if(hasNumber(scoreIndex + 1))
				score += diceContents[scoreIndex] * (scoreIndex + 1);// adds 1 because score index is always one less than the value of the die
		}else if(scoreIndex == 6){// three of a kind
			if(isThreeKind()){
				for(int i = 0; i < diceRolls.length; i++){
					score += diceRolls[i].getValue();
				}
			}
		}else if(scoreIndex == 7){// four of a kind
			if(isFourKind()){
				for(int i = 0; i < diceRolls.length; i++){
					score += diceRolls[i].getValue();
				}
			}
		}else if(scoreIndex == 8){// chance
			for(int i = 0; i < diceRolls.length; i++){
				score += diceRolls[i].getValue();
			}
		}else if(scoreIndex == 9){//full house
			if(isFullHouse())
				score += 25;
		}else if(scoreIndex == 10){//small straight
			if(isSmallStraight())
				score += 30;
		}else if(scoreIndex == 11){//large straight
			if(isLargeStraight())
				score += 40;
		}else if(scoreIndex == 12){//yahtzee
			if(isYahtzee())
				score += 50;
		}
		
		return score;
	}
	
	public void printDice(){
		for(int i = 0; i < diceRolls.length; i++){
			System.out.print(diceRolls[i] + "  ");
		}
		
		System.out.println();
	}
}