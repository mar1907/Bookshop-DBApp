package packall;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ListIterator;

public class BookDataHandler extends DataHandler {
	/**
	 * @param table the table for the superclass
	 */
	public BookDataHandler(String table) {
		super(table);
	}

	public void getFormattedData() {
		super.getUnFormattedData();
		ArrayList<String> aL=new ArrayList<String>();
		for(int i=0;i<super.columns.length;i++){
			aL.add(super.columns[i]);
		}
		aL.add("Genres");
		columns=(String[])aL.toArray(new String[0]);
		for(int i=0; i<super.results.length; i++){
			aL=new ArrayList<String>();
			for(int j=0;j<super.results[i].length;j++){
				aL.add(super.results[i][j]);
			}
			aL.add("");
			results[i]=(String[])aL.toArray(new String[0]);
			super.results[i][3]=MainClass.conn.queryResults(String.format("SELECT Publisher_Name FROM Publisher WHERE Publisher_ID=%s",super.results[i][3]))[0][0];
			super.results[i][2]=MainClass.conn.queryResults(String.format("SELECT Author_Name FROM Author WHERE Author_ID=%s",super.results[i][2]))[0][0];
			super.results[i][4]=MainClass.conn.queryResults(String.format("SELECT Language FROM Language WHERE Language_ID=%s",super.results[i][4]))[0][0];
			String newRow="";
			String[][] outerResults=MainClass.conn.queryResults("SELECT Genre.Genre FROM Book INNER JOIN Book_Genre ON Book.ISBN = Book_Genre.ISBN INNER JOIN Genre ON Book_Genre.Genre_ID = Genre.Genre_ID WHERE (Book.ISBN = \'"+results[i][0]+"\')");
			if (outerResults!=null) {
				newRow=outerResults[0][0];
				for (int j = 1; j < outerResults.length; j++) {
					newRow+=", "+outerResults[j][0];
				}
				results[i][results[i].length - 1] = newRow;
			}
			else{
				results[i][results[i].length - 1]="-";
			}
		}
	}

	public void deleteRow(Object[] key) throws SQLException {
		String query=String.format("DELETE FROM Book_Genre WHERE ISBN=\'%s\'", (String)key[0]);
		MainClass.conn.updateQuery(query);
		query=String.format("DELETE FROM Book WHERE ISBN=\'%s\'", (String)key[0]);
		try {
			MainClass.conn.updateQuery(query);
		} catch (SQLException e) {
			throw e;
		}
	}

	public void updateRow(Object[] key, Object[] data) throws SQLException {
		data[2]=MainClass.conn.queryResults("SELECT Author_ID FROM Author WHERE Author_Name=\'"+data[2]+"\'")[0][0];
		data[3]=MainClass.conn.queryResults("SELECT Publisher_ID FROM Publisher WHERE Publisher_Name =\'"+data[3]+"\'")[0][0];
		data[4]=MainClass.conn.queryResults("SELECT Language_ID FROM Language WHERE Language =\'"+data[4]+"\'")[0][0];
		String query;
		String test1=(String)data[0];
		String test2=(String)data[1];
		if(test1.length()==0 || test2.length()==0){
			SQLException e =new SQLException("");
			throw e;
		}
		query=String.format("UPDATE %s SET %s=\'%s\', %s=%s, %s=%s, %s=%s WHERE %s=\'%s\'", 
							super.table, super.columns[1], (String)data[1], super.columns[2], (String)data[2], super.columns[3], 
							(String)data[3], super.columns[4], (String)data[4], super.columns[0], (String)key[0]);
		try {
			MainClass.conn.updateQuery(query);
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		}
		query=String.format("DELETE FROM Book_Genre WHERE ISBN=\'%s\'", (String)key[0]);
		MainClass.conn.updateQuery(query);
		ListIterator<String> ite=((ArrayList<String>)data[5]).listIterator();
		while(ite.hasNext()){
			String data1=MainClass.conn.queryResults("SELECT Genre_ID FROM Genre WHERE Genre=\'"+ite.next()+"\'")[0][0];
			query=String.format("INSERT INTO Book_Genre (ISBN,Genre_ID) VALUES (\'%s\',%s)", test1, data1);
			try {
				MainClass.conn.updateQuery(query);
			} catch (SQLException e) {
				e.printStackTrace();
				throw e;
			}
		}
	}

	public void insertRow(Object[] data) throws SQLException {
		data[2]=MainClass.conn.queryResults("SELECT Author_ID FROM Author WHERE Author_Name=\'"+data[2]+"\'")[0][0];
		data[3]=MainClass.conn.queryResults("SELECT Publisher_ID FROM Publisher WHERE Publisher_Name =\'"+data[3]+"\'")[0][0];
		data[4]=MainClass.conn.queryResults("SELECT Language_ID FROM Language WHERE Language =\'"+data[4]+"\'")[0][0];
		String query;
		String test1=(String)data[0];
		String test2=(String)data[1];
		if(test1.length()==0 || test2.length()==0){
			SQLException e =new SQLException("");
			throw e;
		}
		query=String.format("INSERT INTO %s (%s,%s,%s,%s,%s) VALUES (\'%s\',\'%s\',%s,%s,%s)", super.table,
							super.columns[0], super.columns[1], super.columns[2], super.columns[3], super.columns[4],
							test1, test2, (String)data[2], (String)data[3], (String)data[4]);
		try {
			MainClass.conn.updateQuery(query);
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		}
		ListIterator<String> ite=((ArrayList<String>)data[5]).listIterator();
		while(ite.hasNext()){
			String data1=MainClass.conn.queryResults("SELECT Genre_ID FROM Genre WHERE Genre=\'"+ite.next()+"\'")[0][0];
			query=String.format("INSERT INTO Book_Genre (ISBN,Genre_ID) VALUES (\'%s\',%s)", test1, data1);
			try {
				MainClass.conn.updateQuery(query);
			} catch (SQLException e) {
				e.printStackTrace();
				throw e;
			}
		}
	}

	public Object[] getColumnIOData() {
		Object[] IOData=new Object[6];
		String[][] data;
		String[] newData;
		int c;
		IOData[0]="StrV";
		IOData[1]="StrV";
		data=MainClass.conn.queryResults("SELECT Author_Name FROM Author");
		c=data.length;
		newData=new String[c];
		for(int i=0;i<c;i++){
			newData[i]=data[i][0];
		}
		IOData[2]=new Object[] {"Author_Name",newData};
		data=MainClass.conn.queryResults("SELECT Publisher_Name FROM Publisher");
		c=data.length;
		newData=new String[c];
		for(int i=0;i<c;i++){
			newData[i]=data[i][0];
		}
		IOData[3]=new Object[] {"Publisher_Name",newData};
		data=MainClass.conn.queryResults("SELECT Language FROM Language");
		c=data.length;
		newData=new String[c];
		for(int i=0;i<c;i++){
			newData[i]=data[i][0];
		}
		IOData[4]=new Object[] {"Language",newData};
		data=MainClass.conn.queryResults("SELECT Genre FROM Genre");
		c=data.length;
		newData=new String[c];
		for(int i=0;i<c;i++){
			newData[i]=data[i][0];
		}
		IOData[5]=new Object[] {"Genre",newData};;
		return IOData;
	}
}
