package alee9707_CSCI201_Assignment1;

import com.google.gson.annotations.SerializedName;
import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.Comparator;

import com.google.gson.*;

public class TimefallShelter implements Comparable<TimefallShelter> {
	/**
	 * Here: all the needed class members and their getters and setters
	 */
	@SerializedName("chiralFrequency")
	private int chiralFrequency;
	@SerializedName("timefall")
	private boolean timefall;
	@SerializedName("guid")
	private String guid;
	@SerializedName("name")
	private String name;
	@SerializedName("phone")
	private String phone;
	@SerializedName("address")
	private String address;
	
	public TimefallShelter(int chiralFreq_, boolean time_, String guid_, String phone_, String name_, String address_) {
		chiralFrequency = chiralFreq_;
		timefall = time_;
		guid = guid_;
		phone = phone_;
		name = name_;
		address = address_;
	}
	
	public int getChiralFrequency() {
		return chiralFrequency;
	}
	
	public void setChiralFrequency(int chiral) {
		this.chiralFrequency = chiral;
	}
	
	public boolean getTimeFall() {
		return timefall;
	}
	
	public void setTimeFall(boolean time) {
		this.timefall = time;
	}
	
	public String getGuid() {
		return guid;
	}
	
	public void setGuid(String guid_) {
		this.guid = guid_;
	}
	
	public String getAddress() {
		return address;
	}
	
	public void setAddress(String address_) {
		this.address = address_;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name_) {
		this.name = name_;
	}
	
	public String getPhone() {
		return phone;
	}
	
	public void setPhone(String phone_) {
		this.phone = phone_;
	}
	
	/**
	 * overriding comparator for sorting
	 */
	@Override
	public int compareTo(TimefallShelter compShelter) {
		/* For Ascending order*/
		if(this.getChiralFrequency() > compShelter.getChiralFrequency()) {
			return 1;
		}
		else {
			return -1;
		}
	}

	/**
	 * String representation of a shelter
	 * Prints shelter with extra line at the end
	 */
	@Override
	public String toString() {
		String ans = "";
		ans += "Shelter Information:\n";
		ans += "Chiral Frequency: " + this.chiralFrequency + "\n";
		if(this.timefall) {
			ans += "Timefall: Current\n";
		}
		else {
			ans += "Timefall: None\n";
		}
		ans += "GUID: " + this.guid + "\n";
		ans += "Name: " + this.name + "\n";
		ans += "Phone: " + this.phone + "\n";
		ans += "Address: " + this.address + "\n";
		System.out.println(ans);
		return ans;
	}
}
