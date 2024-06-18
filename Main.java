package screen;

import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.text.BadLocationException;

public class Main extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	int cont = 0;
	boolean flag = false;
	int passo = 1;
	boolean erro = false;

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
					Main frame = new Main();
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
	public Main() {
		setTitle("Terminal Serial RS232");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 973, 676);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(33, 32, 100));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);

		JPanel pnlComunicacao = new JPanel();
		pnlComunicacao.setBackground(new Color(59, 57, 180));
		pnlComunicacao.setBorder(new LineBorder(new Color(0, 0, 0)));
		pnlComunicacao.setBounds(10, 11, 939, 70);
		contentPane.add(pnlComunicacao);
		pnlComunicacao.setLayout(null);

		JLabel lblPorta = new JLabel("Porta");
		lblPorta.setForeground(new Color(255, 255, 255));
		lblPorta.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblPorta.setBounds(34, 11, 150, 23);
		pnlComunicacao.add(lblPorta);

		JLabel lblBaudrate = new JLabel("BaudRate");
		lblBaudrate.setForeground(new Color(255, 255, 255));
		lblBaudrate.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblBaudrate.setBounds(265, 11, 150, 23);
		pnlComunicacao.add(lblBaudrate);

		JComboBox cmbPortas = new JComboBox(com.listaCom());
		cmbPortas.setBackground(new Color(255, 255, 255));
		cmbPortas.setBounds(34, 37, 150, 22);
		pnlComunicacao.add(cmbPortas);

		JComboBox cmbBaudRate = new JComboBox(baudRate);
		cmbBaudRate.setSelectedIndex(11);
		cmbBaudRate.setBounds(265, 37, 150, 22);
		pnlComunicacao.add(cmbBaudRate);

		JButton btnConectar = new JButton("Conectar");
		btnConectar.setForeground(new Color(255, 255, 255));
		btnConectar.setBackground(new Color(33, 32, 100));
		btnConectar.setFont(new Font("Tahoma", Font.BOLD, 16));
		btnConectar.setBounds(483, 16, 161, 37);
		pnlComunicacao.add(btnConectar);

		JButton btnDesconectar = new JButton("Desconectar");
		btnDesconectar.setForeground(new Color(255, 255, 255));
		btnDesconectar.setBackground(new Color(33, 32, 100));
		btnDesconectar.setEnabled(false);
		btnDesconectar.setFont(new Font("Tahoma", Font.BOLD, 16));
		btnDesconectar.setBounds(705, 16, 161, 37);
		pnlComunicacao.add(btnDesconectar);

		JPanel pnlPrincipal = new JPanel();
		pnlPrincipal.setBackground(new Color(59, 57, 180));
		pnlPrincipal.setBorder(new LineBorder(new Color(0, 0, 0)));
		pnlPrincipal.setBounds(10, 92, 611, 401);
		contentPane.add(pnlPrincipal);
		pnlPrincipal.setLayout(null);

		JLabel lblListaDeComandos = new JLabel("Lista de comandos");
		lblListaDeComandos.setForeground(new Color(255, 255, 255));
		lblListaDeComandos.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblListaDeComandos.setBounds(10, 11, 150, 23);
		pnlPrincipal.add(lblListaDeComandos);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 45, 590, 345);
		pnlPrincipal.add(scrollPane);

		JTextArea txtListaDeComandos = new JTextArea();
		txtListaDeComandos.setEditable(false);
		txtListaDeComandos.setFont(new Font("Monospaced", Font.PLAIN, 16));
		scrollPane.setViewportView(txtListaDeComandos);

		JButton btnAbrir = new JButton("Importar");
		btnAbrir.setFont(new Font("Tahoma", Font.BOLD, 11));
		btnAbrir.setBackground(new Color(33, 32, 100));
		btnAbrir.setForeground(new Color(255, 255, 255));
		btnAbrir.setBounds(420, 12, 85, 25);
		pnlPrincipal.add(btnAbrir);

		JButton btnSalvar = new JButton("Exportar");
		btnSalvar.setFont(new Font("Tahoma", Font.BOLD, 11));
		btnSalvar.setBackground(new Color(33, 32, 100));
		btnSalvar.setForeground(new Color(255, 255, 255));
		btnSalvar.setBounds(515, 12, 85, 25);
		pnlPrincipal.add(btnSalvar);
		
		setPanelEnabled(pnlPrincipal, false);
		
		JTextArea txtVerifica = new JTextArea();
		txtVerifica.setBounds(203, 12, 5, 22);

		btnSalvar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				int option = fileChooser.showSaveDialog(null);
				if (option == JFileChooser.APPROVE_OPTION) {
					File file = fileChooser.getSelectedFile();
					try (FileWriter writer = new FileWriter(file)) {
						writer.write(txtListaDeComandos.getText());
						System.out.println("Texto salvo com sucesso em " + file.getPath());
						Salvo Tela3 = new Salvo();
						Tela3.setLocationRelativeTo(null);
						Tela3.setVisible(true);
					} catch (IOException ex) {
						System.out.println("Erro ao salvar o arquivo: " + ex.getMessage());
					}
				}
			}
		});
		btnAbrir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				int returnValue = fileChooser.showOpenDialog(null);

				if (returnValue == JFileChooser.APPROVE_OPTION) {
					File selectedFile = fileChooser.getSelectedFile();
					try {
						BufferedReader reader = new BufferedReader(new FileReader(selectedFile));
						txtVerifica.setText(null);
						txtVerifica.read(reader, null);
						char vetor[] = { 'G', 'T', 'S', 'M', 'F', 'R', 'X', 'Y', 'Z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '-', ' ', '\n'};
						String aux = txtVerifica.getText().toUpperCase();

						for (int i = 0; i < aux.length(); i++) {
							for (int j = 0; j < vetor.length; j++) {
								if (aux.charAt(i) == vetor[j]) {
									erro = false;
									break;
								}
								else {
									erro = true;
								}
							}
							if (erro) {
								break;
							}
						}
						if (erro) {
								Erro Tela2 = new Erro();
								Tela2.setLocationRelativeTo(null);
								Tela2.setVisible(true);
								aux = null;
						} 
						else {
							erro = false;
						}
						txtListaDeComandos.setText(aux);
						reader.close();
					} catch (IOException ex) {
						System.out.println("Error reading file: " + ex.getMessage());
					}
				}
			}
		});

//------Cria o container onde fica os comandos que podem ser inseridos.---------------------------------------------------------------------------------------------------------
		JPanel pnlFuncoes = new JPanel();
		pnlFuncoes.setBackground(new Color(59, 57, 180));
		pnlFuncoes.setLayout(null);
		pnlFuncoes.setBorder(new LineBorder(new Color(0, 0, 0)));
		pnlFuncoes.setBounds(10, 504, 375, 128);
		contentPane.add(pnlFuncoes);

		JSpinner spinnerG = new JSpinner();
		spinnerG.setBackground(new Color(33, 32, 100));
		spinnerG.setModel(new SpinnerNumberModel(Integer.valueOf(0), null, null, Integer.valueOf(1)));
		spinnerG.setFont(new Font("Tahoma", Font.PLAIN, 16));
		spinnerG.setBounds(170, 45, 71, 23);
		pnlFuncoes.add(spinnerG);

		JCheckBox chckbxG = new JCheckBox("G");
		chckbxG.setForeground(new Color(255, 255, 255));
		chckbxG.setBackground(new Color(59, 57, 180));
		chckbxG.setFont(new Font("Tahoma", Font.PLAIN, 16));
		chckbxG.setBounds(131, 45, 37, 24);
		pnlFuncoes.add(chckbxG);

		JCheckBox chckbxT = new JCheckBox("T");
		chckbxT.setForeground(new Color(255, 255, 255));
		chckbxT.setBackground(new Color(59, 57, 180));
		chckbxT.setFont(new Font("Tahoma", Font.PLAIN, 16));
		chckbxT.setBounds(131, 94, 37, 24);
		pnlFuncoes.add(chckbxT);

		JSpinner spinnerT = new JSpinner();
		spinnerT.setBackground(new Color(33, 32, 100));
		spinnerT.setModel(new SpinnerNumberModel(Integer.valueOf(0), null, null, Integer.valueOf(1)));
		spinnerT.setFont(new Font("Tahoma", Font.PLAIN, 16));
		spinnerT.setBounds(170, 94, 71, 23);
		pnlFuncoes.add(spinnerT);

		JCheckBox chckbxS = new JCheckBox("S");
		chckbxS.setForeground(new Color(255, 255, 255));
		chckbxS.setBackground(new Color(59, 57, 180));
		chckbxS.setFont(new Font("Tahoma", Font.PLAIN, 16));
		chckbxS.setBounds(6, 97, 37, 24);
		pnlFuncoes.add(chckbxS);

		JSpinner spinnerS = new JSpinner();
		spinnerS.setBackground(new Color(33, 32, 100));
		spinnerS.setModel(new SpinnerNumberModel(Integer.valueOf(0), null, null, Integer.valueOf(1)));
		spinnerS.setFont(new Font("Tahoma", Font.PLAIN, 16));
		spinnerS.setBounds(45, 97, 71, 23);
		pnlFuncoes.add(spinnerS);

		JCheckBox chckbxM = new JCheckBox("M");
		chckbxM.setForeground(new Color(255, 255, 255));
		chckbxM.setBackground(new Color(59, 57, 180));
		chckbxM.setFont(new Font("Tahoma", Font.PLAIN, 16));
		chckbxM.setBounds(258, 45, 39, 24);
		pnlFuncoes.add(chckbxM);

		JSpinner spinnerM = new JSpinner();
		spinnerM.setBackground(new Color(33, 32, 100));
		spinnerM.setModel(new SpinnerNumberModel(Integer.valueOf(0), null, null, Integer.valueOf(1)));
		spinnerM.setFont(new Font("Tahoma", Font.PLAIN, 16));
		spinnerM.setBounds(299, 45, 71, 23);
		pnlFuncoes.add(spinnerM);

		JCheckBox chckbxF = new JCheckBox("F");
		chckbxF.setForeground(new Color(255, 255, 255));
		chckbxF.setBackground(new Color(59, 57, 180));
		chckbxF.setFont(new Font("Tahoma", Font.PLAIN, 16));
		chckbxF.setBounds(6, 45, 37, 24);
		pnlFuncoes.add(chckbxF);

		JSpinner spinnerF = new JSpinner();
		spinnerF.setBackground(new Color(33, 32, 100));
		spinnerF.setModel(new SpinnerNumberModel(Integer.valueOf(0), null, null, Integer.valueOf(1)));
		spinnerF.setFont(new Font("Tahoma", Font.PLAIN, 16));
		spinnerF.setBounds(45, 45, 71, 23);
		pnlFuncoes.add(spinnerF);

		JCheckBox chckbxR = new JCheckBox("R");
		chckbxR.setForeground(new Color(255, 255, 255));
		chckbxR.setBackground(new Color(59, 57, 180));
		chckbxR.setFont(new Font("Tahoma", Font.PLAIN, 16));
		chckbxR.setBounds(258, 94, 37, 24);
		pnlFuncoes.add(chckbxR);

		JSpinner spinnerR = new JSpinner();
		spinnerR.setBackground(new Color(33, 32, 100));
		spinnerR.setModel(new SpinnerNumberModel(Integer.valueOf(0), null, null, Integer.valueOf(1)));
		spinnerR.setFont(new Font("Tahoma", Font.PLAIN, 16));
		spinnerR.setBounds(299, 94, 71, 23);
		pnlFuncoes.add(spinnerR);

		JLabel lblNewLabel = new JLabel("Fun\u00E7\u00F5es");
		lblNewLabel.setForeground(new Color(255, 255, 255));
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblNewLabel.setBounds(151, 11, 73, 23);
		pnlFuncoes.add(lblNewLabel);
		
//------Cria o container onde fica os botões.-----------------------------------------------------------------------------------------------------------------------------------
		JPanel pnlEixos = new JPanel();
		pnlEixos.setBackground(new Color(59, 57, 180));
		pnlEixos.setBorder(new LineBorder(new Color(0, 0, 0)));
		pnlEixos.setBounds(631, 262, 318, 231);
		contentPane.add(pnlEixos);
		pnlEixos.setLayout(null);

//------Cria os botões usados para controlar os três eixos do cnc.--------------------------------------------------------------------------------------------------------------
		JButton btnXmais = new JButton("X+");
		btnXmais.setForeground(new Color(255, 255, 255));
		btnXmais.setBackground(new Color(33, 32, 100));
		btnXmais.setBounds(197, 83, 65, 65);
		pnlEixos.add(btnXmais);

		JButton btnXmenos = new JButton("X-");
		btnXmenos.setForeground(new Color(255, 255, 255));
		btnXmenos.setBackground(new Color(33, 32, 100));
		btnXmenos.setBounds(54, 83, 65, 65);
		pnlEixos.add(btnXmenos);

		JButton btnYmais = new JButton("Y+");
		btnYmais.setForeground(new Color(255, 255, 255));
		btnYmais.setBackground(new Color(33, 32, 100));
		btnYmais.setBounds(126, 11, 65, 65);
		pnlEixos.add(btnYmais);

		JButton btnYmenos = new JButton("Y-");
		btnYmenos.setForeground(new Color(255, 255, 255));
		btnYmenos.setBackground(new Color(33, 32, 100));
		btnYmenos.setBounds(126, 159, 65, 65);
		pnlEixos.add(btnYmenos);
		
		JButton btnDefinirZero = new JButton("Zero");
		btnDefinirZero.setForeground(new Color(255, 255, 255));
		btnDefinirZero.setBackground(new Color(33, 32, 100));
		btnDefinirZero.setBounds(126, 83, 65, 65);
		pnlEixos.add(btnDefinirZero);
		
		JPanel pnlPasso = new JPanel();
		pnlPasso.setBackground(new Color(59, 57, 180));
		pnlPasso.setBorder(new LineBorder(new Color(0, 0, 0)));
		pnlPasso.setBounds(723, 92, 226, 159);
		contentPane.add(pnlPasso);
		pnlPasso.setLayout(null);
		
		JLabel lblNewLabel_3 = new JLabel("Passo");
		lblNewLabel_3.setForeground(new Color(255, 255, 255));
		lblNewLabel_3.setBounds(82, 11, 62, 34);
		pnlPasso.add(lblNewLabel_3);
		lblNewLabel_3.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblNewLabel_3.setHorizontalAlignment(SwingConstants.CENTER);

		JRadioButton rdbtnX1 = new JRadioButton("x1");
		rdbtnX1.setForeground(new Color(255, 255, 255));
		rdbtnX1.setBackground(new Color(59, 57, 180));
		rdbtnX1.setFont(new Font("Tahoma", Font.BOLD, 15));
		rdbtnX1.setBounds(10, 77, 57, 23);
		pnlPasso.add(rdbtnX1);
		rdbtnX1.setSelected(true);

		JRadioButton rdbtnX10 = new JRadioButton("x10");
		rdbtnX10.setForeground(new Color(255, 255, 255));
		rdbtnX10.setBackground(new Color(59, 57, 180));
		rdbtnX10.setFont(new Font("Tahoma", Font.BOLD, 15));
		rdbtnX10.setBounds(77, 77, 57, 23);
		pnlPasso.add(rdbtnX10);

		JRadioButton rdbtnX100 = new JRadioButton("x100");
		rdbtnX100.setForeground(new Color(255, 255, 255));
		rdbtnX100.setBackground(new Color(59, 57, 180));
		rdbtnX100.setFont(new Font("Tahoma", Font.BOLD, 15));
		rdbtnX100.setBounds(144, 77, 72, 23);
		pnlPasso.add(rdbtnX100);
		
		ButtonGroup group = new ButtonGroup();

		group.add(rdbtnX100);
		group.add(rdbtnX10);
		group.add(rdbtnX1);
		
		rdbtnX1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (rdbtnX1.isSelected()) {
					passo = 1;
				} else if (rdbtnX10.isSelected()) {
					passo = 10;
				} 
				else {
					passo = 100;
				}
			}
		});
		
		rdbtnX10.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (rdbtnX1.isSelected()) {
					passo = 1;
				} else if (rdbtnX10.isSelected()) {
					passo = 10;
				} 
				else {
					passo = 100;
				}
			}
		});
		
		rdbtnX100.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (rdbtnX1.isSelected()) {
					passo = 1;
				} else if (rdbtnX10.isSelected()) {
					passo = 10;
				} 
				else {
					passo = 100;
				}
			}
		});
		
//------Controla os três eixos do cnc de forma manual.--------------------------------------------------------------------------------------------------------------------------
		btnXmais.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				String dados = "G91 \nG1 X" + passo + " F500\r\n";
				
				com.enviaDados(dados);
			}
		});

		btnXmenos.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String dados = "G91 \nG1 X-" + passo + " F500\r\n";
				com.enviaDados(dados);
			}
		});

		btnYmais.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String dados = "G91 \nG1 Y" + passo + " F500\r\n";
				com.enviaDados(dados);
			}
		});

		btnYmenos.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String dados = "G91 \nG1 Y-" + passo + " F500\r\n";
				com.enviaDados(dados);
			}
		});

		JPanel pnlBotoes = new JPanel();
		pnlBotoes.setBackground(new Color(59, 57, 180));
		pnlBotoes.setBorder(new LineBorder(new Color(0, 0, 0)));
		pnlBotoes.setBounds(631, 504, 318, 128);
		contentPane.add(pnlBotoes);
		pnlBotoes.setLayout(null);

		JButton btnApagarLinha = new JButton("Apagar linha");
		btnApagarLinha.setForeground(new Color(255, 255, 255));
		btnApagarLinha.setFont(new Font("Tahoma", Font.BOLD, 11));
		btnApagarLinha.setBackground(new Color(33, 32, 100));
		btnApagarLinha.setBounds(163, 70, 145, 30);
		pnlBotoes.add(btnApagarLinha);

		JButton btnApagarTudo = new JButton("Apagar tudo");
		btnApagarTudo.setForeground(new Color(255, 255, 255));
		btnApagarTudo.setFont(new Font("Tahoma", Font.BOLD, 11));
		btnApagarTudo.setBackground(new Color(33, 32, 100));
		btnApagarTudo.setBounds(10, 70, 145, 30);
		pnlBotoes.add(btnApagarTudo);

		JButton btnInserir = new JButton("Inserir");
		btnInserir.setForeground(new Color(255, 255, 255));
		btnInserir.setBackground(new Color(33, 32, 100));
		btnInserir.setBounds(10, 14, 145, 50);
		btnInserir.setFont(new Font("Tahoma", Font.BOLD, 16));
		pnlBotoes.add(btnInserir);

		JButton btnEnviar = new JButton("Enviar");
		btnEnviar.setForeground(new Color(255, 255, 255));
		btnEnviar.setBackground(new Color(33, 32, 100));
		btnEnviar.setBounds(163, 14, 145, 50);
		btnEnviar.setFont(new Font("Tahoma", Font.BOLD, 16));
		pnlBotoes.add(btnEnviar);
		
		JPanel pnlCoordenadas = new JPanel();
		pnlCoordenadas.setBackground(new Color(59, 57, 180));
		pnlCoordenadas.setBorder(new LineBorder(new Color(0, 0, 0)));
		pnlCoordenadas.setBounds(395, 504, 226, 128);
		contentPane.add(pnlCoordenadas);
		pnlCoordenadas.setLayout(null);

		JCheckBox chckbxX = new JCheckBox("X");
		chckbxX.setForeground(new Color(255, 255, 255));
		chckbxX.setBackground(new Color(59, 57, 180));
		chckbxX.setBounds(54, 35, 37, 24);
		pnlCoordenadas.add(chckbxX);
		chckbxX.setFont(new Font("Tahoma", Font.PLAIN, 16));

		JSpinner spinnerX = new JSpinner();
		spinnerX.setBackground(new Color(33, 32, 100));
		spinnerX.setBounds(93, 35, 71, 23);
		pnlCoordenadas.add(spinnerX);
		spinnerX.setModel(new SpinnerNumberModel(Integer.valueOf(0), null, null, Integer.valueOf(1)));
		spinnerX.setFont(new Font("Tahoma", Font.PLAIN, 16));

		JCheckBox chckbxY = new JCheckBox("Y");
		chckbxY.setForeground(new Color(255, 255, 255));
		chckbxY.setBackground(new Color(59, 57, 180));
		chckbxY.setBounds(54, 66, 37, 24);
		pnlCoordenadas.add(chckbxY);
		chckbxY.setFont(new Font("Tahoma", Font.PLAIN, 16));

		JSpinner spinnerY = new JSpinner();
		spinnerY.setBackground(new Color(33, 32, 100));
		spinnerY.setBounds(93, 66, 71, 23);
		pnlCoordenadas.add(spinnerY);
		spinnerY.setModel(new SpinnerNumberModel(Integer.valueOf(0), null, null, Integer.valueOf(1)));
		spinnerY.setFont(new Font("Tahoma", Font.PLAIN, 16));

		JCheckBox chckbxZ = new JCheckBox("Z");
		chckbxZ.setForeground(new Color(255, 255, 255));
		chckbxZ.setBackground(new Color(59, 57, 180));
		chckbxZ.setBounds(54, 97, 37, 24);
		pnlCoordenadas.add(chckbxZ);
		chckbxZ.setFont(new Font("Tahoma", Font.PLAIN, 16));

		JSpinner spinnerZ = new JSpinner();
		spinnerZ.setBackground(new Color(33, 32, 100));
		spinnerZ.setBounds(93, 97, 71, 23);
		pnlCoordenadas.add(spinnerZ);
		spinnerZ.setFont(new Font("Tahoma", Font.PLAIN, 16));

		JLabel lblCoodernadas = new JLabel("Coodernadas");
		lblCoodernadas.setForeground(new Color(255, 255, 255));
		lblCoodernadas.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblCoodernadas.setBounds(53, 11, 120, 23);
		pnlCoordenadas.add(lblCoodernadas);
		
		JPanel pnlZ = new JPanel();
		pnlZ.setBackground(new Color(59, 57, 180));
		pnlZ.setBorder(new LineBorder(new Color(0, 0, 0)));
		pnlZ.setBounds(631, 92, 82, 159);
		contentPane.add(pnlZ);
		pnlZ.setLayout(null);

		JButton btnZmais = new JButton("Z+");
		btnZmais.setForeground(new Color(255, 255, 255));
		btnZmais.setBackground(new Color(33, 32, 100));
		btnZmais.setBounds(8, 11, 65, 65);
		pnlZ.add(btnZmais);

		JButton btnZmenos = new JButton("Z-");
		btnZmenos.setForeground(new Color(255, 255, 255));
		btnZmenos.setBackground(new Color(33, 32, 100));
		btnZmenos.setBounds(8, 83, 65, 65);
		pnlZ.add(btnZmenos);

		btnZmenos.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String dados = "G91 \nG1 Z-" + passo + " F500\r\n";
				com.enviaDados(dados);
			}
		});

		btnZmais.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String dados = "G91 \nG1 Z" + passo + " F500\r\n";
				com.enviaDados(dados);
			}
		});
		
//------Desabilita os componentes que não podem ser usados enquanto não estiver conectado.---------------------------------------------------------------------------------------
		setPanelEnabled(pnlFuncoes, false);
		setPanelEnabled(pnlPasso, false);
		setPanelEnabled(pnlEixos, false);
		setPanelEnabled(pnlCoordenadas, false);
		setPanelEnabled(pnlBotoes, false);
		setPanelEnabled(pnlZ, false);
		
//------Define as coordenadas atuais do cnc como o ponto zero.-------------------------------------------------------------------------------------------------------------------
		btnDefinirZero.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				com.enviaDados("G10 P0 L20 X0 Y0 Z0\r\n");
			}
		});

//------Insere os comandos selecionados na lista de comandos.--------------------------------------------------------------------------------------------------------------------
		btnInserir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				if (chckbxG.isSelected()) {
					txtListaDeComandos.append("G" + spinnerG.getValue() + " ");
				}
				
				if (chckbxX.isSelected()) {
					txtListaDeComandos.append("X" + spinnerX.getValue() + " ");
				}

				if (chckbxY.isSelected()) {
					txtListaDeComandos.append("Y" + spinnerY.getValue() + " ");
				}

				if (chckbxZ.isSelected()) {
					txtListaDeComandos.append("Z" + spinnerZ.getValue() + " ");
				}
				
				if (chckbxR.isSelected()) {
					txtListaDeComandos.append("R" + spinnerR.getValue() + " ");
				}
				
				if (chckbxF.isSelected()) {
					txtListaDeComandos.append("F" + spinnerF.getValue() + " ");
				}
				
				if (chckbxM.isSelected()) {
					txtListaDeComandos.append("M" + spinnerM.getValue() + " ");
				}

				if (chckbxT.isSelected()) {
					txtListaDeComandos.append("T" + spinnerT.getValue() + " ");
				}

				if (chckbxS.isSelected()) {
					txtListaDeComandos.append("S" + spinnerS.getValue() + " ");
				}

				txtListaDeComandos.append("\n");

// --------------Reseta os check box depois de inserir os comandos na lista.-----------------------------------------------------------------------------------------------------
				chckbxG.setSelected(false);
				chckbxT.setSelected(false);
				chckbxS.setSelected(false);
				chckbxM.setSelected(false);
				chckbxF.setSelected(false);
				chckbxR.setSelected(false);
				chckbxX.setSelected(false);
				chckbxY.setSelected(false);
				chckbxZ.setSelected(false);

//--------------Reseta os Jspinner depois de inserir os comandos na lista.------------------------------------------------------------------------------------------------------
				spinnerG.setValue(0);
				spinnerT.setValue(0);
				spinnerS.setValue(0);
				spinnerM.setValue(0);
				spinnerF.setValue(0);
				spinnerR.setValue(0);
				spinnerX.setValue(0);
				spinnerY.setValue(0);
				spinnerZ.setValue(0);
			}
		});

//------Verifica se o código G-code contido no arquivo importado está em conformidade com a estrutura G-Code e envia o G-code contido na lista de comandos para o cnc.-----------
		btnEnviar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String dados = txtListaDeComandos.getText();
				com.enviaDados(dados);
			}
		});

//------Apaga a última linha da lista de comandos.-------------------------------------------------------------------------------------------------------------------------------
		btnApagarLinha.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					// Obtenha o início do índice da linha
					int start = txtListaDeComandos.getLineStartOffset(txtListaDeComandos.getLineCount() - 1);
					// Obtenha o fim do índice da linha
					int end = txtListaDeComandos.getLineEndOffset(txtListaDeComandos.getLineCount() - 1);

					if (start == end) {
						// Ajusta o índice para remover a linha anterior também
						start = txtListaDeComandos.getLineStartOffset(txtListaDeComandos.getLineCount() - 2);
					}

					// Remova o texto entre esses índices
					txtListaDeComandos.replaceRange(null, start, end);

				} catch (BadLocationException ex) {
					// Se a linha não for válida, mostre uma mensagem de erro ou lide com isso de
					// forma adequada
					System.out.println("Linha inválida: " + ex.getMessage());
				}
			}
		});

//------Apaga toda a lista de comandos.-----------------------------------------------------------------------------------------------------------------------------------------
		btnApagarTudo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				txtListaDeComandos.setText(null);
			}
		});

//------Conecta o aplicativo com o cnc.-----------------------------------------------------------------------------------------------------------------------------------------
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
					txtListaDeComandos.setText("");
					setPanelEnabled(pnlPasso, true);
					setPanelEnabled(pnlZ, true);
					setPanelEnabled(pnlBotoes, true);
					setPanelEnabled(pnlFuncoes, true);
					setPanelEnabled(pnlCoordenadas, true);
					setPanelEnabled(pnlPrincipal, true);
					setPanelEnabled(pnlFuncoes, true);
					setPanelEnabled(pnlEixos, true);
				}
			}
		});

//------Desconecta o aplicativo com o cnc.--------------------------------------------------------------------------------------------------------------------------------------
		btnDesconectar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				conectado = false;
				com.fechaCom();
				cmbPortas.setEnabled(true);
				cmbBaudRate.setEnabled(true);
				btnConectar.setEnabled(true);
				btnDesconectar.setEnabled(false);
				setPanelEnabled(pnlPasso, false);
				setPanelEnabled(pnlZ, false);
				setPanelEnabled(pnlBotoes, false);
				setPanelEnabled(pnlFuncoes, false);
				setPanelEnabled(pnlCoordenadas, false);
				setPanelEnabled(pnlPrincipal, false);
				setPanelEnabled(pnlFuncoes, false);
				setPanelEnabled(pnlEixos, false);
			}
		});

//------Cria um serviço agendado com uma única Thread.--------------------------------------------------------------------------------------------------------------------------
		ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

		// Tarefa a ser executada periodicamente
		Runnable task = new Runnable() {
			@Override
			public void run() {
				if (conectado) {
					com.enviaDados("?");
				}
			}
		};

//------Agenda a tarefa para ser executada a cada 100 milissegundos.------------------------------------------------------------------------------------------------------------
		scheduler.scheduleAtFixedRate(task, 0, 100, TimeUnit.MILLISECONDS);

//------Encerra o serviço agendado de forma adequada quando necessário.---------------------------------------------------------------------------------------------------------
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

//--método para habilitar ou desabilitar um JPanel.-----------------------------------------------------------------------------------------------------------------------------
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
