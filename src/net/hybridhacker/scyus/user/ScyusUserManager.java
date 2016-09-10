package net.hybridhacker.scyus.user;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;

import net.hybridhacker.scyus.Scyus;

/**
 * Manages Scyus Users
 * 
 * @author Flaflo
 *
 */
public final class ScyusUserManager {

	private final HashMap<String, ScyusUser> users;

	/**
	 * Constructs the user manager
	 */
	public ScyusUserManager() {
		users = new HashMap<>();
		
		new Thread(() -> {
			try {
				downloadExistingUsers();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}).start();
	}
	
	/**
	 * Downloads a list of existing users and registers them
	 * @throws SQLException 
	 */
	private void downloadExistingUsers() throws SQLException {
		Scyus.getInstance().getLogger().info("Downloading existing Users from Database");
		
		final PreparedStatement userListStatement = Scyus.getInstance().getMySql().getConnection().prepareStatement("SELECT * FROM users");
		
		final ResultSet userListResultSet = userListStatement.executeQuery();
		
		while (userListResultSet.next()) {
			final String id = userListResultSet.getString("id");
			final long premiumEnd = userListResultSet.getTimestamp("premiumEnd").getTime();
			
			this.users.put(id, new ScyusUser(id, premiumEnd));
			
			Scyus.getInstance().getLogger().info("Downloading User \"" + id + "\" premium: " + (premiumEnd > System.currentTimeMillis() ? "yes" : "no"));
		}
	}

	/**
	 * @return the users
	 */
	public Collection<ScyusUser> getUsers() {
		return users.values();
	}

	/**
	 * Returns the scyus user instance that belongs to the id, if no user was
	 * found it creates a new instance and registers it
	 * 
	 * @param id the skype id
	 * @return the scyus user instance
	 */
	public ScyusUser getUser(final String id) {
		ScyusUser user = users.get(id);
		
		if (user == null) {
			user = new ScyusUser(id);
		
			users.put(id, user);
		}
		
		return user;
	}
}
