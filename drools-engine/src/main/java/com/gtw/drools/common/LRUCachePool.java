package com.gtw.drools.common;

import java.util.LinkedHashMap;
import java.util.Map.Entry;

/**
 * 采用LRU淘汰方式的缓存池
 * @author gtw
 */
public class LRUCachePool<K, V> extends LinkedHashMap<K, V> {

    private final int CACHE_SIZE;

    public LRUCachePool(int cacheSize) {
        super((int) Math.ceil(cacheSize / 0.75) + 1, 0.75f, true);
        this.CACHE_SIZE = cacheSize;
    }

    @Override
    protected boolean removeEldestEntry(Entry<K, V> eldest) {
        // 当map中的数据量大于指定的缓存个数的时候，就自动删除最老的数据
        return size() > CACHE_SIZE;
    }
}
