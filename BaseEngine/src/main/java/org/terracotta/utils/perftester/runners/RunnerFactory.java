package org.terracotta.utils.perftester.runners;

/**
 * @author Fabien Sanglier
 * 
 */
public abstract class RunnerFactory {
	private int numThreads;
	private long numOperations;
	
	protected RunnerFactory(int numThreads, long numOperations) {
		this.numThreads = numThreads;
		this.numOperations = numOperations;
	}
	
	public int getNumThreads() {
		return numThreads;
	}

	public void setNumThreads(int numThreads) {
		this.numThreads = numThreads;
	}

	public long getNumOperations() {
		return numOperations;
	}

	public void setNumOperations(long numOperations) {
		this.numOperations = numOperations;
	}

	public abstract Runner create();
}
