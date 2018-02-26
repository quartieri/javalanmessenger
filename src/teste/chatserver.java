package teste;
import java.awt.*;
import java.net.*;
import java.io.*;
import java.util.*;

public class chatserver extends Thread
{
  int DEFAULT_PORT=4321;
  protected int port;
  protected ServerSocket server_port;
  protected ThreadGroup CurrentConnections;
  protected Vector connections;
  protected ServerWriter writer;
  private Calendar datatual;

  //Criar um ServerSocket
  public chatserver()
  {
    super("Server");
    this.port=DEFAULT_PORT;
    try
    {
      server_port=new ServerSocket(port);
    }
    catch (IOException e)
    {
      System.err.println(e+"Exception");
    }
    //Cria um threadgroup para as conexoes
    CurrentConnections=new ThreadGroup("Server Connections");

    //Mensagem inicial na janela do servidor
    System.out.println("=== ��� Conexoes Realizadas ��� ===");

    //Um vetor para armazenar as conexoes
    connections=new Vector();
    writer=new ServerWriter(this);

    //Inicia o servidor para ouvindo as conexoes
    this.start();
  }

  public void run()
  {
    try
    {
      while(true)
      {
        datatual = Calendar.getInstance();
        Socket cliente_socket=server_port.accept();
        //Exibe na janela do servidor os clientes que conectam (mostra o host
        //do cliente, a porta e a data e hora da conexao 
        System.out.println("Host:"+cliente_socket.getInetAddress()+"� Porta:"+
                            cliente_socket.getPort()+"� "+datatual.getTime());
        Connection c=new Connection(cliente_socket,CurrentConnections,3,writer);
        //evita o acesso simultaneo
        synchronized(connections)
        {
          //adiciona esta nova conexao a lista
          connections.addElement(c);
        }
      }
    }
    catch(IOException e)
    {
      System.err.println(e+"Exception");
    }
  }

  //Inicia o servidor
  public static void main(String[] args)
  {
    new chatserver();
  }
}
//----------------------------------------------------------------------------
class Connection extends Thread
{
  static int numberOfConnections=0;
  protected Socket client;
  protected DataInputStream in;
  protected PrintStream out;
  protected ServerWriter writer;

  public Connection(Socket cliente_socket, ThreadGroup CurrentConnections,
                 int priority, ServerWriter writer)
  {
    super(CurrentConnections,"Connection number"+numberOfConnections++);
    //define a prioridade
    this.setPriority(priority);
    client=cliente_socket;
    this.writer=writer;
    try
    {
      //Atarraxa os streams aos streams de entrada e saida do socket do
      //cliente e adiciona este outputstream ao vetor que contem todos
      //os streams de saida, usados pelo escritor writer
      in=new DataInputStream(client.getInputStream());
      out=new PrintStream(client.getOutputStream());
      writer.OutputStreams.addElement(out);
    }
    catch(IOException e)
    {
      try
      {
        client.close();
      }
      catch (IOException e2)
      {
        System.err.println("Exception while getting socket streams:"+e);
        return;
      }
    }
    //dispara Thread
    this.start();
  }

  //O metodo run faz um laco lendo as mensagens recebidas
  public void run()
  {
    String inline;
    //Envia uma mensagem de boas vindas ao cliente
    out.println("Bem vindo ao MiniChat...");
    try
    {
      while(true)
      {
        //le uma linha de mensagem
        inline=in.readLine();
        //A conexao eh interrompida se null
        if (inline==null)
          break;
        //Joga a linha no escritor writer
        writer.setOutdata(inline);
        synchronized(writer)
        {
          //chama o escritor synchronized() para evitar que duas linhas
          //Connection o chamem ao mesmo tempo. Esta e uma forma de "bloqueio".
          writer.notify();
        }
      }
    }
    catch(IOException e)
    {}
    finally
    {
      try
      {
        client.close();
      }
      catch(IOException e2)
      {}
    }
  }
}
//----------------------------------------------------------------------------
class ServerWriter extends Thread
{
  protected chatserver server;
  public Vector OutputStreams;
  public String outdata;
  private String outputline;

  public ServerWriter(chatserver s)
  {
    super(s.CurrentConnections,"Server Writer");
    server=s;
    OutputStreams=new Vector();
    this.start();
  }

  public void setOutdata(String mensagem)
  {
    outdata=mensagem;
  }

  public synchronized void run()
  {
    while(true)
    {
      //A linha faz um laco para sempre, mas na vedade so e executada quando
      //a condicao wait for reinicializada por um notify. Isso num bloco
      //sincronizado para bloquear a linha e evitar o acesso multiplo.
      try
      {
        this.wait();
      }
      catch (InterruptedException e)
      {
        System.out.println("Caught an Interrupted Exception");
      }
      outputline=outdata;
      synchronized(server.connections)
      {
        for (int i=0 ; i<OutputStreams.size() ; i++)
        {
          //Eh impressa a mensagem em cada um OutputStreams.
          PrintStream out;
          out=(PrintStream)OutputStreams.elementAt(i);
          out.println(outputline);
        }
      }
    }
  }
}
