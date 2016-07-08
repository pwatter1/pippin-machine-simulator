package pippin;

public class MachineCode1 {


	public static final Instruction[] INSTRUCTIONS = new Instruction[16];
	private CPU cpu = new CPU();
	private Memory memory = new Memory();
	private boolean withGUI = false;
	
	public void halt(){
		if(!withGUI){
			System.exit(0);
		}
	}
	
	private class CPU {
		private int accum;
		private int pc;
	}	
	
}
