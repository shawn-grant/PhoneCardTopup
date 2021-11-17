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

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class ServiceProvider{

	protected String companyId;
	protected String address;
	protected static int numCustomers = 0;

	//stores a list of customers for this Service Provider
	protected ArrayList<Customer> customers = new ArrayList<Customer>();

	//default constructor
	public ServiceProvider(){
		this.companyId = "";
		this.address = "";
	}

	//primary constructor
	public ServiceProvider(String companyId, String address){
		this.companyId = companyId;
		this.address = address;
	}

	//copy constructor
	public ServiceProvider(ServiceProvider sp){
		this.companyId = sp.companyId;
		this.address = sp.address;
	}


	/* add customer to the company file */
	public int addCustomer(Customer newCustomer) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("INVALID INPUT");
		alert.setGraphic(null);
		alert.setHeaderText(null);
		String filename = "";

		if (newCustomer.getTrn().length() != 9) {
			// show error
			alert.setContentText("TRN must be 9 Digits long");
			alert.showAndWait();
			return 0;
		}
		// ensure TRN is all numbers
		try {
			Double.parseDouble(newCustomer.getTrn());
		} catch (NumberFormatException nfe) {
			alert.setContentText("TRN must be numbers ONLY. \n eg: 102307207");
			alert.showAndWait();
			return 0;
		}
		
		if (newCustomer.getLastName().length() < 3) {
			alert.setContentText("Last Name must be atleast 3 characters long");
			alert.showAndWait();
			return 0;
		}

		if (newCustomer.getAddress().length() < 5) {
			alert.setContentText("Address must be atleast 5 characters long");
			alert.showAndWait();
			return 0;
		}

		if (!isValidPhoneNumber(newCustomer.getTelNumber()))
			return 0;

		if (this instanceof Digicel)
			filename = "Digicel_Customers.txt";
		else
			filename = "Flow_Customers.txt";

		//write newCustomer to file
		try {
			File customerFile = new File(filename);
			customerFile.createNewFile(); //create file if it doesnt exist

			// append using fIleWriter: true means append, false is overwrite
			FileWriter myWriter = new FileWriter(customerFile, true);
			myWriter.write(newCustomer.getTrn() + 
						"\t" + newCustomer.getLastName() + 
						"\t" + newCustomer.getAddress() + 
						"\t" + newCustomer.getTelNumber() +
						"\t" + newCustomer.getCreditBal() + "\n");
			myWriter.close(); //close the file

			//increase numCustomers
			numCustomers++;
			customers.add(newCustomer);
			//feedback
			alert.setAlertType(AlertType.INFORMATION);
			alert.setTitle("Saved Successfully");
			alert.setContentText("Customer added to file");
			alert.showAndWait();
			return 1;
		} 
		catch (IOException e) {
			e.printStackTrace();
			alert.setContentText("Could not add customer to file");
			alert.showAndWait();

			return 0;
		}
	}

	// gets a valid phone number from the user
	public boolean isValidPhoneNumber(String tel) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Invalid Phone Number");
		alert.setGraphic(null);
		alert.setHeaderText(null);
		String[] prefixes;

		if (this instanceof Digicel)
			prefixes = new String[]{"301", "302", "303", "304"};
		else
			prefixes = new String[] { "601", "602", "603", "604" };
		
		//validate the number
		if (tel.length() != 10) {
			alert.setContentText("Phone number must be 10 digits");
			alert.showAndWait();
			return false;
		}
		//ensure the string passed is actually numeric
		try {
			Double.parseDouble(tel);
		} catch (NumberFormatException nfe) {
			alert.setContentText("Phone number must be numbers ONLY\n eg: 8763045008");
			alert.showAndWait();
			return false;
		}

		String prefix = tel.substring(3, 6); //get the 3 digit prefix of the phone number

		//check if number has the correct prefix
		if(!Arrays.asList(prefixes).contains(prefix)){
			if(companyId.equals("DIGICEL")) {
				alert.setContentText("Digicel numbers must begin with 301, 302, 303 or 304");
			}
			else {
				alert.setContentText("Flow numbers must begin with 601, 602, 603 or 604");
			}
			alert.showAndWait();
			return false;
		}
		else{
			return true;
		}
	}

	/*gets all customers*/
	public ArrayList<Customer> getCustomerBase(){
		// display all customers
		return customers;
	}

	/*creates the credit and adds it to file*/
	public void createPhoneCredit(int amt) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("PHONE CARD CREATED");
		alert.setGraphic(null);
		alert.setHeaderText(null);
		String filename = "";
		
		if (this instanceof Digicel)
			filename = "Digicel_CardInformation.txt";
		else
			filename = "Flow_CardInformation.txt";

		//write card info to file
		try {
			//create file if it doesnt exist
			File cardFile = new File(filename);
			cardFile.createNewFile();

			// create the credit and set the amount
			Credit newCredit = new Credit();
			newCredit.setAmount(amt);

			// append using fIleWriter: true means append, false is overwrite
			FileWriter myWriter = new FileWriter(filename, true);
			myWriter.write(newCredit.getCardNumber() + "\t" + newCredit.getAmount() + "\t" + newCredit.getStatus() + "\n");
			
			//close the file
			myWriter.close();

			alert.setContentText("Card #: " + newCredit.getCardNumber() +
						"\nAmount: $" + newCredit.getAmount() +
						"\nStatus: " + newCredit.getStatus());
			alert.showAndWait();
		} 
		catch (IOException e) {
			alert.setAlertType(AlertType.ERROR);
			alert.setTitle("ERROR");
			alert.setContentText("Could Not write the credit to files");
			alert.showAndWait();
		}
	}

	/*retreive all phone credits for the service provider*/
	public ArrayList<Credit> getAllPhoneCredit() {
		ArrayList<Credit> list = new ArrayList<Credit>();
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("ERROR");
		alert.setGraphic(null);
		alert.setHeaderText(null);
		String filename = "";
		
		if (this instanceof Digicel)
			filename = "Digicel_CardInformation.txt";
		else
			filename = "Flow_CardInformation.txt";

		try{
			Scanner creditFile = new Scanner(new File(filename));
			
			// read from the file
			while(creditFile.hasNext()){
				String cardNum = creditFile.next();
				float amt = creditFile.nextFloat();
				String status = creditFile.next();

				list.add(new Credit(cardNum, amt, status));
			}

			//close the file
			creditFile.close();
		}
		catch (IOException e) {
			alert.setContentText("Could not get data from file");
			alert.showAndWait();
		}

		return list;
	}

	/*the total number of customers*/
	public int getNumCustomers(){
		return numCustomers;
	}

	//show all company info
	public void displayCompanyInfo(){
		System.out.println("-------Company Info-------");
		System.out.println("ID           : " + companyId);
		System.out.println("Address      : " + address);
	}

	public String getCompanyId(){
		return companyId;
	}

	public String getAddress(){
		return address;
	}
}