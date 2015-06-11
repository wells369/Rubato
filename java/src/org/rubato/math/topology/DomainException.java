package org.rubato.math.topology;

import org.rubato.base.RubatoException;

/**
 * Created by Justin on 4/14/2015.
 */
public final class DomainException extends RubatoException {
    /**
     * Creates a DomainException.
     *
     * @param message  indicates the reason for the exception
     * @param expected the module (or element of that module) that was required
     * @param received the actual module (or element of that module)
     */
    public DomainException(String message, TopologicalSpace expected, TopologicalSpace received) {
        super(message);
        this.expected = expected;
        this.received = received;
    }


    /**
     * Creates a DomainException.
     * A message is generated from <code>expected</code> and <code>received</code>.
     *
     * @param expected the module (or element of that module) that was required
     * @param received the actual module (or element of that module)
     */
    public DomainException(TopologicalSpace expected, TopologicalSpace received) {
        this("Expected domain "+expected+", got "+received+".", expected, received);
    }


    /**
     * Returns the module that was required.
     */
    public TopologicalSpace getExpectedDomain() {
        return expected;
    }


    /**
     * Returns the actual module.
     */
    public TopologicalSpace getReceivedDomain() {
        return received;
    }


    private TopologicalSpace expected;
    private TopologicalSpace received;
}
