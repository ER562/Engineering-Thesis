package server;

import java.util.ArrayList;
import java.util.List;

public class Data {
	public String name;
	public List<DataValues> data = new ArrayList<>();
	
	public void save(int year, int month, String measurement) {
		data.add(new DataValues(year, month, measurement));
	}
	
	public Data(String name, int year, int month, String measurement) {
		this.name = name;
		data.add(new DataValues(year, month, measurement));
	}
	
	public Data(String name) {
		this.name = name;
	}
}
