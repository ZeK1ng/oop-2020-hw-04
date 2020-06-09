// Cracker.java
/*
 Generates SHA hashes of short strings in parallel.
*/

import static org.junit.Assert.*;

import java.security.*;
import java.util.concurrent.CountDownLatch;

public class Cracker {
	// Array of chars used to produce strings
	public static final char[] CHARS = "abcdefghijklmnopqrstuvwxyz0123456789.,-!".toCharArray();
	private static final int MAX_THREADS = 40;
	private static CountDownLatch ctdl;
	private static boolean passFound = false;
	public static String foundPass=null;
	/*
	 Given a byte[] array, produces a hex String,
	 such as "234a6f". with 2 chars for each byte in the array.
	 (provided code)
	*/
	
	public static String hexToString(byte[] bytes) {
		StringBuffer buff = new StringBuffer();
		for (int i=0; i<bytes.length; i++) {
			int val = bytes[i];
			val = val & 0xff;  // remove higher bits, sign
			if (val<16) buff.append('0'); // leading 0
			buff.append(Integer.toString(val, 16));
		}
		return buff.toString();
	}
	
	/*
	 Given a string of hex byte values such as "24a26f", creates
	 a byte[] array of those values, one byte value -128..127
	 for each 2 chars.
	 (provided code)
//	*/
//	public static byte[] hexToArray(String hex) {
//		byte[] result = new byte[hex.length()/2];
//		for (int i=0; i<hex.length(); i+=2) {
//			result[i/2] = (byte) Integer.parseInt(hex.substring(i, i+2), 16);
//		}
//		return result;
//	}
//	
	
	// a! 34800e15707fae815d7c90d49de44aca97e2d759
	// xyz 66b27417d37e024c46526c2f6d358a754fc552f3
	// a is 86f7e437faa5a7fce15d1ddcb9eaeaea377667b8
	// fm is adeb6f2a18fe33af368d91b09587b68e3abcb9a7
			
	public static String generateHash(String inp) {
		try {
			MessageDigest mdg = MessageDigest.getInstance("SHA");
			byte[] bt = mdg.digest(inp.getBytes());
			return hexToString(bt);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private static void startCracking(String inp,int Maxlen, int numWorkers) {
		int lenPerThread = CHARS.length / numWorkers;
		for(int i = 0; i< numWorkers ; i++) {
			int startIndex , endIndex;
			startIndex = i*lenPerThread;
			endIndex = (i == numWorkers-1) ? CHARS.length-1 : (lenPerThread*i+lenPerThread -1);
			Worker wthread = new Worker(inp,startIndex,endIndex,Maxlen);
			wthread.start();
		}
		try {
			ctdl.await();
		}catch(InterruptedException e) {
			e.printStackTrace();
		}
		if(passFound == false) {
			System.out.println("No password Found");
			foundPass = null;
		}
		System.out.println("All Done");
	}
	
	
	public static void main(String[] args) {
		passFound = false;
		foundPass = null;
		if(args.length <1) {
			System.out.println("Not enought params");
			return;
		}
		if (args.length == 1) {
			System.out.println(generateHash(args[0]));
			return;
		}
		// args: targ len [num]
		String targ = args[0];
		int len = Integer.parseInt(args[1]);
		int numWorkers = 1;
		if (args.length>2) {
			numWorkers = Integer.parseInt(args[2]);
			if(numWorkers > MAX_THREADS || numWorkers <= 0) {
				System.out.println("Invalid Third Parameter. Make sure It is more than 0 and less than 41");
				return;
			}
		}
		ctdl = new CountDownLatch(numWorkers);
		startCracking(targ,len,numWorkers);
	}
	
	private static class Worker extends Thread{
		String input;
		int startIndex;
		int endIndex;
		int MaxLen;
		boolean done = false;
		
		public Worker(String input,int startIndex,int endIndex, int MaxLen) {
			this.input=input;
			this.startIndex=startIndex;
			this.endIndex=endIndex;
			this.MaxLen=MaxLen;
		}
		
		@Override
		public void run() {
			for(int i = this.startIndex; i<= this.endIndex; i++) {
				findPass(""+CHARS[i], this.MaxLen);
				if(done) break;
			}
			ctdl.countDown();
		}
		
		private void findPass(String str, int len) {
			if(done) return;
			if(len<= 0 ) return;
			String ans = generateHash(str);
			if(ans.equals(input)) {
				done = true;
				System.out.println("Found Password: " +str);
				passFound = true;
				foundPass = str;
			}
			for(int i =0; i< CHARS.length; i++) {
				findPass(str+CHARS[i] , len-1);
			}
		}
		
		
	}
}
