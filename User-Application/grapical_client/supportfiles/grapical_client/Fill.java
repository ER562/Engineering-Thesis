package grapical_client;

public class Fill {
	public static String fillto(String input, int number) {
		if(input.length()>number) {
			return input.substring(0,number);
		}
		if(input.length()<number) {
			return input + " ".repeat(number-input.length());
		}
		return input;
	}
	
	public static String preparetosend(String input) {
		if(input.length()>120) {
			return input.substring(0,120);
		}
		if(input.length()<120) {
			return input.trim();
		}
		return input;
	}
}
