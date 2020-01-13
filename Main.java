/*
Author: Riya Patel
Description: This file contains the code that creates threads for the customer, Bank_Teller, and the Loan_Officer.
						 It also creates semaphores needed shown in the design pdf included in the folder.
*/
import java.util.*;
import java.util.concurrent.*;

public class Main {
	public static void main(String[] args) {
		System.out.println("Thread activity" + "\n");
		//customer: 5 thread initially, one thread each
		Thread[] customers = new Thread[CUSTOMERS];
		Customer[] customer_bal = new Customer[CUSTOMERS];
		for(int i = 0; i < CUSTOMERS; i++){
			Customer newCustomer = new Customer(i,0,1000); //customer starts off with 1000 dollars
			customer_bal[i] = newCustomer;
			customers[i] = new Thread(newCustomer);
			customers[i].start();
		}
		//BankTeller: two created initially, one thread each
		Thread[] BankTeller = new Thread[Tellers];
		for(int i=0; i < Tellers; i++)
		{BankTeller[i] = new Thread(new Bank_Teller(i)); BankTeller[i].start();}
		// Create 1 Loan Officer w/ 1 thread
		Thread loanOff = new Thread(new Loan_Officer(0));
		loanOff.start();
	  //join all customer threads and catch the error if occurred
		for(int i=0; i < CUSTOMERS; i++){
			try
			{customers[i].join();} //join all the threads together
			catch (InterruptedException e)
			{System.out.println("There was an error! Could not join the threads together.");}
			System.out.println("Customer " + i + " is joined by main");
		}
		//prints the summary report and ends the simulation.
		System.out.println("\n\t\tBank Simulation Summary");
		System.out.println("\tEnding Balance\tLoan Amount");
		int EndingBalanceTotal = 0; int LAmountTotal = 0;
		for(int i=0; i< CUSTOMERS; i++)
		{
			EndingBalanceTotal += customer_bal[i].getBal(); //the total of the ending balance
			LAmountTotal += customer_bal[i].getLAmount(); //the total of the loan amount
			System.out.println("Customer " + i + "\t" + customer_bal[i].getBal() + "\t" + customer_bal[i].getLAmount());
		}
		System.out.println("\nTotal: \t" + EndingBalanceTotal + "\t" + LAmountTotal);
		System.exit(0);
	}

//Create all the Semaphores needed!
	public final static int CUSTOMERS = 5; //5 threads for customers; constant
	public final static int Tellers = 2; //2 threads for BankTeller; constant
	public static Semaphore waitTL = new Semaphore(1, true); //semaphore waitTL = wait for Teller to signal when they are ready
	// Semaphore array for each teller; signal teller that customer has made a request
	public static Semaphore[] requestTL = {new Semaphore(0, true), new Semaphore(0, true)};
	public static Semaphore signalTL = new Semaphore(0,true); //semaphore signalTL = signal teller customer is ready
	public static Semaphore signalOF = new Semaphore(0,true); //sempahore signalOF = signal officer customer is ready
	public static Semaphore waitOL = new Semaphore(1, true); //semaphore waitOL = wait for Office to signal when they are ready
	public static Semaphore requestOL = new Semaphore(0, true); //Semaphore requestOL = signal officer customer made a request
	// Semaphore array for each customer; signal customer that employee has been assigned to them
	public static Semaphore[] signalCust = {new Semaphore(0, true), new Semaphore(0, true), new Semaphore(0, true), new Semaphore(0, true), new Semaphore(0, true)};
	public static Semaphore[] performTask = {new Semaphore(0, true),new Semaphore(0, true),new Semaphore(0, true),new Semaphore(0, true),new Semaphore(0, true)};
	// Use queues to represent the line for the teller and the line for the loan officer
	public static Queue<Customer> TL= new LinkedList<Customer>();
	public static Queue<Customer> OL = new LinkedList<Customer>();
}
