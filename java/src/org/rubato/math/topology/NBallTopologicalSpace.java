package org.rubato.math.topology;

import static org.rubato.xml.XMLConstants.*;
import org.rubato.math.topology.morphism.TopologicalSpaceMorphism;
import org.rubato.math.topology.morphism.TranslationMorphism;
import org.rubato.util.TextUtils;
import org.rubato.xml.XMLWriter;
import org.rubato.xml.XMLReader;
import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Justin on 5/10/2015.
 */
public class NBallTopologicalSpace implements TopologicalSpace {


    public String getElementTypeName() {
        return "NBallTopologicalSpace";
    }

    public TopologicalSpaceMorphism getIdentityMorphism() {
        return TopologicalSpaceMorphism.getIdentityMorphism(this);
    }

    public NBallElement cast(TopologicalSpaceElement element) {
        if(element.getComponent(0) instanceof NBallElement) {
            return new NBallElement(((NBallElement) element.getComponent(0)).getCenter());
        } else if(element.getComponent(0) instanceof NSphereElement) {
            return new NBallElement(((NSphereElement) element.getComponent(0)).getCenter());
        } else if(element.getComponent(0) instanceof CartProdElement) {
            return new NBallElement(((CartProdElement) element.getComponent(0)).getVector());
        } else {
            return null;
        }
    }

    public NBallTopologicalSpace getNullTopologicalSpace() {
        VectorTopologicalSpace vectorTopologicalSpace = VectorTopologicalSpace.make(0);
        return new NBallTopologicalSpace(vectorTopologicalSpace);
    }

    public Ring getRing() {
        return RRing.ring;
    }

    public TopologicalSpaceMorphism getTranslation(TopologicalSpaceElement element) {
        return TranslationMorphism.make(this, element);
    }

    public NBallElement getZero() {
        VectorTopologicalSpace vectorTopologicalSpace = VectorTopologicalSpace.make(0);
        return new NBallElement(vectorTopologicalSpace);
    }

    public int compareTo(TopologicalSpace object) {
        if(object instanceof NBallTopologicalSpace) {
            NBallTopologicalSpace nBallTopologicalSpace = (NBallTopologicalSpace)object;
            int c = getRing().compareTo(nBallTopologicalSpace.getRing());
            if (c != 0) {
                return c;
            }
            int d = getDimension() - nBallTopologicalSpace.getDimension();
            if (d != 0) {
                return d;
            }
            double r = getRadius().getValue() - nBallTopologicalSpace.getRadius().getValue();
            if (r != 0) {
                return (int)r;
            }
            return getCenter().compareTo(nBallTopologicalSpace.getCenter());
        } else {
            return toString().compareTo(object.toString());
        }
    }

    public NBallElement parseString(String string) {
        ArrayList<String> strings = parse(TextUtils.unparenthesize(string));
        if (strings.size() < getDimension()) {
            return null;
        }
        List<VectorElement> coordinateList = new ArrayList<VectorElement>();
        for (int i = 0; i < getDimension(); i++) {
            String s = strings.get(i);
            if((RElement)getRing().parseString(s) == null) {
                return null;
            }
            coordinateList.add(new VectorElement((RElement) getRing().parseString(s)));
        }
        return new NBallElement(new VectorTopologicalSpace(coordinateList));
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

    public void toXML(XMLWriter writer) {
        throw new UnsupportedOperationException("Not implemented");
    }

    public TopologicalSpace fromXML(XMLReader reader, Element element) {
        throw new UnsupportedOperationException("Not implemented");
    }

    public boolean isRing() { return false; }

    public boolean isNullTopologicalSpace()  {
        return (getDimension() == 0);
    }

    public NBallElement createElement(List<TopologicalSpaceElement> elements) {
        if(elements == null) {
            return null;
        }
        if(elements.get(0) instanceof NBallElement) {
            return (NBallElement)elements.get(0);
        }
        else if (elements.get(0) instanceof NSphereElement) {
            return new NBallElement(((NSphereElement) elements.get(0)).getCenter());
        }
        else if (elements.get(0) instanceof CartProdElement) {
            return new NBallElement(((CartProdElement) elements.get(0)).getVector());
        }
        if(elements.get(0) instanceof RElement) {
            if(elements.size() < getDimension()) {
                return null;
            }
            List<VectorElement> coordinateList = new ArrayList<VectorElement>();
            for(int i=0; i<getDimension(); i++) {
                coordinateList.add(new VectorElement((RElement)elements.get(i)));
            }
            return new NBallElement(new VectorTopologicalSpace(coordinateList));
        }
        return null;
    }

    public boolean hasElement(TopologicalSpaceElement element) {
        if(element instanceof NBallElement) {
            VectorTopologicalSpace point = ((NBallElement) element).getCenter();
            RElement dist = (RElement) getCenter().getCenter().distance(point);
            if(dist.getValue() <= getRadius().getValue()) {
                return true;
            }
        } else if(element instanceof NSphereElement) {
            VectorTopologicalSpace point = ((NSphereElement) element).getCenter();
            RElement dist = (RElement) getCenter().getCenter().distance(point);
            if(dist.getValue() <= getRadius().getValue()) {
                return true;
            }
        } else if(element instanceof CartProdElement) {
            VectorTopologicalSpace point = ((CartProdElement) element).getVector();
            RElement dist = (RElement) getCenter().getCenter().distance(point);
            if(dist.getValue() <= getRadius().getValue()) {
                return true;
            }
        }
        return false;
    }

    public String toVisualString() {
        String res = "(" + getCenter().getCenter().toVisualString();
        res += ", " + getRadius() + ")^" + getDimension();
        return res;
    }

    public String toString() {
        return "NBallTopSpace[" + toVisualString() + "]";
    }


    public NBallTopologicalSpace(int dim, NBallElement c, RElement r) {
        dimension = dim;
        center = c;
        radius = r;
    }

    public NBallTopologicalSpace(int dim, VectorTopologicalSpace c, RElement r) {
        dimension = dim;
        center = new NBallElement(c);
        radius = r;
    }

    public NBallTopologicalSpace(NBallElement c) {
        dimension = 0;
        center = c;
        radius = new RElement(0);
    }

    public NBallTopologicalSpace(VectorTopologicalSpace c) {
        dimension = 0;
        center = new NBallElement(c);
        radius = new RElement(0);
    }

    public int getDimension() { return dimension; }

    public NBallElement getCenter() { return center; }

    public RElement getRadius() { return radius; }

    public RElement getDiameter() { return new RElement(2*radius.getValue()); }

    public void setDimension(int d) { dimension = d; }

    public void setCenter(NBallElement c) { center = c; }

    public void setRadius(RElement element) { radius = element; }

    private int dimension;
    private NBallElement center;
    private RElement radius;
}
