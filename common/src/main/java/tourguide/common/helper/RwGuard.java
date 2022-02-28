package tourguide.common.helper;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class RwGuard<T> {
    ReadWriteLock lock;
    T inner;

    public RwGuard(T inner) {
        this.inner = inner;
        this.lock = new ReentrantReadWriteLock();
    }

    public void read(Operation<T> op) {
        Lock lock = this.lock.readLock();
        lock.lock();
        op.operate(inner);
        lock.unlock();
    }

    public void write(Operation<T> op) {
        Lock lock = this.lock.writeLock();
        lock.lock();
        op.operate(inner);
        lock.unlock();
    }

    public <R> R result_read(ResultOperation<T, R> op) {
        Lock lock = this.lock.readLock();
        lock.lock();
        R result = op.operate(inner);
        lock.unlock();
        return result;
    }

    public <R> R result_write(ResultOperation<T, R> op) {
        Lock lock = this.lock.writeLock();
        lock.lock();
        R result = op.operate(inner);
        lock.unlock();
        return result;
    }

    public interface Operation<T> {
        void operate(T inner);
    }

    public interface ResultOperation<T, R> {
        R operate(T inner);
    }
}
