package org.rubato.math.topology.morphism;

import static org.rubato.xml.XMLConstants.MODULEELEMENT;
import static org.rubato.xml.XMLConstants.MODULEMORPHISM;
import static org.rubato.xml.XMLConstants.TYPE_ATTR;

import org.rubato.math.topology.*;
import org.rubato.math.topology.TopologicalSpaceElement;
import org.rubato.xml.XMLInputOutput;
import org.rubato.xml.XMLReader;
import org.rubato.xml.XMLWriter;
import org.w3c.dom.Element;

/**
 * Polynomial mappings.
 *
 * @author Gérard Milmeister
 */
public class PolynomialMorphism extends TopologicalSpaceMorphism {

    /**
     * Creates a mapping which evaluates the given <code>polynomial</code>.
     */
    public PolynomialMorphism(PolynomialElement polynomial) {
        super(polynomial.getRing().getCoefficientRing(),
                polynomial.getRing().getCoefficientRing());
        this.polynomial = polynomial;
    }


    public TopologicalSpaceElement map(TopologicalSpaceElement x)
            throws MappingException {
        if (x instanceof RingElement) {
            try {
                return polynomial.evaluate((RingElement)x);
            }
            catch (DomainException e) {
                throw new MappingException("PolynomialMorphism.map: ", x, this);
            }
        }
        else {
            throw new MappingException("PolynomialMorphism.map: ", x, this);
        }
    }


    public boolean isModuleHomomorphism() {
        return polynomial.getDegree() == 1;
    }


    public boolean isRingHomomorphism() {
        return isIdentity() || polynomial.isZero();
    }


    public boolean isLinear() {
        return polynomial.getDegree() == 1
                && polynomial.getCoefficient(0).isZero();
    }


    public boolean isIdentity() {
        return polynomial.getDegree() == 1
                && polynomial.getCoefficient(0).isZero()
                && polynomial.getCoefficient(1).isOne();
    }


    public boolean isConstant() {
        return polynomial.getDegree() == 0;
    }


    /**
     * Returns the mapping's polynomial.
     */
    public PolynomialElement getPolynomial() {
        return polynomial;
    }


    public TopologicalSpaceMorphism getRingMorphism() {
        return getIdentityMorphism(getDomain().getRing());
    }


    public int compareTo(TopologicalSpaceMorphism object) {
        if (object instanceof PolynomialMorphism) {
            PolynomialMorphism morphism = (PolynomialMorphism)object;
            return polynomial.compareTo(morphism.polynomial);
        }
        else {
            return super.compareTo(object);
        }
    }


    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        else if (object instanceof PolynomialMorphism) {
            PolynomialMorphism morphism = (PolynomialMorphism)object;
            return polynomial.equals(morphism.polynomial);
        }
        else {
            return false;
        }
    }


    public String toString() {
        return "PolynomialMorphism["+polynomial+"]";
    }


    public void toXML(XMLWriter writer) {
        writer.openBlockWithType(MODULEMORPHISM, getElementTypeName());
        getPolynomial().toXML(writer);
        writer.closeBlock();
    }


    public TopologicalSpaceMorphism fromXML(XMLReader reader, Element element) {
        /*
        assert(element.getAttribute(TYPE_ATTR).equals(getElementTypeName()));
        Element childElement = XMLReader.getChild(element, MODULEELEMENT);
        if (childElement != null) {
            TopologicalSpaceElement moduleElement = reader.parseModuleElement(childElement);
            if (moduleElement == null) {
                return null;
            }
            if (moduleElement instanceof PolynomialElement) {
                return new PolynomialMorphism((PolynomialElement)moduleElement);
            }
            else {
                reader.setError("Type %%1 is missing child of type %%2.", getElementTypeName(), "PolynomialElement");
                return null;
            }
        }
        else {
            reader.setError("Type %%1 is missing child of type <%2>.", getElementTypeName(), MODULEELEMENT);
            return null;
        }*/
        return null;
    }


    private static final XMLInputOutput<TopologicalSpaceMorphism> xmlIO =
            new PolynomialMorphism(new PolynomialElement("X", new RElement(0)));

    public static XMLInputOutput<TopologicalSpaceMorphism> getXMLInputOutput() {
        return xmlIO;
    }


    public String getElementTypeName() {
        return "PolynomialMorphism";
    }


    private PolynomialElement polynomial;
}
