package org.terracotta.utils.perftester.monitoring;

import java.io.StringWriter;
import java.text.NumberFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Fabien Sanglier
 * 
 */
public class Stats implements Cloneable {
	private static final NumberFormat nf = NumberFormat.getInstance();
	private static final Logger log = LoggerFactory.getLogger(Stats.class);

	private long transactionsCount;
	private long startTime = System.currentTimeMillis();
	private long endTime = 0;
	private long totalTxLatency;
	private double minLatency, maxLatency;

	public Stats() {
		defaultInit();
	}

	public Stats(Stats stat) {
		if (stat != null) {
			init(stat.getTxnCount(), stat.totalTxLatency,
					stat.minLatency, stat.maxLatency, stat.startTime,
					stat.endTime);
		} else {
			defaultInit();
		}
	}

	private void defaultInit() {
		init(0, 0, Double.MAX_VALUE, Double.MIN_VALUE, 0, 0);
	}

	private void init(long transactionsCount, long total, double minLatency,
			double maxLatency, long startTime, long endTime) {

		this.transactionsCount = transactionsCount;
		this.totalTxLatency = total;
		this.minLatency = minLatency;
		this.maxLatency = maxLatency;

		if (startTime != 0)
			this.startTime = startTime;

		if (endTime != 0)
			this.endTime = endTime;
	}

	/**
	 * Add {@link Stats} to the current.
	 *
	 * @param stat
	 * @return {@link this}
	 */
	public Stats add(Stats stat) {
		if(null != stat){
			this.transactionsCount+= stat.getTxnCount();
			this.totalTxLatency += stat.totalTxLatency;

			if (stat.minLatency < this.minLatency)
				this.minLatency = stat.minLatency;

			if (stat.maxLatency > this.maxLatency)
				this.maxLatency = stat.maxLatency;

			// whichever started earlier
			if (stat.startTime < this.startTime)
				this.startTime = stat.startTime;

			// whichever finished later
			if (stat.endTime != 0) {
				if (this.endTime == 0)
					this.endTime = stat.endTime;
				else if (stat.endTime > this.endTime)
					this.endTime = stat.endTime;
			}
		}
		return this;
	}

	/**
	 * Add transaction length
	 *
	 * @param txLength
	 *            transaction length
	 */
	public void add(long txLatency) {
		if (txLatency > Short.MAX_VALUE) {
			log.warn("stat transaction latency exceeds 32 secs, txLength = {}", txLatency);
		}

		transactionsCount += 1;
		totalTxLatency += txLatency;
		if (txLatency < minLatency) {
			minLatency = txLatency;
		}
		if (txLatency > maxLatency) {
			maxLatency = txLatency;
		}
	}

	/**
	 * @return average latency
	 */
	public double getAvgLatency() {
		if (transactionsCount > 0)
			return (double) totalTxLatency / transactionsCount;
		return 0;
	}

	/**
	 *
	 * @return max latency
	 */
	public double getMaxLatency() {
		if (maxLatency == Double.MIN_VALUE)
			return Double.NaN;
		return maxLatency;
	}

	/**
	 *
	 * @return min latency
	 */
	public double getMinLatency() {
		if (minLatency == Double.MAX_VALUE)
			return Double.NaN;
		return minLatency;
	}

	/**
	 * resets the stats to {@link init()}
	 */
	public void reset() {
		init(0, 0, Double.MAX_VALUE, Double.MIN_VALUE, System.currentTimeMillis(), 0);
	}

	/**
	 * @return total txn count
	 */
	public long getTxnCount() {
		return transactionsCount;
	}

	public long getThroughput() {
		long end = (endTime != 0) ? endTime : System.currentTimeMillis();
		long time = end - this.startTime;
		if (time == 0)
			time = 1;
		return getTxnCount() * 1000 / time;
	}

	public void finalise() {
		if (endTime != 0)
			// stats can be finalized only once, but can be called multiple times
			return;

		this.endTime = System.currentTimeMillis();
	}

	public void printToConsole(){
		printToConsole(null, null);
	}
	
	public void printToConsole(String headerMessage){
		printToConsole(headerMessage, null);
	}
	
	public void printToConsole(String headerMessage, String footerMessage){
		StringWriter toPrint = new StringWriter();
		toPrint.append("[" + Thread.currentThread().getName() + "] ");
		if(null != headerMessage && !"".equals(headerMessage)){
			toPrint.append(headerMessage);
		} else {
			toPrint.append("Thread Stats:");
		}
		
		toPrint.append(" [").append(this.toString()).append("] ");
		
		if(null != footerMessage && !"".equals(footerMessage)){
			toPrint.append(footerMessage);
		}
		
		System.out.println(toPrint);
	}

	@Override
	public String toString() {
		return String
		.format("Exec Time(ms): %s, Txns: %s, TPS: %s, Latency(ms): Avg: %s, Min: %s, Max: %s",
				(endTime > 0)?nf.format(endTime-startTime):"N/A (endTime null)",
				nf.format(this.getTxnCount()),
				nf.format(this.getThroughput()),
				nf.format(this.getAvgLatency()),
				nf.format(this.getMinLatency()),
				nf.format(this.getMaxLatency()));
	}
}
