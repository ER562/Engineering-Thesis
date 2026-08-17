package server;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class DataDownloader {

	public static List<Data> DownloadRain() throws MalformedURLException, IOException {
		List<Data> data = new ArrayList<>();
		
		for(int year = 2001; year<=2002;year++) {
			try(InputStream inputstream = new URL("https://danepubliczne.imgw.pl/data/dane_pomiarowo_obserwacyjne/dane_meteorologiczne/miesieczne/opad/" + year + "/" + year + "_m_o.zip").openStream();
			ZipInputStream zip = new ZipInputStream(new BufferedInputStream(inputstream));){
				ZipEntry entry = zip.getNextEntry();
				Charset encoding = Charset.forName("ISO-8859-2");
				
				try(BufferedReader reader = new BufferedReader(new InputStreamReader(zip, encoding))){
					
					Pattern pattern = Pattern.compile("\"([^\"]*)\",\\\"([^\\\"]*)\\\",\\\"([^\\\"]*)\\\",\\\"([^\\\"]*)\\\",([^\\\"]*),");
					String line;
					while((line = reader.readLine()) != null) {
						Matcher matcher = pattern.matcher(line);
						matcher.find();
						
						boolean found = false;
						int index = -1;
						for(int i = 0;i<data.size();i++) {
							if(data.get(i).name.equals(matcher.group(2))) {
								found = true;
								index = i;
								break;
							}
						}
						
						if(found == true) {
							data.get(index).save(Integer.parseInt(matcher.group(3)), Integer.parseInt(matcher.group(4)), matcher.group(5));
						}else {
							data.add(new Data(matcher.group(2), Integer.parseInt(matcher.group(3)), Integer.parseInt(matcher.group(4)), matcher.group(5)));
						}
					}
				}
			}
		}		

		return data;
			

	}
	
	
	public static List<Data> DownloadLevel() throws MalformedURLException, IOException {
		List<Data> data = new ArrayList<>();
		
		//max 2022 rest have different formating
		for(int year = 2001; year<=2002;year++) {
			try(InputStream inputstream = new URL("https://danepubliczne.imgw.pl/data/dane_pomiarowo_obserwacyjne/dane_hydrologiczne/miesieczne/" + year + "/mies_" + year + ".zip").openStream();
			ZipInputStream zip = new ZipInputStream(new BufferedInputStream(inputstream));){
				ZipEntry entry = zip.getNextEntry();
				Charset encoding = Charset.forName("ISO-8859-2");
				
				try(BufferedReader reader = new BufferedReader(new InputStreamReader(zip, encoding))){
					
					Pattern pattern = Pattern.compile("([^\"]*),\"([^\"]*)\",\"([^\"]*)\",\"([^\"]*)\",\"([^\"]*)\",\"([^\"]*)\",([^\"]*),([^\"]*),([^\\\"]*),\"([^\"]*)\"");
					String line;
					while((line = reader.readLine()) != null) {
						Matcher matcher = pattern.matcher(line);
						matcher.find();
						
						boolean found = false;
						int index = -1;
						for(int i = 0;i<data.size();i++) {
							if(data.get(i).name.equals(matcher.group(2)+" "+matcher.group(3))) {
								found = true;
								index = i;
								break;
							}
						}
						
						if(found == true) {
							data.get(index).save(Integer.parseInt(matcher.group(4)), Integer.parseInt(matcher.group(10)), matcher.group(7));
						}else {
							data.add(new Data(matcher.group(2)+" "+matcher.group(3), Integer.parseInt(matcher.group(4)), Integer.parseInt(matcher.group(10)), matcher.group(7)));
						}
					}
				}
			}
		}
		for(int i =0 ; i < data.size(); i++) {
			data.get(i).data.sort(Comparator.comparing(d -> d.date ));
		}	

		return data;
			

	}

}
