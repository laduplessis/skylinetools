package skylinetools.distributions;

import beast.core.parameter.RealParameter;
import junit.framework.TestCase;
import org.junit.Test;
import skylinetools.distributions.OUPrior;
import test.beast.BEASTTestCase;

/**
 * @author Louis du Plessis
 *         Date: 2018/07/09
 */
public class OUPriorLkTest extends TestCase {


    /**
     * Test that log-likelihood is calculated correctly for a single point
     */
    @Test
    public void testCalculateLogPSingle1() {

        System.out.println("OUPrior log-likelihood (single point 1)");

        // Parameters of the OU-process
        RealParameter x     = new RealParameter();
        RealParameter t     = new RealParameter();
        RealParameter mu    = new RealParameter();
        RealParameter sigma = new RealParameter();
        RealParameter nu    = new RealParameter();

        // Initialise values
        x.initByName("value","3.0 1.0");
        t.initByName("value","0.0 1.0");
        mu.initByName("value","1");
        sigma.initByName("value","1");
        nu.initByName("value","5");

        // Set up OUPrior instance and evaluate likelihood
        OUPrior likelihood = new OUPrior();
        likelihood.initByName("x",x,"times",t,"mean",mu,"sigma",sigma,"nu",nu);

        double expected = 1.150407; // Calculated in R
        double result = likelihood.calculateLogP();

        System.out.println(expected+"\t"+result);
        assertEquals(expected, result, BEASTTestCase.PRECISION);
    }


    /**
     * Test that log-likelihood is calculated correctly for a trajectory of points (all equal)
     */
    @Test
    public void testCalculateLogPTrajectory1() {

        System.out.println("OUPrior log-likelihood test (trajectory 1: all equal)");

        // Parameters of the OU-process
        RealParameter x     = new RealParameter();
        RealParameter t     = new RealParameter();
        RealParameter mu    = new RealParameter();
        RealParameter sigma = new RealParameter();
        RealParameter nu    = new RealParameter();

        // Initialise values
        x.initByName("value","5.0","dimension","11");
        t.initByName("value","0 1 2 3 4 5 6 7 8 9 10");
        mu.initByName("value","5");
        sigma.initByName("value","1");
        nu.initByName("value","2");

        // Set up OUPrior instance and evaluate likelihood
        OUPrior likelihood = new OUPrior();
        likelihood.initByName("x",x,"times",t,"mean",mu,"sigma",sigma,"nu",nu);

        double expected = 7.02389903972889; // Calculated in R
        double result = likelihood.calculateLogP();

        System.out.println(expected+"\t"+result);
        assertEquals(expected, result, BEASTTestCase.PRECISION);
    }

    /**
     * Test that log-likelihood is calculated correctly for a trajectory of points (all equal)
     */
    @Test
    public void testCalculateLogPTrajectory2() {

        System.out.println("OUPrior log-likelihood test (trajectory 2: evolving)");

        // Parameters of the OU-process
        RealParameter x     = new RealParameter();
        RealParameter t     = new RealParameter();
        RealParameter mu    = new RealParameter();
        RealParameter sigma = new RealParameter();
        RealParameter nu    = new RealParameter();

        // Initialise values
        x.initByName("value","10 9 8 7 6 5 5 5 5 6 5");
        t.initByName("value","0 1 2 3 4 5 6 7 8 9 10");
        mu.initByName("value","5");
        sigma.initByName("value","1");
        nu.initByName("value","2");

        // Set up OUPrior instance and evaluate likelihood
        OUPrior likelihood = new OUPrior();
        likelihood.initByName("x",x,"times",t,"mean",mu,"sigma",sigma,"nu",nu);

        double expected = -36.1648364818251; // Calculated in R
        double result = likelihood.calculateLogP();

        System.out.println(expected+"\t"+result);
        assertEquals(expected, result, BEASTTestCase.PRECISION);
    }

}