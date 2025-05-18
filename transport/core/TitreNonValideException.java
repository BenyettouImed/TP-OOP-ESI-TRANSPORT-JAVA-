package transport.core;

import java.io.Serializable;

public class TitreNonValideException extends Exception implements Serializable {
    private static final long serialVersionUID = 1L;  // Version identifier

    public TitreNonValideException(String message) {
        super(message);
    }

    // Optionally add constructor with cause for better exception chaining
    public TitreNonValideException(String message, Throwable cause) {
        super(message, cause);
    }
}