package contato;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.swing.JOptionPane;

public class Contato implements Comparable<Contato>,Serializable {

	private String nome;
	private String ip;
	private static File f;
	private List<Contato> lista;

	static {f = new File("contatos.txt");}

	public List<Contato> recuperaLista() {
		String caminhoDaAplicacao = (" "+Class.class.getResource("/")).trim();
		lista = new ArrayList<Contato>();
		if (caminhoDaAplicacao.indexOf("file:/") >= 0 ) {
			caminhoDaAplicacao = caminhoDaAplicacao.replace("file:/", "").trim();
		}

		//JOptionPane.showMessageDialog(null,">> Aplicação: " + caminhoDaAplicacao);

		if (f.exists()) {
			// le arquivo
			try {
				BufferedReader reader = new BufferedReader(new FileReader(f));
				String linha = null;
				linha = reader.readLine();
				Contato cont = null;
				while(!(linha == null)){
					//JOptionPane.showMessageDialog(null,linha);
					if(linha.indexOf(":") > 0) {
						cont = new Contato();
						cont.setIp(linha.substring(0,linha.indexOf(":")).trim());
						cont.setNome(linha.substring(linha.indexOf(":")+1).trim()); 
						//JOptionPane.showMessageDialog(null,"Ip:"+cont.getIp()+" ----- "+" Nome:"+cont.getNome());
						lista.add(cont);
					}

					linha = reader.readLine();	
				}

			} catch (Exception e) {
				// 
			}

		}else {
			try {
				f.createNewFile();
			} catch (IOException e) {
				JOptionPane.showMessageDialog(null, "Não foi possível criar o arquivo de Contatos,\n"+
						"verifique se a aplicação tem permissão para\n"+
				"criar arquivos txt");
			}
		}
		Collections.sort(lista);
		return lista;
	}	

	public boolean adicionarContato(){
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(f,true));
			bw.write(this.getIp()+":"+this.getNome()+"\n");
			bw.flush();
			bw.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false; 
		}
	}

	public Contato selecionaContatoPorNome(String nome){
		Contato cont ;
		Iterator<Contato> it =this.recuperaLista().iterator();
		while(it.hasNext()){
			cont = it.next();
			if (cont.getNome().equals(nome)){
				return cont;
			}
		}
		return null;	
	}

	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Contato))
			return false;
		Contato other = (Contato) obj;
		if (nome == null) {
			if (other.nome != null)
				return false;
		} else if (!nome.equals(other.nome))
			return false;
		return true;
	}

	@Override
	public int compareTo(Contato o) {
		return  this.nome.compareTo(o.getNome());
	}		

}
