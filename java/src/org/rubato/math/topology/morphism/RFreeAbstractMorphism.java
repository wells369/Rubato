package org.rubato.math.topology.morphism;

import org.rubato.math.topology.*;
import org.rubato.math.topology.morphism.*;

/**
 * Created by Justin on 4/17/2015.
 */
public abstract class RFreeAbstractMorphism extends TopologicalSpaceMorphism {

    public RFreeAbstractMorphism(int domDim, int codomDim) {
        super(RProperFreeTopologicalSpace.make(domDim), RProperFreeTopologicalSpace.make(codomDim));
    }


    public TopologicalSpaceElement map(TopologicalSpaceElement x)
            throws MappingException {
        if (getDomain().hasElement(x)) {
            double[] v;
            if (x instanceof RProperFreeElement) {
                v = ((RProperFreeElement) x).getValue();
            }
            else {
                v = new double[x.getLength()];
                for (int i = 0; i < x.getLength(); i++) {
                    v[i] = ((RElement) x.getComponent(i)).getValue();
                }
            }
            return RProperFreeElement.make(mapValue(v));
        }
        else {
            throw new MappingException("RFreeAbstractMorphism.map: ", x, this);
        }
    }


    /**
     * The low-level map method.
     * This must be implemented in subclasses.
     */
    public abstract double[] mapValue(double[] x);


    public TopologicalSpaceMorphism getRingMorphism() {
        return getIdentityMorphism(RRing.ring);
    }
}
