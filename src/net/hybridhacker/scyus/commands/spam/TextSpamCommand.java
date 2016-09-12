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

package net.hybridhacker.scyus.commands.spam;

import com.skype.Chat;
import com.skype.User;

import net.hybridhacker.scyus.Scyus;
import net.hybridhacker.scyus.commands.ICommand;
import net.hybridhacker.scyus.tools.Spammer;
import net.hybridhacker.scyus.user.ScyusUser;

/**
 * Spams an user with a specified amount of text
 * 
 * @author Flaflo
 *
 */
public final class TextSpamCommand implements ICommand {

	@Override
	public void onCommand(String alias, String[] args, User sender, Chat chat) {
		if (args.length >= 2) {
			final ScyusUser suser = Scyus.getInstance().getUserManager().getUser(sender.getId());

			final User user = User.getInstance(args[0]);

			if (user != null) {
				if (Scyus.getInstance().getUserManager().getUser(user.getId()).isPremium())
					Scyus.getInstance().sendMessage(chat, "Sorry, but this user has bought premium!");
				else {
					final Spammer spammer = new Spammer(sender, user, ICommand.implodeArgs(args, 1, args.length),
							Scyus.getInstance().getSettings().getMaxTextSpamAmount());
	
					if ((!suser.hasCooldown(this) && !suser.isPremium()) || suser.isPremium()) {
						spammer.start();
						Scyus.getInstance().sendMessage(chat, "Spamming \"" + args[0] + "\" " + Scyus.getInstance().getSettings().getMaxTextSpamAmount() + " times!");
						
						suser.addCooldown(this, Scyus.getInstance().getSettings().getTextSpamCooldown());
					} else if (!suser.isPremium())
						Scyus.getInstance().sendMessage(chat,
								"Please wait " + (suser.getCooldown(this) / 1000) + " more seconds!");
				}
			} else
				Scyus.getInstance().sendMessage(chat, "Can't find user \"" + args[0] + "\"!");
		} else
			Scyus.getInstance().sendMessage(chat, ICommand.missingArgsEx(this));
	}

	@Override
	public String[] getAliases() {
		return new String[] { "spam", "textspam", "tspam" };
	}

	@Override
	public String[] getArguments() {
		return new String[] { "user", "text" };
	}

	@Override
	public String getHelp() {
		return "Spams a user certain times with a text";
	}

}
