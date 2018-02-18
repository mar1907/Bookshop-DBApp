package packall;

public class MainClass {
	public static Connector conn;
	public static void main(String[] args){
		conn=new Connector();
		try{
			conn.connectToDB();
			MainPage mainFrame=new MainPage();
			mainFrame.setVisible(true);
		}
		catch(Exception e){
			NoConnection noCon=new NoConnection(0);
			noCon.setVisible(true);
			noCon.setLocation(300,150);
			return;
		}
		finally{}
	}
}
