// Bank.java

/*
 Creates a bunch of accounts and uses threads
 to post transactions to the accounts concurrently.
*/

import java.io.*;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;

public class Bank {
	private Account[] accounts;
	public static final int ACCOUNTS = 20;	 // number of accounts
	private static final int DEF_THREADS_NUMBER = 5; /*Not manuall option*/
	private static final int DEFAULT_QUEUE_SIZE = 100;
	private static final int DEF_BALANCE = 1000;
	private final Transaction nullTrans = new Transaction(-1, 0, 0);
	private static CountDownLatch cdl ;
	private BlockingQueue<Transaction> tqueue ;
	public static Account[] accData;
	public static int runnedThreads = 0;
	
	public Bank(String file , int numWorkers) {
		accounts = new Account[ACCOUNTS];
		tqueue = new ArrayBlockingQueue<Transaction>(DEFAULT_QUEUE_SIZE);
		cdl = new CountDownLatch(numWorkers);
		for(int i = 0; i< ACCOUNTS; i++) {
			accounts[i] = new Account(this,i ,DEF_BALANCE);
		}
	}
	
	/*
	 Reads transaction data (from/to/amt) from a file for processing.
	 (provided code)
	 */
	public void readFile(String file) {
			try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			
			// Use stream tokenizer to get successive words from file
			StreamTokenizer tokenizer = new StreamTokenizer(reader);
			
			while (true) {
				int read = tokenizer.nextToken();
				if (read == StreamTokenizer.TT_EOF) break;  // detect EOF
				int from = (int)tokenizer.nval;
				
				tokenizer.nextToken();
				int to = (int)tokenizer.nval;
				
				tokenizer.nextToken();
				int amount = (int)tokenizer.nval;
				
				// Use the from/to/amount
				Transaction t = new Transaction(from, to, amount);
				tqueue.put(t);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	/*
	 Processes one file of transaction data
	 -fork off workers
	 -read file into the buffer
	 -wait for the workers to finish
	*/
	public void processFile(String file, int numWorkers) {
		for(int i = 0; i < numWorkers; i++) {
			Worker newWorker = new Worker();
			newWorker.start();
		}
		readFile(file);
		try {
			for(int i =0; i< numWorkers; i++) {
				tqueue.put(nullTrans);
			}	
			cdl.await();
		}catch(InterruptedException e) {
			e.printStackTrace();
		}
		for(int i = 0; i<ACCOUNTS; i++) {
			System.out.println(accounts[i].toString());
		}
		accData = new Account[ACCOUNTS];
		for(int i=0; i< ACCOUNTS; i++) {
			accData[i] = accounts[i];
		}
	}

	
	
	/*
	 Looks at commandline args and calls Bank processing.
	*/
	public static void main(String[] args) {
		// deal with command-lines args
		if (args.length == 0) {
			System.out.println("Args: transaction-file [num-workers [limit]]");
			return;
		}
		
		String file = args[0];
		
		int numWorkers = DEF_THREADS_NUMBER;
		if (args.length >= 2) {
			numWorkers = Integer.parseInt(args[1]);
		}
		Bank b = new Bank(file,numWorkers);
		b.processFile(file,numWorkers);
	}
	
	private class Worker extends Thread{
		public void run() {
			try {
				while(true) {
					Transaction t = tqueue.take();
					if(t.equals(nullTrans)) {
						cdl.countDown();
						break;
					}
					accounts[t.from].withdraw(t.amount);
					accounts[t.to].deposit(t.amount);	
				}
			}catch(InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}

