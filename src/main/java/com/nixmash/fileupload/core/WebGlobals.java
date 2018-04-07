package com.nixmash.fileupload.core;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Singleton
public class WebGlobals implements java.io.Serializable {

	private static final Logger logger = LoggerFactory.getLogger(WebGlobals.class);
	private static final long serialVersionUID = -2093038401455686564L;

	public String cloudApplicationId;
	public Boolean inProductionMode;
	public String siteName;
	public String baseUrl;
	public String fileUploadPath;
	public String thumbnailUploadPath;
	public String downloadsPath;
	public String downloadFileBase;

	private final WebConfig webConfig;

    @Inject
	public WebGlobals(WebConfig webConfig) {
		this.webConfig = webConfig;

		Properties properties = new Properties();
		InputStream input = null;

		try {

			input = new FileInputStream(webConfig.globalPropertiesFile);
			properties.load(input);
			this.cloudApplicationId = properties.getProperty("application.cloud.id");
			this.inProductionMode = Boolean.valueOf(properties.getProperty("application.inProductionMode"));
			this.siteName = properties.getProperty("site.name");
			this.baseUrl= properties.getProperty("base.url");
			this.fileUploadPath = properties.getProperty("file.upload.path");
			this.thumbnailUploadPath = properties.getProperty("thumbnail.upload.path");
			this.downloadsPath= properties.getProperty("downloads.path");
			this.downloadFileBase= properties.getProperty("downloads.file.base");

		} catch (IOException ex) {
			logger.error(ex.getMessage());
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}



}
