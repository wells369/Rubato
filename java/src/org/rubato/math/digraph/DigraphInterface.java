package org.rubato.math.digraph;

import java.io.Serializable;
import java.util.List;
//import org.rubato.math.topology.morphism.TopoligicalSpaceMorphism;
import org.rubato.math.matrix.ZMatrix;
import org.rubato.xml.XMLInputOutput;
import org.rubato.xml.XMLReader;
import org.rubato.xml.XMLWriter;
import org.w3c.dom.Element;
import org.rubato.math.Space;

/**
 * Created by Justin on 5/29/2015.
 */
public interface DigraphInterface
        extends Serializable, Comparable<DigraphInterface>,
        XMLInputOutput<DigraphInterface>,  Space {

    /**
     * Returns the dimension of this module.
     */
    public int getDimension();

    /**
     * Returns the Element Type Name for this Digraph.
     */
    public String getElementTypeName();

    /**
     * Returns the null-module corresponding to this module.
     */
    public DigraphInterface getNullDigraph();

    /**
     * Returns true iff this is a null-module.
     */
    public boolean isNullDigraph();

    /*
     * Returns the number of points int the Digraph.
     */
    public int getNumPoints();

    /*
     * Returns the number of arrows in the Digraph.
     */
    public int getNumArrows();

    /*
     * Returns the matrix of arrows in the Digraph.
     */
    public ZMatrix getArrowMatrix();

    /*
     * Sets the name of the Digraph.
     */
    public void setDigraphName(String dName);

    /*
     * Sets the Matrix of Arrows for the Digraph.
     */
    public void setArrowMatrix(ZMatrix matrix);

    /*
     * Removes an arrow from point i to point j
     * in the Digraph. Does nothing if there is
     * no arrow between the specified points.
     */
    public void removeArrow(int i, int j);

    /*
     * Adds an arrow from point i to point j.
     */
    public void addArrow(int i, int j);

    /**
     * Returns true iff Digraph contains the arrow from index i to index j.
     */
    public boolean hasArrow(int i, int j);

    /**
     * Returns the hash code for this Digraph.
     */
    public int hashCode();

    /**
     * Returns true iff this module is equals to <code>object</code>.
     */
    public boolean equals(Object object);

    /**
     * Compares this module with <code>object</code>.
     */
    public int compareTo(DigraphInterface object);

    /**
     * Returns a human readable string representation of this module.
     * The representation is not meant to be parseable.
     */
    public String toString();

    /**
     * Returns a human readable string representation of this module.
     * The representation is not meant to be parseable.
     * The string should be a short representation, possibly using
     * Unicode characters.
     */
    public String toVisualString();
}
