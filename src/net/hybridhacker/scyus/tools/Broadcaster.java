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
					Scyus.getInstance().sendMessage(user, "Broadcast by \"" + creator.getId() + "\"");
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
