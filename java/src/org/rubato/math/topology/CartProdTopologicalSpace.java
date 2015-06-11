package org.rubato.math.topology;

import org.rubato.math.topology.morphism.TopologicalSpaceMorphism;
import org.rubato.math.topology.morphism.TranslationMorphism;
import org.rubato.util.TextUtils;
import org.rubato.xml.XMLReader;
import org.rubato.xml.XMLWriter;
import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Justin on 6/9/2015.
 */
public class CartProdTopologicalSpace implements TopologicalSpace {


    public CartProdElement cast(TopologicalSpaceElement element) {
        if(element.getComponent(0) instanceof NBallElement) {
            return new CartProdElement(((NBallElement) element.getComponent(0)).getCenter());
        } else if(element.getComponent(0) instanceof NSphereElement) {
            return new CartProdElement(((NSphereElement) element.getComponent(0)).getCenter());
        } else if(element.getComponent(0) instanceof CartProdElement) {
            return new CartProdElement(((CartProdElement) element.getComponent(0)).getVector());
        } else {
            return null;
        }
    }

    public int compareTo(TopologicalSpace object) {
        if(object instanceof CartProdTopologicalSpace) {
            CartProdTopologicalSpace space = (CartProdTopologicalSpace)object;
            int c = getRing().compareTo(space.getRing());
            if (c != 0) {
                return c;
            }
            int d = this.getNumVectors() - space.getNumVectors();
            if (d != 0) {
                return d;
            }
            int r = this.getVectorLength() - space.getVectorLength();
            if (r != 0) {
                return r;
            }
            int sum = 0;
            for(int i=0; i<getVectors().size(); i++) {
                sum += getVectors().get(i).getVector().compareTo(space.getVectors().get(i).getVector());
            }
            return sum;
        } else {
            return toString().compareTo(object.toString());
        }
    }

    public boolean equals(Object object) {
        if(object instanceof CartProdTopologicalSpace) {
            return (compareTo((CartProdTopologicalSpace) object) == 0);
        }
        return false;
    }

    public boolean hasElement(TopologicalSpaceElement element) {
        if(element instanceof NBallElement) {
            VectorTopologicalSpace vector = ((NBallElement) element).getCenter();
            return hasVectorInList(vector);
        }
        else if(element instanceof NSphereElement) {
            VectorTopologicalSpace vector = ((NSphereElement) element).getCenter();
            return hasVectorInList(vector);
        }
        else if(element instanceof CartProdElement) {
            VectorTopologicalSpace vector = ((CartProdElement) element).getVector();
            return hasVectorInList(vector);
        }
        return false;
    }

    private boolean hasVectorInList(VectorTopologicalSpace vector) {
        boolean res = false;
        for(int i=0; i<getVectors().size(); i++) {
            if(vectorsEqual(vector, getVectors().get(i).getVector())) {
                res = true;
                break;
            }
        }
        return res;
    }

    private boolean vectorsEqual(VectorTopologicalSpace v1, VectorTopologicalSpace v2) {
        if(v1.getCoordinates().size() == v2.getCoordinates().size()) {
            for(int i=0; i<v1.getCoordinates().size(); i++) {
                if(v1.getCoordinates().get(i).getCoordinate().getValue() != v2.getCoordinates().get(i).getCoordinate().getValue()) {
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }

    public CartProdElement createElement(List<TopologicalSpaceElement> elements) {
        if(elements == null) {
            return null;
        }
        if(elements.get(0) instanceof CartProdElement) {
            return (CartProdElement)elements.get(0);
        }
        else if (elements.get(0) instanceof NSphereElement) {
            return new CartProdElement(((NSphereElement) elements.get(0)).getCenter());
        }
        else if (elements.get(0) instanceof NBallElement) {
            return new CartProdElement(((NBallElement) elements.get(0)).getCenter());
        }
        else if(elements.get(0) instanceof RElement) {
            List<VectorElement> coordinateList = new ArrayList<VectorElement>();
            for(int i=0; i<elements.size(); i++) {
                coordinateList.add(new VectorElement((RElement)elements.get(i)));
            }
            return new CartProdElement(new VectorTopologicalSpace(coordinateList));
        }
        return null;
    }

    public TopologicalSpaceMorphism getIdentityMorphism() {
        return TopologicalSpaceMorphism.getIdentityMorphism(this);
    }

    public TopologicalSpaceMorphism getTranslation(TopologicalSpaceElement element) {
        return TranslationMorphism.make(this, element);
    }

    public CartProdElement getZero() {
        CartProdElement element = new CartProdElement(new VectorTopologicalSpace(1));
        return element;
    }

    public CartProdTopologicalSpace getNullTopologicalSpace() {
        return new CartProdTopologicalSpace(0,0);
    }

    public boolean isNullTopologicalSpace() { return (getNumVectors() == 0 && getVectorLength() == 0); }

    public Ring getRing() { return RRing.ring; }

    public boolean isRing() { return false; }

    public CartProdElement parseString(String string) {
        ArrayList<String> strings = parse(TextUtils.unparenthesize(string));
        List<VectorElement> coordinateList = new ArrayList<VectorElement>();
        for (int i = 0; i < strings.size(); i++) {
            String s = strings.get(i);
            if((RElement)getRing().parseString(s) == null) {
                return null;
            }
            coordinateList.add(new VectorElement((RElement) getRing().parseString(s)));
        }
        return new CartProdElement(new VectorTopologicalSpace(coordinateList));
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

    public String toString() {
        String res = "CartProdTopologicalSpace[";
        if(getVectors() != null) {
            res += getVectors().get(0).getVector().toVisualString();
            for(int i=1; i<getVectors().size(); i++) {
                res += ", ";
                res += getVectors().get(i).getVector().toVisualString();
            }
        }
        res += "]";
        return res;
    }

    public String toVisualString() {
        String res = "CartProd[" + getNumVectors() + "][";
        if(getVectorLength() == -1) {
            res += "No set Dimension in domain";
        } else {
            res += "codomain dimension: " + getVectorLength();
        }
        res += "]";
        return res;
    }

    public String getElementTypeName() { return "CartProdTopologicalSpace"; }

    public int hashCode() {
        int hash = 0;
        for(int i=0; i<getVectors().size(); i++) {
            hash += getVectors().get(i).hashCode();
        }
        return hash;
    }

    public void toXML(XMLWriter writer) {
        throw new UnsupportedOperationException("Not implemented");
    }


    public TopologicalSpace fromXML(XMLReader reader, Element element) {
        throw new UnsupportedOperationException("Not implemented");
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    public CartProdTopologicalSpace(int nVectors, int vecLen) {
        if(nVectors <= 0 || vecLen <= 0) {
            vectorLength = 0;
            numVectors = 0;
            vectors = null;
        } else {
            List<CartProdElement> newVList = new ArrayList<CartProdElement>();
            for(int i=0; i<nVectors; i++) {
                VectorTopologicalSpace tempSpace = new VectorTopologicalSpace(vecLen);
            }
        }
    }

    public CartProdTopologicalSpace(List<CartProdElement> vecs) {
        setVectors(vecs);
    }

    public List<CartProdElement> getVectors() { return vectors; }

    public int getNumVectors() { return numVectors; }

    public int getVectorLength() { return vectorLength; }

    public int getDimension() { return numVectors; }

    public void setVectors(List<CartProdElement> vecs) {
        if(vecs != null) {
            int vLen = vecs.get(0).getVector().getDimension();
            vecs.get(0).setCartProdSpace(this);
            boolean sameVectorLength = true;
            for(int i=1; i<vecs.size(); i++) {
                vecs.get(i).setCartProdSpace(this);
                if(vLen != vecs.get(i).getVector().getDimension()) {
                    sameVectorLength = false;
                }
            }
            if(sameVectorLength) {
                vectorLength = vLen;
            } else {
                vectorLength = -1;
            }
            numVectors = vecs.size();
        } else {
            vectorLength = 0;
            numVectors = 0;
        }
        vectors = vecs;
    }

    private List<CartProdElement> vectors;
    private int numVectors;
    private int vectorLength;
}
