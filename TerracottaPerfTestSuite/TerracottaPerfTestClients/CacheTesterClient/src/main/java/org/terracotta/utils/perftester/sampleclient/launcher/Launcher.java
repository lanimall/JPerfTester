package org.terracotta.utils.perftester.sampleclient.launcher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.terracotta.utils.commons.cache.configs.GlobalConfigSingleton;
import org.terracotta.utils.perftester.cache.launchers.InteractiveLauncher;

public class Launcher extends InteractiveLauncher {
	private static Logger log = LoggerFactory.getLogger(Launcher.class);
	
	public Launcher(String cacheName) {
		super(cacheName);
	}

	public static void main(String[] args) throws Exception {
		Launcher launcher = new Launcher(GlobalConfigSingleton.getInstance().getCacheName());

		if(null != args && args.length > 0){
			launcher.processInput(args);
		} else {
			launcher.run();
		}

		System.out.println("Completed");
		System.exit(0);
	}
}