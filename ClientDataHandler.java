package packall;

import java.sql.SQLException;

public class ClientDataHandler extends DataHandler {

	/**
	 * @param table the table for the superclass
	 */
	public ClientDataHandler(String table) {
		super(table);
	}

	public void getFormattedData() {
		super.getUnFormattedData();
	}

	public void deleteRow(Object[] key) throws SQLException {
		String query=String.format("DELETE FROM %s WHERE %s=%s", super.table, super.columns[0], (String)key[0]);
		try {
			MainClass.conn.updateQuery(query);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	public void updateRow(Object[] key, Object[] data) throws SQLException {
		String query=String.format("UPDATE %s SET %s='%s', %s='%s', %s='%s' where %s=%s", super.table, super.columns[1], (String)data[1], super.columns[2], (String)data[2], super.columns[3], (String)data[3], super.columns[0], (String)key[0]);
		try {
			MainClass.conn.updateQuery(query);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	public void insertRow(Object[] data) throws SQLException {
		if(((String)data[1]).length()==0 || ((String)data[2]).length()==0 || ((String)data[3]).length()==0){
			SQLException e =new SQLException("");
			throw e;
		}
		String query=String.format("INSERT INTO %s (%s, %s, %s) VALUES ('%s','%s','%s');", super.table, super.columns[1], super.columns[2], super.columns[3], (String)data[1], (String)data[2], (String)data[3]);
		try {
			MainClass.conn.updateQuery(query);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	public Object[] getColumnIOData() {
		Object[] IOData=new Object[4];
		IOData[0]="IntX";
		IOData[1]="StrV";
		IOData[2]="StrV";
		IOData[3]="StrV";
		return IOData;
	}
}