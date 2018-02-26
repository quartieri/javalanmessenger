package teste;
import java.io.*;
import java.net.*;
import java.awt.*;
import java.applet.*;

public class ClienteCall extends Applet 
{
  public static final int DEFAULT_PORT=4321;
  public Socket clisoc;
  private Thread reader; 
  public TextArea OutputArea;
  public TextField InputArea, nomefield;
  public PrintStream out;
  public String Name;

  //Cria as linhas de leitura e escrita e as inicia.
  public void init()
  {
    OutputArea=new TextArea(20,45);
    InputArea=new TextField(45);
    nomefield=new TextField(10);

    //Tela da Applet
    add(new Label("MiniChat usando conexao (Socket TCP)"));
    add(new Label("Nome do usuario"));
    add(nomefield);
    add(OutputArea);
    add(new Label("Digite uma mensagem e pressione ENTER"));
    add(InputArea);
    resize(350,445);
    try
    {
      //Cria um socket cliente passando o endereco e a porta do servidor
      clisoc=new Socket("127.0.0.1",DEFAULT_PORT);
      reader=new Reader(this, OutputArea);
      out=new PrintStream(clisoc.getOutputStream());
      //Define prioridades desiguais para que o console seja compartilhado
      //de forma efetiva.
      reader.setPriority(3);
      reader.start();
    }
    catch(IOException e)
    {
      System.err.println(e);
    }
  }

  public boolean handleEvent(Event evt)
  {
    if (evt.target==InputArea) 
    {
      char c=(char)evt.key;
      if (c=='\n')
      //Vigia se o usuario pressiona a tecla ENTER.
      //Isso permite saber a mensagem esta pronta para ser enviada!
      {
        String InLine=InputArea.getText();
        Name=nomefield.getText();
        out.println(Name+">"+InLine);
        InputArea.setText("");
        //Envia a mensagem, mas adiciona o nome do usuario a ela para que os
        //outros clientes saibam quem a enviou.
        return true;
      }
    }
    return false;
  }
}
//----------------------------------------------------------------------------
//A classe Reader le a entrada do soquete e atualiza a OutputArea com as
//novas mensagens.
class Reader extends Thread
{
  protected ClienteCall cliente;
  private TextArea OutputArea;
  public Reader(ClienteCall c, TextArea OutputArea)
  {
    super("chatclient Reader");
    this.cliente=c;
    this.OutputArea=OutputArea;
  }

  public void run()
  {
    DataInputStream in=null;
    String line;
    try
    {
      in=new DataInputStream(cliente.clisoc.getInputStream());
      while(true)
      {
        line=in.readLine();
        //Adiciona a nova mensagem a OutputArea
        OutputArea.appendText(line+"\r\n");
      }
    }
    catch(IOException e)
    {
      System.out.println("Reader:"+e);
    }
  }
}

