import static org.junit.jupiter.api.Assertions.*;


import org.junit.jupiter.api.Test;

class CrackerTest {

	@Test
	void testBad() {
		String[] args = new String[0];
		Cracker.main(args);
	}
	@Test
	void testHashGeneration() {
		String[] args = new String[1];
		args[0]="a";
		Cracker.main(args);
		assertEquals(Cracker.generateHash("a"), "86f7e437faa5a7fce15d1ddcb9eaeaea377667b8");
		assertEquals(Cracker.generateHash("fm"), "adeb6f2a18fe33af368d91b09587b68e3abcb9a7");
		assertEquals(Cracker.generateHash("a!"), "34800e15707fae815d7c90d49de44aca97e2d759");
		assertEquals(Cracker.generateHash("xyz"), "66b27417d37e024c46526c2f6d358a754fc552f3");
		assertEquals(Cracker.generateHash("molly"), "4181eecbd7a755d19fdf73887c54837cbecf63fd");
	}
	@Test
	void testCrackingBadInp() {
		String[] args = new String[3];
		args[0]="86f7e437faa5a7fce15d1ddcb9eaeaea377667b8";
		args[1]="1";
		args[2] = "41";
		Cracker.main(args);
	}
	@Test 
	void testCracking0() {
		String[] args = new String[3];
		args[0]="86f7e437faa5a7fce15d1ddcb9eaeaea377667b8";
		args[1]="1";
		args[2] = "5";
		Cracker.main(args);
		assertEquals("a", Cracker.foundPass);
	}
	@Test
	void testCracking1() {
		String[] args = new String[3];
		args[0]="86f7e437faa5a7fce15d1ddcb9eaeaea377667b8";
		args[1]="1";
		args[2] = "0";
		Cracker.main(args);
	}
	
	@Test
	void testCrackingTwoArgs() {
		String[] args = new String[2];
		args[0]="34800e15707fae815d7c90d49de44aca97e2d759";
		args[1]="2";
		Cracker.main(args);
		assertEquals("a!", Cracker.foundPass);
	}
	@Test
	void testCrackingNoPass() {
		String[] args = new String[3];
		args[0]="anlansdnaskjdnaldonwdamsdaawadasw";
		args[1]="1";
		args[2] = "5";
		Cracker.main(args);
		assertEquals(null, Cracker.foundPass);
	}
}
