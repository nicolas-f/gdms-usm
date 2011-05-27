/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gdms.usm;

import com.vividsolutions.jts.geom.Geometry;
import java.util.List;

/**
 * 
 * @author Thomas Salliou
 */
public class Plot {

    private int buildType;
    private int density;
    private int maxDensity;
    private final int amenitiesIndex;
    private final int constructibilityIndex;
    private final double inverseArea;
    private final int inseeCode;
    private String zoning;
    private final Geometry the_geom;
    private List<Household> householdList;

    /**
     * Builds a new Plot.
     * @param bT a build type
     * @param d a density
     * @param mD a maximum density
     * @param aI an amenities index
     * @param iA the inverse of the area
     * @param cI a constructibility index
     * @param iC an insee code
     * @param z a zoning
     * @param geom a geometry
     */
    public Plot(int bT, int d, int mD, int aI, int iA, int cI, int iC, String z, Geometry geom) {

        this.buildType = bT;
        this.density = d;
        this.maxDensity = mD;
        this.amenitiesIndex = aI;
        this.constructibilityIndex = cI;
        this.inseeCode = iC;
        this.zoning = z;
        this.the_geom = geom;
        this.inverseArea = iA;

    }

    /**
     * Adds the Household to the Household list and increases the density.
     * @param movingHousehold a household moving in
     */
    public void moveIn(Household movingHousehold) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Removes the Household from the Household list and decreases the density.
     * @param movingHousehold a household moving out 
     */
    public void moveOut(Household movingHousehold) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Changes the build type if needed.
     */
    public void updateBuildType() {
        throw new UnsupportedOperationException("Not implemented yet");
    }
    
    /**
     * Indicates if the Plot is full or not.
     * @return a boolean
     */
    public boolean isFull() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Gets the number of households living in the Plot.
     * @return the population on the Plot
     */
    public int getLocalPopulation() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * @return the buildType
     */
    public int getBuildType() {
        return buildType;
    }

    /**
     * @return the density
     */
    public int getDensity() {
        return density;
    }

    /**
     * @return the maxDensity
     */
    public int getMaxDensity() {
        return maxDensity;
    }

    /**
     * @return the amenitiesIndex
     */
    public int getAmenitiesIndex() {
        return amenitiesIndex;
    }

    /**
     * @return the constructibilityIndex
     */
    public int getConstructibilityIndex() {
        return constructibilityIndex;
    }

    /**
     * @return the inseeCode
     */
    public int getInseeCode() {
        return inseeCode;
    }

    /**
     * @return the zoning
     */
    public String getZoning() {
        return zoning;
    }
}
