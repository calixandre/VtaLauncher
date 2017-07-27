package de.vta.vtalauncher.exception;

public class VtaException extends Exception {
    public VtaException(String message) {
        super(message);
    }

    public VtaException(Throwable cause) {
        super(cause);
    }

    public VtaException(String message, Throwable cause) {
        super(message, cause);
    }
}
