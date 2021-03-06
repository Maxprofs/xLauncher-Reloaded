package ru.xeroxp.launcher.process;

import java.lang.reflect.Array;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@SuppressWarnings("SameParameterValue")
public class LimitedCapacityList {

    private final Object[] items;
    private final Class clazz;
    private final ReadWriteLock locks = new ReentrantReadWriteLock();
    private int size;
    private int head;


    public LimitedCapacityList(Class clazz, int maxSize) {
        this.clazz = clazz;
        this.items = (Object[]) Array.newInstance(clazz, maxSize);
    }

    public void add(Object value) {
        this.locks.writeLock().lock();
        this.items[this.head] = value;
        this.head = (this.head + 1) % this.getMaxSize();
        if (this.size < this.getMaxSize()) {
            ++this.size;
        }

        this.locks.writeLock().unlock();
    }

    private int getMaxSize() {
        this.locks.readLock().lock();
        int result = this.items.length;
        this.locks.readLock().unlock();
        return result;
    }

    public Object[] getItems() {
        Object[] result = (Object[]) Array.newInstance(this.clazz, this.size);
        this.locks.readLock().lock();

        for (int i = 0; i < this.size; ++i) {
            int pos = (this.head - this.size + i) % this.getMaxSize();
            if (pos < 0) {
                pos += this.getMaxSize();
            }

            result[i] = this.items[pos];
        }

        this.locks.readLock().unlock();
        return result;
    }
}
