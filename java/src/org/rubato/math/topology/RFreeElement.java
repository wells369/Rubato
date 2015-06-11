package org.rubato.math.topology;

/**
 * Created by Justin on 4/17/2015.
 */
public interface RFreeElement extends FreeElement {

    public RFreeElement sum(TopologicalSpaceElement element)
            throws DomainException;

    public RFreeElement difference(TopologicalSpaceElement element)
            throws DomainException;

    public RFreeElement negated();

    public RFreeElement scaled(RingElement element)
            throws DomainException;

    public RFreeElement resize(int n);

    public RElement getComponent(int i);

    public RElement getRingElement(int i);

    public RFreeElement clone();
}
