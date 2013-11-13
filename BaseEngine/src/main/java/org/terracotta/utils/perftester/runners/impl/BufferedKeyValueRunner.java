package org.terracotta.utils.perftester.runners.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.terracotta.utils.perftester.conditions.Condition;
import org.terracotta.utils.perftester.generators.ObjectGenerator;
import org.terracotta.utils.perftester.runners.Runner;

/**
 * @author Fabien Sanglier
 * 
 * An abstract Runner meant to encapsulate complex operation, allowing sub-classes to implement only a certain number of work methods:
 *  - doBeforeLoad(): init operations to execute before the multi-threaded load
 *  - doAfterLoad(): clean up operations to execute after the multi-threaded load
 *  - getUnitOfWork(): detail of the unit of work that each thread will repeatedly execute 
 *  
 * Not Thread Safe on purpose: each thread should have their own instance of this class
 * 
 * @param <T>
 * 
 */
public abstract class BufferedKeyValueRunner<K, V> extends KeyValueRunner<K, V> implements Runner {
	private static Logger log = LoggerFactory.getLogger(BufferedKeyValueRunner.class);

	private static final boolean isDebug = log.isDebugEnabled();
	private int bufferCounter = 0;
	private int bufferSize;
	private List<K> bufferKeys;
	private List<V> bufferValues;

	protected BufferedKeyValueRunner(Condition termination, ObjectGenerator<K> keyGenerator, ObjectGenerator<V> valueGenerator, int bufferSize) {
		super(termination, keyGenerator, valueGenerator);

		this.bufferSize = bufferSize;
		this.bufferCounter = 0;
		this.bufferKeys = new ArrayList<K>(bufferSize);
		this.bufferValues = new ArrayList<V>(bufferSize);
	}

	private boolean isBufferNonEmpty(){
		return bufferCounter > 0;
	}
	
	private boolean isBufferFull(){
		return bufferCounter >= bufferSize;
	}

	private void addToBuffer(K key, V value){
		this.bufferKeys.add(key);
		this.bufferValues.add(value);
		this.bufferCounter++;
	}

	private void clearBuffer(){
		if(isDebug)
			log.debug("Clearing buffer");
		this.bufferKeys.clear();
		this.bufferValues.clear();
		this.bufferCounter = 0;
	}

	private Object flushBuffer(){
		if(isDebug)
			log.debug("Buffer full...time to flush and clear.");

		try{
			return doUnitOfWorkInternal(System.currentTimeMillis(), bufferKeys, bufferValues);
		} finally {
			clearBuffer();
		}
	}
	
	private Object flushBufferNoStats(){
		if(isDebug)
			log.debug("Buffer full...time to flush and clear.");

		try{
			return doUnitOfWorkInternalNoStats(bufferKeys, bufferValues);
		} finally {
			clearBuffer();
		}
	}
	
	private Object doUnitOfWorkInternal(long iterationStartTime, List<K> key, List<V> values){
		Object unitOfWorkResult = null;
		try {
			unitOfWorkResult = doUnitOfWork(key, values);

			stats.add(System.currentTimeMillis() - iterationStartTime);
		} catch (Exception e) {
			log.error("Error during execution of unit of work. will not count this operation in the timing stats.", e);
		}
		return unitOfWorkResult;
	}

	private Object doUnitOfWorkInternalNoStats(List<K> key, List<V> values){
		Object unitOfWorkResult = null;
		try {
			unitOfWorkResult = doUnitOfWork(key, values);
		} catch (Exception e) {
			log.error("Error during execution of unit of work. will not count this operation in the timing stats.", e);
		}
		return unitOfWorkResult;
	}
	
	public abstract Object doUnitOfWork(List<K> keys, List<V> values);

	@Override
	public Object doUnitOfWork(K key, V value){
		throw new UnsupportedOperationException("This method should not be accessed through the buffered implementation");
	}

	@Override
	protected void execute() {
		Object unitOfWorkResult = null;
		K key = null;
		V value = null;

		if(enableDefaultStats()){ 
			do {
				//perform the object generation outside the timing to time more accurately the doUnitOfWork operation
				key = keyGenerator.generate();
				value = valueGenerator.generate();
				
				addToBuffer(key, value);
				if (isBufferFull()) {
					unitOfWorkResult = flushBuffer();
				}
			} while (!termination.isDone(unitOfWorkResult));
			
			//after the while loop. make sure to execute the remaining operations + clear the buffer
			if (isBufferNonEmpty()) {
				unitOfWorkResult = flushBuffer();
			}
		} else {
			do {
				//perform the object generation outside the timing to time more accurately the doUnitOfWork operation
				key = keyGenerator.generate();
				value = valueGenerator.generate();
				
				addToBuffer(key, value);
				if (isBufferFull()) {
					unitOfWorkResult = flushBufferNoStats();
				}
			} while (!termination.isDone(unitOfWorkResult));
			
			//after the while loop. make sure to execute the remaining operations + clear the buffer
			if (isBufferNonEmpty()) {
				unitOfWorkResult = flushBufferNoStats();
			}
		}
	}
}
