package org.rubato.math.topology;

/**
 * The interface for free modules over polynomials.
 * @see org.rubato.math.module.PolynomialFreeElement
 *
 * @author Gérard Milmeister
 */
public interface PolynomialFreeTopologicalSpace extends FreeTopologicalSpace {

    /**
     * Returns the ring of the coefficients of the polynomials.
     */
    public Ring getCoefficientRing();

    /*
     * Returns the length of the space
     */
    //public int getLength();

    /**
     * Returns the indeterminate of this polynomial module.
     */
    public String getIndeterminate();
}
