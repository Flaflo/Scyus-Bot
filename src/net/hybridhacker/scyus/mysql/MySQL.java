package net.hybridhacker.scyus.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Class to communicate with MySQL
 * 
 * @author Flaflo
 *
 */
public final class MySQL {

	private final String host, username, password, database;
	private final int port;

	private Connection connection;

	/**
	 * Constructs the MySQL object
	 * 
	 * @param host
	 *            the host to connect to
	 * @param port
	 *            the port to connect to
	 * @param username
	 *            the user to authenticate with
	 * @param password
	 *            the password to authenticate with
	 * @param database
	 *            the database to connect to
	 */
	public MySQL(String host, int port, String username, String password, String database) {
		this.host = host;
		this.username = username;
		this.password = password;
		this.database = database;
		this.port = port;
	}

	/**
	 * Returns a mysql connection, if it is invalid, it creates a new instance
	 * 
	 * @return the connection
	 */
	public Connection getConnection() {
		if (!isConnectionValid())
			return createConnection();
		else
			return connection;
	}

	/**
	 * Invokes the mysql driver and creates a connection to the given
	 * information
	 * 
	 * @return the connection
	 */
	private Connection createConnection() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			return DriverManager.getConnection(String
					.format("jdbc:mysql://%s:%d/%s?useUnicode=true&characterEncoding=UTF-8", host, port, database),
					username, password);
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Closes the connection to the mysql server
	 */
	public void closeConnection() {
		if (isConnectionValid())
			try {
				this.connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
	}

	/**
	 * @return true if the connection is valid
	 */
	public boolean isConnectionValid() {
		try {
			return connection != null && !connection.isClosed() && connection.isValid(5);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}

	/**
	 * @return the host
	 */
	public String getHost() {
		return host;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @return the database
	 */
	public String getDatabase() {
		return database;
	}

	/**
	 * @return the port
	 */
	public int getPort() {
		return port;
	}
}
