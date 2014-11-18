package umn.louai;

import java.io.FileNotFoundException;
import java.text.ParseException;

import com.sun.org.apache.xml.internal.security.utils.HelperNodeList;


/**
 * This program have the main three functionality 
 * Convert Geotagged tweets to CSV format 
 * Figure out the missing data in days 
 * @author louai
 *
 */



public class Main {

	public static void main(String[] args) throws FileNotFoundException, ParseException, Exception{
//		args = new String[3];
//		args[0] = "missing";
//		args[1] = "/media/louai/UQU-GISTIC-Twitter1/Crawled_Twitter_Data/GeotaggedSample/";
//		args[2] = "/media/louai/UQU-GISTIC-Twitter1/";
		if (args.length == 3) {
			if(args[0].equals("tweet") || args[0].equals("hashtag")){
				Convert.main(args);
			}else if(args[0].equals("missing")){
				UncompleteTweets.main(args);
			}else{
				helpMessage();
			}
		}else{
			helpMessage();
		}
	}
	
	public static void helpMessage(){
		System.out.println(
				"-------------------------- Usage ------------------------------------------\n"
				+"Uasge : Option[tweet , hashtag,missing] <Data Folder> <output Folder> \n"
				+ "-------------------------- Options ------------------------------------------\n"
				+ "tweet\tThis convert the Geotagged tweets to csv of tweets\n"
				+ "hashtag\tThis convert the Geotagged tweets to csv of hashtags\n"
				+ "missing\tThis finds the Days that are missing tweets\n"
				+ "------------------------------ Example --------------------------------------\n"
				+ "Example: $java -jar ConvertToCSV.jar tweet /data/tweets /ouptputFolder \n"
				+ "------------------------------- Info --------------------------------------\n"
				+ "Program can process uncompressed and compressed files\n" +
				"This program will produce UTF-8 csv files");
	}

	
}
