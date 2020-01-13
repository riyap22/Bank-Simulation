/*
Author:Riya Patel
Description: This file contains the code to process the deposit/withdraw request by the bank teller.
The bank teller waits for the customer to be ready and when the customer is ready and the bank teller is ready,
the customer's request is processed and the task is completed. The task is either deposit or withdraw
*/
public class Bank_Teller implements Runnable {

	//if the request is deposit, get the customer number and update the balance and print out the action performed.
 	private void Deposit(Customer c) throws InterruptedException {
 		int customerNum = c.getid(); //get the id
 		int balance = c.getBal(); //current balance
 		int newBalance = balance + c.getCTaskAmount();
 		c.setBal(newBalance); //update the balance
 		System.out.println("Teller " + this.tellerid + " processes "	+ "deposit of $" + c.getCTaskAmount() + " for customer " + customerNum);
 		Main.performTask[customerNum].release(); //signal the customer that the task is done
 	}

	//if the request is withdraw, get the customer number and upfate the balance and print out the action performed.
		private void Withdraw(Customer c) throws InterruptedException {
			// Process customer withdrawal
			int customerNum = c.getid();  //get the id
			int balance = c.getBal(); //current balance
			int newBalance = balance - c.getCTaskAmount();
			c.setBal(newBalance); //update the balance
			System.out.println("Teller " + this.tellerid + " processes "
					+ "withdrawal of $" + c.getCTaskAmount() + " for customer " + customerNum);
			Main.performTask[customerNum].release(); // Signal this customer that task is done
		}

//get the customer from the line, process the request of either deposit or withdraw and perform the assign task.
	private int tellerid;
	public Bank_Teller(int i) {this.tellerid = i;}
	public int getTellerid() {return tellerid;} //getter method
	public void setTellerid(int tellerid) {this.tellerid = tellerid;} //setter method
	public void run() {
		System.out.println("Teller " + this.tellerid + " created");
		while(true){
			try {
				Main.signalTL.acquire(); //wait for customer to be ready
				Main.waitTL.acquire(); //if the customer is ready pull the customer from the line
				Customer current = Main.TL.poll();
				System.out.println("Teller " + tellerid + " begins serving customer " + current.getid());
				Main.waitTL.release();
				current.setCEmployee(this.tellerid);
				Main.signalCust[current.getid()].release();
				Main.requestTL[this.tellerid].acquire(); //get the request from the customer
				if(current.getCT() == 0){Deposit(current);} //if the task is deposit
				else{	Withdraw(current);} //withdraw
			} catch (InterruptedException e) {
				System.out.println("Sorry an error occurred");
			}
		}
	}
}
