
public class Dice{
	private boolean isHeld = false;
	
	private int value;
	
	public boolean isHeld(){
		return isHeld;
	}
	
	public void hold(){
		isHeld = true;
	}
	
	public void unhold(){
		isHeld = false;
	}
	
	public int getValue(){
		return value;
	}

	public Dice(int value){
		this.value = value;
	}
}
