package coupons.management.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.LinkedBlockingQueue;
/**
 * ConnectionPool implementation
 * @author Leonid S
 *
 */
public class ConnectionPool {
	private static ConnectionPool instance = new ConnectionPool();
	private LinkedBlockingQueue<Connection> connections = new LinkedBlockingQueue<>();
	private static final int MAX_CONNECTIONS = 15;
	
	/**
	 * default constructor
	 */
	private ConnectionPool() {
		
		for (int i = 0; i < MAX_CONNECTIONS; i++) {
			 
			try {
				Connection c = DriverManager.getConnection("jdbc:sqlserver://localhost;database=CouponsManagement;integratedSecurity=true");
				connections.offer(c);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		}
	}
	
	/**
	 * getInstance
	 * @return single instance of the class
	 */
	public static ConnectionPool getInstance() {
		return instance;
	}
	
	/**
	 * get Connection from a pool
	 * @return
	 */
	public synchronized Connection getConnection() {
		
		if(connections.isEmpty()) {
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//System.out.println(connections.size());
		Connection con = connections.poll();
		return con;
	}
	
	/**
	 * return connection to the pool
	 * @param con
	 */
	public synchronized void returnConnection(Connection con) {
		connections.offer(con);
		notify();
	}
	
	/**
	 * close all connections
	 */
	public void closeAllConnections() {
		for (int i = 0; i < MAX_CONNECTIONS; i++) {
			Connection c = connections.poll();
			try {
				c.close();
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
		}
	}
	
}
