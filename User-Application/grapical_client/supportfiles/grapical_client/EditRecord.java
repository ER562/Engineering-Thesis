package grapical_client;

public class Edit {
	public static String code(String name, String location, String alarm, String distance, String serialnumber) {
		String message = "code:";
		boolean editname = true;
		boolean editlocation = true;
		boolean editalarm = true;
		boolean editdistance = true;
		
		if(name.length()==0) {
			message += "0";
			editname = false;
		}else {
			message += "1";
		}

		if(location.length()==0) {
			message += "0";
			editlocation = false;
		}else {
			message += "1";
		}
		
		if(alarm.length()==0) {
			message += "0";
			editalarm = false;
		}else {
			message += "1";
		}
		
		if(distance.length()==0) {
			message += "0";
			editdistance = false;
		}else {
			message += "1";
		}
		
		message = Fill.fillto(message, 14);
		
		message += Fill.fillto(serialnumber, 10);
		
		if(editname) {
			message += Fill.fillto(name, 20);
		}
		
		if(editlocation) {
			message += Fill.fillto(location, 20);
		}
		
		if(editalarm) {
			message += Fill.fillto(alarm, 10);
		}
		
		if(editdistance) {
			message += Fill.fillto(distance, 10);
		}
		
		return message;
	}
}
