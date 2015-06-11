package org.rubato.math.topology;

import java.util.Iterator;

/**
 * Created by Justin on 4/14/2015.
 */
public interface FreeElement
        extends TopologicalSpaceElement, Iterable<RingElement> {
    /**
     * Returns the <code>i</code>-th component of this free element.
     */
    public abstract TopologicalSpaceElement getComponent(int i);

    /**
     * Returns the <code>i</code>-th ring component of this free element.
     */
    public abstract RingElement getRingElement(int i);

    /**
     * Returns the length of this free element.
     */
    public abstract int getLength();

    /**
     * Returns the componentwise product of this module element and <code>element</code>.
     *
     * @throws DomainException if <code>element</code> is not in domain
     */
    public TopologicalSpaceElement productCW(TopologicalSpaceElement element)
            throws DomainException;

    /**
     * Multiply this module element componentwise with <code>element</code>.
     * This is a destructive operation.
     *
     * @throws DomainException if <code>element</code> is not in domain
     */
    public void multiplyCW(TopologicalSpaceElement element)
            throws DomainException;

    /**
     * Returns an iterator to the factors of the element.
     */
    public abstract Iterator<RingElement> iterator();

    /**
     * Returns this free element resized to length <code>n</code>.
     * If the new length <code>n</code> is greater than the old length,
     * the new values are filled with the zero of the underlying ring.
     * If the new length <code>n</code> is less than the old length,
     * the vector of values is simply truncated.
     */
    public abstract FreeElement resize(int n);
}
