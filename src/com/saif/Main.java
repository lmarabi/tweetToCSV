package com.saif;

import org.json.JSONArray;
import org.json.JSONObject;

import com.sun.org.apache.bcel.internal.generic.LSTORE;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

import java.io.*;
import java.net.Inet4Address;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.zip.GZIPInputStream;

public class Main {

	public static String json2csvHashtag(String jsonLine) throws ParseException {
		
		String csvOutput = "";
		List<String> hashes = new ArrayList<String>();

		double[] coords = new double[2];
		try {
			JSONObject post = new JSONObject(jsonLine);
			String created_at = fixDate(post.getString("created_at"));
			JSONObject geo = post.getJSONObject("geo");
			JSONArray coordsJSON = (JSONArray) geo.get("coordinates");
			JSONObject entities = post.getJSONObject("entities");
			JSONArray hashtags1 = (JSONArray) entities.get("hashtags");
			coords[0] = coordsJSON.getDouble(0);
			coords[1] = coordsJSON.getDouble(1);
			JSONObject hashObj;
			for (int i = 0; i < hashtags1.length(); i++) {
				hashObj = hashtags1.getJSONObject(i);
				String hashtagName = hashObj.getString("text");
				hashes.add(hashtagName);
			}
			if (hashes.size() > 0) {
				for (String hash : hashes) {
					csvOutput = created_at + "," + coords[0] + "," + coords[1] + ","
							+ hash + "\n";
				}
			}

		} catch (org.json.JSONException e) {

		}

		return csvOutput;
	}

	public static String json2csv(String jsonLine, String shortname)
			throws ParseException {
		
		String csvOutput = "";
		double[] coords = new double[2];

		try {
			JSONObject post = new JSONObject(jsonLine);
			JSONObject user = post.getJSONObject("user");
			String created_at = fixDate(post.getString("created_at"));
			String tweetID = post.getString("id_str");
			String user_id = user.getString("id_str");
			String user_screen_name = user.getString("screen_name");
			String tweetText = post.getString("text");
			String language = user.getString("lang").replace(",", ".");
			String os = getOperatingSystem(post.getString("source"));
			String fixedTweetText = tweetText.replace('\n', ' ');
			String anotherFixedTweetText = fixedTweetText.replace(",", ".");
			String yetAnotherFixedTweeet = anotherFixedTweetText.replaceAll(
					"[\\t\\n\\r]", " ");
			String followers_count = Integer.toString(user
					.getInt("followers_count"));
			JSONObject geo = post.getJSONObject("geo");
			JSONArray coordsJSON = (JSONArray) geo.get("coordinates");
			coords[0] = coordsJSON.getDouble(0);
			coords[1] = coordsJSON.getDouble(1);
			
			
			csvOutput = created_at + "," + tweetID + "," + user_id + ","
					+ user_screen_name + "," + yetAnotherFixedTweeet.trim()
					+ "," + followers_count + "," + language + "," + os + ","
					+ coords[0] + "," + coords[1] 
					+ "\n";

		} catch (org.json.JSONException e) {
			csvOutput = "";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return csvOutput;
	}
	
	public static String getOperatingSystem(String os){
		String result = "";
		if(os.contains("BlackBerry")){
			result = "BlackBerry";
		}else if(os.contains("Android")){
			result = "Android";
		}else if(os.contains("Mac")){
			result = "Mac";
		}else if(os.contains("iPhone")){
			result = "iPhone";
		}else if(os.contains("Nokia")){
			result = "Nokia";
		}else if(os.contains("Instagram")){
			result = "Instagram";
		}else if(os.contains("iOS")){
			result = "iOS";
		}else if(os.contains("FourSquare")){
			result = "FourSquare";
		}else if(os.contains("Windows")){
			result = "Windows";
		}else if(os.contains("iPad")){
			result = "iPad";
		}else{
			result = "Other";
		}
		return result;
	}

	public static Date getTwitterDate(String date) throws ParseException {

		final String TWITTER = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
		SimpleDateFormat sf = new SimpleDateFormat(TWITTER);
		sf.setLenient(true);

		return sf.parse(date);
	}

	public static String fixDate(String date) throws ParseException {
		Date dates = getTwitterDate(date);
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
		return sdf1.format(dates);
	}

	public static File[] getOuputFiles(String directoryName) {
		 File[] temp = new File(directoryName).listFiles();
		 Arrays.sort(temp);
		 return temp;
		 
	}
	
	

	public static List<File> listf(String directoryName) {
		File directory = new File(directoryName);

		List<File> resultList = new ArrayList<File>();

		// get all the files from a directory
		File[] fList = directory.listFiles();
		resultList.addAll(Arrays.asList(fList));
		for (File file : fList) {
			if (file.isFile()) {

			} else if (file.isDirectory()) {
				resultList.addAll(listf(file.getAbsolutePath()));
			}
		}
		// System.out.println(fList);
		return resultList;
	}

	 
	public static String outputDir;

	public static void main(String[] args) throws FileNotFoundException,
			ParseException, Exception {
//		args = new String[3];
//		args[0] = "tweets";
//		args[1] = "/Users/louai/microblogsDataset/output/jsontweets/";
//		args[2] = "/Users/louai/microblogsDataset/output/tweets/";
		boolean flag = false;
		double currenttime = System.currentTimeMillis();
		if (args.length == 3) {

			
			File[] outputFiles = getOuputFiles(args[1]);
//			 for (File f: getOuputFiles(args[1]))
//						 System.out.println(f.getAbsolutePath());
			outputDir = args[2];
			for (int days = 0; days < outputFiles.length; days++) {
				File file = outputFiles[days];
				if (file.isFile() || file.getName().equals(".DS_Store")
						|| file.getName().equals("some.txt")) {
					// Do nothing
				} else {
					if (args[0].equals("tweets")) {
						flag = false;
					} else if (args[0].equals("hashtag")) {
						flag = true;
					} else {
						// nothing
					}
					List<File> innerFiles = listf(file.getAbsolutePath());
					for (File inn : innerFiles) {
						writeCSVFile(inn.getAbsolutePath(), inn.getName(),
								flag);
					}
					
				}
			}
			closeOutputStreamer();
			double endtime = System.currentTimeMillis();
			System.out.println("*************************\n" +
					"Processing time in seconds = "+((endtime-currenttime)*0.001));

		} else {
			System.out.println("Uasge : Option[tweets , hashtag] <Data Folder> <output Folder> "
							+ "\n Example: java -jar ConvertToCSV.jar tweets /data/tweets /ouptputFolder \n"
							+ "---------------------------------------------------------------------\n"
							+ "Program can process uncompressed and compressed files\n" +
							"This program will produce UTF-8 csv files");
		}

	}

	private static void closeOutputStreamer() throws IOException {
		for(BufferedWriter stream : outputWriter.values()){
			stream.flush();
			stream.close();
		}
		outputWriter.clear();
	}
	
	private static BufferedWriter printWriter;
	private static HashMap<String, BufferedWriter> outputWriter = new HashMap<String, BufferedWriter>();

	private static void writeCSVFile(String fileName, String shortName,
			boolean isHashtag)
			throws IOException {
		System.out.println(fileName);
		String LastLine = "";
		
		BufferedReader br = null;
		
		try {
			if (!fileName.contains(".gzip")) {
				br = new BufferedReader(new FileReader(fileName));
			} else {
				// This is extecuted if the dataset is compressed using tar.gz
				br = new BufferedReader(new InputStreamReader(
						new GZIPInputStream(new FileInputStream(fileName))));
			}

			String fileNameFixed = shortName.substring(0, 10);

			String line;

			while ((line = br.readLine()) != null) {
				if (line.equals("")) {

				} else {
					if (!isHashtag)
						LastLine = json2csv(line, fileNameFixed);
					else
						LastLine = json2csvHashtag(line);
					if (!LastLine.equals("")) {
						String day = LastLine.substring(0, 10);
						if (!outputWriter.containsKey(day)) {
							String outputFile = outputDir + day;
							printWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile, true),"UTF-8"));
							outputWriter.put(day, printWriter);
						}
						printWriter = outputWriter.get(day);
						printWriter.append(LastLine);
					}

				}
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	
}
