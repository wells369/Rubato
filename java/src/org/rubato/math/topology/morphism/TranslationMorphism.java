package org.rubato.math.topology.morphism;

import static org.rubato.xml.XMLConstants.*;

import org.rubato.math.topology.*;
import org.rubato.xml.XMLInputOutput;
import org.rubato.xml.XMLReader;
import org.rubato.xml.XMLWriter;
import org.w3c.dom.Element;

/**
 * Created by Justin on 4/14/2015.
 */
public final class TranslationMorphism extends TopologicalSpaceMorphism {
    /**
     * Create a morphism in module <code>module</code> translated by <code>element</code>.
     * The resulting morphism <i>h</i> is such that <i>h(x) = x+element</i>.
     *
     * @return null if translation is not valid
     */
    static public TopologicalSpaceMorphism make(TopologicalSpace module, TopologicalSpaceElement element) {
        if (!module.hasElement(element)) {
            return null;
        }
        else {
            return new TranslationMorphism(module, element);
        }
    }


    public TopologicalSpaceElement map(TopologicalSpaceElement x)
            throws MappingException {
        if (getDomain().hasElement(x)) {
            try {
                return x.sum(translate);
            }
            catch (DomainException e) {
                throw new MappingException("TranslationMorphism.map: ", x, this);
            }
        }
        else {
            throw new MappingException("TranslationMorphism.map: ", x, this);
        }
    }


    public boolean isModuleHomomorphism() {
        return true;
    }



    public boolean isRingHomomorphism() {
        return getDomain().isRing() && translate.isZero();
    }


    public boolean isLinear() {
        return translate.isZero();
    }


    public boolean isIdentity() {
        return translate.isZero();
    }


    public TopologicalSpaceMorphism getRingMorphism() {
        return getIdentityMorphism(getDomain().getRing());
    }


    /**
     * Returns the translate <i>t</i> of <i>h(x) = x+t</i>.
     */
    public TopologicalSpaceElement getTranslate() {
        return translate;
    }


    public int compareTo(TopologicalSpaceMorphism object) {
        if (object instanceof TranslationMorphism) {
            TranslationMorphism morphism = (TranslationMorphism)object;
            return translate.compareTo(morphism.translate);
        }
        else {
            return super.compareTo(object);
        }
    }


    public boolean equals(Object object) {
        if (object instanceof TranslationMorphism) {
            TranslationMorphism morphism = (TranslationMorphism)object;
            return translate.equals(morphism.translate);
        }
        else {
            return false;
        }
    }


    public String toString() {
        return "TranslationMorphism["+translate+"]";
    }


    public void toXML(XMLWriter writer) {
        writer.openBlockWithType(TOPOLOGICALSPACEMORPHISM, getElementTypeName());
        getDomain().toXML(writer);
        translate.toXML(writer);
        writer.closeBlock();
    }


    public TopologicalSpaceMorphism fromXML(XMLReader reader, Element element) {
        assert(element.getAttribute(TYPE_ATTR).equals(getElementTypeName()));
        Element childElement = XMLReader.getChild(element, MODULE);
        /* FIX reader.parseModule()
        if (childElement != null) {
            TopologicalSpace f = reader.parseModule(childElement);
            Element el = XMLReader.getNextSibling(childElement, MODULEELEMENT);
            if (el == null) {
                reader.setError("Type %%1 is missing second child of type <%2>.", getElementTypeName(), MODULEELEMENT);
                return null;
            }
            TopologicalSpaceElement trslte = reader.parseModuleElement(el);
            if (f == null || trslte == null) {
                return null;
            }
            try {
                TopologicalSpaceMorphism morphism = make(f, trslte);
                return morphism;
            }
            catch (IllegalArgumentException e) {
                reader.setError(e.getMessage());
                return null;
            }
        }
        else {
            reader.setError("Type %%1 is missing child of type <%2>.", getElementTypeName(), MODULE);
            return null;
        }*/
        return null;
    }


    private static final XMLInputOutput<TopologicalSpaceMorphism> xmlIO =
            new TranslationMorphism(RRing.ring, new RElement(0));


    public static XMLInputOutput<TopologicalSpaceMorphism> getXMLInputOutput() {
        return xmlIO;
    }


    public String getElementTypeName() {
        return "TranslationMorphism";
    }


    private TranslationMorphism(TopologicalSpace domain, TopologicalSpaceElement translate) {
        super(domain, domain);
        this.translate = translate;
    }


    private TopologicalSpaceElement translate;
}
