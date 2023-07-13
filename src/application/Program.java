package application;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import db.DB;
import db.DbException;
import db.DbIntegrityException;

public class Program {

	public static void main(String[] args) {
	
		Connection conn = null;
		Statement st = null;
		try {
			conn = DB.getConnection();
			
			conn.setAutoCommit(false);
			
			st = conn.createStatement();
			/*In this case the system update the first command, but the second not. Because the error FAKE*/
			int rouws1 = st.executeUpdate("UPDATE seller SET BaseSalary = 2090 WHERE DepartmentId = 1");
			/*FAKE ERROR
			int x = 1;
			if (x <2) {  
				throw new SQLException("Fake Error");
			}*/
			
			int rouws2 = st.executeUpdate("UPDATE seller SET BaseSalary = 3090 WHERE DepartmentId = 2");
			
			conn.commit();
			
			System.out.println("rouws1 " + rouws1);
			System.out.println("rouws2 " + rouws2);
		}
		catch (SQLException e) {
			try {
				conn.rollback();
				throw new DbException("Transaction rolled back! Caused by: "+e.getMessage());
			} catch (SQLException e1) {
				e1.printStackTrace();
				throw new DbException("Erro trying to rollback! Caused by: "+e1.getMessage());
			}
		} 
		
		finally {
			DB.closeStatement(st);
			DB.closeConnection();
		}

	}
}
