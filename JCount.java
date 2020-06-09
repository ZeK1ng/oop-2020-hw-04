// JCount.java

/*
 Basic GUI/Threading exercise.
*/

import javax.swing.*;

import java.awt.Dimension;
import java.awt.event.*;

public class JCount extends JPanel {
	private static final int COUNTER_NUM = 4;
	private int maxCount = (int) 1e6;
	private Worker wk;
	
	public JCount() {
		// Set the JCount to use Box layout
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		JTextField tf = new JTextField(12);
		tf.setText(""+maxCount);
		add(tf);
		JButton startB = new JButton("Start");
		add(startB);
		JButton stopB = new JButton("Stop");
		add(stopB);
		JLabel lbl = new JLabel("0");
		add(lbl);
		wk = new Worker(maxCount,lbl);
		wk.start();
		addListeners(startB,stopB,tf,lbl);
	}
	
	
	private void addListeners(JButton startB, JButton stopB, JTextField tf, JLabel lbl) {
		startB.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(wk.isAlive()) wk.interrupt();
				if(tf.getText().equals("")) return;
				wk = new Worker(Integer.parseInt(tf.getText()),lbl);
				wk.start();
			}
		});
		stopB.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(wk.isAlive()) wk.interrupt();
			}
		});
	}


	static public void main(String[] args)  {
		// Creates a frame with 4 JCounts in it.
		// (provided)
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				createAndShowGui();
			}
		});
	}
	private static void createAndShowGui() {
		JFrame frame = new JFrame("The Count");
		frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
		
		for(int i =0; i< COUNTER_NUM; i++) {
			frame.add(new JCount());
			if(i <COUNTER_NUM-1)
				frame.add(Box.createRigidArea(new Dimension(0,30)));
		}

		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	private class Worker extends Thread{
		private static final int interval = (int) 1e4;
		private static final int sleep_time = 100;
		private int target_amount;
		private int curr_amount;
		private JLabel lbl;
		public Worker(int num,JLabel lbl) {
			target_amount = num;
			this.lbl = lbl;
			curr_amount=0;
		}
		@Override
		public void run() {
			while(true) {
				if(isInterrupted())break;
				if(curr_amount == target_amount) {
					SwingUtilities.invokeLater(new Runnable() {
						@Override
						public void run() {
							lbl.setText(curr_amount+"");
						}
					});
					break;
				}else if( curr_amount % interval ==0) {
					SwingUtilities.invokeLater(new Runnable() {
						@Override
						public void run() {
							lbl.setText(curr_amount+"");
						}
					});
					try {
						sleep(sleep_time);
					}catch(InterruptedException e) {
						break;
					}
				}
				curr_amount++;
			}
		}
	}
}

