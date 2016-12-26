package pippin;

import java.awt.Color;
import java.awt.GridLayout;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JSlider;

public class ControlPanel implements Observer {
	private MachineView machineView;
	private JButton stepButton = new JButton("Step");
	private JButton clearButton = new JButton("Clear");
	private JButton runButton = new JButton("Run/Pause");;
	private JButton reloadButton = new JButton("Reload");;

	public ControlPanel(MachineView machineView) {
		this.machineView = machineView;
		machineView.addObserver(this);
	}

	public JComponent createControlDisplay() {
		JPanel returnPanel = new JPanel();
		returnPanel.setLayout(new GridLayout(1,0));
		stepButton.setBackground(Color.WHITE);
		stepButton.addActionListener(e -> machineView.step());
		returnPanel.add(stepButton);
		clearButton.setBackground(Color.WHITE);
		clearButton.addActionListener(e -> machineView.clearAll());
		returnPanel.add(clearButton);
		runButton.setBackground(Color.WHITE);
		runButton.addActionListener(e -> machineView.toggleAutoStep());
		returnPanel.add(runButton);
		reloadButton.setBackground(Color.WHITE);
		reloadButton.addActionListener(e -> machineView.reload());
		returnPanel.add(reloadButton);
		JSlider slider = new JSlider(5,1000);
		slider.addChangeListener(e -> machineView.setPeriod(slider.getValue()));
		returnPanel.add(slider);
		return returnPanel;
	}

	@Override
	public void update(Observable arg0, Object arg1) 
	{
		runButton.setEnabled(machineView.getState().getRunPauseActive());
		stepButton.setEnabled(machineView.getState().getStepActive());
		clearButton.setEnabled(machineView.getState().getClearActive());
		reloadButton.setEnabled(machineView.getState().getReloadActive());
	}
	
}
