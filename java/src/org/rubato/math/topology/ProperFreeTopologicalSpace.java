package org.rubato.math.topology;

import org.rubato.math.topology.morphism.TopologicalSpaceMorphism;
import org.rubato.math.topology.morphism.TranslationMorphism;

/**
 * Created by Justin on 4/14/2015.
 */
public abstract class ProperFreeTopologicalSpace implements FreeTopologicalSpace {

    public ProperFreeTopologicalSpace(int dimension) {
        dimension = (dimension < 0)?0:dimension;
        this.dimension = dimension;
    }


    public final TopologicalSpaceMorphism getIdentityMorphism() {
        return TopologicalSpaceMorphism.getIdentityMorphism(this);
    }


    public final TopologicalSpaceMorphism getTranslation(TopologicalSpaceElement element) {
        return TranslationMorphism.make(this, element);
    }


    public final TopologicalSpaceMorphism getProjection(int index) {
        if (index < 0) { index = 0; }
        if (index > getDimension()-1) { index = getDimension()-1; }
        return _getProjection(index);
    }


    public final TopologicalSpaceMorphism getInjection(int index) {
        if (index < 0) { index = 0; }
        if (index > getDimension()-1) { index = getDimension()-1; }
        return _getInjection(index);
    }


    public final boolean isRing() {
        return false;
    }


    public int compareTo(TopologicalSpace object) {
        if (object instanceof FreeTopologicalSpace) {
            FreeTopologicalSpace m = (FreeTopologicalSpace)object;
            int c;
            if ((c = getRing().compareTo(m.getRing())) != 0) {
                return c;
            }
            else {
                return getDimension()-m.getDimension();
            }
        }
        else {
            return toString().compareTo(object.toString());
        }
    }


    public final int getDimension() {
        return dimension;
    }


    protected abstract TopologicalSpaceMorphism _getProjection(int index);

    protected abstract TopologicalSpaceMorphism _getInjection(int index);

    private final int dimension;
}
