package org.rubato.math.topology;

import java.io.Serializable;
import java.util.List;

import org.rubato.math.topology.morphism.TopologicalSpaceMorphism;
import org.rubato.xml.XMLInputOutput;

/**
 * Created by Justin on 4/14/2015.
 */
public interface TopologicalSpace
        extends Serializable, Comparable<TopologicalSpace>,
        XMLInputOutput<TopologicalSpace> {

    /**
     * Returns the zero element in this module.
     */
    public TopologicalSpaceElement getZero();

    /*
     * Returns the length of this
     */

    /**
     * Returns the identity morphism in this module.
     */
    public TopologicalSpaceMorphism getIdentityMorphism();

    /**
     * Returns the dimension of this module.
     */
    public int getDimension();

    /**
     * Returns the null-module corresponding to this module.
     */
    public TopologicalSpace getNullTopologicalSpace();

    /**
     * Returns true iff this is a null-module.
     */
    public boolean isNullTopologicalSpace();

    /**
     * Returns true iff this module is a ring.
     */
    public boolean isRing();

    /**
     * Returns the underlying ring of this module.
     */
    public Ring getRing();

    /**
     * Returns the <code>i</code>-th component module.
     */
    //public TopologicalSpace getComponentTopologicalSpace(int i);

    /**
     * Returns true iff <code>element</code> is an element of this module.
     */
    public boolean hasElement(TopologicalSpaceElement element);

    /**
     * Returns a morphism that translates by <code>element</code>.
     */
    public TopologicalSpaceMorphism getTranslation(TopologicalSpaceElement element);

    /**
     * Casts <code>element</code> to an element in this module if possible.
     * @return null if cast is not possible
     */
    public TopologicalSpaceElement cast(TopologicalSpaceElement element);

    /**
     * Creates an element in this module from a list of module elements.
     * @return null if no element in this module can be created from
     *         the arguments.
     */
    public TopologicalSpaceElement createElement(List<TopologicalSpaceElement> elements);

    /**
     * Creates an element in this module from a string representation.
     * @return null if the string is in the wrong format
     */
    public TopologicalSpaceElement parseString(String string);

    /**
     * Returns the hash code for this module.
     */
    public int hashCode();

    /**
     * Returns true iff this module is equals to <code>object</code>.
     */
    public boolean equals(Object object);

    /**
     * Compares this module with <code>object</code>.
     */
    public int compareTo(TopologicalSpace object);

    /**
     * Returns a human readable string representation of this module.
     * The representation is not meant to be parseable.
     */
    public String toString();

    /**
     * Returns a human readable string representation of this module.
     * The representation is not meant to be parseable.
     * The string should be a short representation, possibly using
     * Unicode characters.
     */
    public String toVisualString();
}
