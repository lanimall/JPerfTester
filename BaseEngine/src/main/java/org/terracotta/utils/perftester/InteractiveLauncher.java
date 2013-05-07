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

	public static final String ARGS_SEPARATOR = " ";
	public static final String COMMAND_SEPARATOR = ",";

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
		if(log.isDebugEnabled())
			log.debug("Processing raw command: " + input);

		String[] inputs = input.split(ARGS_SEPARATOR);
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
				//there could be several command chained together with comma...hence let's try to find it out
				String joinedCommand = joinStringArray(args, ARGS_SEPARATOR);

				if(log.isDebugEnabled())
					log.debug("Full command: " + joinedCommand);

				String[] multipleCommands = joinedCommand.split(COMMAND_SEPARATOR);
				for(String inputCommand : multipleCommands){
					processInput(inputCommand);
				}

				//if args are specified directly, it should run once and exit (useful for batch scripting)
				keepRunning = false;
			} else {
				printOptions();
				String input = getInput();
				if (input.length() == 0) {
					continue;
				}
				
				if(log.isDebugEnabled())
					log.debug("Full command: " + input);

				String[] multipleCommands = input.split(COMMAND_SEPARATOR);
				for(String inputCommand : multipleCommands){
					keepRunning = processInput(inputCommand);
					if(!keepRunning)
						break;
				}
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
		System.out.println("######################## Processing command '" + input + "' with args:" + joinStringArray(args, ARGS_SEPARATOR) + "");
		return api.launch(input, args);
	}

	private String joinStringArray(String[] arr, String separator){
		String join = "";
		if(null != arr){
			for(String s : arr){
				if(join.length() > 0)
					join += separator;
				join += s;
			}
		}
		return join;
	}
}
