package com.noah;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

public class App {

	public static void main(String[] args) throws Exception {

		//if ehcache.xml is under /src/main/resources then it can be detected automatically
		//or it need to tell ehcache where it is, i.e. we put it under /config folder
		/*
			CacheManager cacheManager1 = CacheManager.create(App.class.getResourceAsStream("/config/ehcache.xml"));
			System.out.println(cacheManager1.getCacheNames()[0]);
			Cache sampleCache = cacheManager1.getCache(cacheManager1.getCacheNames()[0]);
			sampleCache.put(new Element("key1", "value1"));
			checkElement(sampleCache, "key1");
			checkElement(sampleCache, "key1");
			checkElement(sampleCache, "key1");
		*/
		
		
		// And we can also define a new cache without ehcache.xml
		// because EhCache provide a default config file ehcache-failsafe.xml
		CacheManager cacheManager2 = CacheManager.create();
		cacheManager2.addCache(new Cache("sampleCache2", 1, false, false, 5, 3));
		System.out.println(cacheManager2.getCacheNames()[0]);

		Cache sampleCache2 = cacheManager2.getCache(cacheManager2.getCacheNames()[0]);
		sampleCache2.put(new Element("key2", "value2"));
		// we can define the timeToIdleSeconds and timeToLiveSeconds when new Element, to override the Cache setting
		//sampleCache2.put(new Element("key2", "value2", 6,10));
		
		checkElement(sampleCache2, "key2");
		checkElement(sampleCache2, "key2");
		checkElement(sampleCache2, "key2");
		
		
		//if we add a Cache which enable "overflowToDisk" or "diskPersistent" into a Cache Manager, like
		//new Cache("sampleCache2", 1, true, false, 5, 3)); the 3th param is true means it enable the overflowToDisk
		//and do not config diskStore correctly, then the JVM will never terminate
		//Below segment from ehcache-failsafe.xml can explain
		/**
		   The diskStore element is optional. It must be configured if you have overflowToDisk or diskPersistent enabled
           for any cache. If it is not configured, a warning will be issues and java.io.tmpdir will be used.
           
           overflowToDisk: Sets whether elements can overflow to disk when the in-memory cache has reached the maxInMemory limit.
		 * */
		//When the thread running main(String [] args) is going to finish, JVM will recollect the memory
		//Then the cache will reach in-memory limit, then need to store it to disk
		//But diskStore does not configured correctly, so it keeps waiting.
		

	}

	private static void checkElement(Cache cache, String key) throws Exception {
		Thread.sleep(2000L);
		Element element = cache.get(key);
		if (element == null) {
			System.out.println(key+" is expired");
		} else {
			System.out.println(element.getObjectKey() + " -> " + element.getObjectValue());
		}
	}

}
