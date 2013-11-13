package org.terracotta.utils.commons;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExecutorsUtil {
	private static Logger log = LoggerFactory.getLogger(ExecutorsUtil.class);
	
	public static void shutdownPool(ExecutorService service) throws InterruptedException{
		service.shutdown();
		while(!service.isTerminated()) {
			service.awaitTermination(1, TimeUnit.SECONDS);
		}
	}

	public static void shutdownAndAwaitTermination(ExecutorService pool, boolean doPrintWaitCueToConsole) {
		pool.shutdown(); // Disable new tasks from being submitted

		try {
			// Wait until existing tasks to terminate
			if(doPrintWaitCueToConsole){
				while(!pool.awaitTermination(5, TimeUnit.SECONDS)){
					System.out.print(".");
				}
			} else {
				while(!pool.awaitTermination(5, TimeUnit.SECONDS));
			}
			
			pool.shutdownNow(); // Cancel currently executing tasks

			// Wait a while for tasks to respond to being canceled
			if (!pool.awaitTermination(Integer.MAX_VALUE, TimeUnit.SECONDS))
				log.error("Pool did not terminate");
		} catch (InterruptedException ie) {
			// (Re-)Cancel if current thread also interrupted
			pool.shutdownNow();

			// Preserve interrupt status
			Thread.currentThread().interrupt();
		}
	}
}
