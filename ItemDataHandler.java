package packall;

import java.sql.SQLException;

public class ItemDataHandler extends DataHandler {
	/**
	 * @param table the table for the superclass
	 */
	public ItemDataHandler(String table) {
		super(table);
	}

	public void getFormattedData() {
		super.getUnFormattedData();
		for(int i=0; i<super.results.length; i++){
			super.results[i][0]=MainClass.conn.queryResults(String.format("SELECT Title FROM Book WHERE ISBN=\'%s\'",super.results[i][0]))[0][0];
			super.results[i][1]=MainClass.conn.queryResults(String.format("SELECT Format FROM Format WHERE Format_ID=%s",super.results[i][1]))[0][0];
			super.results[i][2]=MainClass.conn.queryResults(String.format("SELECT Support FROM Support WHERE Support_ID=%s",super.results[i][2]))[0][0];
		}
	}

	public void deleteRow(Object[] key) throws SQLException {
		key[0]=MainClass.conn.queryResults("SELECT ISBN FROM Book WHERE Title=\'"+key[0]+"\'")[0][0];
		key[1]=MainClass.conn.queryResults("SELECT Format_ID FROM Format WHERE Format =\'"+key[1]+"\'")[0][0];
		key[2]=MainClass.conn.queryResults("SELECT Support_ID FROM Support WHERE Support =\'"+key[2]+"\'")[0][0];
		String query=	String.format("DELETE FROM %s WHERE %s=\'%s\' AND %s=%s AND %s=%s", super.table, 
						super.columns[0], (String)key[0], super.columns[1], (String)key[1], super.columns[2], (String)key[2]);
		try {
			MainClass.conn.updateQuery(query);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	public void updateRow(Object[] key, Object[] data) throws SQLException {
		key[0]=MainClass.conn.queryResults("SELECT ISBN FROM Book WHERE Title=\'"+key[0]+"\'")[0][0];
		key[1]=MainClass.conn.queryResults("SELECT Format_ID FROM Format WHERE Format =\'"+key[1]+"\'")[0][0];
		key[2]=MainClass.conn.queryResults("SELECT Support_ID FROM Support WHERE Support =\'"+key[2]+"\'")[0][0];
		String query=	String.format("UPDATE %s SET %s=%s, %s=%s WHERE %s=\'%s\' AND %s=%s AND %s=%s", super.table, 
						super.columns[3], (String)data[3], super.columns[4], (String)data[4],
						super.columns[0], (String)key[0], super.columns[1], (String)key[1], super.columns[2], (String)key[2]);
		try {
			MainClass.conn.updateQuery(query);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	public void insertRow(Object[] data) throws SQLException {
		data[0]=MainClass.conn.queryResults("SELECT ISBN FROM Book WHERE Title=\'"+data[0]+"\'")[0][0];
		data[1]=MainClass.conn.queryResults("SELECT Format_ID FROM Format WHERE Format =\'"+data[1]+"\'")[0][0];
		data[2]=MainClass.conn.queryResults("SELECT Support_ID FROM Support WHERE Support =\'"+data[2]+"\'")[0][0];
		String query;
		String test=(String)data[4];
		System.out.println(test.length());
		if (test.length()!=0) {
			query = String.format("INSERT INTO %s (%s, %s, %s, %s, %s) VALUES (\'%s\',%s,%s,%s,%s)", super.table,
					super.columns[0], super.columns[1], super.columns[2], super.columns[3], super.columns[4],
					(String) data[0], (String) data[1], (String) data[2], (String) data[3], (String) data[4]);
		}
		else{
			query = String.format("INSERT INTO %s (%s, %s, %s, %s) VALUES (\'%s\',%s,%s,%s)", super.table,
					super.columns[0], super.columns[1], super.columns[2], super.columns[3],
					(String) data[0], (String) data[1], (String) data[2], (String) data[3]);
		}
		try {
			MainClass.conn.updateQuery(query);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	public Object[] getColumnIOData() {
		Object[] IOData=new Object[5];
		String[][] data;
		String[] newData;
		int c;
		data=MainClass.conn.queryResults("SELECT Title FROM Book");
		c=data.length;
		newData=new String[c];
		for(int i=0;i<c;i++){
			newData[i]=data[i][0];
		}
		IOData[0]=new Object[] {"Title",newData};
		data=MainClass.conn.queryResults("SELECT Format FROM Format");
		c=data.length;
		newData=new String[c];
		for(int i=0;i<c;i++){
			newData[i]=data[i][0];
		}
		IOData[1]=new Object[] {"Format",newData};
		data=MainClass.conn.queryResults("SELECT Support FROM Support");
		c=data.length;
		newData=new String[c];
		for(int i=0;i<c;i++){
			newData[i]=data[i][0];
		}
		IOData[2]=new Object[] {"Support",newData};
		IOData[3]="FltV";
		IOData[4]="IntV";
		return IOData;
	}
}
