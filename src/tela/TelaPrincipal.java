package tela;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import mensagem.Mensagem;
import contato.Contato;
import escuta.Escuta;

public class TelaPrincipal extends JFrame {

	private static JTable table;
	private static List listaDeContatos;
	private Escuta escuta;
	private List<TelaComunicator> listaDeJanelas;
	private static Contato usuarioLocal;

	private Thread verificaMensagem = new Thread(){

		public void run(){
			while(true){
				synchronized (this) {
					if (escuta.mensagem != null){
						Mensagem mm = escuta.mensagem;
						mensagemPara(mm);
						escuta.mensagem = null; // se tirar essa linha dá pau

					}
					
				}

			}
		}

	};

	public void mensagemPara(Mensagem mens){
		TelaComunicator tc = new TelaComunicator(mens.getDestinatario(),mens.getRemetente());
		tc.setTitle("Conversa com "+mens.getRemetente().getNome());
		if (!(listaDeJanelas.contains(tc))){
			listaDeJanelas.add(tc);
		}
		tc = listaDeJanelas.get(listaDeJanelas.indexOf(tc));		
		tc.recebeMensagem(mens);
		tc.setVisible(true);
		tc.requestFocus();
	} 


	public void preencheTabela(JTable tabela, List<Contato> ls) {
		// cria um modelo vazio   
		DefaultTableModel modelo = new DefaultTableModel();   
		String colunas[]  = {"Lista de contatos:"};
		modelo.setColumnIdentifiers(colunas); 
		// itera pelas linhas do nosso List
		Iterator<Contato> it = ls.iterator();
		while (it.hasNext()) {
			Contato cont = it.next();
			// insere uma nova linha no modelo   
			modelo.addRow(new Object [] {cont.getNome()});   
		}   
		// configura o modelo da tabela   
		tabela.setModel(modelo);   //jTableGrid
	}

	public static void main(String args[]) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TelaPrincipal frame = new TelaPrincipal();
					TelaLogin login = new TelaLogin();

					login.setVisible(true);
					if (login.getC() == null){
						JOptionPane.showMessageDialog(null, "Sem o preenchimento de um usuário o sistema não irá continuar");
						System.exit(1);
					}

					usuarioLocal = login.getC();
					listaDeContatos = usuarioLocal.recuperaLista();
					frame.preencheTabela(table , listaDeContatos);
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
	public TelaPrincipal() {
		super();
		getContentPane().setForeground(new Color(0, 0, 128));
		setTitle("Java LAN Messenger");
		setResizable(false);
		setForeground(new Color(0, 0, 128));
		setFont(new Font("Tahoma", Font.PLAIN, 10));
		getContentPane().setLayout(null);
		setBounds(100, 100, 262, 568);
		setLocationRelativeTo(null);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		listaDeJanelas = new ArrayList<TelaComunicator>();
		escuta = new Escuta();
		escuta.start();

		verificaMensagem.start();


		final JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(5, 18, 248, 498);
		getContentPane().add(scrollPane);

		table = new JTable();
		table.setForeground(new Color(0, 0, 255));
		table.setDefaultEditor(Object.class, null);
		table.addMouseListener(new MouseAdapter() {
			//private long lastClick; 
			public void mouseClicked(final MouseEvent e) {
				if(e.getClickCount() == 2){

					TelaComunicator tc = new TelaComunicator(usuarioLocal,new Contato().selecionaContatoPorNome(table.getModel().getValueAt(table.getSelectedRow(),0).toString()));

					if (!(listaDeJanelas.contains(tc))){
						listaDeJanelas.add(tc);
						tc.setVisible(true);
						tc.requestFocus();
					}else{
						tc = listaDeJanelas.get(listaDeJanelas.indexOf(tc));;
						tc.setVisible(true);
						tc.requestFocus();
					}

				}

			}
		});
		table.setCellEditor(null);
		table.setShowHorizontalLines(false);
		table.setCursor(new Cursor(Cursor.HAND_CURSOR));
		scrollPane.setViewportView(table);

		final JMenuBar menuBar = new JMenuBar();
		menuBar.setForeground(new Color(0, 0, 128));
		setCursor(new Cursor(Cursor.HAND_CURSOR));
		setJMenuBar(menuBar);

		final JMenu contatosMenu = new JMenu();
		contatosMenu.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				//
			}
		});
		contatosMenu.setForeground(new Color(0, 0, 128));
		contatosMenu.setText("Contatos");
		menuBar.add(contatosMenu);

		final JMenuItem newItemMenuItem = new JMenuItem();
		newItemMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				TelaAdicionarContato telaAdd = new TelaAdicionarContato();
				telaAdd.setResizable(false);
				telaAdd.setLocationRelativeTo(null);
				telaAdd.setModal(true);
				telaAdd.setVisible(true);

				Contato c = new Contato();
				listaDeContatos = c.recuperaLista();
				preencheTabela(table , listaDeContatos);				


			}
		});
		newItemMenuItem.setForeground(new Color(0, 0, 128));
		newItemMenuItem.setText("Adicionar");
		contatosMenu.add(newItemMenuItem);

		final JMenuItem newItemMenuItem_1 = new JMenuItem();
		newItemMenuItem_1.setForeground(new Color(0, 0, 128));
		newItemMenuItem_1.setText("Remover");
		contatosMenu.add(newItemMenuItem_1);
		//

	}

}
