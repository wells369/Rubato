package org.rubato.math.topology.morphism;

import static org.rubato.xml.XMLConstants.*;

import java.util.Arrays;

import org.rubato.math.topology.morphism.*;
import org.rubato.math.topology.morphism.TopologicalSpaceMorphism;
import org.rubato.math.matrix.RMatrix;
import org.rubato.xml.XMLInputOutput;
import org.rubato.xml.XMLReader;
import org.rubato.xml.XMLWriter;
import org.w3c.dom.Element;

/**
 * Created by Justin on 4/17/2015.
 */
public final class RFreeAffineMorphism extends RFreeAbstractMorphism {

    public static TopologicalSpaceMorphism make(RMatrix A, double[] b) {
        if (A.getColumnCount() == 1 && A.getRowCount() == 1 && b.length == 1) {
            return new RAffineMorphism(A.get(0, 0), b[0]);
        }
        else {
            return new RFreeAffineMorphism(A, b);
        }
    }


    private RFreeAffineMorphism(RMatrix A, double[] b) {
        super(A.getColumnCount(), A.getRowCount());
        if (A.getRowCount() != b.length) {
            String s = "Rows of A ("+A.getRowCount()+") don't match length of b ("
                    +b.length+")";
            throw new IllegalArgumentException(s);
        }
        this.A = A;
        this.b = b;
    }


    public double[] mapValue(double[] x) {
        double[] res;
        res = A.product(x);
        for (int i = 0; i < res.length; i++) {
            res[i] += b[i];
        }
        return res;
    }


    public boolean isModuleHomomorphism() {
        return true;
    }


    public boolean isLinear() {
        for (int i = 0; i < b.length; i++) {
            if (b[i] != 0) {
                return false;
            }
        }
        return true;
    }


    public boolean isIdentity() {
        return isLinear() && A.isUnit();
    }


    public boolean isConstant() {
        return A.isZero();
    }


    public TopologicalSpaceMorphism compose(TopologicalSpaceMorphism morphism)
            throws CompositionException {
        if (morphism instanceof RFreeAffineMorphism) {
            RFreeAffineMorphism rmorphism = (RFreeAffineMorphism) morphism;
            if (getDomain().getDimension() == rmorphism.getCodomain().getDimension()) {
                RMatrix resA = A.product(rmorphism.A);
                double[] resb = A.product(rmorphism.b);
                for (int i = 0; i < resb.length; i++) {
                    resb[i] += b[i];
                }
                return new RFreeAffineMorphism(resA, resb);
            }
        }
        return super.compose(morphism);
    }


    public TopologicalSpaceMorphism sum(TopologicalSpaceMorphism morphism)
            throws CompositionException {
        if (morphism instanceof RFreeAffineMorphism) {
            RFreeAffineMorphism rmorphism = (RFreeAffineMorphism) morphism;
            if (getDomain().getDimension() == rmorphism.getDomain().getDimension() &&
                    getCodomain().getDimension() == rmorphism.getCodomain().getDimension()) {
                RMatrix resA = A.sum(rmorphism.A);
                double[] resb = new double[b.length];
                for (int i = 0; i < resb.length; i++) {
                    resb[i] = b[i] + rmorphism.b[i];
                }
                return new RFreeAffineMorphism(resA, resb);
            }
        }
        return super.sum(morphism);
    }


    public TopologicalSpaceMorphism difference(TopologicalSpaceMorphism morphism)
            throws CompositionException {
        if (morphism instanceof RFreeAffineMorphism) {
            RFreeAffineMorphism rmorphism = (RFreeAffineMorphism) morphism;
            if (getDomain().getDimension() == rmorphism.getCodomain().getDimension() &&
                    getCodomain().getDimension() == rmorphism.getCodomain().getDimension()) {
                RMatrix resA = A.difference(rmorphism.A);
                double[] resb = new double[b.length];
                for (int i = 0; i < resb.length; i++) {
                    resb[i] = b[i] - rmorphism.b[i];
                }
                return new RFreeAffineMorphism(resA, resb);
            }
        }
        return super.difference(morphism);
    }


    public int compareTo(TopologicalSpaceMorphism object) {
        if (object instanceof RFreeAffineMorphism) {
            RFreeAffineMorphism m = (RFreeAffineMorphism)object;
            int comp = A.compareTo(m.A);
            if (comp == 0) {
                for (int i = 0; i < b.length; i++) {
                    if (b[i] < m.b[i]) {
                        return -1;
                    }
                    else if (b[i] > m.b[i]) {
                        return 1;
                    }
                }
                return 0;
            }
            else {
                return comp;
            }
        }
        else {
            return super.compareTo(object);
        }
    }


    public boolean equals(Object object) {
        if (object instanceof RFreeAffineMorphism) {
            RFreeAffineMorphism morphism = (RFreeAffineMorphism)object;
            return A.equals(morphism.A) && Arrays.equals(b, morphism.b);
        }
        else {
            return false;
        }
    }


    private final static String A_ATTR = "A";
    private final static String B_ATTR = "b";


    public void toXML(XMLWriter writer) {
        writer.openBlockWithType(TOPOLOGICALSPACEMORPHISM, getElementTypeName(),
                ROWS_ATTR, A.getRowCount(),
                COLUMNS_ATTR, A.getColumnCount());
        String s = "";
        for (int i = 0; i < A.getRowCount(); i++) {
            for (int j = 0; j < A.getColumnCount(); j++) {
                if (i != 0 || j != 0) {
                    s += ",";
                }
                s += A.get(i, j);
            }
        }
        writer.openInline(A_ATTR);
        writer.text(s);
        writer.closeInline();
        s = Double.toString(b[0]);
        for (int i = 1; i < b.length; i++) {
            s += ","+b[i];
        }
        writer.openInline(B_ATTR);
        writer.text(s);
        writer.closeInline();
        writer.closeBlock();
    }


    public TopologicalSpaceMorphism fromXML(XMLReader reader, Element element) {
        assert(element.getAttribute(TYPE_ATTR).equals(getElementTypeName()));

        if (!element.hasAttribute(ROWS_ATTR)) {
            reader.setError("Type %%1 is missing attribute %%2.", getElementTypeName(), ROWS_ATTR);
            return null;
        }
        int rows;
        try {
            rows = Integer.parseInt(element.getAttribute(ROWS_ATTR));
        }
        catch (NumberFormatException e) {
            reader.setError("Attribute %%1 of type %%2 must be an integer.", ROWS_ATTR, getElementTypeName());
            return null;
        }

        if (!element.hasAttribute(COLUMNS_ATTR)) {
            reader.setError("Type %%1 is missing attribute %%2.", getElementTypeName(), COLUMNS_ATTR);
            return null;
        }
        int columns;
        try {
            columns = Integer.parseInt(element.getAttribute(COLUMNS_ATTR));
        }
        catch (NumberFormatException e) {
            reader.setError("Attribute %%1 of type %%2 must be an integer.", COLUMNS_ATTR, getElementTypeName());
            return null;
        }

        final int numberCount = rows*columns;
        Element aElement = XMLReader.getChild(element, A_ATTR);
        if (aElement == null) {
            reader.setError("Type %%1 is missing child of type <%2>.", getElementTypeName(), A_ATTR);
            return null;
        }
        String[] numbers = aElement.getTextContent().trim().split(",");
        if (numbers.length != numberCount) {
            reader.setError("Element <%1> must have a comma-separated list with %2 real numbers.", A_ATTR, numberCount);
            return null;
        }

        RMatrix A0 = new RMatrix(rows, columns);
        try {
            int n = 0;
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < columns; j++) {
                    A0.set(i, j, Double.parseDouble(numbers[n]));
                    n++;
                }
            }
        }
        catch (NumberFormatException e) {
            reader.setError("Element <%1> must have a comma-separated list with %2 real numbers.", A_ATTR, numberCount);
            return null;
        }

        Element bElement = XMLReader.getChild(element, B_ATTR);
        if (bElement == null) {
            reader.setError("Type %%1 is missing child of type <%2>.", getElementTypeName(), B_ATTR);
            return null;
        }
        numbers = bElement.getTextContent().trim().split(",");
        if (numbers.length != rows) {
            reader.setError("Element <%1> must have a comma-separated list with %2 real numbers.", B_ATTR, rows);
            return null;
        }

        double b0[]= new double[rows];
        try {
            for (int i = 0; i < rows; i++) {
                b0[i] = Double.parseDouble(numbers[i]);
            }
        }
        catch (NumberFormatException e) {
            reader.setError("Element <%1> must have a comma-separated list with %2 real numbers.", B_ATTR, rows);
            return null;
        }

        return new RFreeAffineMorphism(A0, b0);
    }


    private static final XMLInputOutput<TopologicalSpaceMorphism> xmlIO = RFreeAffineMorphism.make(RMatrix.getUnitMatrix(0), new double[0]);

    public static XMLInputOutput<TopologicalSpaceMorphism> getXMLInputOutput() {
        return xmlIO;
    }


    public String getElementTypeName() {
        return "RFreeAffineMorphism";
    }


    public String toString() {
        return "RFreeAffineMorphism["+getDomain().getDimension()+","+getCodomain().getDimension()+"]";
    }


    /**
     * Returns the linear part.
     */
    public RMatrix getMatrix() {
        return A;
    }


    /**
     * Returns the translation part.
     */
    public double[] getVector() {
        return b;
    }


    private RMatrix  A;
    private double[] b;
}
