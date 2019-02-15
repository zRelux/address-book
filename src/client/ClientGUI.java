package client;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class ClientGUI extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtLocalhost;
	private JTextField txtPorta;
	private JTextField txtMessaggio;
	private TCPClient client = null;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ClientGUI frame = new ClientGUI();
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
	public ClientGUI() {
		setResizable(false);
		setTitle("Client");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(0, 0, 475, 370);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);
		contentPane = new JPanel();
		contentPane.setBackground(SystemColor.inactiveCaption);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblIpServer = new JLabel("IP server:");
		lblIpServer.setHorizontalAlignment(SwingConstants.CENTER);
		lblIpServer.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblIpServer.setBounds(10, 11, 105, 30);
		contentPane.add(lblIpServer);

		txtLocalhost = new JTextField();
		txtLocalhost.setText("localhost");
		txtLocalhost.setBounds(125, 13, 105, 30);
		contentPane.add(txtLocalhost);
		txtLocalhost.setColumns(10);

		JLabel lblPortaTcp = new JLabel("Porta TCP:");
		lblPortaTcp.setHorizontalAlignment(SwingConstants.CENTER);
		lblPortaTcp.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblPortaTcp.setBounds(240, 11, 105, 30);
		contentPane.add(lblPortaTcp);

		txtPorta = new JTextField();
		txtPorta.setText("50000");
		txtPorta.setColumns(10);
		txtPorta.setBounds(355, 13, 105, 30);
		contentPane.add(txtPorta);

		txtMessaggio = new JTextField();
		txtMessaggio.setBounds(125, 257, 335, 30);
		contentPane.add(txtMessaggio);
		txtMessaggio.setColumns(10);

		JLabel lblMessaggio = new JLabel("Messaggio");
		lblMessaggio.setHorizontalAlignment(SwingConstants.CENTER);
		lblMessaggio.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblMessaggio.setBounds(10, 255, 105, 30);
		contentPane.add(lblMessaggio);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setBounds(10, 72, 450, 174);
		contentPane.add(scrollPane);

		JTextArea txtArea = new JTextArea();
		scrollPane.setViewportView(txtArea);

		JButton btnInvia = new JButton("Invia");
		btnInvia.addActionListener(new ActionListener() {
			private String text;

			public void actionPerformed(ActionEvent arg0) {
				if (client == null) {
					try {
						client = new TCPClient(txtLocalhost.getText(), Integer.parseInt(txtPorta.getText()));
					} catch (NumberFormatException e) {
						e.printStackTrace();
					} catch (IOException e) {
						text = "Server Chiuso riattivare";
					}
					txtPorta.setEnabled(false);
					txtLocalhost.setEnabled(false);
					try {
						text = client.comunicaServer(txtMessaggio.getText());
					} catch (Exception e) {
						text = "Server Chiuso riattivare";
					}

					if (text.equals("-e-n-d") || text.equals("Server Chiuso riattivare")) {
						client = null;
						txtPorta.setEnabled(true);
						txtLocalhost.setEnabled(true);
					}
					txtArea.setText(text);
				} else {
					try {
						text = client.comunicaServer(txtMessaggio.getText());
					} catch (Exception e) {
						text = "Server Chiuso riattivare";
					}
					if (text.equals("-e-n-d") || text.equals("Server Chiuso riattivare")) {
						client = null;
						txtPorta.setEnabled(true);
						txtLocalhost.setEnabled(true);
					}
					txtArea.setText(text);
				}
			}
		});
		btnInvia.setBounds(192, 303, 89, 23);
		getRootPane().setDefaultButton(btnInvia);
		contentPane.add(btnInvia);

	}
}