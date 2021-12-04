package io.lumine.mythic.lib.exception;

public class InvalidWeightException extends RuntimeException {
    private static final long serialVersionUID = 6918775136447677769L;

    public InvalidWeightException() {
        super("Invalid weight! Weights must be between 1 and " + Integer.MAX_VALUE);
    }
}
