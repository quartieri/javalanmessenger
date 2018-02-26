package mensageiro;

import java.io.ObjectOutputStream;
import java.net.Socket;
import javax.swing.JOptionPane;
import mensagem.Mensagem;
import contato.Contato;

public class Mensageiro {

	private Contato remetente;
	private Contato destinatario;
	private Mensagem mensagem;
	private Socket socket = null;
	private ObjectOutputStream enviador = null;
	 

	public Mensageiro(final Mensagem mens){
		this.remetente = mens.getRemetente();
		this.destinatario = mens.getDestinatario();
		this.mensagem = mens;
		Thread r =	new Thread() {
			public void run() {
				Mensageiro.this.enviaMensagem(mens);	
			}
		};
		r.start();
	}

	public Mensageiro(){}


	public synchronized void enviaMensagem(Mensagem mens){
		try {
			socket = new Socket(destinatario.getIp(), 85); 
			enviador = new ObjectOutputStream(socket.getOutputStream());
			enviador.writeObject(mens) ;
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Mensagem não enviada, este usuário pode não estar ativo no momento!","Atenção",2);
		}	
	}
	
}
