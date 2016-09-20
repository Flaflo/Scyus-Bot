/*******************************************************************************
* Copyright 2016 HybridHacker.net
*
* Licensed under the GNU Public License, Version 3.0 (the "License");
* you may not use this file except in compliance with the License.				
* You may obtain a copy of the License at
*
*     https://www.gnu.org/licenses/gpl.html
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.		
********************************************************************************/

package net.hybridhacker.scyus.user;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.HashMap;

import net.hybridhacker.scyus.Scyus;
import net.hybridhacker.scyus.commands.ICommand;

/**
 * Represents a Scyus Bot User
 * 
 * @author Flaflo
 *
 */
public final class ScyusUser {

	private final HashMap<ICommand, Long> commandCooldowns;

	private final String id;

	private long premiumEnd;

	private long lastInfoCheck;
	
	/**
	 * Constructs the User
	 * 
	 * @param id
	 *            the user id
	 */
	public ScyusUser(final String id) {
		this(id, 0);
		
		try {
			this.downloadUserInfo();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Constructs the User
	 * 
	 * @param id
	 *            the user id
	 * @param premiumEnd
	 *            the time the premium ends
	 */
	public ScyusUser(final String id, final long premiumEnd) {
		this.commandCooldowns = new HashMap<>();

		this.id = id;
		this.premiumEnd = premiumEnd;
	}

	/**
	 * Downloads User info from mysql
	 * 
	 * @throws SQLException
	 */
	private void downloadUserInfo() throws SQLException {
		final PreparedStatement userStatement = Scyus.getInstance().getMySql().getConnection()
				.prepareStatement("SELECT * FROM users WHERE id = ?");

		userStatement.setString(1, id);

		final ResultSet userResultSet = userStatement.executeQuery();

		if (userResultSet.next()) {
			final Timestamp premiumEnd = userResultSet.getTimestamp("premiumEnd");

			if (premiumEnd != null)
				this.premiumEnd = premiumEnd.getTime();

			userResultSet.close();
		} else {
			Scyus.getInstance().getLogger().info("Could not find user \"" + id + "\", creating one.");
			uploadUserInfo();
		}
		
		lastInfoCheck = System.currentTimeMillis();
	}

	/**
	 * Creates the user in the mysql database
	 * 
	 * @throws SQLException
	 */
	private void uploadUserInfo() throws SQLException {
		final PreparedStatement userStatement = Scyus.getInstance().getMySql().getConnection()
				.prepareStatement("INSERT INTO users (id, premiumEnd) VALUES ('" + id + "', ?)");

		userStatement.setTimestamp(1, Timestamp.from(Instant.EPOCH));

		userStatement.executeUpdate();
	}

	/**
	 * Adds a cooldown to a command
	 * 
	 * @param command
	 *            the command to add to
	 * @param duration
	 *            the cooldown duration in milliseconds
	 */
	public void addCooldown(final ICommand command, final long duration) {
		if (commandCooldowns.containsKey(command))
			commandCooldowns.replace(command, System.currentTimeMillis() + duration);
		else
			commandCooldowns.put(command, System.currentTimeMillis() + duration);
	}

	/**
	 * Removes a cooldown from a command
	 * 
	 * @param command
	 *            the command to remove from
	 */
	public void removeCooldown(final ICommand command) {
		commandCooldowns.remove(command);
	}

	/**
	 * @param command
	 *            the command of the cooldown
	 * @return true if the user has a cooldown
	 */
	public boolean hasCooldown(final ICommand command) {
		return commandCooldowns.containsKey(command) && commandCooldowns.get(command) >= System.currentTimeMillis();
	}

	/**
	 * @param command
	 *            the command to get from
	 * @return the time till cooldown end in milliseconds
	 */
	public long getCooldown(final ICommand command) {
		return commandCooldowns.containsKey(command) ? commandCooldowns.get(command) - System.currentTimeMillis() : 0;
	}

	/**
	 * @return the id
	 */
	public String getSkypeUserId() {
		return id;
	}

	/**
	 * @return the premium
	 */
	public boolean isPremium() {
		if (System.currentTimeMillis() - lastInfoCheck >= 10000)
			try {
				this.downloadUserInfo();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		
		return System.currentTimeMillis() < premiumEnd;
	}

	/**
	 * @return the premiumEnd
	 */
	public long getPremiumEnd() {
		if (System.currentTimeMillis() - lastInfoCheck >= 10000)
			try {
				this.downloadUserInfo();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		
		return premiumEnd;
	}

	/**
	 * @return the commandCooldowns
	 */
	public HashMap<ICommand, Long> getCommandCooldowns() {
		return commandCooldowns;
	}
}
