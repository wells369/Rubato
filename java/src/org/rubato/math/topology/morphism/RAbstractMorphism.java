package org.rubato.math.topology.morphism;

import org.rubato.math.topology.TopologicalSpaceElement;
import org.rubato.math.topology.RElement;
import org.rubato.math.topology.RRing;
import org.rubato.math.topology.morphism.*;
import org.rubato.math.topology.morphism.TopologicalSpaceMorphism;

/**
 * Created by Justin on 4/17/2015.
 */
public abstract class RAbstractMorphism extends TopologicalSpaceMorphism {

    public RAbstractMorphism() {
        super(RRing.ring, RRing.ring);
    }


    public final TopologicalSpaceElement map(TopologicalSpaceElement x)
            throws MappingException {
        if (getDomain().hasElement(x)) {
            double v = ((RElement) x.getComponent(0)).getValue();
            return new RElement(mapValue(v));
        }
        else {
            throw new MappingException("RAbstractMorphism.map: ", x, this);
        }
    }


    /**
     * The low-level map method.
     * This must be implemented in subclasses.
     */
    public abstract double mapValue(double x);


    public TopologicalSpaceMorphism getRingMorphism() {
        return getIdentityMorphism(RRing.ring);
    }
}
