/**
 * Copyright (C) 2010, 2011 by Arne Kesting, Martin Treiber,
 *                             Ralph Germ, Martin Budden
 *                             <info@movsim.org>
 * ----------------------------------------------------------------------
 * 
 *  This file is part of 
 *  
 *  MovSim - the multi-model open-source vehicular-traffic simulator 
 *
 *  MovSim is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  MovSim is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with MovSim.  If not, see <http://www.gnu.org/licenses/> or
 *  <http://www.movsim.org>.
 *  
 * ----------------------------------------------------------------------
 */
package org.movsim.input.model.output.impl;

import org.jdom.Element;
import org.movsim.input.model.output.TrajectoriesInput;

// TODO: Auto-generated Javadoc
/**
 * The Class TrajectoriesInputImpl.
 */
public class TrajectoriesInputImpl implements TrajectoriesInput{

	/** The dt. */
	private double dt;
	
	/** The start time. */
	private double startTime;
	
	/** The end time. */
	private double endTime;
	
	/** The start position. */
	private double startPosition;
	
	/** The end position. */
	private double endPosition;
	
	/** The is initialized. */
	private boolean isInitialized;
	
	
	
	 /**
 	 * Instantiates a new trajectories input impl.
 	 *
 	 * @param elem the elem
 	 */
 	public TrajectoriesInputImpl(Element elem) {
		 if (elem == null) {
			 isInitialized = false;
			 return;
		 }
		 
		 dt = Double.parseDouble(elem.getAttributeValue("dt"));
		 startTime = 60*Double.parseDouble(elem.getAttributeValue("t_start_min"));
		 endTime = 60*Double.parseDouble(elem.getAttributeValue("t_end_min"));
		 startPosition = 1000*Double.parseDouble(elem.getAttributeValue("x_start_km"));
		 endPosition = 1000*Double.parseDouble(elem.getAttributeValue("x_end_km"));
		 isInitialized = true;
	 }



	/**
	 * Gets the dt.
	 *
	 * @return the dt
	 */
	public double getDt() {
		return dt;
	}



	/**
	 * Gets the start time.
	 *
	 * @return the startTime
	 */
	public double getStartTime() {
		return startTime;
	}



	/**
	 * Gets the end time.
	 *
	 * @return the endTime
	 */
	public double getEndTime() {
		return endTime;
	}



	/**
	 * Gets the start position.
	 *
	 * @return the startPosition
	 */
	public double getStartPosition() {
		return startPosition;
	}



	/**
	 * Gets the end position.
	 *
	 * @return the endPosition
	 */
	public double getEndPosition() {
		return endPosition;
	}



	/**
	 * Checks if is initialized.
	 *
	 * @return the isInitialized
	 */
	public boolean isInitialized() {
		return isInitialized;
	}




}