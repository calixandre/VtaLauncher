package de.vta.vtalauncher.exception;

public class VtaLogicException extends vtaException {
    public VtaLogicException(String message) {
        super(message);
    }

    public VtaLogicException(Throwable cause) {
        super(cause);
    }

    public VtaLogicException(String message, Throwable cause) {
        super(message, cause);
    }
}
