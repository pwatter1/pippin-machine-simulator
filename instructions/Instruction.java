package pippin;

public interface Instruction {

	public void execute(int arg, int flags);
	
	public static int numOnes(int i){
		/**
		int count = 0;
		String s = Integer.toUnsignedString(k, 2);
		for(int i = 0; i < s.length(); i++){
			
			if(s.charAt(i) == '1'){
				count += 1;
			}
		}
		return count;
		**/
		i = i - ((i >>> 1) & 0x55555555);
		i = (i & 0x33333333) + ((i >>> 2) & 0x33333333);
		return (((i + (i >>> 4)) & 0x0F0F0F0F) * 0x01010101) >>> 24;
	}
	public static void checkParity(int p){
		int count = 0;
		String s = Integer.toUnsignedString(p, 2);
		for(int i = 0; i < s.length(); i++){
			if(s.charAt(i) == '1'){
				count += 1;
			}
		}
		if(count%2 != 0){
			throw new ParityCheckException("This instruction is corrupted");
		}
	}
}

