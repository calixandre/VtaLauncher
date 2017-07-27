package de.vta.vtalauncher.exception;

public class VtaAuthenticationException extends VtaLogicException {
    private final Reason reason;

    public VtaAuthenticationException(Reason reason) {
        super(reason.name());
        this.reason = reason;
    }
VtaAuthenticationException(Reason reason, Throwable cause) {
        super(reason.name(), cause);
        this.reason = reason;
    }

    public Reason getReason() {
        return reason;
    }

    public enum Reason {
        DID_NOT_BUY_MINECRAFT,
        USER_CREDENTIALS_ARE_WRONG
    }
}

