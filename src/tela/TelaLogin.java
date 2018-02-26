package tela;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import contato.Contato;

public class TelaLogin extends JDialog {

	private JTextField txtIp;
	private JTextField txtNome;
	private Contato c;
	public Contato getC() {
		return c;
	}

	public void setC(Contato c) {
		this.c = c;
	}

	/**
	 * Launch the application
	 * @param args
	 */
	public static void main(String args[]) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TelaLogin dialog = new TelaLogin();
					dialog.addWindowListener(new WindowAdapter() {
						public void windowClosing(WindowEvent e) {
							System.exit(0);
						}
					});
					dialog.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the dialog
	 */
	public TelaLogin() {
		super();
		setResizable(false);
		setTitle("Java Lan Messenger");
		getContentPane().setLayout(null);
		setBounds(100, 100, 317, 167);
		setLocationRelativeTo(null);
		setModal(true);
		

		final JPanel panel = new JPanel();
		panel.setBorder(new LineBorder(Color.black, 1, false));
		panel.setLayout(null);
		panel.setBounds(7, 10, 299, 126);
		getContentPane().add(panel);

		txtNome = new JTextField();
		txtNome.setBounds(10, 20, 279, 20);
		panel.add(txtNome);
		txtNome.setText(System.getProperty("user.name"));
		
		final JButton adicionarButton = new JButton();
		adicionarButton.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				if (!((txtNome.getText().trim().equals(""))&&(txtIp.getText().trim().equals("")))){
					c = new Contato();
					c.setNome(txtNome.getText().trim());
					c.setIp(txtIp.getText().trim());					
					TelaLogin.this.setVisible(false);
				}
				
				
			}
		});
		adicionarButton.setText("Entrar");
		adicionarButton.setBounds(10, 88, 279, 26);
		setCursor(new Cursor(Cursor.HAND_CURSOR));
		panel.add(adicionarButton);		

		txtIp = new JTextField();
		txtIp.addKeyListener(new KeyAdapter() {
			public void keyPressed(final KeyEvent e) {
				
				if (e.getKeyCode() == e.VK_ENTER){
					adicionarButton.doClick();
				}
			}
		});
		txtIp.setBounds(10, 60, 279, 20);
		panel.add(txtIp);
		InetAddress addr;
		try {
			addr = InetAddress.getLocalHost();
			txtIp.setText(addr.getHostName());
		} catch (UnknownHostException e1) {
			 txtIp.setText("SEM REDE");
		}
		

		final JLabel nomeLabel = new JLabel();
		nomeLabel.setText("Nome: ");
		nomeLabel.setBounds(10, 3, 66, 16);
		panel.add(nomeLabel);

		final JLabel nomeLabel_1 = new JLabel();
		nomeLabel_1.setText("IP ou nome na rede:");
		nomeLabel_1.setBounds(10, 43, 122, 16);
		panel.add(nomeLabel_1);


		//
	}

}
