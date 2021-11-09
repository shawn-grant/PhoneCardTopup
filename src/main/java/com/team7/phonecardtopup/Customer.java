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

import java.util.*;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import java.io.*; 

public class Customer{
	//Attributes
	private String trn;
	private String lastName;
	private String address;
	private String telNumber;
	private float creditBal;
	
	//default constructor
	public Customer (){
		this.trn = "000000000";
		this.lastName = "";
		this.address = "";
		this.telNumber = "0000000000";
		this.creditBal = 100; //default
	}

	//primary constructor
	public Customer (String trn, String lastName, String address, String telNumber){
		this.trn = trn;
		this.lastName = lastName;
		this.address = address;
		this.telNumber = telNumber;
		this.creditBal = 100; //default
	}

	//another constructor with creditbal
	public Customer (String trn, String lastName, String address, String telNumber, float creditBal){
		this.trn = trn;
		this.lastName = lastName;
		this.address = address;
		this.telNumber = telNumber;
		this.creditBal = creditBal; 
	}

	//copy constructor
	public Customer (Customer customer){
		this.trn = customer.trn;
		this.lastName = customer.lastName;
		this.address = customer.address;
		this.telNumber = customer.telNumber;
		this.creditBal = customer.creditBal;
	}

	/* retreive a specific customer by number*/
	public Customer getCustomerByPhone(String phoneNumber, String spId) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("ERROR");
		alert.setGraphic(null);
		alert.setHeaderText(null);
		String filename;
		Customer cust = null;

		// get the service providers file
		if (spId.equals("DIGICEL")) {
			filename = "Digicel_Customers.txt";
		} else {
			filename = "Flow_Customers.txt";
		}

		// search and return the customer,
		try {
			Scanner fileReader = new Scanner(new File(filename));

			// read from the file
			while (fileReader.hasNextLine()) {

				String line = fileReader.nextLine();
				String[] values = line.split("\t");

				String trn = values[0];
				String lname = values[1];
				String address = values[2];
				String tel = values[3];
				float bal = Float.parseFloat(values[4]);

				if (tel.equals(phoneNumber)) {
					cust = new Customer(trn, lname, address, tel, bal);
					break;
				}
			}

			fileReader.close();
		} catch (IOException e) {
			alert.setContentText("An Error Occurred While Retrieving Customer Data");
			alert.showAndWait();
		}

		// return empty customer if none found
		return cust;
	}
	
	//ADD CREDIT TO A CUSTOMERS BALANCE
	//RETURNS 1 IF IT WAS SUCCESSFUL
	public int addCredit(String credCode) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("ERROR");
		alert.setGraphic(null);
		alert.setHeaderText(null);

		String phoneNumber="", cardNumber="", testCode;
		String[] digicelPrefixes = new String[]{"301", "302", "303", "304"};
		String[] flowPrefixes = new String[]{"601", "602", "603", "604"};

		//get the phone and card number
		// asterisk is a special character in split() and needs to be escaped to avoid a PatternSyntaxException
		if(credCode.split("\\*").length == 4){ //4 segments should be returned if the format is valid
			cardNumber = credCode.split("\\*")[2];
			phoneNumber = credCode.split("\\*")[3].replace("#", ""); //remove the #
		}

		testCode = "*121*" + cardNumber + "*" + phoneNumber + "#";

		if(testCode.equals(credCode)){ // valid format
			// determine the service provider
			String prefix = phoneNumber.substring(3, 6); //get the 3 digit prefix of the phone number

			if(Arrays.asList(digicelPrefixes).contains(prefix)){ //prefix belongs to digicel
				Customer cust = new Customer().getCustomerByPhone(phoneNumber, "DIGICEL");
				Credit credit;

				if(cust == null) //customer doesnt exist
				{
					alert.setContentText("CUSTOMER DOESN'T EXIST ON FILE");
					alert.showAndWait();
				}
				else{ //customer is found
					
					// check if the credit number is valid
					credit = new Credit().findCredit(cardNumber, "Digicel_CardInformation.txt");

					if(credit == null){ // card was not found
						alert.setContentText("CARD NUMBER INVALID OR ALREADY USED");
						alert.showAndWait();
					}
					else{
						// increase the customers balance
						updateCustomerBalance(cust, credit.getAmount(), "Digicel_Customers.txt");
						// change card status to used
						new Credit().useCard(credit, "Digicel_CardInformation.txt");

						alert.setTitle("CREDIT ADDED");
						alert.setContentText("SUCCESSFULY ADDED CREDIT");
						alert.showAndWait();
						return 1; //success
					}
				}
			}
			else if(Arrays.asList(flowPrefixes).contains(prefix)){ //prefix belongs to flow
				Customer cust = new Customer().getCustomerByPhone(phoneNumber, "FLOW");
				Credit credit;

				if(cust == null) //customer doesnt exist
				{
					alert.setContentText("CUSTOMER DOESN'T EXIST ON FILE");
					alert.showAndWait();
				}
				else{ //customer is found
					
					// check if the credit number is valid
					credit = new Credit().findCredit(cardNumber, "Flow_CardInformation.txt");

					if(credit == null){ // card was not found
						alert.setContentText("CARD NUMBER INVALID OR ALREADY USED");
						alert.showAndWait();
					}
					else{
						// increase the customers balance
						updateCustomerBalance(cust, credit.getAmount(), "Flow_Customers.txt");
						// change card status to used
						new Credit().useCard(credit, "Flow_CardInformation.txt");

						alert.setTitle("CREDIT ADDED");
						alert.setContentText("SUCCESSFULY ADDED CREDIT");
						alert.showAndWait();
						return 1; //success
					}
				}
			}
			else{
				alert.setContentText("NUMBER DOES NOT BELONG TO DIGICEL OR FLOW");
				alert.showAndWait();
			}
		}
		else {
			alert.setContentText("INVALID CODE FORMAT");
			alert.showAndWait();
		}
		
		return 0;
	}

	public void updateCustomerBalance(Customer cust, float amount, String filename) {
		ArrayList<Customer> customers = new ArrayList<Customer>();

		//get customers
		try {
			Scanner customerFile = new Scanner(new File(filename));
			// read from the file
			while (customerFile.hasNextLine()) {
				String line = customerFile.nextLine();
				String[] values = line.split("\t");

				String trn = values[0];
				String lname = values[1];
				String address = values[2];
				String tel = values[3];
				float bal = Float.parseFloat(values[4]);

				//increase the balance for the pased customer
				if (tel.equals(cust.getTelNumber()))
					customers.add(new Customer(trn, lname, address, tel, bal + amount));
				else
					customers.add(new Customer(trn, lname, address, tel, bal));
			}

			//close the file
			customerFile.close();
		} catch (IOException e) {
			System.out.println("An Error Occurred While Accessing Customer Data");
		}

		String newData = "";
		// update the balance
		for (int i = 0; i < customers.size(); i++) {
			Customer c = customers.get(i);

			newData += c.getTrn() + "\t" + c.getLastName() + "\t" + c.getAddress() + "\t" + c.getTelNumber() + "\t"
					+ c.getCreditBal() + "\n";
		}

		// OVERWRITE THE FILE WITH UPDATED DETAILS
		try {
			FileWriter myWriter = new FileWriter(filename, false);
			myWriter.write(newData);
			myWriter.close(); //close the file
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public float checkCustomerBalance(String checkCode) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("ERROR");
		alert.setGraphic(null);
		alert.setHeaderText(null);

		String phoneNumber="", testCode;
		String[] digicelPrefixes = new String[]{"301", "302", "303", "304"};
		String[] flowPrefixes = new String[]{"601", "602", "603", "604"};

		//get the phone and card number
		// asterisk is a special character in split() and needs to be escaped to avoid a PatternSyntaxException
		if(checkCode.split("\\*").length > 2){
			phoneNumber = checkCode.split("\\*")[2].replace("#", ""); //remove the #
		}

		testCode = "*120*" + phoneNumber + "#";

		if(testCode.equals(checkCode)){ // valid format
			// determine the service provider
			String prefix = phoneNumber.substring(3, 6); //get the 3 digit prefix of the phone number

			if(Arrays.asList(digicelPrefixes).contains(prefix)){ //prefix belongs to digicel
				Customer cust = new Customer().getCustomerByPhone(phoneNumber, "DIGICEL");
				
				if(cust == null) //customer doesnt exist
				{
					alert.setContentText("CUSTOMER DOESN'T EXIST");
					alert.showAndWait();
				}
				else{ //customer is found
					return cust.getCreditBal();
				}
			}
			else if(Arrays.asList(flowPrefixes).contains(prefix)){ //prefix belongs to flow
				Customer cust = new Customer().getCustomerByPhone(phoneNumber, "FLOW");

				if(cust == null) //customer doesnt exist
				{
					alert.setContentText("CUSTOMER DOESN'T EXIST");
					alert.showAndWait();
				}
				else{ //customer is found
					return cust.getCreditBal();
				}
			}
			else{
				alert.setContentText("NUMBER DOES NOT BELONG TO DIGICEL OR FLOW");
				alert.showAndWait();			}
		}
		else {
			alert.setContentText("INVALID CODE FORMAT");
			alert.showAndWait();
		}
		
		return 0;
	}

	//Getters and Setters
	public String getTrn()
	{
		return trn;
	}

	public void setTrn( String trn)
	{ 
		this.trn = trn;
	}

	public String getLastName()
	{
		return lastName;
	}
	
	public void setLastName(String lastName)
	{
		this.lastName = lastName;
	}
  
	public String getAddress ()
	{
		return address;
	}
  
	public void setAddress (String address)
	{
		this.address = address;
	}

	public String getTelNumber()
	{
		return telNumber;
	}

	public void setTelNumber (String telNumber)
	{
		this.telNumber = telNumber;
	}

	public float getCreditBal ()
	{
		return creditBal;
	}
	
	public void setCreditBal (float creditBal)
	{
		this.creditBal = creditBal;
	}

	//utility method
	public void display ()
	{
		System.out.println ("\tCustomer Id(TRN) : " + getTrn());
		System.out.println ("\tLast Name        : " + getLastName());
		System.out.println ("\tAddress          : " + getAddress());
		System.out.println ("\tTelephone Number : " + getTelNumber());
		System.out.println ("\tCredit Balance   : " + getCreditBal());
	}
}