package org.terracotta.utils.perftester.cache;

import net.sf.ehcache.Cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.terracotta.utils.commons.ClazzUtils;
import org.terracotta.utils.commons.cache.CacheUtils;
import org.terracotta.utils.commons.cache.configs.ConfigWrapper;
import org.terracotta.utils.perftester.LauncherAPI;
import org.terracotta.utils.perftester.cache.launchers.HarnessCachePutDecorator;
import org.terracotta.utils.perftester.cache.launchers.HarnessClientSyncDecorator;
import org.terracotta.utils.perftester.cache.runnerFactories.CachePutOperationFactory;
import org.terracotta.utils.perftester.cache.runners.CacheGetOperation.CacheGetOperationFactory;
import org.terracotta.utils.perftester.cache.runners.CacheSearchOperation.CacheSearchOperationFactory;
import org.terracotta.utils.perftester.generators.ObjectGeneratorFactory;
import org.terracotta.utils.perftester.launchers.ConcurrentLauncher;
import org.terracotta.utils.perftester.launchers.HarnessLauncher;
import org.terracotta.utils.perftester.runners.RunnerFactory;
import org.terracotta.utils.perftester.runners.impl.RamdomMixRunner.RamdomMixRunnerFactory;

public class CacheLauncherAPI implements LauncherAPI {
	private static Logger log = LoggerFactory.getLogger(CacheLauncher.class);
	private Cache cache = null;

	private static int operationIdCounter = 1;

	private enum OPERATIONS {
		OP_WARMUP_PUTS("Warmup phase: Puts cache elements (Usage: @@opInput@@ <Threads> <Total Operations> <?bulk load=*true*|false> <?clear all first=*true*|false>)", 2)
		{
			@Override
			public HarnessLauncher getHarnessLauncher(Cache cache, String[] args) {
				boolean doBulkLoad = true;
				boolean doClearAllFirst = true;

				int nbThreads;
				try{
					nbThreads = Integer.parseInt(args[0]);
				} catch (NumberFormatException nfe){
					nbThreads = 1;
				}

				int nbOfOperations;
				try{
					nbOfOperations = Integer.parseInt(args[1]);
				} catch (NumberFormatException nfe){
					nbOfOperations = 0;
				}

				if(args.length > 2){
					doBulkLoad = Boolean.parseBoolean(args[2]);
					if(args.length > 3){
						doClearAllFirst = Boolean.parseBoolean(args[3]);
					}
				}

				ObjectGeneratorFactory keyGeneratorFactory = ClazzUtils.getObjectGeneratorFactory(ConfigWrapper.getCacheWarmupKeyGenFactory());
				ObjectGeneratorFactory valueGeneratorFactory = ClazzUtils.getObjectGeneratorFactory(ConfigWrapper.getCacheWarmupValueGenFactory());

				RunnerFactory cacheOpFactory = new CachePutOperationFactory(
						cache, 
						nbOfOperations/nbThreads, 
						(null != keyGeneratorFactory)? keyGeneratorFactory.createObjectGenerator():null, 
								(null != valueGeneratorFactory)?valueGeneratorFactory.createObjectGenerator():null
						);

				return new ConcurrentLauncher(
						nbThreads, 
						cacheOpFactory, 
						new HarnessCachePutDecorator(cache, doBulkLoad, doClearAllFirst, ConfigWrapper.isAppMultiClientsSyncEnabled(), ConfigWrapper.getAppNbClients()));
			}
		},
		OP_WARMUP_GETS("Warmup phase: Get all cache elements (Usage: @@opInput@@ <Threads> <Total Operations>)", 2)
		{
			@Override
			public HarnessLauncher getHarnessLauncher(Cache cache, String[] args) {
				int nbThreads;
				try{
					nbThreads = Integer.parseInt(args[0]);
				} catch (NumberFormatException nfe){
					nbThreads = 1;
				}

				int nbOfOperations;
				try{
					nbOfOperations = Integer.parseInt(args[1]);
				} catch (NumberFormatException nfe){
					nbOfOperations = 0;
				}

				ObjectGeneratorFactory keyGeneratorFactory = ClazzUtils.getObjectGeneratorFactory(ConfigWrapper.getCacheWarmupKeyGenFactory());
				
				System.out.println("*********** Getting cache entries *************");
				System.out.println("Params:");
				System.out.println("Number of Loader Threads: " + nbThreads);
				System.out.println("Number of objects to fetch: " + nbOfOperations);
				System.out.println("Number of operations per thread: " + (nbOfOperations/nbThreads));
				
				RunnerFactory cacheOpFactory = new CacheGetOperationFactory(
						cache, 
						nbOfOperations/nbThreads,
						(null != keyGeneratorFactory)? keyGeneratorFactory.createObjectGenerator():null
						);

				return new ConcurrentLauncher(nbThreads, cacheOpFactory, new HarnessClientSyncDecorator(cache, ConfigWrapper.isAppMultiClientsSyncEnabled(), ConfigWrapper.getAppNbClients()));
			}
		},
		OP_STEADY_GETS("Steady State phase: Cache Gets (Usage: @@opInput@@ <Threads> <Total Operations>)", 2)
		{
			@Override
			public HarnessLauncher getHarnessLauncher(Cache cache, String[] args) {
				int nbThreads;
				try{
					nbThreads = Integer.parseInt(args[0]);
				} catch (NumberFormatException nfe){
					nbThreads = 1;
				}

				int nbOfOperations;
				try{
					nbOfOperations = Integer.parseInt(args[1]);
				} catch (NumberFormatException nfe){
					nbOfOperations = 0;
				}

				ObjectGeneratorFactory keyGeneratorFactory = ClazzUtils.getObjectGeneratorFactory(ConfigWrapper.getCacheSteadyKeyGenFactory());
				
				System.out.println("*********** Getting random cache entries *************");
				System.out.println("Params:");
				System.out.println("Number of Loader Threads: " + nbThreads);
				System.out.println("Number of objects to fetch: " + nbOfOperations);
				System.out.println("Number of operations per thread: " + (nbOfOperations/nbThreads));
				
				RunnerFactory cacheOpFactory = new CacheGetOperationFactory(
						cache, 
						nbOfOperations/nbThreads, 
						(null != keyGeneratorFactory)? keyGeneratorFactory.createObjectGenerator():null
						);

				return new ConcurrentLauncher(nbThreads, cacheOpFactory, new HarnessClientSyncDecorator(cache, ConfigWrapper.isAppMultiClientsSyncEnabled(), ConfigWrapper.getAppNbClients()));
			}
		},
		OP_STEADY_PUTS("Steady State phase: Cache Puts (Usage: @@opInput@@ <Threads> <Total Operations>)", 2)
		{
			@Override
			public HarnessLauncher getHarnessLauncher(Cache cache, String[] args) {
				int nbThreads;
				try{
					nbThreads = Integer.parseInt(args[0]);
				} catch (NumberFormatException nfe){
					nbThreads = 1;
				}

				int nbOfOperations;
				try{
					nbOfOperations = Integer.parseInt(args[1]);
				} catch (NumberFormatException nfe){
					nbOfOperations = 0;
				}

				ObjectGeneratorFactory keyGeneratorFactory = ClazzUtils.getObjectGeneratorFactory(ConfigWrapper.getCacheSteadyKeyGenFactory());
				ObjectGeneratorFactory valueGeneratorFactory = ClazzUtils.getObjectGeneratorFactory(ConfigWrapper.getCacheSteadyValueGenFactory());

				RunnerFactory cacheOpFactory = new CachePutOperationFactory(
						cache, 
						nbOfOperations/nbThreads, 
						(null != keyGeneratorFactory)? keyGeneratorFactory.createObjectGenerator():null, 
								(null != valueGeneratorFactory)?valueGeneratorFactory.createObjectGenerator():null
						);

				return new ConcurrentLauncher(nbThreads, cacheOpFactory, new HarnessClientSyncDecorator(cache, ConfigWrapper.isAppMultiClientsSyncEnabled(), ConfigWrapper.getAppNbClients())); 
			}
		},
		OP_STEADY_SEARCH("Steady State phase: Cache Searches (Usage: @@opInput@@ <Threads> <Total Operations>)", 2){
			@Override
			public HarnessLauncher getHarnessLauncher(Cache cache, String[] args) {
				int nbThreads;
				try{
					nbThreads = Integer.parseInt(args[0]);
				} catch (NumberFormatException nfe){
					nbThreads = 1;
				}

				int nbOfOperations;
				try{
					nbOfOperations = Integer.parseInt(args[1]);
				} catch (NumberFormatException nfe){
					nbOfOperations = 0;
				}

				ObjectGeneratorFactory searchGeneratorFactory = ClazzUtils.getObjectGeneratorFactory(ConfigWrapper.getCacheSteadySearchQueryGenFactory());

				RunnerFactory cacheOpFactory = new CacheSearchOperationFactory(
						cache, 
						nbOfOperations/nbThreads, 
						(null != searchGeneratorFactory)? searchGeneratorFactory.createObjectGenerator():null
						);

				return new ConcurrentLauncher(nbThreads, cacheOpFactory, new HarnessClientSyncDecorator(cache, ConfigWrapper.isAppMultiClientsSyncEnabled(), ConfigWrapper.getAppNbClients())); 
			}
		},
		OP_STEADY_MIX("Steady State phase: Mix of Cache Gets/Puts/Searches (Usage: @@opInput@@ <Threads> <Total Operations> <% Gets> <% Puts> <% Search> (default: 60 25 15))", 2){
			@Override
			public HarnessLauncher getHarnessLauncher(Cache cache, String[] args) {
				int nbThreads;
				try{
					nbThreads = Integer.parseInt(args[0]);
				} catch (NumberFormatException nfe){
					nbThreads = 1;
				}

				int nbOfOperations;
				try{
					nbOfOperations = Integer.parseInt(args[1]);
				} catch (NumberFormatException nfe){
					nbOfOperations = 0;
				}

				RamdomMixRunnerFactory cacheOpFactory = new RamdomMixRunnerFactory(nbOfOperations/nbThreads);
				
				ObjectGeneratorFactory keyGeneratorFactory = ClazzUtils.getObjectGeneratorFactory(ConfigWrapper.getCacheSteadyKeyGenFactory());
				ObjectGeneratorFactory valueGeneratorFactory = ClazzUtils.getObjectGeneratorFactory(ConfigWrapper.getCacheSteadyValueGenFactory());
				ObjectGeneratorFactory searchGeneratorFactory = ClazzUtils.getObjectGeneratorFactory(ConfigWrapper.getCacheSteadySearchQueryGenFactory());

				String[] mixArgs;
				if(args.length <=2){
					if(cache.isSearchable())
						mixArgs = new String[] {"60", "25", "15"};
					else
						mixArgs = new String[] {"70", "30"};
				} else {
					mixArgs = new String[args.length - 2];
					for(int i=2; i<args.length;i++){
						mixArgs[i-2]=args[i];
					}
				}
				
				for(int i=0; i<mixArgs.length;i++){
					int mix = 0;
					try {
						mix = Integer.parseInt(mixArgs[i]);
						if(mix > 100)
							throw new NumberFormatException("Mix value should be 100 or less");

						switch(i){
						case 0:
							cacheOpFactory.addOperationMix(
									new CacheGetOperationFactory(
											cache, 
											1,  
											(null != keyGeneratorFactory)? keyGeneratorFactory.createObjectGenerator():null), 
									mix);
							break;
						case 1:
							cacheOpFactory.addOperationMix(
									new CachePutOperationFactory(
											cache, 
											1, 
											(null != keyGeneratorFactory)? keyGeneratorFactory.createObjectGenerator():null, 
											(null != valueGeneratorFactory)?valueGeneratorFactory.createObjectGenerator():null
									),
									mix);
							break;
						case 2:
							if(mix > 0 && null != cache && !cache.isSearchable()){
								log.error("The cache " + cache.getName() + " is not searchable...not adding this operation mix");
								break;
							}
							
							cacheOpFactory.addOperationMix(
									new CacheSearchOperationFactory(
											cache, 
											1, 
											(null != searchGeneratorFactory)?searchGeneratorFactory.createObjectGenerator():null),
									mix);
							
							/*
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

							if(null != queries && queries.size() > 0)
								((CacheRandomMixLauncher)cacheLauncher).addCacheSearchOperationMix(mix, getCache(), queries.toArray(new Query[queries.size()]));
							 */
							break;
						}
					} catch (Exception e) {
						log.error("Could not add the operation to the ramdomMix", e);
					}
				}
				
				return new ConcurrentLauncher(nbThreads, cacheOpFactory, new HarnessClientSyncDecorator(cache, ConfigWrapper.isAppMultiClientsSyncEnabled(), ConfigWrapper.getAppNbClients()));
			}
		};

		private String opInput;
		private String opDetail;
		private int opInputParamsMadatorySize;

		private OPERATIONS() {
			this(operationIdCounter++, "", 0);
		}

		private OPERATIONS(String opDetail) {
			this(operationIdCounter++, opDetail, 0);
		}

		private OPERATIONS(String opDetail, int opInputParamsMadatorySize) {
			this(operationIdCounter++, opDetail, opInputParamsMadatorySize);
		}

		private OPERATIONS(int opInput, String opDetail, int opInputParamsMadatorySize) {
			this(new Integer(opInput).toString(), opDetail, opInputParamsMadatorySize);
		}

		private OPERATIONS(String opInput, String opDetail, int opInputParamsMadatorySize) {
			this.opInput = opInput;
			this.opInputParamsMadatorySize = opInputParamsMadatorySize;
			if(null != opDetail){
				opDetail = opDetail.replaceAll("@@opInput@@", String.valueOf(opInput));
			}
			this.opDetail = opDetail;
		}

		//simple validation on param counts
		public boolean validateInputParams(String[] args){
			boolean isValid = false;
			if (opInputParamsMadatorySize > 0){
				isValid = null != args && args.length >= opInputParamsMadatorySize;
			} else {
				isValid = true;
			}
			return isValid;
		}

		public abstract HarnessLauncher getHarnessLauncher(Cache cache, String[] args);

		@Override
		public String toString() {
			return String.valueOf(opInput) + " - " + opDetail;
		}

		public static OPERATIONS getById(String input){
			for(OPERATIONS op : values()){
				if(op.opInput.equals(input))
					return op;
			}
			return null;
		}
	}

	public CacheLauncherAPI(String cacheName) {
		try {
			this.cache = CacheUtils.getCache(cacheName);
		} catch (Exception e) {
			log.error("An erro occured while fetching the cache", e);
		}
	}

	public CacheLauncherAPI(Cache cache) {
		this.cache = cache;
	}

	@Override
	public boolean isReady() {
		return null != cache;
	}

	@Override
	public String[] getOptions(){
		String[] options = new String[OPERATIONS.values().length];
		int i = 0;
		for(OPERATIONS op : OPERATIONS.values()){
			options[i] = op.toString();
			i++;
		}
		return options;
	}

	@Override
	public boolean launch(String command, String[] args) throws Exception{
		if(null == command || "".equals(command)){
			System.out.println("Null entry...do nothing.");
			return true; 
		}

		//get the operation based on input
		OPERATIONS operation = OPERATIONS.getById(command);
		if(null == operation){
			System.out.println("Unrecognized entry...do nothing.");
			return true;
		}
		
		if(!operation.validateInputParams(args)){
			System.out.println("Mandatory parameters not specified...do nothing.");
			return true;
		}

		HarnessLauncher launcher = operation.getHarnessLauncher(cache, args);
		if(null != launcher){
			launcher.launch();
		}

		return true;
	}
}
