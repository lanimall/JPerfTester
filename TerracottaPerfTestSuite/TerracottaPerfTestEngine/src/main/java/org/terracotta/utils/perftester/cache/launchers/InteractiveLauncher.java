package org.terracotta.utils.perftester.cache.launchers;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import net.sf.ehcache.Cache;
import net.sf.ehcache.search.Attribute;
import net.sf.ehcache.search.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.terracotta.utils.commons.cache.CacheUtils;
import org.terracotta.utils.commons.cache.SearchUtils;
import org.terracotta.utils.commons.cache.configs.GlobalConfigSingleton;
import org.terracotta.utils.perftester.generators.ObjectGeneratorFactory;

/**
 * @author Fabien Sanglier
 * 
 */
public class InteractiveLauncher {
	private static Logger log = LoggerFactory.getLogger(InteractiveLauncher.class);
	public static final char LAUNCH_INPUT_QUIT = 'Q';
	private Cache cache = null;

	public static final char LAUNCH_INPUT_FULLTEST1 = '1';
	public static final char LAUNCH_INPUT_FULLTEST2 = '2';
	public static final char LAUNCH_INPUT_BULKLOAD = '3';
	public static final char LAUNCH_INPUT_WARMUP = '4';
	public static final char LAUNCH_INPUT_RDM_GETS = '5';
	public static final char LAUNCH_INPUT_RDMMIX = '6';

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

	public static void main(String[] args) throws Exception {
		InteractiveLauncher launcher = new InteractiveLauncher(GlobalConfigSingleton.getInstance().getCacheName());
		launcher.run(args);
		System.out.println("Completed");
		System.exit(0);
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

	public void printOptions() {
		System.out.println();
		System.out.println("What do you want to do now?");
		System.out.println(LAUNCH_INPUT_FULLTEST1 + " - Start Full Sequence 1 (Load, Warmup, Random Gets)");
		System.out.println(LAUNCH_INPUT_FULLTEST2 + " - Start Full Sequence 2 (Load, Warmup, Random Mix)");
		System.out.println(LAUNCH_INPUT_BULKLOAD + " - Start Cache Object Loading");
		System.out.println(LAUNCH_INPUT_WARMUP + " - Start Cache Warmup");
		System.out.println(LAUNCH_INPUT_RDM_GETS + " - Start Cache Random Gets");
		System.out.println(LAUNCH_INPUT_RDMMIX + " - Start Cache Random Mix - 3 Params: [% Gets] [% Puts] [% Search] (default: 60 25 15)");
		System.out.println(LAUNCH_INPUT_QUIT + " - Quit");
	}

	public boolean processInput(String input, String[] args) throws Exception {
		BaseCacheLauncher cacheLauncher = null;

		if(null == input || "".equals(input)){
			System.out.println("Unrecognized entry...");
			return true; 
		}

		switch (input.charAt(0)) {
		case LAUNCH_INPUT_FULLTEST1:
			processInput(String.valueOf(LAUNCH_INPUT_BULKLOAD));
			processInput(String.valueOf(LAUNCH_INPUT_WARMUP));
			processInput(String.valueOf(LAUNCH_INPUT_RDM_GETS));
			break;
		case LAUNCH_INPUT_FULLTEST2:
			processInput(String.valueOf(LAUNCH_INPUT_BULKLOAD));
			processInput(String.valueOf(LAUNCH_INPUT_WARMUP));
			processInput(String.valueOf(LAUNCH_INPUT_RDMMIX));
			break;
		case LAUNCH_INPUT_BULKLOAD:
			//TODO: new RandomCustomerGenerator(new RandomAddressGenerator(new RandomAddressCategoryGenerator())
			cacheLauncher = new CachePutLauncher(
					getCache(),
					GlobalConfigSingleton.getInstance().getCacheLoaderThreads(),
					GlobalConfigSingleton.getInstance().getCacheLoaderNbObjects(), 
					getObjectGeneratorFactory(GlobalConfigSingleton.getInstance().getCacheLoaderKeyGenFactory()),
					getObjectGeneratorFactory(GlobalConfigSingleton.getInstance().getCacheLoaderValueGenFactory()),
					GlobalConfigSingleton.getInstance().getCacheLoaderKeyStart(),
					GlobalConfigSingleton.getInstance().isCacheLoaderDoBulkLoad(), 
					GlobalConfigSingleton.getInstance().isCacheLoaderRemoveAll());
			break;
		case LAUNCH_INPUT_WARMUP:
			cacheLauncher = new CacheSequentialGetLauncher(
					getCache(),
					GlobalConfigSingleton.getInstance().getCacheWarmerThreads(), 
					GlobalConfigSingleton.getInstance().getCacheWarmerNbObjects(), 
					GlobalConfigSingleton.getInstance().getCacheWarmerKeyStart());
			break;
		case LAUNCH_INPUT_RDM_GETS:
			cacheLauncher = new CacheRandomGetLauncher(
					getCache(), 
					GlobalConfigSingleton.getInstance().getCacheSteadyStateThreads(), 
					GlobalConfigSingleton.getInstance().getCacheSteadyStateNbOperations(), 
					GlobalConfigSingleton.getInstance().getCacheSteadyStateKeyMinValue(),
					GlobalConfigSingleton.getInstance().getCacheSteadyStateKeyMaxValue()
			);
			break;
		case LAUNCH_INPUT_RDMMIX:
			cacheLauncher = new CacheRandomMixLauncher(
					getCache(),
					GlobalConfigSingleton.getInstance().getCacheSteadyStateThreads(), 
					GlobalConfigSingleton.getInstance().getCacheSteadyStateNbOperations());
			
			if(null == args){
				if(getCache().isSearchable())
					args = new String[] {"60", "25", "15"};
				else
					args = new String[] {"70", "30"};
			}

			for(int i=0; i<args.length;i++){
				int mix = 0;
				try {
					mix = Integer.parseInt(args[i]);
					if(mix > 100)
						throw new NumberFormatException("Mix value should be 100 or less");

					switch(i){
					case 0:
						((CacheRandomMixLauncher)cacheLauncher).addCacheGetOperationMix(mix, getCache(), GlobalConfigSingleton.getInstance().getCacheSteadyStateKeyMinValue(), GlobalConfigSingleton.getInstance().getCacheSteadyStateKeyMaxValue());
						break;
					case 1:
						((CacheRandomMixLauncher)cacheLauncher).addCachePutOperationMix(mix, getCache(), getObjectGeneratorFactory(GlobalConfigSingleton.getInstance().getCacheLoaderKeyGenFactory()), getObjectGeneratorFactory(GlobalConfigSingleton.getInstance().getCacheLoaderValueGenFactory()));
						break;
					case 2:
						int maxResults = 50;
						List<Query> queries = new LinkedList<Query>();
						queries.add(SearchUtils.buildSearchTextQuery(
								getCache(),
								new Attribute[] {getCache().getSearchAttribute("firstName")}, 
								new String[] {"Sar*"}, 
								false, 
								maxResults));

						queries.add(SearchUtils.buildSearchTextQuery(
								getCache(),
								new Attribute[] {getCache().getSearchAttribute("firstName"), getCache().getSearchAttribute("lastName")}, 
								new String[] {"Sar*","Carls*"}, 
								false,
								maxResults));

						((CacheRandomMixLauncher)cacheLauncher).addCacheSearchOperationMix(mix, getCache(), queries.toArray(new Query[queries.size()]));
						break;
					}
				} catch (Exception e) {
					log.error("Could not add the operation to the ramdomMix", e);
				}
			}
			break;
		case LAUNCH_INPUT_QUIT:
			return false;
		default:
			System.out.println("Unrecognized entry...");
			return true;
		}

		if(null != cacheLauncher){
			cacheLauncher.setMultiClientEnabled(GlobalConfigSingleton.getInstance().isAppMultiClientsSyncEnabled());
			cacheLauncher.setNumClients(GlobalConfigSingleton.getInstance().getAppNbClients());
			cacheLauncher.launch();
		}

		return true;
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

	protected ObjectGeneratorFactory getObjectGeneratorFactory(String factoryClass){
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
