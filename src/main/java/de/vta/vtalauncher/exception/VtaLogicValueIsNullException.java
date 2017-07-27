package de.vta.vtalauncher.exception;

public class VtaLogicValueIsNullException extends VtaLogicException {
    public VtaLogicValueIsNullException(String message) {
        super(message);
    }

    public VtaLogicValueIsNullException(Throwable cause) {
        super(cause);
    }

    public VtaLogicValueIsNullException(String message, Throwable cause) {
        super(message, cause);
    }
}
