package pippin;


import static org.junit.Assert.*;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.Rule;
//import org.junit.contrib.java.lang.system.ExpectedSystemExit;

public class InstructionTester1 {

	MachineModel machine = new MachineModel(false);
	int[] dataCopy = new int[Memory.DATA_SIZE];
	int accInit;
	int ipInit;
	/**
	@Before
	public static void setUp {
		for (int i = 0; i < Memory.DATA_SIZE; i++) {
			dataCopy[i] = -5*Memory.DATA_SIZE + 10*i;
			machine.setData(i, -5*Memory.DATA_SIZE + 10*i);
			// Initially the machine will contain a known spread
			// of different numbers: 
			// -2560, -2550, -2540, ..., 0, 10, 20, ..., 2550 
			// This allows us to check that the instructions do 
			// not corrupt machine unexpectedly.
			// 0 is at index 256
		}
		accInit = 0;
		ipInit = 0;
	}
	**/
	
	@Test
	public void testNOP(){
		Instruction instr = machine.get(0x0);
		instr.execute(0,0);
		//Test machine is not changed
		assertArrayEquals(dataCopy, machine.getData());
		//Test program counter incremented
		assertEquals("Program counter incremented", ipInit+1,
				machine.getPC());
		//Test accumulator untouched
		assertEquals("Accumulator unchanged", accInit,
				machine.getAccum());
	}

	@Test
	// Test whether load is correct with immediate addressing
	public void testLODI(){
		Instruction instr = machine.get(0x1);
		machine.setAccum(27);
		int arg = 12;
			// should load 12 into the accumulator
			instr.execute(arg, 2);
		//Test machine is not changed
        assertArrayEquals(dataCopy, machine.getData());
        //Test program counter incremented
        assertEquals("Program counter incremented", ipInit+1,
                machine.getPC());
        //Test accumulator modified
        assertEquals("Accumulator changed", 12,
                machine.getAccum());
	}

	@Test
	// Test whether load is correct with direct addressing
	public void testLOD(){
		Instruction instr = machine.get(0x1);
		machine.setAccum(27);
		int arg = 12;
			// should load -2560+120 into the accumulator
		instr.execute(arg, 0);
		//Test machine is not changed
        assertArrayEquals(dataCopy, machine.getData());
        //Test program counter incremented
        assertEquals("Program counter incremented", ipInit+1,
        		machine.getPC());
        //Test accumulator modified
        assertEquals("Accumulator changed", -2560+120,
        		machine.getAccum());
	}

	@Test
	// Test whether load is correct with direct addressing
	public void testLODN() {
		Instruction instr = machine.get(0x1);
		machine.setAccum(-1);
		int arg = 260;
		// should load data[-2560+2600] = data[40] = -2560 + 400
		// into the accumulator
		instr.execute(arg, 4);
		//Test machine is not changed
        assertArrayEquals(dataCopy, machine.getData());
        //Test program counter incremented
        assertEquals("Program counter incremented", ipInit+1,
                machine.getPC());
        //Test accumulator modified
        assertEquals("Accumulator changed", -2560+400,
                machine.getAccum());
	}	
	
	@Test
	// Test whether store is correct with direct addressing
	public void testSTO() {
		Instruction instr = machine.get(0x2);
		int arg = 12;
		machine.setAccum(567);
		dataCopy[12] = 567;
		instr.execute(arg, 0);
		//Test machine is changed correctly
		assertArrayEquals(dataCopy, machine.getData());
		//Test program counter incremented
		assertEquals("Program counter incremented", ipInit+1,
				machine.getPC());
		//Test accumulator unchanged
		assertEquals("Accumulator unchanged", 567,
				machine.getAccum());
	}

	@Test
	// Test whether store is correct with indirect addressing
	public void testSTON() {
		Instruction instr = machine.get(0x2);
		int arg = 260; // -2560+2600 = 40
		machine.setAccum(567);
		dataCopy[40] = 567;
		instr.execute(arg, 4);
		//Test machine is changed correctly
		assertArrayEquals(dataCopy, machine.getData());
		//Test program counter incremented
		assertEquals("Program counter incremented", ipInit+1,
				machine.getPC());
		//Test accumulator unchanged
		assertEquals("Accumulator unchanged", 567,
				machine.getAccum());
	}

	@Test 
	// this test checks whether the relative jump is done correctly, when
	// address is the argument
	public void testJMP0() {
		// first increment PC to 10
		Instruction instr = machine.get(0x0);
		for(int i = 0; i < 10; i++) instr.execute(0, 0);
		instr = machine.get(0x3);
		int arg = 160;  
		machine.setAccum(100);
		instr.execute(arg, 0); 
		// should have set the program counter to 40
		assertArrayEquals(dataCopy, machine.getData()); 
		assertEquals("Program counter was changed", 170,
				machine.getPC());
		assertEquals("Accumulator was not changed", 100,
				machine.getAccum());
	}

	@Test 
	// this test checks whether the absolute jump is done correctly, when
	// address is the argument
	public void testJMP2() {
		Instruction instr = machine.get(0x3);
		int arg = 260;  
		machine.setAccum(200);
		instr.execute(arg, 2); 
		// should have set the program counter to 40
		assertArrayEquals(dataCopy, machine.getData()); 
		assertEquals("Program counter was changed", 260,
				machine.getPC());
		assertEquals("Accumulator was not changed", 200,
				machine.getAccum());
	}

	@Test 
	// this test checks whether the relative jump is done correctly, when
	// address is in memory
	public void testJMP4() {
		// first increment PC to 20
		Instruction instr = machine.get(0x0);
		for(int i = 0; i < 20; i++) instr.execute(0, 0);
		instr = machine.get(0x3);
		int arg = 260; // the machine value is data[260] = -2560+2600 = 40 
		machine.setAccum(200);
		instr.execute(arg, 4); 
		// should have set the program counter to 40
		assertArrayEquals(dataCopy, machine.getData()); 
		assertEquals("Program counter was changed", 60,
				machine.getPC());
		assertEquals("Accumulator was not changed", 200,
				machine.getAccum());
	}

	@Test 
	// this test checks whether the absolute jump is done correctly, when
	// address is in memory
	public void testJMP6() {
		Instruction instr = machine.get(0x3);
		int arg = 260; // the machine value is data[260] = -2560+2600 = 40 
		machine.setAccum(200);
		instr.execute(arg, 6); 
		// should have set the program counter to 40
		assertArrayEquals(dataCopy, machine.getData()); 
		assertEquals("Program counter was changed", 40,
				machine.getPC());
		assertEquals("Accumulator was not changed", 200,
				machine.getAccum());
	}

	@Test 
	// this test checks whether the relative jump is done correctly, when
	// address is the argument
	public void testJMPZ0() {
		// first increment PC to 10
		Instruction instr = machine.get(0x0);
		for(int i = 0; i < 10; i++) instr.execute(0, 0);
		instr = machine.get(0x4);
		int arg = 260;  
		machine.setAccum(0);
		instr.execute(arg, 0); 
		// should have set the program counter to 40
		assertArrayEquals(dataCopy, machine.getData()); 
		assertEquals("Program counter was changed", 270,
				machine.getPC());
		assertEquals("Accumulator was not changed", 0,
				machine.getAccum());
	}

	@Test 
	// this test checks whether the absolute jump is done correctly, when
	// address is the argument
	public void testJMPZ2() {
		Instruction instr = machine.get(0x4);
		int arg = 260;  
		machine.setAccum(0);
		instr.execute(arg, 2); 
		// should have set the program counter to 40
		assertArrayEquals(dataCopy, machine.getData()); 
		assertEquals("Program counter was changed", 260,
				machine.getPC());
		assertEquals("Accumulator was not changed", 0,
				machine.getAccum());
	}

	@Test 
	// this test checks whether the relative jump is done correctly, when
	// address is in memory
	public void testJMPZ4() {
		// first increment PC to 20
		Instruction instr = machine.get(0x0);
		for(int i = 0; i < 20; i++) instr.execute(0, 0);
		instr = machine.get(0x4);
		int arg = 260; // the machine value is data[260] = -2560+2600 = 40 
		machine.setAccum(0);
		instr.execute(arg, 4); 
		// should have set the program counter to 40
		assertArrayEquals(dataCopy, machine.getData()); 
		assertEquals("Program counter was changed", 60,
				machine.getPC());
		assertEquals("Accumulator was not changed", 0,
				machine.getAccum());
	}

	@Test 
	// this test checks whether the absolute jump is done correctly, when
	// address is in memory
	public void testJMPZ6() {
		Instruction instr = machine.get(0x4);
		int arg = 260; // the machine value is data[260] = -2560+2600 = 40 
		machine.setAccum(0);
		instr.execute(arg, 6); 
		// should have set the program counter to 40
		assertArrayEquals(dataCopy, machine.getData()); 
		assertEquals("Program counter was changed", 40,
				machine.getPC());
		assertEquals("Accumulator was not changed", 0,
				machine.getAccum());
	}

	@Test 
	// this test checks whether no jump is done if accumulator is zero, 
	// address is the argument
	public void testJMZ0accumNonZero() {
		Instruction instr = machine.get(0x4);
		int arg = 260;  
		machine.setAccum(200);
		instr.execute(arg, 0); 
		// should have set the program counter incremented
		assertArrayEquals(dataCopy, machine.getData()); 
		assertEquals("Program counter was incremented", ipInit+1,
				machine.getPC());
		assertEquals("Accumulator was not changed", 200,
				machine.getAccum());
	}

	@Test 
	// this test checks whether no jump is done if accumulator is zero, 
	// address is the argument
	public void testJMZ2accumNonZero() {
		Instruction instr = machine.get(0x4);
		int arg = 260;  
		machine.setAccum(200);
		instr.execute(arg, 2); 
		// should have set the program counter incremented
		assertArrayEquals(dataCopy, machine.getData()); 
		assertEquals("Program counter was incremented", ipInit+1,
				machine.getPC());
		assertEquals("Accumulator was not changed", 200,
				machine.getAccum());
	}

	@Test 
	// this test checks whether no jump is done if accumulator is zero, 
	// address is in memory
	public void testJMPZ4directAccumNonZero() {
		Instruction instr = machine.get(0x4);
		int arg = 260; // the machine value is data[260] = -2560+2600 = 40 
		machine.setAccum(200);
		instr.execute(arg, 4); 
		// should have set the program counter incremented
		assertArrayEquals(dataCopy, machine.getData()); 
		assertEquals("Program counter was incremented", ipInit+1,
				machine.getPC());
		assertEquals("Accumulator was not changed", 200,
				machine.getAccum());
	}

	@Test 
	// this test checks whether no jump is done if accumulator is zero, 
	// address is in memory
	public void testJMPZ6directAccumNonZero() {
		Instruction instr = machine.get(0x4);
		int arg = 260; // the machine value is data[260] = -2560+2600 = 40 
		machine.setAccum(200);
		instr.execute(arg, 6); 
		// should have set the program counter incremented
		assertArrayEquals(dataCopy, machine.getData()); 
		assertEquals("Program counter was incremented", ipInit+1,
				machine.getPC());
		assertEquals("Accumulator was not changed", 200,
				machine.getAccum());
	}

	@Test 
	// this test checks whether the add is done correctly, when
	// addressing is immediate
	public void testADDI() {
		Instruction instr = machine.get(0x5);
		int arg = 12; 
		machine.setAccum(200);
		instr.execute(arg, 2); 
		// should have added 12 to accumulator
		assertArrayEquals(dataCopy, machine.getData()); 
		assertEquals("Program counter was incremented", ipInit + 1,
				machine.getPC());
		assertEquals("Accumulator was changed", 200+12,
				machine.getAccum());
	}

	@Test 
	// this test checks whether the add is done correctly, when
	// addressing is direct
	public void testADD() {
		Instruction instr = machine.get(0x5);
		int arg = 12; // we know that machine value is -2560+120
		machine.setAccum(200);
		instr.execute(arg, 0); 
		// should have added -2560+120 to accumulator
		assertArrayEquals(dataCopy, machine.getData()); 
		assertEquals("Program counter was incremented", ipInit + 1,
				machine.getPC());
		assertEquals("Accumulator was changed", 200-2560+120,
				machine.getAccum());
	}

	@Test 
	// this test checks whether the add is done correctly, when
	// addressing is indirect
	public void testADDN() {
		Instruction instr = machine.get(0x5);
		int arg = 260; // we know that address is -2560+2600 = 40
		// and the machine value is data[40] = -2560+400 = -2160 
		machine.setAccum(200);
		instr.execute(arg, 4); 
		// should have added -2560+400 to accumulator
		assertArrayEquals(dataCopy, machine.getData()); 
		assertEquals("Program counter was incremented", ipInit + 1,
				machine.getPC());
		assertEquals("Accumulator was changed", 200-2560+400,
				machine.getAccum());
	}

	@Test 
	// this test checks whether the subtract is done correctly, when
	// addressing is immediate
	public void testSUBI() {
		Instruction instr = machine.get(0x6);
		int arg = 12; 
		machine.setAccum(200);
		instr.execute(arg, 2); 
		// should have subtracted 12 from accumulator
		assertArrayEquals(dataCopy, machine.getData()); 
		assertEquals("Program counter was incremented", ipInit + 1,
				machine.getPC());
		assertEquals("Accumulator was changed", 200-12,
				machine.getAccum());
	}

	@Test 
	// this test checks whether the subtract is done correctly, when
	// addressing is direct
	public void testSUB() {
		Instruction instr = machine.get(0x6);
		int arg = 12; // we know that machine value is -2560+120
		machine.setAccum(200);
		instr.execute(arg, 0); 
		// should have subtracted -2560+120 from accumulator
		assertArrayEquals(dataCopy, machine.getData()); 
		assertEquals("Program counter was incremented", ipInit + 1,
				machine.getPC());
		assertEquals("Accumulator was changed", 200+2560-120,
				machine.getAccum());
	}

	@Test 
	// this test checks whether the subtract is done correctly, when
	// addressing is indirect
	public void testSUBN() {
		Instruction instr = machine.get(0x6);
		int arg = 260; // we know that address is -2560+2600 = 40
		// and the machine value is data[40] = -2560+400 = -2160 
		machine.setAccum(200);
		instr.execute(arg, 4); 
		// should have subtracted -2560+400 from accumulator
		assertArrayEquals(dataCopy, machine.getData()); 
		assertEquals("Program counter was incremented", ipInit + 1,
				machine.getPC());
		assertEquals("Accumulator was changed", 200+2560-400,
				machine.getAccum());
	}

	@Test 
	// this test checks whether the multiplication is done correctly, when
	// addressing is immediate
	public void testMULI() {
		Instruction instr = machine.get(0x7);
		int arg = 12; 
		machine.setAccum(200);
		instr.execute(arg, 2); 
		// should have multiplied accumulator by 12
		assertArrayEquals(dataCopy, machine.getData()); 
		assertEquals("Program counter was incremented", ipInit + 1,
				machine.getPC());
		assertEquals("Accumulator was changed", 200*12,
				machine.getAccum());
	}

	@Test 
	// this test checks whether the multiplication is done correctly, when
	// addressing is direct
	public void testMUL() {
		Instruction instr = machine.get(0x7);
		int arg = 12; // we know that machine value is -2560+120
		machine.setAccum(200);
		instr.execute(arg, 0); 
		// should have multiplied accumulator by -2560+120 
		assertArrayEquals(dataCopy, machine.getData()); 
		assertEquals("Program counter was incremented", ipInit + 1,
				machine.getPC());
		assertEquals("Accumulator was changed", 200*(-2560+120),
				machine.getAccum());
	}

	@Test 
	// this test checks whether the multiplication is done correctly, when
	// addressing is indirect
	public void testMULN() {
		Instruction instr = machine.get(0x7);
		int arg = 260; // we know that address is -2560+2600 = 40
		// and the machine value is data[40] = -2560+400 = -2160 
		machine.setAccum(200);
		instr.execute(arg, 4); 
		// should have multiplied to accumulator -2560+400
		assertArrayEquals(dataCopy, machine.getData()); 
		assertEquals("Program counter was incremented", ipInit + 1,
				machine.getPC());
		assertEquals("Accumulator was changed", 200*(-2560+400),
				machine.getAccum());
	}

 	@Test 
	// this test checks whether the division is done correctly, when
	// addressing is immediate
	public void testDIVI() {
		Instruction instr = machine.get(0x8);
		int arg = 12; 
		machine.setAccum(200);
		instr.execute(arg, 2); 
		// should have divided accumulator by 12
		assertArrayEquals(dataCopy, machine.getData()); 
		assertEquals("Program counter was incremented", ipInit + 1,
				machine.getPC());
		assertEquals("Accumulator was changed", 200/12,
				machine.getAccum());
	}

	@Test 
	// this test checks whether the division is done correctly, when
	// addressing is direct
	public void testDIV() {
		Instruction instr = machine.get(0x8);
		int arg = 12; // we know that machine value is -2560+120
		machine.setAccum(200);
		instr.execute(arg, 0); 
		// should have divided accumulator by -2560+120 
		assertArrayEquals(dataCopy, machine.getData()); 
		assertEquals("Program counter was incremented", ipInit + 1,
				machine.getPC());
		assertEquals("Accumulator was changed", 200/(-2560+120),
				machine.getAccum());
	}

	@Test 
	// this test checks whether the division is done correctly, when
	// addressing is indirect
	public void testDIVN() {
		Instruction instr = machine.get(0x8);
		int arg = 260; // we know that address is -2560+2600 = 40
		// and the machine value is data[40] = -2560+400 = -2160 
		machine.setAccum(200);
		instr.execute(arg, 4); 
		// should have divided to accumulator -2560+400
		assertArrayEquals(dataCopy, machine.getData()); 
		assertEquals("Program counter was incremented", ipInit + 1,
				machine.getPC());
		assertEquals("Accumulator was changed", 200/(-2560+400),
				machine.getAccum());
	}
	
	@Test (expected=DivideByZeroException.class) 
	// this test checks whether the DivideByZeroException is thrown 
	// for immediate division by 0
	public void testDIVIzero() {
		Instruction instr = machine.get(0x8);
		int arg = 0; 
		instr.execute(arg, 2);
	}

	@Test (expected=DivideByZeroException.class) 
	// this test checks whether the DivideByZeroException is thrown 
	// for division by 0 from machine
	public void testDIVzero() {
		Instruction instr = machine.get(0x8);
		int arg = 256; 
		instr.execute(arg, 0);
	}

	@Test (expected=DivideByZeroException.class) 
	// this test checks whether the DivideByZeroException is thrown 
	// for division by 0 from machine
	public void testDIVNzero() {
		Instruction instr = machine.get(0x8);
		machine.setData(100, 256);
		int arg = 100; 
		instr.execute(arg, 4);
	}
	
	@Test
	// Check ANDI when accum and arg equal to 0 gives false
	public void testANDIaccEQ0argEQ0() {
		Instruction instr = machine.get(0x9);
		int arg = 0;
		machine.setAccum(0);
		instr.execute(arg, 2);
		//Test machine is not changed
        assertArrayEquals(dataCopy, machine.getData()); 
        //Test program counter incremented
        assertEquals("Program counter incremented", ipInit + 1,
                machine.getPC());
        //Accumulator is 1
        assertEquals("Accumulator is 0", 0,
                machine.getAccum());
	}
	
	@Test
	// Check ANDI when accum and arg pos gives true
	public void testANDIaccGT0argGT0() {
		Instruction instr = machine.get(0x9);
		int arg = 300;
		machine.setAccum(10);
		instr.execute(arg, 2);
		//Test machine is not changed
        assertArrayEquals(dataCopy, machine.getData()); 
        //Test program counter incremented
        assertEquals("Program counter incremented", ipInit + 1,
                machine.getPC());
        //Accumulator is 1
        assertEquals("Accumulator is 1", 1,
                machine.getAccum());
	}
	
	@Test
	// Check ANDI when accum and arg neg gives true
	public void testANDIaccLT0argLT0() {
		Instruction instr = machine.get(0x9);
		int arg = -200;
		machine.setAccum(-10);
		instr.execute(arg, 2);
		//Test machine is not changed
        assertArrayEquals(dataCopy, machine.getData()); 
        //Test program counter incremented
        assertEquals("Program counter incremented", ipInit + 1,
                machine.getPC());
        //Accumulator is 1
        assertEquals("Accumulator is 1", 1,
                machine.getAccum());
	}
	
	@Test
	// Check ANDI when accum neg and arg pos gives true
	public void testANDIaccLT0argGT0() {
		Instruction instr = machine.get(0x9);
		int arg = 300;
		machine.setAccum(-10);
		instr.execute(arg, 2);
		//Test machine is not changed
        assertArrayEquals(dataCopy, machine.getData()); 
        //Test program counter incremented
        assertEquals("Program counter incremented", ipInit + 1,
                machine.getPC());
        //Accumulator is 1
        assertEquals("Accumulator is 1", 1,
                machine.getAccum());
	}
	
	@Test
	// Check ANDI when accum pos and arg neg gives true
	public void testANDIaccGT0argLT0() {
		Instruction instr = machine.get(0x9);
		int arg = -200;
		machine.setAccum(10);
		instr.execute(arg, 2);
		//Test machine is not changed
        assertArrayEquals(dataCopy, machine.getData()); 
        //Test program counter incremented
        assertEquals("Program counter incremented", ipInit + 1,
                machine.getPC());
        //Accumulator is 1
        assertEquals("Accumulator is 1", 1,
                machine.getAccum());
	}
	
	@Test
	// Check AND when accum pos mem equal to zero gives false
	public void testANDIaccGT0argEQ0() {
		Instruction instr = machine.get(0x9);
		int arg = 0;
		machine.setAccum(10);
		instr.execute(arg, 2);
		//Test machine is not changed
        assertArrayEquals(dataCopy, machine.getData()); 
        //Test program counter incremented
        assertEquals("Program counter incremented", ipInit + 1,
                machine.getPC());
        //Accumulator is 1
        assertEquals("Accumulator is 0", 0,
                machine.getAccum());
	}
	
	@Test
	// Check ANDI when accum neg mem equal to zero gives false
	public void testANDIaccLT0argEQ0() {
		Instruction instr = machine.get(0x9);
		int arg = 0;
		machine.setAccum(-10);
		instr.execute(arg, 2);
		//Test machine is not changed
        assertArrayEquals(dataCopy, machine.getData()); 
        //Test program counter incremented
        assertEquals("Program counter incremented", ipInit + 1,
                machine.getPC());
        //Accumulator is 1
        assertEquals("Accumulator is 0", 0,
                machine.getAccum());
	}
	
	@Test
	// Check ANDI when accum equal to zero and mem pos gives false
	public void testANDIaccEQ0argGT0() {
		Instruction instr = machine.get(0x9);
		int arg = 300;
		machine.setAccum(0);
		instr.execute(arg, 2);
		//Test machine is not changed
        assertArrayEquals(dataCopy, machine.getData()); 
        //Test program counter incremented
        assertEquals("Program counter incremented", ipInit + 1,
                machine.getPC());
        //Accumulator is 1
        assertEquals("Accumulator is 0", 0,
                machine.getAccum());
	}
	
	@Test
	// Check ANDI when accum equal to zero and mem neg gives false
	public void testANDIaccEQ0argLT0() {
		Instruction instr = machine.get(0x9);
		int arg = -200;
		machine.setAccum(0);
		instr.execute(arg, 2);
		//Test machine is not changed
        assertArrayEquals(dataCopy, machine.getData()); 
        //Test program counter incremented
        assertEquals("Program counter incremented", ipInit + 1,
                machine.getPC());
        //Accumulator is 1
        assertEquals("Accumulator is 0", 0,
                machine.getAccum());
	}

	@Test
	// Check AND when accum and mem equal to 0 gives false
	public void testANDaccEQ0memEQ0() {
		Instruction instr = machine.get(0x9);
		int arg = 256;
		machine.setAccum(0);
		instr.execute(arg, 0);
		//Test machine is not changed
        assertArrayEquals(dataCopy, machine.getData()); 
        //Test program counter incremented
        assertEquals("Program counter incremented", ipInit + 1,
                machine.getPC());
        //Accumulator is 1
        assertEquals("Accumulator is 0", 0,
                machine.getAccum());
	}
	
	@Test
	// Check AND when accum and mem pos gives true
	public void testANDaccGT0memGT0() {
		Instruction instr = machine.get(0x9);
		int arg = 300;
		machine.setAccum(10);
		instr.execute(arg,0);
		//Test machine is not changed
        assertArrayEquals(dataCopy, machine.getData()); 
        //Test program counter incremented
        assertEquals("Program counter incremented", ipInit + 1,
                machine.getPC());
        //Accumulator is 1
        assertEquals("Accumulator is 1", 1,
                machine.getAccum());
	}
	
	@Test
	// Check AND when accum and mem neg gives true
	public void testANDaccLT0memLT0() {
		Instruction instr = machine.get(0x9);
		int arg = 200;
		machine.setAccum(-10);
		instr.execute(arg,0);
		//Test machine is not changed
        assertArrayEquals(dataCopy, machine.getData()); 
        //Test program counter incremented
        assertEquals("Program counter incremented", ipInit + 1,
                machine.getPC());
        //Accumulator is 1
        assertEquals("Accumulator is 1", 1,
                machine.getAccum());
	}
	
	@Test
	// Check AND when accum neg and mem pos gives true
	public void testANDaccLT0memGT0() {
		Instruction instr = machine.get(0x9);
		int arg = 300;
		machine.setAccum(-10);
		instr.execute(arg,0);
		//Test machine is not changed
        assertArrayEquals(dataCopy, machine.getData()); 
        //Test program counter incremented
        assertEquals("Program counter incremented", ipInit + 1,
                machine.getPC());
        //Accumulator is 1
        assertEquals("Accumulator is 1", 1,
                machine.getAccum());
	}
	
	@Test
	// Check AND when accum pos and mem neg gives true
	public void testANDaccGT0memLT0() {
		Instruction instr = machine.get(0x9);
		int arg = 200;
		machine.setAccum(10);
		instr.execute(arg,0);
		//Test machine is not changed
        assertArrayEquals(dataCopy, machine.getData()); 
        //Test program counter incremented
        assertEquals("Program counter incremented", ipInit + 1,
                machine.getPC());
        //Accumulator is 1
        assertEquals("Accumulator is 1", 1,
                machine.getAccum());
	}
	
	@Test
	// Check AND when accum pos mem equal to zero gives false
	public void testANDaccGT0memEQ0() {
		Instruction instr = machine.get(0x9);
		int arg = 256;
		machine.setAccum(10);
		instr.execute(arg,0);
		//Test machine is not changed
        assertArrayEquals(dataCopy, machine.getData()); 
        //Test program counter incremented
        assertEquals("Program counter incremented", ipInit + 1,
                machine.getPC());
        //Accumulator is 1
        assertEquals("Accumulator is 0", 0,
                machine.getAccum());
	}
	
	@Test
	// Check AND when accum neg mem equal to zero gives false
	public void testANDaccLT0memEQ0() {
		Instruction instr = machine.get(0x9);
		int arg = 256;
		machine.setAccum(-10);
		instr.execute(arg,0);
		//Test machine is not changed
        assertArrayEquals(dataCopy, machine.getData()); 
        //Test program counter incremented
        assertEquals("Program counter incremented", ipInit + 1,
                machine.getPC());
        //Accumulator is 1
        assertEquals("Accumulator is 0", 0,
                machine.getAccum());
	}
	
	@Test
	// Check AND when accum equal to zero and mem pos gives false
	public void testANDaccEQ0memGT0() {
		Instruction instr = machine.get(0x9);
		int arg = 300;
		machine.setAccum(0);
		instr.execute(arg,0);
		//Test machine is not changed
        assertArrayEquals(dataCopy, machine.getData()); 
        //Test program counter incremented
        assertEquals("Program counter incremented", ipInit + 1,
                machine.getPC());
        //Accumulator is 1
        assertEquals("Accumulator is 0", 0,
                machine.getAccum());
	}
	
	@Test
	// Check AND when accum equal to zero and mem neg gives false
	public void testANDaccEQ0memLT0() {
		Instruction instr = machine.get(0x9);
		int arg = 200;
		machine.setAccum(0);
		instr.execute(arg,0);
		//Test machine is not changed
        assertArrayEquals(dataCopy, machine.getData()); 
        //Test program counter incremented
        assertEquals("Program counter incremented", ipInit + 1,
                machine.getPC());
        //Accumulator is 1
        assertEquals("Accumulator is 0", 0,
                machine.getAccum());
	}

	@Test
	// Check NOT greater than 0 gives false
	public void testNOTaccGT0() {
		Instruction instr = machine.get(0XA);
		machine.setAccum(10);
		instr.execute(0,0);
		//Test machine is not changed
        assertArrayEquals(dataCopy, machine.getData()); 
        //Test program counter incremented
        assertEquals("Program counter incremented", ipInit + 1,
                machine.getPC());
        //Accumulator is 1
        assertEquals("Accumulator is 0", 0,
                machine.getAccum());
	}

	@Test
	// Check NOT equal to 0 gives true
	public void testNOTaccEQ0() {
		Instruction instr = machine.get(0XA);
		machine.setAccum(0);
		instr.execute(0,0);
		//Test machine is not changed
        assertArrayEquals(dataCopy, machine.getData()); 
        //Test program counter incremented
        assertEquals("Program counter incremented", ipInit + 1,
                machine.getPC());
        //Accumulator is 1
        assertEquals("Accumulator is 1", 1,
                machine.getAccum());
	}
	
	@Test
	// Check NOT less than 0 gives false
	public void testNOTaccLT0() {
		Instruction instr = machine.get(0XA);
		machine.setAccum(-10);
		instr.execute(0,0);
		//Test machine is not changed
        assertArrayEquals(dataCopy, machine.getData()); 
        //Test program counter incremented
        assertEquals("Program counter incremented", ipInit + 1,
                machine.getPC());
        //Accumulator is 1
        assertEquals("Accumulator is 0", 0,
                machine.getAccum());
	}
	
	@Test
	// Check CMPL when comparing less than 0 gives true
	public void testCMPLmemLT0() {
		Instruction instr = machine.get(0xB);
		int arg = 100;
		instr.execute(arg,0);
		//Test machine is not changed
        assertArrayEquals(dataCopy, machine.getData()); 
        //Test program counter incremented
        assertEquals("Program counter incremented", ipInit + 1,
                machine.getPC());
        //Accumulator is 1
        assertEquals("Accumulator is 1", 1,
                machine.getAccum());
	}

	@Test
	// Check CMPL when comparing equal to 0 gives false
	public void testCMPLmemEQ0() {
		Instruction instr = machine.get(0xB);
		int arg = 256;
		instr.execute(arg,0);
		//Test machine is not changed
        assertArrayEquals(dataCopy, machine.getData()); 
        //Test program counter incremented
        assertEquals("Program counter incremented", ipInit + 1,
                machine.getPC());
        //Accumulator is 1
        assertEquals("Accumulator is 0", 0,
                machine.getAccum());
	}

	@Test
	// Check CMPL when comparing greater than 0 gives false
	public void testCMPLmemGT0() {
		Instruction instr = machine.get(0xB);
		int arg = 300;
		instr.execute(arg,0);
		//Test machine is not changed
        assertArrayEquals(dataCopy, machine.getData()); 
        //Test program counter incremented
        assertEquals("Program counter incremented", ipInit + 1,
                machine.getPC());
        //Accumulator is 1
        assertEquals("Accumulator is 0", 0,
                machine.getAccum());
	}

	@Test
	// Check CMPZ when comparing less than 0 gives false
	public void testCMPZmemLT0() {
		Instruction instr = machine.get(0xC);
		int arg = 100;
		instr.execute(arg,0);
		//Test machine is not changed
        assertArrayEquals(dataCopy, machine.getData()); 
        //Test program counter incremented
        assertEquals("Program counter incremented", ipInit + 1,
                machine.getPC());
        //Accumulator is 1
        assertEquals("Accumulator is 0", 0,
                machine.getAccum());
	}

	@Test
	// Check CMPZ when comparing equal to 0 gives true
	public void testCMPZmemEQ0() {
		Instruction instr = machine.get(0xC);
		int arg = 256;
		instr.execute(arg,0);
		//Test machine is not changed
        assertArrayEquals(dataCopy, machine.getData()); 
        //Test program counter incremented
        assertEquals("Program counter incremented", ipInit + 1,
                machine.getPC());
        //Accumulator is 1
        assertEquals("Accumulator is 1", 1,
                machine.getAccum());
	}

	@Test
	// Check CMPZ when comparing greater than 0 gives false
	public void testCMPZmemGT0() {
		Instruction instr = machine.get(0xC);
		int arg = 300;
		instr.execute(arg,0);
		//Test machine is not changed
        assertArrayEquals(dataCopy, machine.getData()); 
        //Test program counter incremented
        assertEquals("Program counter incremented", ipInit + 1,
                machine.getPC());
        //Accumulator is 1
        assertEquals("Accumulator is 0", 0,
                machine.getAccum());
	}

	// HALT is missing at this point
}
