package org.rubato.math.topology.morphism;

import static org.rubato.xml.XMLConstants.TOPOLOGICALSPACE;
import static org.rubato.xml.XMLConstants.TOPOLOGICALSPACEMORPHISM;
import static org.rubato.xml.XMLConstants.TYPE_ATTR;

import org.rubato.math.topology.*;
import org.rubato.xml.XMLInputOutput;
import org.rubato.xml.XMLReader;
import org.rubato.xml.XMLWriter;
import org.w3c.dom.Element;

/**
 * A projection from a ProductRing to one its factors.
 * Instances are created using the {@link #make(ProductRing,int)} method.
 *
 * @see ProductRing
 * @author Justin Wells
 */
public class ProjectionMorphism extends TopologicalSpaceMorphism {

    /**
     * Creates a projection from <code>domain</code> to the factor <code>i</code>
     * of <code>domain</code>.
     * @param domain a product ring
     * @param i the index of codomain factor
     */
    public static ProjectionMorphism make(ProductRing domain, int i) {
        if (i < 0) {
            i = 0;
        }
        else if (i >= domain.getFactorCount()) {
            i = domain.getFactorCount()-1;
        }
        Ring codomain = domain.getFactor(i);
        return new ProjectionMorphism(domain, codomain, i);
    }


    /**
     * Returns the index of the codomain factor.
     */
    public int getIndex() {
        return index;
    }


    public TopologicalSpaceElement map(TopologicalSpaceElement x)
            throws MappingException {
        if (!getDomain().hasElement(x)) {
            throw new MappingException("ProjectionMorphism.map: ", x, this);
        }
        return ((ProductElement)x).getFactor(index);
    }


    public boolean isRingHomomorphism() {
        return true;
    }


    public TopologicalSpaceMorphism getRingMorphism() {
        return this;
    }


    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        else if (object instanceof ProjectionMorphism) {
            ProjectionMorphism m = (ProjectionMorphism)object;
            return getDomain().equals(m.getDomain()) && getIndex() == m.getIndex();
        }
        else {
            return false;
        }
    }


    public int compareTo(TopologicalSpaceMorphism object) {
        if (object instanceof ProjectionMorphism) {
            ProjectionMorphism m = (ProjectionMorphism)object;
            int res = getDomain().compareTo(m.getDomain());
            if (res == 0) {
                return getIndex()-m.getIndex();
            }
            else {
                return res;
            }
        }
        else {
            return super.compareTo(object);
        }
    }


    public String toString() {
        return "ProductProjectMorphism["+getDomain()+","+index+"]";
    }


    private final static String INDEX_ATTR = "index";


    public void toXML(XMLWriter writer) {
        writer.openBlockWithType(TOPOLOGICALSPACEMORPHISM, getElementTypeName(), INDEX_ATTR, getIndex());
        getDomain().toXML(writer);
        writer.closeBlock();
    }


    public TopologicalSpaceMorphism fromXML(XMLReader reader, Element element) {
        /*
        assert(element.getAttribute(TYPE_ATTR).equals(getElementTypeName()));
        if (!element.hasAttribute(INDEX_ATTR)) {
            reader.setError("Type %%1 is missing attribute %%2.", getElementTypeName(), INDEX_ATTR);
            return null;
        }
        int index0 = XMLReader.getIntAttribute(element, INDEX_ATTR, 0);
        Element childElement = XMLReader.getChild(element, TOPOLOGICALSPACE);
        if (childElement != null) {
            TopologicalSpaceMorphism m = reader.parseModule(childElement);
            if (m == null) {
                return null;
            }
            if (m instanceof ProductRing) {
                ProjectionMorphism pm = make((ProductRing)m, index0);
                return pm;
            }
            else {
                reader.setError("The module in type %%1 must be a product ring.", getElementTypeName());
                return null;
            }
        }
        else {
            reader.setError("Type %%1 is missing child of type <%2>.", getElementTypeName(), TOPOLOGICALSPACE);
            return null;
        }*/
        return null;
    }


    private static final XMLInputOutput<TopologicalSpaceMorphism> xmlIO =
            ProjectionMorphism.make(ProductRing.make(RRing.ring, RRing.ring), 0);

    public static XMLInputOutput<TopologicalSpaceMorphism> getXMLInputOutput() {
        return xmlIO;
    }


    public String getElementTypeName() {
        return "ProjectionMorphism";
    }


    private ProjectionMorphism(ProductRing domain, Ring codomain, int index) {
        super(domain, codomain);
        this.index = index;
    }


    private int index;
}
