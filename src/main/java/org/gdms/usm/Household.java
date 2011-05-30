/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gdms.usm;

import java.util.Queue;

/**
 *
 * @author Thomas Salliou
 */
public class Household {
    
    private final int id;
    private int age;
    private final int maxWealth;
    private Queue<Integer> dissatisfactionMemory;
    private Parcel housingPlot;
    
    /**
     * Builds a new Household with given age and maximum wealth.
     * @param a
     * @param mW 
     */
    public Household(int id, int a, int mW){
        this.id = id;
        this.age = a;
        this.maxWealth = mW;
    }
    
    /**
     * Builds a new Household with given age, maximum wealth and housing parcel.
     * @param a
     * @param mW
     * @param hP 
     */
    public Household(int id, int a, int mW, Parcel hP){
        this.id = id;
        this.age = a;
        this.maxWealth = mW;
        this.housingPlot = hP;
    }

    /**
     * @return the age
     */
    public int getAge() {
        return age;
    }

    /**
     * @return the maxWealth
     */
    public int getMaxWealth() {
        return maxWealth;
    }

    /**
     * @return the housingPlot
     */
    public Parcel getHousingPlot() {
        return housingPlot;
    }
    
    /**
     * Increments the household age.
     */
    public void grow(){
        this.age++;
    }
    
    /**
     * Gets the household's wealth.
     * @return the household's wealth
     */
    public int getWealth(){
        if (age < 60) return maxWealth*age/60;
        else return maxWealth;
    }
    
    /**
     * Gets the immediate dissatisfaction index.
     * @return the immediate dissatisfaction index
     */
    public int getImmediateDissatisfaction(){
        throw new UnsupportedOperationException("Not implemented yet");
    }
    
    /**
     * Gets the cumulated dissatisfaction index, based on the dissatisfaction memory.
     * @return the cumulated dissatisfaction index.
     */
    public int getCumulatedDissatisfaction(){
        throw new UnsupportedOperationException("Not implemented yet");
    }
    
    /**
     * Gets the willingness-to-move coefficient.
     * @return the willingness to move coefficient
     */
    public int getWillMoveCoefficient(){
        int wmc;
        
        //is it Nantes or not
        if (this.housingPlot.getInseeCode() == 44109) wmc=12;
        else wmc=6;
        
        //how about the age bracket
        if      (this.age < 25) wmc+=36;
        else if (this.age < 35) wmc+=30;
        else if (this.age < 50) wmc+=18;
        else if (this.age < 65) wmc+=8;
        else                    wmc+=2;
        
        return wmc;
    }
    
    /**
     * Gets the ideal housing coefficient.
     * @return the ideal housing coefficient
     */
    public int getIdealHousingCoefficient(){
        
        int ihcByWealth = 66;
        int ihcByAge = 66;
        
        switch(this.housingPlot.getBuildType()) {
            
            case 1:
                if      (this.getWealth() < 18000) ihcByWealth = 62;
                else if (this.getWealth() < 25200) ihcByWealth = 67;
                else if (this.getWealth() < 35400) ihcByWealth = 56;
                else if (this.getWealth() < 45000) ihcByWealth = 57;
                else                               ihcByWealth = 52;
                
                if      (this.getAge() < 25)       ihcByAge = 43;
                else if (this.getAge() < 35)       ihcByAge = 58;
                else if (this.getAge() < 50)       ihcByAge = 53;
                else if (this.getAge() < 65)       ihcByAge = 63;
                else                               ihcByAge = 81;
                break;
            case 2:
                if      (this.getWealth() < 18000) ihcByWealth = 62;
                else if (this.getWealth() < 25200) ihcByWealth = 61;
                else if (this.getWealth() < 35400) ihcByWealth = 55;
                else if (this.getWealth() < 45000) ihcByWealth = 49;
                else                               ihcByWealth = 69;
                
                if      (this.getAge() < 25)       ihcByAge = 81;
                else if (this.getAge() < 35)       ihcByAge = 54;
                else if (this.getAge() < 50)       ihcByAge = 53;
                else if (this.getAge() < 65)       ihcByAge = 59;
                else                               ihcByAge = 61;
                break;
            case 3: case 4:
                if      (this.getWealth() < 18000) ihcByWealth = 77;
                else if (this.getWealth() < 25200) ihcByWealth = 72;
                else if (this.getWealth() < 35400) ihcByWealth = 89;
                else if (this.getWealth() < 45000) ihcByWealth = 94;
                else                               ihcByWealth = 79;
                
                if      (this.getAge() < 25)       ihcByAge = 76;
                else if (this.getAge() < 35)       ihcByAge = 88;
                else if (this.getAge() < 50)       ihcByAge = 94;
                else if (this.getAge() < 65)       ihcByAge = 79;
                else                               ihcByAge = 58;
                break;
        }
        
        return (ihcByAge+ihcByWealth)/2;
        
    }
    
    /**
     * @return the id 
     */
    public int getId() {
        return id;
    }
}
