package teste;
/**
 * @author Dkid
 * Baseado nos artigos
 * http://java.sun.com/developer/onlineTraining/Programming/BasicJava2/socket.html
 * http://www.devarticles.com/c/a/Java/Socket-Programming-in-Java/
 */

import java.awt.*;
import java.awt.Dimension;
import java.awt.event.*;

import javax.swing.*;
import java.io.*; // pacote de streams
import java.net.*; // pacote de sockets


class SocketCliente extends JFrame implements ActionListener 
{
	JLabel text,clicked;
	JButton botao;
	JPanel painel;
	JTextField campotexto;
	Socket socket = null;
	PrintWriter saida = null;
	BufferedReader entrada = null;

	SocketCliente() //iniciamos o constructor
	{
		text = new JLabel("Texto a enviar por socket:");
		campotexto= new JTextField(30);
		botao = new JButton("Enviar!");
		botao.addActionListener(this);

		painel = new JPanel();
		painel.setLayout(new BorderLayout());
		painel.setBackground(Color.white);
		getContentPane().add(painel);
		setSize(new Dimension(400, 300));
		painel.add("North", text);
		painel.add("Center", campotexto);
		painel.add("South", botao);
	}
	@Override
	public void actionPerformed(ActionEvent event) 
	{
		Object fonte = event.getSource();

		if (fonte == botao) //Envia informacao pelo socket
		{
			String text = campotexto.getText();
			saida.println(text);
			campotexto.setText(new String("")); //recebe do servidor

			try{
				String linha = entrada.readLine();
				System.out.println("Texto recebido: " + linha);
			}
			catch (IOException e)
			{
				System.out.println("Falha na leitura!");
				System.exit(1);
			}
		}
	}

	public void EscutaSocket() //cria ligacao de socket
	{
		try
		{
			String destino = JOptionPane.showInputDialog(null,"Digite o destinatário");
			socket = new Socket(destino, 85); //aqui decidimos qual o IP e a sua porta, neste caso 127.0.0.1:85
			saida = new PrintWriter(socket.getOutputStream(), true);
			entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		
		}
		catch(UnknownHostException e)
		{
			System.out.println("Host desconhecido: localhost");
			System.exit(1);
		}
		catch (IOException e)
		{
			System.out.println("NO I/O");
			System.exit(1);
		}
	}

	public static void main (String[] args)
	{
		SocketCliente frame = new SocketCliente();
		frame.setTitle("Cliente");
		WindowListener ls = new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				System.exit(0);
			}
		};
		frame.addWindowListener(ls);
		frame.pack();
		frame.setVisible(true);
        frame.setSize(400, 300);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);  
		frame.EscutaSocket();
	}
}
