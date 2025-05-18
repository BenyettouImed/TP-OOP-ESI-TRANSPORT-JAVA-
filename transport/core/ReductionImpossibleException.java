package transport.core;

import java.io.Serializable;

public class ReductionImpossibleException extends Exception implements Serializable {
    private static final long serialVersionUID = 1L;

    public ReductionImpossibleException(String message) {
        super(message);
    }
}