package org.rubato.math.topology.morphism;

import org.rubato.math.topology.RRing;
import org.rubato.math.topology.CartProdTopologicalSpace;
import org.rubato.math.topology.TopologicalSpaceElement;
import org.rubato.math.topology.VectorTopologicalSpace;
import org.rubato.util.TextUtils;
import org.rubato.xml.XMLReader;
import org.rubato.xml.XMLWriter;
import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Justin on 6/9/2015.
 */
public final class CartesianProductMorphism extends TopologicalSpaceMorphism {



    public TopologicalSpaceMorphism getRingMorphism() {
        return RRing.ring.getIdentityMorphism();
    }

    public TopologicalSpaceElement map(TopologicalSpaceElement x)
            throws MappingException {
        return null;
    }

    public boolean equals(Object object) {
        if(object instanceof CartesianProductMorphism) {

        }
        return false;
    }

    public String toString() {
        return "";
    }

    public String getElementTypeName() { return "CartesianProductMorphism"; }

    public void toXML(XMLWriter writer) {
        throw new UnsupportedOperationException("Not implemented");
    }


    public TopologicalSpaceMorphism fromXML(XMLReader reader, Element element) {
        throw new UnsupportedOperationException("Not implemented");
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /*public CartesianProductMorphism make(List<VectorTopologicalSpace> dVectorList) {
        return null;
    }*/

    private CartesianProductMorphism(CartProdTopologicalSpace dVectorList, CartProdTopologicalSpace dVectorList2) {
        //domainVectorList = dVectorList;

        super(dVectorList, dVectorList2);
    }

    private List<VectorTopologicalSpace> domainVectorList;
    private List<VectorTopologicalSpace> codomainVectorList;
    private int dimension;
}
