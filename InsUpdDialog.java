package packall;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Dialog.ModalityType;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.*;
import java.awt.event.ActionEvent;

public class InsUpdDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	public Object[] data;
	private JTextField[] jTF;
	private JComboBox[] comboBox;
	private Object[] collection;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			InsUpdDialog dialog = new InsUpdDialog(null);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public InsUpdDialog(DataHandler dH) { //for insertion
		setTitle("Insert Values");
		setModalityType(ModalityType.APPLICATION_MODAL);
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		{
			int c=dH.getColumnIOData().length;
			JPanel[] panel = new JPanel[c];
			comboBox = new JComboBox[c];
			jTF=new JTextField[c];
			collection=new Object[c];
			for(int i=0; i<c; i++){
				panel[i]=new JPanel();
				panel[i].setLayout(new BorderLayout());
				contentPanel.add(panel[i]);
				JLabel label=new JLabel(dH.columns[i]);
				label.setFont(new Font("Tahoma", Font.BOLD, 13));
				panel[i].add(label,BorderLayout.NORTH);
				if(dH.getColumnIOData()[i].getClass().isArray()){
					comboBox[i] = new JComboBox();
					comboBox[i].setModel(new DefaultComboBoxModel((String[])((Object[])dH.getColumnIOData()[i])[1]));
					panel[i].add(comboBox[i], BorderLayout.CENTER);
					if(dH.getTable().equals("Book")&&i==5){
						collection[i]=new ArrayList<String>();
						jTF[i]=new JTextField();						
						panel[i].add(jTF[i],BorderLayout.SOUTH);
						JPanel newPanel=new JPanel();
						panel[i].add(newPanel,BorderLayout.EAST);
						JButton addBtn = new JButton("Add");
						final int j=i;
						addBtn.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent arg0) {
								if( !( (ArrayList<String>)collection[j] ).contains(( (String) comboBox[j].getSelectedItem() )) ){
									( (ArrayList<String>)collection[j] ).add(( (String) comboBox[j].getSelectedItem() ));
								}
								ListIterator<String> ite=( (ArrayList<String>)collection[j] ).listIterator();
								String data="";
								while(ite.hasNext()){
									data+=ite.next()+", ";
								}
								jTF[j].setText(data);
							}
						});
						newPanel.add(addBtn);
						JButton removeBtn = new JButton("Remove");
						removeBtn.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent arg0) {
								ListIterator<String> ite=( (ArrayList<String>)collection[j] ).listIterator();
								( (ArrayList<String>)collection[j] ).remove(((ArrayList<String>)collection[j]).size()-1);
								String data="";
								while(ite.hasNext()){
									data+=ite.next()+", ";
								}
								jTF[j].setText(data);
							}
						});
						newPanel.add(removeBtn);
					}
					if(dH.getTable().equals("Order_1")&&i==5){
						collection[i]=new HashMap<String,Integer>();
						for(int j=0;j<((String[])((Object[])dH.getColumnIOData()[i])[1]).length;j++){
							((HashMap<String,Integer>)collection[i]).put(((String[])((Object[])dH.getColumnIOData()[i])[1])[j], 0);
						}
						jTF[i]=new JTextField();						
						panel[i].add(jTF[i],BorderLayout.SOUTH);
						JPanel newPanel=new JPanel();
						panel[i].add(newPanel,BorderLayout.EAST);
						JButton addBtn = new JButton("Add");
						
						final int j=i;
						addBtn.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent arg0) {
								if( ( (HashMap<String,Integer>)collection[j] ).containsKey(( (String) comboBox[j].getSelectedItem() ))){
									( (HashMap<String,Integer>)collection[j] ).put(( (String) comboBox[j].getSelectedItem() ), ( (HashMap<String,Integer>)collection[j] ).get(( (String) comboBox[j].getSelectedItem() ))+1);
								}
								Iterator ite=((HashMap<String,Integer>)collection[j]).entrySet().iterator();
								String data="";
								while(ite.hasNext()){
									Map.Entry pair=(Map.Entry)ite.next();
									if (!pair.getValue().equals(0)) {
										data += pair.getKey() + ", " + pair.getValue() + "; ";
									}
								}
								jTF[j].setText(data);
							}
						});
						newPanel.add(addBtn);
						JButton removeBtn = new JButton("Remove");
						removeBtn.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent arg0) {
								int i;
								String remData=(String)comboBox[j].getSelectedItem();
								if( (i=((HashMap<String,Integer>)collection[j]).get(remData)) >0){
									i--;
									((HashMap<String,Integer>)collection[j]).put(remData, i);
								}
								Iterator ite=((HashMap<String,Integer>)collection[j]).entrySet().iterator();
								String data="";
								while(ite.hasNext()){
									Map.Entry pair=(Map.Entry)ite.next();
									if (!pair.getValue().equals(0)) {
										data += pair.getKey() + ", " + pair.getValue() + "; ";
									}
								}
								jTF[j].setText(data);
							}
						});
						newPanel.add(removeBtn);
					}
				}
				else if(dH.getColumnIOData()[i].equals("IntX")){
					jTF[i]=new JTextField("Auto-complete");
					panel[i].add(jTF[i],BorderLayout.SOUTH);
					jTF[i].setEditable(false);
				}
				else if(dH.getColumnIOData()[i].equals("IntV")){
					jTF[i]=new JTextField("", 20);
					panel[i].add(jTF[i],BorderLayout.SOUTH);
				}
				else if(dH.getColumnIOData()[i].equals("StrV")){
					jTF[i]=new JTextField("", 20);
					panel[i].add(jTF[i],BorderLayout.SOUTH);
				}
				else if(dH.getColumnIOData()[i].equals("FltV")){
					jTF[i]=new JTextField("", 20);
					panel[i].add(jTF[i],BorderLayout.SOUTH);
				}
			}
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						int c=dH.getColumnIOData().length;
						data=new Object[c];
						for(int i=0; i<c;i++){
							if(dH.getColumnIOData()[i].getClass().isArray()){
								data[i]=comboBox[i].getSelectedItem();
								if(dH.getTable().equals("Book") && i==5){
									data[i]=collection[i];
								}
								if(dH.getTable().equals("Order_1") && i==5){
									data[i]=collection[i];
								}
							}
							else
								data[i]=jTF[i].getText();
						}
						if(data!=null){
							try {
								dH.insertRow(data);
								dispose();
							} catch (SQLException e1) {
								e1.printStackTrace();
								System.out.println(e1.getMessage());
								NoConnection noCon;
								switch(dH.table){
									case "Book":String str="";
												if(jTF[0].getText().length()!=13){
													str+="Insert ISBN: 13 digits. ";
												}
												if(jTF[1].getText().length()<=0){
													str+="Please insert a title.";
												}
												noCon=new NoConnection(str);
												noCon.setVisible(true);
												noCon.setLocation(300,150);
												break;
									case "Client":noCon=new NoConnection("All fields must be completed");
												noCon.setVisible(true);
												noCon.setLocation(300,150);
												break;
									case "Item":noCon=new NoConnection("Please insert: Price>0, Pages>0 or null");
												noCon.setVisible(true);
												noCon.setLocation(300,150);
												break;
									default:noCon=new NoConnection("Do not leave any empty fields");
												noCon.setVisible(true);
												noCon.setLocation(300,150);
												break;
								}
								//NoConnection noCon=new NoConnection(e1.getMessage());
								
								}
						}
						
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						data=null;
						dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}
	public InsUpdDialog(DataHandler dH, String[] inputData, JTable table, Object[] deleteID) { //for update
		setTitle("Update Values");
		setModalityType(ModalityType.APPLICATION_MODAL);
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		{
			int c=dH.getColumnIOData().length;
			JPanel[] panel = new JPanel[c];
			comboBox = new JComboBox[c];
			jTF=new JTextField[c];
			collection=new Object[c];
			for(int i=0; i<c; i++){
				panel[i]=new JPanel();
				panel[i].setLayout(new BorderLayout());
				contentPanel.add(panel[i]);
				JLabel label=new JLabel(dH.columns[i]);
				label.setFont(new Font("Tahoma", Font.BOLD, 13));
				panel[i].add(label,BorderLayout.NORTH);
				if(dH.getColumnIOData()[i].getClass().isArray()){
					if(dH.getTable().equals("Item")){
						comboBox[i] = new JComboBox();
						String[] data=new String[]{(String)table.getValueAt(table.getSelectedRow(), i)};
						comboBox[i].setModel(new DefaultComboBoxModel(data));
						comboBox[i].setEnabled(false);
						panel[i].add(comboBox[i],BorderLayout.SOUTH);
					}
					if(dH.getTable().equals("Book")){
						comboBox[i] = new JComboBox();
						comboBox[i].setModel(new DefaultComboBoxModel((String[])((Object[])dH.getColumnIOData()[i])[1]));
						panel[i].add(comboBox[i], BorderLayout.CENTER);
						if (i==5) {
							if (!((String) table.getValueAt(table.getSelectedRow(), i)).equals("-")) {
								collection[i] = new ArrayList<String>(Arrays.asList(
										((String) table.getValueAt(table.getSelectedRow(), i)).split(", ")));
							}
							else{
								collection[i] = new ArrayList<String>();
							}
							ListIterator<String> ite = ((ArrayList<String>) collection[i]).listIterator();
							String data = "";
							while (ite.hasNext()) {
								data += ite.next() + ", ";
							}
							jTF[i] = new JTextField(data);
							panel[i].add(jTF[i], BorderLayout.SOUTH);
							JPanel newPanel = new JPanel();
							panel[i].add(newPanel, BorderLayout.EAST);
							JButton addBtn = new JButton("Add");
							final int j = i;
							addBtn.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent arg0) {
									if (!((ArrayList<String>) collection[j])
											.contains(((String) comboBox[j].getSelectedItem()))) {
										((ArrayList<String>) collection[j])
												.add(((String) comboBox[j].getSelectedItem()));
									}
									ListIterator<String> ite = ((ArrayList<String>) collection[j]).listIterator();
									String data = "";
									while (ite.hasNext()) {
										data += ite.next() + ", ";
									}
									jTF[j].setText(data);
								}
							});
							newPanel.add(addBtn);
							JButton removeBtn = new JButton("Remove");
							removeBtn.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent arg0) {
									ListIterator<String> ite = ((ArrayList<String>) collection[j]).listIterator();
									((ArrayList<String>) collection[j])
											.remove(((ArrayList<String>) collection[j]).size() - 1);
									String data = "";
									ite = ((ArrayList<String>) collection[j]).listIterator();
									while (ite.hasNext()) {
										data += ite.next() + ", ";
									}
									jTF[j].setText(data);
								}
							});
							newPanel.add(removeBtn);
						}
					}
					if(dH.getTable().equals("Order_1")){
						comboBox[i] = new JComboBox();
						comboBox[i].setModel(new DefaultComboBoxModel((String[])((Object[])dH.getColumnIOData()[i])[1]));
						panel[i].add(comboBox[i], BorderLayout.CENTER);
						if (i==5) {
							collection[i]=new HashMap<String,Integer>();
							for(int j=0;j<((String[])((Object[])dH.getColumnIOData()[i])[1]).length;j++){
								((HashMap<String,Integer>)collection[i]).put(((String[])((Object[])dH.getColumnIOData()[i])[1])[j], 0);
							}
							String[] tData;
							if (!((String) table.getValueAt(table.getSelectedRow(), i)).equals("-")) {
								tData = ((String) table.getValueAt(table.getSelectedRow(), i)).split("; ");
								for(int j=0; j<tData.length; j++){
									String[] nData=tData[j].split(", ");
									tData[j]="";
									int k;
									for(k=0; k<nData.length-1; k++){
										tData[j]+=nData[k]+", ";
									}
									Integer[] tNr=new Integer[tData.length];
									tNr[j]=Integer.parseInt(nData[k]);
									tData[j]=tData[j].substring(0, tData[j].length()-2);
									((HashMap<String,Integer>)collection[i]).put(tData[j], tNr[j]);
								}
							}
							jTF[i] = new JTextField();
							panel[i].add(jTF[i], BorderLayout.SOUTH);
							JPanel newPanel = new JPanel();
							panel[i].add(newPanel, BorderLayout.EAST);
							JButton addBtn = new JButton("Add");
							Iterator ite=((HashMap<String,Integer>)collection[i]).entrySet().iterator();
							String data="";
							while(ite.hasNext()){
								Map.Entry pair=(Map.Entry)ite.next();
								if (!pair.getValue().equals(0)) {
									data += pair.getKey() + ", " + pair.getValue() + "; ";
								}
							}
							jTF[i].setText(data);
							final int j = i;
							addBtn.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent arg0) {
									if( ( (HashMap<String,Integer>)collection[j] ).containsKey(( (String) comboBox[j].getSelectedItem() ))){
										( (HashMap<String,Integer>)collection[j] ).put(( (String) comboBox[j].getSelectedItem() ), ( (HashMap<String,Integer>)collection[j] ).get(( (String) comboBox[j].getSelectedItem() ))+1);
									}
									Iterator ite=((HashMap<String,Integer>)collection[j]).entrySet().iterator();
									String data="";
									while(ite.hasNext()){
										Map.Entry pair=(Map.Entry)ite.next();
										if (!pair.getValue().equals(0)) {
											data += pair.getKey() + ", " + pair.getValue() + "; ";
										}
									}
									jTF[j].setText(data);
								}
							});
							newPanel.add(addBtn);
							JButton removeBtn = new JButton("Remove");
							removeBtn.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent arg0) {
									int i;
									String remData=(String)comboBox[j].getSelectedItem();
									if( (i=((HashMap<String,Integer>)collection[j]).get(remData)) >0){
										i--;
										((HashMap<String,Integer>)collection[j]).put(remData, i);
									}
									Iterator ite=((HashMap<String,Integer>)collection[j]).entrySet().iterator();
									String data="";
									while(ite.hasNext()){
										Map.Entry pair=(Map.Entry)ite.next();
										if (!pair.getValue().equals(0)) {
											data += pair.getKey() + ", " + pair.getValue() + "; ";
										}
									}
									jTF[j].setText(data);
								}
							});
							newPanel.add(removeBtn);
						}
					}
				}
				else if(dH.getColumnIOData()[i].equals("IntX")){
					jTF[i]=new JTextField("Auto-complete");
					panel[i].add(jTF[i],BorderLayout.SOUTH);
					jTF[i].setEditable(false);
				}
				else if(dH.getColumnIOData()[i].equals("IntV")){
					jTF[i]=new JTextField(inputData[i]);
					panel[i].add(jTF[i],BorderLayout.SOUTH);
				}
				else if(dH.getColumnIOData()[i].equals("StrV")){
					jTF[i]=new JTextField(inputData[i]);
					panel[i].add(jTF[i],BorderLayout.SOUTH);
					if(dH.getTable().equals("Book") && i==0){
						jTF[i].setEditable(false);
					}
				}
				else if(dH.getColumnIOData()[i].equals("FltV")){
					jTF[i]=new JTextField(inputData[i]);
					panel[i].add(jTF[i],BorderLayout.SOUTH);
				}
			}
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						int c=dH.getColumnIOData().length;
						data=new Object[c];
						for(int i=0; i<c;i++){
							if(dH.getColumnIOData()[i].getClass().isArray()){
								data[i]=comboBox[i].getSelectedItem();
								if(dH.getTable().equals("Book") && i==5){
									data[i]=collection[i];
								}
								if(dH.getTable().equals("Order_1") && i==5){
									data[i]=collection[i];
								}
							}
							else
								data[i]=jTF[i].getText();
						}
						if(data!=null){
							try {
								dH.updateRow(deleteID, data);
								dispose();
							} catch (SQLException e1) {
								e1.printStackTrace();
								NoConnection noCon;
								switch(dH.table){
									case "Book":String str="";
												if(jTF[0].getText().length()!=13){
													str+="Insert ISBN: 13 digits. ";
												}
												if(jTF[1].getText().length()<=0){
													str+="Please insert a title.";
												}
												noCon=new NoConnection(str);
												noCon.setVisible(true);
												noCon.setLocation(300,150);
												break;
									case "Client":noCon=new NoConnection("All fields must be completed");
												noCon.setVisible(true);
												noCon.setLocation(300,150);
												break;
									case "Item":noCon=new NoConnection("Please insert: Price>0, (Pages>0 or null)");
												noCon.setVisible(true);
												noCon.setLocation(300,150);
												break;
									default:noCon=new NoConnection("Do not leave any empty fields");
												noCon.setVisible(true);
												noCon.setLocation(300,150);
												break;
								}
							}
						}
						
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						data=null;
						dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}
}
