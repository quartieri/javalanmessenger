package tela;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.border.LineBorder;

import mensageiro.Mensageiro;
import mensagem.Mensagem;

import contato.Contato;

public class TelaComunicator extends JFrame {

	private JTextArea txtMensagensRecebidas;

	private Contato destinatario;
	private Contato remetente;
	public boolean ehResposta = true; 

	public Contato getRemetente() {
		return remetente;
	}

	public void setRemetente(Contato remetente) {
		this.remetente = remetente;
	}

	private Mensageiro recebedor;

	public void recebeMensagem(Mensagem m){

		this.txtMensagensRecebidas.setText(this.txtMensagensRecebidas.getText()+"\n"+this.destinatario.getNome()+" disse: "+m.getTexto());		

	}

	public static void main(String args[]) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					
					TelaComunicator frame = new TelaComunicator();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame
	 */

	public TelaComunicator(Contato remetente, Contato destinatario){
		this();
		this.setRemetente(remetente);
		this.setDestinatario(destinatario);
	}	

	public TelaComunicator() {
		super();

		recebedor = new Mensageiro();
		setTitle("Conversa");
		getContentPane().setLayout(null);
		setBounds(100, 100, 500, 348);
		setResizable(false);
		setLocationRelativeTo(null);  
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

		final JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBorder(new LineBorder(Color.black, 1, false));
		scrollPane.setBounds(10, 10, 474, 199);
		getContentPane().add(scrollPane);

		txtMensagensRecebidas = new JTextArea();
		txtMensagensRecebidas.setFocusable(false);
		txtMensagensRecebidas.setEditable(false);
		scrollPane.setViewportView(txtMensagensRecebidas);
		addWindowListener(new WindowAdapter() {
			public void windowClosed(final WindowEvent e) {

			}
		});

		final JEditorPane txtMensagemAEnviar = new JEditorPane();
		txtMensagemAEnviar.setFocusTraversalPolicyProvider(true);
		txtMensagemAEnviar.addKeyListener(new KeyAdapter() {
			public void keyPressed(final KeyEvent e) {
				if (e.getKeyCode() == e.VK_ENTER){
					///////////////
				}
			}
		});
		txtMensagemAEnviar.setBorder(new LineBorder(Color.black, 1, false));
		txtMensagemAEnviar.setBounds(10, 215, 383, 97);
		getContentPane().add(txtMensagemAEnviar);

		final JButton btnEnviar = new JButton();
		btnEnviar.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				/////////////
				if ((ehResposta)){
					Mensagem mens = new Mensagem(TelaComunicator.this.remetente,TelaComunicator.this.destinatario);
					mens.setTexto(txtMensagemAEnviar.getText().trim());
					Mensageiro enviador = new Mensageiro(mens);
					txtMensagensRecebidas.setText("\n"+txtMensagensRecebidas.getText()+"\n"+"Você disse: "+txtMensagemAEnviar.getText().trim());
					txtMensagemAEnviar.setRequestFocusEnabled(true);
					txtMensagemAEnviar.requestFocusInWindow();
					txtMensagemAEnviar.setText("");

				}else{
					Mensagem mens = new Mensagem(TelaComunicator.this.remetente,TelaComunicator.this.destinatario);
					mens.setTexto(txtMensagemAEnviar.getText().trim());
					Mensageiro enviador = new Mensageiro(mens);
					txtMensagensRecebidas.setText("\n"+txtMensagensRecebidas.getText()+"\n"+"Você disse: "+txtMensagemAEnviar.getText().trim());
					txtMensagemAEnviar.setRequestFocusEnabled(true);
					txtMensagemAEnviar.requestFocusInWindow();
					txtMensagemAEnviar.setText("");

				}
				ehResposta = (!(ehResposta));
				//////////////
				
			}
		});
		btnEnviar.setText("Enviar");
		btnEnviar.setBounds(399, 215, 85, 97);
		getContentPane().add(btnEnviar);

	}

	public Contato getDestinatario() {
		return destinatario;
	}

	public void setDestinatario(Contato destinatario) {
		this.destinatario = destinatario;
		this.setTitle("Conversa com "+this.destinatario.getNome());

	}

	public boolean equals(Object obj){
		if (!(obj instanceof TelaComunicator)) 
			return false;
		TelaComunicator mt = (TelaComunicator) obj;
		return (this.getTitle()+this.remetente.getNome()).equals(mt.getTitle()+mt.remetente.getNome());
	}

}
