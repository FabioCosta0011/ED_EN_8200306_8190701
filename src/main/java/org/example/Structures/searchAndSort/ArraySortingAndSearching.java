package org.example.Structures.searchAndSort;

public class ArraySortingAndSearching<T> {

    public static <T extends Comparable<? super T>> boolean linearSearch(T[] data, int min, int max, T target) {
        int index = min;
        boolean found = false;

        while (!found && index <= max) {
            if (data[index].compareTo(target) == 0) {
                found = true;
            }
            index++;
        }
        return found;
    }

    public static <T extends Comparable<? super T>> boolean binarySearch(T[] data, int min, int max, T target) {
        boolean found = false;
        int midpoint = (min + max) / 2;

        if (data[midpoint].compareTo(target) == 0) {
            found = true;
        } else if (data[midpoint].compareTo(target) > 0) {
            if (min <= midpoint - 1) {
                found = binarySearch(data, min, midpoint - 1, target);
            }
        } else if (midpoint + 1 <= max) {
            found = binarySearch(data, midpoint + 1, max, target);
        }
        return found;
    }

    public static <T extends Comparable<? super T>> void selectionSort(T[] data) {
        int min;
        T temp;

        for (int index = 0; index < data.length - 1; index++) {
            min = index;

            for (int scan = index + 1; scan < data.length; scan++) {
                if (data[scan].compareTo(data[min]) < 0) {
                    min = scan;
                }
            }

            temp = data[min];
            data[min] = data[index];
            data[index] = temp;
        }
    }

    public static <T extends Comparable<? super T>> void insertionSort(T[] data) {
        for (int index = 1; index < data.length; index++) {
            T key = data[index];
            int position = index;

            while (position > 0 && data[position - 1].compareTo(key) > 0) {
                data[position] = data[position - 1];
                position--;
            }

            data[position] = key;
        }
    }

    public static <T extends Comparable<? super T>> void bubbleSort(T[] data) {
        int position, scan;
        T temp;

        for (position = data.length - 1; position >= 0; position--) {
            for (scan = 0; scan <= position - 1; scan++) {
                if (data[scan].compareTo(data[scan + 1]) > 0) {
                    temp = data[scan];
                    data[scan] = data[scan + 1];
                    data[scan + 1] = temp;
                }
            }
        }
    }

    public static <T extends Comparable<? super T>> void quickSort(T[] data, int min, int max) {
        int indexOfPartition;

        if (max - min > 0) {
            indexOfPartition = findPartition(data, min, max);
            quickSort(data, min, indexOfPartition - 1);
            quickSort(data, indexOfPartition + 1, max);
        }
    }

    public static <T extends Comparable<? super T>> void mergeSort(T[] data, int min, int max) {
        T[] temp;
        int index1, left, right;

        if (min == max) {
            return;
        }

        int size = max - min + 1;
        int pivot = (min + max) / 2;
        temp = (T[]) (new Comparable[size]);

        mergeSort(data, min, pivot);
        mergeSort(data, pivot + 1, max);

        for (index1 = 0; index1 < size; index1++) {
            temp[index1] = data[min + index1];
        }

        left = 0;
        right = pivot - min + 1;

        for (index1 = 0; index1 < size; index1++) {
            if (right <= max - min) {
                if (left <= pivot - min) {
                    if (temp[left].compareTo(temp[right]) > 0) {
                        data[index1 + min] = temp[right++];
                    } else {
                        data[index1 + min] = temp[left++];
                    }
                } else {
                    data[index1 + min] = temp[right++];
                }
            } else {
                data[index1 + min] = temp[left++];
            }
        }
    }

    private static <T extends Comparable<? super T>> int findPartition(T[] data, int min, int max) {
        int left, right;
        T temp, partitionElement;
        int middle = (min + max) / 2;

        partitionElement = data[middle];
        left = min;
        right = max;

        while (left < right) {
            while (data[left].compareTo(partitionElement) < 0) {
                left++;
            }

            while (data[right].compareTo(partitionElement) > 0) {
                right--;
            }

            if (left < right) {
                temp = data[left];
                data[left] = data[right];
                data[right] = temp;
            }
        }

        temp = data[min];
        data[min] = data[right];
        data[right] = temp;

        return right;
    }
}
