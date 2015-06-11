package org.rubato.math.topology.morphism;

import org.rubato.base.RubatoException;

/**
 * Created by Justin on 4/15/2015.
 */
public final class CompositionException extends RubatoException {

    /**
     * Creates a CompositionExecption with the specified message.
     */
    public CompositionException(String msg) {
        super(msg);
    }


    /**
     * Creates a CompositionException with the specified message,
     * with a predefined message generated from <code>f</code> and
     * <code>g</code>, which are the components of the composition, appended.
     */
    public CompositionException(String msg, TopologicalSpaceMorphism f, TopologicalSpaceMorphism g) {
        super(msg+"Failed to compose "+f+" and "+g);
    }


    /**
     * Creates a CompositionException with
     * predefined message generated from <code>f</code> and
     * <code>g</code>, which are the components of the composition, appended.
     */
    public CompositionException(TopologicalSpaceMorphism f, TopologicalSpaceMorphism g) {
        this("", f, g);
    }
}
