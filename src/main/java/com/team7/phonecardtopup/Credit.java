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

import java.io.*;
import java.util.*;

public class Credit{
	private String cardNumber;
	private float amount;
  	private String status;

	//default constructor
	public Credit (){
		this.cardNumber = generateCardNumber();
		this.amount = 0;
    	this.status = "Available";
	}

	//primary constructor 
	public Credit(String cardNumber, float amount, String status){

		this.cardNumber = cardNumber;
		this.amount = amount;
		this.status = status;
	}

	//generate the 13 digit card code
	public String generateCardNumber() {
		int min = 0, max = 9;
		String number = "";
		Random random = new Random();

		// runs 13 times, appending a single digit
		// between 1 and 9 each time
		for (int i = 0; i < 13; i++) {
			number += random.nextInt(max + min) + min;
		}

		return number;
	}
	
	public Credit findCredit(String cardNumber, String filename) {
		Credit credit = null;

		// find the card
		try {
			Scanner creditFile = new Scanner(new File(filename));

			// read from the file
			while (creditFile.hasNext()) {
				String cardNum = creditFile.next();
				Float amt = creditFile.nextFloat();
				String status = creditFile.next();

				if (cardNum.equals(cardNumber) && !status.equals("Used")) {
					credit = new Credit(cardNum, amt, status);
					break;
				}
			}

			//close the file
			creditFile.close();
		} catch (IOException e) {
			//System.out.println(UI.RED + "An Error Occurred" + UI.WHITE);
		}

		return credit;
	}
	
	// CHANGE THE STATUS OF A CARD TO USED IN THE FILE
	public void useCard(Credit credit, String filename){
		ArrayList<Credit> credits = new ArrayList<Credit>();

		//get credits
		try{
			Scanner creditFile = new Scanner(new File(filename));
			// read from the file
			while(creditFile.hasNext()){
				String cardNum = creditFile.next();
				Float amt = creditFile.nextFloat();
				String status = creditFile.next();

				if(cardNum.equals(credit.getCardNumber()))
					credits.add(new Credit(cardNum, amt, "Used"));
				else
					credits.add(new Credit(cardNum, amt, status));
			}

			//close the file
			creditFile.close();
		}
		catch (IOException e) {
			System.out.println("An Error Occurred While Accessing Card Data");
		}

		String newData = "";
		// update the balance
		for(int i = 0; i < credits.size(); i++){
			Credit c = credits.get(i); 

			newData += c.getCardNumber() + "\t" + c.getAmount() + "\t" + c.getStatus() + "\n";
		}

		// OVERWRITE THE FILE WITH UPDATED DETAILS
		try{
			FileWriter myWriter = new FileWriter(filename, false);
			myWriter.write(newData);
			myWriter.close(); //close the file
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	//setters and getters
	public String getCardNumber(){
		return cardNumber;
	}
	
	public void setCardNumber(String cardNumber){
		this.cardNumber = cardNumber;
	}

	public float getAmount(){
		return amount;
	}
	
	public void setAmount(float amount){
		this.amount = amount;
	}
	
	public String getStatus(){
		return status;
	}

	public void setStatus(String status){
		this.status = status;
	}

  	//utility method
	public void display ()
	{
		System.out.println("Card Number: " + getCardNumber());
		System.out.println("Amount     : $" + getAmount());
		System.out.println("Status     : " + getStatus());
	}  
  
}