package net.hybridhacker.scyus.tools;

import com.skype.Friend;
import com.skype.Skype;
import com.skype.SkypeException;
import com.skype.User;
import com.skype.User.BuddyStatus;

import net.hybridhacker.scyus.Scyus;

/**
 * Represents a Skype text broadcast
 * 
 * @author Flaflo
 *
 */
public final class Broadcaster extends Thread {

	private final User creator;
	private final String message;

	/**
	 * Constructs the Broadcaster
	 * 
	 * @param creator
	 *            the broadcast creator
	 * @param message
	 *            the broadcast message
	 */
	public Broadcaster(User creator, String message) {
		this.creator = creator;
		this.message = message;
	}

	public void run() {
		try {
			for (final Friend friend : Skype.getContactList().getAllFriends()) {
				if (friend.getBuddyStatus().equals(BuddyStatus.ADDED)) {
					final User user = User.getInstance(friend.getId());

					user.send(message);
					Scyus.getInstance().sendMessage(user, "Broadcast by " + creator.getFullName());
				}
			}
		} catch (SkypeException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @return the creator
	 */
	public User getCreator() {
		return creator;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}
}
