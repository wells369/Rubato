package org.rubato.rubettes.alteration;

import org.rubato.base.RubatoException;
import org.rubato.math.module.Module;
import org.rubato.math.module.RElement;
import org.rubato.math.module.RRing;
import org.rubato.math.module.morphism.CanonicalMorphism;
import org.rubato.math.module.morphism.CompositionException;
import org.rubato.math.module.morphism.ModuleMorphism;
import org.rubato.math.yoneda.Denotator;
import org.rubato.math.yoneda.NameDenotator;
import org.rubato.math.yoneda.SimpleDenotator;
import org.rubato.math.yoneda.SimpleForm;

public class Alterator {
	
	public Denotator alter(Denotator d1, Denotator d2, double degree, int[][] paths) throws RubatoException {
		for (int i = 0; i < paths.length; i++) {
			int[] currentPath = paths[i];
			d1 = this.replaceSimpleDenotatorWithAltered(d1, d2, currentPath, degree);
		}
		return d1;
	}
	
	protected Denotator morphDenotator(Denotator d1, Denotator d2, int[][] paths, double[] degrees) throws RubatoException {
		for (int i = 0; i < paths.length; i++) {
			int[] currentPath = paths[i];
			d1 = this.replaceSimpleDenotatorWithAltered(d1, d2, currentPath, degrees[i]);
		}
		return d1;
	}
	
	protected Denotator replaceSimpleDenotatorWithAltered(Denotator d1, Denotator d2, int[] path, double degree) throws RubatoException {
		SimpleDenotator simple1 = (SimpleDenotator) d1.get(path);
		ModuleMorphism morphism0 = simple1.getModuleMorphism();
		ModuleMorphism morphism1 = ((SimpleDenotator) d2.get(path)).getModuleMorphism();
		ModuleMorphism newMorphism = this.makeAlteredMorphism(morphism0, morphism1, degree);
		
		
		System.out.println(morphism1);
		SimpleForm form = (SimpleForm)simple1.getForm();
		Denotator newSimple = new SimpleDenotator(NameDenotator.make(""), form, newMorphism);
		return d1.replace(path, newSimple);
	}
	
	protected ModuleMorphism makeAlteredMorphism(ModuleMorphism m0, ModuleMorphism m1, double percentage) {
		if (percentage == 0) {
			return m0;
		} else if (percentage == 1) {
			return m1;
		} else {
			try {
				Module module = m0.getCodomain();
				m0 = this.getCastedMorphism(m0, RRing.ring);
				m1 = this.getCastedMorphism(m1, RRing.ring);
				ModuleMorphism scaled0 = m0.scaled(new RElement(1-percentage));
				ModuleMorphism scaled1 = m1.scaled(new RElement(percentage));
				ModuleMorphism result = scaled0.sum(scaled1);
				result = this.getCastedMorphism(result, module);
				return result;
			} catch (CompositionException e) {
				e.printStackTrace();
				return null;
			}
		}
	}
	
	/*
	 * composes the input morphism with a casting morphism for the specified codomain.
	 * example: m:Q->Q, c:R, then return m:Q->R 
	 */
	private ModuleMorphism getCastedMorphism(ModuleMorphism morphism, Module newCodomain) throws CompositionException {
		Module oldCodomain = morphism.getCodomain();
		if (!newCodomain.equals(oldCodomain)) {
			ModuleMorphism castingMorphism = CanonicalMorphism.make(oldCodomain, newCodomain);
			morphism = castingMorphism.compose(morphism);
		}
		return morphism;
	}

}
