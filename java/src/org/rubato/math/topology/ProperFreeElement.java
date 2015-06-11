package org.rubato.math.topology;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * Created by Justin on 4/14/2015.
 */
public abstract class ProperFreeElement implements FreeElement {

    public int compareTo(TopologicalSpaceElement object) {
        return getTopologicalSpace().compareTo(object.getTopologicalSpace());
    }


    public Iterator<RingElement> iterator() {
        LinkedList<RingElement> elements = new LinkedList<RingElement>();
        for (int i = 0; i < getLength(); i++) {
            elements.add(getRingElement(i));
        }
        return elements.iterator();
    }


    public abstract ProperFreeElement clone();
}
