/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gdms.usm;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 *
 * @author Thomas Salliou
 */
public final class StatisticalDecisionMaker extends IsMovingDecisionMaker {

    private Map<Household, LimitedQueue<Double>> dissatisfactionMemories;
    public static final int HOUSEHOLD_MEMORY = 3;
    public static final double MOVING_THRESHOLD = 30;

    public StatisticalDecisionMaker() {
        dissatisfactionMemories = new HashMap<Household, LimitedQueue<Double>>();
    }

    @Override
    public boolean isMoving(Household h) {
        addToDissatisfactionQueue(h, getImmediateDissatisfaction(h));
        double cumulatedDissatisfaction = getCumulatedDissatisfaction(h);
        if (cumulatedDissatisfaction > MOVING_THRESHOLD) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Gets the willingness-to-move coefficient for the specified Household.
     * @return the willingness to move coefficient
     */
    public int getWillMoveCoefficient(Household h) {
        int wmc;

        //is it Nantes or not
        if (h.getHousingPlot().getInseeCode() == 44109) {
            wmc = 12;
        } else {
            wmc = 6;
        }

        //how about the age bracket
        if (h.getAge() < 25) {
            wmc += 36;
        } else if (h.getAge() < 35) {
            wmc += 30;
        } else if (h.getAge() < 50) {
            wmc += 18;
        } else if (h.getAge() < 65) {
            wmc += 8;
        } else {
            wmc += 2;
        }

        return wmc;
    }

    /**
     * Gets the immediate dissatisfaction index of the specified household.
     * @return the immediate dissatisfaction index
     */
    public double getImmediateDissatisfaction(Household h) {
        double amenitiesPart = (20.0 - h.getHousingPlot().getAmenitiesIndex()) / 20.0;
        double willMoveCoeffPart = getWillMoveCoefficient(h) / 48.0;
        double idealHousingCoeffPart = h.getIdealHousingCoefficient() / 100.0;

        return amenitiesPart + willMoveCoeffPart + idealHousingCoeffPart;
    }

    /**
     * Adds a double to the dissatisfaction limited queue of the specified household.
     */
    public void addToDissatisfactionQueue(Household h, double immdis) {
        dissatisfactionMemories.get(h).add(immdis);
    }

    /**
     * Gets the cumulated dissatisfaction index, based on the dissatisfaction memory, for a specified household.
     * @return the cumulated dissatisfaction index.
     */
    public double getCumulatedDissatisfaction(Household h) {
        Iterator<Double> i = dissatisfactionMemories.get(h).iterator();
        double cumulatedDissatisfaction = 0;
        while (i.hasNext()) {
            cumulatedDissatisfaction += i.next();
        }
        return cumulatedDissatisfaction;
    }

    /**
     * Gets the ideal housing coefficient for a specified household.
     * @return the ideal housing coefficient
     */
    public int getIdealHousingCoefficient(Household h) {
        return h.getIdealHousingCoefficient();
    }

    /**
     * Returns the dissatisfactionMemory of the specified Household.
     * @return the dissatisfactionMemory 
     */
    public LimitedQueue<Double> getDissatisfactionMemory(Household h) {
        return dissatisfactionMemories.get(h);
    }

    public void addHousehold(Household h) {
        dissatisfactionMemories.put(h, new LimitedQueue<Double>(HOUSEHOLD_MEMORY));
    }

    public void deleteHousehold(Household h) {
        dissatisfactionMemories.remove(h);
    }
}