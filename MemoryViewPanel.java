package pippin;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

public class MemoryViewPanel implements Observer {
	private MachineView machineView;
	private JScrollPane scroller;
	private JTextField[] dataDecimal = new JTextField[Memory.DATA_SIZE];
	private JTextField[] dataHex = new JTextField[Memory.DATA_SIZE];
	private int lower = -1;
	private int upper = -1;
	private int previousColor = -1;
	
	public MemoryViewPanel(MachineView p, int i, int a){
		machineView = p;
		lower = i;
		upper = a;
		machineView.addObserver(this);
	}
	public JComponent createMemoryDisplay() {
		JPanel returnPanel = new JPanel();
		returnPanel.setLayout(new BorderLayout());
		Border border = BorderFactory.createTitledBorder(
				BorderFactory.createLineBorder(Color.BLACK), 
				"Data Memory View [" + lower + "-" + upper + "]",
				TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION);
		returnPanel.setBorder(border);
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		JPanel numPanel = new JPanel();
		JPanel decimalPanel = new JPanel();
		JPanel hexPanel = new JPanel();
		numPanel.setLayout(new GridLayout(0,1));
		decimalPanel.setLayout(new GridLayout(0,1));
		hexPanel.setLayout(new GridLayout(0,1));
		panel.add(numPanel, BorderLayout.LINE_START);
		panel.add(decimalPanel, BorderLayout.CENTER);
		panel.add(hexPanel, BorderLayout.LINE_END);
		for(int i = lower; i < upper; i++) {
			numPanel.add(new JLabel(i+": ", JLabel.RIGHT));
			dataDecimal[i] = new JTextField(10);
			dataHex[i] = new JTextField(10);
			decimalPanel.add(dataDecimal[i]);
			hexPanel.add(dataHex[i]);
		}
		scroller = new JScrollPane(panel);
		returnPanel.add(scroller);
		return returnPanel;
	}
	@Override
	public void update(Observable arg0, Object arg1) {
		for(int i = lower; i < upper; i++) {
			dataDecimal[i].setText("" + machineView.getData(i));
			dataHex[i].setText(Integer.toHexString(machineView.getData(i)));
		}
		if(arg1 != null && arg1.equals("Clear")) {
			for(int i = lower; i < upper; i++) {
				dataDecimal[i].setText("");
				dataHex[i].setText("");
			}			
			if(lower <= previousColor && previousColor < upper) {
//was for debuggin	System.out.println("\tagain" + previousColor);
				dataDecimal[previousColor].setBackground(Color.WHITE);
				dataHex[previousColor].setBackground(Color.WHITE);
				previousColor = -1;
			}
		} else {
			if(previousColor  >= lower && previousColor < upper) {
				dataDecimal[previousColor].setBackground(Color.WHITE);
				dataHex[previousColor].setBackground(Color.WHITE);
			}
			previousColor = machineView.getChangedIndex();
			if(previousColor  >= lower && previousColor < upper) {
				dataDecimal[previousColor].setBackground(Color.YELLOW);
				dataHex[previousColor].setBackground(Color.YELLOW);
			} 
		}
		if(scroller != null && machineView != null) {
			JScrollBar bar= scroller.getVerticalScrollBar();
			if (machineView.getChangedIndex() >= lower &&
					machineView.getChangedIndex() < upper && 
					dataDecimal[machineView.getChangedIndex()] != null) {
				Rectangle bounds = dataDecimal[machineView.getChangedIndex()].getBounds();
				bar.setValue(Math.max(0, bounds.y - 15*bounds.height));
			}
		}
	}
	
}
