package mensagem;

import java.io.Serializable;
import contato.Contato;

public class Mensagem implements Serializable{
	
	private String texto;
	private Contato remetente;
	private Contato destinatario;
	private boolean respondendo;
	
	public Mensagem(Contato rem, Contato dest){
		this.remetente = rem;
		this.destinatario = dest;
		this.respondendo = false;
	}

	public Contato getRemetente() {
		return remetente;
	}

	public void setRemetente(Contato remetente) {
		this.remetente = remetente;
	}

	public Contato getDestinatario() {
		return destinatario;
	}

	public void setDestinatario(Contato destinatario) {
		this.destinatario = destinatario;
	}

	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}
	

}
