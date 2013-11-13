package org.terracotta.utils.commons;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

import javax.naming.OperationNotSupportedException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Fabien Sanglier
 *
 */
public class PropertyUtils {
	private static Logger log = LoggerFactory.getLogger(PropertyUtils.class);
	
	private Properties properties;	

	public PropertyUtils(String propLocation) throws Exception {
		this.properties = loadProperties(propLocation);
	}

	public Properties getProperties(){
		return properties;
	}
	
	public String getProperty(String key){
		if(log.isDebugEnabled())
			log.debug("Getting key:" + key);
		
		String val = System.getProperty(key);
		if (val == null)
			val = properties.getProperty(key);
		
		if(log.isDebugEnabled())
			log.debug("value:" + val);
		
		return (val == null)?null:val.trim();
	}

	public Boolean getPropertyAsBoolean(String key){
		String val = getProperty(key);
		if (val == null)
			return null;
		
		return Boolean.parseBoolean(val);
	}

	public Integer getPropertyAsInt(String key){
		String val = getProperty(key);
		if (val == null)
			return null;
		
		try{
			return Integer.parseInt(val);
		} catch (NumberFormatException nfe){
			return null;
		}
	}

	public Long getPropertyAsLong(String key){
		String val = getProperty(key);
		if (val == null)
			return null;
		
		try{
			return Long.parseLong(val);
		} catch (NumberFormatException nfe){
			return null;
		}
	}

	public String getProperty(String key, String defaultVal){
		String val = getProperty(key);
		if (val == null)
			val = defaultVal;
		return val;
	}

	public Long getPropertyAsLong(String key, long defaultVal){
		Long val = getPropertyAsLong(key);
		if (val == null)
			return defaultVal;
		return val;
	}

	public Integer getPropertyAsInt(String key, int defaultVal){
		Integer val = getPropertyAsInt(key);
		if (val == null)
			return defaultVal;
		return val;
	}

	public Boolean getPropertyAsBoolean(String key, boolean defaultVal){
		Boolean val = getPropertyAsBoolean(key);
		if (val == null)
			return defaultVal;
		return val;
	}

	private Properties loadProperties(final String location) throws Exception {
		if(null == location)
			throw new OperationNotSupportedException("Location cannot be null");

		Properties props = new Properties();
		InputStream inputStream = null;
		try {
			if(location.indexOf("file:") > -1){
				inputStream = new FileInputStream(location.substring("file:".length()));
			} else {
				inputStream = this.getClass().getClassLoader().getResourceAsStream(location);
			}

			if (inputStream == null) {
				throw new FileNotFoundException("Property file '" + location
						+ "' not found in the classpath");
			}

			props.load(inputStream);

			inputStream.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			inputStream.close();
		}
		return props;
	}
}
