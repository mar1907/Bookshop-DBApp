package packall;

import java.sql.SQLException;

/**This class can handle data for the following tables: Author, Format, Genre, Language, Publisher, Support, Payment_method
 * @author Marius
 *
 */
public class AuthorDataHandler extends DataHandler{
	/**
	 * @param table the table for the superclass
	 */
	public AuthorDataHandler(String table){
		super(table);
	}
		
	public void getFormattedData(){
		super.getUnFormattedData();
	}

	public void deleteRow(Object[] key) throws SQLException{
		//method: try to delete the row, if unsuccessful, tell the user to delete associated data first
		String query=String.format("DELETE FROM %s WHERE %s=%s", super.table, super.columns[0], (String)key[0]);
		try {
			MainClass.conn.updateQuery(query);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	public void updateRow(Object[] key, Object[] data) throws SQLException{
		String query=String.format("UPDATE %s SET %s='%s' where %s=%s", super.table, super.columns[1], (String)data[1], super.columns[0], (String)key[0]);
		try {
			MainClass.conn.updateQuery(query);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	public void insertRow(Object[] data) throws SQLException{
		if(((String)data[1]).length()==0){
			SQLException e=new SQLException("");
			throw e;
		}
		String query=String.format("INSERT INTO %s (%s) VALUES ('%s');", super.table, super.columns[1], (String)data[1]);
		try {
			MainClass.conn.updateQuery(query);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	public Object[] getColumnIOData() {
		Object[] IOData=new Object[2];
		IOData[0]="IntX";
		IOData[1]="StrV";
		return IOData;
	}
	
}
