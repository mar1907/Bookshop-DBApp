package packall;
import java.sql.*;

public class Connector {
	public Connection con;
	public void connectToDB() throws Exception{
		String url ="jdbc:sqlserver://localhost;database=Bookstore;integratedSecurity=true";
		Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		con = DriverManager.getConnection(url);
	}
	public void closeConnection(){
		try{
			con.close();
		}
		catch(SQLException e){
			e.printStackTrace();
		}
	}
	public String[][] queryResults(String query){
		try{
			int rows=0,cols=0;
			Statement stmt=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			ResultSet rs=stmt.executeQuery(query);
			ResultSetMetaData rsmd=rs.getMetaData();
			cols=rsmd.getColumnCount();
			cols--;
			if(rs.last()){
				rows=rs.getRow();
				rows--;
			}
			rs.first();
			String [][] results=new String[rows+1][cols+1];
			for(int i=0;i<=rows;i++){
				for(int j=0;j<=cols; j++){
					results[i][j]=""+rs.getObject(j+1);
				}
				rs.next();
			}
			return results;
		}
		catch(Exception e){
			return null;
		}
	}
	
	public void updateQuery(String query) throws SQLException{
		try {
			Statement stmt=con.createStatement();
			stmt.executeUpdate(query);
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		}
	}
}
