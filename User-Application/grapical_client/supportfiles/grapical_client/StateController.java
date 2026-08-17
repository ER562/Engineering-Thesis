package grapical_client;

public class Statecontroller {
	
	//object refers to sensors where as part refers to one variable
	//partnumbersent is 0 when nothing was sent, 1 where first part was sent and resets when is equal to maxparts
	public int maxobjects;
	public int objectnumbersent;
	
	public void init(int maxobjects) {
		this.maxobjects = maxobjects;
		
		this.objectnumbersent = 0;
	}
	
	public int getobjecttosend() {
		return objectnumbersent+1;
	}
	
	//returns true if all was finished, otherwise return false
	public boolean partwassent() {
		objectnumbersent++;
		if(objectnumbersent==maxobjects) {
			return true;
		}
		return false;
	}
}
