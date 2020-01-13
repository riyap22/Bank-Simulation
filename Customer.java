/*
Author:Riya Patel
Description: There are 5 customers created initially, one thread each.
Each customer makes 3 visits to the bank and each customer starts off with a balance of 1000 dollars.
On each visit a customer is randomly assigned a task. The tasks are making a deposit of
random amount from 100-500 (increments of 100),making a withdrawal of a random amount
from 100-500(increments of 1000), requesting a loan of a random amount from 100-500 (increment of 100)
*/

import java.util.*;

public class Customer implements Runnable{
	private int id, visit, balance, cTask, cTaskAmount, lAmount, cEmployee;
	public Customer(int i, int v, int bal) {this.id = i; this.visit = v; this.balance = bal; this.lAmount = 0;}

	// Getter and Setter methods
	public int getid(){return id;}
	public int getBal() {return balance;}
	public int getVisit() {return visit;}
	public int getCT() {return cTask;}
	public int getCTaskAmount() {return cTaskAmount;}
	public int getLAmount() {return lAmount;}
	public int getCEmployee() {return cEmployee;}
	public void setCEmployee(int cEmployee) {this.cEmployee = cEmployee;}
	public void setBal(int balance) {this.balance = balance;}
	public void setVisit(int visit) {this.visit = visit;}
	public void setCTask(int cTask) {this.cTask = cTask;}
	public void setCTaskAmount(int cTaskAmount) {this.cTaskAmount = cTaskAmount;}
	public void setLAmount(int lAmount) {this.lAmount = lAmount;}

	// Get a random amount for the customer task
	private int get_Task_Amount() {
		Random randomNum = new Random();
		int randomAmount = (randomNum.nextInt(5) + 1) * 100;
		return randomAmount;
	}

	// Get a random task for the customer
	private int get_Task() {
		Random randomNum = new Random();
    int randomTask = randomNum.nextInt(3);
		return randomTask;
	}

	public void run() {
		// A Customer has been created
		System.out.println("Customer " + this.id + " created");
		while(this.visit != 3){
			this.cTask = get_Task(); //assign task
			this.cTaskAmount = get_Task_Amount(); //assign task amount
	    	if(this.cTask == 0 || this.cTask == 1){
				try {
					Main.waitTL.acquire(); //customer waits in line
					Main.TL.add(this); //customer added to the queue
					Main.waitTL.release();
					Main.signalTL.release(); //customer signals teller they are ready
					Main.signalCust[this.id].acquire(); //customer waits to be called by the teller.
					if(this.cTask == 0){ //customer requests tasks once called by the teller.Either deposit or withdraw
						System.out.println("Customer " + this.getid() + " " + "requests of teller " + this.cEmployee + " to make a deposit of $" + this.getCTaskAmount());
					}
					else{
						System.out.println("Customer " + this.getid() + " "	+ "requests of teller " + this.cEmployee + " to make a withdrawal of $" + this.getCTaskAmount());
					}
					Main.requestTL[this.cEmployee].release();
					Main.performTask[this.id].acquire(); //wait for the teller to finish the task and gets receipt.
					if(this.cTask == 0){
						System.out.println("Customer " + this.id + " gets receipt from teller " + this.cEmployee);
					}
					else{
						System.out.println("Customer " + this.id + " gets cash and receipt from teller " + this.cEmployee);
					}
				  }
				catch (InterruptedException e) {
					System.out.println("Sorry an error occurred.");
				}
	    	}

				//if the task is not withdraw or deposit
	    	else if(this.cTask == 2){
				try {
					Main.waitOL.acquire(); //customer waits in the line
					Main.OL.add(this); //customer is added to the queue
					Main.waitOL.release();
					Main.signalOF.release(); //customer signals loan officer they are ready
					Main.signalCust[this.id].acquire(); //customer is called by the loan officer.
					System.out.println("Customer " + this.getid() + " "	+ "requests of teller " + this.cEmployee + " to apply for a loan of $" + this.getCTaskAmount());
					Main.requestOL.release();
					Main.performTask[this.id].acquire(); //wait for the officer to perform task and get the receipt
					System.out.println("Customer " + this.id + " gets loan from loan officer " + this.cEmployee);}
				catch (InterruptedException e) {
					System.out.println("Sorry an error occurred.");
				}
	    	}

			this.visit++; //customer makes another visit
		}
		System.out.println("Customer " + this.id + " departs the bank"); //if the customer has visited the bank three times.
	}

}
