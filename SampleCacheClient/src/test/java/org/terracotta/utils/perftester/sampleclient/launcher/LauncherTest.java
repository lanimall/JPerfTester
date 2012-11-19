package org.terracotta.utils.perftester.sampleclient.launcher;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LauncherTest {
	private static Logger log = LoggerFactory.getLogger(LauncherTest.class);
	private static Launcher launcher = null;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		launcher = new Launcher(Launcher.CACHE_NAME);
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		launcher = null;
	}

	@Test
	public void CachePutLauncherTest(){
		try {
			launcher.processInput(String.valueOf(Launcher.LAUNCH_INPUT_BULKLOAD));
		} catch (Exception e) {
			log.error("", e);
		}
	}

	@Test
	public void CacheSequentialGetLauncherTest(){
		try {
			launcher.processInput(String.valueOf(Launcher.LAUNCH_INPUT_WARMUP));
		} catch (Exception e) {
			log.error("", e);
		}
	}

	@Test
	public void CacheRandomGetLauncherTest(){
		try {
			launcher.processInput(String.valueOf(Launcher.LAUNCH_INPUT_RDM_GETS));
		} catch (Exception e) {
			log.error("", e);
		}
	}
}
