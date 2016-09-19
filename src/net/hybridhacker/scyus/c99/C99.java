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

package net.hybridhacker.scyus.c99;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

/**
 * A Class used to implement api.c99.nl
 * 
 * @author Flaflo
 *
 */
public final class C99 {

	/**
	 * The gender used in the random info generator
	 * 
	 * @author Flaflo
	 *
	 */
	public enum Gender {
		/**
		 * The male gender
		 */
		MALE,
		/**
		 * The female gender
		 */
		FEMALE,
		/**
		 * All possible genders
		 */
		ALL
	}

	/**
	 * The language for the translator
	 * 
	 * @author Flaflo
	 *
	 */
	public enum Language {
		/**
		 * The English language
		 */
		EN,
		/**
		 * The German language
		 */
		DE;
	}

	// API-URL Constants
	private static final String SKYPE_RESOLVER = "http://api.c99.nl/skyperesolver.php?key=%s&username=%s";
	private static final String RESOLVE_DATABASE = "http://api.c99.nl/resolvedb.php?key=%s&username=%s";
	private static final String IP_2_SKYPE = "http://api.c99.nl/ip2skype.php?key=%s&ip=%s";
	private static final String PING = "http://api.c99.nl/ping.php?key=%s&host=%s";
	private static final String IP_TO_HOST = "http://api.c99.nl/gethostname.php?key=%s&host=%s";
	private static final String HOST_TO_IP = "http://api.c99.nl/dnsresolver.php?key=%s&host=%s";
	private static final String DOMAIN_CHECKER = "http://api.c99.nl/domainchecker.php?key=%s&domain=%s";
	private static final String SCREENSHOT_TOOL = "http://api.c99.nl/createscreenshot.php?key=%s&url=%s";
	private static final String GEO_IP = "http://api.c99.nl/geoip.php?key=%s&host=%s";
	private static final String WEBSITE_UP_OR_DOWN = "http://api.c99.nl/upordown.php?key=%s&host=%s";
	private static final String GET_WEBSITE_HEADERS = "http://api.c99.nl/getheaders.php?key=%s&host=%s";
	private static final String LINK_BACKUP = "http://api.c99.nl/linkbackup.php?key=%s&url=%s";
	private static final String TEXT_PASTE = "http://api.c99.nl/textpaste.php?key=%s&content=%s";
	private static final String HTML_PASTE = "http://api.c99.nl/htmlpaste.php?key=%s&content=%s";
	private static final String URL_SHORTENER = "http://api.c99.nl/urlshortener.php?key=%s&url=%s";
	private static final String RANDOM_STRING_PICKER = "http://api.c99.nl/randomstringpicker.php?key=%s&textfile=%s";
	private static final String DICTIONARY = "http://api.c99.nl/dictionary.php?key=%s&word=%s";
	private static final String EMAIL_VALIDATOR = "http://api.c99.nl/emailvalidatorexternal.php?key=%s&email=%s";
	private static final String IP_VALIDATOR = "http://api.c99.nl/ipvalidator.php?key=%s&ip=%s";
	private static final String TRANSLATOR = "http://api.c99.nl/translate.php?key=%s&text=%s&tolanguage=%s";
	private static final String RANDOM_INFO_GENERATOR = "http://api.c99.nl/randomperson.php?key=%s&gender=%s";
	private static final String YOUTUBE_VIDEO_DETAILS = "http://api.c99.nl/youtubedetails.php?key=%s&videoid=%s";
	private static final String BTC_BALANCE_CHECKER = "http://api.c99.nl/bitcoinbalance.php?key=%s&address=%s";

	private String apiKey;

	/**
	 * Constructs the C99 Api
	 * 
	 * @param apiKey
	 *            the apiKey
	 */
	public C99(final String apiKey) {
		this.apiKey = apiKey;
	}

	/**
	 * @param address
	 *            the address to check
	 * @return the current balance of the address
	 */
	public String btcBalanceChecker(final String address) {
		return new String(downloadContent(String.format(BTC_BALANCE_CHECKER, apiKey, address)));
	}

	/**
	 * @param videoId
	 *            the YouTube video id
	 * @return the video details
	 */
	public String youtubeVideoDetails(final String videoId) {
		return new String(downloadContent(String.format(YOUTUBE_VIDEO_DETAILS, apiKey, videoId)));
	}

	/**
	 * @param gender
	 *            the gender to generate for
	 * @return the generated info
	 */
	public String randomInfoGenerator(final Gender gender) {
		return new String(downloadContent(String.format(RANDOM_INFO_GENERATOR, apiKey, gender.name().toLowerCase())));
	}

	/**
	 * @param text
	 *            the text to translate from
	 * @param language
	 *            the language to translate to
	 * @return the translated text
	 */
	public String translator(final String text, final Language language) {
		return new String(downloadContent(String.format(TRANSLATOR, apiKey, text, language.name().toLowerCase())));
	}

	/**
	 * @param ip
	 *            the IP to check
	 * @return true if the IP is valid
	 */
	public boolean ipValidator(final String ip) {
		return new String(downloadContent(String.format(IP_VALIDATOR, apiKey, ip))).equalsIgnoreCase("true"); // TODO
	}

	/**
	 * @param email
	 *            the email to check
	 * @return true if the email is valid
	 */
	public boolean emailValidator(final String email) {
		return new String(downloadContent(String.format(EMAIL_VALIDATOR, apiKey, email))).equalsIgnoreCase("true"); // TODO
	}

	/**
	 * @param word
	 *            the word to search
	 * @return the dictionary to the given word
	 */
	public String dictionary(final String word) {
		return new String(downloadContent(String.format(DICTIONARY, apiKey, word)));
	}

	/**
	 * @param textFile
	 *            the URL to the file
	 * @return a random string from the given textFile
	 */
	public String randomStringPicker(final String textFile) {
		return new String(downloadContent(String.format(RANDOM_STRING_PICKER, apiKey, textFile)));
	}

	/**
	 * @param url
	 *            the URL to shorten
	 * @return the shortened URL
	 */
	public String urlShortener(final String url) {
		return new String(downloadContent(String.format(URL_SHORTENER, apiKey, url)));
	}

	/**
	 * @param content
	 *            the content to paste
	 * @return the URL to the paste
	 */
	public String htmlPaste(final String content) {
		return new String(downloadContent(String.format(HTML_PASTE, apiKey, content)));
	}

	/**
	 * @param content
	 *            the content to paste
	 * @return the URL to the paste
	 */
	public String textPaste(final String content) {
		return new String(downloadContent(String.format(TEXT_PASTE, apiKey, content)));
	}

	/**
	 * @param url
	 *            the URL to backup
	 * @return the URL to the backup
	 */
	public String linkBackup(final String url) {
		return new String(downloadContent(String.format(LINK_BACKUP, apiKey, url)));
	}

	/**
	 * @param host
	 *            the host to get from
	 * @return the headers from the host
	 */
	public String getWebisteHeaders(final String host) {
		return new String(downloadContent(String.format(GET_WEBSITE_HEADERS, apiKey, host)));
	}

	/**
	 * @param host
	 *            the host to check
	 * @return true if the website is available
	 */
	public boolean websiteUpOrDown(final String host) {
		return new String(downloadContent(String.format(WEBSITE_UP_OR_DOWN, apiKey, host))).equalsIgnoreCase("true");
	}

	/**
	 * @param host
	 *            the host to GEO IP
	 * @return the GEO location of the IP
	 */
	public String geoIp(final String host) {
		return new String(downloadContent(String.format(GEO_IP, apiKey, host)));
	}

	/**
	 * @param url
	 *            the URL to screenshot
	 * @return the taken screenshot from the URL
	 * @throws IOException
	 */
	public BufferedImage screenshotTool(final String url) throws IOException {
		return ImageIO.read(new ByteArrayInputStream(downloadContent(String.format(SCREENSHOT_TOOL, apiKey, url))));
	}

	/**
	 * @param domain
	 *            the domain to check
	 * @return true if the domain is available
	 */
	public boolean domainChecker(final String domain) {
		return new String(downloadContent(String.format(DOMAIN_CHECKER, apiKey, domain))).equalsIgnoreCase("true"); // TODO
	}

	/**
	 * @param host
	 *            the host to resolve
	 * @return the resolved IP
	 */
	public String hostToIp(final String host) {
		return new String(downloadContent(String.format(HOST_TO_IP, apiKey, host)));
	}

	/**
	 * 
	 * @param ip
	 *            the IP to resolve
	 * @return the resolved host
	 */
	public String ipToHost(final String ip) {
		return new String(downloadContent(String.format(IP_TO_HOST, apiKey, ip)));
	}

	/**
	 * @param username
	 *            the username to resolve
	 * @return the resolved IP from database
	 */
	public String resolveDatabase(final String username) {
		return new String(downloadContent(String.format(RESOLVE_DATABASE, apiKey, username)));
	}

	/**
	 * @param ip
	 *            the IP to resolve
	 * @return the resolved IP
	 */
	public String ip2Skype(final String ip) {
		return new String(downloadContent(String.format(IP_2_SKYPE, apiKey, ip)));
	}

	/**
	 * @param host
	 *            the host to ping
	 * @return the ping-string
	 */
	public String ping(final String host) {
		return new String(downloadContent(String.format(PING, apiKey, host)));
	}

	/**
	 * @param username
	 *            the username to resolve
	 * @return the resolved IP
	 */
	public String skypeResolver(final String username) {
		return new String(downloadContent(String.format(SKYPE_RESOLVER, apiKey, username)));
	}

	/**
	 * @return the apiKey
	 */
	public String getApiKey() {
		return apiKey;
	}

	/**
	 * @param apiKey
	 *            the apiKey to set
	 */
	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	/**
	 * Downloads content from a website
	 * 
	 * @param url
	 *            the url to download from
	 * @return the downloaded content
	 */
	private static byte[] downloadContent(final String url) {
		try {
			final DataInputStream dataIn = new DataInputStream(new URL(url).openStream());
			final byte[] content = new byte[dataIn.available()];
			dataIn.readFully(content);

			return content;
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}
}
