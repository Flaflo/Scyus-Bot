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

import com.skype.SkypeException;
import com.skype.User;

import net.hybridhacker.scyus.Scyus;

/**
 * Represents a spam attack on a skype user
 * 
 * @author Flaflo
 *
 */
public final class Spammer extends Thread {

	private final User creator, victim;

	private final int count;
	private int sent;

	private final String message;

	/**
	 * Constructs the Spammer
	 * 
	 * @param creator
	 *            the spam task creator
	 * @param victim
	 *            the spam victim
	 * @param message
	 *            the message to send
	 * @param count
	 *            the messages to send
	 */
	public Spammer(User creator, User victim, String message, int count) {
		this.creator = creator;
		this.victim = victim;
		this.message = message;
		this.count = count;
	}

	@Override
	public void run() {
		Scyus.getInstance().getLogger().info("[" + creator.getId() + "] Starting spam with count " + count);

		while (sent++ < count)
			try {
				victim.send(message);
			} catch (SkypeException e) {
				e.printStackTrace();
			}

		Scyus.getInstance().sendMessage(victim, "You got spammed by \"" + creator.getId() + "\"");
		Scyus.getInstance().getLogger().info("[" + creator.getId() + "] Spamming finished");
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @return the sent
	 */
	public int getSent() {
		return sent;
	}

	/**
	 * @return the creator
	 */
	public User getCreator() {
		return creator;
	}

	/**
	 * @return the victim
	 */
	public User getVictim() {
		return victim;
	}

	/**
	 * @return the count
	 */
	public int getCount() {
		return count;
	}
}
