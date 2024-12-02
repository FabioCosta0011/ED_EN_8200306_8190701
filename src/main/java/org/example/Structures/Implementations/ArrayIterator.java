package org.example.Structures.Implementations;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * An ArrayIterator provides an iterator for traversing the elements of a generic array.
 * It implements the Iterator interface and allows traversal through the elements of the array
 * in a sequential manner, without modifying the underlying collection.
 *
 * @param <T> the type of elements returned by this iterator
 */
public class ArrayIterator<T> implements Iterator {

    // Number of elements in the array
    private int count;

    // Current index of the iteration
    private int current;

    // The array of items to be iterated over
    private T[] items;

    /**
     * Constructs an ArrayIterator for the specified array and size.
     *
     * @param collection the array of items to iterate over
     * @param size the number of elements in the array to iterate
     */
    public ArrayIterator(T[] collection, int size) {
        items = collection;
        count = size;
        current = 0;
    }

    /**
     * Returns true if the iteration has more elements.
     * In other words, returns true if next() would return an element
     * rather than throwing an exception.
     *
     * @return true if there are more elements to iterate, false otherwise
     */
    @Override
    public boolean hasNext() {
        return (current < count);
    }

    /**
     * Returns the next element in the iteration.
     * Throws NoSuchElementException if the iteration has no more elements.
     *
     * @return the next element in the array
     * @throws NoSuchElementException if there are no more elements to return
     */
    @Override
    public T next() {
        if (! hasNext())
            throw new NoSuchElementException();

        current++;
        return items[current - 1];

    }

    /**
     * Removes the last element returned by this iterator (optional operation).
     * This implementation throws UnsupportedOperationException, as removal
     * is not supported by this iterator.
     *
     * @throws UnsupportedOperationException as the remove operation is not supported
     */
    @Override
    public void remove() throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }
}

