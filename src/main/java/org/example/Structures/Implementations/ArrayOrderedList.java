package org.example.Structures.Implementations;


import org.example.Structures.Exceptions.NonComparableElementException;
import org.example.Structures.Interfaces.OrderedListADT;

/**
 * ArrayOrderedList is an implementation of the OrderedListADT interface using an array.
 * Elements are stored in a sorted order based on their natural ordering (Comparable).
 *
 * @param <T> the type of elements stored in the list; must implement Comparable
 */
public class ArrayOrderedList<T> extends ArrayList<T> implements OrderedListADT<T> {

    /**
     * Creates an empty ordered list with the default initial capacity.
     */
    public ArrayOrderedList() {
        super();
    }

    /**
     * Creates an empty ordered list with the specified initial capacity.
     *
     * @param initialCapacity the initial size of the underlying array
     */
    public ArrayOrderedList (int initialCapacity) {
        super(initialCapacity);
    }

    /**
     * Adds the specified element to the list, maintaining the elements in sorted order.
     * If the list is full, it will automatically expand its capacity.
     *
     * @param element the element to be added to the ordered list
     * @throws ClassCastException if the element does not implement Comparable
     */
    @Override
    public void add(T element) {
        // Expand capacity if the list is full
        if (size() == list.length) {
            expandCapacity();
        }

        // Cast the element too Comparable to enable sorting
        Comparable temp;

        if (element instanceof Comparable) {
            temp = (Comparable) element;
        } else {
            throw new NonComparableElementException("array ordered list");
        }

        // Find the correct position for the element by comparing it with existing elements
        int scan = 0;
        while (scan < rear && temp.compareTo(list[scan]) > 0) {
            scan++;
        }

        // Shift elements to the right to make space for the new element
        for (int scan2=rear; scan2 > scan; scan2--) {
            list[scan2] = list[scan2 - 1];
        }

        // Insert the element in its sorted position
        list[scan] = element;
        rear++;
    }
}
