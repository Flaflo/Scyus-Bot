package xyz.flaflo.scyus.commands.general;

import com.skype.Chat;
import com.skype.User;

import xyz.flaflo.scyus.Scyus;
import xyz.flaflo.scyus.commands.ICommand;
import xyz.flaflo.scyus.user.ScyusUser;

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
