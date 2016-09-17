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

package net.hybridhacker.scyus.automation;

import com.skype.Friend;
import com.skype.Skype;
import com.skype.SkypeException;
import com.skype.User;

import net.hybridhacker.scyus.Scyus;

/**
 * Accepts automatically Friend requests
 * 
 * @author Flaflo
 *
 */
public final class AutoAccepter extends Thread {

	private volatile boolean running;

	/**
	 * Constructs the Autoaccepter 
	 */
	public AutoAccepter() {
		this.setName("Auto Accept Thread");
		this.setDaemon(true);
	}
	
	@Override
	public synchronized void start() {
		super.start();

		running = true;
	}

	@Override
	public void run() {
		while (running) {
			if (Scyus.getInstance().isAttached()) {
				try {
					final Friend[] requests = Skype.getContactList().getAllUserWaitingForAuthorization();

					if (requests != null && requests.length > 0)
						for (final Friend friend : requests) {
							final User user = User.getInstance(friend.getId());
							Scyus.getInstance().getLogger().info("Accepting Friend request " + user.getId());

							friend.setAuthorized(true);
							Scyus.getInstance().sendMessage(user,
									"Welcome to " + Scyus.getInstance().getSettings().getNickname() + " type \""
											+ Scyus.getInstance().getSettings().getCommandPrefix()
											+ "help\" for help.");
						}
				} catch (SkypeException e) {
					e.printStackTrace();
				}
			}

			try {
				Thread.sleep(1000L);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * @return the running
	 */
	public boolean isRunning() {
		return running;
	}

	/**
	 * @param running
	 *            the running to set
	 */
	public void setRunning(boolean running) {
		this.running = running;
	}
}
