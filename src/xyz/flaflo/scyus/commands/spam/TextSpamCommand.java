package xyz.flaflo.scyus.commands.spam;

import com.skype.Chat;
import com.skype.User;

import xyz.flaflo.scyus.Scyus;
import xyz.flaflo.scyus.commands.ICommand;
import xyz.flaflo.scyus.tools.Spammer;
import xyz.flaflo.scyus.user.ScyusUser;

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
