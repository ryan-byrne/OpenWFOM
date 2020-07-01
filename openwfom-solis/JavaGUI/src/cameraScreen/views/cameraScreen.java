package cameraScreen.views;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public class cameraScreen extends JFrame {
	
	
	protected static final Object[] String = null;
	private JPanel contentPane;
	List<String> orderList = new ArrayList<String>();
	String orderString = new String();
	int arduinoState = 0;
	int ledState = 0;
	int solisState = 0;
	int mode = 1;
	boolean ledOn[] = {false, false, false, false};
	String colors[] = {"Blue","Green","Lime","Red"};
	String mouse = new String();
	String uni = new String();
	boolean readyToDeploy = false;
	private JTextField framerate;
	private JTextField setHeight;
	private JTextField exposureTime;
	private JTextField setWidth;
	private JTextField setBottom;
	private JTextField setLeft;

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
					cameraScreen frame = new cameraScreen();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 * @throws IOException 
	 */
	public cameraScreen() throws IOException {
		setResizable(false);
		initComponents();
	}
	private void initComponents() throws IOException {
		setTitle("Camera Settings");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 330, 254);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		final JSlider binning = new JSlider();
		binning.setSnapToTicks(true);
		binning.setPaintTicks(true);
		binning.setBounds(15, 45, 43, 111);
		binning.setToolTipText("");
		binning.setOrientation(SwingConstants.VERTICAL);
		binning.setMajorTickSpacing(1);
		binning.setMaximum(4);
		binning.setMinimum(1);
		
		JLabel lblBinning = new JLabel("Binning");
		lblBinning.setBounds(42, 30, 34, 14);
		
		JLabel lblSetFramerate = new JLabel("Set Framerate (fps)");
		lblSetFramerate.setBounds(110, 30, 95, 14);
		lblSetFramerate.setEnabled(false);
		
		framerate = new JTextField();
		framerate.setHorizontalAlignment(SwingConstants.CENTER);
		framerate.setBounds(110, 45, 86, 20);
		framerate.setEnabled(false);
		framerate.setText(readSettings()[1]);
		framerate.setColumns(10);
		
		JLabel lblSetHeight = new JLabel("Set Height");
		lblSetHeight.setBounds(214, 30, 50, 14);
		
		setHeight = new JTextField();
		setHeight.setHorizontalAlignment(SwingConstants.CENTER);
		setHeight.setBounds(214, 45, 86, 20);
		setHeight.setText("2048");
		setHeight.setColumns(10);
		
		JLabel lblExposureTimes = new JLabel("Exposure Time (s)");
		lblExposureTimes.setBounds(110, 71, 86, 14);
		
		exposureTime = new JTextField();
		exposureTime.setHorizontalAlignment(SwingConstants.CENTER);
		exposureTime.setBounds(110, 91, 86, 20);
		exposureTime.setText(readSettings()[0]);
		exposureTime.setColumns(10);
		
		JLabel lblSetWidth = new JLabel("Set Width");
		lblSetWidth.setBounds(214, 71, 47, 14);
		
		setWidth = new JTextField();
		setWidth.setHorizontalAlignment(SwingConstants.CENTER);
		setWidth.setBounds(214, 91, 86, 20);
		setWidth.setText("2048");
		setWidth.setColumns(10);
		
		JLabel lblx = new JLabel("1x1");
		lblx.setBounds(68, 45, 18, 14);
		
		JLabel lblx_1 = new JLabel("2x2");
		lblx_1.setBounds(68, 77, 18, 14);
		
		JLabel lblx_2 = new JLabel("4x4");
		lblx_2.setBounds(68, 110, 18, 14);
		
		JLabel lblx_3 = new JLabel("8x8");
		lblx_3.setBounds(68, 142, 18, 14);
		
		final JButton btnContinue = new JButton("Continue");
		btnContinue.setEnabled(false);
		btnContinue.setBounds(168, 180, 86, 23);
		btnContinue.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					writeSettings(
							Integer.toString((int) (16*Math.exp(-0.693*binning.getValue()))),
							framerate.getText(),
							setHeight.getText(),
							exposureTime.getText(),
							setWidth.getText(),
							setBottom.getText(),
							setLeft.getText(),
							true
							);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.getMessage();
				}
				System.exit(0);
			}

		});
		JLabel lblStrobeOrder = new JLabel();
		lblStrobeOrder.setBounds(264, 235, 0, 0);
		contentPane.setLayout(null);
		contentPane.add(btnContinue);
		contentPane.add(lblStrobeOrder);
		contentPane.add(binning);
		contentPane.add(lblx_2);
		contentPane.add(lblx_1);
		contentPane.add(lblx_3);
		contentPane.add(lblx);
		contentPane.add(lblBinning);
		contentPane.add(exposureTime);
		contentPane.add(lblExposureTimes);
		contentPane.add(framerate);
		contentPane.add(lblSetFramerate);
		contentPane.add(setHeight);
		contentPane.add(setWidth);
		contentPane.add(lblSetWidth);
		contentPane.add(lblSetHeight);;
		
		JLabel lblBottom = new JLabel("Bottom");
		lblBottom.setBounds(110, 116, 86, 14);
		contentPane.add(lblBottom);
		
		setBottom = new JTextField();
		setBottom.setHorizontalAlignment(SwingConstants.CENTER);
		setBottom.setText("1");
		setBottom.setColumns(10);
		setBottom.setBounds(110, 136, 86, 20);
		contentPane.add(setBottom);
		
		JLabel lblLeft = new JLabel("Left");
		lblLeft.setBounds(214, 116, 86, 14);
		contentPane.add(lblLeft);
		
		setLeft = new JTextField();
		setLeft.setHorizontalAlignment(SwingConstants.CENTER);
		setLeft.setText("1");
		setLeft.setColumns(10);
		setLeft.setBounds(214, 136, 86, 20);
		contentPane.add(setLeft);
		
		JButton btnPreview = new JButton("Preview");
		btnPreview.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e1) {
				try {
					// Write the entered settings to settings.txt
					btnContinue.setEnabled(false);
					writeSettings(
							Integer.toString((int) (16*Math.exp(-0.693*binning.getValue()))),
							framerate.getText(),
							setHeight.getText(),
							exposureTime.getText(),
							setWidth.getText(),
							setBottom.getText(),
							setLeft.getText(),
							false
							);
					btnContinue.setEnabled(true);
				} catch (Exception e3) {
					// TODO Auto-generated catch block
					System.out.println(e3.getMessage());
				}
			}
		});
		btnPreview.setBounds(72, 180, 86, 23);
		contentPane.add(btnPreview);
	}


	private java.lang.String[] readSettings() throws IOException {
		// TODO Auto-generated method stub
		FileReader fr = new FileReader("../resources/solis_scripts/settings.txt");
		BufferedReader br = new BufferedReader(fr);
		String line = null;
		int count = 0;
		String[] settings = new String[2];
		while((line= br.readLine()) != null) {
			if (count == 5) {
				settings[0] = line;
			}
			else if (count == 6){
				settings[1] = line;
			}
			count++;
		}
		fr.close();
		br.close();
		return settings;
		
	}

	private void writeSettings(String b, String f, String h, String e, String w, String btm, String lft, boolean deploy) throws Exception {

		
		PrintWriter writer = new PrintWriter("../resources/solis_scripts/settings.txt", "UTF-8");
		writer.println(b);
		writer.println(h);
		writer.println(btm);
		writer.println(w);
		writer.println(lft);
		writer.println(e);
		writer.println(f);
		writer.println("5.00");
		writer.println("run");
		writer.println("/CCD");
		writer.println("1");
		if (deploy == true) {
			writer.println("deployed");
		}
		else {
	
		}
		writer.close();
		
	}
	
	protected JSONObject readJSON(String file) throws FileNotFoundException, JSONException {
		FileReader r = new FileReader(file+".json");
		JSONTokener t = new JSONTokener(r);
		JSONObject obj = new JSONObject(t);
		return obj;
	}
	
}


