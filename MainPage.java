package packall;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.FlowLayout;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.AbstractListModel;
import javax.swing.ListSelectionModel;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Writer;
import java.sql.SQLException;
import java.awt.event.ActionEvent;
import javax.swing.JTable;
import javax.swing.DefaultComboBoxModel;
import java.io.*;

public class MainPage extends JFrame {

	private JPanel contentPane;
	private JTable table;
	private DataHandler dH;
	private JScrollPane scrollPane_2;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainPage frame = new MainPage();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MainPage() {
		scrollPane_2=null;
		setTitle("DBApp");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 774, 364);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBounds(247, 310, -236, -242);
		contentPane.add(panel);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBounds(12, 32, 245, 240);
		contentPane.add(panel_1);
		panel_1.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 29, 128, 198);
		panel_1.add(scrollPane);
		
		JList list = new JList();
		scrollPane.setViewportView(list);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setModel(new AbstractListModel() {
			String[] values = new String[] {"Author", "Book", "Client", "Format", "Genre", "Item", "Language", "Order_1", "Payment_method", "Publisher", "Support"};
			public int getSize() {
				return values.length;
			}
			public Object getElementAt(int index) {
				return values[index];
			}
		});
		list.setSelectedIndex(0);
		
		JLabel lblSelectTable = new JLabel("Select table");
		lblSelectTable.setBounds(12, 13, 77, 16);
		panel_1.add(lblSelectTable);
		lblSelectTable.setFont(new Font("Tahoma", Font.BOLD, 13));
		
		JButton btnGetData = new JButton("Get data");
		btnGetData.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if(table!=null){
					contentPane.remove(scrollPane_2);
				}
				
				switch((String)list.getSelectedValue()){
					case "Client":	dH=new ClientDataHandler((String)list.getSelectedValue());
									break;
					case "Order_1":	dH=new Order_1DataHandler((String)list.getSelectedValue());
									break;
					case "Book":	dH=new BookDataHandler((String)list.getSelectedValue());
									break;
					case "Item":	dH=new ItemDataHandler((String)list.getSelectedValue());
									break;
					default:dH=new AuthorDataHandler((String)list.getSelectedValue());
				}
				
				dH.getFormattedData();
				table=new JTable(dH.results, dH.columns);
				
				scrollPane_2 = new JScrollPane();
				scrollPane_2.setBounds(269, 13, 260, 291);
				
				contentPane.add(scrollPane_2);
				
				scrollPane_2.setViewportView(table);
				table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
				table.setRowSelectionInterval(0, 0);
			}
		});
		btnGetData.setBounds(152, 114, 81, 25);
		panel_1.add(btnGetData);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBounds(541, 64, 203, 167);
		contentPane.add(panel_2);
		panel_2.setLayout(null);
		
		JButton btnInsertNewRow = new JButton("Insert new row");
		btnInsertNewRow.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(dH!=null){
					InsUpdDialog dialog = new InsUpdDialog(dH);
					dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
					dialog.setVisible(true);
					
				}
				else{
					NoConnection noCon=new NoConnection(1);
					noCon.setVisible(true);
					noCon.setLocation(300,150);
				}
			}
		});
		btnInsertNewRow.setBounds(42, 5, 119, 25);
		panel_2.add(btnInsertNewRow);
		
		JButton btnViewTable = new JButton("View table");
		btnViewTable.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(dH!=null){
					ShowData sD=new ShowData(dH.results,dH.columns,table.getSelectedRow(),dH);
					sD.setVisible(true);
					if(table!=null){
						contentPane.remove(scrollPane_2);
					}
					dH.getFormattedData();
					table=new JTable(dH.results, dH.columns);
					int i=sD.getSelection();
					table.setRowSelectionInterval(i, i);
				}
				else{
					NoConnection noCon=new NoConnection(1);
					noCon.setVisible(true);
					noCon.setLocation(300,150);
				}
			}
		});
		btnViewTable.setBounds(42, 45, 119, 25);
		panel_2.add(btnViewTable);
		
		JButton btnUpdateRow = new JButton("Update row");
		btnUpdateRow.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(dH!=null){
					int c=dH.columns.length;
					String[] inputData=new String[c];
					for(int i=0;i<c;i++){
						inputData[i]=(String)table.getValueAt(table.getSelectedRow(), i);
					}
					Object[] deleteID;
					if(dH.table.equals("Item")){
						deleteID=new Object[3];
						deleteID[0]=table.getValueAt(table.getSelectedRow(), 0);
						deleteID[1]=table.getValueAt(table.getSelectedRow(), 1);
						deleteID[2]=table.getValueAt(table.getSelectedRow(), 2);
					}
					else{
						deleteID=new Object[1];
						deleteID[0]=table.getValueAt(table.getSelectedRow(), 0);
					}
					InsUpdDialog dialog = new InsUpdDialog(dH,inputData,table,deleteID);
					dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
					dialog.setVisible(true);
					
				}
				else{
					NoConnection noCon=new NoConnection(1);
					noCon.setVisible(true);
					noCon.setLocation(300,150);
				}
			}
		});
		btnUpdateRow.setBounds(42, 83, 119, 25);
		panel_2.add(btnUpdateRow);
		
		JButton btnDeleteRow = new JButton("Delete row");
		btnDeleteRow.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(dH!=null){
					Object[] deleteID;
					if(dH.table.equals("Item")){
						deleteID=new Object[3];
						deleteID[0]=table.getValueAt(table.getSelectedRow(), 0);
						deleteID[1]=table.getValueAt(table.getSelectedRow(), 1);
						deleteID[2]=table.getValueAt(table.getSelectedRow(), 2);
					}
					else{
						deleteID=new Object[1];
						deleteID[0]=table.getValueAt(table.getSelectedRow(), 0);
					}
					try {
						dH.deleteRow(deleteID);
					} catch (SQLException e) {
						e.printStackTrace();
						NoConnection noCon=new NoConnection(3);
						noCon.setVisible(true);
						noCon.setLocation(300,150);
					}
				}
				else{
					NoConnection noCon=new NoConnection(1);
					noCon.setVisible(true);
					noCon.setLocation(300,150);
				}
			}
		});
		btnDeleteRow.setBounds(42, 121, 119, 25);
		panel_2.add(btnDeleteRow);
		
		JPanel panel_3 = new JPanel();
		panel_3.setBounds(541, 244, 203, 60);
		contentPane.add(panel_3);
		panel_3.setLayout(new BorderLayout(0, 0));
		
		JLabel lblAfterInsertupdatedeletePress = new JLabel("<html><center>*After insert/update/delete, press \"Get data\" again to see the correct data</center></html>");
		lblAfterInsertupdatedeletePress.setFont(new Font("Tahoma", Font.PLAIN, 11));
		panel_3.add(lblAfterInsertupdatedeletePress, BorderLayout.CENTER);
		
		JButton btnGetTableInfo = new JButton("Get Table info");
		btnGetTableInfo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String currentLine;
				String msg="<HTML>";
				try {
					BufferedReader br=new BufferedReader(new FileReader("z1.txt"));
					while((currentLine=br.readLine())!=null){
						msg+=currentLine+"<br>";
					}
					msg+="</HTML>";
				} catch (IOException e) {
					e.printStackTrace();
				}
				if(msg.length()!=0){
					NoConnection nocon=new NoConnection(msg);
					nocon.setVisible(true);
				}
				Writer writer = null;

				try {
				    writer = new BufferedWriter(new OutputStreamWriter(
				          new FileOutputStream("filename.txt"), "utf-8"));
				    writer.write(msg);
				} catch (IOException ex) {
				  // report
				} finally {
				   try {writer.close();} catch (Exception ex) {/*ignore*/}
				}
			}
		});
		btnGetTableInfo.setBounds(83, 279, 113, 25);
		contentPane.add(btnGetTableInfo);
	}
}
