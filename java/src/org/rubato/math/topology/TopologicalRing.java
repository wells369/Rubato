package org.rubato.math.topology;

/**
 * Created by Justin on 4/14/2015.
 */
public abstract class TopologicalRing extends Ring {


    public int compareTo(TopologicalSpace object) {
        if (object instanceof TopologicalRing) {
            return compareTo((TopologicalRing)object);
        } else {
            return super.compareTo(object);
        }
    }

    /**
     * Compares this number ring with the number ring <code>r</code>.
     */
    public int compareTo(TopologicalRing r) {
        return getNumberRingOrder()-r.getNumberRingOrder();
    }

    /**
     * Returns a number that indicates the position in
     * the order of rings.
     */
    protected abstract int getNumberRingOrder();
}

