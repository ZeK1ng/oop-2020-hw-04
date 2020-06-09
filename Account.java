// Account.java

/*
 Simple, thread-safe Account class encapsulates
 a balance and a transaction count.
*/
public class Account {
	private int id;
	private int balance;
	private int transactions;
	
	// It may work out to be handy for the account to
	// have a pointer to its Bank.
	// (a suggestion, not a requirement)
	private Bank bank;  
	
	public Account(Bank bank, int id, int balance) {
		this.bank = bank;
		this.id = id;
		this.balance = balance;
		transactions = 0;
	}
	public int getBalance() {
		return this.balance;
	}
	public int getId() {
		return this.id;
	}
	public int getTransactions() {
		return this.transactions;
	}
	
	/*Make a transaction*/
	public synchronized void deposit(int amount) {
		this.balance+= amount;
		this.transactions++;
	}
	public synchronized void withdraw(int amount) {
		this.balance-= amount;
		this.transactions++;
	}
	/*Override toString*/
	@Override
	public String toString() {
		return "AccId:" + this.id + " Balance:" + this.balance + " Transaction Amount:" + this.transactions;
	}
	
}
