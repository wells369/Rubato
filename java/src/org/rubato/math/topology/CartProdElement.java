package org.rubato.math.topology;

import org.rubato.util.TextUtils;
import org.rubato.xml.XMLReader;
import org.rubato.xml.XMLWriter;
import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Justin on 6/9/2015.
 */
public final class CartProdElement implements TopologicalSpaceElement {


    public CartProdElement cast(TopologicalSpace topologicalSpace) {
        if(topologicalSpace instanceof NBallTopologicalSpace) {
            NBallTopologicalSpace space = (NBallTopologicalSpace) topologicalSpace;
            return new CartProdElement(space.getCenter().getCenter());
        }
        else if(topologicalSpace instanceof NSphereTopologicalSpace) {
            NSphereTopologicalSpace space = (NSphereTopologicalSpace) topologicalSpace;
            return new CartProdElement(space.getCenter().getCenter());
        }
        else if(topologicalSpace instanceof VectorTopologicalSpace) {
            VectorTopologicalSpace space = (VectorTopologicalSpace) topologicalSpace;
            return new CartProdElement(space);
        }
        return null;
    }

    public int compareTo(TopologicalSpaceElement object) {
        if(object instanceof CartProdElement) {
            return getVector().compareTo(((NSphereElement) object).getCenter());
        } else {
            return toString().compareTo(object.toString());
        }
    }

    public boolean equals(Object object) {
        if(object instanceof CartProdElement) {
            return ((CartProdElement) object).getVector().equals(getVector());
        }
        return false;
    }

    public void scale(RingElement element)
            throws DomainException {
        try {
            List<VectorElement> coordinateList = getVector().getCoordinates();
            List<VectorElement> newList = new ArrayList<VectorElement>();
            for(int i=0; i<coordinateList.size(); i++) {
                newList.add(coordinateList.get(i).scaled(element));
            }
            setVector(new VectorTopologicalSpace(newList));
        }
        catch (DomainException e) {
            throw new DomainException(this.getTopologicalSpace().getRing(), element.getRing());
        }
    }

    public CartProdElement scaled(RingElement element)
            throws DomainException {
        try {
            CartProdElement newElement = clone();
            newElement.scale(element);
            return newElement;
        }
        catch (DomainException e) {
            throw new DomainException(this.getTopologicalSpace().getRing(), element.getRing());
        }
    }

    public void negate() {
        List<VectorElement> coordinateList = getVector().getCoordinates();
        for(int i=0; i<coordinateList.size(); i++) {
            coordinateList.get(i).negate();
        }
        setVector(new VectorTopologicalSpace(coordinateList));
    }

    public CartProdElement negated() {
        CartProdElement newElement = clone();
        newElement.negate();
        return newElement;
    }

    public CartProdElement sum(TopologicalSpaceElement element)
            throws DomainException {
        if (element instanceof CartProdElement) {
            return sum((CartProdElement)element);
        }
        else if (element instanceof  NSphereElement) {
            return sum(new CartProdElement(((NSphereElement) element).getCenter()));
        }
        else if (element instanceof  NBallElement) {
            return sum(new CartProdElement(((NBallElement) element).getCenter()));
        }
        else {
            throw new DomainException(this.getTopologicalSpace(), element.getTopologicalSpace());
        }
    }

    public CartProdElement sum(CartProdElement element)
            throws DomainException {
        if (getVector().getCoordinates().size() == element.getVector().getCoordinates().size()) {
            VectorTopologicalSpace vectorSpace = getVector().sum(element.getVector());
            return new CartProdElement(vectorSpace, getTopologicalSpace());
        }
        else {
            throw new DomainException(this.getTopologicalSpace(), element.getTopologicalSpace());
        }
    }

    public void add(TopologicalSpaceElement element)
            throws DomainException {
        if (element instanceof CartProdElement) {
            add((CartProdElement) element);
        }
        else if (element instanceof  NSphereElement) {
            add(new CartProdElement(((NSphereElement) element).getCenter()));
        }
        else if (element instanceof  NBallElement) {
            add(new CartProdElement(((NBallElement) element).getCenter()));
        }
        else {
            throw new DomainException(this.getTopologicalSpace(), element.getTopologicalSpace());
        }
    }

    public void add(CartProdElement element)
            throws DomainException {
        if (getVector().getCoordinates().size() == element.getVector().getCoordinates().size()) {
            VectorTopologicalSpace vectorSpace = getVector().sum(element.getVector());
            setVector(vectorSpace);
        }
        else {
            throw new DomainException(this.getTopologicalSpace(), element.getTopologicalSpace());
        }
    }

    public CartProdElement difference(TopologicalSpaceElement element)
            throws DomainException {
        if (element instanceof CartProdElement) {
            return difference((CartProdElement) element);
        }
        else if (element instanceof  NSphereElement) {
            return difference(new CartProdElement(((NSphereElement) element).getCenter()));
        }
        else if (element instanceof  NBallElement) {
            return difference(new CartProdElement(((NBallElement) element).getCenter()));
        }
        else {
            throw new DomainException(this.getTopologicalSpace(), element.getTopologicalSpace());
        }
    }

    public CartProdElement difference(CartProdElement element)
            throws DomainException {
        if (getVector().getCoordinates().size() == element.getVector().getCoordinates().size()) {
            VectorTopologicalSpace vectorSpace = getVector().difference(element.getVector());
            return new CartProdElement(vectorSpace, getTopologicalSpace());
        }
        else {
            throw new DomainException(this.getTopologicalSpace(), element.getTopologicalSpace());
        }
    }

    public void subtract(TopologicalSpaceElement element)
            throws DomainException {
        if (element instanceof CartProdElement) {
            subtract((CartProdElement) element);
        }
        else if (element instanceof  NSphereElement) {
            subtract(new CartProdElement(((NSphereElement) element).getCenter()));
        }
        else if (element instanceof  NBallElement) {
            subtract(new CartProdElement(((NBallElement) element).getCenter()));
        }
        else {
            throw new DomainException(this.getTopologicalSpace(), element.getTopologicalSpace());
        }
    }

    public void subtract(CartProdElement element)
            throws DomainException {
        if (getVector().getCoordinates().size() == element.getVector().getCoordinates().size()) {
            setVector(getVector().difference(element.getVector()));
        }
        else {
            throw new DomainException(this.getTopologicalSpace(), element.getTopologicalSpace());
        }
    }

    public CartProdTopologicalSpace getTopologicalSpace() {
        if (cartProdSpace == null) {
            List<CartProdElement> vectorList = new ArrayList<CartProdElement>();
            vectorList.add(this);
            cartProdSpace = new CartProdTopologicalSpace(vectorList);
        }
        return cartProdSpace;
    }

    public int getLength() { return getVector().getDimension(); }

    public CartProdElement getComponent(int i) { return this; }

    public CartProdElement clone() {
        CartProdElement newElement = new CartProdElement(getVector(), getTopologicalSpace());
        return newElement;
    }

    public boolean isZero() { return getVector().isZero(); }

    public String toString() {
        return "CartProdElement[" + getVector().toVisualString() + "]";
    }

    public String stringRep(boolean ... parens) {
        if(getVector() == null) {
            return "Is Null";
        } else {
            List<VectorElement> vector = getVector().getCoordinates();
            StringBuilder buf = new StringBuilder(50);
            buf.append("<");
            for(int i=0; i<vector.size(); i++) {
                buf.append("("+vector.get(i).getCoordinate().getValue()+")");
                if(i != vector.size()-1) {
                    buf.append(", ");
                }
            }
            buf.append(">");
            if (parens.length > 0) {
                return TextUtils.parenthesize(buf.toString());
            }
            else {
                return buf.toString();
            }
        }
    }

    public String getElementTypeName() { return "CartProdElement"; }

    public int hashCode() {
        double res = 0;
        int num = 1;
        for(int i=0; i<getVector().getCoordinates().size(); i++) {
            res += num * getVector().getCoordinates().get(i).getCoordinate().getValue();
        }
        return (int)res;
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

    public CartProdElement(VectorTopologicalSpace v) {
        vector = v;
        cartProdSpace = getTopologicalSpace();
    }

    public CartProdElement(VectorTopologicalSpace v, CartProdTopologicalSpace space) {
        vector = v;
        cartProdSpace = space;
    }

    public VectorTopologicalSpace getVector() { return vector; }

    public CartProdTopologicalSpace getCartProdSpace() { return cartProdSpace; }

    public RRing getRing() { return RRing.ring; }

    public void setVector(VectorTopologicalSpace v) { vector = v; }

    public void setCartProdSpace(CartProdTopologicalSpace space) { cartProdSpace = space; }

    private VectorTopologicalSpace vector;
    private CartProdTopologicalSpace cartProdSpace;
}
