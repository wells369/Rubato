package org.rubato.math.topology;

/**
 * The interface for free modules over product rings.
 * @see org.rubato.math.module.ProductFreeElement
 *
 * @author Gérard Milmeister
 */
public interface ProductFreeTopologicalSpace extends FreeTopologicalSpace {

    public int getFactorCount();

    public Ring[] getFactors();

    public Ring getFactor(int i);
}
