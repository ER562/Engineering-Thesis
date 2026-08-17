package grapical_client;

public class Sensordecode {
	public static Object[] decode(String message) {
		String name = message.substring(20,40);
		String location = message.substring(40,60);
		String serialnumber = message.substring(60,70);
		String measurement = message.substring(70,80);
		String alarm = message.substring(80,90);
		String logintime = message.substring(90,110);
		String disttobottom = message.substring(110,120);
		return new Object[] {name, location, serialnumber, measurement, alarm, logintime, disttobottom};
	}
}
