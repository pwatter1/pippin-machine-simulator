package pippin;

import java.util.Observable;

public class MachineModel extends Observable {

	public static final Instruction[] INSTRUCTIONS = new Instruction[16];
	private CPU cpu = new CPU();
	private Memory memory = new Memory();
	private boolean withGUI = false;
	private Code code = new Code();
	private boolean running = false;
	
	public Code getCode(){
		return code;
	}
	public int getChangedIndex() {
		return memory.getChangedIndex();
	}
	public void setPC(int x){
		cpu.pc = x;
	}
	public void halt(){
		if(!withGUI){
			System.exit(0);
		} else {
			running = false;
		}
	}
	
	public boolean isRunning(){
		return running;
	}
	public void setRunning(boolean p){
		running = p;
	}
	public MachineModel(){
		this(false);
	}
	public void setCode(int op, int arg){
		code.setCode(op, arg);
	}
	public int getData(int i){
		return memory.getData(i);
	}
	public void clear(){
		memory.clear();
		code.clear();
		cpu.accum = 0;
		cpu.pc = 0;
	}
	public void step(){
		//System.out.println("in step");
		try{
			int opPart = code.getOpPart(cpu.pc);
			//System.out.println(opPart/8);
			int arg = code.getArg(cpu.pc);
			Instruction.checkParity(opPart);
			INSTRUCTIONS[opPart/8].execute(arg, opPart%8);
		
			
		} catch(Exception e) {
		e.printStackTrace();
			halt();
			throw e;
		}
	}
	
	
	
	public MachineModel(boolean par){
		
		withGUI = par;
		
		//INSTRUCTION_MAP entry for "NOP"
		INSTRUCTIONS[0] = (arg, flags) -> {
			flags = flags & 0x6; // remove parity bit that will have been verified
			if(flags != 0) {
				String fString = "(" + (flags%8 > 3?"1":"0") + 
							(flags%4 > 1?"1":"0") + ")";
				throw new IllegalInstructionException(
					"Illegal flags for this instruction: " + fString);
			}
			cpu.pc++;			
		};
		
		//INSTRUCTION entry for ADD (add)
		INSTRUCTIONS[0x5] = (arg, flags) -> {
			flags = flags & 0x6; // remove parity bit that will have been verified
			if(flags == 0) { // direct addressing
				cpu.accum += memory.getData(arg);
			} else if(flags == 2) { // immediate addressing
				cpu.accum += arg;
			} else if(flags == 4) { // indirect addressing
				cpu.accum += memory.getData(memory.getData(arg));				
			} else { // here the illegal case is "11"
				String fString = "(" + (flags%8 > 3?"1":"0") 
							+ (flags%4 > 1?"1":"0") + ")";
				throw new IllegalInstructionException(
					"Illegal flags for this instruction: " + fString);
			}
			cpu.pc++;			
		};
		
		//INSTRUCTION entry for LOD (load accumulator)
		INSTRUCTIONS[0x1] = (arg, flags) -> {
			flags = flags & 0x6;
			if(flags == 0){
				cpu.accum = memory.getData(arg);
			} else if(flags == 2){
				cpu.accum = arg;
			} else if(flags == 4){
				cpu.accum = memory.getData(memory.getData(arg));
			} else {
				throw new IllegalInstructionException();
			}
			cpu.pc++;
		};
		
		//INSTRUCTION STO
		INSTRUCTIONS[0X2] = (arg, flags) -> {
			flags = flags & 0x6;
			if(flags == 0){
				memory.setData(arg, cpu.accum);
			}
			else if(flags == 4){
				memory.setData(memory.getData(arg), cpu.accum);
			}else{
				throw new IllegalInstructionException();
			}
			cpu.pc++;
			//System.out.println("here sto" + cpu.pc);
		};
		//INSTRUCTION JUMP
		INSTRUCTIONS[0X3] = (arg, flags) -> {
			flags = flags & 0x6;
			if(flags == 0){
				cpu.pc = cpu.pc + arg;
			}
			else if(flags == 2){
				cpu.pc = arg;
			}
			else if(flags == 4){
				cpu.pc = cpu.pc + memory.getData(arg);
			}else{
				cpu.pc = memory.getData(arg);
			}
		};
		//INSTRUCTION JMPZ
		INSTRUCTIONS[0X4] = (arg, flags) -> {
			flags = flags & 0x6;
			if(cpu.accum == 0){
				if(flags == 0){
					cpu.pc = cpu.pc + arg;
				}
				else if(flags == 2){
					cpu.pc = arg;
				}
				else if(flags == 4){
					cpu.pc = cpu.pc + memory.getData(arg);
				}else{
					cpu.pc = memory.getData(arg);
				}
			}
			else{
				cpu.pc += 1;
			}
		};
		//INSTRUCTION SUB
		INSTRUCTIONS[0X6] = (arg, flags) -> {
			flags = flags & 0x6; // remove parity bit that will have been verified
			if(flags == 0) { // direct addressing
				cpu.accum -= memory.getData(arg);
			} else if(flags == 2) { // immediate addressing
				cpu.accum -= arg;
			} else if(flags == 4) { // indirect addressing
				cpu.accum -= memory.getData(memory.getData(arg));				
			} else { // here the illegal case is "11"
				String fString = "(" + (flags%8 > 3?"1":"0") 
							+ (flags%4 > 1?"1":"0") + ")";
				throw new IllegalInstructionException(
					"Illegal flags for this instruction: " + fString);
			}
			cpu.pc++;			
		};
		//INSTRUCTION MUL
		INSTRUCTIONS[0X7] = (arg, flags) -> {
			flags = flags & 0x6;
			if(flags == 0) { // direct addressing
				cpu.accum *= memory.getData(arg);
			} else if(flags == 2) { // immediate addressing
				cpu.accum *= arg;
			} else if(flags == 4) { // indirect addressing
				cpu.accum *= memory.getData(memory.getData(arg));				
			} else { // here the illegal case is "11"
				String fString = "(" + (flags%8 > 3?"1":"0") 
							+ (flags%4 > 1?"1":"0") + ")";
				throw new IllegalInstructionException(
					"Illegal flags for this instruction: " + fString);
			}
			cpu.pc++;			
		};
		//INSTRUCTIONS DIV
		INSTRUCTIONS[0X8] = (arg, flags) -> {
			flags = flags & 0x6;
			if(flags == 0) { // direct addressing
				if(memory.getData(arg)!= 0){
					cpu.accum /= memory.getData(arg);
				}else{throw new DivideByZeroException();}
			} else if(flags == 2) { // immediate addressing
				if(arg != 0){
					cpu.accum /= arg;
				}else{throw new DivideByZeroException();}
			} else if(flags == 4) { // indirect addressing
				if(memory.getData(memory.getData(arg)) != 0){
					cpu.accum /= memory.getData(memory.getData(arg));
				}else{throw new DivideByZeroException();}
			} else { // here the illegal case is "11"
				String fString = "(" + (flags%8 > 3?"1":"0") 
							+ (flags%4 > 1?"1":"0") + ")";
				throw new IllegalInstructionException(
					"Illegal flags for this instruction: " + fString);
			}
			cpu.pc++;
		};
		//INSTRUCTIONS AND
		INSTRUCTIONS[0X9] = (arg, flags) -> {
			flags = flags & 0x6;
			if(flags == 0){
				if(cpu.accum != 0 && memory.getData(arg) != 0){
					cpu.accum = 1;
				}
				else{
					cpu.accum = 0;
				}
			}
			else if(flags == 2){
				if(cpu.accum != 0 && arg != 0){
					cpu.accum = 1;
				}
				else{
					cpu.accum = 0;
				}
			} else {
				String fString = "(" + (flags%8 > 3?"1":"0") 
						+ (flags%4 > 1?"1":"0") + ")";
				throw new IllegalInstructionException(
					"Illegal flags for this instruction: " + fString);
			}
			cpu.pc++;
		};
		//INSTRUCTIONS NOT
		INSTRUCTIONS[0XA] = (arg, flags) -> {
			flags = flags & 0x6;
			if(flags == 0){
				if(cpu.accum == 0){
					cpu.accum = 1;
				} else {
					cpu.accum = 0;
				}
			} else {
				String fString = "(" + (flags%8 > 3?"1":"0") 
						+ (flags%4 > 1?"1":"0") + ")";
				throw new IllegalInstructionException(
						"Illegal flags for this instruction: " + fString);
			}
			cpu.pc++;
		};
		//INSTRUCTIONS CMPL
		INSTRUCTIONS[0XB] = (arg, flags) -> {
			flags = flags & 0x6;
			if(flags == 0){
				if(memory.getData(arg) < 0){
					cpu.accum = 1;
				} else {
					cpu.accum = 0;
				}
			} else {
				String fString = "(" + (flags%8 > 3?"1":"0") 
						+ (flags%4 > 1?"1":"0") + ")";
				throw new IllegalInstructionException(
						"Illegal flags for this instruction: " + fString);
			}
			cpu.pc++;
		};
		//INSTRUCTIONS CMPZ
		INSTRUCTIONS[0XC] = (arg, flags) -> {
			flags = flags & 0x6;
			if(flags == 0){
				if(memory.getData(arg) == 0){
					cpu.accum = 1;
				} else {
					cpu.accum = 0;
				}
			} else {
				String fString = "(" + (flags%8 > 3?"1":"0") 
						+ (flags%4 > 1?"1":"0") + ")";
				throw new IllegalInstructionException(
						"Illegal flags for this instruction: " + fString);
			}
			cpu.pc++;
		};
		//INSTRUCTIONS FOR
		INSTRUCTIONS[0XD] = (arg, flags) -> {
			flags = flags & 0x6;
			int op;
			if(flags == 0){
				op = getData(arg);
			}else if(flags == 2){
				op = arg;
			}else{
				String fString = "(" + (flags%8 > 3?"1":"0") 
						+ (flags%4 > 1?"1":"0") + ")";
				throw new IllegalInstructionException(
						"Illegal flags for this instruction: " + fString);
			}
			int x = op%0x1000;
			int q = op/0x1000;
			System.out.println(x);
			System.out.println(q);
			int first = ++cpu.pc;
			for(int i = 0; i<x; i++){
				cpu.pc = first;
				for(int p = 0; p<q; p++){
					step();
				}
			}//cpu.pc++;
		};
		//INSTRUCTIONS HALT
		INSTRUCTIONS[0XF] = (arg, flags) -> {
			flags = flags & 0x6;
			if(flags == 0){
				halt();
			} else {
				String fString = "(" + (flags%8 > 3?"1":"0") 
						+ (flags%4 > 1?"1":"0") + ")";
				throw new IllegalInstructionException(
						"Illegal flags for this instruction: " + fString);
			}
		};
		
	}
	
	private class CPU {
		private int accum;
		private int pc;
	}

	public void setData(int i, int j) {
		memory.setData(i, j);		
	}
	public Instruction get(int i) {
		return INSTRUCTIONS[i];
	}
	int[] getData() {
		return memory.getData();
	}
	public int getPC() {
		return cpu.pc;
	}
	public int getAccum() {
		return cpu.accum;
	}
	public void setAccum(int i) {
		cpu.accum = i;
	}
}