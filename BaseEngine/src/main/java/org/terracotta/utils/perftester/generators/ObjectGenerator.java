package org.terracotta.utils.perftester.generators;

/**
 * @author Fabien Sanglier
 * 
 */
public interface ObjectGenerator<T> {
	public T generate();
}
