package org.example.Structures.Exceptions;


public class UnsupportedDataTypeException extends RuntimeException {
    public UnsupportedDataTypeException() {
        throw new UnsupportedOperationException();
    }

    public UnsupportedDataTypeException(String not_Comparable) {
        throw new UnsupportedOperationException(not_Comparable);
    }
}
