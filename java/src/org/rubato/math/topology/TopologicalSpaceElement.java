package org.rubato.math.topology;

import org.rubato.xml.XMLInputOutput;

import java.io.Serializable;

/**
 * Created by Justin on 4/14/2015.
 */
public interface TopologicalSpaceElement
        extends Cloneable, Serializable, Comparable<TopologicalSpaceElement>,
        XMLInputOutput<TopologicalSpaceElement> {

    /**
     * Returns true iff this element is zero.
     */
    public boolean isZero();

    /**
     * Returns the product of this element with <code>element</code>.
     *
     * @throws org.rubato.math.module.DomainException if <code>element</code> is not in domain
     */
    public TopologicalSpaceElement scaled(RingElement element)
            throws DomainException;

    /**
     * Multiplies this element with <code>element</code>.
     * This is a destructive operation.
     *
     * @throws DomainException if <code>element</code> is not in domain
     */
    public void scale(RingElement element)
            throws DomainException;

    /**
     * Returns the length of the element.
     */
    public int getLength();

    /**
     * Returns the value of the element.
     */
    //public double getValue();

    /**
     * Returns the <code>i</code>-th component element.
     */
    public TopologicalSpaceElement getComponent(int i);

    /**
     * Returns the sum of this module element and <code>element</code>.
     *
     * @throws DomainException if <code>element</code> is not in domain
     */
    public TopologicalSpaceElement sum(TopologicalSpaceElement element)
            throws DomainException;

    /**
     * Adds <code>element</code> to this module element.
     * This is a destructive operation.
     *
     * @throws DomainException if <code>element</code> is not in domain
     */
    public void add(TopologicalSpaceElement element)
            throws DomainException;

    /**
     * Returns the difference of this module element and <code>element</code>.
     *
     * @throws DomainException if <code>element</code> is not in domain
     */
    public TopologicalSpaceElement difference(TopologicalSpaceElement element)
            throws DomainException;

    /**
     * Subtracts <code>element</code> from this module element.
     * This is a destructive operation.
     *
     * @throws DomainException if <code>element</code> is not in domain
     */
    public void subtract(TopologicalSpaceElement element)
            throws DomainException;

    /**
     * Returns the negative of this module element.
     */
    public TopologicalSpaceElement negated();

    /**
     * Negate this module element.
     * This is a destructive operation.
     */
    public void negate();

    /**
     * Fold <code>elements</code> assuming they are of this same type.
     */
    public double[] fold(TopologicalSpaceElement[] elements);

    /**
     * Returns the module that this module element is an element of.
     */
    public TopologicalSpace getTopologicalSpace();

    /**
     * Tries to cast this element to an element in the given module.
     * @return a new module element in the required module
     *         and null if the cast cannot be performed.
     */
    public TopologicalSpaceElement cast(TopologicalSpace topologicalSpace);

    /**
     * Returns a string representation of this module element.
     * The representation is meant to be parseable.
     * If the argument parens is present then the the representation
     * is parenthesized if necessary.
     */
    public String stringRep(boolean ... parens);

    /**
     * Returns a human readable string representation of this module element.
     * The representation is not meant to be parseable.
     */
    public String toString();

    /**
     * Returns true iff this element is equal to <code>object</code>.
     */
    public boolean equals(Object object);

    /**
     * Compares this module element with <code>object</code>.
     */
    public int compareTo(TopologicalSpaceElement object);

    /**
     * Returns a deep copy of this module element.
     */
    public TopologicalSpaceElement clone();

    /**
     * Returns the hash code for this module element.
     */
    public int hashCode();
}
