package com.whenling.castle.support.core.test;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.StampedLock;
import java.util.stream.IntStream;

import org.junit.Test;

public class Java8ConcurrencyBySynchronizationAndLocks {

	int count = 0;

	void increment() {
		count = count + 1;
	}

	synchronized void incrementSynchronized() {
		count = count + 1;
	}

	ReentrantLock lock = new ReentrantLock();

	void incrementByReentrantLock() {
		lock.lock();
		try {
			count++;
		} finally {
			lock.unlock();
		}
	}

	@Test
	public void done() {
		ExecutorService executor = Executors.newFixedThreadPool(2);

		IntStream.range(0, 10000).forEach(i -> executor.submit(this::incrementByReentrantLock));

		stop(executor);

		System.out.println(count); // 9965
	}

	@Test
	public void reentrantLock() {
		ExecutorService executor = Executors.newFixedThreadPool(2);
		ReentrantLock lock = new ReentrantLock();

		executor.submit(() -> {
			lock.lock();
			System.out.println("start ");
			try {
				sleep(1);
				System.out.println("end ");
			} finally {
				lock.unlock();
			}
		});

		executor.submit(() -> {
			System.out.println("Locked: " + lock.isLocked());
			System.out.println("Held by me: " + lock.isHeldByCurrentThread());
			boolean locked = lock.tryLock();
			System.out.println("Lock acquired: " + locked);
		});

		stop(executor);

	}

	@Test
	public void readWriteLock() {
		ExecutorService executor = Executors.newFixedThreadPool(2);
		Map<String, String> map = new HashMap<>();
		ReadWriteLock lock = new ReentrantReadWriteLock();

		executor.submit(() -> {
			lock.writeLock().lock();
			try {
				sleep(1);
				map.put("foo", "bar");
			} finally {
				lock.writeLock().unlock();
			}
		});

		Runnable readTask = () -> {
			lock.readLock().lock();
			try {
				System.out.println(map.get("foo"));
				sleep(1);
			} finally {
				lock.readLock().unlock();
			}
		};

		executor.submit(readTask);
		executor.submit(readTask);

		stop(executor);
	}

	@Test
	public void stampedLockForReadWrite() {
		ExecutorService executor = Executors.newFixedThreadPool(2);
		Map<String, String> map = new HashMap<>();
		StampedLock lock = new StampedLock();

		executor.submit(() -> {
			long stamp = lock.writeLock();
			try {
				sleep(1);
				map.put("foo", "bar");
			} finally {
				lock.unlockWrite(stamp);
			}
		});

		Runnable readTask = () -> {
			long stamp = lock.readLock();
			try {
				System.out.println(map.get("foo"));
				sleep(1);
			} finally {
				lock.unlockRead(stamp);
			}
		};

		executor.submit(readTask);
		executor.submit(readTask);

		stop(executor);

	}

	@Test
	public void stampedLockForOptimistic() {
		ExecutorService executor = Executors.newFixedThreadPool(2);
		StampedLock lock = new StampedLock();

		executor.submit(() -> {
			long stamp = lock.tryOptimisticRead();
			try {
				System.out.println("Optimistic Lock Valid: " + lock.validate(stamp));
				sleep(1);
				System.out.println("Optimistic Lock Valid: " + lock.validate(stamp));
				sleep(2);
				System.out.println("Optimistic Lock Valid: " + lock.validate(stamp));
			} finally {
				lock.unlock(stamp);
			}
		});

		executor.submit(() -> {
			long stamp = lock.writeLock();
			try {
				System.out.println("Write Lock acquired");
				sleep(2);
			} finally {
				lock.unlock(stamp);
				System.out.println("Write done");
			}
		});

		stop(executor);

	}

	@Test
	public void stampedLockForConvert() {
		ExecutorService executor = Executors.newFixedThreadPool(2);
		StampedLock lock = new StampedLock();

		executor.submit(() -> {
			long stamp = lock.readLock();
			try {
				if (count == 0) {
					stamp = lock.tryConvertToWriteLock(stamp);
					if (stamp == 0L) {
						System.out.println("Could not convert to write lock");
						stamp = lock.writeLock();
					}
					count = 23;
				}
				System.out.println(count);
			} finally {
				lock.unlock(stamp);
			}
		});

		stop(executor);

	}

	private void sleep(int i) {

		try {
			TimeUnit.SECONDS.sleep(i);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void stop(ExecutorService executor) {

		try {
			System.out.println("attempt to shutdown executor");
			executor.shutdown();
			executor.awaitTermination(5, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			System.err.println("tasks interrupted");
		} finally {
			if (!executor.isTerminated()) {
				System.err.println("cancel non-finished tasks");
			}
			executor.shutdownNow();
			System.out.println("shutdown finished");
		}
	}
}
