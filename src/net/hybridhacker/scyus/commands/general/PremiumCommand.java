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

package net.hybridhacker.scyus.commands.general;

import com.skype.Chat;
import com.skype.User;

import net.hybridhacker.scyus.Scyus;
import net.hybridhacker.scyus.commands.ICommand;
import net.hybridhacker.scyus.user.ScyusUser;

/**
 * A Command to check premium status
 * @author Flaflo
 *
 */
public final class PremiumCommand implements ICommand {

	@Override
	public void onCommand(String alias, String[] args, User sender, Chat chat) {
		final ScyusUser suser = Scyus.getInstance().getUserManager().getUser(sender.getId());
		
		Scyus.getInstance().sendMessage(chat, "You are" + (suser.isPremium() ? " " : " not ") + "premium.");
	}

	@Override
	public String[] getAliases() {
		return new String[] { "premium" };
	}

	@Override
	public String[] getArguments() {
		return null;
	}

	@Override
	public String getHelp() {
		return "Shows your premium status";
	}

}
