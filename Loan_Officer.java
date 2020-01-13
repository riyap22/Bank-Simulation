/*
Author:Riya Patel
Description: This file contains the code to process the loan request by the loan officer.
The loan officer waits for the customer to be ready and when the customer is ready and the loan officer is ready,
the customer's request is processed and the task is completed. The task is loan.
*/
public class Loan_Officer implements Runnable {

	private int officerid;
	public Loan_Officer(int i) {this.officerid = i;}

//process the customer's loan request and update the balance
	private void LoanProcess(Customer c) throws InterruptedException {
		// Process customer loan
		// Set customer balance
		int customerNum = c.getid(); //get the id
		int balance = c.getBal(); //view the balance
		int newBalance = balance + c.getCTaskAmount();
		c.setBal(newBalance); //set the new balance
		int loan = c.getLAmount(); //get the loan amount
		int newLoan = loan + c.getCTaskAmount();
		c.setLAmount(newLoan); //set the loan amount
		System.out.println("Loan Officer " + this.officerid + " approves "	+ "loan of $" + c.getCTaskAmount() + " for customer " + customerNum);
		Main.performTask[customerNum].release(); //task is done and the customer is notified
	}

	public int getOfficerid() {return officerid;} //getter method
	public void setOfficerid(int officerNum) {this.officerid = officerNum;} //settermethod

	public void run() {
		System.out.println("Loan Officer " + this.officerid+ " created");
		while(true){
			try {
				Main.signalOF.acquire(); //wait for the customer to be ready
				Main.waitOL.acquire(); //get the customer from the line
				Customer current = Main.OL.poll();
				System.out.println("Loan Officer " + this.officerid + " begins serving customer " + current.getid());
				Main.waitOL.release();
				current.setCEmployee(this.officerid);
				Main.signalCust[current.getid()].release(); //call the customer to process the request
				Main.requestOL.acquire();
				LoanProcess(current); //process the customer's request.
			} catch (InterruptedException e) {
			}
		}
	}



}
