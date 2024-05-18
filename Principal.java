package screen;

import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class Principal extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtEnviar;

	// flag para sinalizar status da porta
	boolean conectado = false;

	// String para preenchimento do Baud Rate
	String[] baudRate = { "110", "300", "600", "1200", "2400", "4800", "9600", "14400", "19200", "38400", "57600",
			"115200", "128000", "256000" };

	// cria objeto para comunicação serial
	Serial com = new Serial();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Principal frame = new Principal();
					frame.setVisible(true);
					frame.setLocationRelativeTo(null);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Principal() {
		setTitle("Terminal Serial RS232");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 600);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(192, 192, 192));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);

		JPanel pnlComunicacao = new JPanel();
		pnlComunicacao.setBorder(new LineBorder(new Color(0, 0, 0)));
		pnlComunicacao.setBounds(10, 11, 764, 70);
		contentPane.add(pnlComunicacao);
		pnlComunicacao.setLayout(null);

		JLabel lblPorta = new JLabel("Porta");
		lblPorta.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblPorta.setBounds(34, 11, 150, 23);
		pnlComunicacao.add(lblPorta);

		JLabel lblBaudrate = new JLabel("BaudRate");
		lblBaudrate.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblBaudrate.setBounds(218, 11, 150, 23);
		pnlComunicacao.add(lblBaudrate);

		JComboBox cmbPortas = new JComboBox(com.listaCom());
		cmbPortas.setBounds(34, 37, 150, 22);
		pnlComunicacao.add(cmbPortas);

		JComboBox cmbBaudRate = new JComboBox(baudRate);
		cmbBaudRate.setSelectedIndex(11);
		cmbBaudRate.setBounds(218, 37, 150, 22);
		pnlComunicacao.add(cmbBaudRate);

		JButton btnConectar = new JButton("Conectar");
		btnConectar.setFont(new Font("Tahoma", Font.BOLD, 16));
		btnConectar.setBounds(402, 16, 129, 37);
		pnlComunicacao.add(btnConectar);

		JButton btnDesconectar = new JButton("Desconectar");
		btnDesconectar.setEnabled(false);
		btnDesconectar.setFont(new Font("Tahoma", Font.BOLD, 16));
		btnDesconectar.setBounds(565, 16, 161, 37);
		pnlComunicacao.add(btnDesconectar);

		JPanel pnlPrincipal = new JPanel();
		pnlPrincipal.setBorder(new LineBorder(new Color(0, 0, 0)));
		pnlPrincipal.setBounds(10, 92, 764, 458);
		contentPane.add(pnlPrincipal);
		pnlPrincipal.setLayout(null);

		JTextArea txtDadosRecebidos = new JTextArea();
		txtDadosRecebidos.setFont(new Font("Monospaced", Font.PLAIN, 16));
		txtDadosRecebidos.setBounds(10, 45, 744, 357);
		pnlPrincipal.add(txtDadosRecebidos);

		txtEnviar = new JTextField();
		txtEnviar.setFont(new Font("Tahoma", Font.PLAIN, 16));
		txtEnviar.setBounds(10, 414, 389, 34);
		pnlPrincipal.add(txtEnviar);
		txtEnviar.setColumns(10);

		JButton btnEnviar = new JButton("Enviar");
		btnEnviar.setFont(new Font("Tahoma", Font.BOLD, 16));
		btnEnviar.setBounds(625, 413, 129, 37);
		pnlPrincipal.add(btnEnviar);

		JLabel lblDadosRecebidos = new JLabel("Dados Recebidos");
		lblDadosRecebidos.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblDadosRecebidos.setBounds(10, 11, 150, 23);
		pnlPrincipal.add(lblDadosRecebidos);

		JComboBox cmbFinalDeLinha = new JComboBox();
		cmbFinalDeLinha.setFont(new Font("Tahoma", Font.PLAIN, 12));
		cmbFinalDeLinha.setModel(new DefaultComboBoxModel(new String[] { "Sem final de linha", "Retorno de linha",
				"Nova linha", "Retorno de linha e nova linha" }));
		cmbFinalDeLinha.setBounds(409, 414, 208, 34);
		pnlPrincipal.add(cmbFinalDeLinha);

		setPanelEnabled(pnlPrincipal, false);

		btnConectar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean resultado = com.abreCom(cmbPortas.getSelectedItem().toString(),
						Integer.parseInt(cmbBaudRate.getSelectedItem().toString()));
				if (resultado) {
					conectado = true;
					cmbPortas.setEnabled(false);
					cmbBaudRate.setEnabled(false);
					btnConectar.setEnabled(false);
					btnDesconectar.setEnabled(true);
					setPanelEnabled(pnlPrincipal, true);
					txtDadosRecebidos.setText("");
				}
			}
		});

		btnDesconectar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				conectado = false;
				com.fechaCom();
				cmbPortas.setEnabled(true);
				cmbBaudRate.setEnabled(true);
				btnConectar.setEnabled(true);
				btnDesconectar.setEnabled(false);
				setPanelEnabled(pnlPrincipal, false);
			}
		});

		btnEnviar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String dados = txtEnviar.getText();
				if (cmbFinalDeLinha.getSelectedIndex() == 1) {
					dados.concat("\r");
				} else if (cmbFinalDeLinha.getSelectedIndex() == 2) {
					dados.concat("\n");
				} else if (cmbFinalDeLinha.getSelectedIndex() == 3) {
					dados.concat("\r\n");
				}
				com.enviaDados(dados);
			}
		});

		// Cria um serviço agendado com uma única Thread
		ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

		// Tarefa a ser executada periodicamente
		Runnable task = new Runnable() {
			@Override
			public void run() {
				if (conectado) {
					txtDadosRecebidos.append(com.leDados());
				}
			}
		};

		// Agenda a tarefa para ser executada a cada 100 milissegundos
		scheduler.scheduleAtFixedRate(task, 0, 100, TimeUnit.MILLISECONDS);

		// Encerra o serviço agendado de forma adequada quando necessário
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				scheduler.shutdown();
				try {
					if (!scheduler.awaitTermination(1, TimeUnit.SECONDS)) {
						scheduler.shutdownNow();
					}
				} catch (InterruptedException e) {
					scheduler.shutdownNow();
				}
			}
		});
	}

	// método para habilitar ou desabilitar um JPanel
	void setPanelEnabled(JPanel panel, Boolean isEnabled) {
		panel.setEnabled(isEnabled);

		Component[] components = panel.getComponents();

		for (Component component : components) {
			if (component instanceof JPanel) {
				setPanelEnabled((JPanel) component, isEnabled);
			}
			component.setEnabled(isEnabled);
		}
	}
}
