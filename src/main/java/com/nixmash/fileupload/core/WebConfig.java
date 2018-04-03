package com.nixmash.fileupload.core;

import com.google.inject.Singleton;
import com.nixmash.fileupload.utils.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Serializable;
import java.util.Properties;

@Singleton
public class WebConfig implements Serializable {

	// region properties

	private static final Logger logger = LoggerFactory.getLogger(WebConfig.class);
	private static final long serialVersionUID = -4129304441129842839L;

	public String applicationId;
	public String globalPropertiesFile;
	public String applicationDescription;
	public String currentLocale;
	public String pageTitlePrefix;
	public String unauthorizedUrl;
	public String datasourceName;
	public String configPath;

    // endregion

	// region getResourceBundle()

	public WebConfig() {

		Properties properties = new Properties();

		try {
			String propertiesFile = !WebUtils.isInTestingMode() ? "fileuploader" : "test";
			properties.load(getClass().getResourceAsStream(String.format("/%s.properties", propertiesFile)));
		} catch (IOException e) {
			logger.error(e.getMessage());
		}

		String user_home = System.getProperty("user.home");
		this.applicationId = properties.getProperty("application.id");
		this.applicationDescription = properties.getProperty("application.description");
		this.globalPropertiesFile = user_home + properties.getProperty("global.properties.file");
		this.currentLocale = properties.getProperty("application.currentLocale");
		this.pageTitlePrefix = properties.getProperty("web.pagetitle.prefix");
		this.unauthorizedUrl = properties.getProperty("shiro.unauthorizedurl");
		this.datasourceName = properties.getProperty("datasource.name");
		this.configPath = user_home + properties.getProperty("config.path");
	}

	// endregion

}
