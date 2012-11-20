package org.terracotta.utils.perftester.cache.launchers;

import java.util.Arrays;
import java.util.Scanner;

import net.sf.ehcache.Cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.terracotta.utils.commons.cache.CacheUtils;

/**
 * @author Fabien Sanglier
 * 
 */
public abstract class InteractiveLauncher {
	private static Logger log = LoggerFactory.getLogger(InteractiveLauncher.class);
	public static final char LAUNCH_INPUT_QUIT = 'Q';
	private Cache cache = null;
	
	public InteractiveLauncher(String cacheName) {
		printLineSeparator();
		
		try {
			cache = CacheUtils.getCache(cacheName);
		} catch (Exception e) {
			log.error("An erro occured while fetching the cache", e);
		}

		if (null == cache){
			System.out.println("Could not find cache " + cacheName + ". Verify config file path for ehcache is accurate.");
			System.exit(1);
		}
	}

	public Cache getCache() {
		return cache;
	}

	public void printLineSeparator(){
		String lineSeparator = System.getProperty("line.separator");
		byte[] bytes = lineSeparator.getBytes();
		StringBuilder sb = new StringBuilder();
		for (byte b : bytes) {
			sb.append(String.format("%02X", b) + " ");
		}
		System.out.println("Line separator = " + lineSeparator + " (hex = " + sb.toString() + ")");
	}

	public abstract void printOptions();
	
	public boolean processInput(String input) throws Exception{
		String[] inputs = input.split(" ");
		return processInput(inputs);
	}
	
	public boolean processInput(String[] args) throws Exception{
		String[] inputArgs = null;
		String inputCommand = "";
		if(null != args && args.length > 0){
			inputCommand = args[0];
			if(args.length > 1){
				inputArgs = Arrays.copyOfRange(args, 1, args.length);
			}
		}
		
		return processInput(inputCommand, inputArgs);
	}
	
	public abstract boolean processInput(String input, String[] args) throws Exception;
	
	public void run() throws Exception {
		boolean keepRunning = true;
		while (keepRunning) {
			printOptions();
			
			String input = getInput();
			if (input.length() == 0) {
				continue;
			}
			
			keepRunning = processInput(input);
		}
	}

	private String getInput() throws Exception {
		System.out.println(">>");

		// option1
		Scanner sc = new Scanner(System.in);
		sc.useDelimiter(System.getProperty("line.separator"));
		return sc.nextLine();

		// option2
		// BufferedReader br = new BufferedReader(new
		// InputStreamReader(System.in));
		// return br.readLine();
	}
}
