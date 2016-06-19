package pippin;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

public class MenuBarBuilder implements Observer {
	private JMenuItem assemble = new JMenuItem("Assemble Source...");
	private JMenuItem load = new JMenuItem("Load Program...");
	private JMenuItem exit = new JMenuItem("Exit");
	private JMenuItem data = new JMenuItem("Load Data...");
	private JMenuItem go = new JMenuItem("Go");
	private MachineView machineView;
	
	public MenuBarBuilder(MachineView machineView) {
		this.machineView = machineView;
		machineView.addObserver(this);
	}
	
	public JMenu createFileMenu() {
		JMenu returnMenu = new JMenu("File");
		returnMenu.setMnemonic(KeyEvent.VK_F);
		assemble.setMnemonic(KeyEvent.VK_A);
		assemble.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_A, ActionEvent.CTRL_MASK));
		assemble.addActionListener(e -> machineView.assembleFile());
		returnMenu.add(assemble);
		load.setMnemonic(KeyEvent.VK_L);
		load.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_L, ActionEvent.CTRL_MASK));
		load.addActionListener(e -> machineView.LoadCode());
		returnMenu.add(load);
		returnMenu.addSeparator(); // puts a line across the menu
		data.setMnemonic(KeyEvent.VK_L);
		data.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_L, ActionEvent.CTRL_MASK));
		data.addActionListener(e -> machineView.loadData());
		returnMenu.add(data);
		returnMenu.addSeparator(); // puts a line across the menu
		exit.setMnemonic(KeyEvent.VK_E);
		exit.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_E, ActionEvent.CTRL_MASK));
		exit.addActionListener(e -> machineView.exit());
		returnMenu.add(exit);
		
		return returnMenu;
	}
	
	public JMenu createExecuteMenu() {
		JMenu menu = new JMenu("Execute");
		menu.setMnemonic(KeyEvent.VK_X);
		go.setMnemonic(KeyEvent.VK_G);
		go.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_G, ActionEvent.CTRL_MASK));
		go.addActionListener(e -> machineView.execute());
		menu.add(go);
		return menu;
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		assemble.setEnabled(machineView.getState().getAssembleFileActive());
		load.setEnabled(machineView.getState().getLoadFileActive());
		go.setEnabled(machineView.getState().getStepActive());
	}	
}