package teste;
/**
 * @author Dkid
 * Baseado nos artigos
 * http://java.sun.com/developer/onlineTraining/Programming/BasicJava2/socket.html
 * http://www.devarticles.com/c/a/Java/Socket-Programming-in-Java/
 */
import java.awt.Color;
import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt.event.*;
import javax.swing.*;

import java.io.*;
import java.net.*;

class SocketServer extends JFrame
		implements ActionListener {

   JButton botao;
   JLabel label = new JLabel("Texto Recebido:");
   JPanel painel;
   JTextArea campotexto = new JTextArea();
   ServerSocket servidor = null;

   BufferedReader entrada = null;
   PrintWriter saida = null;
   Socket cliente = null;
   String linha;

   SocketServer(){ //Constructor
     botao = new JButton("Receber!");
     botao.addActionListener(this);

     painel = new JPanel();
     painel.setLayout(new BorderLayout());
     painel.setBackground(Color.white);
     getContentPane().add(painel);
     setSize(new Dimension(400, 300));
     painel.add("North", label);
     painel.add("Center", campotexto);
     painel.add("South", botao);

   } 

  public void actionPerformed(ActionEvent event) {
     Object source = event.getSource();

     if(source == botao){
         campotexto.setText(linha);
     }
  }

  public void EscutaSocket(){

    try{
      servidor = new ServerSocket(85); //Definição da porta que vai ficar à escuta, neste caso : porta 85
    } catch (IOException e) {
      System.out.println("Não é possivel escutar na porta 85");
      System.exit(-1);
    }

    try{
      cliente = servidor.accept();
    } catch (IOException e) {
      System.out.println("Servidor não aceitou: 85"); //
      System.exit(-1);
    }

    try{
      entrada = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
      saida = new PrintWriter(cliente.getOutputStream(), true);
    } catch (IOException e) {
      System.out.println("Falha de permissão: 85");
      System.exit(-1);
    }
 
    while(true){
      try{
        linha = entrada.readLine();
//Send data back to cliente
        saida.println(linha);
      } catch (IOException e) {
        System.out.println("Falha de Leitura!");
        System.exit(-1);
      }
    }
  }

  protected void finalize(){
//Clean up 
     try{
        entrada.close();
        saida.close();
        servidor.close();
    } catch (IOException e) {
        System.out.println("Não é possivel fechar!.");
        System.exit(-1);
    }
  }

  public static void main(String[] args){
        SocketServer frame = new SocketServer();
	    frame.setTitle("Servidor");
        WindowListener l = new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
                        System.exit(0);
                }
        };
        frame.addWindowListener(l);
        frame.pack();
        frame.setVisible(true);
        frame.setSize(400, 300);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);         
	frame.EscutaSocket();
  }
}
