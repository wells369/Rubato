package org.rubato.math.topology;

/**
 * Created by Justin on 4/14/2015.
 */
public final class DivisionException extends Exception {

    public DivisionException(RingElement divident, RingElement divisor) {
        super("Cannot divide " + divident + " by " + divisor);
    }
}
