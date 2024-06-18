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

public class Salvo extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			Salvo dialog = new Salvo();
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
	public Salvo() {
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
				JLabel lblNewLabel = new JLabel("Informa\u00E7\u00E3o");
				lblNewLabel.setForeground(new Color(255, 255, 255));
				lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 20));
				lblNewLabel.setBounds(10, 3, 127, 28);
				panel.add(lblNewLabel);
			}
		}
		{
			JLabel lblNewLabel_1 = new JLabel("");
			lblNewLabel_1.setIcon(new ImageIcon(Erro.class.getResource("/resources/images/verificar.png")));
			lblNewLabel_1.setBounds(14, 94, 75, 75);
			contentPanel.add(lblNewLabel_1);
		}
		{
			JLabel lblNewLabel_2 = new JLabel("Arquivo salvo com sucesso!");
			lblNewLabel_2.setFont(new Font("Tahoma", Font.BOLD, 20));
			lblNewLabel_2.setBounds(99, 117, 312, 28);
			contentPanel.add(lblNewLabel_2);
		}
		{
			JButton btnNewButton = new JButton("OK");
			btnNewButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					Salvo.this.dispose();
				}
			});
			btnNewButton.setFont(new Font("Tahoma", Font.BOLD, 15));
			btnNewButton.setBounds(155, 202, 125, 35);
			contentPanel.add(btnNewButton);
		}
	}

}
