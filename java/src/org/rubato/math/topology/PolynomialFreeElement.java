package org.rubato.math.topology;

/**
 * The interface for elements in a free module of polynomials.
 * @see org.rubato.math.module.PolynomialFreeModule
 *
 * @author Gérard Milmeister
 */
public interface PolynomialFreeElement extends FreeElement {

    /**
     * Returns the ring of the coefficients of the polynomial.
     */
    public Ring getCoefficientRing();

    /**
     * Returns the indeterminate of the polynomial.
     */
    public String getIndeterminate();
}
