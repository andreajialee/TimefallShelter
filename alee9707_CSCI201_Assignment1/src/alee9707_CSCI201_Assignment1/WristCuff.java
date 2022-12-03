package alee9707_CSCI201_Assignment1;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

public class WristCuff {

    //set initial values for needed members
	private ArrayList<TimefallShelter> shelters;
	private ArrayList<Integer> freqs;
	String file;
	
    public WristCuff(ArrayList<TimefallShelter> shelter, ArrayList<Integer> fre, String name) {
    	this.shelters = shelter;
    	this.freqs = fre;
    	this.file = name;
    }


    /**
     * List all available shelters within the min and max of supported Chiral frequencies
     */
    void listAllShelters() {
    	ArrayList<TimefallShelter> list = new ArrayList<TimefallShelter>();
    	// Create an array of shelters with the available chiral frequencies 
    	for(int i=0; i<freqs.size(); i++) {
    		for(int j=0; j<shelters.size(); j++) {
    			if(shelters.get(j).getChiralFrequency() == freqs.get(i)) {
    				if(!shelters.get(j).getTimeFall()) {
    					list.add(shelters.get(j));
    					break;
    				}
    			}
    		}
    	}
    	
    	// Print out these shelters
    	System.out.println(list.size() + " results\n");
    	for(int i=0; i<list.size(); i++) {
    		list.get(i).toString();
    	}
    }


    /**
     * Functions for:
     * Search for a shelter by Chiral frequency
     * Search for a shelter by name
     */
    public boolean findByChiral(int chiral) {
    	for(int i=0; i<shelters.size(); i++) {
    		if(shelters.get(i).getChiralFrequency() == chiral) {
    			System.out.println("Found!\n");
    			shelters.get(i).toString();
    			return true;
    		}
    	}
    	System.out.println("That chiral frequency does not exist.\n");
    	return false;
    	
    }
    public boolean findByName(String name) {
    	name = name.toLowerCase();				// Case insensitive
    	for(int i=0; i<shelters.size(); i++) {
    		String shelterName = shelters.get(i).getName().toLowerCase();
    		if(name.compareTo(shelterName) == 0) {
    			System.out.println("Found!\n");
    			shelters.get(i).toString();
    			return true;
    		}
    	}
    	System.out.println("No such shelter...\n");
    	return false;
    }
    
    /**
     * Find an available shelter with the lowest supported Chiral frequency
     */
    public void findShelter() {
    	System.out.println("=== Commencing timefall shelter search ===");
    	for(int i=0; i<freqs.size(); i++) {
    		int chiralNum = freqs.get(i);
    		for(int j=0; j<shelters.size(); j++) {
    			if(shelters.get(j).getChiralFrequency() == chiralNum) {
    				if(!shelters.get(j).getTimeFall()) {
    					System.out.println("=== Matching timefall shelter found! ===");
        				shelters.get(j).toString();
        				System.out.println("=== Commencing Chiral jump, see you in safety. ===");
        				return;
    				}
    				else {
    					System.out.println("=== Chiral frequency " + chiralNum + " unstable, Chiral jump unavailable. ===\n"
    			    			+ "=== Removing target shelter from the list of shelters and saving updated list to disk. ===\n");
    			    	shelters.remove(j);
    			    	j--;
    			    	try {
    			    		save(file);
    			    	}
    			    	catch(IOException err){
    			    		err.printStackTrace();
    			    	}
    				}
    			}
    			
    		}
    	}
    	try {
    		save(file);
    	}
    	catch(IOException err) {
    		err.printStackTrace();
    	}
    	System.out.println("=== No shelter available. You are DOOMED. ===");
    }



    /**
     * Sort shelters by Chiral frequency
     */
    public void sortShelters() throws IOException{
    	Collections.sort(shelters);
    	Collections.sort(freqs);
    	save(file);		// Save to file IO
    }

    /**
     * Saves the updated list of shelters to disk
     */
    public void save(String file) throws IOException {
    	try {
    		Gson gson = new GsonBuilder().setPrettyPrinting().create();
    		Writer w = Files.newBufferedWriter(Paths.get(file));
    		gson.toJson(shelters, w);
        	w.close();
    	}
    	catch(IOException err) {
    		System.out.println("Can't find file");
    	}
    }
    	
}
