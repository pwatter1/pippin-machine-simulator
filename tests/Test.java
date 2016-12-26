package pippin;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class Test {

	@Test(expected = ParityCheckException.class)
	public void test() {
		int q = 5;
		System.out.println(Instruction.numOnes(q));
		assertEquals(2, Instruction.numOnes(q));
		int r = 18778;
		System.out.println(Instruction.numOnes(r));
		assertEquals(7, Instruction.numOnes(r));
		Instruction.checkParity(5);
		Instruction.checkParity(234);
		Instruction.checkParity(1234);
	}

}
