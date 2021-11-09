/*
	Subject: Object Oriented Programming(CIT2004)
	Occurence: UE2
	Tutor: Oniel Charles
	Programmers: [
		Kiyana Gordon (1902726)
		Shawn Grant   (2002432)
		Malik Morgan  (2007793)
	]
*/
package com.team7.phonecardtopup;

import java.io.*;  // Import the File class
import java.util.*;

public class Digicel extends ServiceProvider{
	private int numBranches;
	
	public Digicel() {
		companyId = "DIGICEL";
		address = "14 Ocean Blvd, Kingston";
		numBranches = 8;

		getCustomersFromFile();
	}

	//populate the customer array in the parent class
	public void getCustomersFromFile() {
		String filename = "Digicel_Customers.txt";

		try{
			Scanner customerFile = new Scanner(new File(filename));

			// read from the file
			while(customerFile.hasNextLine()){

				String line = customerFile.nextLine();
				String[] values = line.split("\t");

				String trn = values[0];
				String lname = values[1];
				String address = values[2];
				String tel = values[3];
				float bal = Float.parseFloat(values[4]);

				Customer cust = new Customer (trn, lname, address, tel, bal);

				// add to list
				customers.add(cust);
				//increase numCustomers
				numCustomers ++;
			}

			//close the file
			customerFile.close();
		}
		catch (IOException e) {
			System.out.println("An Error Occurred While Retrieving Customer Data");
		}
	}

	//overrided method
	@Override
	public void displayCompanyInfo() {
		// call the parent class method
		super.displayCompanyInfo();
		// display child specific info
		System.out.println("# of Branches: " + numBranches);
		System.out.println("-----------------------------\n");

	}

	public int getNumBranches() {
		return numBranches;
	}
}
