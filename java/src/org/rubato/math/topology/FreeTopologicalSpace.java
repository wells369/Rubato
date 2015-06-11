package org.rubato.math.topology;

import org.rubato.math.topology.morphism.TopologicalSpaceMorphism;

/**
 * Created by Justin on 4/14/2015.
 */

public interface FreeTopologicalSpace extends TopologicalSpace {
    /**
     * Returns true if this free module is a vector space.
     */
    public boolean isVectorspace();

    /*
     * Returns the length of the space
     */
    //public int getLength();

    /**
     * Returns the unit vector with 1 at position <code>i</code>.
     */
    public TopologicalSpaceElement getUnitElement(int i);

    /**
     * Returns a module morphism that projects the free module
     * at the component <code>index</code>.
     * @param index the number of the component to project to,
     *        the index will be clamped between 0 and the dimension-1 of
     *        the free module
     */
    public TopologicalSpaceMorphism getProjection(int index);

    /**
     * Returns a module morphism that injects a ring into the free module
     * at the component <code>index</code>.
     * @param index the number of the component to project to,
     *        the index will be clamped between 0 and the dimension-1 of
     *        the free module
     */
    public TopologicalSpaceMorphism getInjection(int index);
}
