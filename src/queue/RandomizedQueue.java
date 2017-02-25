package queue;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Created by oderor on 2/25/2017.
 */
public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] q;       // queue elements
    private int n;          // number of elements on queue

    // construct an empty randomized queue
    public RandomizedQueue() {
        q = (Item[]) new Object[2];
        n = 0;
    }

    // resize the underlying array
    private void resize(int capacity) {
        assert capacity >= n;
        Item[] temp = (Item[]) new Object[capacity];
        System.arraycopy(q, 0, temp, 0, n);
        q = temp;
    }

    // is the queue empty?
    public boolean isEmpty() {
        return n == 0;
    }

    // return the number of items on the queue
    public int size() {
        return n;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) {
            throw new NullPointerException();
        }
        if (n == q.length) {
            resize(2 * n);
        }
        q[n++] = item;
    }

    // remove and return a random item
    public Item dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        int i = StdRandom.uniform(n);
        Item item = q[i];
        q[i] = q[--n];
        q[n] = null;
        if (n > 0 && n == q.length / 4) {
            resize(q.length / 2);
        }
        return item;
    }

    // return (but do not remove) a random item
    public Item sample() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        int i = StdRandom.uniform(n);
        return q[i];
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator();
    }

    private class RandomizedQueueIterator implements Iterator<Item> {
        private int i = 0;
        private Item[] temp;

        public RandomizedQueueIterator() {
            temp = (Item[]) new Object[n];
            System.arraycopy(q, 0, temp, 0, n);
        }

        public boolean hasNext() {
            return i < n;
        }

        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            int k = StdRandom.uniform(n - i);
            Item item = temp[k];
            temp[k] = temp[n - (++i)];
            temp[n - i] = null;
            return item;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    /**
     * Returns a string representation of this queue.
     *
     * @return the sequence of items in FIFO order, separated by spaces
     */
    public String toString() {
        StringBuilder s = new StringBuilder();
        for (Item item : this) {
            s.append(item);
            s.append(' ');
        }
        return s.toString();
    }

    // unit testing (optional)
    public static void main(String[] args) {
        RandomizedQueue<Integer> randomizedQueue = new RandomizedQueue<>();
        for (int i = 0; i < 5; i++) {
            randomizedQueue.enqueue(i);
        }
        StdOut.println("Queue: " + randomizedQueue.toString());
        for (int i = 0; i < 5; i++) {
            StdOut.print(randomizedQueue.dequeue());
            StdOut.print(' ');
        }
    }
}
