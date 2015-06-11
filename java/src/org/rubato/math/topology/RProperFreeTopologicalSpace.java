package org.rubato.math.topology;

import static org.rubato.xml.XMLConstants.DIMENSION_ATTR;
import static org.rubato.xml.XMLConstants.TOPOLOGICALSPACE;
import static org.rubato.xml.XMLConstants.TYPE_ATTR;

import java.util.Iterator;
import java.util.List;

import org.rubato.math.matrix.RMatrix;
import org.rubato.math.topology.morphism.TopologicalSpaceMorphism;
import org.rubato.math.topology.morphism.RFreeAffineMorphism;
import org.rubato.util.TextUtils;
import org.rubato.xml.XMLInputOutput;
import org.rubato.xml.XMLReader;
import org.rubato.xml.XMLWriter;
import org.w3c.dom.Element;

/**
 * Created by Justin on 4/17/2015.
 */
public final class RProperFreeTopologicalSpace
        extends ProperFreeTopologicalSpace
        implements RFreeTopologicalSpace {

    public static final RProperFreeTopologicalSpace nullTopologicalSpace = new RProperFreeTopologicalSpace(0);

    public static RFreeTopologicalSpace make(int dimension) {
        dimension = (dimension < 0)?0:dimension;
        if (dimension == 0) {
            return nullTopologicalSpace;
        }
        else if (dimension == 1) {
            return RRing.ring;
        }
        else {
            return new RProperFreeTopologicalSpace(dimension);
        }
    }


    public RProperFreeElement getZero() {
        double[] res = new double[getDimension()];
        for (int i = 0; i < getDimension(); i++) {
            res[i] = 0;
        }
        return (RProperFreeElement)RProperFreeElement.make(res);
    }


    public RProperFreeElement getUnitElement(int i) {
        double[] v = new double[getDimension()];
        v[i] = 1;
        return (RProperFreeElement)RProperFreeElement.make(v);
    }


    public TopologicalSpace getNullTopologicalSpace() {
        return nullTopologicalSpace;
    }


    public boolean isNullTopologicalSpace() {
        return (this == nullTopologicalSpace);
    }


    public TopologicalSpace getComponentModule(int i) {
        return RRing.ring;
    }


    public Ring getRing() {
        return RRing.ring;
    }


    public boolean isVectorspace() {
        return true;
    }


    public boolean hasElement(TopologicalSpaceElement element) {
        return (element instanceof RProperFreeElement &&
                element.getLength() == getDimension());
    }


    public int compareTo(TopologicalSpace object) {
        if (object instanceof RProperFreeTopologicalSpace) {
            RProperFreeTopologicalSpace module = (RProperFreeTopologicalSpace)object;
            return getDimension()-module.getDimension();
        }
        else {
            return super.compareTo(object);
        }
    }


    public RFreeElement createElement(List<TopologicalSpaceElement> elements) {
        if (elements.size() < getDimension()) {
            return null;
        }

        Iterator<TopologicalSpaceElement> iter = elements.iterator();
        double[] values = new double[getDimension()];
        for (int i = 0; i < getDimension(); i++) {
            TopologicalSpaceElement castElement = iter.next().cast(RRing.ring);
            if (castElement == null) {
                return null;
            }
            values[i] = ((RElement)castElement).getValue();
        }

        return RProperFreeElement.make(values);
    }


    public TopologicalSpaceElement cast(TopologicalSpaceElement element) {
        if (element.getLength() == getDimension()) {
            if (element instanceof RProperFreeElement) {
                return element;
            }
            else {
                double[] elements = new double[getDimension()];
                for (int i = 0; i < getDimension(); i++) {
                    TopologicalSpaceElement castElement = RRing.ring.cast(element.getComponent(i));
                    if (castElement == null) {
                        return null;
                    }
                    elements[i] = ((RElement)castElement).getValue();
                }
                return RProperFreeElement.make(elements);
            }
        }
        else {
            return null;
        }
    }


    public boolean equals(Object object) {
        return (object instanceof RProperFreeTopologicalSpace &&
                getDimension() == ((RProperFreeTopologicalSpace)object).getDimension());
    }


    public TopologicalSpaceElement parseString(String string) {
        string = TextUtils.unparenthesize(string);
        String[] components = string.split(",");
        if (components.length != getDimension()) {
            return null;
        }
        else {
            double[] values = new double[components.length];
            for (int i = 0; i < values.length; i++) {
                try {
                    values[i] = Double.parseDouble(components[i]);
                }
                catch (NumberFormatException e) {
                    return null;
                }
            }
            return RProperFreeElement.make(values);
        }
    }


    public String toString() {
        return "RFreeModule["+getDimension()+"]";
    }


    public String toVisualString() {
        return "R^"+getDimension();
    }


    public void toXML(XMLWriter writer) {
        writer.emptyWithType(TOPOLOGICALSPACE, getElementTypeName(), DIMENSION_ATTR, getDimension());
    }


    public TopologicalSpace fromXML(XMLReader reader, Element element) {
        assert(element.getAttribute(TYPE_ATTR).equals(getElementTypeName()));

        if (!element.hasAttribute(DIMENSION_ATTR)) {
            reader.setError("Type %%1 is missing attribute %%2.", getElementTypeName(), DIMENSION_ATTR);
            return null;
        }

        int dimension;
        try {
            dimension = Integer.parseInt(element.getAttribute("dimension"));
        }
        catch (NumberFormatException e) {
            reader.setError("Attribute %%1 of type %%2 must be an integer.", DIMENSION_ATTR, getElementTypeName());
            return null;
        }
        if (dimension < 0) {
            reader.setError("Attribute %%1 of type %%2 must be an integer >= 0.", DIMENSION_ATTR, getElementTypeName());
            return null;
        }

        return RProperFreeTopologicalSpace.make(dimension);
    }


    public static XMLInputOutput<TopologicalSpace> getXMLInputOutput() {
        return RProperFreeTopologicalSpace.nullTopologicalSpace;
    }


    public String getElementTypeName() {
        return "RFreeModule";
    }


    public int hashCode() {
        return 37*basicHash + getDimension();
    }


    protected TopologicalSpaceMorphism _getProjection(int index) {
        RMatrix A = new RMatrix(1, getDimension());
        A.set(0, index, 1);
        return RFreeAffineMorphism.make(A, new double[] { 0.0 });
    }


    protected TopologicalSpaceMorphism _getInjection(int index) {
        RMatrix A = new RMatrix(getDimension(), 1);
        A.set(index, 0, 1.0);
        double[] b = new double[getDimension()];
        for (int i = 0; i < getDimension(); i++) {
            b[i] = 0.0;
        }
        return RFreeAffineMorphism.make(A, b);
    }


    private RProperFreeTopologicalSpace(int dimension) {
        super(dimension);
    }


    private final static int basicHash = "RFreeModule".hashCode();
}
