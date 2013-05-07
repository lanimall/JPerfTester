package org.terracotta.utils.perftester.conditions;

/**
 * @author Fabien Sanglier
 * 
 */
public interface Condition<T> extends Cloneable {
	boolean isDone(T obj);
}
