package screen;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class Erro extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			Erro dialog = new Erro();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
			dialog.setLocationRelativeTo(null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public Erro() {
		setModal(true);
		setAlwaysOnTop(true);
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBackground(new Color(192, 192, 192));
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
			JPanel panel = new JPanel();
			panel.setBackground(new Color(59, 57, 180));
			panel.setBorder(null);
			panel.setBounds(0, 0, 436, 35);
			contentPanel.add(panel);
			panel.setLayout(null);
			{
				JLabel lblNewLabel = new JLabel("Error");
				lblNewLabel.setForeground(new Color(255, 255, 255));
				lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 20));
				lblNewLabel.setBounds(10, 7, 68, 20);
				panel.add(lblNewLabel);
			}
		}
		{
			JLabel lblNewLabel_1 = new JLabel("");
			lblNewLabel_1.setIcon(new ImageIcon(Erro.class.getResource("/resources/images/botao-x.png")));
			lblNewLabel_1.setBounds(10, 85, 75, 75);
			contentPanel.add(lblNewLabel_1);
		}
		{
			JLabel lblNewLabel_2 = new JLabel("Erro de Importa\u00E7\u00E3o de Arquivo");
			lblNewLabel_2.setFont(new Font("Tahoma", Font.BOLD, 15));
			lblNewLabel_2.setBounds(99, 46, 237, 28);
			contentPanel.add(lblNewLabel_2);
		}
		{
			JLabel lblNewLabel_3 = new JLabel("O texto contido no arquivo que voc\u00EA ");
			lblNewLabel_3.setFont(new Font("Tahoma", Font.PLAIN, 12));
			lblNewLabel_3.setBounds(98, 85, 227, 14);
			contentPanel.add(lblNewLabel_3);
		}
		{
			JLabel lblNewLabel_4 = new JLabel("est\u00E1 tentando importar est\u00E1 fora do padr\u00E3o ");
			lblNewLabel_4.setFont(new Font("Tahoma", Font.PLAIN, 12));
			lblNewLabel_4.setBounds(99, 110, 237, 14);
			contentPanel.add(lblNewLabel_4);
		}
		{
			JLabel lblNewLabel_5 = new JLabel("esperado para envio ao CNC.");
			lblNewLabel_5.setFont(new Font("Tahoma", Font.PLAIN, 12));
			lblNewLabel_5.setBounds(99, 135, 237, 14);
			contentPanel.add(lblNewLabel_5);
		}
		{
			JButton btnNewButton = new JButton("OK");
			btnNewButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					Erro.this.dispose();
				}
			});
			btnNewButton.setFont(new Font("Tahoma", Font.BOLD, 15));
			btnNewButton.setBounds(155, 202, 125, 35);
			contentPanel.add(btnNewButton);
		}
	}

}
