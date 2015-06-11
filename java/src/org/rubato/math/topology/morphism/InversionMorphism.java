package org.rubato.math.topology.morphism;

import org.rubato.math.topology.TopologicalSpace;
import org.rubato.math.topology.RRing;
import org.rubato.math.topology.TopologicalSpaceElement;
import org.rubato.xml.XMLReader;
import org.rubato.xml.XMLWriter;
import org.w3c.dom.Element;

/**
 * Created by Justin on 6/9/2015.
 */
public class InversionMorphism extends TopologicalSpaceMorphism {


    public TopologicalSpaceMorphism getRingMorphism() {
        return RRing.ring.getIdentityMorphism();
    }

    public TopologicalSpaceElement map(TopologicalSpaceElement x)
            throws MappingException {
        return null;
    }

    public boolean equals(Object object) {
        if(object instanceof CartesianProductMorphism) {

        }
        return false;
    }

    public String toString() {
        return "";
    }

    public String getElementTypeName() { return "InversionMorphism"; }

    public void toXML(XMLWriter writer) {
        throw new UnsupportedOperationException("Not implemented");
    }


    public TopologicalSpaceMorphism fromXML(XMLReader reader, Element element) {
        throw new UnsupportedOperationException("Not implemented");
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public InversionMorphism(TopologicalSpace domain, TopologicalSpace codomain) {
        super(domain, codomain);
    }
}
