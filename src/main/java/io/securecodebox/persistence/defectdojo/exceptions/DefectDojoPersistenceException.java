package io.securecodebox.persistence.defectdojo.exceptions;

public class DefectDojoPersistenceException extends RuntimeException {
    public DefectDojoPersistenceException(String message) {
        super(message);
    }

    public DefectDojoPersistenceException(String message, Throwable cause) {
        super(message, cause);
    }
}
