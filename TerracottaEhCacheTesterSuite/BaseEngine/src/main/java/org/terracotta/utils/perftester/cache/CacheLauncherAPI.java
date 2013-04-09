package org.terracotta.utils.perftester.cache;

import net.sf.ehcache.Cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.terracotta.utils.commons.cache.CacheUtils;
import org.terracotta.utils.commons.cache.configs.GlobalConfigSingleton;
import org.terracotta.utils.perftester.LauncherAPI;
import org.terracotta.utils.perftester.cache.launchers.BaseCacheLauncher;
import org.terracotta.utils.perftester.cache.launchers.CachePutLauncher;
import org.terracotta.utils.perftester.cache.launchers.CacheRandomGetLauncher;
import org.terracotta.utils.perftester.cache.launchers.CacheRandomMixLauncher;
import org.terracotta.utils.perftester.cache.launchers.CacheSearchLauncher;
import org.terracotta.utils.perftester.cache.launchers.CacheSequentialGetLauncher;
import org.terracotta.utils.perftester.generators.ObjectGeneratorFactory;

public class CacheLauncherAPI implements LauncherAPI {
	private static Logger log = LoggerFactory.getLogger(CacheLauncher.class);
	private Cache cache = null;

	private static int operationIdCounter = 1;
	private enum OPERATIONS {
		OP_LOAD("Load cache with elements (Usage: @@opInput@@ <number of elements>)"),
		OP_WARMUP("Perform cache warmup (Usage: @@opInput@@ <Threads> <iterations>)"),
		OP_RDM_GETS("Perform random gets (Usage: @@opInput@@ <Threads> <iterations>)"),
		OP_RDM_PUTS("Perform random puts (Usage: @@opInput@@ <Threads> <iterations>)"),
		OP_RDM_SEARCH("Perform random searches (Usage: @@opInput@@ <Threads> <iterations>)"),
		OP_RDM_MIX("Perform cache gets/puts/searches (Usage: @@opInput@@ <% Gets> <% Puts> <% Search> (default: 60 25 15))"),
		OP_QUIT('Q', "Quit program");

		private char opInput;
		private String opDetail;

		private OPERATIONS() {
			this(operationIdCounter++, "");
		}
		
		private OPERATIONS(String opDetail) {
			this(operationIdCounter++, opDetail);
		}
		
		private OPERATIONS(int opInput, String opDetail) {
			this(new Integer(opInput).toString().charAt(0), opDetail);
		}
		
		private OPERATIONS(char opInput, String opDetail) {
			this.opInput = opInput;
			if(null != opDetail){
				opDetail = opDetail.replaceAll("@@opInput@@", String.valueOf(opInput));
			}
			this.opDetail = opDetail;
		}

		@Override
		public String toString() {
			return String.valueOf(opInput) + " - " + opDetail;
		}

		public static OPERATIONS getById(char input){
			for(OPERATIONS op : values()){
				if(op.opInput == input)
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
		return null != getCache();
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
		BaseCacheLauncher cacheLauncher = null;

		if(null == command || "".equals(command)){
			System.out.println("Null entry...do nothing.");
			return true; 
		}

		//get the operation based on input
		OPERATIONS opInput = OPERATIONS.getById(command.charAt(0));
		
		switch (opInput) {
		case OP_LOAD:
			cacheLauncher = getObjectLoadLauncher();
			break;
		case OP_WARMUP:
			cacheLauncher = getObjectWarmupLauncher();
			break;
		case OP_RDM_PUTS:
			cacheLauncher = getObjectLoadLauncher();
			break;
		case OP_RDM_GETS:
			cacheLauncher = getObjectRandomGetLauncher();
			break;
		case OP_RDM_SEARCH:
			cacheLauncher = getObjectSearchLauncher();
			break;
		case OP_RDM_MIX:
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
						((CacheRandomMixLauncher)cacheLauncher).addCacheGetOperationMix(mix, getCache(), getObjectRandomGetLauncher().getRunnerFactory());
						break;
					case 1:
						((CacheRandomMixLauncher)cacheLauncher).addCachePutOperationMix(mix, getCache(), getObjectLoadLauncher(false, false).getRunnerFactory());
						break;
					case 2:
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
						
						((CacheRandomMixLauncher)cacheLauncher).addCacheSearchOperationMix(mix, getCache(), getObjectSearchLauncher().getRunnerFactory());
						break;
					}
				} catch (Exception e) {
					log.error("Could not add the operation to the ramdomMix", e);
				}
			}
			break;
		case OP_QUIT:
			return false;
		default:
			System.out.println("Unrecognized entry...do nothing.");
			return true;
		}

		if(null != cacheLauncher){
			cacheLauncher.setMultiClientEnabled(GlobalConfigSingleton.getInstance().isAppMultiClientsSyncEnabled());
			cacheLauncher.setNumClients(GlobalConfigSingleton.getInstance().getAppNbClients());
			cacheLauncher.launch();
		}

		return true;
	}
	
	protected Cache getCache() {
		return cache;
	}
	
	//the following method are created to potentially be overwritten by descendant launchers
	protected BaseCacheLauncher getObjectLoadLauncher(){
		return getObjectLoadLauncher(GlobalConfigSingleton.getInstance().isCacheLoaderDoBulkLoad(),GlobalConfigSingleton.getInstance().isCacheLoaderRemoveAll());
	}
	
	protected BaseCacheLauncher getObjectLoadLauncher(boolean doBulkLoad, boolean doRemoveAllFirst){
		return new CachePutLauncher(
				getCache(),
				GlobalConfigSingleton.getInstance().getCacheLoaderThreads(),
				GlobalConfigSingleton.getInstance().getCacheLoaderNbObjects(), 
				getObjectGeneratorFactory(GlobalConfigSingleton.getInstance().getCacheLoaderKeyGenFactory()),
				getObjectGeneratorFactory(GlobalConfigSingleton.getInstance().getCacheLoaderValueGenFactory()),
				GlobalConfigSingleton.getInstance().getCacheLoaderKeyStart(),
				doBulkLoad, 
				doRemoveAllFirst);
	}
	
	protected BaseCacheLauncher getObjectWarmupLauncher(){
		return new CacheSequentialGetLauncher(
				getCache(),
				GlobalConfigSingleton.getInstance().getCacheWarmerThreads(), 
				GlobalConfigSingleton.getInstance().getCacheWarmerNbObjects(), 
				GlobalConfigSingleton.getInstance().getCacheWarmerKeyStart());
	}
	
	protected BaseCacheLauncher getObjectRandomGetLauncher(){
		return new CacheRandomGetLauncher(
				getCache(), 
				GlobalConfigSingleton.getInstance().getCacheSteadyStateThreads(), 
				GlobalConfigSingleton.getInstance().getCacheSteadyStateNbOperations(), 
				GlobalConfigSingleton.getInstance().getCacheSteadyStateKeyMinValue(),
				GlobalConfigSingleton.getInstance().getCacheSteadyStateKeyMaxValue()
		);
	}
	
	protected BaseCacheLauncher getObjectSearchLauncher(){
		return new CacheSearchLauncher(
				getCache(),
				GlobalConfigSingleton.getInstance().getCacheLoaderThreads(),
				GlobalConfigSingleton.getInstance().getCacheLoaderNbObjects(),
				getObjectGeneratorFactory(GlobalConfigSingleton.getInstance().getCacheSearchQueryGenFactory())
		);
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
