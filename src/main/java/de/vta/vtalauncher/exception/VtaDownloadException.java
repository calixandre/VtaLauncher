package de.vta.vtalauncher.exception;

public class VtaDownloadException extends VtaLogicException {
    public VtaDownloadException(String message) {
        super(message);
    }

    public VtaDownloadException(String message, Throwable cause) {
        super(message, cause);
    }
}
