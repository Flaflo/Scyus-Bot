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

package net.hybridhacker.scyus;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.time.Instant;
import java.util.Date;
import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import org.skife.config.Config;
import org.skife.config.ConfigurationObjectFactory;
import org.skife.config.Default;

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

		try {
			this.loadSettings();
		} catch (IOException e) {
			e.printStackTrace();
		}

		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			try {
				this.saveSettings();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}));
		
		this.logger = Logger.getLogger("Scyus");
		this.logger.setUseParentHandlers(false);
		final ConsoleHandler consoleHandler = new ConsoleHandler();
		consoleHandler.setFormatter(new Formatter() {
			@Override
			public String format(LogRecord logRecord) {
				return new StringBuilder().append(logRecord.getLoggerName()).append(' ')
						.append(logRecord.getLevel().getName().toUpperCase()).append(": ")
						.append(this.formatMessage(logRecord)).append('\n');
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
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	private void loadSettings() throws FileNotFoundException, IOException {
		if (logger != null)
			logger.info("Loading Settings..");

		final ConfigurationObjectFactory cof = new ConfigurationObjectFactory(IScyusSettings.CONFIG_PROPERTIES);
		
		//Workaround?
		for (final Method method : IScyusSettings.class.getMethods()) {
			if (method.getAnnotation(Config.class) != null && method.getAnnotation(Default.class) != null) {
				final Config cfg = method.getAnnotation(Config.class);
				final Default def = method.getAnnotation(Default.class);

				IScyusSettings.CONFIG_PROPERTIES.setProperty(cfg.value()[0], def.value());
			}
		}
		
		if (!IScyusSettings.CONFIG_FILE.exists())
			this.saveSettings();

		IScyusSettings.CONFIG_PROPERTIES.loadFromXML(new FileInputStream(IScyusSettings.CONFIG_FILE));
		
		this.settings = cof.build(IScyusSettings.class);
	}
	
	/**
	 * Saves the Settings
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	private void saveSettings() throws FileNotFoundException, IOException {
		if (logger != null)
			logger.info("Saving Settings..");
		
		if (!IScyusSettings.CONFIG_FILE.exists())
			IScyusSettings.CONFIG_FILE.createNewFile();
		
		IScyusSettings.CONFIG_PROPERTIES.storeToXML(new FileOutputStream(IScyusSettings.CONFIG_FILE), "Generated on " + Date.from(Instant.now()));
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
