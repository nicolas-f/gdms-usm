/**
 *
 * Gdms-USM is a library dedicated to multi-agent simulation for modeling urban sprawl.
 * It is based on the GDMS library. It uses the OrbisGIS renderer to display results.
 *
 * This version is developed at French IRSTV Institute and at LIENSs UMR 7266 laboratory
 * (http://lienss.univ-larochelle.fr/) as part of the VegDUD project, funded by the
 * French Agence Nationale de la Recherche (ANR) under contract ANR-09-VILL-0007.
 *
 * Gdms-USM is distributed under GPL 3 license. It is maintained by the "Atelier SIG" team of
 * the IRSTV Institute <http://www.irstv.fr/> CNRS FR 2488.
 *
 * Copyright (C) 2011-2012 IRSTV (FR CNRS 2488)
 *
 * Gdms-USM is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * Gdms-USM is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * Gdms-USM. If not, see <http://www.gnu.org/licenses/>.
 *
 * For more information, please consult: <http://trac.orbisgis.org/>
 * or contact directly:
 * info_at_ orbisgis.org
 */
package org.gdms.usm;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;
import java.io.File;
import java.io.IOException;
import junit.framework.TestCase;
import org.gdms.data.DataSourceCreationException;
import org.gdms.data.DataSourceFactory;
import org.gdms.data.NoSuchTableException;
import org.gdms.data.NonEditableDataSourceException;
import org.gdms.data.indexes.IndexException;
import org.gdms.driver.DriverException;

/**
 *
 * @author Thomas Salliou
 */
public class ParcelTest extends TestCase {
    
    public ParcelTest(String testName) {
        super(testName);
    }
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }
    
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        new File(outputPathForTests+"/Household.gdms").delete();
        new File(outputPathForTests+"/HouseholdState.gdms").delete();
        new File(outputPathForTests+"/Plot.gdms").delete();
        new File(outputPathForTests+"/PlotState.gdms").delete();
        new File(outputPathForTests+"/Step.gdms").delete();
    }

    private String dataPathForTests = "src/test/resources/initialdatabase.gdms";
    private String globalsPathForTests = "src/test/resources/globals.gdms";
    private String outputPathForTests = "src/test/resources";
    private DataSourceFactory dsf = new DataSourceFactory(outputPathForTests + "/gdms");
    private BufferBuildTypeCalculator bbtc = new BufferBuildTypeCalculator();
    private StatisticalDecisionMaker sdm = new StatisticalDecisionMaker();
    private GaussParcelSelector gps = new GaussParcelSelector();
    
    /**
     * Builds a Parcel with given density and maxDensity, and default values for the rest, for test purposes.
     * @param density
     * @param maxDensity
     * @return
     * @throws ParseException 
     */
    private Parcel defaultParcelBuilder(double density, double maxDensity) throws ParseException{
        WKTReader wktr = new WKTReader();
        Geometry geometry = wktr.read("MULTIPOLYGON (((30 20, 10 40, 45 40, 30 20)),((15 5, 40 10, 10 20, 5 10, 15 5)))");
        return new Parcel(7,2,density,maxDensity,10,10,10,50,44109,"AB",geometry, bbtc);
    }
    
    /**
     * Tests if the isFull method is doing its job for both cases.
     * @throws ParseException 
     */
    public void testIsFull() throws ParseException{
        Parcel rez = defaultParcelBuilder(30,40);
        assertTrue(!rez.isFull());
        
        Parcel rez2 = defaultParcelBuilder(30,30.0016);
        assertTrue(rez2.isFull());
    }
    
    public void testMoveInAndOut() throws ParseException {
        WKTReader wktr = new WKTReader();
        Geometry geometry = wktr.read("POLYGON((0 0, 0 10, 10 10, 10 0, 0 0))");
        
        Parcel rez = new Parcel(8,1,2,2,2,20,10,50,44109,"AB",geometry, bbtc);
        Household movingInHousehold = new Household(9,40,50000);
        
        rez.addHousehold(movingInHousehold);
        assertTrue(Math.abs(rez.getDensity()-2.01) < 0.000001);
        assertTrue(rez.getHouseholdList().contains(movingInHousehold));
        assertTrue(rez.getHouseholdList().size() == 1);
        
        rez.removeHousehold(movingInHousehold);
        assertTrue(rez.getHouseholdList().isEmpty());
        assertTrue(Math.abs(rez.getDensity()-2.00) < 0.000001);
    }
    
    public void testGetLocalPopulation() throws ParseException {
        WKTReader wktr = new WKTReader();
        Geometry geometry = wktr.read("POLYGON((0 0, 0 10, 10 10, 10 0, 0 0))");
        
        Parcel rez = new Parcel(8,1,2,20,20,20,10,50,44109,"AB",geometry, bbtc);
        Household dupont = new Household(9,40,50000);
        Household dupond = new Household(10,47,48000);
        Household dhupondt = new Household(11,24,78000);
        
        rez.addHousehold(dupont);
        rez.addHousehold(dupond);
        rez.addHousehold(dhupondt);
        
        assertTrue(rez.getLocalPopulation() == 3);
    }
    
    public void testGetAverageWealth() throws ParseException {
        WKTReader wktr = new WKTReader();
        Geometry geometry = wktr.read("POLYGON((0 0, 0 10, 10 10, 10 0, 0 0))");
        
        Parcel parisSeizieme = new Parcel(8,1,2,20,20,20,10,50,44109,"AB",geometry, bbtc);
        Household firstHousehold = new Household(1,24,48752); //19500
        Household secondHousehold = new Household(2,35,143258); //83567
        Household thirdHousehold = new Household(3,68,26587); //26587
        Household fourthHousehold = new Household(4,47,49852); //39050
        Household fifthHousehold = new Household(5,25,69703); //29042
        Household sixthHousehold = new Household(6,64,87012); //87012
        
        assertTrue(parisSeizieme.getAverageWealth() == 0);
        
        parisSeizieme.addHousehold(firstHousehold);
        parisSeizieme.addHousehold(secondHousehold);
        parisSeizieme.addHousehold(thirdHousehold);
        parisSeizieme.addHousehold(fourthHousehold);
        parisSeizieme.addHousehold(fifthHousehold);
        parisSeizieme.addHousehold(sixthHousehold);
        
        //Wealth is an integer, averageWealth too, be careful !
        assertTrue(parisSeizieme.getAverageWealth() == 47459);
    }
    
    public void testUpdateBuildType() throws ParseException {
        WKTReader wktr = new WKTReader();
        Geometry geometry = wktr.read("POLYGON((0 0, 0 10, 10 10, 10 0, 0 0))");
        
        Parcel rez = new Parcel(8,1,0.000002,20,20,20,10,50,44109,"AB",geometry, bbtc);
        
        rez.updateBuildType(0, 0.000155, 0.001000, 0.001466);
        assertTrue(rez.getBuildType() == 2);
        rez.updateBuildType(0, 0.000155, 0.001000, 0.001466);
        assertTrue(rez.getBuildType() == 2);
        
        Parcel rez2 = new Parcel(8,2,0.000158,2000,2000,2000,10,50,44109,"AB",geometry, bbtc);
        rez2.updateBuildType(0, 0.000155, 0.001000, 0.001466);
        assertTrue(rez2.getBuildType() == 3);
        
        Parcel rez3 = new Parcel(8,1,0.001200,2000,2000,2000,10,50,44109,"AB",geometry, bbtc);
        rez3.updateBuildType(0, 0.000155, 0.001000, 0.001466);
        assertTrue(rez3.getBuildType() == 4);
        
        Parcel rez4 = new Parcel(8,5,0.001852,2000,2000,2000,10,50,44109,"AB",geometry, bbtc);
        assertTrue(rez4.getBuildType() == 5);
    }
    
    public void testGetUpgradePotential() throws DataSourceCreationException, DriverException, NoSuchTableException, NonEditableDataSourceException, IOException, IndexException {
        Step s = new Step(2000, dataPathForTests, globalsPathForTests, outputPathForTests, bbtc, sdm, gps, dsf);
        Manager m = new Manager(s, dataPathForTests, globalsPathForTests, outputPathForTests, bbtc, sdm, gps, dsf);
        bbtc.setManager(m);
        m.initializeGlobals();
        m.initializeSimulation();
        m.initializeOutputDatabase();
        m.getNbtc().setNeighbours();
        
        assertTrue(Math.abs(m.getParcelList().get(412).getUpgradePotential() - 0.9658773693035715) < 0.000001);
    }
      
}
