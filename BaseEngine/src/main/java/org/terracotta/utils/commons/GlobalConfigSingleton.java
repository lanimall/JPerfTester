package org.terracotta.utils.commons;

import java.text.NumberFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class GlobalConfigSingleton {
	private static Logger log = LoggerFactory.getLogger(GlobalConfigSingleton.class);
	private static final NumberFormat nf = NumberFormat.getInstance();

	public static final String CONFIGPATH_DEFAULT = "app.properties";
	public static final String CONFIGPATH_ENVPROP = "app.config.path";
	
	//singleton instance
	private static GlobalConfigSingleton instance;
	
	//wrapper property
	private final PropertyUtils propWrapper;
	
	private GlobalConfigSingleton(String propertyFile) throws Exception {
		propWrapper = new PropertyUtils(propertyFile);
	}

	public PropertyUtils getPropWrapper() {
		return propWrapper;
	}

	public static GlobalConfigSingleton getInstance() {
		if (instance == null)
		{
			synchronized(GlobalConfigSingleton.class) {  //1
				if (instance == null){
					try {
						String location = "";
						if(null != System.getProperty(CONFIGPATH_ENVPROP)){
							location = System.getProperty(CONFIGPATH_ENVPROP);
							log.info(CONFIGPATH_ENVPROP + " environment property specified: Loading application configuration from " + location);
						}
						else{
							log.info("Loading application configuration from classpath " + CONFIGPATH_DEFAULT);
							location = CONFIGPATH_DEFAULT;
						}

						instance = new GlobalConfigSingleton(location);
					} catch (Exception e) {
						log.error("Could not load the property file", e);
					}
				}
			}
		}
		return instance;
	}
}
