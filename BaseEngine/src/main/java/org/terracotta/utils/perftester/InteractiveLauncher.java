package org.terracotta.utils.perftester;

import java.util.Arrays;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Fabien Sanglier
 * 
 */
public class InteractiveLauncher {
	private static Logger log = LoggerFactory.getLogger(InteractiveLauncher.class);
	private final LauncherAPI api;
	
	public InteractiveLauncher(LauncherAPI api) {
		if (null == api){
			throw new IllegalArgumentException("Could not find the api to work with.");
		}
		
		if (!api.isReady()){
			throw new IllegalArgumentException("The api is not ready. Verify config is accurate.");
		}
		
		this.api = api;
		printLineSeparator();
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
