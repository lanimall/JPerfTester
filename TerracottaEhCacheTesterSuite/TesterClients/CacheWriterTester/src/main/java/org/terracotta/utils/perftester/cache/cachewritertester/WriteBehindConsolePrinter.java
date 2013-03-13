package org.terracotta.utils.perftester.cache.cachewritertester;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicLong;

import net.sf.ehcache.CacheEntry;
import net.sf.ehcache.CacheException;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import net.sf.ehcache.writer.CacheWriter;
import net.sf.ehcache.writer.writebehind.operations.SingleOperationType;

public class WriteBehindConsolePrinter implements CacheWriter {
	private AtomicLong counter = new AtomicLong(0);
	
	// comment this if using ehcache implementation < 2.5.x
 	@Override
    public void throwAway(Element element, SingleOperationType singleOperationType, RuntimeException e) {
        System.out.println("Calling throw away ...");
    }

    @Override
	public CacheWriter clone(Ehcache arg0) throws CloneNotSupportedException {
		System.out.println("Calling clone ...");
		return null;
	}
	
	@Override
	public void delete(CacheEntry arg0) throws CacheException {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void deleteAll(Collection<CacheEntry> arg0) throws CacheException {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void dispose() throws CacheException {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void write(Element arg0) throws CacheException {
		System.out.println("Write : Key is " + arg0.getKey());
		System.out.println("Write : Value is " + arg0.getValue());
	}
	
	@Override
	public void writeAll(Collection<Element> arg0) throws CacheException {
		// TODO Auto-generated method stub
		System.out.println("Writing All: " + arg0.size() + " elements.");
		System.out.println("Current writing state: " + counter.addAndGet(arg0.size()));
	}
}
