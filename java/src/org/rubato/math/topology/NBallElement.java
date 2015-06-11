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
public class NBallElement implements TopologicalSpaceElement {


    public NBallElement cast(TopologicalSpace topologicalSpace) {
        if(topologicalSpace instanceof NBallTopologicalSpace) {
            NBallTopologicalSpace space = (NBallTopologicalSpace) topologicalSpace;
            return space.getCenter().clone();
        }
        else if(topologicalSpace instanceof NSphereTopologicalSpace) {
            NSphereTopologicalSpace space = (NSphereTopologicalSpace) topologicalSpace;
            return new NBallElement(space.getCenter().getCenter());
        }
        else if(topologicalSpace instanceof VectorTopologicalSpace) {
            VectorTopologicalSpace space = (VectorTopologicalSpace) topologicalSpace;
            return new NBallElement(space);
        }
        return null;
    }

    public boolean equals(Object object) {
        if(object instanceof NBallElement) {
            return ((NBallElement) object).getCenter().equals(getCenter());
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

    public NBallElement scaled(RingElement element)
            throws DomainException {
        try {
            NBallElement newElement = clone();
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

    public NBallElement negated() {
        NBallElement newElement = clone();
        newElement.negate();
        return newElement;
    }

    public NBallElement sum(TopologicalSpaceElement element)
            throws DomainException {
        if (element instanceof NBallElement) {
            return sum((NBallElement)element);
        }
        else if (element instanceof  NSphereElement) {
            return sum(new NBallElement(((NSphereElement) element).getCenter()));
        }
        else if (element instanceof  CartProdElement) {
            return sum(new NBallElement(((CartProdElement) element).getVector()));
        }
        else {
            throw new DomainException(this.getTopologicalSpace(), element.getTopologicalSpace());
        }
    }

    public NBallElement sum(NBallElement element)
            throws DomainException {
        if (getCenter().getCoordinates().size() == element.getCenter().getCoordinates().size()) {
            VectorTopologicalSpace vectorSpace = getCenter().sum(element.getCenter());
            return new NBallElement(vectorSpace, getTopologicalSpace());
        }
        else {
            throw new DomainException(this.getTopologicalSpace(), element.getTopologicalSpace());
        }
    }

    public void add(TopologicalSpaceElement element)
            throws DomainException {
        if (element instanceof NBallElement) {
            add((NBallElement)element);
        }
        else if (element instanceof  NSphereElement) {
            add(new NBallElement(((NSphereElement) element).getCenter()));
        }
        else if (element instanceof  CartProdElement) {
            add(new NBallElement(((CartProdElement) element).getVector()));
        }
        else {
            throw new DomainException(this.getTopologicalSpace(), element.getTopologicalSpace());
        }
    }

    public void add(NBallElement element)
            throws DomainException {
        if (getCenter().getCoordinates().size() == element.getCenter().getCoordinates().size()) {
            VectorTopologicalSpace vectorSpace = getCenter().sum(element.getCenter());
            setCenter(vectorSpace);
        }
        else {
            throw new DomainException(this.getTopologicalSpace(), element.getTopologicalSpace());
        }
    }

    public NBallElement difference(TopologicalSpaceElement element)
            throws DomainException {
        if (element instanceof NBallElement) {
            return difference((NBallElement) element);
        }
        else if (element instanceof  NSphereElement) {
            return difference(new NBallElement(((NSphereElement) element).getCenter()));
        }
        else if (element instanceof  CartProdElement) {
            return difference(new NBallElement(((CartProdElement) element).getVector()));
        }
        else {
            throw new DomainException(this.getTopologicalSpace(), element.getTopologicalSpace());
        }
    }

    public NBallElement difference(NBallElement element)
            throws DomainException {
        if (getCenter().getCoordinates().size() == element.getCenter().getCoordinates().size()) {
            VectorTopologicalSpace vectorSpace = getCenter().difference(element.getCenter());
            return new NBallElement(vectorSpace, getTopologicalSpace());
        }
        else {
            throw new DomainException(this.getTopologicalSpace(), element.getTopologicalSpace());
        }
    }

    public void subtract(TopologicalSpaceElement element)
            throws DomainException {
        if (element instanceof NBallElement) {
            subtract((NBallElement) element);
        }
        else if (element instanceof  NSphereElement) {
            subtract(new NBallElement(((NSphereElement)element).getCenter()));
        }
        else if (element instanceof  CartProdElement) {
            subtract(new NBallElement(((CartProdElement)element).getVector()));
        }
        else {
            throw new DomainException(this.getTopologicalSpace(), element.getTopologicalSpace());
        }
    }

    public void subtract(NBallElement element)
            throws DomainException {
        if (getCenter().getCoordinates().size() == element.getCenter().getCoordinates().size()) {
            setCenter(getCenter().difference(element.getCenter()));
        }
        else {
            throw new DomainException(this.getTopologicalSpace(), element.getTopologicalSpace());
        }
    }

    public NBallTopologicalSpace getTopologicalSpace() {
        if (nBallTopologicalSpace == null) {
            nBallTopologicalSpace = new NBallTopologicalSpace(this);
        }
        return nBallTopologicalSpace;
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
        return new NBallElement(getCenter());
    }

    public NBallElement clone() { return new NBallElement(getCenter(), getnBallTopologicalSpace()); }

    public int compareTo(TopologicalSpaceElement object) {
        if(object instanceof NBallElement) {
            return getCenter().compareTo(((NBallElement) object).getCenter());
        } else {
            return toString().compareTo(object.toString());
        }
    }

    public String toString() {
        return getElementTypeName() + "(" + getCenter().toString() + ")";
    }


    public String getElementTypeName() {
        return "NBallElement";
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

    public NBallElement(VectorTopologicalSpace c, NBallTopologicalSpace space) {
        ring = RRing.ring;
        nBallTopologicalSpace = space;
        center = c;
    }

    public NBallElement(VectorTopologicalSpace c) {
        ring = RRing.ring;
        nBallTopologicalSpace = getTopologicalSpace();
        center = c;
    }

    public NBallElement(List<VectorElement> c) {
        ring = RRing.ring;
        nBallTopologicalSpace = getTopologicalSpace();
        center = new VectorTopologicalSpace(c);
    }

    public NBallElement(VectorElement[] c) {
        ring = RRing.ring;
        nBallTopologicalSpace = getTopologicalSpace();
        center = new VectorTopologicalSpace(c);
    }

    public NBallElement(RElement[] c) {
        ring = RRing.ring;
        nBallTopologicalSpace = getTopologicalSpace();
        center = new VectorTopologicalSpace(c);
    }

    public NBallTopologicalSpace getnBallTopologicalSpace() { return nBallTopologicalSpace; }

    public RRing getRing() { return ring; }

    public VectorTopologicalSpace getCenter() { return center; }

    public void setnBallTopologicalSpace(NBallTopologicalSpace space) { nBallTopologicalSpace = space; }

    public void setCenter(VectorTopologicalSpace c) { center = c; }

    private NBallTopologicalSpace nBallTopologicalSpace;
    private RRing ring;
    private VectorTopologicalSpace center;
}
