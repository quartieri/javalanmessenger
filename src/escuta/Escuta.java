package escuta;

import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

import mensagem.Mensagem;

public class Escuta extends Thread {

	private ServerSocket servidor = null;
	private Socket cliente = null;
	public Mensagem mensagem;

	public Escuta(){}

	public void run(){

		try{
			servidor = new ServerSocket(85); 
			while(true){
				cliente = servidor.accept();
				synchronized (this) {
					mensagem = (Mensagem) new ObjectInputStream(cliente.getInputStream()).readObject();	
				}	
			}
		}catch (Exception e){
			//
		}

	}

}
