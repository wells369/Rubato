package org.rubato.math.topology.morphism;

import org.rubato.math.topology.TopologicalSpace;
import org.rubato.math.topology.TopologicalSpaceElement;
import org.rubato.math.topology.RingElement;
import org.rubato.xml.XMLInputOutput;

import java.io.Serializable;

/**
 * Created by Justin on 4/14/2015.
 */
public abstract class TopologicalSpaceMorphism
        implements Comparable<TopologicalSpaceMorphism>, Cloneable, Serializable, XMLInputOutput<TopologicalSpaceMorphism> {

    /**
     * Creates a new morphism with <code>domain</code>
     * and <code>codomain<code> as indicated.
     */
    public TopologicalSpaceMorphism(TopologicalSpace domain, TopologicalSpace codomain) {
        this.domain = domain;
        this.codomain = codomain;
    }


    /**
     * Maps the element <code>x</code>.
     * This must be implemented for each specific morphism type.
     *
     * @return the result of mapping element <code>x</code>
     * @throws MappingException if mapping of <code>element<code> fails
     */
    public abstract TopologicalSpaceElement map(TopologicalSpaceElement x)
            throws MappingException;


    /**
     * Returns the composition this*<code>morphism</code>.
     *
     * @throws CompositionException if composition could not be performed
     */
    public TopologicalSpaceMorphism compose(TopologicalSpaceMorphism morphism)
            throws CompositionException {
        return CompositionMorphism.make(this, morphism);
    }


    /**
     * Returns the sum of this module morphism and <code>morphism</code>.
     *
     * @throws CompositionException if sum could not be performed
     */
    public TopologicalSpaceMorphism sum(TopologicalSpaceMorphism morphism)
            throws CompositionException {
        return SumMorphism.make(this, morphism);
    }


    /**
     * Returns the difference of this module morphism and <code>morphism</code>.
     *
     * @throws CompositionException if difference could not be performed
     */
    public TopologicalSpaceMorphism difference(TopologicalSpaceMorphism morphism)
            throws CompositionException {
        return DifferenceMorphism.make(this, morphism);
    }


    /**
     * Returns this module morphism scaled by <code>element</code>.
     */
    public TopologicalSpaceMorphism scaled(RingElement element)
            throws CompositionException {
        TopologicalSpaceMorphism m = ScaledMorphism.make(this, element);
        if (m == null) {
            throw new CompositionException("TopologicalSpaceMorphism.scaled: "+this+" cannot be scaled by "+element);
        }
        return m;
    }

    /**
     * Returns the value of the morphism evaluated at the zero of the domain.
     */
    public TopologicalSpaceElement atZero() {
        try {
            return map(getDomain().getZero());
        }
        catch (MappingException e) {
            throw new AssertionError("This should never happen!");
        }
    }


    /**
     * Returns this module morphism raise to the power <code>n</code>.
     * The power must be non-negative and the domain must be equal
     * to the codomain.
     *
     * @throws CompositionException if power could not be performed
     */
    public TopologicalSpaceMorphism power(int n)
            throws CompositionException {
        return PowerMorphism.make(this, n);
    }


    /**
     * Returns the identity morphism in <code>module</code>.
     */
    public static TopologicalSpaceMorphism getIdentityMorphism(TopologicalSpace module) {
        return new IdentityMorphism(module);
    }


    /**
     * Returns the constant <code>value</code> morphism in <code>module</code>.
     */
    public static TopologicalSpaceMorphism getConstantMorphism(TopologicalSpace module, TopologicalSpaceElement value) {
        return new ConstantMorphism(module, value);
    }


    /**
     * Returns a constant morphism with the domain of this
     * morphism that returns the specified constant <code>value</code>.
     */
    public TopologicalSpaceMorphism getConstantMorphism(TopologicalSpaceElement value) {
        return new ConstantMorphism(getDomain(), value);
    }


    /**
     * Returns true iff this morphism is the identity morphism.
     */
    public boolean isIdentity() {
        return false;
    }


    /**
     * Returns true iff this morphism is constant.
     */
    public boolean isConstant() {
        return false;
    }


    /**
     * Returns the domain of this morphism.
     */
    public final TopologicalSpace getDomain() {
        return domain;
    }


    /**
     * Returns the codomain of this morphism.
     */
    public final TopologicalSpace getCodomain() {
        return codomain;
    }


    /**
     * If true, then this is a module homomorphism.
     */
    public boolean isModuleHomomorphism() {
        return false;
    }


    /**
     * If true, then this is a ring homomorphism.
     */
    public boolean isRingHomomorphism() {
        return false;
    }


    /**
     * If true, then this is a morphism between rings;
     */
    public boolean isRingMorphism() {
        return getDomain().isRing() && getCodomain().isRing();
    }


    /**
     * Returns the the ring morphism that transforms between
     * the rings of the domain and codomain modules.
     */
    public abstract TopologicalSpaceMorphism getRingMorphism();


    /**
     * Returns true iff this is a linear morphism.
     */
    public boolean isLinear() {
        return false;
    }


    /**
     * Returns true iff the composition <code>f</code>*<code>g</code>
     * is possible.
     */
    public final static boolean composable(TopologicalSpaceMorphism f, TopologicalSpaceMorphism g) {
        return f.getDomain().equals(g.getCodomain());
    }


    /**
     * Returns true iff element <code>x</code> is in the domain of the morphism.
     */
    public boolean inDomain(TopologicalSpaceElement x) {
        return domain.hasElement(x);
    }


    /**
     * Compares two module morphisms.
     * Checks first for equality.
     * The default comparison is on names, subclasses may implement
     * a more meaningful comparison.
     */
    public int compareTo(TopologicalSpaceMorphism morphism) {
        if (this.equals(morphism)) {
            return 0;
        }
        else {
            return toString().compareTo(morphism.toString());
        }
    }


    /**
     * Returns true iff this morphism is equal to <code>object</code>.
     * In general it is not possible to determine whether to functions
     * are the same, so this returns true iff both morphisms have
     * the same structure.
     */
    public abstract boolean equals(Object object);


    /**
     * TopologicalSpaceMorphism objects cannot be changed, so clone
     * returns the object itself.
     */
    public Object clone() {
        return this;
    }


    /**
     * Returns a string representation of this morphism.
     * This string is used for generic comparison.
     */
    public abstract String toString();


    private final TopologicalSpace domain;
    private final TopologicalSpace codomain;
}

