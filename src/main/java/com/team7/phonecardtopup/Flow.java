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

public class Flow extends ServiceProvider{
	private String parentName;
	
	public Flow() {
		companyId = "FLOW";
		address = "2-6 Carlton Cres, Kingston";
		parentName = "Liberty Latin America";

		getCustomersFromFile();
	}

	//populate the customer array in the parent class
	public void getCustomersFromFile(){
		String filename = "Flow_Customers.txt";

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

	// overrided method
	@Override
	public void displayCompanyInfo(){
		// call parent method
		super.displayCompanyInfo();
		// child specific info           :
		System.out.println("Parent Company: " + parentName);
		System.out.println("-----------------------------\n");

	}

	public String getParentName() {
		return parentName;
	}
}