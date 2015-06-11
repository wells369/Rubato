package org.rubato.math.topology.morphism;

import static org.rubato.xml.XMLConstants.TOPOLOGICALSPACEMORPHISM;
import static org.rubato.xml.XMLConstants.TYPE_ATTR;

import org.rubato.math.topology.DomainException;
import org.rubato.math.topology.RRing;
import org.rubato.math.topology.TopologicalSpaceElement;
import org.rubato.xml.XMLInputOutput;
import org.rubato.xml.XMLReader;
import org.rubato.xml.XMLWriter;
import org.w3c.dom.Element;

/**
 * Created by Justin on 4/19/2015.
 */
public final class DifferenceMorphism extends TopologicalSpaceMorphism {

    /**
     * Creates a morphism from <code>f</code> and <code>g</code>.
     * The resulting morphism h is such that h(x) = f(x)-g(x).
     *
     * @throws CompositionException if difference is not valid
     */
    public static TopologicalSpaceMorphism make(TopologicalSpaceMorphism f, TopologicalSpaceMorphism g)
            throws CompositionException {
        if (!f.getDomain().equals(g.getDomain()) ||
                !f.getCodomain().equals(g.getCodomain())) {
            throw new CompositionException("DifferenceMorphism.make: Cannot subtract "+g+" from "+f);
        }
        else if (f.isConstant() && g.isConstant()) {
            try {
                TopologicalSpaceElement zero = f.getDomain().getZero();
                TopologicalSpaceElement fe = f.map(zero);
                TopologicalSpaceElement ge = g.map(zero);
                return new ConstantMorphism(fe.difference(ge));
            }
            catch (DomainException e) {
                throw new AssertionError("This should never happen!");
            }
            catch (MappingException e) {
                throw new AssertionError("This should never happen!");
            }
        }
        else {
            return new DifferenceMorphism(f, g);
        }
    }


    private DifferenceMorphism(TopologicalSpaceMorphism f, TopologicalSpaceMorphism g) {
        super(f.getDomain(), f.getCodomain());
        this.f = f;
        this.g = g;
    }


    public TopologicalSpaceElement map(TopologicalSpaceElement x)
            throws MappingException {
        try {
            return f.map(x).difference(g.map(x));
        }
        catch (DomainException e) {
            throw new MappingException("DifferenceMorphism.map: ", x, this);
        }
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


    /**
     * Returns the morphism <i>f</i> of the difference <i>f-g</i>.
     */
    public TopologicalSpaceMorphism getFirstMorphism() {
        return f;
    }


    /**
     * Returns the morphism <i>g</i> of the difference <i>f-g</i>.
     */
    public TopologicalSpaceMorphism getSecondMorphism() {
        return g;
    }


    public TopologicalSpaceMorphism getRingMorphism() {
        return f.getRingMorphism();
    }


    public int compareTo(TopologicalSpaceMorphism object) {
        if (object instanceof DifferenceMorphism) {
            DifferenceMorphism morphism = (DifferenceMorphism)object;
            int comp = f.compareTo(morphism.f);
            if (comp == 0) {
                return g.compareTo(morphism.g);
            }
            else {
                return comp;
            }
        }
        else {
            return super.compareTo(object);
        }
    }


    public boolean equals(Object object) {
        if (object instanceof DifferenceMorphism) {
            DifferenceMorphism m = (DifferenceMorphism)object;
            return f.equals(m.f) && g.equals(m.g);
        }
        else {
            return false;
        }
    }


    public String toString() {
        return "DifferenceMorphism["+f+","+g+"]";
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
        /* FIX reader.parseModuleMorphism()
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
                TopologicalSpaceMorphism morphism = DifferenceMorphism.make(f0, g0);
                return morphism;
            }
            catch (CompositionException e) {
                reader.setError("Cannot take the difference of the two morphisms %1 and %2.", f0.toString(), g0.toString());
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
            new DifferenceMorphism(getIdentityMorphism(RRing.ring), getIdentityMorphism(RRing.ring));

    public static XMLInputOutput<TopologicalSpaceMorphism> getXMLInputOutput() {
        return xmlIO;
    }


    public String getElementTypeName() {
        return "DifferenceMorphism";
    }


    private TopologicalSpaceMorphism f;
    private TopologicalSpaceMorphism g;
}
