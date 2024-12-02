package org.example.Structures.Implementations;



import org.example.Structures.Interfaces.QueueADT;

import java.util.Iterator;
import java.util.NoSuchElementException;


public class LinkedQueue<T> implements QueueADT<T>, Iterable<T>{

    private Node<T> front;
    private Node<T> rear;
    private int count;

    public LinkedQueue() {
        count = 0;
        front = rear = null;
    }

    @Override
    public void enqueue(T element) {
        Node<T> node = new Node<T>(element);

        if (isEmpty())
            front = node;
        else
            rear.setNext (node);

        rear = node;
        count++;
    }

    @Override
    public T dequeue() {
        if (this.count == 0) {
            return null;
        }

        T oldFirst = this.front.getData();
        this.front = this.front.getNext();
        this.count--;

        return oldFirst;
    }

    @Override
    public T first() {
        return this.front.getData();
    }

    @Override
    public boolean isEmpty() {
        return (this.count == 0);
    }

    @Override
    public int size() {
        return this.count;
    }

    @Override
    public String toString() {
        String tmp = "";

        Node<T> n = this.front;

        while (n.getNext() != null) {
            tmp += n.getData() + "; ";
            n = n.getNext();
        }
        tmp += n.getData();

        return tmp;
    }

    public Iterator<T> iterator()  {
        return new LinkedIterator<T>();
    }

    //
    private class LinkedIterator<T> implements Iterator<T> {
        private Node current = front;

        public boolean hasNext()  { return current != null;                     }

        public T next() {
            if (!hasNext()) throw new NoSuchElementException();
            T item =(T) current.getData();
            current =current.getNext();
            return item;
        }
    }

    public void clear() {
        front = null;
        rear = null;
        count = 0;
    }

}
