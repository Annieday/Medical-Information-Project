

import java.sql.*;


public class Connector {

	//Connection object
	private Connection conn; 
	
	//Class constructor: initializes the connection using Oracle's JDBC
	public Connector(String connInfo, String username, String password) {
		
		try {
			String m_driverName = "oracle.jdbc.driver.OracleDriver";
			Class<?> drvClass = Class.forName(m_driverName);
			DriverManager.registerDriver((Driver)drvClass.newInstance());
			conn = DriverManager.getConnection(connInfo, username, password);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//Closes the connection when we are done (remember to ALWAYS close the connection when no longer required).	
	public void closeConnection () {
		try {
			conn.close(); 
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	//A nice method that executes non queries such as inserts, updates, etc.
	//Note: this returns true if successful, false otherwise. 
	
	public Boolean executeNonQuery(String statement)  {
		try {
			Statement stmt = conn.createStatement();
			Boolean rs = stmt.execute(statement);
			return rs;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return false;
	}

	//This method executes a SQL query and return the ResultSet
	
	public ResultSet executeQuery(String query)  {
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			return rs;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	
}
