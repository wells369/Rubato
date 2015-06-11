package org.rubato.math.topology;

import static org.rubato.xml.XMLConstants.*;

import org.rubato.util.TextUtils;
import org.rubato.xml.XMLWriter;
import org.rubato.xml.XMLReader;
import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Justin on 5/10/2015.
 */
public class NSphereElement implements TopologicalSpaceElement {


    public NSphereElement cast(TopologicalSpace topologicalSpace) {
        if(topologicalSpace instanceof NBallTopologicalSpace) {
            NBallTopologicalSpace space = (NBallTopologicalSpace) topologicalSpace;
            return new NSphereElement(space.getCenter().getCenter());
        }
        else if(topologicalSpace instanceof NSphereTopologicalSpace) {
            NSphereTopologicalSpace space = (NSphereTopologicalSpace) topologicalSpace;
            return space.getCenter().clone();
        }
        else if(topologicalSpace instanceof VectorTopologicalSpace) {
            VectorTopologicalSpace space = (VectorTopologicalSpace) topologicalSpace;
            return new NSphereElement(space);
        }
        return null;
    }

    public boolean equals(Object object) {
        if(object instanceof NSphereElement) {
            return ((NSphereElement) object).getCenter().equals(getCenter());
        }
        return false;
    }

    public void scale(RingElement element)
            throws DomainException {
        try {
            List<VectorElement> coordinateList = getCenter().getCoordinates();
            List<VectorElement> newList = new ArrayList<VectorElement>();
            for(int i=0; i<coordinateList.size(); i++) {
                newList.add(coordinateList.get(i).scaled(element));
            }
            setCenter(new VectorTopologicalSpace(newList));
        }
        catch (DomainException e) {
            throw new DomainException(this.getTopologicalSpace().getRing(), element.getRing());
        }
    }

    public NSphereElement scaled(RingElement element)
            throws DomainException {
        try {
            NSphereElement newElement = clone();
            newElement.scale(element);
            return newElement;
        }
        catch (DomainException e) {
            throw new DomainException(this.getTopologicalSpace().getRing(), element.getRing());
        }
    }

    public void negate() {
        List<VectorElement> coordinateList = getCenter().getCoordinates();
        for(int i=0; i<coordinateList.size(); i++) {
            coordinateList.get(i).negate();
        }
        setCenter(new VectorTopologicalSpace(coordinateList));
    }

    public NSphereElement negated() {
        NSphereElement newElement = clone();
        newElement.negate();
        return newElement;
    }

    public NSphereElement sum(TopologicalSpaceElement element)
            throws DomainException {
        if (element instanceof NSphereElement) {
            return sum((NSphereElement)element);
        }
        else if (element instanceof  NBallElement) {
            return sum(new NSphereElement(((NBallElement)element).getCenter()));
        }
        else if (element instanceof  CartProdElement) {
            return sum(new NSphereElement(((CartProdElement)element).getVector()));
        }
        else {
            throw new DomainException(this.getTopologicalSpace(), element.getTopologicalSpace());
        }
    }

    public NSphereElement sum(NSphereElement element)
            throws DomainException {
        if (getCenter().getCoordinates().size() == element.getCenter().getCoordinates().size()) {
            VectorTopologicalSpace vectorSpace = getCenter().sum(element.getCenter());
            return new NSphereElement(vectorSpace, getTopologicalSpace());
        }
        else {
            throw new DomainException(this.getTopologicalSpace(), element.getTopologicalSpace());
        }
    }

    public void add(TopologicalSpaceElement element)
            throws DomainException {
        if (element instanceof NSphereElement) {
            add((NSphereElement)element);
        }
        else if (element instanceof  NBallElement) {
            add(new NSphereElement(((NBallElement)element).getCenter()));
        }
        else if (element instanceof  CartProdElement) {
            add(new NSphereElement(((CartProdElement)element).getVector()));
        }
        else {
            throw new DomainException(this.getTopologicalSpace(), element.getTopologicalSpace());
        }
    }

    public void add(NSphereElement element)
            throws DomainException {
        if (getCenter().getCoordinates().size() == element.getCenter().getCoordinates().size()) {
            VectorTopologicalSpace vectorSpace = getCenter().sum(element.getCenter());
            setCenter(vectorSpace);
        }
        else {
            throw new DomainException(this.getTopologicalSpace(), element.getTopologicalSpace());
        }
    }

    public NSphereElement difference(TopologicalSpaceElement element)
            throws DomainException {
        if (element instanceof NSphereElement) {
            return difference((NSphereElement) element);
        }
        else if (element instanceof  NBallElement) {
            return difference(new NSphereElement(((NBallElement) element).getCenter()));
        }
        else if (element instanceof  CartProdElement) {
            return difference(new NSphereElement(((CartProdElement) element).getVector()));
        }
        else {
            throw new DomainException(this.getTopologicalSpace(), element.getTopologicalSpace());
        }
    }

    public NSphereElement difference(NSphereElement element)
            throws DomainException {
        if (getCenter().getCoordinates().size() == element.getCenter().getCoordinates().size()) {
            VectorTopologicalSpace vectorSpace = getCenter().difference(element.getCenter());
            return new NSphereElement(vectorSpace, getTopologicalSpace());
        }
        else {
            throw new DomainException(this.getTopologicalSpace(), element.getTopologicalSpace());
        }
    }

    public void subtract(TopologicalSpaceElement element)
            throws DomainException {
        if (element instanceof NSphereElement) {
            subtract((NSphereElement) element);
        }
        else if (element instanceof  NBallElement) {
            subtract(new NSphereElement(((NBallElement) element).getCenter()));
        }
        else if (element instanceof  CartProdElement) {
            subtract(new NSphereElement(((CartProdElement) element).getVector()));
        }
        else {
            throw new DomainException(this.getTopologicalSpace(), element.getTopologicalSpace());
        }
    }

    public void subtract(NSphereElement element)
            throws DomainException {
        if (getCenter().getCoordinates().size() == element.getCenter().getCoordinates().size()) {
            setCenter(getCenter().difference(element.getCenter()));
        }
        else {
            throw new DomainException(this.getTopologicalSpace(), element.getTopologicalSpace());
        }
    }

    public NSphereTopologicalSpace getTopologicalSpace() {
        if (nSphereTopologicalSpace == null) {
            nSphereTopologicalSpace = new NSphereTopologicalSpace(this);
        }
        return nSphereTopologicalSpace;
    }

    public int getLength() { return getCenter().getDimension(); }

    public String stringRep(boolean ... parens) {
        List<VectorElement> coordinateList = getCenter().getCoordinates();
        if(coordinateList == null || coordinateList.size() == 0) {
            return "Null";
        }
        StringBuilder buf = new StringBuilder(30);
        buf.append(coordinateList.get(0).stringRep());
        for (int i = 1; i < coordinateList.size(); i++) {
            buf.append(",");
            buf.append(coordinateList.get(i).stringRep());
        }
        if (parens.length > 0) {
            return TextUtils.parenthesize(buf.toString());
        }
        else {
            return buf.toString();
        }
    }

    public boolean isZero() {
        return (getCenter().getDimension() == 0);
    }

    public TopologicalSpaceElement getComponent(int i) {
        return new NSphereElement(getCenter());
    }

    public NSphereElement clone() { return new NSphereElement(getCenter(), getnSphereTopologicalSpace()); }

    public int compareTo(TopologicalSpaceElement object) {
        if(object instanceof NSphereElement) {
            return getCenter().compareTo(((NSphereElement) object).getCenter());
        } else {
            return toString().compareTo(object.toString());
        }
    }

    public String toString() {
        return getElementTypeName() + "(" + getCenter().toString() + ")";
    }


    public String getElementTypeName() {
        return "NSphereElement";
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

    public NSphereElement(VectorTopologicalSpace c, NSphereTopologicalSpace space) {
        ring = RRing.ring;
        nSphereTopologicalSpace = space;
        center = c;
    }

    public NSphereElement(VectorTopologicalSpace c) {
        ring = RRing.ring;
        nSphereTopologicalSpace = getTopologicalSpace();
        center = c;
    }

    public NSphereElement(List<VectorElement> c) {
        ring = RRing.ring;
        nSphereTopologicalSpace = getTopologicalSpace();
        center = new VectorTopologicalSpace(c);
    }

    public NSphereElement(VectorElement[] c) {
        ring = RRing.ring;
        nSphereTopologicalSpace = getTopologicalSpace();
        center = new VectorTopologicalSpace(c);
    }

    public NSphereElement(RElement[] c) {
        ring = RRing.ring;
        nSphereTopologicalSpace = getTopologicalSpace();
        center = new VectorTopologicalSpace(c);
    }

    public NSphereTopologicalSpace getnSphereTopologicalSpace() { return nSphereTopologicalSpace; }

    public RRing getRing() { return ring; }

    public VectorTopologicalSpace getCenter() { return center; }

    public void setnSphereTopologicalSpace(NSphereTopologicalSpace space) { nSphereTopologicalSpace = space; }

    public void setCenter(VectorTopologicalSpace c) { center = c; }

    private NSphereTopologicalSpace nSphereTopologicalSpace;
    private RRing ring;
    private VectorTopologicalSpace center;
}
