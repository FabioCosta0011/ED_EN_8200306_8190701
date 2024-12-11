package org.example.Structures.Implementations;



import org.example.Structures.Exceptions.ElementNotFoundException;
import org.example.Structures.Exceptions.EmptyCollectionException;
import org.example.Structures.Interfaces.ListADT;

import java.util.Iterator;

/**
 * This class implements a basic generic array-based list.
 * It provides a foundational structure for a dynamic array list
 * that can be extended to more specific implementations.
 *
 * @param <T> the type of elements stored in this list
 */
public class ArrayList<T> implements ListADT<T> {

    // Default initial capacity of the array
    protected final int DEFAULT_CAPACITY = 10;

    // Constant representing a "not found" result
    private final int NOT_FOUND = -1;

    // Number of elements in the array (logical size)
    protected int rear;

    // The array where elements are stored
    protected T[] list;

    /**
     * Creates an empty list with the default capacity.
     */
    public ArrayList() {
        rear = 0;
        list = (T[]) (new Object[DEFAULT_CAPACITY]);
    }

    /**
     * Creates an empty list with a specified initial capacity.
     *
     * @param initialCapacity the initial capacity of the array
     */
    public ArrayList(int initialCapacity) {
        rear = 0;
        list = (T[]) (new Object[initialCapacity]);
    }

    /**
     * Removes and returns the first element from the list.
     * Throws an EmptyCollectionException if the list is empty.
     *
     * @return the first element in the list
     * @throws EmptyCollectionException if the list is empty
     */
    @Override
    public T removeFirst() throws EmptyCollectionException {
        if (isEmpty()) {
            throw new EmptyCollectionException("list");
        }

        T result = list[0];
        rear--;

        // shift the elements
        for (int scan = 0; scan < rear; scan++) {
            list[scan] = list[scan + 1];
        }

        list[rear] = null;// Clear the last element
        return result;
    }

    /**
     * Removes and returns the last element from the list.
     * Throws an EmptyCollectionException if the list is empty.
     *
     * @return the last element in the list
     * @throws EmptyCollectionException if the list is empty
     */
    @Override
    public T removeLast() throws EmptyCollectionException {
        T result;

        if (isEmpty()) {
            throw new EmptyCollectionException("list");
        }

        rear--;
        result = list[rear];
        list[rear] = null; // Clear the last element
        return result;
    }

    /**
     * Removes and returns the specified element from the list.
     * Throws an ElementNotFoundException if the element is not found.
     *
     * @param element the element to be removed
     * @return the removed element
     * @throws ElementNotFoundException if the element is not found
     */
    @Override
    public T remove(T element) {
        int index = find(element);

        if (index == NOT_FOUND) {
            throw new ElementNotFoundException("list");
        }

        T result = list[index];
        rear--;

        // Shift the elements to the left from the removed index
        for (int scan = index; scan < rear; scan++) {
            list[scan] = list[scan + 1];
        }

        list[rear] = null; // Clear the last element
        return result;
    }

    /**
     * Returns the first element in the list without removing it.
     * Throws an EmptyCollectionException if the list is empty.
     *
     * @return the first element in the list
     * @throws EmptyCollectionException if the list is empty
     */
    @Override
    public T first() throws EmptyCollectionException {
        if (isEmpty()) {
            throw new EmptyCollectionException("list");
        }
        return list[0];
    }

    /**
     * Returns the last element in the list without removing it.
     * Throws an EmptyCollectionException if the list is empty.
     *
     * @return the last element in the list
     * @throws EmptyCollectionException if the list is empty
     */
    @Override
    public T last() throws EmptyCollectionException {
        if (isEmpty()) {
            throw new EmptyCollectionException("list");
        }
        return list[rear - 1];
    }

    /**
     * Checks if the list contains the specified element.
     *
     * @param target the element to search for
     * @return true if the list contains the element, false otherwise
     */
    @Override
    public boolean contains(T target) {
        return (find(target) != NOT_FOUND);
    }

    /**
     * Finds and returns the index of the specified element.
     *
     * @param target the element to find
     * @return the index of the element, or NOT_FOUND if the element is not found
     */
    public int find(T target) {
        int scan = 0, result = NOT_FOUND;
        boolean found = false;

        if (!isEmpty())
            while (!found && scan < rear)
                if (target.equals(list[scan]))
                    found = true;
                else
                    scan++;

        if (found)
            result = scan;

        return result;
    }

    /**
     * Returns true if the list is empty.
     *
     * @return true if the list has no elements, false otherwise
     */
    @Override
    public boolean isEmpty() {
        return (rear == 0);
    }

    /**
     * Returns the number of elements in the list.
     *
     * @return the number of elements in the list
     */
    @Override
    public int size() {
        return rear;
    }

    /**
     * Returns an iterator over the elements in this list.
     *
     * @return an iterator for this list
     */
    @Override
    public Iterator<T> iterator() {
        return new ArrayIterator<T>(list, rear);
    }

    /**
     * Returns a string representation of the list.
     * Each element is separated by a newline.
     *
     * @return a string representation of the list
     */
    @Override
    public String toString() {
        String result = "";

        for (int scan = 0; scan < rear; scan++)
            result = result + list[scan].toString() + "\n";

        return result;
    }

    /**
     * Doubles the capacity of the list when more space is needed.
     */
    protected void expandCapacity() {
        T[] larger = (T[]) (new Object[list.length * 2]);

        for (int scan = 0; scan < list.length; scan++)
            larger[scan] = list[scan];

        list = larger;
    }
}
