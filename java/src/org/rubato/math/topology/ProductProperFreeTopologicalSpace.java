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
 * Free modules over a product ring.
 * @see org.rubato.math.module.ProductProperFreeElement
 *
 * @author Gérard Milmeister
 */
public final class ProductProperFreeTopologicalSpace
        extends ProperFreeTopologicalSpace
        implements ProductFreeTopologicalSpace {

    public static FreeTopologicalSpace make(Ring[] rings, int dimension) {
        dimension = (dimension < 0)?0:dimension;
        if (dimension == 1) {
            return ProductRing.make(rings);
        }
        else {
            if (rings.length == 1) {
                return rings[0].getFreeTopologicalSpace(dimension);
            }
            else {
                return new ProductProperFreeTopologicalSpace(rings, dimension);
            }
        }
    }


    public static ProductFreeTopologicalSpace make(ProductRing ring, int dimension) {
        if (dimension == 1) {
            return ring;
        }
        else {
            return new ProductProperFreeTopologicalSpace(ring, dimension);
        }
    }


    public ProductFreeElement getZero() {
        ProductElement[] res = new ProductElement[getDimension()];
        for (int i = 0; i < getDimension(); i++) {
            res[i] = ring.getZero();
        }
        return ProductProperFreeElement.make(ring, res);
    }


    public ProductProperFreeElement getUnitElement(int i) {
        ProductElement[] v = new ProductElement[getDimension()];
        for (int j = 0; j < getDimension(); j++) {
            v[j] = (ProductElement)getZero();
        }
        v[i] = getRing().getOne();
        return (ProductProperFreeElement)ProductProperFreeElement.make(getRing(), v);
    }


    public ProductProperFreeTopologicalSpace getNullTopologicalSpace() {
        return (ProductProperFreeTopologicalSpace)make(ring, 0);
    }


    public boolean isNullTopologicalSpace() {
        return getDimension() == 0;
    }


    public ProductRing getComponentModule(int i) {
        return ring;
    }


    public ProductRing getRing() {
        return ring;
    }


    public int getFactorCount() {
        return getRing().getFactorCount();
    }


    public Ring[] getFactors() {
        return getRing().getFactors();
    }


    public Ring getFactor(int i) {
        return getRing().getFactor(i);
    }


    public boolean isVectorspace() {
        return ring.isField();
    }


    public boolean hasElement(TopologicalSpaceElement element) {
        return element.getTopologicalSpace().equals(this);
    }


    public int compareTo(TopologicalSpace object) {
        if (object instanceof ProductProperFreeTopologicalSpace) {
            ProductProperFreeTopologicalSpace m = (ProductProperFreeTopologicalSpace)object;
            int c = getRing().compareTo(m.getRing());
            if (c != 0) {
                return c;
            }
            else {
                return getDimension()-m.getDimension();
            }
        }
        else {
            return super.compareTo(object);
        }
    }


    public ProductFreeElement createElement(List<TopologicalSpaceElement> elements) {
        if (elements.size() < getDimension()) {
            return null;
        }

        ProductElement[] components = new ProductElement[getDimension()];
        Iterator<TopologicalSpaceElement> iter = elements.iterator();
        for (int i = 0; i < getDimension(); i++) {
            TopologicalSpaceElement object = iter.next();
            if (object instanceof ProductElement) {
                ProductElement productElement = (ProductElement)object.cast(getRing());
                if (productElement == null) {
                    return null;
                }
                else {
                    components[i] = productElement;
                }
            }
            else {
                return null;
            }
        }
        return ProductProperFreeElement.make(getRing(), components);
    }


    public ProductProperFreeElement cast(TopologicalSpaceElement element) {
        if (element.getLength() >= getDimension()) {
            ProductElement[] components = new ProductElement[getDimension()];
            for (int i = 0; i < getDimension(); i++) {
                TopologicalSpaceElement component = element.getComponent(i);
                ProductElement productElement = getRing().cast(component);
                if (productElement == null) {
                    return null;
                }
                components[i] = productElement;
            }
            return (ProductProperFreeElement)ProductProperFreeElement.make(getRing(), components);
        }
        else {
            return null;
        }
    }


    public boolean equals(Object object) {
        if (object instanceof ProductProperFreeTopologicalSpace) {
            ProductProperFreeTopologicalSpace m = (ProductProperFreeTopologicalSpace)object;
            if (getDimension() != m.getDimension()) {
                return false;
            }
            else if (!getRing().equals(m.getRing())) {
                return false;
            }
            else {
                return true;
            }
        }
        else {
            return false;
        }
    }


    public ProductFreeElement parseString(String string) {
        ArrayList<String> m = parse(TextUtils.unparenthesize(string));
        if (m.size() != getDimension()) {
            return null;
        }

        ProductElement[] components = new ProductElement[getDimension()];
        for (int i = 0; i < getDimension(); i++) {
            TopologicalSpaceElement element = getRing().parseString(m.get(i));
            if (element == null) {
                return null;
            }
            components[i] = (ProductElement)element;
        }
        return ProductProperFreeElement.make(getRing(), components);
    }


    private ArrayList<String> parse(String s) {
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
        m.add(s.substring(lastpos,pos));
        return m;
    }


    public String toString() {
        return "ProductFreeModule["+getDimension()+"]["+getRing()+"]";
    }


    public String toVisualString() {
        return "("+getRing().toVisualString()+")"+"^"+getDimension();
    }


    public void toXML(XMLWriter writer) {
        writer.openBlockWithType(MODULE, getElementTypeName(),
                DIMENSION_ATTR, getDimension());
        getRing().toXML(writer);
        writer.closeBlock();
    }


    public TopologicalSpace fromXML(XMLReader reader, Element element) {
        /*
        assert(element.getAttribute(TYPE_ATTR).equals(getElementTypeName()));

        if (!element.hasAttribute(DIMENSION_ATTR)) {
            reader.setError("Type %%1 is missing attribute %%2.", getElementTypeName(), DIMENSION_ATTR);
            return null;
        }

        int dimension;
        try {
            dimension = Integer.parseInt(element.getAttribute(DIMENSION_ATTR));
        }
        catch (NumberFormatException e) {
            reader.setError("Attribute %%1 of type %%2 must be an integer.", DIMENSION_ATTR, getElementTypeName());
            return null;
        }
        if (dimension < 0) {
            reader.setError("Attribute %%1 of type %%2 must be an integer >= 0.", DIMENSION_ATTR, getElementTypeName());
            return null;
        }

        Element childElement = XMLReader.getChild(element, MODULE);
        if (childElement == null) {
            reader.setError("Type %%1 must have a child of type <%2>.", getElementTypeName(), MODULE);
            return null;
        }
        TopologicalSpace module = reader.parseModule(childElement);
        if (module == null || !(module instanceof ProductRing)) {
            reader.setError("Module in %%1 must be a product ring.", getElementTypeName());
            return null;
        }
        ProductRing productRing = (ProductRing)module;

        return ProductProperFreeTopologicalSpace.make(productRing, dimension);
        */
        return null;
    }


    public String getElementTypeName() {
        return "ProductFreeModule";
    }


    public static XMLInputOutput<TopologicalSpace> getXMLInputOuput() {
        return new ProductProperFreeTopologicalSpace((ProductRing)null, 0);
    }


    public int hashCode() {
        if (hashcode == 0) {
            hashcode = 37*basicHash+7*ring.hashCode()+getDimension();
        }
        return hashcode;
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


    private ProductProperFreeTopologicalSpace(Ring[] rings, int dimension) {
        super(dimension);
        this.ring = ProductRing.make(rings);
    }


    private ProductProperFreeTopologicalSpace(ProductRing ring, int dimension) {
        super(dimension);
        this.ring = ring;
    }


    private ProductRing ring;

    private final static int basicHash = "ProductFreeModule".hashCode();
    int hashcode = 0;
}

