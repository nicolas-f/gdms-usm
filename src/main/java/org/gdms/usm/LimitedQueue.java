/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gdms.usm;

import java.util.AbstractQueue;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * A Queue structure which has a limited size.
 * If an element has to be added, it checks if the Queue is full.
 * In that case, it will automatically remove the oldest element and then add the new one.
 * @author Thomas Salliou
 */
public class LimitedQueue<E> extends AbstractQueue<E> {

    private LinkedList<E> ll;
    private int size;
    private int maxSize;

    /**
     * Builds a new LimitedQueue with an integer limit.
     * @param s the limit
     */
    public LimitedQueue(int s) {
        this.ll = new LinkedList<E>();
        this.maxSize = s;
        this.size = 0;
    }

    @Override
    public Iterator<E> iterator() {
        return new LimitedQueueIterator();
    }

    @Override
    public int size() {
        return size;
    }

    /**
     * Checks the size of the list in order to choose behavior :
     * If full, removes the oldest element and adds the new one.
     * If not, adds the new element and increments the size.
     * @param e the element to be added
     * @return a boolean
     */
    public boolean offer(E e) {
        if (this.size == this.maxSize) {
            ll.removeFirst();
        } else {
            this.size++;
        }
        ll.offer(e);
        return true;
    }

    public E poll() {
        if (size != 0) {
            size--;
        }
        return ll.poll();
    }

    public E peek() {
        return ll.peek();
    }

    @Override
    public void clear() {
        size = 0;
        ll.clear();
    }

    class LimitedQueueIterator implements Iterator<E> {

        private Iterator<E> i;

        public LimitedQueueIterator() {
            this.i = ll.iterator();
        }

        public void remove() {
            size--;
            i.remove();
        }

        public boolean hasNext() {
            return i.hasNext();
        }

        public E next() {
            return i.next();
        }
    }
}