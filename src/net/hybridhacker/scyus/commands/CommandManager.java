package net.hybridhacker.scyus.commands;

import java.util.ArrayList;
import java.util.Arrays;

import com.skype.Chat;
import com.skype.ChatMessage;
import com.skype.ChatMessageAdapter;
import com.skype.SkypeException;
import com.skype.User;

import net.hybridhacker.scyus.Scyus;
import net.hybridhacker.scyus.commands.general.HelpCommand;
import net.hybridhacker.scyus.commands.general.PremiumCommand;
import net.hybridhacker.scyus.commands.spam.TextSpamCommand;

/**
 * Class that manages incomming messages and commands
 * 
 * @author Flaflo
 *
 */
public final class CommandManager extends ChatMessageAdapter {

	private final ArrayList<ICommand> commands;

	/**
	 * Constructs the Command Manager
	 */
	public CommandManager() {
		commands = new ArrayList<>();

		commands.add(new HelpCommand());
		commands.add(new TextSpamCommand());
		commands.add(new PremiumCommand());
	}

	@Override public void chatMessageSent(ChatMessage sentChatMessage) throws SkypeException { }

	@Override
	public void chatMessageReceived(ChatMessage receivedChatMessage) throws SkypeException {
		if (receivedChatMessage.getType().equals(ChatMessage.Type.SAID)) {
			try {
				Thread.sleep(5L); // Prevent pre sending
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			final String message = receivedChatMessage.getContent();

			if (message.length() > 1 && message.startsWith(Scyus.getInstance().getSettings().getCommandPrefix())) {
				final User sender = receivedChatMessage.getSender();
				final Chat chat = receivedChatMessage.getChat();

				final String[] splitted = message.split(" ");
				final int argsLength = splitted.length - 1;

				final String[] args = new String[argsLength];
				System.arraycopy(splitted, 1, args, 0, argsLength);

				if (!callCommand(splitted[0].replaceFirst(Scyus.getInstance().getSettings().getCommandPrefix(), ""),
						args, sender, chat))
					Scyus.getInstance().sendMessage(chat, "Could not recognize command.");
			}
		}
	}

	/**
	 * Executes a command
	 * 
	 * @param alias
	 *            the commands alias
	 * @param args
	 *            the arguments
	 * @param sender
	 *            the command sender
	 * @param chat
	 *            the chat the command was sent in
	 * @return true if the command was found
	 */
	public boolean callCommand(String alias, String[] args, User sender, Chat chat) {
		for (final ICommand command : commands)
			if (Arrays.binarySearch(command.getAliases(), alias.toLowerCase()) > -1) {
				command.onCommand(alias, args, sender, chat);

				return true;
			}

		return false;
	}

	/**
	 * @return a list of all commands
	 */
	public ArrayList<ICommand> getCommands() {
		return commands;
	}

	/**
	 * @param string
	 *            the command alias
	 * @return the command that belongs to the alias
	 */
	public ICommand getCommand(String string) {
		for (final ICommand command : commands)
			if (Arrays.binarySearch(command.getAliases(), string.toLowerCase()) > -1)
				return command;

		return null;
	}
}
