package org.rubato.math.topology.morphism;

import static org.rubato.xml.XMLConstants.TOPOLOGICALSPACEMORPHISM;
import static org.rubato.xml.XMLConstants.TYPE_ATTR;

import org.rubato.math.topology.RRing;
import org.rubato.math.topology.TopologicalSpaceElement;
import org.rubato.xml.XMLInputOutput;
import org.rubato.xml.XMLReader;
import org.rubato.xml.XMLWriter;
import org.w3c.dom.Element;

/**
 * Created by Justin on 4/17/2015.
 */
public final class CompositionMorphism extends TopologicalSpaceMorphism {

    /**
     * Constructs a morphism from <code>f</code> and <code>g</code>.
     * The resulting morphism <code>h</code> is such that <i>h(x) = f(g(x))</i>.
     * This is used instead of a constructor, so that simplifications
     * can be made.
     *
     * @throws CompositionException if composition is not valid
     */
    static TopologicalSpaceMorphism make(TopologicalSpaceMorphism f, TopologicalSpaceMorphism g)
            throws CompositionException {
        if (!composable(f, g)) {
            throw new CompositionException("CompositionMorphism.make: Cannot compose "+f+" with "+g);
        }
        else if (f.isIdentity() && g.isIdentity()) {
            return TopologicalSpaceMorphism.getIdentityMorphism(f.getDomain());
        }
        else if (f.isIdentity()) {
            return g;
        }
        else if (g.isIdentity()) {
            return f;
        }
        else if (f.isConstant()) {
            return f;
        }
        else if (g.isConstant()) {
            try {
                return TopologicalSpaceMorphism.getConstantMorphism(f.getCodomain(), f.map(g.atZero()));
            }
            catch (MappingException e) {
                throw new CompositionException("CompositionMorphism.make: Cannot not compose "+f+" and "+g);
            }
        }
        else {
            return new CompositionMorphism(f, g);
        }
    }


    public TopologicalSpaceElement map(TopologicalSpaceElement x)
            throws MappingException {
        return f.map(g.map(x));
    }


    public boolean isModuleHomomorphism() {
        return f.isModuleHomomorphism() && g.isModuleHomomorphism();
    }


    public boolean isRingHomomorphism() {
        return f.isRingHomomorphism() && g.isRingHomomorphism();
    }


    public boolean isLinear() {
        return f.isLinear() && g.isLinear();
    }


    public boolean isIdentity() {
        // this should never return true, because of simplifications
        // made in the virtual constructor
        return f.isIdentity() && g.isIdentity();
    }


    public boolean isConstant() {
        // this should never return true, because of simplifications
        // made in the virtual constructor
        return f.isConstant() || g.isConstant();
    }


    public TopologicalSpaceMorphism getRingMorphism() {
        try {
            return f.getRingMorphism().compose(g.getRingMorphism());
        }
        catch (CompositionException e) {
            // this should never occur
            throw new AssertionError(e);
        }
    }



    /**
     * Returns the morphism <i>f</i> of the composition <i>f.g</i>.
     */
    public TopologicalSpaceMorphism getFirstMorphism() {
        return f;
    }


    /**
     * Returns the morphism <i>g</i> of the composition <i>f.g</i>.
     */
    public TopologicalSpaceMorphism getSecondMorphism() {
        return g;
    }


    public int compareTo(TopologicalSpaceMorphism object) {
        if (object instanceof CompositionMorphism) {
            CompositionMorphism m = (CompositionMorphism)object;
            int res = f.compareTo(m.f);
            if (res == 0) {
                return g.compareTo(m.g);
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
        if (object instanceof CompositionMorphism) {
            CompositionMorphism morphism = (CompositionMorphism)object;
            return f.equals(morphism.f) && g.equals(morphism.g);
        }
        else {
            return false;
        }
    }


    public String toString() {
        return "CompositionMorphism["+f.toString()+","+g.toString()+"]";
    }


    private CompositionMorphism(TopologicalSpaceMorphism f, TopologicalSpaceMorphism g) {
        super(g.getDomain(), f.getCodomain());
        this.f = f;
        this.g = g;
    }


    public void toXML(XMLWriter writer) {
        writer.openBlockWithType(TOPOLOGICALSPACEMORPHISM, getElementTypeName());
        f.toXML(writer);
        g.toXML(writer);
        writer.closeBlock();
    }


    public TopologicalSpaceMorphism fromXML(XMLReader reader, Element element) {
        assert(element.getAttribute(TYPE_ATTR).equals(getElementTypeName()));
        Element childElement = XMLReader.getChild(element, TOPOLOGICALSPACEMORPHISM);
        /* FIX reader.parseModuleMorphism();
        if (childElement != null) {
            TopologicalSpaceMorphism f0 = reader.parseModuleMorphism(childElement);
            Element el = XMLReader.getNextSibling(childElement, TOPOLOGICALSPACEMORPHISM);
            if (el == null) {
                reader.setError("Type %%1 is missing second child of type <%2>.", getElementTypeName(), TOPOLOGICALSPACEMORPHISM);
                return null;
            }
            TopologicalSpaceMorphism g0 = reader.parseModuleMorphism(el);
            if (f0 == null || g0 == null) {
                return null;
            }
            try {
                TopologicalSpaceMorphism morphism = make(f0, g0);
                return morphism;
            }
            catch (CompositionException e) {
                reader.setError("Cannot compose morphism %%1 with %%2", f0.toString(), g0.toString());
                return null;
            }
        }
        else {
            reader.setError("Type %%1 is missing children of type <%2>", getElementTypeName(), TOPOLOGICALSPACEMORPHISM);
            return null;
        }*/
        return null;
    }


    private static final XMLInputOutput<TopologicalSpaceMorphism> xmlIO =
            new CompositionMorphism(getIdentityMorphism(RRing.ring), getIdentityMorphism(RRing.ring));

    public static XMLInputOutput<TopologicalSpaceMorphism> getXMLInputOutput() {
        return xmlIO;
    }


    public String getElementTypeName() {
        return "CompositionMorphism";
    }


    private TopologicalSpaceMorphism f;
    private TopologicalSpaceMorphism g;
}
