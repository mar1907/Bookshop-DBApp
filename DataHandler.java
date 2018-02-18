package packall;

import java.sql.SQLException;

/**Abstract class that handles the data from and to the database. It is extended by specific classes for each table
 * 
 * @author Marius
 *
 */
public abstract class DataHandler {
	protected String[][] results;
	protected String[] columns;
	protected String table;
	
	/**Constructor for DataHandler objects
	 * 
	 * @param table	the table for which the object holds data
	 */
	public DataHandler(String table) {
		this.table = table;
	}
	
	/**
	 * This method runs the appropriate queries for each table in order to return meaningful data. It is accessible to subclasses and cannot be overridden. It affects the class fields.
	 */
	protected final void getUnFormattedData(){
		results=MainClass.conn.queryResults("SELECT * FROM "+table);
		String[][] res2=MainClass.conn.queryResults("SELECT * FROM Bookstore.INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME ='"+table+"'");
		columns=new String[res2.length];
		for(int i=0;i<res2.length;i++){
			columns[i]=res2[i][3];
		}
	}
	
	protected final void getUnFormattedData(String orderBy){
		results=MainClass.conn.queryResults("SELECT * FROM "+table+" ORDER BY "+orderBy+" ASC");
		String[][] res2=MainClass.conn.queryResults("SELECT * FROM Bookstore.INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME ='"+table+"'");
		columns=new String[res2.length];
		for(int i=0;i<res2.length;i++){
			columns[i]=res2[i][3];
		}
	}

	/**
	 * @return the results
	 */
	public final String[][] getResults() {
		return results;
	}

	/**
	 * @return the columns
	 */
	public final String[] getColumns() {
		return columns;
	}

	/**
	 * @return the table
	 */
	public final String getTable() {
		return table;
	}
	
	/**
	 * @return the insertRows
	 */
	public final Object getInsertRows() {
		//return insertRows;
		return null;
	}

	//abstract methods for update, insert, delete
	public abstract void getFormattedData();
	public abstract void deleteRow(Object[] key) throws SQLException;
	public abstract void updateRow(Object[] key, Object[] data) throws SQLException;
	public abstract void insertRow(Object[] data) throws SQLException;
	public abstract Object[] getColumnIOData();
	
}