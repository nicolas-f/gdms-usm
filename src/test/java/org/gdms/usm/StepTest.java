/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gdms.usm;

import java.io.File;
import java.io.IOException;
import junit.framework.TestCase;
import org.gdms.data.DataSource;
import org.gdms.data.DataSourceCreationException;
import org.gdms.data.DataSourceFactory;
import org.gdms.data.NoSuchTableException;
import org.gdms.data.NonEditableDataSourceException;
import org.gdms.data.SpatialDataSourceDecorator;
import org.gdms.data.indexes.IndexException;
import org.gdms.driver.DriverException;

/**
 *
 * @author Thomas Salliou
 */
public class StepTest extends TestCase {
    
    public StepTest(String testName) {
        super(testName);
    }
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }
    
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        new File(outputPathForTests+"Household.gdms").delete();
        new File(outputPathForTests+"HouseholdState.gdms").delete();
        new File(outputPathForTests+"Plot.gdms").delete();
        new File(outputPathForTests+"PlotState.gdms").delete();
        new File(outputPathForTests+"Step.gdms").delete();
    }
    
    private String dataPathForTests = "src/test/resources/initialdatabase.gdms";
    private String outputPathForTests = "src/test/resources/";
    private BufferBuildTypeCalculator bbtc = new BufferBuildTypeCalculator();
    private StatisticalDecisionMaker sdm = new StatisticalDecisionMaker();
    private SchellingDecisionMaker schdm = new SchellingDecisionMaker();
    private GaussParcelSelector gps = new GaussParcelSelector();
    
    public void testInitialize() throws DataSourceCreationException, DriverException, NoSuchTableException, NonEditableDataSourceException, IOException, IndexException {
        Step s = new Step(2000, dataPathForTests, outputPathForTests, bbtc, sdm, gps);
        Manager m = s.getManager();
        s.initialize();
        
        //This test tests exactly the same assertions as in the 3 called methods :
        //initializeSimulation, initializeOutputDatabase, and setNeighbours.
        //The purpose is only to see if these methods are called properly.
        
        //initializeSimulation part
        assertTrue(m.getParcelList().size() == 6978);
        assertTrue(m.getParcelList().get(3).getId() == 3);
        assertTrue(m.getParcelList().get(3).getBuildType() == 2);
        assertTrue(Math.abs(m.getParcelList().get(3).getDensity()- 0.000177042857497) < 0.000000001);
        assertTrue(Math.abs(m.getParcelList().get(3).getMaxDensity()- 0.000155) < 0.000000001);
        assertTrue(m.getParcelList().get(3).getAmenitiesIndex() == 13);
        assertTrue(m.getParcelList().get(3).getConstructibilityIndex() == 16);
        assertTrue(m.getParcelList().get(3).getInseeCode() == 44171);
        assertTrue("A".equals(m.getParcelList().get(3).getZoning()));
        assertTrue(m.getParcelList().get(3).getHouseholdList().size() == 22);
        assertTrue(m.getParcelList().contains(m.getParcelList().get(3).getHouseholdList().iterator().next().getHousingPlot()));
        assertTrue(m.getParcelList().get(3).getHouseholdList().iterator().next().getMaxWealth() > 34132);
        assertTrue(m.getParcelList().get(3).getHouseholdList().iterator().next().getMaxWealth() < 41717);
        assertTrue(m.getPopulation() == 262650);
        assertTrue(m.getParcelList().get(6977).getHouseholdList().isEmpty());
        assertTrue(m.getParcelList().get(17).getHouseholdList().size() == 34);
        assertTrue(m.getParcelList().contains(m.getParcelList().get(17).getHouseholdList().iterator().next().getHousingPlot()));
        assertTrue(m.getParcelList().get(17).getHouseholdList().iterator().next().getMaxWealth() > 31718);
        assertTrue(m.getParcelList().get(17).getHouseholdList().iterator().next().getMaxWealth() < 38767);
        
        //initializeOutputDatabase part
        DataSourceFactory dsf = m.getDsf();
        DataSource householdDS = dsf.getDataSource("Household");
        DataSource plotDS = dsf.getDataSource("Plot");
        DataSource householdStateDS = dsf.getDataSource("HouseholdState");
        DataSource plotStateDS = dsf.getDataSource("PlotState");
        DataSource stepDS = dsf.getDataSource("Step");
        SpatialDataSourceDecorator plotSDS = new SpatialDataSourceDecorator(plotDS);
        householdDS.open();
        plotSDS.open();
        householdStateDS.open();
        plotStateDS.open();
        stepDS.open();
        assertTrue(new File(outputPathForTests+"Household.gdms").exists());
        assertTrue(new File(outputPathForTests+"HouseholdState.gdms").exists());
        assertTrue(new File(outputPathForTests+"Plot.gdms").exists());
        assertTrue(new File(outputPathForTests+"PlotState.gdms").exists());
        assertTrue(new File(outputPathForTests+"Step.gdms").exists());
        assertTrue(householdDS.getMetadata().getFieldName(1).equals("maximumWealth"));
        assertTrue(householdStateDS.getMetadata().getFieldName(3).equals("age"));
        assertTrue(plotDS.getMetadata().getFieldName(1).equals("the_geom"));
        assertTrue(plotDS.getMetadata().getFieldType(1).getTypeCode() == 4096);
        assertTrue(plotStateDS.getMetadata().getFieldName(2).equals("buildType"));
        assertTrue(stepDS.getMetadata().getFieldName(0).equals("stepNumber"));
        assertTrue(householdDS.getRowCount() == 262650);
        assertTrue(plotSDS.getRowCount() == 6978);
        assertTrue(m.getDsf().getIndexManager().isIndexed("Plot", "the_geom"));
        householdDS.close();
        plotSDS.close();
        householdStateDS.close();
        plotStateDS.close();
        stepDS.close();
        
        //setNeighbours part
        BufferBuildTypeCalculator a = (BufferBuildTypeCalculator) m.getNbtc();
        Parcel[] myNeighbours = a.getNeighbours(m.getParcelList().get(3425));
        assertTrue(myNeighbours.length == 7);
        assertTrue(myNeighbours[0].getId() == 3440);
        assertTrue(myNeighbours[1].getId() == 3383);
        assertTrue(myNeighbours[2].getId() == 3382);
        assertTrue(myNeighbours[3].getId() == 3439);
        assertTrue(myNeighbours[4].getId() == 3404);
        assertTrue(myNeighbours[5].getId() == 3438);
        assertTrue(myNeighbours[6].getId() == 3403);
    }
}