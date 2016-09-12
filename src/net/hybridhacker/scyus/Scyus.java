/*******************************************************************************
* Copyright 2016 HybridHacker.net											   *
*																			   *
* Licensed under the GNU Public License, Version 3.0 (the "License");		   *
* you may not use this file except in compliance with the License.			   *				
* You may obtain a copy of the License at									   *
*																			   *
*     https://www.gnu.org/licenses/gpl.html									   *
*																			   *
* Unless required by applicable law or agreed to in writing, software		   *
* distributed under the License is distributed on an "AS IS" BASIS,			   *
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.	   *
* See the License for the specific language governing permissions and		   *
* limitations under the License.											   *		
********************************************************************************/

package net.hybridhacker.scyus;

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

import net.hybridhacker.scyus.automation.AutoAccepter;
import net.hybridhacker.scyus.commands.CommandManager;
import net.hybridhacker.scyus.config.IScyusSettings;
import net.hybridhacker.scyus.mysql.MySQL;
import net.hybridhacker.scyus.user.ScyusUserManager;

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
