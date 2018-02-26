package tela;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import contato.Contato;

public class TelaAdicionarContato extends JDialog {

	private JTextField txtIp;
	private JTextField txtNome;
	/**
	 * Launch the application
	 * @param args
	 */
	public static void main(String args[]) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TelaAdicionarContato dialog = new TelaAdicionarContato();
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
	public TelaAdicionarContato() {
		super();
		getContentPane().setForeground(new Color(0, 0, 128));
		setTitle("Adicionar Contato");
		getContentPane().setLayout(null);
		setBounds(100, 100, 483, 471);
		setResizable(false);

		final JPanel panel = new JPanel();
		panel.setForeground(new Color(0, 0, 128));
		panel.setLayout(null);
		panel.setBorder(new LineBorder(Color.black, 1, false));
		panel.setBounds(10, 10, 455, 80);
		getContentPane().add(panel);

		txtNome = new JTextField();
		txtNome.setForeground(new Color(0, 0, 128));
		txtNome.setBounds(10, 20, 185, 20);
		panel.add(txtNome);

		txtIp = new JTextField();
		txtIp.setForeground(new Color(0, 0, 128));
		txtIp.setBounds(205, 20, 240, 20);
		panel.add(txtIp);

		final JLabel nomeLabel = new JLabel();
		nomeLabel.setForeground(new Color(0, 0, 128));
		nomeLabel.setFont(new Font("", Font.BOLD, 12));
		nomeLabel.setText("Nome:");
		nomeLabel.setBounds(10, 3, 66, 16);
		panel.add(nomeLabel);

		final JLabel nomeLabel_1 = new JLabel();
		nomeLabel_1.setForeground(new Color(0, 0, 128));
		nomeLabel_1.setFont(new Font("", Font.BOLD, 12));
		nomeLabel_1.setText("IP ou nome na rede:");
		nomeLabel_1.setBounds(208, 3, 122, 16);
		panel.add(nomeLabel_1);

		final JButton adicionarButton = new JButton();
		adicionarButton.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				
				if ((!(txtIp.getText().trim().equals("")))&&(!(txtNome.getText().trim().equals("")))){
					Contato c = new Contato();	
					c.setIp(txtIp.getText().trim());
					c.setNome(txtNome.getText().trim());
					if (c.adicionarContato()){
						JOptionPane.showMessageDialog(null, "Contato inserido com sucesso!");
						txtIp.setText("");
						txtNome.setText("");
					}else{
						JOptionPane.showMessageDialog(null, "Contato não inserido");
					}
				}
				
				
			}
		});
		adicionarButton.setText("Adicionar");
		adicionarButton.setBounds(10, 45, 435, 26);
		setCursor(new Cursor(Cursor.HAND_CURSOR));
		panel.add(adicionarButton);

		final JPanel panel_1 = new JPanel();
		panel_1.setLayout(null);
		panel_1.setBackground(Color.WHITE);
		panel_1.setForeground(Color.WHITE);
		panel_1.setBounds(5, 415, 465, 25);
		getContentPane().add(panel_1);

		final JLabel label = new JLabel();
		label.setForeground(new Color(0, 0, 255));
		label.setFont(new Font("", Font.BOLD, 12));
		label.setText("Esta aplicação usa a porta 85.");
		label.setBounds(6, 4, 215, 16);
		panel_1.add(label);
	
	}

}
