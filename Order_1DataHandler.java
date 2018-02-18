package packall;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Order_1DataHandler extends DataHandler {

	/**
	 * @param table the table for the superclass
	 */
	public Order_1DataHandler(String table) {
		super(table);
	}

	public void getFormattedData() {
		super.getUnFormattedData();
		ArrayList<String> aL=new ArrayList<String>();
		for(int i=0;i<super.columns.length;i++){
			aL.add(super.columns[i]);
		}
		aL.add("ISBN, Title, Author, Support, Format, Price, Quantity (for each item)");
		columns=(String[])aL.toArray(new String[0]);
		for(int i=0; i<super.results.length; i++){
			aL=new ArrayList<String>();
			for(int j=0;j<super.results[i].length;j++){
				aL.add(super.results[i][j]);
			}
			aL.add("");
			results[i]=(String[])aL.toArray(new String[0]);
			super.results[i][4]=MainClass.conn.queryResults(String.format("SELECT Method FROM Payment_Method WHERE Payment_Method_ID=%s",super.results[i][4]))[0][0];
			super.results[i][2]=MainClass.conn.queryResults(String.format("SELECT Client_Name+', '+Client_Acc_Name+', '+Email FROM Client WHERE Client_ID=%s",super.results[i][2]))[0][0];
			String newRow="";
			String[][] outerResults=MainClass.conn.queryResults("SELECT Item.ISBN, Book.Title, Author.Author_Name, "
					+ "Support.Support, Format.Format, Item.Price, Item_Order.Quantity FROM Order_1 INNER JOIN Item_Order ON Order_1.Order_ID = Item_Order.Order_ID "
					+ "INNER JOIN Item ON Item_Order.ISBN = Item.ISBN AND Item_Order.Format_ID = Item.Format_ID "
					+ "AND Item_Order.Support_ID = Item.Support_ID INNER JOIN Book ON Item.ISBN = Book.ISBN "
					+ "INNER JOIN Author ON Book.Author_ID = Author.Author_ID INNER JOIN Support ON Item.Support_ID = Support.Support_ID "
					+ "INNER JOIN Format ON Item.Format_ID = Format.Format_ID WHERE (Order_1.Order_ID = "+results[i][0]+")");
			if (outerResults!=null) {
				for (int j = 0; j < outerResults.length; j++) {
					String newElement = outerResults[j][0];
					for (int k = 1; k < outerResults[0].length; k++) {
						newElement += ", " + outerResults[j][k];
					}
					newRow += newElement+"; ";
				}
				results[i][results[i].length - 1] = newRow;
			}
			else{
				results[i][results[i].length - 1]="-";
			}
		}
	}

	public void deleteRow(Object[] key) throws SQLException {
		String query=String.format("DELETE FROM Item_Order WHERE Order_ID=%s", (String)key[0]);
		MainClass.conn.updateQuery(query);
		query=String.format("DELETE FROM Order_1 WHERE Order_ID=%s", (String)key[0]);
		try {
			MainClass.conn.updateQuery(query);
		} catch (SQLException e) {
			throw e;
		}
	}

	public void updateRow(Object[] key, Object[] data) throws SQLException {
		data[2]=MainClass.conn.queryResults("SELECT Client_ID FROM Client WHERE Client_Name+', '+Client_Acc_Name+', '+Email =\'"+data[2]+"\'")[0][0];
		data[4]=MainClass.conn.queryResults("SELECT Payment_Method_ID FROM Payment_Method WHERE Method=\'"+data[4]+"\'")[0][0];
		String query;
		String test1=(String)data[1];
		String test2=(String)data[3];
		if(test1.length()==0 && test2.length()==0){
			query=String.format("UPDATE %s SET %s=%s, %s=%s WHERE Order_ID=%s", super.table,
					super.columns[2], (String)data[2], super.columns[4], (String)data[4], (String)key[0]);
		}
		else if(test1.length()==0 && !(test2.length()==0)){
			query=String.format("UPDATE %s SET %s=%s, %s=\'%s\', %s=%s WHERE Order_ID=%s", super.table,
								super.columns[2], (String)data[2], super.columns[3], (String)data[3], 
								super.columns[4], (String)data[4], (String)key[0]);
		}
		else if(!(test1.length()==0) && test2.length()==0){
			query=String.format("UPDATE %s SET %s=%s, %s=%s, %s=\'%s\' WHERE Order_ID=%s", super.table,
								super.columns[1], (String)data[1], super.columns[2], (String)data[2], 
								super.columns[4], (String)data[4], (String)key[0]);
		}
		else
			query=String.format("UPDATE %s SET %s=\'%s\', %s=%s, %s=\'%s\', %s=%s WHERE Order_ID=%s", super.table,
					super.columns[1], (String)data[1], super.columns[2], (String)data[2], 
					super.columns[3], (String)data[3], super.columns[4], (String)data[4], (String)key[0]);
		try {
			MainClass.conn.updateQuery(query);
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		}
		query=String.format("DELETE FROM Item_Order WHERE Order_ID=%s", (String)key[0]);
		MainClass.conn.updateQuery(query);
		Iterator ite=((HashMap<String,Integer>)data[5]).entrySet().iterator();
		while(ite.hasNext()){
			Map.Entry pair=(Map.Entry)ite.next();
			String[] data1=((String)pair.getKey()).split(", ");
			data1[3]=MainClass.conn.queryResults("SELECT Support_ID FROM Support WHERE Support=\'"+data1[3]+"\'")[0][0];
			data1[4]=MainClass.conn.queryResults("SELECT Format_ID FROM Format WHERE Format=\'"+data1[4]+"\'")[0][0];
			int i=(Integer)pair.getValue();
			if((Integer)pair.getValue()>0){
				query=String.format("INSERT INTO Item_Order (%s, %s, %s, %s, %s) VALUES (%s,\'%s\',%s,%s,%s)", 
									"Order_ID","ISBN","Format_ID","Support_ID","Quantity",
									key[0], data1[0], data1[4],data1[3],pair.getValue());
				try {
					MainClass.conn.updateQuery(query);
				} catch (SQLException e) {
					e.printStackTrace();
					throw e;
				}
			}
		}
	}

	public void insertRow(Object[] data) throws SQLException {
		data[2]=MainClass.conn.queryResults("SELECT Client_ID FROM Client WHERE Client_Name+', '+Client_Acc_Name+', '+Email =\'"+data[2]+"\'")[0][0];
		data[4]=MainClass.conn.queryResults("SELECT Payment_Method_ID FROM Payment_Method WHERE Method=\'"+data[4]+"\'")[0][0];
		String query;
		String test1=(String)data[1];
		String test2=(String)data[3];
		if(test1.length()==0 && test2.length()==0){
			query=String.format("INSERT INTO %s (%s,%s) VALUES (%s,%s)", super.table,
					super.columns[2], super.columns[4],
					(String)data[2], (String)data[4]);
		}
		else if(test1.length()==0 && !(test2.length()==0)){
			query=String.format("INSERT INTO %s (%s,%s,%s) VALUES (%s,\'%s\',%s)", super.table,
					super.columns[2], super.columns[3], super.columns[4],
					(String)data[2], (String)data[3], (String)data[4]);
		}
		else if(!(test1.length()==0) && test2.length()==0){
			query=String.format("INSERT INTO %s (%s,%s,%s) VALUES (\'%s\',%s,%s)", super.table,
					super.columns[1], super.columns[2], super.columns[4],
					(String)data[1], (String)data[2], (String)data[4]);
		}
		else
			query=String.format("INSERT INTO %s (%s,%s,%s,%s) VALUES (\'%s\',%s,\'%s\',%s)", super.table,
							super.columns[1], super.columns[2], super.columns[3], super.columns[4],
							(String)data[1], (String)data[2], (String)data[3], (String)data[4]);
		try {
			MainClass.conn.updateQuery(query);
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		}
		String key=MainClass.conn.queryResults("SELECT MAX(Order_ID) FROM Order_1")[0][0];
		Iterator ite=((HashMap<String,Integer>)data[5]).entrySet().iterator();
		while(ite.hasNext()){
			Map.Entry pair=(Map.Entry)ite.next();
			String[] data1=((String)pair.getKey()).split(", ");
			data1[3]=MainClass.conn.queryResults("SELECT Support_ID FROM Support WHERE Support=\'"+data1[3]+"\'")[0][0];
			data1[4]=MainClass.conn.queryResults("SELECT Format_ID FROM Format WHERE Format=\'"+data1[4]+"\'")[0][0];
			int i=(Integer)pair.getValue();
			if((Integer)pair.getValue()>0){
				query=String.format("INSERT INTO Item_Order (%s, %s, %s, %s, %s) VALUES (%s,\'%s\',%s,%s,%s)", 
									"Order_ID","ISBN","Format_ID","Support_ID","Quantity",
									key, data1[0], data1[4],data1[3],pair.getValue());
				try {
					MainClass.conn.updateQuery(query);
				} catch (SQLException e) {
					e.printStackTrace();
					throw e;
				}
			}
		}
	}

	public Object[] getColumnIOData() {
		Object[] IOData=new Object[6];
		String[][] data;
		String[] newData;
		int c;
		IOData[0]="IntX";
		IOData[1]="StrV";
		data=MainClass.conn.queryResults("SELECT Client_Name+', '+Client_Acc_Name+', '+Email FROM Client");
		c=data.length;
		newData=new String[c];
		for(int i=0;i<c;i++){
			newData[i]=data[i][0];
		}
		IOData[2]=new Object[] {"Client_Name+', '+Client_Acc_Name+', '+Email",newData};
		IOData[3]="StrV";
		data=MainClass.conn.queryResults("SELECT Method FROM Payment_method");
		c=data.length;
		newData=new String[c];
		for(int i=0;i<c;i++){
			newData[i]=data[i][0];
		}
		IOData[4]=new Object[] {"Method",newData};
		data=MainClass.conn.queryResults("SELECT Item.ISBN, Book.Title, Author.Author_Name, "
				+ "Support.Support, Format.Format, Item.Price FROM Order_1 INNER JOIN Item_Order ON Order_1.Order_ID = Item_Order.Order_ID "
				+ "INNER JOIN Item ON Item_Order.ISBN = Item.ISBN AND Item_Order.Format_ID = Item.Format_ID "
				+ "AND Item_Order.Support_ID = Item.Support_ID INNER JOIN Book ON Item.ISBN = Book.ISBN "
				+ "INNER JOIN Author ON Book.Author_ID = Author.Author_ID INNER JOIN Support ON Item.Support_ID = Support.Support_ID "
				+ "INNER JOIN Format ON Item.Format_ID = Format.Format_ID");
		c=data.length;
		newData=new String[c];
		for(int i=0;i<c;i++){
			newData[i]=data[i][0]+", "+data[i][1]+", "+data[i][2]+", "+data[i][3]+", "+data[i][4]+", "+data[i][5];
		}
		IOData[5]=new Object[] {"ISBN, Title, Author, Support, Format, Price, Quantity (for each item)",newData};
		return IOData;
	}
}
