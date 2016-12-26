package pippin;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
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

public class CodeViewPanel implements Observer {
	private MachineView machineView;
	private Code code;
	private JScrollPane scroller;
	private JTextField[] codeText = new JTextField[Code.CODE_MAX];
	private int previousColor = -1;
	
	public CodeViewPanel(MachineView p){
		machineView = p;
		machineView.addObserver(this);
	}
	public JComponent createCodeDisplay() {
		JPanel returnPanel = new JPanel();
		JPanel panel = new JPanel();
		JPanel numPanel = new JPanel();
		JPanel sourcePanel = new JPanel();
		returnPanel.setPreferredSize(new Dimension(300,150));;
		returnPanel.setLayout(new BorderLayout());
		panel.setLayout(new BorderLayout());
		numPanel.setLayout(new GridLayout(0,1));
		sourcePanel.setLayout(new GridLayout(0,1));
		for(int i = 0; i < Code.CODE_MAX; i++) {
			numPanel.add(new JLabel(i+": ", JLabel.RIGHT));
			codeText[i] = new JTextField(10);
			sourcePanel.add(codeText[i]);
		}
		Border border = BorderFactory.createTitledBorder(
				BorderFactory.createLineBorder(Color.BLACK), "Code Memory View",
				TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION);
		returnPanel.setBorder(border);

		panel.add(numPanel, BorderLayout.LINE_START);
		panel.add(sourcePanel, BorderLayout.CENTER);
		scroller = new JScrollPane(panel);
		returnPanel.add(scroller);
		return returnPanel;
	}
	@Override
	public void update(Observable arg0, Object arg1) {
		if(arg1 != null && arg1.equals("Load Code")) {
			code = machineView.getCode();
			for(int i = 0; i < Code.CODE_MAX; i++) {
				codeText[i].setText(code.getText(i));
			}	
			previousColor = machineView.getPC();			
			codeText[previousColor].setBackground(Color.YELLOW);
		}	
		if(arg1 != null && arg1.equals("Clear")) {
			for(int i = 0; i < Code.CODE_MAX; i++) {
				codeText[i].setText("");
			}	
			if(previousColor >= 0 && previousColor < Code.CODE_MAX) {
				codeText[previousColor].setBackground(Color.WHITE);
			}
			previousColor = -1;
		}		
		if(this.previousColor >= 0 && previousColor < Code.CODE_MAX) {
			codeText[previousColor].setBackground(Color.WHITE);
			previousColor = machineView.getPC();
			if(this.previousColor >= 0 && previousColor < Code.CODE_MAX) {
				codeText[previousColor].setBackground(Color.YELLOW);
			}
		} 

		if(scroller != null && code != null && machineView!= null) {
			JScrollBar bar= scroller.getVerticalScrollBar();
			int pc = machineView.getPC();
			if(pc < Code.CODE_MAX && codeText[pc] != null) {
				Rectangle bounds = codeText[pc].getBounds();
				bar.setValue(Math.max(0, bounds.y - 15*bounds.height));
			}
		}
	}
}
