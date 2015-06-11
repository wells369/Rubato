package org.rubato.math.topology;

import org.rubato.math.arith.Folding;
import org.rubato.math.topology.morphism.TopologicalSpaceMorphism;
import org.rubato.util.TextUtils;
import org.rubato.xml.XMLReader;
import org.rubato.xml.XMLWriter;
import org.rubato.xml.XMLInputOutput;
import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Justin on 6/6/2015.
 */
public final class VectorElement implements TopologicalSpaceElement {

    /**
     * Returns true iff this element is equal to <code>object</code>.
     */
    public boolean equals(Object object) {
        if (object instanceof VectorElement) {
            VectorElement vectorElement = (VectorElement)object;
            if(vectorSpace == null) {
                if(vectorElement.getCoordinate().getValue() == getCoordinate().getValue()) {
                    return true;
                } else {
                    return false;
                }
            } else {
                if(vectorElement.getCoordinate().getValue() == getCoordinate().getValue()
                        && getVectorSpace().equals(vectorElement.getVectorSpace())) {
                    return true;
                } else {
                    return false;
                }
            }

        } else {
            return false;
        }
    }

    /*public double getValue() {
        return getCoordinate().getValue();
    }*/

     public TopologicalSpaceElement cast(TopologicalSpace topologicalSpace) {
         return topologicalSpace.cast(this);
     }

     public void toXML(XMLWriter writer) {
         throw new UnsupportedOperationException("Not implemented");
     }

     public TopologicalSpaceElement fromXML(XMLReader reader, Element element) {
         throw new UnsupportedOperationException("Not implemented");
     }

     public double[] fold(TopologicalSpaceElement[] elements) {
         throw new UnsupportedOperationException("Not implemented");
     }

     public void scale(RingElement element)
             throws DomainException {
         try {
             getCoordinate().scale(element);
         }
         catch (DomainException e) {
             throw new DomainException(this.getTopologicalSpace().getRing(), element.getRing());
         }
     }

     public VectorElement scaled(RingElement element)
             throws DomainException {
         try {
             return new VectorElement(coordinate.scaled(element), vectorSpace, ring);
         }
         catch (DomainException e) {
             throw new DomainException(this.getTopologicalSpace().getRing(), element.getRing());
         }
     }

     public void add(TopologicalSpaceElement element)
             throws DomainException {
         if (element instanceof VectorElement) {
             add((VectorElement)element);
         }
         else {
             throw new DomainException(this.getTopologicalSpace(), element.getTopologicalSpace());
         }
     }

     public void add(VectorElement vectorElement)
            throws DomainException {
         setCoordinate(new RElement(vectorElement.getCoordinate().getValue() + coordinate.getValue()));
     }

     public void negate() {
         coordinate.negate();
     }

     public TopologicalSpaceElement negated() {
         return new VectorElement(coordinate.negated(), vectorSpace, ring);
     }

     public String stringRep(boolean ... parens) {
        if(coordinate == null) {
            return "Is Null";
        } else {
            StringBuilder buf = new StringBuilder(30);
            buf.append(coordinate.stringRep());
            if (parens.length > 0) {
                return TextUtils.parenthesize(buf.toString());
            }
            else {
                return buf.toString();
            }
        }
     }

    public int getLength() { return 1; }

     public VectorElement clone() {
         return new VectorElement(coordinate, vectorSpace, ring);
     }

     public RElement getComponent(int i) {
        return coordinate;
     }

     public TopologicalSpaceElement sum(TopologicalSpaceElement element)
             throws DomainException {
         if(element instanceof VectorElement) {
             return new VectorElement(new RElement(coordinate.getValue()+((VectorElement) element).coordinate.getValue()), vectorSpace, ring);
         } else {
             throw new DomainException(this.getTopologicalSpace(), element.getTopologicalSpace());
         }
     }

     public String getElementTypeName() {
        return "VectorElement";
     }

     public TopologicalSpace getTopologicalSpace() {
         if (vectorSpace == null) {
             vectorSpace = VectorTopologicalSpace.make(1);
         }
         return vectorSpace;
     }

     public TopologicalSpaceElement difference(TopologicalSpaceElement element)
             throws DomainException {
         if(element instanceof VectorElement) {
             return new VectorElement(new RElement(coordinate.getValue()-((VectorElement) element).coordinate.getValue()), vectorSpace, ring);
         } else {
             throw new DomainException(this.getTopologicalSpace(), element.getTopologicalSpace());
         }
     }

     /*public double[] fold(TopologicalSpaceElement[] elements) {
         throw new UnsupportedOperationException("Not implemented");
     }*/

     public int compareTo(TopologicalSpaceElement object) {
         if(object instanceof VectorElement) {
             int val1 = (int)coordinate.getValue();
             int val2 =  (int)((VectorElement) object).getCoordinate().getValue();
             return val1 - val2;
         } else {
             throw new UnsupportedOperationException("Cannot compare different types");
         }
     }

     public void subtract(TopologicalSpaceElement element)
             throws DomainException {
         if(element instanceof VectorElement) {
             setCoordinate(new RElement(coordinate.getValue() - ((VectorElement) element).coordinate.getValue()));
         } else {
             throw new DomainException(this.getTopologicalSpace(), element.getTopologicalSpace());
         }
     }

     public String toString() {
         StringBuilder buf = new StringBuilder(30);
         buf.append("VectorElement[");
         buf.append(coordinate.toString());
         if(vectorSpace != null) {
             buf.append("][");
             buf.append(vectorSpace.toString());
         }
         buf.append("]");
         return buf.toString();
     }

     /*
      * Subtracts this element by the parameter element, returns a VectorElement
      */
     public VectorElement difference(VectorElement vectorElement) {
         return new VectorElement(new RElement(getCoordinate().getValue() - vectorElement.getCoordinate().getValue()));
     }


     /*
      * Subtracts this element by the parameter element, returns a VectorElement
      */
     public VectorElement difference(RElement element) {
         return new VectorElement(new RElement(getCoordinate().getValue() - element.getValue()));
     }


    /*
     * Returns true if it's value is 1
     */
     public boolean isOne() {
         return getCoordinate().isOne();
     }


     /*
      * Returns true if it's value is 0
      */
     public boolean isZero() {
         return getCoordinate().isZero();
     }


    public RElement getCoordinate() { return coordinate; }

    public VectorTopologicalSpace getVectorSpace() { return vectorSpace; }

    public void setCoordinate(RElement element) { coordinate = element; }

    public void setVectorSpace(VectorTopologicalSpace vSpace) { vectorSpace = vSpace; }

    public VectorElement(RElement element) {
        coordinate = element;
        vectorSpace = null;
        ring = null;
    }

    public VectorElement(RElement element, VectorTopologicalSpace vSpace) {
        coordinate = element;
        vectorSpace = vSpace;
        ring = null;
    }

    public  VectorElement(RElement element, VectorTopologicalSpace vSpace, RRing rring) {
        coordinate = element;
        vectorSpace = vSpace;
        ring = rring;
    }

    private RElement coordinate;
    private VectorTopologicalSpace vectorSpace;
    private RRing ring;
}
