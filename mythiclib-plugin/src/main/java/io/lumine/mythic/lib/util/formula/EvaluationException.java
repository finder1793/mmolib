package io.lumine.mythic.lib.util.formula;

import io.lumine.mythic.lib.util.annotation.NotUsed;

@NotUsed
@Deprecated
public class EvaluationException extends RuntimeException {
    public EvaluationException(Throwable cause) {
        super(cause);
    }

    public EvaluationException(String message, Throwable cause) {
        super(message, cause);
    }

    public EvaluationException(String message) {
        super(message);
    }
}
