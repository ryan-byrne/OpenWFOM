package infoScreen.views;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public class infoScreen extends JFrame {

	private JPanel contentPane;
	private JTextField uniField;
	private JLabel lblEnterTheMouse;
	private JTextField mouseField;

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
					infoScreen frame = new infoScreen();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 * @throws JSONException 
	 */
	public infoScreen() throws JSONException {
		setResizable(false);
		setTitle("Info");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 147, 375);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		uniField = new JTextField();
		uniField.setColumns(10);
		
		JLabel lblUni = new JLabel("Enter Your UNI:");
	
		
		lblEnterTheMouse = new JLabel("Enter the Mouse Name:");
		
		mouseField = new JTextField();
		mouseField.setColumns(10);
		final JLabel lblSelectMouse = new JLabel("Select the Mouse");
		FileReader reader;
		JSONArray mice = new JSONArray();
		JSONArray settings = new JSONArray();
		try {
			System.out.println(Paths.get(".").toAbsolutePath().normalize().toString());
			reader = new FileReader("archive.json");
			JSONTokener tokener = new JSONTokener(reader);
			JSONObject obj = new JSONObject(tokener);
			mice = obj.getJSONObject("mice").names();
			settings = obj.getJSONObject("settings").names();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String[] mice_names = new String[mice.length()];
		for (int i=0; i<mice.length(); i++) {
		    mice_names[i] = mice.getString(i);
		}
		
		String[] settings_names = new String[settings.length()];
		for (int i=0; i<settings.length(); i++) {
			settings_names[i] = settings.getString(i);
		}
		final JCheckBox chckbxNewMouse = new JCheckBox("New Mouse?");
		
		final JComboBox comboboxMouse = new JComboBox();
		comboboxMouse.setModel(new DefaultComboBoxModel(mice_names));
		chckbxNewMouse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (chckbxNewMouse.isSelected()) {
					comboboxMouse.setEnabled(false);
					lblSelectMouse.setEnabled(false);
					mouseField.setEnabled(true);
					lblEnterTheMouse.setEnabled(true);
				}
				else {
					comboboxMouse.setEnabled(true);
					lblSelectMouse.setEnabled(true);
					mouseField.setEnabled(false);
					lblEnterTheMouse.setEnabled(false);
				}
			}
		});

		mouseField.setEnabled(false);
		lblEnterTheMouse.setEnabled(false);
		
		final JComboBox comboboxSettings = new JComboBox();
		comboboxSettings.setModel(new DefaultComboBoxModel(settings_names));
		comboboxSettings.setEnabled(false);
		final JLabel lblSelectTheSettings = new JLabel("Select the Settings");
		lblSelectTheSettings.setEnabled(false);
		
		final JCheckBox useExistingSettings = new JCheckBox("Use Existing Settings");
		JButton btnContinue = new JButton("Continue");
		btnContinue.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					String mouse = new String();
					String uni = uniField.getText();
					JSONObject archive = readJSON("archive");
					JSONObject settings = new JSONObject();
					JSONObject info = new JSONObject();
					
					if (chckbxNewMouse.isSelected()){
						mouse = mouseField.getText();
						JSONObject mice = archive.getJSONObject("mice");
						JSONObject last_trial = new JSONObject();
						last_trial.put("last_trial", 0);
						mice.put(mouse, last_trial);
						archive.put("mice", mice);
						writeJSON(archive, "archive");
					}
					else {
						mouse = comboboxMouse.getSelectedItem().toString();
					}
					
					info.put("uni", uni);
					info.put("mouse", mouse);
					Date date = new Date();
					SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
					info.put("timestamp", formatter.format(date).toString());
					
					if (useExistingSettings.isSelected()) {
						// If Existing Settings are to be used
						String settingsName = comboboxSettings.getSelectedItem().toString();
						settings.put("camera", settingsName);
						settings.put("info", info);
					}
					else {
						settings.put("info", info);
						
					}
					writeJSON(settings, "settings");
					System.exit(0);
				} catch (IOException | JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		useExistingSettings.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (useExistingSettings.isSelected()) {
					comboboxSettings.setEnabled(true);
					lblSelectTheSettings.setEnabled(true);
				}
				else {
					comboboxSettings.setEnabled(false);
					lblSelectTheSettings.setEnabled(false);

				}
			}
		});
		
				
		JSeparator separator = new JSeparator();
		
		JSeparator separator_1 = new JSeparator();
		
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(25)
					.addComponent(btnContinue)
					.addContainerGap(27, Short.MAX_VALUE))
				.addGroup(gl_contentPane.createSequentialGroup()
					.addComponent(separator, GroupLayout.DEFAULT_SIZE, 123, Short.MAX_VALUE)
					.addGap(40))
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(14)
							.addComponent(separator_1, GroupLayout.PREFERRED_SIZE, 111, GroupLayout.PREFERRED_SIZE))
						.addComponent(useExistingSettings, Alignment.LEADING))
					.addContainerGap(38, Short.MAX_VALUE))
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(13)
					.addComponent(lblSelectTheSettings, GroupLayout.PREFERRED_SIZE, 98, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(42, Short.MAX_VALUE))
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(14)
					.addComponent(comboboxSettings, GroupLayout.PREFERRED_SIZE, 96, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(43, Short.MAX_VALUE))
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(23)
					.addComponent(mouseField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(48, Short.MAX_VALUE))
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblEnterTheMouse)
					.addContainerGap(34, Short.MAX_VALUE))
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(18)
					.addComponent(comboboxMouse, GroupLayout.PREFERRED_SIZE, 96, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(43, Short.MAX_VALUE))
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(25)
					.addComponent(lblSelectMouse)
					.addContainerGap(50, Short.MAX_VALUE))
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(24)
					.addComponent(chckbxNewMouse)
					.addContainerGap(48, Short.MAX_VALUE))
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(23)
					.addComponent(uniField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(48, Short.MAX_VALUE))
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(28)
					.addComponent(lblUni)
					.addContainerGap(53, Short.MAX_VALUE))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblUni)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(uniField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(11)
					.addComponent(separator, GroupLayout.PREFERRED_SIZE, 2, GroupLayout.PREFERRED_SIZE)
					.addGap(7)
					.addComponent(chckbxNewMouse)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblSelectMouse)
					.addGap(2)
					.addComponent(comboboxMouse, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblEnterTheMouse)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(mouseField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(separator_1, GroupLayout.PREFERRED_SIZE, 2, GroupLayout.PREFERRED_SIZE)
					.addGap(7)
					.addComponent(useExistingSettings)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblSelectTheSettings)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(comboboxSettings, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnContinue)
					.addContainerGap(33, Short.MAX_VALUE))
		);
		contentPane.setLayout(gl_contentPane);
	}

	protected void writeJSON(JSONObject o, String file) throws IOException {
		FileWriter f = new FileWriter(file+".json");
		f.write(o.toString());
		f.flush();
		f.close();
	}

	protected JSONObject readJSON(String file) throws FileNotFoundException, JSONException {
		FileReader r = new FileReader(file+".json");
		JSONTokener t = new JSONTokener(r);
		JSONObject obj = new JSONObject(t);
		return obj;
	}
}