package org.aiwolf.client.ui;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

class NextButtonPanel extends JPanel implements ActionListener, WaitListener {
	private static final long DEFAULT_WAIT_TIME = 500;
	protected boolean step;
	protected boolean skip;

	
	protected long waitTime = DEFAULT_WAIT_TIME;
	protected JButton autoButton;
	protected JButton nextButton;
	protected JButton skipAllButton;
	protected JPanel stepActionPanel;
	protected boolean isAlwaysAuto = false;
	
	public NextButtonPanel(){

		nextButton = new JButton("NEXT");
		nextButton.addActionListener(this);
		
		autoButton = new JButton("Auto");
		autoButton.addActionListener(this);

		skipAllButton = new JButton("SKIP ALL");
		skipAllButton.addActionListener(this);
//		stepActionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
//		stepActionPanel.add(nextButton);
//		stepActionPanel.add(autoButton);
//		stepActionPanel.add(skipAllButton);
		setLayout(new FlowLayout(FlowLayout.CENTER));
		add(nextButton);
		add(autoButton);
		add(skipAllButton);
		step = false;
		skip = false;
		isAlwaysAuto = false;
//		add(stepActionPanel);
//		setVisible(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == nextButton){
			step = true;
			nextButton.setEnabled(false);
		}
		else if(e.getSource() == autoButton){
			if(!skip){
				auto(true);
			}
			else{
				auto(false);
			}
		}
		else if(e.getSource() == skipAllButton){
//			step = true;
//			skip = true;
			waitTime = 0;
			auto(true);
		}
		
	}

	/**
	 * @param setAuto
	 */
	protected void auto(boolean setAuto) {
		if(isAlwaysAuto){
			setAuto = true;
		}
		if(!setAuto){
			skip = false;
			step = false;
			waitTime = DEFAULT_WAIT_TIME;
			autoButton.setText("Auto");
		}
		else{
			step = true;
			skip = true;
			autoButton.setText("Stop");
		}
	}
	

	/**
	 * Wait until next button is pushed
	 */
	@Override
	public void waitForNext() {
		if(skip){
			waitSecond();
			return;
		}
		step = false;
		nextButton.setEnabled(true);
		while(!step){
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		step = false;
		nextButton.setEnabled(false);
	}

	protected void waitSecond(){
		try {
			Thread.sleep(waitTime);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void setAlwaysAuto(boolean b) {
		isAlwaysAuto = true;
		waitTime = 50;
		auto(true);
	}
}
