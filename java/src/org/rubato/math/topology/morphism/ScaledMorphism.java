package org.rubato.math.topology.morphism;

import static org.rubato.xml.XMLConstants.TOPOLOGICALSPACEMORPHISM;
import static org.rubato.xml.XMLConstants.TYPE_ATTR;

import org.rubato.math.topology.*;
import org.rubato.xml.XMLInputOutput;
import org.rubato.xml.XMLReader;
import org.rubato.xml.XMLWriter;
import org.w3c.dom.Element;

/**
 * Created by Justin on 4/19/2015.
 */
public final class ScaledMorphism extends TopologicalSpaceMorphism {

    /**
     * Create a morphism from <code>f</code> and scalar <code>value</code>.
     * The resulting morphism <i>h</i> is such that <i>h(x) = value*f(x)</i>.
     * This is a virtual constructor so that simplifications can be made.
     *
     * @return null if <code>f</code> cannot be scaled by <code>value</code>
     */
    public static TopologicalSpaceMorphism make(TopologicalSpaceMorphism f, RingElement scalar) {
        if (!f.getCodomain().getRing().hasElement(scalar)) {
            return null;
        }
        if (scalar.isOne()) {
            return f;
        }
        else if (scalar.isZero()) {
            return getConstantMorphism(f.getCodomain(), f.getCodomain().getZero());
        }
        else if (f.isConstant()) {
            try {
                return new ConstantMorphism(f.getDomain(), f.map(f.getDomain().getZero()).scaled(scalar));
            }
            catch (DomainException e) {
                throw new AssertionError("This should never happen!");
            }
            catch (MappingException e) {
                throw new AssertionError("This should never happen!");
            }
        }
        else {
            return new ScaledMorphism(f, scalar);
        }
    }


    public TopologicalSpaceElement map(TopologicalSpaceElement x)
            throws MappingException {
        try {
            return f.map(x).scaled(scalar);
        }
        catch (DomainException e) {
            throw new MappingException("ScaledMorphism.map: ", x, this);
        }
    }


    public int compareTo(TopologicalSpaceMorphism object) {
        if (object instanceof ScaledMorphism) {
            ScaledMorphism m = (ScaledMorphism)object;
            int comp = f.compareTo(m.f);
            if (comp == 0) {
                return scalar.compareTo(m.scalar);
            }
            else {
                return comp;
            }
        }
        else {
            return super.compareTo(object);
        }
    }


    public boolean isModuleHomomorphism() {
        return f.isModuleHomomorphism();
    }


    public boolean isLinear() {
        return f.isLinear();
    }


    public boolean isConstant() {
        return f.isConstant();
    }


    public TopologicalSpaceMorphism getRingMorphism() {
        return f.getRingMorphism();
    }


    /**
     * Returns the morphism <i>f</i> from <i>a*f</i>.
     */
    public TopologicalSpaceMorphism getMorphism() {
        return f;
    }


    /**
     * Returns the scalar <i>a</i> from <i>a*f</i>.
     */
    public RingElement getScalar() {
        return scalar;
    }


    public boolean equals(Object object) {
        if (object instanceof ScaledMorphism) {
            ScaledMorphism morphism = (ScaledMorphism)object;
            return f.equals(morphism.f) && scalar.equals(morphism.scalar);
        }
        else {
            return false;
        }
    }


    public String toString() {
        return "ScaledMorphism["+f+","+scalar+"]";
    }


    public void toXML(XMLWriter writer) {
        writer.openBlockWithType(TOPOLOGICALSPACEMORPHISM, getElementTypeName());
        f.toXML(writer);
        scalar.toXML(writer);
        writer.closeBlock();
    }


    public TopologicalSpaceMorphism fromXML(XMLReader reader, Element element) {
        assert(element.getAttribute(TYPE_ATTR).equals(getElementTypeName()));
        Element childElement = XMLReader.getChild(element, TOPOLOGICALSPACEMORPHISM);
        /* FIX reader.parseModuleElement()
        if (childElement != null) {
            TopologicalSpaceMorphism f0 = reader.parseModuleMorphism(childElement);
            Element el = XMLReader.getNextSibling(childElement, TOPOLOGICALSPACEELEMENT);
            if (el == null) {
                reader.setError("Type %%1 is missing second child of type <%2>.", getElementTypeName(), TOPOLOGICALSPACEELEMENT);
                return null;
            }
            TopologicalSpaceElement value = reader.parseModuleElement(el);
            if (f0 == null || value == null) {
                return null;
            }
            if (value instanceof RingElement) {
                TopologicalSpaceMorphism m = make(f0, (RingElement)value);
                if (m == null) {
                    reader.setError("Cannot scale %1 by %2.", f0, value);
                }
                return m;
            }
            else {
                reader.setError("The ModuleElement in type %%1 must be a ring element.", getElementTypeName());
                return null;
            }
        }
        else {
            reader.setError("Type %%1 is missing children of type <%2>.", getElementTypeName(), TOPOLOGICALSPACEMORPHISM);
            return null;
        }*/
        return null;
    }


    private static final XMLInputOutput<TopologicalSpaceMorphism> xmlIO =
            new ScaledMorphism(getIdentityMorphism(RRing.ring), new RElement(0));

    public static XMLInputOutput<TopologicalSpaceMorphism> getXMLInputOutput() {
        return xmlIO;
    }


    public String getElementTypeName() {
        return "ScaledMorphism";
    }


    private ScaledMorphism(TopologicalSpaceMorphism f, RingElement scalar) {
        super(f.getDomain(), f.getCodomain());
        this.f = f;
        this.scalar = scalar;
    }


    private TopologicalSpaceMorphism f;
    private RingElement    scalar;
}
