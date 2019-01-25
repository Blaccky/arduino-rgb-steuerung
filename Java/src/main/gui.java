package main;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.border.EmptyBorder;
import com.fazecast.jSerialComm.SerialPort;

public class gui extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1;
	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					gui frame = new gui();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	
	SerialPort ap;
	PrintWriter pw;
	JLabel lblNichtVerbunden;
	
	public gui() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(gui.class.getResource("/images/icon.png")));
		setTitle("Arduino - LED Steuerung");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 700);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		SerialPort[] spl = SerialPort.getCommPorts();
		
		JColorChooser rgb = new JColorChooser();
		rgb.setBackground(new Color(255, 255, 255));
		rgb.setFont(new Font("Lucida Handwriting", Font.PLAIN, 12));
		rgb.setForeground(Color.WHITE);
		rgb.setLocation(221, 214);
		rgb.setSize(483, 235);
		
		JButton btnSend = new JButton("send");
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Hier wandeln wir die Werte aus dem JColorChosser in Strings um
				String rot = Integer.toString(rgb.getColor().getRed());
				String blau = Integer.toString(rgb.getColor().getBlue());
				String gruen = Integer.toString(rgb.getColor().getGreen());
				
				String rend;
				String bend;
				String gend;
				// Da die Arduino Funktion den gesamten Output in drei Substring teilt die jeweils drei Zeichen lang sind müssen wir für Zahlen a la 10,2,... jeweils eine oder zwei nullen hinzufügen
				if(rot.length() < 3) {
					if(rot.length() < 2) {
						String r1 = "00" + rot;
						rend = r1;
					} else {String r2 = "0" + rot;
					rend = r2;}
				} else { 
				rend = rot;}
				
				if(blau.length() < 3) {
					if(blau.length() < 2) {
						String b1 = "00" + blau;
						bend = b1;
					} else {String b2 = "0" + blau;
					bend = b2;}
				} else bend = blau;
				
				if(gruen.length() < 3) {
					if(gruen.length() < 2) {
						String g1 = "00" + gruen;
						gend = g1;
					} else {String g2 = "0" + gruen;
					gend = g2;}
				} else gend = gruen;
				
				// mittels print & flush senden wir es dem Arduino
				pw.print(rend + gend + bend);
				pw.flush();
				
			
				
			}
		});
		
		JLabel lblLedSteuerung = new JLabel("Arduino - LED Steuerung");
		lblLedSteuerung.setForeground(new Color(0, 0, 128));
		lblLedSteuerung.setFont(new Font("Viner Hand ITC", Font.PLAIN, 35));
		lblLedSteuerung.setBounds(180, 11, 524, 67);
		contentPane.add(lblLedSteuerung);
		
		JLabel label_1 = new JLabel("");
		label_1.setBounds(160, 0, 10, 671);
		contentPane.add(label_1);
		label_1.setBackground(new Color(0, 0, 0));
		btnSend.setBounds(317, 480, 89, 23);
		contentPane.add(btnSend);
		contentPane.add(rgb);
		
		JButton btnGetPanels = new JButton("OFF");
		btnGetPanels.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// 000000000 ist der RGB Code für Schwarz ergo ist die Leiste Aus (da der Arduino keine Output auf die Transistoren gibt kann kein Strom fließen)
				pw.print("000000000");
				pw.flush();
			}
		});
		btnGetPanels.setBounds(530, 480, 89, 23);
		contentPane.add(btnGetPanels);
		
		JPanel panel = new JPanel();
		panel.setBackground(new Color(0, 0, 128));
		panel.setBounds(0, 0, 161, 671);
		contentPane.add(panel);
		panel.setLayout(null);
		
		JComboBox<String> comboBox = new JComboBox<String>();
		comboBox.setBounds(10, 11, 141, 20);
		panel.add(comboBox);
		
		JButton btnConnect = new JButton("Verbinden");
		btnConnect.setBounds(10, 42, 141, 23);
		panel.add(btnConnect);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBackground(new Color(192, 192, 192));
		panel_1.setBounds(150, 634, 644, 37);
		contentPane.add(panel_1);
		panel_1.setLayout(null);
		
		JProgressBar progressBar = new JProgressBar();
		progressBar.setStringPainted(true);
		progressBar.setBounds(488, 11, 146, 14);
		panel_1.add(progressBar);
		
		lblNichtVerbunden = new JLabel("Status: nicht Verbunden");
		lblNichtVerbunden.setBounds(20, 9, 292, 14);
		panel_1.add(lblNichtVerbunden);
		
		JLabel label = new JLabel("");
		label.setIcon(new ImageIcon(gui.class.getResource("/images/logo.png")));
		label.setBounds(160, 0, 634, 639);
		contentPane.add(label);
		btnConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Hier erstellen wir einen neuen Thread damit das Programm Multitaskfähig wird und nichts hängt
				Thread op = new Thread() {
				
				@Override
				public void run() {
					// Hier checken wir ob der ausgewählte SerialPort auch belegt ist
					if(ap != null) {
						System.out.println("versuche verbindung aufzubauen...");
						progressBar.setValue(20);
						ap.setComPortTimeouts(SerialPort.TIMEOUT_SCANNER, 0, 0);
						progressBar.setValue(30);
						// Nun verbinden wir uns mit dem Arduino mit einer Baudrate von 9600
						ap.openPort(9600, 0, 0);
						try {
							// Der Thread muss kurz schlafen damit der Arduino die Verbindung verarbeiten kann
							Thread.sleep(100);
						} catch (InterruptedException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						progressBar.setValue(40);
						// Hier erstellen wir einen PrintWriter mit dem OutputStream von dem SerialPort damit wir dem Arduino Daten senden können
						pw = new PrintWriter(ap.getOutputStream());
						progressBar.setValue(50);
						if(ap.isOpen()) {
							System.out.println("verbindung steht");
							progressBar.setValue(70);
							if(pw != null) {
								// Wenn alles geklappt hat geben wir uns ein kleines Feedback
								System.out.println("PrintWriter steht");
								progressBar.setValue(100);
								lblNichtVerbunden.setText("Status: Verbunden");
								btnConnect.setText("Trennen");
							}
						}
						}
				}
				
			};
			
			// Hier fragen wir ab ob es sich wirklich um einen Arduino handelt
			if(comboBox.getSelectedItem().toString().startsWith("Arduino")) {
				// Hier erstellen wir einen SerialPort aus dem ausgewählten Arduino
				ap = spl[comboBox.getSelectedIndex()];
				
				if(ap.isOpen()) {
					pw.close();
					ap.closePort();
					progressBar.setValue(0);
					lblNichtVerbunden.setText("Status: nicht Verbunden");
					btnConnect.setText("Verbinden");
				} else {
					
					op.start();
					
				progressBar.setValue(10);
				}
				} else lblNichtVerbunden.setText("Status: Wählen Sie ein Arduino Board aus!");
			
			
			
			}
		});
		comboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				
			}
		});
		// Hier fügen wir die Ports zu der JComboBox hinzu
		for(int i = 0; i < spl.length; i++) {
			comboBox.addItem(spl[i].getDescriptivePortName());
		}
		
		
		
		
		
	}
}
