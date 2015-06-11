package org.rubato.math.topology;

import java.util.List;

/**
 * Created by Justin on 4/17/2015.
 */
public interface RFreeTopologicalSpace extends FreeTopologicalSpace {

    public RFreeElement createElement(List<TopologicalSpaceElement> elements);

    public RFreeElement getZero();

    public RFreeElement getUnitElement(int i);
}
