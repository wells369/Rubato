package org.rubato.math.topology;

import static org.rubato.xml.XMLConstants.TOPOLOGICALSPACE;
import static org.rubato.xml.XMLConstants.TYPE_ATTR;

import java.util.List;

import org.rubato.math.topology.morphism.TopologicalSpaceMorphism;
import org.rubato.util.TextUtils;
import org.rubato.xml.XMLInputOutput;
import org.rubato.xml.XMLReader;
import org.rubato.xml.XMLWriter;
import org.w3c.dom.Element;

/**
 * Created by Justin on 4/17/2015.
 */
public final class RRing extends TopologicalRing implements RFreeTopologicalSpace {

    /**
     * The unique instance of the ring of reals.
     */
    public static final RRing ring = new RRing();

    public RElement getZero() {
        return new RElement(0);
    }


    public RElement getOne() {
        return new RElement(1);
    }


    public RElement getUnitElement(int i) {
        return getOne();
    }


    public TopologicalSpace getNullTopologicalSpace() {
        return RProperFreeTopologicalSpace.nullTopologicalSpace;
    }


    public boolean isNullTopologicalSpace() {
        return false;
    }


    public boolean isField() {
        return true;
    }


    public boolean isVectorspace() {
        return true;
    }


    public TopologicalSpaceMorphism getIdentityMorphism() {
        return TopologicalSpaceMorphism.getIdentityMorphism(this);
    }


    public boolean hasElement(TopologicalSpaceElement element) {
        return (element instanceof RElement);
    }


    public FreeTopologicalSpace getFreeTopologicalSpace(int dimension) {
        return RProperFreeTopologicalSpace.make(dimension);
    }


    public boolean equals(Object object) {
        return this == object;
    }


    public int compareTo(TopologicalSpace object) {
        if (this == object) {
            return 0;
        }
        else {
            return super.compareTo(object);
        }
    }


    public RElement createElement(List<TopologicalSpaceElement> elements) {
        if (!elements.isEmpty()) {
            return (RElement)elements.get(0).cast(this);
        }
        else {
            return null;
        }
    }


    public TopologicalSpaceElement cast(TopologicalSpaceElement element) {
        if (element instanceof RElement) {
            return element;
        } else {
            return null;
        }
    }

/*
    public RElement cast(ZElement element) {
        return new RElement(element.getValue());
    }


    public RElement cast(ZnElement element) {
        return new RElement(element.getValue());
    }


    public RElement cast(QElement element) {
        return new RElement(element.getValue().doubleValue());
    }


    public RElement cast(CElement element) {
        return new RElement(element.getValue().getReal());
    }

*/
    public String toString() {
        return "RRing";
    }


    public String toVisualString() {
        return "R";
    }


    public TopologicalSpaceElement parseString(String string) {
        try {
            double value = Double.parseDouble(TextUtils.unparenthesize(string));
            return new RElement(value);
        }
        catch (NumberFormatException e) {
            return null;
        }
    }


    public void toXML(XMLWriter writer) {
        writer.emptyWithType(TOPOLOGICALSPACE, getElementTypeName());
    }


    public TopologicalSpace fromXML(XMLReader reader, Element element) {
        assert(element.getAttribute(TYPE_ATTR).equals(getElementTypeName()));
        return RRing.ring;
    }


    public String getElementTypeName() {
        return "RRing";
    }


    public static XMLInputOutput<TopologicalSpace> getXMLInputOuput() {
        return RRing.ring;
    }


    public int hashCode() {
        return basicHash;
    }


    protected int getNumberRingOrder() {
        return 300;
    }


    private final static int basicHash = "RRing".hashCode();

    private RRing() { /* not allowed */ }
}
