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
 * @param <T>
 */
public abstract class BufferedKeyRunner<K> extends KeyRunner<K> implements Runner {
	private static Logger log = LoggerFactory.getLogger(BufferedKeyRunner.class);
	private static final boolean isDebug = log.isDebugEnabled();
	private int bufferCounter = 0;
	private int bufferSize;
	private List<K> buffer;

	protected BufferedKeyRunner(Condition termination, ObjectGenerator<K> keyGenerator, int bufferSize) {
		super(termination,keyGenerator);

		this.bufferSize = bufferSize;
		this.bufferCounter = 0;
		this.buffer = new ArrayList<K>(bufferSize);
	}

	private boolean isBufferNonEmpty(){
		return bufferCounter > 0;
	}

	private boolean isBufferFull(){
		return bufferCounter >= bufferSize;
	}

	private void addToBuffer(K key){
		this.buffer.add(key);
		this.bufferCounter++;
	}

	private void clearBuffer(){
		if(isDebug)
			log.debug("Clearing buffer");
		this.buffer.clear();
		this.bufferCounter = 0;
	}

	private Object flushBuffer(){
		if(isDebug)
			log.debug("Buffer full...time to flush and clear.");

		try{
			return doUnitOfWorkInternal(System.currentTimeMillis(), buffer);
		} finally {
			clearBuffer();
		}
	}
	
	private Object flushBufferNoStats(){
		if(isDebug)
			log.debug("Buffer full...time to flush and clear.");

		try{
			return doUnitOfWorkInternalNoStats(buffer);
		} finally {
			clearBuffer();
		}
	}

	public abstract Object doUnitOfWork(List<K> key);

	@Override
	public Object doUnitOfWork(K key){
		throw new UnsupportedOperationException("This method should not be accessed through the buffered implementation");
	}

	private Object doUnitOfWorkInternal(long iterationStartTime, List<K> key){
		Object unitOfWorkResult = null;
		try {
			unitOfWorkResult = doUnitOfWork(key);

			stats.add(System.currentTimeMillis() - iterationStartTime);
		} catch (Exception e) {
			log.error("Error during execution of unit of work. will not count this operation in the timing stats.", e);
		}
		return unitOfWorkResult;
	}

	private Object doUnitOfWorkInternalNoStats(List<K> key){
		Object unitOfWorkResult = null;
		try {
			unitOfWorkResult = doUnitOfWork(key);
		} catch (Exception e) {
			log.error("Error during execution of unit of work. will not count this operation in the timing stats.", e);
		}
		return unitOfWorkResult;
	}

	@Override
	protected void execute() {
		long iterationStartTime;
		Object unitOfWorkResult = null;
		K key = null;

		if(enableDefaultStats()){
			do {
				//perform the object generation outside the timing to time more accurately the doUnitOfWork operation
				key = keyGenerator.generate();

				addToBuffer(key);
				if ( isBufferFull() ) {
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

				addToBuffer(key);
				if ( isBufferFull() ) {
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
