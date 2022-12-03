package alee9707_CSCI201_Assignment1;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

public class Main {
	private static WristCuff cuff;
	
	/**
	 * Checks if a GUID line is valid
	 * Referenced:
	 * https://www.geeksforgeeks.org/how-to-validate-guid-globally-unique-identifier-using-regular-expression/
	 */
	private boolean validGuid(String guid) {
		guid = guid.replaceAll(" ", "");
		
		String regex
        = "^[{]?[0-9a-fA-F]{8}"
          + "-([0-9a-fA-F]{4}-)"
          + "{3}[0-9a-fA-F]{12}[}]?$";
		
		// Compile the ReGex
		Pattern p = Pattern.compile(regex);
		
		// If the GUID string is empty, return false
		if (guid == null) {
			return false;
		}

		// Find match between given string and regular expression
		// uSing Pattern.matcher()
		Matcher m = p.matcher(guid);

		// Return if the string matches the ReGex
		return m.matches();
	}
	
	/**
	 * Checks if a phone number is valid
	 * Referenced:
	 * https://www.baeldung.com/java-regex-validate-phone-numbers 
	 */
	private boolean validPhone(String phone) {
		phone = phone.replace(" ", "");	// Remove Whitespace
		Pattern p = Pattern.compile("^(\\+\\d{1,3}( )?)?((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$");
		Matcher m = p.matcher(phone);
		return m.matches();
	}
	
	/**
	 * Uses GSON to read the file inputed by the user
	 * Returns ArrayList<TimefallShelter> from the file
	 */
	private ArrayList<TimefallShelter> readFile(String file) throws IOException, InputMismatchException, JsonParseException{
		File f =  new File(file);
		Scanner s = new Scanner(f);
		String input = "";
		while(s.hasNext()) {
			input += s.nextLine();
		}
		Gson gson = new Gson();
		ArrayList<TimefallShelter> shelter = gson.fromJson(input, new TypeToken<ArrayList<TimefallShelter>>() {}.getType());
		
		// Check JSON Data
		for(int i=0; i<shelter.size(); i++) {
			
			if(shelter.get(i).getName() == null || shelter.get(i).getGuid() == null || shelter.get(i).getPhone() == null || shelter.get(i).getAddress() == null) {
				throw new InputMismatchException("Missing parameters in file\n");
			}
			if(!validPhone(shelter.get(i).getPhone())) {
				throw new InputMismatchException("Invalid phone format\n");
			}
			if(!validGuid(shelter.get(i).getGuid())) {
				throw new InputMismatchException("Invalid GUID format\n");
			}	
		}
		return shelter;
	}

	/**
	 * Gets the supported chiral frequencies from the user
	 * Returns ArrayList<Integer> of supported frequencies 
	 */
	private ArrayList<Integer> setSupportedFrequencies(String freq) {
		
		if(freq == null) {					// Empty String, return NULL
			return null;
		}
		
		ArrayList<Integer> freqs = new ArrayList<Integer>();
		
		freq = freq.trim();					// Remove whitespace
		freq = freq.replace(",", " ");		// Remove commas & non integer values
		freq = freq.replaceAll("[^\\d]", " ");
		Scanner scan = new Scanner(freq);	// Create scanner to read in integers
	
		while(scan.hasNextInt()) {			// Read in numbers from line
			int num = scan.nextInt();
			if(!freqs.contains(num)) {		// Avoid duplicates & add chiralFreqs to array
				freqs.add(num);
			}
		}
		scan.close();
		return freqs;
	}

	/**
	 * Prints the option menu
	 */
	private void displayOptions() {
		System.out.println(
				"1) List all available shelters within the min and max of supported Chiral frequencies\n"
				+ "2) Search for a shelter by Chiral frequency\n"
				+ "3) Search for a shelter by name\n"
				+ "4) Sort shelters by Chiral frequency\n"
				+ "5) Jump to a shelter with the lowest supported Chiral frequency"
		);
	}


	public static void main(String[] args) {
		Main solution = new Main();
		System.out.println("Welcome to Bridges Link.\n");
		Scanner sc = new Scanner(System.in);
		
		boolean accepted = true;
		String file = null;
		ArrayList<TimefallShelter> shelters = new ArrayList<TimefallShelter>();
		
		// Check the provided json file
		while(accepted) {
			System.out.print("Please provide timefall shelter data source: ");
			while(true) {
				file = sc.nextLine();
				file = file.trim();
				System.out.println();
				if(file != null) {
					break;
				}
			}
			
			try {
				shelters = solution.readFile(file);
				System.out.println("=== Data accepted ===\n");
				break;
			}
			catch(InputMismatchException err){
				System.out.println(err.getMessage());
			}
			catch(IOException err) {
				System.out.println("The file " + file + " could not be found.\n");
			}
			catch(JsonParseException err) {
				System.out.println("The file " + file + " has mising parameters, cannot be parsed, or has incorrect formatting.\n");
			}
		}
		
		// Check the provided chiral frequencies
		accepted = true;
		String inputFreq;
		ArrayList<Integer> freqs = new ArrayList<Integer>();
		while(accepted) {
			System.out.print("Please provide supported Chiral frequencies: ");
			inputFreq = sc.nextLine();
			System.out.println();
			freqs = solution.setSupportedFrequencies(inputFreq);
			if(freqs != null) {
				accepted = false;
			}
			else {
				System.out.println("Invalid Input\n");
				sc.nextLine();
			}
		}
		
		cuff = new WristCuff(shelters, freqs, file);
		// Prompt user to pick an option
		accepted = true;
		int option = 0;
		while(accepted) {
			solution.displayOptions();
			boolean validOption = true;
			while(validOption) {
				System.out.print("Choose an option: ");
				try {
					option = sc.nextInt();
					System.out.println();
					if(option <= 5 && option >= 1) {
						sc.nextLine();
						break;
					}
					else {
						System.out.println("Invalid number, enter a number between 1-5\n");
					}
				}
				catch(InputMismatchException err) {
					System.out.println("Invalid input, enter a number between 1-5\n");
					sc.nextLine();
				}
			}
			
			if(option == 1) {
				cuff.listAllShelters();
			}
			else if(option == 2) {
				boolean prompt = true;
				while(prompt) {
					System.out.print("What chiral frequency are you looking for? ");
					int chiral = 0;
					while(true) {	// Check if integer
						try {
							chiral = sc.nextInt();
							System.out.println();
							break;
						}
						catch(InputMismatchException err) {
							System.out.println("Enter an integer\n");
							sc.nextLine();
						}
					}
					prompt = !cuff.findByChiral(chiral);
				}
			}
			else if(option == 3) {
				boolean prompt = true;
				while(prompt) {
					System.out.print("What shelter's name are you looking for? ");
					String shelterName = "";
					while(true) {	// Check if String
						try {
							shelterName = sc.nextLine();
							System.out.println();
							break;
						}
						catch(InputMismatchException err) {
							System.out.println("Enter an string\n");
							sc.nextLine();
						}
					}
					prompt = !cuff.findByName(shelterName);
				}
			}
			else if(option == 4) {
				try {
					cuff.sortShelters();
					System.out.println("Shelters successful sorted by Chiral Frequency.\n");
				}
				catch(IOException err) {
					err.printStackTrace();
				}
				
			}
			else if(option == 5) {
				cuff.findShelter();
				break;
			}
		}
	}
}
