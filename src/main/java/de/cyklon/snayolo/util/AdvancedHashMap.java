package de.cyklon.snayolo.util;

import io.github.cyklon73.cytils.utils.Pair;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;


public class AdvancedHashMap<K, V> extends LinkedHashMap<K, V> implements Map<K, V>, Iterable<Pair<K, V>> {

    public AdvancedHashMap(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
    }

    public AdvancedHashMap(int initialCapacity) {
        super(initialCapacity);
    }

    public AdvancedHashMap() {
    }

    public AdvancedHashMap(Map<? extends K, ? extends V> m) {
        super(m);
    }

    public AdvancedHashMap(int initialCapacity, float loadFactor, boolean accessOrder) {
        super(initialCapacity, loadFactor, accessOrder);
    }

    public AdvancedHashMap(Collection<Pair<K, V>> collection) {
        for (Pair<K, V> pair : collection) {
            put(pair);
        }
    }

    private void put(Pair<K, V> pair) {
        put(pair.getFirst(), pair.getSecond());
    }

    public Spliterator<Pair<K, V>> spliterator() {
        return Spliterators.spliterator(this.asCollection(), 0);
    }

    public Stream<Pair<K, V>> stream() {
        return StreamSupport.stream(spliterator(), false);
    }

    public Stream<Pair<K, V>> parallelStream() {
        return StreamSupport.stream(spliterator(), true);
    }

    public Iterator<Pair<K, V>> iterator() {
        Iterator<Pair<K, V>> baseIterator = this.asCollection().iterator();
        return new Iterator<Pair<K, V>>() {
            @Override
            public boolean hasNext() {
                return baseIterator.hasNext();
            }

            @Override
            public Pair<K, V> next() {
                return baseIterator.next();
            }

            @Override
            public void remove() {
                AdvancedHashMap.this.remove(AdvancedHashMap.this.size()-1);
            }

            @Override
            public void forEachRemaining(Consumer action) {
                baseIterator.forEachRemaining(action);
            }
        };
    }

    public Collection<Pair<K, V>> asCollection() {
        List<Pair<K, V>> list = new ArrayList<>();
        this.forEach((k, v) -> list.add(new Pair<>(k, v)));
        return list;
    }

    public Pair<K, V> get(int index) {
        AtomicInteger curr = new AtomicInteger();
        AtomicBoolean flag = new AtomicBoolean(false);
        AtomicReference<Pair<K, V>> value = null;
        this.forEach((k, v) -> {
            if (curr.get() ==index) {
                value.set(new Pair<>(k, v));
                flag.set(true);
            }
            curr.getAndIncrement();
        });
        if (flag.get()) return value.get();
        throw new IndexOutOfBoundsException(String.format("Index %s is not in length %s", index, this.size()));
    }

    public void remove(int index) {
        Pair<K, V> pair = get(index);
        remove(pair.getFirst(), pair.getSecond());
    }

}
