package org.rubato.math.topology.morphism;

import static org.rubato.xml.XMLConstants.TOPOLOGICALSPACEMORPHISM;
import static org.rubato.xml.XMLConstants.TYPE_ATTR;

import org.rubato.math.topology.TopologicalSpaceElement;
import org.rubato.math.topology.RRing;
import org.rubato.xml.XMLInputOutput;
import org.rubato.xml.XMLReader;
import org.rubato.xml.XMLWriter;
import org.w3c.dom.Element;

/**
 * Created by Justin on 4/19/2015.
 */
public final class PowerMorphism extends TopologicalSpaceMorphism {

    /**
     * Creates a morphism from <code>f</code> raised to <code>power</code>.
     * The resulting morphism h is such that h(x) = f(f(...(f(x))...)),
     * where there are <code>exp</code> repetitions of <code>f</code>.
     * This is a virtual constructor, so that simplifications can be made.
     */
    public static TopologicalSpaceMorphism make(TopologicalSpaceMorphism f, int exp)
            throws CompositionException {
        if (!f.getDomain().equals(f.getCodomain())) {
            throw new CompositionException("PowerMorphism.make: Cannot raise "+f+" to power "+exp);
        }
        else if (exp < 0) {
            throw new CompositionException("PowerMorphism.make: Cannot raise "+f+" to a negative power "+exp);
        }
        else if (exp == 0) {
            return TopologicalSpaceMorphism.getIdentityMorphism(f.getDomain());
        }
        else if (exp == 1) {
            return f;
        }
        else if (f.isIdentity()) {
            return f;
        }
        else if (f.isConstant()) {
            return f;
        }
        else {
            return new PowerMorphism(f, exp);
        }
    }


    public TopologicalSpaceElement map(TopologicalSpaceElement x)
            throws MappingException {
        TopologicalSpaceElement res = x;
        for (int i = 1; i < exponent; i++) {
            res = f.map(res);
        }
        return res;
    }


    public boolean isModuleHomomorphism() {
        return f.isModuleHomomorphism();
    }


    public boolean isRingHomomorphism() {
        return f.isRingHomomorphism();
    }


    public boolean isLinear() {
        return f.isLinear();
    }


    public boolean isIdentity() {
        return exponent == 0 || f.isIdentity();
    }


    public boolean isConstant() {
        return f.isConstant();
    }


    public TopologicalSpaceMorphism getRingMorphism() {
        return getIdentityMorphism(getDomain().getRing());
    }


    /**
     * Returns the base morphism <i>f</i> of the power <i>f^n</i>.
     */
    public TopologicalSpaceMorphism getBaseMorphism() {
        return f;
    }

    /**
     * Returns the exponent <i>n</i> of the power <i>f^n</i>.
     */
    public int getExponent() {
        return exponent;
    }


    public TopologicalSpaceMorphism power(int n)
            throws CompositionException {
        return make(f, exponent+n);
    }


    public int compareTo(TopologicalSpaceMorphism object) {
        if (object instanceof PowerMorphism) {
            PowerMorphism morphism = (PowerMorphism)object;
            int res = f.compareTo(morphism.f);
            if (res == 0) {
                return exponent-morphism.exponent;
            }
            else {
                return res;
            }
        }
        else {
            return super.compareTo(object);
        }
    }


    public boolean equals(Object object) {
        if (object instanceof PowerMorphism) {
            PowerMorphism morphism = (PowerMorphism)object;
            return (f.equals(morphism.f) && exponent == morphism.exponent);
        }
        else {
            return false;
        }
    }


    public String toString() {
        return "PowerMorphism["+f+","+exponent+"]";
    }


    private final static String POWER_ATTR = "power";

    public void toXML(XMLWriter writer) {
        writer.openBlockWithType(TOPOLOGICALSPACEMORPHISM, getElementTypeName(), POWER_ATTR, exponent);
        f.toXML(writer);
        writer.closeBlock();
    }


    public TopologicalSpaceMorphism fromXML(XMLReader reader, Element element) {
        assert(element.getAttribute(TYPE_ATTR).equals(getElementTypeName()));
        if (!element.hasAttribute(POWER_ATTR)) {
            reader.setError("Type %%1 is missing attribute %%2.", getElementTypeName(), POWER_ATTR);
            return null;
        }
        int power;
        try {
            power = Integer.parseInt(element.getAttribute(POWER_ATTR));
        }
        catch (NumberFormatException e) {
            reader.setError("Attribute %%1 of type %%2 must be an integer.", POWER_ATTR, getElementTypeName());
            return null;
        }
        /* FIX reader.parseModuleMorphism()
        Element childElement = XMLReader.getChild(element, TOPOLOGICALSPACEMORPHISM);
        if (childElement != null) {
            TopologicalSpaceMorphism f0 = reader.parseModuleMorphism(childElement);
            if (f0 == null) {
                return null;
            }
            try {
                return make(f0, power);
            }
            catch (CompositionException e) {
                reader.setError(e.getMessage());
                return null;
            }
        }
        else {
            reader.setError("Type %%1 is missing child of type <%2>.", getElementTypeName(), TOPOLOGICALSPACEMORPHISM);
            return null;
        }*/
        return null;
    }


    private static final XMLInputOutput<TopologicalSpaceMorphism> xmlIO =
            new PowerMorphism(getIdentityMorphism(RRing.ring), 0);

    public static XMLInputOutput<TopologicalSpaceMorphism> getXMLInputOutput() {
        return xmlIO;
    }


    public String getElementTypeName() {
        return "PowerMorphism";
    }


    private PowerMorphism(TopologicalSpaceMorphism f, int exp) {
        super(f.getDomain(), f.getCodomain());
        this.f = f;
        this.exponent = exp;
    }


    private TopologicalSpaceMorphism f;
    private int            exponent;
}
