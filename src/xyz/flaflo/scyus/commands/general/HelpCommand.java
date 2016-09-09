package xyz.flaflo.scyus.commands.general;

import java.util.Arrays;

import com.skype.Chat;
import com.skype.User;

import xyz.flaflo.scyus.Scyus;
import xyz.flaflo.scyus.commands.ICommand;

/**
 * Prints a list of available commands and their description
 * 
 * @author Flaflo
 *
 */
public final class HelpCommand implements ICommand {

	@Override
	public void onCommand(String alias, String[] args, User sender, Chat chat) {
		if (args.length == 0) {
			Scyus.getInstance().sendMessage(chat, "Available Commands:");

			for (final ICommand command : Scyus.getInstance().getCommandManager().getCommands())
				Scyus.getInstance()
						.sendMessage(chat, Scyus.getInstance().getSettings().getCommandPrefix()
								+ command.getAliases()[0] + " " + (command.getArguments() != null
										? Arrays.toString(command.getArguments()) : ""));
		} else {
			final ICommand command = Scyus.getInstance().getCommandManager().getCommand(args[0]);

			if (command != null) {
				Scyus.getInstance().sendMessage(chat, "Command \"" + command.getAliases()[0] + "\":");

				final String help = command.getHelp();
				final String[] cargs = command.getArguments();
				final String[] aliases = command.getAliases();

				if (help != null)
					Scyus.getInstance().sendMessage(chat, "Help: " + help);

				if (args != null)
					Scyus.getInstance().sendMessage(chat, "Args: " + Arrays.toString(cargs));

				if (aliases != null)
					Scyus.getInstance().sendMessage(chat, "Alias: " + Arrays.toString(aliases));
			} else
				Scyus.getInstance().sendMessage(chat, "That command could not be found.");
		}
	}

	@Override
	public String[] getAliases() {
		return new String[] { "help" };
	}

	@Override
	public String[] getArguments() {
		return new String[] { "command" };
	}

	@Override
	public String getHelp() {
		return "Its like a lexikon, just use it!";
	}

}
