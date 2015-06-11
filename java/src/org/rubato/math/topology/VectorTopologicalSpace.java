package org.rubato.math.topology;

import org.rubato.math.arith.Folding;
import org.rubato.math.topology.morphism.TopologicalSpaceMorphism;
import org.rubato.math.topology.morphism.TranslationMorphism;
import org.rubato.util.TextUtils;
import org.rubato.xml.XMLReader;
import org.rubato.xml.XMLWriter;
import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Justin on 6/6/2015.
 */
public final class VectorTopologicalSpace implements TopologicalSpace {


    public static VectorTopologicalSpace make(List<VectorElement> list, int dim) {
        if (dim > 0) {
            return new VectorTopologicalSpace(dim, list);
        } else {
            return null;
        }
    }

    public static VectorTopologicalSpace make(int dim) {
        if (dim > 0) {
            return new VectorTopologicalSpace(dim);
        } else {
            return null;
        }
    }

    public boolean equals(Object object) {
        boolean result = false, intermediate = true;
        if(object instanceof VectorTopologicalSpace) {
            VectorTopologicalSpace vectorSpace = (VectorTopologicalSpace)object;
            if(getDimension() == vectorSpace.getDimension() && getCoordinates().size() == getDimension() &&
                    vectorSpace.getCoordinates().size() == vectorSpace.getDimension()) {
                for(int i=0; i<getDimension(); i++) {
                    if(getCoordinates().get(i).getCoordinate().getValue() !=
                            vectorSpace.getCoordinates().get(i).getCoordinate().getValue()) {
                        intermediate = false;
                        break;
                    }
                }
                if(intermediate) {
                    result = true;
                }
            }
        }
        return result;
    }

    public TopologicalSpaceElement getZero() {
        return new VectorElement(new RElement(0));
    }

    public TopologicalSpaceElement cast(TopologicalSpaceElement element) {
        if(element.getComponent(0) instanceof RElement) {
            RElement relement = (RElement)element;
            return new VectorElement(relement);
        } else {
            throw new AssertionError("Cannot cast an Element other than RElement!");
        }
    }

    public Ring getRing() {
        return RRing.ring;
    }

    public boolean isNullTopologicalSpace() {
        return (getDimension() == 0);
    }

    public TopologicalSpaceElement parseString(String string) {
        ArrayList<String> strings = parse(TextUtils.unparenthesize(string));
        if (strings.size() == 1) {
            return new VectorElement((RElement)getRing().parseString(strings.get(0)));
        } else {
            return null;
        }
    }

    private static ArrayList<String> parse(String s) {
        int pos = 0;
        int lastpos = 0;
        int level = 0;
        ArrayList<String> m = new ArrayList<String>();
        while (pos < s.length()) {
            if (s.charAt(pos) == '(') {
                pos++;
                level++;
            }
            else if (s.charAt(pos) == ')') {
                pos++;
                level--;
            }
            else if (s.charAt(pos) == ',' && level == 0) {
                m.add(s.substring(lastpos, pos));
                pos++;
                lastpos = pos;
            }
            else {
                pos++;
            }
        }
        m.add(s.substring(lastpos,pos).trim());
        return m;
    }

    public String getElementTypeName() { return "VectorTopologicalSpace"; }

    public TopologicalSpaceElement getComponentTopologicalSpace(int i) {
        if(i >= 0 && i < getDimension()) {
            return getCoordinates().get(i);
        } else {
            return null;
        }
    }

    public String toVisualString() {
        String res = "<";
        res += coordinatesToString();
        res += ">";
        return res;
    }

    private String coordinatesToString() {
        String s = "";
        for(int i=0; i<coordinates.size(); i++) {
            s += coordinates.get(i);
            if(i != (coordinates.size() - 1)) {
                s += ", ";
            }
        }
        return s;
    }

    public TopologicalSpaceMorphism getTranslation(TopologicalSpaceElement element) {
        return TranslationMorphism.make(this, element);
    }

    public TopologicalSpaceMorphism getIdentityMorphism() {
        return TopologicalSpaceMorphism.getIdentityMorphism(this);
    }

    public int compareTo(TopologicalSpace object) {
        if(object instanceof VectorTopologicalSpace) {
            VectorTopologicalSpace vectorSpace = (VectorTopologicalSpace)object;
            int c = getRing().compareTo(vectorSpace.getRing());
            if (c != 0) {
                return c;
            }
            int d = getDimension()-vectorSpace.getDimension();
            if (d != 0) {
                return d;
            }
            return 0;
        } else {
            return toString().compareTo(object.toString());
        }
    }

    public TopologicalSpace getNullTopologicalSpace() {
        return new VectorTopologicalSpace(0);
    }

    public boolean hasElement(TopologicalSpaceElement element) {
        boolean res = false;
        for(int i=0; i<coordinates.size(); i++) {
            if(coordinates.get(i).equals(element)) {
                res = true;
            }
        }
        return res;
    }

    public void toXML(XMLWriter writer) {
        throw new UnsupportedOperationException("Not implemented");
    }

    public TopologicalSpace fromXML(XMLReader reader, Element element) {
        throw new UnsupportedOperationException("Not implemented");
    }

    public boolean isRing() {
        return false;
    }

    public TopologicalSpaceElement createElement(List<TopologicalSpaceElement> elements) {
        if (elements == null) {
            return null;
        }
        return elements.get(0).clone();
    }

    /*
     * Sums two n-dimensional points. If the two points don't have the same
     * dimension, they cannot be summed, so it returns this point, not the
     * parameter point.
     */
    public VectorTopologicalSpace sum(VectorTopologicalSpace vectorSpace) {
        if(getDimension() == vectorSpace.getDimension()) {
            for(int i=0; i<getDimension(); i++) {
                double val1 = coordinates.get(i).getCoordinate().getValue();
                double val2 = vectorSpace.getCoordinates().get(i).getCoordinate().getValue();
                vectorSpace.setCoordinate(new VectorElement(new RElement(val1 + val2)), i);
            }
        } else {
            return this;
        }
        return vectorSpace;
    }


    /*
     * Subtracts two n-dimensional points. If the two points don't have the same
     * dimension, they cannot be summed, so it returns this point, not the
     * parameter point.
     *    returns (this - param)
     */
    public VectorTopologicalSpace difference(VectorTopologicalSpace vectorSpace) {
        if(getDimension() == vectorSpace.getDimension()) {
            for(int i=0; i<getDimension(); i++) {
                double val1 = coordinates.get(i).getCoordinate().getValue();
                double val2 = vectorSpace.getCoordinates().get(i).getCoordinate().getValue();
                vectorSpace.setCoordinate(new VectorElement(new RElement(val1 - val2)), i);
            }
        } else {
            return this;
        }
        return vectorSpace;
    }

    /*
     * Finds the distance between two n-dimensional Vectors (this and param vector)
     * Returns null if the two vectors don't have the same dimension
     */
    public TopologicalSpaceElement distance(VectorTopologicalSpace vectorSpace) {
        if(vectorSpace == null || getDimension() == 0 || vectorSpace.getDimension() == 0
                || getDimension() != vectorSpace.getDimension() || getDimension() != getCoordinates().size()) {
            return null;
        }
        double sum = 0, dist;
        for(int i=0; i<dimension; i++) {
            dist = coordinates.get(i).getCoordinate().getValue() - vectorSpace.getCoordinates().get(i).getCoordinate().getValue();
            sum += Math.pow(dist,2);
        }
        return new RElement(Math.sqrt(sum));
    }


    /*
     * Gets Length of Vector
     */
    public TopologicalSpaceElement getLength() {
        if(dimension == 0 || dimension != coordinates.size()) {
            return null;
        }
        double sum = 0;
        for(int i=0; i<dimension; i++) {
            sum += Math.pow(coordinates.get(i).getCoordinate().getValue(),2);
        }
        return new RElement(Math.sqrt(sum));
    }


    /*
     * Finds the distance from the point to the origin by finding the square root of
     * the sum of the squares of each element in the coordinates.
     */
    public TopologicalSpaceElement distanceFromOrigin() {
        return getLength();
    }


    /*
     * Returns true if it's distance from the origin is 1
     */
    public boolean isOne() {
        boolean result = false;
        if(distanceFromOrigin() instanceof RElement) {
            if(((RElement) distanceFromOrigin()).getValue() == 1) {
                result = true;
            }
        } else {
            throw new AssertionError("Invalid type for coordinates, cannot cast");
        }
        return result;
    }


    /*
     * Returns true if it's distance from the origin is 0 (i.e. the point is
     * the origin)
     */
    public boolean isZero() {
        boolean result = false;
        if(distanceFromOrigin() instanceof RElement) {
            if(((RElement) distanceFromOrigin()).getValue() == 0) {
                result = true;
            }
        } else {
            throw new AssertionError("Invalid type for coordinates, cannot cast");
        }
        return result;
    }


    public VectorTopologicalSpace(int dim, List<VectorElement> c) {
        if(c.size() == dim) {
            this.dimension = dim;
            this.coordinates = c;
        } else {
            throw new AssertionError("Cannot set a VectorTopologicalSpace with a different dimension and number of coordinates");
        }
    }

    public VectorTopologicalSpace(List<VectorElement> c) {
        coordinates = c;
        dimension = c.size();
    }

    public VectorTopologicalSpace(int dim, VectorElement[] c) {
        if(c.length == dim) {
            this.dimension = dim;
            List<VectorElement> c2 = new ArrayList<VectorElement>();
            for(int i=0; i<dim; i++) {
                c2.add(c[i]);
            }
            this.coordinates = c2;
        } else {
            throw new AssertionError("Cannot set a VectorTopologicalSpace with a different dimension and number of coordinates");
        }
    }

    public VectorTopologicalSpace(VectorElement[] c) {
        List<VectorElement> c2 = new ArrayList<VectorElement>();
        for(int i=0; i<c.length; i++) {
            c2.add(c[i]);
        }
        this.coordinates = c2;
        this.dimension = c.length;
    }

    public VectorTopologicalSpace(int dim, RElement[] c) {
        if(c.length == dim) {
            this.dimension = dim;
            List<VectorElement> c2 = new ArrayList<VectorElement>();
            for(int i=0; i<dim; i++) {
                c2.add(new VectorElement(c[i]));
            }
            this.coordinates = c2;
        } else {
            throw new AssertionError("Cannot set a VectorTopologicalSpace with a different dimension and number of coordinates");
        }
    }

    public VectorTopologicalSpace(RElement[] c) {
        List<VectorElement> c2 = new ArrayList<VectorElement>();
        for(int i=0; i<c.length; i++) {
            c2.add(new VectorElement(c[i]));
        }
        this.coordinates = c2;
        this.dimension = c.length;
    }

    public VectorTopologicalSpace(int dim) {
        if(dim == 0) {
            this.dimension = 0;
            this.coordinates = null;
        } else {
            List<VectorElement> c = new ArrayList<VectorElement>();
            for(int i=0; i<dim; i++) {
                c.add(new VectorElement(new RElement(0)));
            }
            this.coordinates = c;
            this.dimension = dim;
        }
    }

    public VectorTopologicalSpace(int dim, double val) {
        List<VectorElement> c = new ArrayList<VectorElement>();
        for(int i=0; i<dim; i++) {
            c.add(new VectorElement(new RElement(val)));
        }
        this.coordinates = c;
    }

    public int getDimension() { return this.dimension; }

    public List<VectorElement> getCoordinates() { return this.coordinates; }

    public void setDimension(int dim) { this.dimension = dim; }

    public void setCoordinates(List<VectorElement> c) { this.coordinates = c; }

    public void setCoordinate(VectorElement element, int index) {
        if(index < this.dimension && index >= 0) {
            this.coordinates.set(index, element);
        }
    }

    private int dimension;
    private List<VectorElement> coordinates;
}
