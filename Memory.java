package pippin;

public class Memory {
	
	public static final int DATA_SIZE = 512;
	String s = Integer.toUnsignedString(DATA_SIZE, 2);
	private int[] data = new int[DATA_SIZE];
	private int changedIndex = -1;
	public int[] getData(){
		return data;
	}
	public int getData(int index){
		return data[index];
	}
	public void setData(int index, int value){
		data[index] = value;
		changedIndex = index;
	}
	public void clear(){
		data = new int[DATA_SIZE];
		changedIndex = -1;
	}
	public int getChangedIndex(){
		return changedIndex;
	}
}
