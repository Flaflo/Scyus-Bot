package xyz.flaflo.scyus.commands;

import java.util.Arrays;

import com.skype.Chat;
import com.skype.User;

/**
 * Represents a command that can be called by a user
 * 
 * @author Flaflo
 *
 */
public interface ICommand {

	/**
	 * Called when the command is called
	 * 
	 * @param alias
	 *            the alias the command is called with
	 * @param args
	 *            the arguments given with
	 * @param sender
	 *            the command sender
	 * @param chat
	 *            the chat the command was sent in
	 */
	public void onCommand(String alias, String[] args, User sender, Chat chat);

	/**
	 * @return the commands aliases
	 */
	public String[] getAliases();

	/**
	 * @return the arguments the command consumes
	 */
	public String[] getArguments();

	/**
	 * @return the help description
	 */
	public String getHelp();

	/**
	 * @param args
	 *            the command arguments
	 * @return a printable exception string for too less arguments given
	 */
	static String missingArgsEx(final ICommand command) {
		return "Not enough arguments for " + Arrays.toString(command.getAliases());
	}

	/**
	 * @param cooldown the coodlown
	 * @return a printable exception for active cooldown
	 */
	static String cooldownEx(long cooldown) {
		return "Please wait " + (cooldown / 1000) + " more seconds!";
	}
	
	/**
	 * @param args the arguments to implode
	 * @return a string array glued with a space char to one string
	 */
	static String implodeArgs(String[] args) {
		return implodeArgs(args, 0, args.length);
	}
	
	/**
	 * @param args the arguments to implode
	 * @param start the start of the imploding
	 * @param end the end of the imploding
	 * @return a string array glued with a space char to one string
	 */
	static String implodeArgs(String[] args, int start, int end) {
		final StringBuilder text = new StringBuilder();

		end = Math.min(Math.max(end, 0), args.length);
		start = Math.min(Math.max(start, 0), end);
		
		for (int i = start; i < end; i++) {
			if (i > start)
				text.append(" ");

			text.append(args[i]);
		}
		
		return text.toString();
	}
}