package org.rubato.math.topology;

import static org.rubato.xml.XMLConstants.DIMENSION_ATTR;
import static org.rubato.xml.XMLConstants.MODULE;
import static org.rubato.xml.XMLConstants.TYPE_ATTR;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.rubato.math.topology.morphism.GenericAffineMorphism;
import org.rubato.math.topology.morphism.TopologicalSpaceMorphism;
import org.rubato.util.TextUtils;
import org.rubato.xml.XMLInputOutput;
import org.rubato.xml.XMLReader;
import org.rubato.xml.XMLWriter;
import org.w3c.dom.Element;

/**
 * Free modules over polynomials.
 * @see org.rubato.math.module.PolynomialProperFreeElement
 *
 * @author Gérard Milmeister
 */
public final class PolynomialProperFreeTopologicalSpace
        extends ProperFreeTopologicalSpace
        implements PolynomialFreeTopologicalSpace {

    public static PolynomialFreeTopologicalSpace make(Ring coefficientRing, String indeterminate, int dimension) {
        dimension = (dimension < 0)?0:dimension;
        if (dimension == 1) {
            return PolynomialRing.make(coefficientRing, indeterminate);
        }
        else {
            return new PolynomialProperFreeTopologicalSpace(coefficientRing, indeterminate, dimension);
        }
    }


    public static PolynomialFreeTopologicalSpace make(PolynomialRing polyRing, int dimension) {
        if (dimension == 1) {
            return polyRing;
        }
        else {
            return new PolynomialProperFreeTopologicalSpace(polyRing.getCoefficientRing(), polyRing.getIndeterminate(), dimension);
        }
    }


    public PolynomialFreeElement getZero() {
        PolynomialElement[] res = new PolynomialElement[getDimension()];
        for (int i = 0; i < getDimension(); i++) {
            res[i] = ring.getZero();
        }
        return PolynomialProperFreeElement.make(ring, res);
    }


    public PolynomialFreeElement getUnitElement(int i) {
        PolynomialElement[] v = new PolynomialElement[getDimension()];
        assert(i >= 0 && i < getDimension());
        for (int j = 0; j < getDimension(); j++) {
            v[j] = getRing().getZero();
        }
        v[i] = getRing().getOne();
        return PolynomialProperFreeElement.make(getRing(), v);
    }


    public PolynomialFreeTopologicalSpace getNullTopologicalSpace() {
        return make(ring.getCoefficientRing(), ring.getIndeterminate(), 0);
    }


    public boolean isNullTopologicalSpace() {
        return this.getDimension() == 0;
    }


    public PolynomialRing getComponentModule(int i) {
        return ring;
    }


    public PolynomialRing getRing() {
        return ring;
    }


    public Ring getCoefficientRing() {
        return ring.getCoefficientRing();
    }


    public String getIndeterminate() {
        return ring.getIndeterminate();
    }


    public boolean isVectorspace() {
        return false;
    }


    public boolean hasElement(TopologicalSpaceElement element) {
        return (element instanceof PolynomialProperFreeElement &&
                element.getLength() == getDimension() &&
                ring.equals(((PolynomialElement)element).getRing()));
    }


    public int compareTo(TopologicalSpace object) {
        if (object instanceof PolynomialProperFreeTopologicalSpace) {
            PolynomialProperFreeTopologicalSpace module = (PolynomialProperFreeTopologicalSpace)object;
            int c = getRing().compareTo(module.getRing());
            if (c != 0) {
                return c;
            }
            int d = getDimension()-module.getDimension();
            if (d != 0) {
                return d;
            }
            return 0;
        }
        else {
            return super.compareTo(object);
        }
    }


    public PolynomialProperFreeElement createElement(List<TopologicalSpaceElement> elements) {
        if (elements.size() < getDimension()) {
            return null;
        }
        PolynomialElement[] values = new PolynomialElement[getDimension()];
        Iterator<TopologicalSpaceElement> iter = elements.iterator();
        for (int i = 0; i < getDimension(); i++) {
            values[i] = ring.cast(iter.next());
            if (values[i] == null) {
                return null;
            }
        }
        return (PolynomialProperFreeElement)PolynomialProperFreeElement.make(ring, values);
    }


    public PolynomialProperFreeElement cast(TopologicalSpaceElement element) {
        int dim = element.getLength();
        if (dim != getDimension()) {
            return null;
        }
        PolynomialElement[] values = new PolynomialElement[dim];
        for (int i = 0; i < dim; i++) {
            values[i] = ring.cast(element.getComponent(i));
            if (values[i] == null) {
                return null;
            }
        }
        return (PolynomialProperFreeElement)PolynomialProperFreeElement.make(ring, values);
    }


    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        else if (object instanceof PolynomialProperFreeTopologicalSpace) {
            PolynomialProperFreeTopologicalSpace m = (PolynomialProperFreeTopologicalSpace)object;
            if (getDimension() != m.getDimension()) {
                return false;
            }
            return getRing().equals(m.getRing());
        }
        return false;
    }


    public PolynomialProperFreeElement parseString(String string) {
        ArrayList<String> strings = parse(TextUtils.unparenthesize(string));
        if (strings.size() < getDimension()) {
            return null;
        }
        PolynomialElement[] values = new PolynomialElement[getDimension()];
        for (int i = 0; i < getDimension(); i++) {
            String s = strings.get(i);
            values[i] = ring.parseString(s);
            if (values[i] == null) {
                return null;
            }
        }
        return (PolynomialProperFreeElement)PolynomialProperFreeElement.make(ring, values);
    }


    private static ArrayList<String> parse(String s) {
        int pos = 0;
        int lastpos = 0;
        int level = 0;
        ArrayList<String> m = new ArrayList<String>();
        while (pos < s.length()) {
            if (s.charAt(pos) == '(') {
                pos++;
                level++;
            }
            else if (s.charAt(pos) == ')') {
                pos++;
                level--;
            }
            else if (s.charAt(pos) == ',' && level == 0) {
                m.add(s.substring(lastpos, pos));
                pos++;
                lastpos = pos;
            }
            else {
                pos++;
            }
        }
        m.add(s.substring(lastpos,pos).trim());
        return m;
    }


    public String toString() {
        return "PolynomialFreeModule["+getRing()+","+getDimension()+"]";
    }


    public String toVisualString() {
        return "("+getRing()+")^"+getDimension();
    }


    private final static String INDETERMINATE_ATTR = "indeterminate";


    public void toXML(XMLWriter writer) {
        writer.openBlockWithType(MODULE, getElementTypeName(),
                INDETERMINATE_ATTR, getIndeterminate(),
                DIMENSION_ATTR, getDimension());
        getCoefficientRing().toXML(writer);
        writer.closeBlock();
    }


    public TopologicalSpace fromXML(XMLReader reader, Element element) {
        /*
        assert(element.getAttribute(TYPE_ATTR).equals(getElementTypeName()));
        if (!(element.hasAttribute("indeterminate"))) {
            reader.setError("Type %%1 must have attribute %%2.", getElementTypeName(), INDETERMINATE_ATTR);
        }
        String indeterminate = element.getAttribute(INDETERMINATE_ATTR);
        int dimension = XMLReader.getIntAttribute(element, DIMENSION_ATTR, 0, Integer.MAX_VALUE, 0);
        Element childElement = XMLReader.getChild(element, MODULE);
        if (childElement != null) {
            TopologicalSpace module = reader.parseModule(childElement);
            if (module == null) {
                return null;
            }
            if (!(module instanceof Ring)) {
                reader.setError("Type %%1 must have a child of type %%2.", getElementTypeName(), "Ring");
                return null;
            }
            Ring rng = (Ring)module;
            PolynomialFreeTopologicalSpace pfm = PolynomialProperFreeTopologicalSpace.make(rng, indeterminate, dimension);
            return pfm;
        }
        else {
            reader.setError("Type %%1 must have a child of type %%2.", getElementTypeName(), "Ring");
            return null;
        }*/
        return null;
    }


    private final static PolynomialFreeTopologicalSpace xmlIO =
            PolynomialProperFreeTopologicalSpace.make(RRing.ring, "X", 0);

    public static XMLInputOutput<TopologicalSpace> getXMLInputOutput() {
        return xmlIO;
    }


    public String getElementTypeName() {
        return "PolynomialFreeModule";
    }


    public int hashCode() {
        return (37*basicHash + getDimension()) ^ ring.hashCode();
    }


    protected TopologicalSpaceMorphism _getProjection(int index) {
        GenericAffineMorphism m = new GenericAffineMorphism(getRing(), getDimension(), 1);
        m.setMatrix(0, index, getRing().getOne());
        return m;
    }


    protected TopologicalSpaceMorphism _getInjection(int index) {
        GenericAffineMorphism m = new GenericAffineMorphism(getRing(), 1, getDimension());
        m.setMatrix(index, 0, getRing().getOne());
        return m;
    }


    private PolynomialProperFreeTopologicalSpace(Ring coefficientRing, String indeterminate, int dimension) {
        super(dimension);
        this.ring = PolynomialRing.make(coefficientRing, indeterminate);

    }


    private PolynomialRing ring;

    private final static int basicHash = "PolynomialFreeModule".hashCode();
}
