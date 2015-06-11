package org.rubato.math.topology.morphism;

import static org.rubato.xml.XMLConstants.*;

import org.rubato.math.topology.*;
import org.rubato.xml.XMLInputOutput;
import org.rubato.xml.XMLReader;
import org.rubato.xml.XMLWriter;
import org.w3c.dom.Element;

/**
 * Created by Justin on 4/17/2015.
 */
public final class ConstantMorphism extends TopologicalSpaceMorphism {

    /**
     * Creates a constant morphism with value <code>v</code>
     * and domain <code>m</code>.
     */
    public ConstantMorphism(TopologicalSpace m, TopologicalSpaceElement value) {
        super(m, value.getTopologicalSpace());
        this.value = value;
    }


    /**
     * Creates a constant morphism with value <code>v</code>.
     * The domain is the same as the codomain.
     */
    public ConstantMorphism(TopologicalSpaceElement value) {
        super(value.getTopologicalSpace(), value.getTopologicalSpace());
        this.value = value;
    }


    public TopologicalSpaceElement map(TopologicalSpaceElement x) {
        return value;
    }


    public boolean isModuleHomomorphism() {
        return true;
    }


    public boolean isRingHomomorphism() {
        return value.isZero();
    }


    public boolean isLinear() {
        return value.isZero();
    }


    public boolean isConstant() {
        return true;
    }

    /*
     * Cannot use in TopologicalSpace package
     */
    public TopologicalSpaceMorphism getRingMorphism() {
        /*Ring domainRing = getDomain().getRing();
        Ring codomainRing = getCodomain().getRing();
        return CanonicalMorphism.make(domainRing, codomainRing);*/
        return null;
    }


    public TopologicalSpaceElement atZero() {
        return getValue();
    }


    /**
     * Returns the constant value.
     */
    public TopologicalSpaceElement getValue() {
        return value;
    }


    public int compareTo(TopologicalSpaceMorphism object) {
        if (object instanceof ConstantMorphism) {
            ConstantMorphism cm = (ConstantMorphism)object;
            int comp = getDomain().compareTo(cm.getDomain());
            if (comp == 0) {
                return value.compareTo(cm.getValue());
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
        if (object instanceof ConstantMorphism) {
            ConstantMorphism cm = (ConstantMorphism)object;
            return getDomain().equals(cm.getDomain())
                    && value.equals(cm.getValue());
        }
        else {
            return false;
        }
    }


    public String toString() {
        return "ConstantMorphism["+getValue()+"]";
    }


    public void toXML(XMLWriter writer) {
        writer.openBlockWithType(TOPOLOGICALSPACEMORPHISM, getElementTypeName());
        if (!getDomain().equals(getCodomain())) {
            getDomain().toXML(writer);
        }
        value.toXML(writer);
        writer.closeBlock();
    }


    public TopologicalSpaceMorphism fromXML(XMLReader reader, Element element) {
        assert(element.getAttribute(TYPE_ATTR).equals(getElementTypeName()));
        TopologicalSpace domain = null;
        /* FIX reader.parseModuleMorphism();
        Element childElement = XMLReader.getChild(element, MODULE);
        if (childElement != null) {
            domain = reader.parseModule(childElement);
            if (domain == null) {
                return null;
            }
            childElement = XMLReader.getNextSibling(childElement, MODULEELEMENT);
        }
        else {
            childElement = XMLReader.getChild(element, MODULEELEMENT);
        }

        if (childElement != null) {
            TopologicalSpaceElement moduleElement = reader.parseModuleElement(childElement);
            if (moduleElement == null) {
                return null;
            }
            if (domain == null) {
                return new ConstantMorphism(moduleElement);
            }
            else {
                return new ConstantMorphism(domain, moduleElement);
            }
        }
        else {
            reader.setError("Type %%1 is missing child of type <%2>.", getElementTypeName(), MODULEELEMENT);
            return null;
        }*/
        return null;
    }


    private static final XMLInputOutput<TopologicalSpaceMorphism> xmlIO = new ConstantMorphism(new RElement(0));

    public static XMLInputOutput<TopologicalSpaceMorphism> getXMLInputOutput() {
        return xmlIO;
    }


    public String getElementTypeName() {
        return "ConstantMorphism";
    }


    private TopologicalSpaceElement value;
}

