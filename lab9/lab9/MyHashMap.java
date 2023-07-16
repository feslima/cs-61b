package lab9;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 *  A hash table-backed Map implementation. Provides amortized constant time
 *  access to elements via get(), remove(), and put() in the best case.
 *
 *  @author Your name here
 */
public class MyHashMap<K, V> implements Map61B<K, V> {

    private static final int DEFAULT_SIZE = 16;
    private static final double MAX_LF = 0.75;

    private ArrayMap<K, V>[] buckets;
    private int size;

    private int loadFactor() {
        return size / buckets.length;
    }
    private Set<K> keyset;

    public MyHashMap() {
        buckets = new ArrayMap[DEFAULT_SIZE];
        keyset = new HashSet<K>();
        this.clear();
    }

    /* Removes all of the mappings from this map. */
    @Override
    public void clear() {
        this.size = 0;
        for (int i = 0; i < this.buckets.length; i += 1) {
            this.buckets[i] = new ArrayMap<>();
        }
    }

    /** Computes the hash function of the given key. Consists of
     *  computing the hashcode, followed by modding by the number of buckets.
     *  To handle negative numbers properly, uses floorMod instead of %.
     */
    private int hash(K key) {
        if (key == null) {
            return 0;
        }

        int numBuckets = buckets.length;
        return Math.floorMod(key.hashCode(), numBuckets);
    }

    /* Returns the value to which the specified key is mapped, or null if this
     * map contains no mapping for the key.
     */
    @Override
    public V get(K key) {
        ArrayMap<K,V> bucket = buckets[hash(key)];
        if (bucket == null) {
            return null;
        }

        return bucket.get(key);
    }

    /* Associates the specified value with the specified key in this map. */
    @Override
    public void put(K key, V value) {
        ArrayMap<K,V> bucket = buckets[hash(key)];
        if (bucket == null) {
            bucket = new ArrayMap<K, V>();
            buckets[hash(key)] = bucket;
        }

        if (!bucket.containsKey(key)) {
            if (loadFactor() >= MAX_LF) {
                resize(2 * buckets.length);
                bucket = new ArrayMap<K, V>();
                buckets[hash(key)] = bucket;
            }
            keyset.add(key);
            size += 1;
        }
        bucket.put(key, value);
    }

    private void resize(int capacity) {
        ArrayMap<K,V>[] newBuckets =  new ArrayMap[capacity];
        keyset = new HashSet<K>();

        for (K oldKey : this) {
            V value = get(oldKey);
            int newHash = Math.floorMod(oldKey.hashCode(), capacity);

            ArrayMap<K, V> bucket = newBuckets[newHash];
            if (bucket == null) {
                bucket = new ArrayMap<K, V>();
                newBuckets[newHash] = bucket;
            }
            bucket.put(oldKey, value);
            keyset.add(oldKey);
        }

        buckets = newBuckets;
    }

    /* Returns the number of key-value mappings in this map. */
    @Override
    public int size() {
        return size;
    }

    //////////////// EVERYTHING BELOW THIS LINE IS OPTIONAL ////////////////

    /* Returns a Set view of the keys contained in this map. */
    @Override
    public Set<K> keySet() {
        return keyset;
    }

    /* Removes the mapping for the specified key from this map if exists.
     * Not required for this lab. If you don't implement this, throw an
     * UnsupportedOperationException. */
    @Override
    public V remove(K key) {
        V value = get(key);
        buckets[hash(key)] = null;
        keyset.remove(key);
        size -= 1;

        return value;
    }

    /* Removes the entry for the specified key only if it is currently mapped to
     * the specified value. Not required for this lab. If you don't implement this,
     * throw an UnsupportedOperationException.*/
    @Override
    public V remove(K key, V value) {
        V removedValue = get(key);
        if (value == removedValue) {
            buckets[hash(key)] = null;
            keyset.remove(key);
            size -= 1;
            return removedValue;
        }
        return null;
    }

    @Override
    public Iterator<K> iterator() {
        return keyset.iterator();
    }
}
