package packall;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Window.Type;
import javax.swing.JDialog;
import java.awt.Dialog.ModalityType;

public class NoConnection extends JDialog {

	private JPanel contentPane;

	public NoConnection(int errNo) {
		setModalityType(ModalityType.APPLICATION_MODAL);
		setTitle("Error");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 284, 147);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNoConnectionTo = new JLabel();
		lblNoConnectionTo.setHorizontalAlignment(SwingConstants.CENTER);
		switch(errNo){
			case 1:	lblNoConnectionTo.setText("<html><center>Please select a table first!</center></html>");
					break;
			case 2:	lblNoConnectionTo.setText("<html><center>Invalid data!</html></center>");
					break;
			case 3:	lblNoConnectionTo.setText("<html><center><small>Cannot delete row as it is linked to other rows in other tables, delete those first!</small></html></center>");	
					break;
			case 4:	lblNoConnectionTo.setText("<html><center>Invalid data for update</html></center>");
					break;
			default:lblNoConnectionTo.setText("<html><center>No connection to database!</html></center>");
		}
		
		lblNoConnectionTo.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblNoConnectionTo.setBounds(12, 13, 242, 33);
		contentPane.add(lblNoConnectionTo);
		JButton btnOk = new JButton("OK");
		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnOk.setBounds(89, 62, 97, 25);
		contentPane.add(btnOk);
	}

	public NoConnection(String msg) {
		setModalityType(ModalityType.APPLICATION_MODAL);
		setTitle("Error");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 800, 200);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout());
	
		JLabel lblNoConnectionTo = new JLabel(msg);
		lblNoConnectionTo.setHorizontalAlignment(SwingConstants.CENTER);
	
		lblNoConnectionTo.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblNoConnectionTo.setBounds(12, 13, 242, 33);
		contentPane.add(lblNoConnectionTo,BorderLayout.CENTER);
		JButton btnOk = new JButton("OK");
		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnOk.setBounds(89, 62, 97, 25);
		contentPane.add(btnOk,BorderLayout.SOUTH);
	}
}

