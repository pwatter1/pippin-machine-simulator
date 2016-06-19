package pippin;

import java.awt.GridLayout;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class CPUViewPanel implements Observer {
	private MachineView machineView;
	private JTextField acc = new JTextField();
	private JTextField pc = new JTextField();
	
	public CPUViewPanel(MachineView p){
		machineView = p;
		machineView.addObserver(this);
	}
	public JComponent createCPUDisplay() {
		JPanel returnPanel = new JPanel();
		returnPanel.setLayout(new GridLayout(1,0));
		returnPanel.add(new JLabel("Accumulator: ", JLabel.RIGHT));
		returnPanel.add(acc);
		returnPanel.add(new JLabel("Program Counter: ", JLabel.RIGHT));
		returnPanel.add(pc);
		return returnPanel;
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		if(machineView != null) {
			acc.setText("" + machineView.getAccum());
			pc.setText("" + machineView.getPC());
		}
	}

}
