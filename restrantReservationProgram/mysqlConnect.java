package restrantReservationProgram;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class mysqlConnect {
	
	public static Statement stmt;
	public ResultSet rs;
	public static Connection con;
	
	public static final String root = "jdbc:mysql://localhost:3306/Restrant?useSSL=false";
	public static final String id ="root";
	public static final String passwd = "sungeun3831";
	public static int connectAlready = 0;
	
	public mysqlConnect(){}
	
	public void DBSetting(){
		
		try{
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection(root,id,passwd);
			stmt = con.createStatement();
			stmt.executeQuery("use Restrant");
		}catch(Exception e){
			e.printStackTrace();
		}
		connectAlready=1;
	}
	
	public int GetLastSeqFromDatetable() throws SQLException{
		PreparedStatement pstmt = null;
		pstmt = con.prepareStatement("SELECT seq FROM Date");
		ResultSet rs = pstmt.executeQuery();
		int SeqCount = 0;
		while(rs.next()){
			SeqCount = rs.getInt("seq");
		}
		SeqCount++;
		
		if(rs != null) try{rs.close();}catch(SQLException sqle){}
		
		if(pstmt != null) try{pstmt.close();}catch(SQLException sqle){}
		
		return SeqCount;
	}
	
}
