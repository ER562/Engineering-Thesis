package server;

import java.util.UUID;

public class Code {
	public static String generate() {
		String code;
		while(true) {
			code = UUID.randomUUID().toString().substring(0,4);
			if(code.equals("0000") != true) {
				return code;
			}
		}
	}
}
