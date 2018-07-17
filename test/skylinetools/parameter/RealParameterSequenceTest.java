package skylinetools.parameter;


import junit.framework.TestCase;
import org.junit.Test;
import skylinetools.parameter.RealParameterSequence;
import test.beast.BEASTTestCase;

/**
 * @author Louis du Plessis
 *         Date: 2018/07/17
 */
public class RealParameterSequenceTest extends TestCase {

    @Test
    public void testSeqBy1() {

        System.out.println("Test sequence from 5 to 15 with integer increments");

        RealParameterSequence seq = new RealParameterSequence();
        seq.initByName("from","5","to","15","by","1");

        double [] vals = seq.getDoubleValues(),
              expected = {5,6,7,8,9,10,11,12,13,14,15};

        // Check dimension
        assertEquals(expected.length, seq.getDimension());

        // Check values
        for (int i = 0; i < vals.length; i++) {
            assertEquals(expected[i], vals[i]);
        }
    }

    @Test
    public void testSeqBy2() {

        System.out.println("Test sequence from 5 to 15 with integer increments (non-divisible)");

        RealParameterSequence seq = new RealParameterSequence();
        seq.initByName("from","5","to","15","by","4");

        double [] vals = seq.getDoubleValues(),
              expected = {5,9,13};

        // Check dimension
        assertEquals(expected.length, seq.getDimension());
        assertEquals(expected.length, vals.length);


        // Check values
        for (int i = 0; i < vals.length; i++) {
            assertEquals(expected[i], vals[i]);
        }
    }

    @Test
    public void testSeqBy3() {

        System.out.println("Test sequence from 1 to 0 with negative increments");

        RealParameterSequence seq = new RealParameterSequence();
        seq.initByName("from","1","to","0","by","-0.1");

        double [] vals = seq.getDoubleValues(),
              expected = {1, 0.9, 0.8, 0.7, 0.6, 0.5, 0.4, 0.3, 0.2, 0.1, 0.0};

        // Check dimension
        assertEquals(expected.length, seq.getDimension());
        assertEquals(expected.length, vals.length);


        // Check values
        for (int i = 0; i < vals.length; i++) {
            assertEquals(expected[i], vals[i], BEASTTestCase.PRECISION);
        }
    }

    @Test
    public void testSeqBy4() {

        System.out.println("Test sequence from 1 to 0 with negative increments (non-divisible)");

        RealParameterSequence seq = new RealParameterSequence();
        seq.initByName("from","1","to","0","by","-0.3");

        double [] vals = seq.getDoubleValues(),
              expected = {1, 0.7, 0.4, 0.1};

        // Check dimension
        assertEquals(expected.length, seq.getDimension());
        assertEquals(expected.length, vals.length);

        // Check values
        for (int i = 0; i < vals.length; i++) {
            assertEquals(expected[i], vals[i], BEASTTestCase.PRECISION);
        }
    }



    @Test
    public void testSeqLength1() {

        System.out.println("Test sequence from 5 to 15 with length 11");

        RealParameterSequence seq = new RealParameterSequence();
        seq.initByName("from","5","to","15","dimension","11");

        double [] vals = seq.getDoubleValues(),
                expected = {5,6,7,8,9,10,11,12,13,14,15};

        // Check dimension
        assertEquals(expected.length, seq.getDimension());

        // Check values
        for (int i = 0; i < vals.length; i++) {
            assertEquals(expected[i], vals[i]);
        }
    }

    @Test
    public void testSeqLength2() {

        System.out.println("Test sequence from 5.5 to 14.3 with length 3");

        RealParameterSequence seq = new RealParameterSequence();
        seq.initByName("from","5.5","to","14.3","dimension","3");

        double [] vals = seq.getDoubleValues(),
                expected = {5.5, 9.9, 14.3};

        // Check dimension
        assertEquals(expected.length, seq.getDimension());

        // Check values
        for (int i = 0; i < vals.length; i++) {
            assertEquals(expected[i], vals[i]);
        }
    }

    @Test
    public void testSeqLength3() {

        System.out.println("Test sequence from 10 to 0 with length 5");

        RealParameterSequence seq = new RealParameterSequence();
        seq.initByName("from","10","to","0","dimension","5");

        double [] vals = seq.getDoubleValues(),
                expected = {10, 7.5, 5, 2.5, 0.0};

        // Check dimension
        assertEquals(expected.length, seq.getDimension());

        // Check values
        for (int i = 0; i < vals.length; i++) {
            assertEquals(expected[i], vals[i]);
        }
    }

}
