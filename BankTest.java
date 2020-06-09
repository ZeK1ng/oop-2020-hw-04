import static org.junit.jupiter.api.Assertions.*;

import org.junit.AfterClass;
import org.junit.Rule;

import org.junit.jupiter.api.Test;

class BankTest {
	
	@Test
	void testBasicArgs() throws InterruptedException {
		String[] args = new String[2];
		args[0] = "small.txt";
		args[1] = "2";
		Bank.main(args);
		assertEquals(20, Bank.accData.length);

	}
	@Test
	void testBasicNoArgs() throws InterruptedException {
		String[] args = new String[1];
		args[0] = "small.txt";
		Bank.main(args);
		int fullBalance = 0;
		int fullTransitions = 0 ; 
		for(int i=0; i< Bank.accData.length; i++) {
			fullBalance+=Bank.accData[i].getBalance();
			fullTransitions+=Bank.accData[i].getTransactions();
		}
		assertEquals(20, Bank.accData.length);
		assertEquals(20, fullTransitions);
		assertEquals(20000, fullBalance);
	}
	@Test
	void testBad() throws InterruptedException{
		String[] args = new String[0];
		Bank.main(args);
	}

	
	
}
