package org.terracotta.utils.perftester.generators;

/**
 * @author Fabien Sanglier
 * 
 */
public interface ObjectGenerator<T> extends Cloneable {
	public T generate();
}
