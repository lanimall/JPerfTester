package org.terracotta.utils.commons;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.terracotta.utils.perftester.generators.ObjectGeneratorFactory;

public class ClazzUtils {
	private static Logger log = LoggerFactory.getLogger(ClazzUtils.class);

	public static ObjectGeneratorFactory getObjectGeneratorFactory(String factoryClass){
		ObjectGeneratorFactory objGenFactory = null;
		log.info("Trying to instanciate the ObjectGeneratorFactory defined by:" + factoryClass);
		if(null != factoryClass && !"".equals(factoryClass)){
			try{
				Class objFactoryClazz = Class.forName(factoryClass);
				if(ObjectGeneratorFactory.class.isAssignableFrom(objFactoryClazz)){
					objGenFactory = (ObjectGeneratorFactory)objFactoryClazz.newInstance();
				} else {
					new IllegalArgumentException("The class " + factoryClass + " is not an ObjectGeneratorFactory class");
				}
			} catch (ClassNotFoundException cne){
				log.error("Could not find the class " + factoryClass + " in the classpath.", cne);
			} catch (Exception exc){
				log.error("An error occurred while instanciating the class " + factoryClass, exc);
			}
		}
		return objGenFactory;
	}
}
