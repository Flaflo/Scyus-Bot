package xyz.flaflo.scyus;

import java.util.Properties;
import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import org.skife.config.ConfigurationObjectFactory;

import com.skype.Chat;
import com.skype.Skype;
import com.skype.SkypeException;
import com.skype.User;

import xyz.flaflo.scyus.automation.AutoAccepter;
import xyz.flaflo.scyus.commands.CommandManager;
import xyz.flaflo.scyus.config.IScyusSettings;
import xyz.flaflo.scyus.mysql.MySQL;
import xyz.flaflo.scyus.user.ScyusUserManager;

/**
 * Scyus Bot class
 * 
 * @author Flaflo
 *
 */
public final class Scyus {

	private static Scyus instance;

	private final Logger logger;

	private final CommandManager commandManager;
	private final ScyusUserManager userManager;

	private final AutoAccepter autoAccepter;

	private IScyusSettings settings;

	private final MySQL mySql;

	private boolean attached;

	/**
	 * Constructs the Scyus
	 */
	public Scyus() {
		instance = this;

		this.loadSettings();

		this.logger = Logger.getLogger("Scyus");
		this.logger.setUseParentHandlers(false);
		final ConsoleHandler consoleHandler = new ConsoleHandler();
		consoleHandler.setFormatter(new Formatter() {
			@Override
			public String format(LogRecord logRecord) {
				return new StringBuilder().append(logRecord.getLoggerName()).append(' ').append(logRecord.getLevel().getName().toUpperCase()).append(": ").append(this.formatMessage(logRecord)).append('\n');
			}
		});
		this.logger.addHandler(consoleHandler);

		this.commandManager = new CommandManager();

		this.autoAccepter = new AutoAccepter();
		this.autoAccepter.start();

		this.mySql = new MySQL(settings.getMySqlHost(), settings.getMySqlPort(), settings.getMySqlUsername(),
				settings.getMySqlPassword(), settings.getMySqlDatabse());

		this.userManager = new ScyusUserManager();

		this.attach();
	}

	/**
	 * Loads the Settings
	 */
	private void loadSettings() {
		settings = new ConfigurationObjectFactory(new Properties()).build(IScyusSettings.class);
	}

	/**
	 * Attaches the Bot to Skype
	 */
	private void attach() {
		Skype.setDaemon(false);

		try {
			Skype.addApplication("Scyus");
			Skype.addChatMessageListener(commandManager);

			logger.info("Attached successfully to Skype.");

			attached = true;
		} catch (SkypeException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Sends a formatted message to a chat
	 * 
	 * @param chat
	 *            the chat to send to
	 * @param msg
	 *            the message to send
	 */
	public void sendMessage(Chat chat, String msg) {
		try {
			chat.send(String.format(settings.getSendMessageFormat(), settings.getNickname(), msg));
		} catch (SkypeException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Sends a formatted message to a user
	 * 
	 * @param user
	 *            the user to send to
	 * @param msg
	 *            the message to send
	 */
	public void sendMessage(User user, String msg) {
		try {
			user.send(String.format(settings.getSendMessageFormat(), settings.getNickname(), msg));
		} catch (SkypeException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @return the userManager
	 */
	public ScyusUserManager getUserManager() {
		return userManager;
	}

	/**
	 * @return the autoAccepter
	 */
	public AutoAccepter getAutoAccepter() {
		return autoAccepter;
	}

	/**
	 * @return the mySql
	 */
	public MySQL getMySql() {
		return mySql;
	}

	/**
	 * @param settings
	 *            the settings to set
	 */
	public void setSettings(IScyusSettings settings) {
		this.settings = settings;
	}

	/**
	 * @return the settings
	 */
	public IScyusSettings getSettings() {
		return settings;
	}

	/**
	 * @return the bots attachement state
	 */
	public boolean isAttached() {
		return attached;
	}

	/**
	 * @return the command manager
	 */
	public CommandManager getCommandManager() {
		return commandManager;
	}

	/**
	 * @return the logger
	 */
	public Logger getLogger() {
		return logger;
	}

	/**
	 * @return the Singleton instance of Scyus
	 */
	public static Scyus getInstance() {
		return instance;
	}
}
