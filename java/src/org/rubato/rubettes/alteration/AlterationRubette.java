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

package org.rubato.rubettes.alteration;

import static org.rubato.xml.XMLConstants.FORM;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Window;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import org.rubato.base.AbstractRubette;
import org.rubato.base.Repository;
import org.rubato.base.RubatoConstants;
import org.rubato.base.Rubette;
import org.rubato.base.RubatoException;
import org.rubato.composer.RunInfo;
import org.rubato.composer.Utilities;
import org.rubato.composer.components.JSelectForm;
import org.rubato.composer.components.JStatusline;
import org.rubato.math.module.RElement;
import org.rubato.math.module.RRing;
import org.rubato.math.yoneda.Denotator;
import org.rubato.math.yoneda.Form;
import org.rubato.math.yoneda.NameDenotator;
import org.rubato.math.yoneda.PowerForm;
import org.rubato.math.yoneda.PowerDenotator;
import org.rubato.xml.XMLReader;
import org.rubato.xml.XMLWriter;
import org.w3c.dom.Element;

/**
 * A rubette for performing the alteration of a denotator towards another.
 * 
 * @author Florian Thalmann
 */
public class AlterationRubette extends AbstractRubette implements ActionListener, FocusListener {
	
	private NameDenotator emptyName = NameDenotator.make("");
	
	private PowerForm inputForm;
	private boolean global;
	private double globalStartDegree;
	private double globalEndDegree;
	
	private JPanel propertiesPanel;
	private JSelectForm selectInputForm;
	private JCheckBox globalCheckBox;
	private JTextField globalStartDegreeField, globalEndDegreeField;
	private JButton addDimensionButton, removeDimensionButton,
		dimensionUpButton, dimensionDownButton;
	private JAlterationDimensionsTable dimensionsTable;
	private JStatusline statusline;
	
	private NearestNeighborFinder neighborFinder;
	private Alterator alterator;
	
	/**
	 * Creates a basic AlterationRubette.
	 */
	public AlterationRubette() {
		this.setInCount(2);
		this.setOutCount(1);
		this.globalStartDegree = 0.0;
		this.globalEndDegree = 1.0;
		this.selectInputForm = new JSelectForm(Repository.systemRepository(), Form.POWER);
		this.dimensionsTable = new JAlterationDimensionsTable(this);
		this.statusline = new JStatusline();
		this.alterator = new Alterator();
	}

	@Override
	public String getName() {
		return "Alteration";
	}

	@Override
	public Rubette newInstance() {
		return new AlterationRubette();
	}
		
    @Override
    public Rubette duplicate() {
        // TODO: This must be correctly implemented
        return newInstance();
    }

    public void init() {}

	@Override
	public void run(RunInfo arg0) {
		PowerDenotator input0 = (PowerDenotator) this.getInput(0);
		PowerDenotator input1 = (PowerDenotator) this.getInput(1);
		if (this.inputFormAndDenotatorsValid(input0, input1)) {
			try {
				PowerDenotator output = this.getMorphing(input0, input1);
				this.setOutput(0, output);
			} catch (RubatoException e) {
				e.printStackTrace();
				this.addError("Error during rubette execution.");
			}
		}
	}
	
	protected boolean inputFormAndDenotatorsValid(PowerDenotator input0, PowerDenotator input1) {
		if (this.inputForm == null) {
            this.addError("Input form not set.");
        } else if (input0 == null) {
            this.addError("Input denotator #0 is null.");
        } else if (input1 == null) {
            this.addError("Input denotator #1 is null.");
        } else if (!input0.getForm().equals(this.inputForm)) {
			this.addError("Input #0 is of the wrong form.");
		} else if (!input1.getForm().equals(this.inputForm)) {
			this.addError("Input #1 is of the wrong form.");
		}
		return !this.hasErrors();
	}
	
	protected PowerDenotator getMorphing(PowerDenotator input0, PowerDenotator input1) throws RubatoException {
		List<Denotator> morphedFactors = new ArrayList<Denotator>();
		Iterator<Denotator> input0Coordinates = input0.iterator();
		int[][] differentPaths = this.dimensionsTable.getDifferentRelativeToFormPaths();
		int[][] allPaths = this.dimensionsTable.getRelativeToFormPaths();
		int[] pathIndices = this.getIndicesOf(allPaths, differentPaths);
		double[] startDegrees = this.dimensionsTable.getStartDegrees();
		double[] endDegrees = this.dimensionsTable.getEndDegrees();
		double[][] minAndMax = this.getMinAndMaxDouble(input0, differentPaths);
		this.neighborFinder = new NearestNeighborFinder(input1, this.dimensionsTable.getElementPaths());
		while (input0Coordinates.hasNext()) {
			Denotator currentDenotator = input0Coordinates.next();
			Denotator nearestNeighbour = this.neighborFinder.findNearestNeighbor(currentDenotator);
			Denotator morphedDenotator;
			//need to copy both denotators, since copy makes an address change so sum fails
			morphedDenotator = this.morphDenotator(currentDenotator.copy(), nearestNeighbour.copy(), pathIndices, differentPaths, minAndMax, startDegrees, endDegrees);
			morphedFactors.add(morphedDenotator);
		}
		return new PowerDenotator(this.emptyName, input0.getPowerForm(), morphedFactors);
	}
	
	protected int[] getIndicesOf(int[][] array1, int[][] array2) {
		int[] indices = new int[array1.length];
		for (int i = 0; i < indices.length; i++) {
			indices[i] = -1;
			for (int j = 0; j < array2.length; j++) {
				if (Arrays.equals(array2[j], array1[i])) {
					indices[i] = j;
				}
			}
		}
		return indices;
	}
	
	protected double[][] getMinAndMaxDouble(PowerDenotator denotator, int[][] paths) throws RubatoException {
		double[] minima = new double[paths.length];
		double[] maxima = new double[paths.length];
		Iterator<Denotator> subDenotators = denotator.iterator();
		//set starting values
		Denotator currentDenotator = subDenotators.next();
		for (int i = 0; i < paths.length; i++) {
			double currentDouble = ((RElement)currentDenotator.getElement(paths[i]).cast(RRing.ring)).getValue();
			minima[i] = currentDouble;
			maxima[i] = currentDouble;
		}
		//calculate mins and maxes
		while (subDenotators.hasNext()) {
			currentDenotator = subDenotators.next();
			for (int i = 0; i < paths.length; i++) {
				double currentDouble = ((RElement)currentDenotator.getElement(paths[i]).cast(RRing.ring)).getValue();
				if (currentDouble > maxima[i]) {
					maxima[i] = currentDouble;
				}
				if (currentDouble < minima[i]) {
					minima[i] = currentDouble;
				}
			}
		}
		return new double[][]{minima, maxima};
	}
	
	protected Denotator morphDenotator(Denotator morphed, Denotator pole, int[] indices, int[][] differentPaths, double[][] minAndMax, double[] startDegrees, double[] endDegrees) throws RubatoException {
		int[][] allPaths = this.dimensionsTable.getPaths();
		double[] doubleValues = this.getCastDoubleValues(morphed, differentPaths);
		double[] degrees = this.getInterpolatedDegrees(startDegrees, endDegrees, doubleValues, minAndMax, indices);
		
		return this.alterator.morphDenotator(morphed, pole, allPaths, degrees);
	}
	
	private double[] getCastDoubleValues(Denotator denotator, int[][] paths) throws RubatoException{
		double[] values = new double[paths.length];
		for (int i = 0; i < values.length; i++) {
			values[i] = ((RElement)denotator.getElement(paths[i]).cast(RRing.ring)).getValue();
		}
		return values;
	}
	
	protected double[] getInterpolatedDegrees(double[] startDegrees, double[] endDegrees, double[] values, double[][] minAndMax, int[] indices) {
		double[] interpolated = new double[startDegrees.length];
		for (int i = 0; i < startDegrees.length; i++) {
			int j = indices[i];
			interpolated[i] = this.getInterpolatedDegree(startDegrees[i], endDegrees[i], values[j], minAndMax[0][j], minAndMax[1][j]);
		}
		return interpolated;
	}
	
	protected double getInterpolatedDegree(double startDegree, double endDegree, double value, double min, double max) {
		double relativeValue = (value-min)/(max-min);
		return relativeValue*(endDegree-startDegree)+startDegree;
	}
	
	/**
     * Returns the fact that this rubette has properties.
     */
    public boolean hasProperties() {
        return true;
    }

    /**
     * Returns the properties panel for wallpaper specification.
     */
    public JComponent getProperties() {
        if (this.propertiesPanel == null) {
            this.propertiesPanel = new JPanel();            
            this.propertiesPanel.setLayout(new BorderLayout());
			
            this.selectInputForm.setBorder(Utilities.makeTitledBorder("Input form"));
            this.selectInputForm.addActionListener(this);
            this.propertiesPanel.add(this.selectInputForm, BorderLayout.NORTH);
            
            JPanel dimensionsPanel = new JPanel();
			dimensionsPanel.setLayout(new BorderLayout());
			dimensionsPanel.setBorder(Utilities.makeTitledBorder("Alteration Dimensions"));
			dimensionsPanel.setMinimumSize(new Dimension(0, 50));
			
			JPanel globalDegreesPanel = new JPanel();
			this.globalCheckBox = new JCheckBox("Global degree ");
			this.globalCheckBox.setHorizontalTextPosition(SwingConstants.LEFT);
			this.globalCheckBox.addActionListener(this);
			this.globalStartDegreeField = new JTextField(this.globalStartDegree+"", 5);
			this.globalStartDegreeField.addFocusListener(this);
			this.globalEndDegreeField = new JTextField(this.globalEndDegree+"", 5);
			this.globalEndDegreeField.addFocusListener(this);
			globalDegreesPanel.add(this.globalCheckBox);
			globalDegreesPanel.add(new JLabel("Low "));
			globalDegreesPanel.add(this.globalStartDegreeField);
			globalDegreesPanel.add(new JLabel("High "));
			globalDegreesPanel.add(this.globalEndDegreeField);
			dimensionsPanel.add(globalDegreesPanel, BorderLayout.NORTH);
            
            JPanel dimensionButtonsPanel = new JPanel();
			this.addDimensionButton = new JButton("Add");
			this.addDimensionButton.addActionListener(this);
			this.removeDimensionButton = new JButton("Remove");
			this.removeDimensionButton.addActionListener(this);
			this.dimensionUpButton = new JButton("Up");
			this.dimensionUpButton.addActionListener(this);
			this.dimensionDownButton = new JButton("Down");
			this.dimensionDownButton.addActionListener(this);
			dimensionButtonsPanel.add(this.addDimensionButton);
			dimensionButtonsPanel.add(this.removeDimensionButton);
			dimensionButtonsPanel.add(this.dimensionUpButton);
			dimensionButtonsPanel.add(this.dimensionDownButton);
			dimensionsPanel.add(dimensionButtonsPanel, BorderLayout.CENTER);
			
			dimensionsPanel.add(this.dimensionsTable.getTableHeader(), BorderLayout.SOUTH);
			dimensionsPanel.add(this.dimensionsTable, BorderLayout.SOUTH);
			
			this.propertiesPanel.add(dimensionsPanel, BorderLayout.CENTER);
			this.propertiesPanel.add(this.statusline, BorderLayout.SOUTH);
			this.revertProperties();
        }
        return this.propertiesPanel;
    }
    
    /**
     * Applies the changes of the morphisms table and the rubette properties
     */
    public boolean applyProperties() {
        PowerForm tempInputForm = (PowerForm) this.selectInputForm.getForm();
        if (tempInputForm == null) {
        	this.setStatuslineText("Input form not selected");
			return false;
        }
        boolean tempGlobal = this.globalCheckBox.isSelected();
        double tempGlobalStartDegree = 0;
        double tempGlobalEndDegree = 0;
        if (tempGlobal) {
        	try {
        		tempGlobalStartDegree = Double.parseDouble(this.globalStartDegreeField.getText());
        		tempGlobalEndDegree = Double.parseDouble(this.globalEndDegreeField.getText());
        	} catch (NumberFormatException e) {
        		this.setStatuslineText("Global degrees are not valid numbers");
        		return false;
        	}
        }
        
        //last thing to do is apply the table's changes
        if (this.dimensionsTable.getRowCount() > 0) {
        	boolean tableChangesApplied = this.dimensionsTable.applyChanges();
			if (!tableChangesApplied) {
				return false;
			}
		}
        this.setInputForm(tempInputForm);
        this.global = tempGlobal;
        if (tempGlobal) {
        	this.globalStartDegree = tempGlobalStartDegree;
        	this.globalEndDegree = tempGlobalEndDegree;
        }
        this.setStatuslineText("");
        return true;
    }
    
    /**
	 * Reverts this properties and the changes to this JMorphingDimensionsTable. 
	 */
    public void revertProperties() {
        this.updateInputFormInProperties();
        this.globalCheckBox.setSelected(this.global);
        this.globalStartDegreeField.setText(new Double(this.globalStartDegree).toString());
        this.globalEndDegreeField.setText(new Double(this.globalEndDegree).toString());
		this.dimensionsTable.revertChanges();
		this.updateGlobalDegreeFields();
    }
    
    /*
     * sets this input form and updates properties window and internal variables
     */
    protected void setInputForm(PowerForm form) {
		this.inputForm = form;
		this.updateInputFormInProperties();
	}
    
    protected void setGlobal(boolean global) {
    	this.global = global;
    	this.updateGlobalDegreeInTable();
    }
    
    private void updateInputFormInProperties() {
    	this.selectInputForm.setForm(this.inputForm);
		this.dimensionsTable.setParentForm(this.inputForm);
    }
    
    /*
     * sets a text to the statusline located in the south of the properties window
     */
    protected void setStatuslineText(String text) {
    	this.statusline.setText(text);
    }
    
    /**
     * Processes the events comming from the JSelectForm and the JButtons in the properties window.
     */
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if (src == this.selectInputForm) {
        	this.dimensionsTable.setParentForm((PowerForm) this.selectInputForm.getForm());
		} else if (src == this.globalCheckBox) {
			this.updateGlobalDegreeFields();
			this.updateTempGlobalDegreeInTable();
		} else if (src == this.addDimensionButton) {
        	this.dimensionsTable.addDimension();
        	this.packPropertiesWindow();
		} else if (src == this.removeDimensionButton) {
			this.dimensionsTable.removeSelectedRows();
			this.packPropertiesWindow();
		} else if (src == this.dimensionUpButton) {
			this.dimensionsTable.moveSelectedRowsUp();
		} else if (src == this.dimensionDownButton) {
			this.dimensionsTable.moveSelectedRowsDown();
		}
    }
    
    /**
     * Updates the local degrees if the global degree changes.
     */
    public void focusLost(FocusEvent e) {
    	Object src = e.getSource();
    	if (src == this.globalStartDegreeField || src == this.globalEndDegreeField) {
    		this.updateTempGlobalDegreeInTable();
    	}
    }
    
    public void focusGained(FocusEvent e) {}
    
    private void updateGlobalDegreeFields() {
    	boolean tempGlobal = this.getTempGlobal();
    	this.globalStartDegreeField.setEnabled(tempGlobal);
    	this.globalEndDegreeField.setEnabled(tempGlobal);
    }
    
    private void updateGlobalDegreeInTable() {
    	if (this.global) {
    		this.dimensionsTable.setGlobalDegree(this.globalStartDegree, this.globalEndDegree);
    	} else {
    		this.dimensionsTable.removeGlobalDegree();
   		}
    }
    
    private void updateTempGlobalDegreeInTable() {
    	if (this.getTempGlobal()) {
    		try {
   				double tempStartDegree = Double.parseDouble(this.globalStartDegreeField.getText());
   				double tempEndDegree = Double.parseDouble(this.globalEndDegreeField.getText());
   				this.dimensionsTable.setGlobalDegree(tempStartDegree, tempEndDegree);
   			} catch (NumberFormatException e) {
    			this.setStatuslineText("Global degrees are not valid numbers");
    		}
    	} else {
    		this.dimensionsTable.removeGlobalDegree();
   		}
    }
    
    private boolean getTempGlobal() {
    	return this.globalCheckBox.isSelected();
    }
	
	/*
     * returns the input form currently selected in the properties window (not applied yet)
     */
	protected PowerForm getTempInputForm() {
		if (this.selectInputForm != null) {
			return (PowerForm)this.selectInputForm.getForm();
		} else {
			return this.inputForm;
		}
	}
	
	protected JAlterationDimensionsTable getDimensionsTable() {
		return this.dimensionsTable;
	}
    
    /*
	 * packs this properties window
	 */
	private void packPropertiesWindow() {
		Window propertiesWindow = Utilities.getWindow(this.propertiesPanel);
		if (propertiesWindow != null) {
			propertiesWindow.pack();
		}
	}
	
	/**
     * Returns the fact that MorphingRubette belongs to the core rubettes
     */
    public String getGroup() {
        return RubatoConstants.CORE_GROUP;
    }
	
	public boolean hasInfo() {
        return true;
    }
	
	public String getInfo() {
    	String status = this.statusline.getText();
        if (!status.equals("") && !status.equals("Info")) {
            return status;
        } else if (this.inputForm == null) {
            return "Input form not set";
        } else {
        	return this.inputForm.getNameString() + ": " + this.inputForm.getTypeString() + "\n" 
    		+ this.dimensionsTable.dimensionCount() + " dimensions";
        }
    }
    
    public String getShortDescription() {
        return "Performs a morphing between two power denotators";
    }

    public String getLongDescription() {
        return "Performs a morphing between simple forms of two power denotators.";        
    }
    
    public String getInTip(int i) {
        return "Input power denotator #" + i;
    }
    
    public String getOutTip(int i) {
        return "Output power denotator";
    }
    
    private static final String GLOBAL = "Global";
    private static final String START = "start";
    private static final String END = "end";
    
    public void toXML(XMLWriter writer) {
        if (this.inputForm != null) {
            writer.writeFormRef(this.inputForm);
            if (this.global) {
            	writer.empty(GLOBAL,
            		START, this.globalStartDegree,
            		END, this.globalEndDegree);
            }
            
            this.dimensionsTable.toXML(writer);
        }
    }
	
    public Rubette fromXML(XMLReader reader, Element element) {
		AlterationRubette loadedRubette = new AlterationRubette();
		Element child = XMLReader.getChild(element, FORM);
		loadedRubette.setInputForm((PowerForm) reader.parseAndResolveForm(child));
		
		Element nextChild = XMLReader.getNextSibling(child, GLOBAL);
		if (nextChild != null) {
			loadedRubette.globalStartDegree = XMLReader.getRealAttribute(nextChild, START, 0);
			loadedRubette.globalEndDegree = XMLReader.getRealAttribute(nextChild, END, 1);
			loadedRubette.setGlobal(true);
			child = nextChild;
		}
		
		loadedRubette.dimensionsTable.fromXML(reader, child);
		
		return loadedRubette;
    }

}
