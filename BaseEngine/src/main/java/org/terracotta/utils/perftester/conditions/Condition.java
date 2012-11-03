package org.terracotta.utils.perftester.conditions;

/**
 * @author Fabien Sanglier
 * 
 */
public interface Condition extends Cloneable {
	boolean isDone();
}
