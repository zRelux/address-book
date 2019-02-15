package server;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class ServerGUI extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtPorta;
	private JLabel lblRunning;
	private TCPServer server;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ServerGUI frame = new ServerGUI();
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
	public ServerGUI() {
		setResizable(false);
		setTitle("Server");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(0, 0, 265, 220);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);
		contentPane = new JPanel();
		contentPane.setBackground(SystemColor.inactiveCaption);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblPortaTcp = new JLabel("Porta TCP:");
		lblPortaTcp.setHorizontalAlignment(SwingConstants.CENTER);
		lblPortaTcp.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblPortaTcp.setBounds(10, 11, 105, 30);
		contentPane.add(lblPortaTcp);

		txtPorta = new JTextField();
		txtPorta.setText("50000");
		txtPorta.setColumns(10);
		txtPorta.setBounds(125, 13, 105, 30);
		contentPane.add(txtPorta);

		lblRunning = new JLabel("RUNNING");
		lblRunning.setVisible(false);
		lblRunning.setHorizontalAlignment(SwingConstants.CENTER);
		lblRunning.setForeground(Color.BLACK);
		lblRunning.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblRunning.setBounds(77, 80, 105, 30);
		contentPane.add(lblRunning);

		JButton btnInvia = new JButton("AvviaServer");
		btnInvia.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int port;
				try {
					port = Integer.parseInt(txtPorta.getText());
					server = new TCPServer(port);
					lblRunning.setVisible(true);
					server.start();
					btnInvia.setEnabled(false);
				}catch (Exception e) {
					JOptionPane.showMessageDialog(null, "Errore Porta selezionata sbagliata");
				}
				
			}
		});
		btnInvia.setBounds(77, 157, 105, 23);
		getRootPane().setDefaultButton(btnInvia);
		contentPane.add(btnInvia);

	}
}