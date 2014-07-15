package com.finefit.base;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

//import au.com.bytecode.opencsv.CSVReader;

	public class FileUtils {

		/**
		 * Read HTML file, concatenate all lines into one string
		 * @param fileName
		 * @return String
		 */
		public static String readFile(String fileName) {
			String allFile = "";
			try {
				File input = new File(fileName);

				String line = "";
				
				BufferedReader buf = null;

				buf = new BufferedReader(new FileReader(input));
				line = buf.readLine();
				while (line != null) {
					allFile = allFile + line;
					line = buf.readLine();
				}

				buf.close();
			} catch (FileNotFoundException ex) {
				System.err.println("Unable to open file '" + fileName + "'");
			} catch (IOException ex) {
				System.err.println("Error reading file '" + fileName + "'");
			}
			System.out.println("");
			return allFile;
		}

		/**
		 * Write to txt file, if append is false then override the data and write to beginning, 
		 * if true then write to the end of the file
		 * @param fileName
		 * @param outPut
		 * @param append
		 */
		public static void writeToFile(String fileName, String outPut, boolean append) {
			try {
				FileWriter outFile = new FileWriter(fileName, append);
				PrintWriter out = new PrintWriter(outFile);

				// Write text to file
				out.println(outPut);

				out.close();
			} catch (FileNotFoundException ex) {
				System.err.println("Unable to open file '" + fileName + "'");
			} catch (IOException ex) {
				System.err.println("Error reading file '" + fileName + "'");
			}
		}

		
		/**
		 * Write List of strings to txt file, if append is false then override the data and write to beginning,  
		 * if true then write to the end of the file
		 * @param fileName
		 * @param outPut
		 * @param append
		 */
		public static void writeToFile(String fileName, List<String> listOfStrings, boolean append) {
			try {
				FileWriter outFile = new FileWriter(fileName, append);
				PrintWriter out = new PrintWriter(outFile);

				for(String str: listOfStrings){
					out.println(str);
				}

				out.close();
			} catch (FileNotFoundException ex) {
				System.err.println("Unable to open file '" + fileName + "'");
			} catch (IOException ex) {
				System.err.println("Error reading file '" + fileName + "'");
			}
		}
}