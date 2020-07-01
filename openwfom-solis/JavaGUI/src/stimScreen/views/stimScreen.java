package stimScreen.views;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import javax.swing.JCheckBox;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import java.awt.event.InputMethodListener;
import java.awt.event.InputMethodEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class stimScreen extends JFrame {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5172397928490535869L;
	private JPanel contentPane;
	List<String> orderList = new ArrayList<String>();
	String orderString = new String();
	int mode = 1;
	boolean stimStatus;
	boolean ledOn[] = {false, false, false, false};
	String colors[] = {"Blue","Green","Lime","Red"};
	String mouse = new String();
	String uni = new String();
	boolean readyToDeploy = false;
	private JTextField runningTime;
	private JTextField numStim;
	private JTextField numRuns;
	private JTextField preStim;
	private JTextField postStim;
	private JTextField stimLength;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Throwable e) {
			e.printStackTrace();
		}
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					stimScreen frame = new stimScreen();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public stimScreen() {
		setResizable(false);
		initComponents();
	}
	private void initComponents() {
		setTitle("Stim Settings");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 330, 252);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JButton btnDeploySettingsTo_1 = new JButton("Continue");
		btnDeploySettingsTo_1.setBounds(74, 185, 175, 23);
		btnDeploySettingsTo_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String r = numRuns.getText();
				String len = runningTime.getText();
				JSONObject stim = new JSONObject();
				JSONObject run = new JSONObject();
				if (stimStatus == false) {
					try {
						run.put("num_run", r);
						run.put("run_len", len);
						stim.put("NO", "STIM");
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else {
					try {
						stim.put("pre_stim", preStim.getText());
						stim.put("stim_length", stimLength.getText());
						stim.put("post_stim", postStim.getText());
						stim.put("num_of_stims", numStim.getText());
						run.put("num_run", numStim.getText());
						run.put("run_len", len);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				try {
					writeJsonSettings(run, stim);
					System.exit(0);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					System.out.println(e1.getMessage());
				}
			}

		});
		JLabel lblStrobeOrder = new JLabel();
		lblStrobeOrder.setBounds(264, 235, 0, 0);
		contentPane.setLayout(null);
		contentPane.add(btnDeploySettingsTo_1);
		contentPane.add(lblStrobeOrder);;
		
		JCheckBox chckbxStim = new JCheckBox("Stim Functionality");
		chckbxStim.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent e) {
				if (e.getNewValue().toString() == "CHECKEDHOT") {
					stimStatus = true;
					float rt = Float.parseFloat(numStim.getText())*(Float.parseFloat(preStim.getText())+Float.parseFloat(stimLength.getText())+Float.parseFloat(postStim.getText()));
					runningTime.setEnabled(false);
					runningTime.setText(Float.toString(rt));
					numRuns.setEnabled(false);
					numStim.setEnabled(true);
					preStim.setEnabled(true);
					postStim.setEnabled(true);
					stimLength.setEnabled(true);
				}
				else if (e.getNewValue().toString() == "UNCHECKEDHOT") {
					stimStatus = false;
					runningTime.setEnabled(true);
					numRuns.setEnabled(true);
					numStim.setEnabled(false);
					preStim.setEnabled(false);
					postStim.setEnabled(false);
					stimLength.setEnabled(false);
					
				}
				else {
					return;
				}
			}
		});
		chckbxStim.setBounds(107, 7, 109, 23);
		contentPane.add(chckbxStim);
		
		runningTime = new JTextField();
		runningTime.setHorizontalAlignment(SwingConstants.CENTER);
		runningTime.setText("5.000");
		runningTime.setColumns(10);
		runningTime.setBounds(65, 136, 86, 20);
		contentPane.add(runningTime);
		
		numStim = new JTextField();
		numStim.setHorizontalAlignment(SwingConstants.CENTER);
		numStim.setEnabled(false);
		numStim.setText("1");
		numStim.setColumns(10);
		numStim.setBounds(138, 37, 47, 20);
		contentPane.add(numStim);
		
		JLabel lblStimLengths = new JLabel("Running Time (s)");
		lblStimLengths.setBounds(65, 160, 86, 14);
		contentPane.add(lblStimLengths);
		
		JLabel lblNumberOfStims = new JLabel("Number of Stims");
		lblNumberOfStims.setBounds(118, 61, 86, 14);
		contentPane.add(lblNumberOfStims);
		
		numRuns = new JTextField();
		numRuns.setHorizontalAlignment(SwingConstants.CENTER);
		numRuns.setText("1");
		numRuns.setColumns(10);
		numRuns.setBounds(175, 136, 86, 20);
		contentPane.add(numRuns);
		
		JLabel numRunslbl = new JLabel("# of Runs");
		numRunslbl.setHorizontalAlignment(SwingConstants.CENTER);
		numRunslbl.setBounds(175, 159, 86, 14);
		contentPane.add(numRunslbl);
		
		preStim = new JTextField();
		preStim.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				float rt = Float.parseFloat(numStim.getText())*(Float.parseFloat(preStim.getText())+Float.parseFloat(stimLength.getText())+Float.parseFloat(postStim.getText()));
				runningTime.setText(Float.toString(rt));
			}
		});
		preStim.setText("6.00");
		preStim.setHorizontalAlignment(SwingConstants.CENTER);
		preStim.setEnabled(false);
		preStim.setColumns(10);
		preStim.setBounds(10, 88, 86, 20);
		contentPane.add(preStim);
		
		JLabel lblPrestim = new JLabel("Pre-Stim (s)");
		lblPrestim.setHorizontalAlignment(SwingConstants.CENTER);
		lblPrestim.setBounds(10, 111, 86, 14);
		contentPane.add(lblPrestim);
		
		JLabel lblPoststim = new JLabel("Post-Stim (s)");
		lblPoststim.setHorizontalAlignment(SwingConstants.CENTER);
		lblPoststim.setBounds(211, 111, 86, 14);
		contentPane.add(lblPoststim);
		
		postStim = new JTextField();
		postStim.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				float rt = Float.parseFloat(numStim.getText())*(Float.parseFloat(preStim.getText())+Float.parseFloat(stimLength.getText())+Float.parseFloat(postStim.getText()));
				runningTime.setText(Float.toString(rt));
			}
		});
		postStim.setText("12.00");
		postStim.setHorizontalAlignment(SwingConstants.CENTER);
		postStim.setEnabled(false);
		postStim.setColumns(10);
		postStim.setBounds(211, 88, 86, 20);
		contentPane.add(postStim);
		
		JLabel lblStims = new JLabel("Stim (s)");
		lblStims.setHorizontalAlignment(SwingConstants.CENTER);
		lblStims.setBounds(116, 109, 86, 14);
		contentPane.add(lblStims);
		
		stimLength = new JTextField();
		stimLength.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				float rt = Float.parseFloat(numStim.getText())*(Float.parseFloat(preStim.getText())+Float.parseFloat(stimLength.getText())+Float.parseFloat(postStim.getText()));
				runningTime.setText(Float.toString(rt));
			}
		});
		stimLength.setText("4.00");
		stimLength.setHorizontalAlignment(SwingConstants.CENTER);
		stimLength.setEnabled(false);
		stimLength.setColumns(10);
		stimLength.setBounds(116, 86, 86, 20);
		contentPane.add(stimLength);
		numStim.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent arg0) {
				float rt = Float.parseFloat(numStim.getText())*(Float.parseFloat(preStim.getText())+Float.parseFloat(stimLength.getText())+Float.parseFloat(postStim.getText()));
				runningTime.setText(Float.toString(rt));
				numRuns.setText(numStim.getText());
			}
		});
	}
	
	private void writeJsonSettings(JSONObject run, JSONObject stim) throws Exception {
		FileReader reader;
		reader = new FileReader("settings.json");
		JSONTokener tokener = new JSONTokener(reader);
		JSONObject settings = new JSONObject(tokener);
		reader.close();
		settings.put("run", run);
		settings.put("stim", stim);
		PrintWriter out = new PrintWriter("settings.json");
		out.println(settings.toString());
		out.close();
	}
}
