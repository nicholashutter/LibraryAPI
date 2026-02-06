package com.library.exceptions;

public class ApplicationException extends Exception {
    public ApplicationException(Errors err) {
        super("The following error occurred: " + err.toString());
    }
}
