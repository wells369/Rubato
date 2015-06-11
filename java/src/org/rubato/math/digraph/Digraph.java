package org.rubato.math.digraph;

import static org.rubato.xml.XMLConstants.*;

import java.lang.reflect.Array;
import java.util.LinkedList;
import java.util.List;

import org.rubato.math.arith.Folding;
import org.rubato.util.TextUtils;
import org.rubato.xml.XMLInputOutput;
import org.rubato.xml.XMLReader;
import org.rubato.xml.XMLWriter;
import org.w3c.dom.Element;
import org.rubato.math.Space;
import org.rubato.math.SpaceElement;
import org.rubato.math.matrix.Matrix;
import org.rubato.math.matrix.ZMatrix;

/**
 * Created by Justin on 5/29/2015.
 */
public final class Digraph implements DigraphInterface {

    public DigraphInterface getNullDigraph() {
        Digraph digraph = new Digraph("NullDigraph");
        return digraph;
    }

    public int compareTo(DigraphInterface object) {
        if (object instanceof Digraph) {
            Digraph digraph = (Digraph)object;
            return getDimension() - digraph.getDimension();
        }
        else {
            return toString().compareTo(object.toString());
        }
    }

    public boolean hasArrow(int i, int j) {
        if(i<getDimension() && j < getDimension() && i>=0 && j >= 0) {
            return (this.ArrowMatrix.get(i,j) > 0);
        } else {
            return false;
        }
    }

    public void toXML(XMLWriter writer) {
        writer.emptyWithType(MODULE, getElementTypeName(), DIMENSION_ATTR, getDimension());
    }

    public DigraphInterface fromXML(XMLReader reader, Element element) {
        assert(element.getAttribute(TYPE_ATTR).equals(getElementTypeName()));
        int dimension = XMLReader.getIntAttribute(element, DIMENSION_ATTR, 0, Integer.MAX_VALUE, 0);
        String name = XMLReader.getStringAttribute(element, NAME_ATTR);
        Digraph result = new Digraph(name, dimension);
        return result;
    }

    private String matrixToString() {
        String m = "(";
        for(int i=0; i<this.NumPoints; i++) {
            for(int j=0; j<this.NumPoints; j++) {
                m += this.ArrowMatrix.get(i,j);
                if(j != this.NumPoints - 1) {
                    m += ",";
                }
            }
            if(i == this.NumPoints - 1) {
                m += ")";
            } else {
                m += "\n";
            }
        }
        return m;
    }

    public String toString() {
        String s = "Digraph:"+this.digraphName+"\n";
        s += "   points:"+this.NumPoints+"\n";
        s += "   arrows:"+this.NumArrows+"\n";
        s += "   Matrix:\n"+matrixToString();;
        return s;
    }

    public String toVisualString() {
        return "Digraph:"+this.digraphName+","+this.NumPoints+","+this.NumArrows;
    }

    public boolean equals(Object object) {
        if(this == object) {
            return true;
        }
        else if (object instanceof Digraph) {
            Digraph d = (Digraph)object;
            if(getDimension() != d.getDimension()) {
                return false;
            } else if(getNumArrows() != d.getNumArrows()) {
                return false;
            } else if(!compareMatrices(d.getArrowMatrix())) {
                return false;
            } else { return true; }
        } else {
            return false;
        }
    }

    public int getDimension() {
        return this.NumPoints;
    }

    public String getElementTypeName() {
        return "Digraph";
    }

    public int getNumPoints(){
        return this.NumPoints;
    }

    public int getNumArrows(){
        return this.NumArrows;
    }

    public ZMatrix getArrowMatrix(){
        return this.ArrowMatrix;
    }

    public String getDigraphName(){
        return this.digraphName;
    }

    public void setDigraphName(String dName) {
        this.digraphName = dName;
    }

    public void setArrowMatrix(ZMatrix matrix) {
        if(matrix != null) {
            if(matrix.getRowCount() == matrix.getColumnCount()) {
                this.ArrowMatrix = matrix;
                this.NumPoints = matrix.getRowCount();
                this.NumArrows = matrix.sum();
            } else {
                throw new AssertionError("Cannot set Digraph with a non-square Matrix!");
            }
        }
        else {
            throw new AssertionError("Cannot set Digraph with a Null Matrix!");
        }
    }

    public void removeArrow(int i, int j) {
        if(i >= 0 && i < this.NumPoints && j >= 0 && j < this.NumPoints) {
            int val = this.ArrowMatrix.get(i, j);
            if(val > 0) {
                this.ArrowMatrix.set(i, j, val--);
                this.NumArrows--;
            }
        }
    }

    public void addArrow(int i, int j) {
        if(i >= 0 && i < this.NumPoints && j >= 0 && j < this.NumPoints) {
            int val = this.ArrowMatrix.get(i, j);
            this.ArrowMatrix.set(i, j, val++);
            this.NumArrows++;
        }
    }

    public Digraph(String dName){
        this.digraphName = dName;
        this.NumArrows = 0;
        this.NumPoints = 0;
    }

    public Digraph(String dName, ZMatrix matrix) {
        this.digraphName = dName;
        this.ArrowMatrix = matrix;
        this.NumPoints = matrix.getRowCount();
        this.NumArrows = matrix.sum();
    }

    public Digraph(String dName, int nPoints) {
        if(nPoints > 0) {
            this.digraphName = dName;
            this.NumPoints = nPoints;
            ZMatrix matrix = new ZMatrix(nPoints, nPoints);
            this.ArrowMatrix = matrix;
        }
        else {
            this.digraphName = dName;
            this.NumArrows = 0;
            this.NumPoints = 0;
        }
    }

    public boolean isNullDigraph() {
        return (this.NumPoints == 0);
    }

    public boolean compareMatrices(ZMatrix matrix) {
        if(matrix == null && isNullDigraph()) {
            return true;
        } else if(matrix == null) {
            return false;
        } else if (this.NumPoints != matrix.getRowCount() || this.NumPoints != matrix.getColumnCount()) {
            return false;
        }
        boolean result = true;
        for(int i=0; i<this.NumPoints; i++)
            for(int j=0; j<this.NumPoints; j++)
                if(this.ArrowMatrix.get(i,j) != matrix.get(i,j))
                    result = false;
        return result;
    }

    public int hashCode() {
        return 37*getDimension() + this.NumArrows;
    }

    private int NumPoints;
    private int NumArrows;
    private ZMatrix ArrowMatrix;
    private String digraphName;
}
