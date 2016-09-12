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

package net.hybridhacker.scyus.config;

import org.skife.config.Config;
import org.skife.config.Default;

/**
 * Setttings for Scyus
 * 
 * @author Flaflo
 *
 */
public interface IScyusSettings {

	/**
	 * @return the message format
	 */
	@Default("%s >> %s")
	@Config("bot.msgformat")
	public String getSendMessageFormat();
	
	/**
	 * @return the nickname
	 */
	@Default("Scyus")
	@Config("bot.nickname")
	public String getNickname();

	/**
	 * @return the prefix
	 */
	@Default("#")
	@Config("command.prefix")
	public String getCommandPrefix();

	/**
	 * @return the cooldown
	 */
	@Default("60000")
	@Config("command.spam.text.cooldown")
	public long getTextSpamCooldown();

	/**
	 * @return the max spam amount
	 */
	@Default("50")
	@Config("command.spam.text.max")
	public int getMaxTextSpamAmount();
	
	/**
	 * @return the host
	 */
	@Default("localhost")
	@Config("mysql.host")
	public String getMySqlHost();
	
	/**
	 * @return the port
	 */
	@Default("3306")
	@Config("mysql.port")
	public int getMySqlPort();
	
	/**
	 * @return the username
	 */
	@Default("root")
	@Config("mysql.user")
	public String getMySqlUsername();
	
	/**
	 * @return the password
	 */
	@Default("6]:ZAcrg5#:$@Nd")
	@Config("mysql.password")
	public String getMySqlPassword();
	
	/**
	 * @return the database
	 */
	@Default("scyus")
	@Config("mysql.databse")
	public String getMySqlDatabse();
	
}
