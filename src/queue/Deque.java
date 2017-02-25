package queue;

import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Created by oderor on 2/25/2017.
 */
public class Deque<Item> implements Iterable<Item> {
    private Node<Item> first;    // beginning of deque
    private Node<Item> last;     // end of deque
    private int n;               // number of elements on deque

    // helper linked list class
    private static class Node<Item> {
        private Item item;
        private Node<Item> next, prev;

        public Node(Item item) {
            this.item = item;
            next = null;
            prev = null;
        }
    }

    // construct an empty deque
    public Deque() {
        first = null;
        last = null;
        n = 0;
    }

    // is the deque empty?
    public boolean isEmpty() {
        return n == 0;
    }

    // return the number of items on the deque
    public int size() {
        return n;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null) {
            throw new NullPointerException();
        }

        Node<Item> oldFirst = first;
        first = new Node<>(item);
        first.next = oldFirst;
        first.prev = null;

        if (isEmpty()) {
            last = first;
        } else {
            oldFirst.prev = first;
        }
        n++;
    }

    // add the item to the end
    public void addLast(Item item) {
        if (item == null) {
            throw new NullPointerException();
        }

        Node<Item> oldLast = last;
        last = new Node<>(item);
        last.next = null;
        last.prev = oldLast;

        if (isEmpty()) {
            first = last;
        } else {
            oldLast.next = last;
        }
        n++;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        Item item = first.item;
        first = first.next;
        n--;

        if (isEmpty()) {
            last = null;
        } else {
            first.prev = null;
        }
        return item;
    }

    // remove and return the item from the end
    public Item removeLast() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        Item item = last.item;
        last = last.prev;
        n--;

        if (isEmpty()) {
            first = null;
        } else {
            last.next = null;
        }
        return item;
    }

    // return an iterator over items in order from front to end
    public Iterator<Item> iterator() {
        return new DequeIterator(first);
    }

    // an iterator, doesn't implement remove() since it's optional
    private class DequeIterator implements Iterator<Item> {
        private Node<Item> current;

        public DequeIterator(Node<Item> first) {
            current = first;
        }

        public boolean hasNext() {
            return current != null;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            Item item = current.item;
            current = current.next;
            return item;
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
        Deque<Integer> deque = new Deque<>();
        for (int i = 0; i < 1; i++) {
            deque.addFirst(i);
            deque.addLast(i);
            deque.removeLast();
            deque.removeFirst();
        }
        StdOut.println("Queue (addFirst): " + deque.toString());
    }
}
