package tourGuide.helper;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class RwLockList<T> {
    ReadWriteLock lock;
    List<T> list;

    public RwLockList(List<T> list) {
        this.list = list;
        this.lock = new ReentrantReadWriteLock();
    }

    public RwListGuard<T> read() {
        return new RwListGuard<>(lock.readLock(), list);
    }

    public RwListGuard<T> write() {
        return new RwListGuard<>(lock.writeLock(), list);
    }

    public static class RwListGuard<T> {
        Lock lock;
        List<T> list;
        boolean isLocked;

        public RwListGuard(Lock lock, List<T> list) {
            this.lock = lock;
            this.list = list;
            lock.lock();
            isLocked = true;
        }

        public List<T> inner() {
            if (isLocked) {
                return list;
            } else {
                return Collections.emptyList();
            }
        }

        public void release() {
            lock.unlock();
            isLocked = false;
        }
    }
}
