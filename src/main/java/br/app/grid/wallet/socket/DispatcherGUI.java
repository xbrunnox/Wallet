package br.app.grid.wallet.socket;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.time.LocalDateTime;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class DispatcherGUI extends JFrame {

	private static final long serialVersionUID = 1L;
	
	private String sinalUrl = "cotacao.grid.app.br";
	private int porta = 22341;

	private JLabel textoAtivo;
	private JLabel textoVolume;

	private JTextField campoAtivo;
	private JTextField campoVolume;

	private JButton botaoComprar;
	private JButton botaoVender;
	
	private JLabel textoHorario;

	private Container tela;

//	private DispatcherServer server;

	private Socket socket;

	private BufferedReader reader;

	private BufferedWriter writer;

	public DispatcherGUI() {
		super("Control");
		setSize(250, 200);
		tela = getContentPane();
		tela.setLayout(null);
		inicializar();
		posicoes();
		acoes();
		constroiTela();
	}

	private void inicializar() {
		textoAtivo = new JLabel("Ativo: ");
		textoVolume = new JLabel("Volume: ");
		textoHorario = new JLabel("");

		campoAtivo = new JTextField("WDOX22");
		campoVolume = new JTextField("1.0");

		botaoComprar = new JButton("Comprar");
		botaoVender = new JButton("Vender");

//		server = DispatcherServer.getInstance();
//		server.aguardarConexao();
		try {
			socket = new Socket(sinalUrl, porta);

			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void posicoes() {
		textoAtivo.setBounds(0, 7, 100, 24);
		campoAtivo.setBounds(100, 7, 100, 24);
		textoVolume.setBounds(0, 32, 100, 24);
		campoVolume.setBounds(100, 32, 100, 24);
		botaoComprar.setBounds(10, 57, 90, 24);
		botaoVender.setBounds(110, 57, 90, 24);
		textoHorario.setBounds(10, 80, 200, 24);

		textoAtivo.setHorizontalAlignment(JLabel.RIGHT);
		textoVolume.setHorizontalAlignment(JLabel.RIGHT);
	}

	private void acoes() {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		botaoComprar.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				textoHorario.setText(LocalDateTime.now().toString());
				try {
					writer.write("buy GUI " + campoAtivo.getText() + " " + campoVolume.getText()+"\n");
					writer.flush();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		botaoVender.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				textoHorario.setText(LocalDateTime.now().toString());
				try {
					writer.write("sell GUI " + campoAtivo.getText() + " " + campoVolume.getText()+"\n");
					writer.flush();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
	}

	private void constroiTela() {
		tela.add(textoAtivo);
		tela.add(textoVolume);
		tela.add(textoHorario);
		tela.add(campoAtivo);
		tela.add(campoVolume);
		tela.add(botaoComprar);
		tela.add(botaoVender);
	}

	public static void main(String[] args) {
		DispatcherGUI gui = new DispatcherGUI();
		gui.setLocationRelativeTo(null);
		gui.setVisible(true);
	}

}
