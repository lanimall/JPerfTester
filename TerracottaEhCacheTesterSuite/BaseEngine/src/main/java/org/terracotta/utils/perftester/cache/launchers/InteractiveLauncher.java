package org.terracotta.utils.perftester.cache.launchers;

import java.util.Arrays;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.terracotta.utils.commons.cache.configs.GlobalConfigSingleton;

/**
 * @author Fabien Sanglier
 * 
 */
public class InteractiveLauncher {
	private static Logger log = LoggerFactory.getLogger(InteractiveLauncher.class);
	private final LauncherAPI api;
	public InteractiveLauncher(LauncherAPI api) {
		this.api = api;
		printLineSeparator();

		if (null == this.api.getCache()){
			System.out.println("Could not find the cache to work on. Verify config file path for ehcache is accurate.");
			System.exit(1);
		}
	}

	public static void main(String[] args) throws Exception {
		InteractiveLauncher launcher = new InteractiveLauncher(new LauncherAPI(GlobalConfigSingleton.getInstance().getCacheName()));
		launcher.run(args);
		System.out.println("Completed");
		System.exit(0);
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

	public void printOptions() {
		System.out.println();
		System.out.println("What do you want to do now?");
		for(String option : api.getOptions()){
			System.out.println(option);
		}
	}

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

	public void run(String[] args) throws Exception {
		boolean keepRunning = true;
		while (keepRunning) {
			if(null != args && args.length > 0){
				keepRunning = processInput(args);
				args=null;
			} else {
				printOptions();
				String input = getInput();
				if (input.length() == 0) {
					continue;
				}
				keepRunning = processInput(input);
			}
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
	
	public boolean processInput(String input, String[] args) throws Exception {
		return api.launch(input, args);
	}
}
