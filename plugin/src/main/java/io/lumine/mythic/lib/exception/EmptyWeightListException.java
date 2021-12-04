package io.lumine.mythic.lib.exception;

public class EmptyWeightListException extends RuntimeException {
    private static final long serialVersionUID = -6109415438533989367L;

    public EmptyWeightListException() {
        super("Couldn't select weighted object; Weighted List is empty.");
    }
}
