package pippin;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Loader {
	public static String load(MachineModel model, File program){
		if(model == null || program == null) return null;
		try (Scanner input = new Scanner(program)) {
			while(input.hasNextLine()){
				String line = input.nextLine();
				Scanner parser = new Scanner(line);
				int op = parser.nextInt(16);
				int arg = parser.nextInt(16);
				model.setCode(op, arg);
				parser.close();
				}
			return "success";
		} catch (ArrayIndexOutOfBoundsException e) {
			return("Code array Index " + e.getMessage());
		} catch (NoSuchElementException e) {
			return("NoSuchElementException");
		} catch (FileNotFoundException e1) {
			return("File " + program.getName() + " Not Found");
		}
	}
	public static String load(MachineModel model, File program, File data) {
		if(model == null || program == null || data == null) return null;
		try (Scanner input = new Scanner(program)) {
			while(input.hasNextLine()){
				String line = input.nextLine();
				Scanner parser = new Scanner(line);
				int op = parser.nextInt(16);
				int arg = parser.nextInt(16);
				model.setCode(op, arg);
				parser.close();
				}
		} catch (ArrayIndexOutOfBoundsException e) {
			return("Code array Index " + e.getMessage());
		} catch (NoSuchElementException e) {
			return("NoSuchElementException");
		} catch (FileNotFoundException e1) {
			return("File " + program.getName() + " Not Found");
		}
	
		try(Scanner inp = new Scanner(data)){
			while(inp.hasNextLine()){
				String line = inp.nextLine();
				Scanner parser = new Scanner(line);
				int addr = parser.nextInt(16);
				int val = parser.nextInt(16);
				model.setData(addr, val);
				parser.close();
			}
		
			return "success";
		} catch (ArrayIndexOutOfBoundsException e) {
			return("Data array Index " + e.getMessage());
		} catch (NoSuchElementException e) {
			return("NoSuchElementException");
		} catch (FileNotFoundException e1) {
			return("File " + data.getName() + " Not Found");
		}
	}
	
	public static void main(String[] args) {
		System.out.println("Enter the name of the file without extension: ");
		try (Scanner keyboard = new Scanner(System.in)) {
			String filename = keyboard.nextLine();
			MachineModel test = new MachineModel();
			System.out.println(Loader.load(test, new File(filename + ".pexe"), 
					new File(filename + ".dat")));
			int result = 0;
			test.setRunning(true);
			while(test.isRunning()) {
				if(result != test.getData(1)){
					result = test.getData(1);
					System.out.println("0 => " + test.getData(0) + 
							"; 1 => " + result);
				}
				test.step();
	//the following lines mess up the behavior for positive numbers but
	//provide an output for 0 or negative data
				System.out.println("0 => " + test.getData(0) + 
						"; 1 => " + result);
			}
		}
	}
}
