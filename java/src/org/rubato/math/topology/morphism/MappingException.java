package org.rubato.math.topology.morphism;

import org.rubato.base.RubatoException;
import org.rubato.math.topology.TopologicalSpaceElement;

/**
 * Created by Justin on 4/15/2015.
 */
public final class MappingException extends RubatoException {

    /**
     * Creates a MappingException with a standard message string.
     * The standard message indicates the module element, the required
     * domain, and morphism where the failure occurred.
     *
     * @param msg a message to prepend to the standard message
     * @param element the TopologicalSpaceElement to be mapped
     * @param morphism the TopologicalSpaceMorphism where the exception occurred
     */
    public MappingException(String msg, TopologicalSpaceElement element, TopologicalSpaceMorphism morphism) {
        super(msg+"Failed to map "+element+" in domain "+morphism.getDomain()+" in "+
                "morphism "+morphism);
        this.element = element;
        this.morphism = morphism;
    }


    /**
     * Creates a MappingException with a standard message string.
     * The standard message indicates the module element, the required
     * domain, and morphism where the failure occurred.
     *
     * @param element the TopologicalSpaceElement to be mapped
     * @param morphism the TopologicalSpaceMorphism where the exception occurred
     */
    public MappingException(TopologicalSpaceElement element, TopologicalSpaceMorphism morphism) {
        this("", element, morphism);
    }


    /**
     * Returns the element that failed to be mapped.
     */
    public TopologicalSpaceElement getElement() {
        return element;
    }


    /**
     * Returns the morphism where the failure occurred.
     */
    public TopologicalSpaceMorphism getTopologicalSpaceMorphism() {
        return morphism;
    }


    private TopologicalSpaceMorphism morphism;
    private TopologicalSpaceElement  element;
}
