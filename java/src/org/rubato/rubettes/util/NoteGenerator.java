/*
 * Copyright (C) 2006 Florian Thalmann
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of version 2 of the GNU General Public
 * License as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this program; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA 02111-1307, USA.
 *
 */

package org.rubato.rubettes.util;

import java.util.ArrayList;
import java.util.List;

import org.rubato.base.Repository;
import org.rubato.base.RubatoException;
import org.rubato.math.arith.Rational;
import org.rubato.math.module.DomainException;
import org.rubato.math.module.ModuleElement;
import org.rubato.math.module.QElement;
import org.rubato.math.module.RElement;
import org.rubato.math.module.ZElement;
import org.rubato.math.yoneda.Denotator;
import org.rubato.math.yoneda.LimitDenotator;
import org.rubato.math.yoneda.LimitForm;
import org.rubato.math.yoneda.NameDenotator;
import org.rubato.math.yoneda.PowerDenotator;
import org.rubato.math.yoneda.PowerForm;
import org.rubato.math.yoneda.SimpleDenotator;
import org.rubato.math.yoneda.SimpleForm;

/**
 * A utility class for generating Note and Score denotators with ease. The following possibilities
 * are provided for parallel use:
 * - melody generation from a pitch list
 * - iterative melody generation
 * - single note generation
 * 
 * @author Florian Thalmann
 */
public class NoteGenerator {
	
	private PowerForm scoreForm = (PowerForm) Repository.systemRepository().getForm("Score");
	private LimitForm noteForm = (LimitForm) Repository.systemRepository().getForm("Note");
	private SimpleForm onsetForm = (SimpleForm) Repository.systemRepository().getForm("Onset");
	private SimpleForm pitchForm = (SimpleForm) Repository.systemRepository().getForm("Pitch");
	private SimpleForm loudnessForm = (SimpleForm) Repository.systemRepository().getForm("Loudness");
	private SimpleForm durationForm = (SimpleForm) Repository.systemRepository().getForm("Duration");
	
	private NameDenotator emptyName = NameDenotator.make("");
	
	private List<Denotator> currentMelody;
	private double noteDistance;
	private double currentOnset;
	
	/**
	 * Returns a Score denotator containing a note for every specified pitch. The first note
	 * has onset 0.
	 * 
	 * @param noteDistance - the distance between subsequent notes
	 * @param pitches - an array of pitch values
	 */
	public PowerDenotator createSimpleMelody(double noteDistance, Double[] pitches) {
		this.startNewMelody(noteDistance);
		for (int i = 0; i < pitches.length; i++) {
			this.addNoteToMelody(pitches[i]);
		}
		return this.createScoreWithMelody();
	}
	
	/**
	 * Returns a Score denotator containing a note for every specified pitch. The first note
	 * has onset 0.
	 * 
	 * @param noteDistance - the distance between subsequent notes
	 * @param pitches - the pitch values
	 */
	public PowerDenotator createSimpleMelody(double noteDistance, double... pitches) {
		this.startNewMelody(noteDistance);
		for (int i = 0; i < pitches.length; i++) {
				this.addNoteToMelody(pitches[i]);
		}
		return this.createScoreWithMelody();
	}
	
	/**
	 * Starts a new iterative melody with the specified note distance.
	 * 
	 * @param noteDistance - the distance between subsequent notes
	 */
	public void startNewMelody(double noteDistance) {
		this.currentMelody = new ArrayList<Denotator>();
		this.noteDistance = noteDistance;
		this.currentOnset = 0;
	}
	
	/**
	 * Adds a note with the specified pitch to the iterative melody.
	 * 
	 * @param pitch - the pitch of the new note
	 */
	public void addNoteToMelody(double pitch) {
		Denotator note = this.createNoteDenotator(this.currentOnset, pitch, 120, 1);
		this.currentMelody.add(note);
		this.currentOnset += this.noteDistance;
	}
	
	/**
	 * Returns the current iteratively generated melody.
	 */
	public PowerDenotator createScoreWithMelody() {
		try {
			return new PowerDenotator(this.emptyName, this.scoreForm, this.currentMelody);
		} catch (RubatoException e) { return null; }
	}
	
	/**
	 * Returns a new Note denotator with the specified parameters.
	 * 
	 * @param onset
	 * @param pitch
	 * @param loudness
	 * @param duration
	 */
	public Denotator createNoteDenotator(double onset, double pitch, int loudness, double duration) {
		try {
			List<Denotator> coordinates = new ArrayList<Denotator>();
			coordinates.add(this.createSimpleDenotator(this.onsetForm, new RElement(onset)));
			coordinates.add(new SimpleDenotator(this.emptyName, this.pitchForm, new QElement(new Rational(pitch))));
			coordinates.add(new SimpleDenotator(this.emptyName, this.loudnessForm, new ZElement(loudness)));
			coordinates.add(new SimpleDenotator(this.emptyName, this.durationForm, new RElement(duration)));
			
			//this takes a lot of time compared to the other operations
			return new LimitDenotator(this.emptyName, this.noteForm, coordinates);
		} catch (RubatoException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Modifies the elements of the coordinates of a note denotator with the specified new values.
	 *
	 * @param note - the note, the values of which have to be replaced
	 * @param onset - the new onset
	 * @param loudness - the new loudness
	 * @param duration - the new duration
	 * @throws RubatoException
	 */
	public void modifyNoteDenotator(LimitDenotator note, double onset, int loudness, double duration) throws RubatoException {
		this.modifyNoteDenotator(note, onset, duration);
		Denotator loudnessDenotator = this.createSimpleDenotator(this.loudnessForm, new ZElement(loudness));
		note.setFactor(2, loudnessDenotator);
	}
	
	/**
	 * Modifies the elements of the coordinates of a note denotator with the specified new values.
	 * 
	 * @param note - the note, the values of which have to be replaced
	 * @param onset - the new onset
	 * @param duration - the new duration
	 * @throws RubatoException
	 */
	public void modifyNoteDenotator(LimitDenotator note, double onset, double duration) throws RubatoException {
		Denotator onsetDenotator = this.createSimpleDenotator(this.onsetForm, new RElement(onset));
		Denotator durationDenotator = this.createSimpleDenotator(this.durationForm, new RElement(duration));
		
		note.setFactor(0, onsetDenotator);
		note.setFactor(3, durationDenotator);
	}
	
	/*
	 * Returns a new denotator of the specified form containing the specified element.
	 */
	private Denotator createSimpleDenotator(SimpleForm form, ModuleElement element) {
		try {
			return new SimpleDenotator(this.emptyName, form, element);
		} catch(DomainException e) { return null; }
	}
	
	
	/**
	 * Returns the Score form from the system repository.
	 */
	public PowerForm getScoreForm() { return this.scoreForm; }
	
	/**
	 * Returns the Note form from the system repository.
	 */
	public LimitForm getNoteForm() { return this.noteForm; }
	
	/**
	 * Returns the Onset form from the system repository.
	 */
	public SimpleForm getOnsetForm() { return this.onsetForm; }
	
	/**
	 * Returns the Pitch form from the system repository.
	 */
	public SimpleForm getPitchForm() { return this.pitchForm; }
	
	/**
	 * Returns the Loudness form from the system repository.
	 */
	public SimpleForm getLoudnessForm() { return this.loudnessForm; }
	
	/**
	 * Returns the Duration form from the system repository.
	 */
	public SimpleForm getDurationForm() { return this.durationForm; }
	
}
