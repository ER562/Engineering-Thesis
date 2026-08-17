package server;

public class Statecontroller {
	
	//object refers to sensors where as part refers to one variable
	//partnumbersent is 0 when nothing was sent, 1 where first part was sent and resets when is equal to maxparts
	public int maxobjects;
	public int objectnumbersent;
	
	public boolean isused;
	
	public Statecontroller() {
		isused = false;
	}
	
	public boolean init(int maxobjects) {
		if(isused == true) {
			return false;
		}
		this.maxobjects = maxobjects;
		this.objectnumbersent = 0;
		this.isused = true;
		return true;
	}
	
	public int getobjecttosend() {
		return objectnumbersent;
	}
	
	//returns true if all was finished, otherwise return false
	public boolean partreceived() {
		objectnumbersent++;
		if(objectnumbersent>=maxobjects) {
			isused = false;
			return true;
		}
		return false;
	}
	
	public void unsetisused() {
		this.isused = false;
	}
}
