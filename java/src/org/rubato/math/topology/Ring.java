package org.rubato.math.topology;

import org.rubato.math.topology.morphism.TopologicalSpaceMorphism;
import org.rubato.math.topology.morphism.TranslationMorphism;

/**
 * Created by Justin on 4/14/2015.
 */
public abstract class Ring implements FreeTopologicalSpace {
    /**
     * Returns the zero element of this ring.
     */
    public abstract RingElement getZero();

    /**
     * Returns the unit element of this ring.
     */
    public abstract RingElement getOne();

    /**
     * Returns the unit vector with 1 at position <code>i</code>.
     * In the case of rings, this is simple the unit.
     */
    public RingElement getUnitElement(int i) {
        return getOne();
    }


    public TopologicalSpaceMorphism getProjection(int index) {
        return getIdentityMorphism();
    }


    public TopologicalSpaceMorphism getInjection(int index) {
        return getIdentityMorphism();
    }

    /**
     * Returns true if this ring is a field.
     */
    public abstract boolean isField();

    /**
     * Returns true if this module is a ring.
     */
    public boolean isRing() {
        return true;
    }

    /**
     * Returns the corresponding free module of dimension <code>dim</code>.
     */
    public abstract FreeTopologicalSpace getFreeTopologicalSpace(int dimension);

    /**
     * Here, the dimension of a ring as a module is 1.
     */
    public int getDimension() {
        return 1;
    }

    /**
     * A ring has just one component module: itself.
     */
    public TopologicalSpace getComponentModule(int i) {
        return this;
    }

    /**
     * Here, a ring is not a null-module.
     */
    public boolean isNullTopologicalSpace() {
        return false;
    }

    /**
     * The underlying ring of a ring as a module is itself.
     */
    public Ring getRing() {
        return this;
    }

    public abstract TopologicalSpaceElement parseString(String s);

    /**
     * Returns a morphism that translates by <code>element</code>.
     */

    public TopologicalSpaceMorphism getTranslation(TopologicalSpaceElement element) {
        return TranslationMorphism.make(this, element);
    }

    public int compareTo(TopologicalSpace object) {
        return toString().compareTo(object.toString());
    }
}
