package org.terracotta.utils.perftester.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.terracotta.utils.commons.cache.configs.ConfigWrapper;
import org.terracotta.utils.perftester.InteractiveLauncher;

/**
 * @author Fabien Sanglier
 * 
 */
public class CacheLauncher extends InteractiveLauncher {
	private static Logger log = LoggerFactory.getLogger(CacheLauncher.class);
	
	public CacheLauncher(CacheLauncherAPI api) {
		super(api);
	}

	public static void main(String[] args) throws Exception {
		CacheLauncher launcher = new CacheLauncher(new CacheLauncherAPI(ConfigWrapper.getCacheName()));
		launcher.run(args);
		System.out.println("Completed");
		System.exit(0);
	}
}