package org.rubato.math.topology.morphism;

import static org.rubato.xml.XMLConstants.TOPOLOGICALSPACE;
import static org.rubato.xml.XMLConstants.TOPOLOGICALSPACEMORPHISM;
import static org.rubato.xml.XMLConstants.TYPE_ATTR;

import org.rubato.math.topology.TopologicalSpace;
import org.rubato.math.topology.TopologicalSpaceElement;
import org.rubato.math.topology.RRing;
import org.rubato.xml.XMLInputOutput;
import org.rubato.xml.XMLReader;
import org.rubato.xml.XMLWriter;
import org.w3c.dom.Element;

/**
 * Created by Justin on 4/17/2015.
 */
public final class IdentityMorphism extends TopologicalSpaceMorphism {

    /**
     * Creates an identity morphism on the module <code>m</code>.
     */
    public IdentityMorphism(TopologicalSpace m) {
        super(m, m);
    }


    public TopologicalSpaceElement map(TopologicalSpaceElement x)
            throws MappingException {
        if (getDomain().hasElement(x)) {
            return x;
        }
        else {
            throw new MappingException("IdentityMorphism.map: ", x, this);
        }
    }


    public boolean isModuleHomomorphism() {
        return true;
    }


    public boolean isRingHomomorphism() {
        return true;
    }


    public boolean isLinear() {
        return true;
    }


    public boolean isIdentity() {
        return true;
    }


    public boolean isConstant() {
        return false;
    }


    public TopologicalSpaceMorphism getRingMorphism() {
        return getIdentityMorphism(getDomain().getRing());
    }


    public TopologicalSpaceMorphism compose(TopologicalSpaceMorphism morphism)
            throws CompositionException {
        if (composable(this, morphism)) {
            return morphism;
        }
        else {
            return super.compose(morphism);
        }
    }


    public TopologicalSpaceMorphism power(int n) {
        return this;
    }


    public TopologicalSpaceElement atZero() {
        return getCodomain().getZero();
    }


    public int compareTo(TopologicalSpaceMorphism object) {
        if (object instanceof IdentityMorphism) {
            return ((IdentityMorphism)object).getDomain().compareTo(getDomain());
        }
        else {
            return super.compareTo(object);
        }
    }


    public boolean equals(Object object) {
        if (object instanceof IdentityMorphism) {
            return getDomain().equals(((IdentityMorphism)object).getDomain());
        }
        else {
            return false;
        }
    }


    public String toString() {
        return "IdentityMorphism["+getDomain()+"]";
    }


    public void toXML(XMLWriter writer) {
        writer.openBlockWithType(TOPOLOGICALSPACEMORPHISM, getElementTypeName());
        getDomain().toXML(writer);
        writer.closeBlock();
    }


    public TopologicalSpaceMorphism fromXML(XMLReader reader, Element element) {
        assert(element.getAttribute(TYPE_ATTR).equals(getElementTypeName()));
        Element childElement = XMLReader.getChild(element, TOPOLOGICALSPACE);
        /* FIX reader.parseModule();
        if (childElement != null) {
            TopologicalSpace module = reader.parseModule(childElement);
            if (module == null) {
                return null;
            }
            return new IdentityMorphism(module);
        }
        else {
            reader.setError("Type %%1 is missing child of type <%2>.", getElementTypeName(), TOPOLOGICALSPACE);
            return null;
        }*/
        return null;
    }


    private static final XMLInputOutput<TopologicalSpaceMorphism> xmlIO =
            new IdentityMorphism(RRing.ring);

    public static XMLInputOutput<TopologicalSpaceMorphism> getXMLInputOutput() {
        return xmlIO;
    }


    public String getElementTypeName() {
        return "IdentityMorphism";
    }
}
