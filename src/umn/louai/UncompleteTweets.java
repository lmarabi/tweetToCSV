package umn.louai;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 
 * @author louai
 *
 */

public class UncompleteTweets {
	public static String outputDir;

	public static void main(String[] args) throws IOException {
		outputDir = args[2];
		outputDir += "_missing_Days.txt";
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(outputDir, false), "UTF-8"));
		double currenttime = System.currentTimeMillis();
		File[] outputFiles = getOuputFiles(args[1]);
		// for (File f: getOuputFiles(args[1]))
		// System.out.println(f.getAbsolutePath());

		for (int days = 0; days < outputFiles.length; days++) {
			File file = outputFiles[days];
			if (file.isFile() || file.getName().equals(".DS_Store")
					|| file.getName().equals("some.txt")) {
				// Do nothing
			} else {
				List<File> innerFiles = listf(file.getAbsolutePath());
				if (!completeDay(innerFiles)) {
					System.out.println(file.getName() + " Missing data");
					writer.write(file.getName()+"\n");
				}

			}
		}
		writer.close();
		double endtime = System.currentTimeMillis();
		System.out.println("*************************\n"
				+ "Processing time in seconds = "
				+ ((endtime - currenttime) * 0.001));

	}

	public static boolean completeDay(List<File> files) {
		int[] time = new int[files.size()];
		int i = 0;
		for (File Filetime : files) {
			String s = Filetime.getName().toString().substring(11, 13);
			time[i++] = Integer.valueOf(s);
			// System.out.println(Filetime.getName()+ "***"+ s);
		}

		Arrays.sort(time);
		int prevoius = 0;
		for (i = 0; i < files.size(); i++) {
			if (time[i] == prevoius) {

			} else {

				if ((time[i]) == (prevoius + 1)) {
					prevoius = time[i];
				} else {
					return false;
				}
			}
		}

		return true;
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

	public static File[] getOuputFiles(String directoryName) {
		File[] temp = new File(directoryName).listFiles();
		Arrays.sort(temp);
		return temp;

	}

}
