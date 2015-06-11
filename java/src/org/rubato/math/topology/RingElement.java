package org.rubato.math.topology;

import java.util.Collections;
import java.util.Iterator;

/**
 * Created by Justin on 4/14/2015.
 */
public abstract class RingElement implements FreeElement {

    /**
     * Returns true if this ring element is one.
     */
    public abstract boolean isOne();


    public RingElement sum(RingElement element)
            throws DomainException {
        return (RingElement)sum((TopologicalSpaceElement)element);
    }


    public RingElement difference(RingElement element)
            throws DomainException {
        return (RingElement)difference((TopologicalSpaceElement)element);
    }

    /**
     * Returns the product of this ring element with <code>element</code>.
     *
     * @throws DomainException if <code>element</code> is not in domain
     */
    public abstract RingElement product(RingElement element)
            throws DomainException;


    /**
     * Multiplies this ring element with <code>element</code>.
     *
     * @throws DomainException if <code>element</code> is not in domain
     */
    public abstract void multiply(RingElement element)
            throws DomainException;


    public TopologicalSpaceElement productCW(TopologicalSpaceElement element)
            throws DomainException {
        if (element instanceof RingElement) {
            return product((RingElement)element);
        }
        else {
            throw new DomainException(RRing.ring, element.getTopologicalSpace());
        }
    }


    public void multiplyCW(TopologicalSpaceElement element)
            throws DomainException {
        if (element instanceof RingElement) {
            multiply((RingElement)element);
        }
        else {
            throw new DomainException(RRing.ring, element.getTopologicalSpace());
        }
    }


    /**
     * Returns true if this ring element is invertible.
     */
    public abstract boolean isInvertible();


    /**
     * Returns the inverse of this ring element, if it has an inverse.
     */
    public abstract RingElement inverse();


    /**
     * Inverts this ring element, if it has an inverse.
     */
    public abstract void invert();


    /**
     * Returns the solution <i>x</i> of
     * <code>element</code>*<i>x</i> = <code>this</code>,
     * if it exists, otherwise a DivisionException is thrown.
     */
    public abstract RingElement quotient(RingElement element)
            throws DomainException, DivisionException;


    /**
     * Replaces <code>this</code> by the solution <i>x</i> of
     * <code>element</code>*<i>x</i> = <code>this</code>, if it exists,
     * otherwise a DivisionException is thrown.
     */
    public abstract void divide(RingElement element)
            throws DomainException, DivisionException;


    /**
     * Return true iff the solution <i>x</i> of
     * <code>this</code>*<i>x</i> = <code>element</code>
     * exists.
     */
    public abstract boolean divides(RingElement element);


    /**
     * Raises this ring element to the power <code>n</code>.
     */
    public RingElement power(int n) {
        if (n == 0) {
            return getRing().getOne();
        }

        RingElement factor = this.clone();

        if (n < 0) {
            factor.invert();
            n = -n;
        }

        // Finding leading bit in the exponent n
        int bpos = 31; // bits per int
        while ((n & (1 << bpos)) == 0) {
            bpos--;
        }

        RingElement result = getRing().getOne();
        try {
            while (bpos >= 0) {
                result = result.product(result);
                if ((n & (1 << bpos)) != 0) {
                    result = result.product(factor);
                }
                bpos--;
            }
        }
        catch (DomainException e) {}

        return result;
    }


    /**
     * Returns the length of this ring element.
     * @return always 1
     */
    public int getLength() {
        return 1;
    }


    public TopologicalSpaceElement getComponent(int i) {
        return this;
    }


    public RingElement getRingElement(int i) {
        return this;
    }


    public Iterator<RingElement> iterator() {
        return Collections.singleton(this).iterator();
    }


    /**
     * Returns the ring this element is a member of.
     */
    public Ring getRing() {
        return (Ring)getTopologicalSpace();
    }


    public int compareTo(TopologicalSpaceElement object) {
        return getTopologicalSpace().compareTo(object.getTopologicalSpace());
    }


    public abstract String stringRep(boolean ... parens);


    public abstract RingElement clone();
}
