package skylinetools.util;

import beast.evolution.tree.Tree;
import beast.util.TreeParser;
import junit.framework.TestCase;
import org.junit.Test;
import test.beast.BEASTTestCase;

/**
 * @author Louis du Plessis
 *         Date: 2018/07/10
 */
public class TreeSlicerTest extends TestCase {


    /**
     * Test slice that divides in equidistant intervals between present and tmrca
     */
    @Test
    public void testSliceTMRCA1() {

        System.out.println("TreeSlicer: Equidistant between present and tmrca (tree 1)");

        Tree tree = new TreeParser("((D4Philip56:2.0,(D4Philip64:3.0,D4Philip84:23.0):7.0):10.0,(D4SLanka78:19.0,(D4Thai78:5.0,D4Thai84:11.0):14.0):15.0);",false);

        TreeSlicer treeSlicer = new TreeSlicer();
        treeSlicer.initByName("tree",tree,"stop","tmrca","includeLast","false","dimension","4");


        /*   tmrca = 40 / 1944, most recent tip = 1984

             4 intervals:
                40/4 = 10.00
                (0.0, 10, 20, 30)
         */

        Double [] values = treeSlicer.getValues();
        Double [] expected = {0.0, 10.0, 20.0, 30.0};

        for (int i = 0; i < values.length; i++) {
            //System.out.print(values[i]+"\t");
            assertEquals(expected[i], values[i]);        }
        //System.out.println();
    }

    /**
     * Test slice that divides in equidistant intervals between present and tmrca
     */
    @Test
    public void testSliceTMRCA2() {

        System.out.println("TreeSlicer: Equidistant between present and tmrca (tree 2)");

        Tree tree = new TreeParser("((((D4Mexico84:5.0,D4ElSal94:15.0):1.0,D4PRico86:8.0):1.0,D4Tahiti79:2.0):5.0,D4Indon77:5.0);",false);

        TreeSlicer treeSlicer = new TreeSlicer();
        treeSlicer.initByName("tree",tree,"stop","tmrca","includeLast","false","dimension","4");

        /*   tmrca = 22 / 1972, most recent tip = 1994

             4 intervals:
                22/4 = 5.5
                (0.0, 5.5, 11, 16.5)
         */

        Double [] values = treeSlicer.getValues();
        Double [] expected = {0.0, 5.5, 11.0, 16.5};

        for (int i = 0; i < values.length; i++) {
            //System.out.print(values[i]+"\t");
            assertEquals(expected[i], values[i]);
        }
        //System.out.println();
    }


    /**
     * Test slice that divides in equidistant intervals between present and tmrca
     * Stop time of the slice is included in the slice
     */
    @Test
    public void testSliceTMRCAincludeLast1() {

        System.out.println("TreeSlicer: Equidistant between present and tmrca, includeLast (tree 1)");

        Tree tree = new TreeParser("((D4Philip56:2.0,(D4Philip64:3.0,D4Philip84:23.0):7.0):10.0,(D4SLanka78:19.0,(D4Thai78:5.0,D4Thai84:11.0):14.0):15.0);",false);

        TreeSlicer treeSlicer = new TreeSlicer();
        treeSlicer.initByName("tree",tree,"stop","tmrca","includeLast","true","dimension","4");


        /*   tmrca = 40 / 1944, most recent tip = 1984

             4 intervals:
                40/3 = 13.334
                (0.0, 13.334, 26.667, 40)
         */

        Double [] values = treeSlicer.getValues();
        Double [] expected = {0.0, 13.333333333, 26.666666666, 40.0};

        for (int i = 0; i < values.length; i++) {
            //System.out.print(values[i]+"\t");
            assertEquals(expected[i], values[i], BEASTTestCase.PRECISION);
        }
        //System.out.println();
    }

    /**
     * Test slice that divides in equidistant intervals between present and tmrca
     * Stop time of the slice is included in the slice
     */
    @Test
    public void testSliceTMRCAincludeLast2() {

        System.out.println("TreeSlicer: Equidistant between present and tmrca, includeLast (tree 2)");

        Tree tree = new TreeParser("((((D4Mexico84:5.0,D4ElSal94:15.0):1.0,D4PRico86:8.0):1.0,D4Tahiti79:2.0):5.0,D4Indon77:5.0);",false);

        TreeSlicer treeSlicer = new TreeSlicer();
        treeSlicer.initByName("tree",tree,"stop","tmrca","includeLast","true","dimension","4");

        /*   tmrca = 22 / 1972, most recent tip = 1994

             4 intervals:
                22/3 = 5.5
                (0.0, 7.334, 14.667, 22)
         */

        Double [] values = treeSlicer.getValues();
        Double [] expected = {0.0, 7.333333, 14.6666666, 22.0};

        for (int i = 0; i < values.length; i++) {
            //System.out.print(values[i]+"\t");
            assertEquals(expected[i], values[i], BEASTTestCase.PRECISION);
        }
        //System.out.println();
    }

    /**
     * Test slice that divides in equidistant intervals between present and oldest sample
     * Stop time of the slice is included in the slice
     */
    @Test
    public void testSliceOldestSample1() {

        System.out.println("TreeSlicer: Equidistant between present and oldest sample (tree 1)");

        Tree tree = new TreeParser("((D4Philip56:2.0,(D4Philip64:3.0,D4Philip84:23.0):7.0):10.0,(D4SLanka78:19.0,(D4Thai78:5.0,D4Thai84:11.0):14.0):15.0);",false);

        TreeSlicer treeSlicer = new TreeSlicer();
        treeSlicer.initByName("tree",tree,"stop","oldestSample","includeLast","true","dimension","5");


        /*   oldest tip = 28 / 1956, most recent tip = 0 / 1984

             4 intervals:
                28/4 = 7
                (0.0, 7, 14, 21, 28)
         */

        Double [] values = treeSlicer.getValues();
        Double [] expected = {0.0, 7.0, 14.0, 21.0, 28.0};

        for (int i = 0; i < values.length; i++) {
            //System.out.print(values[i]+"\t");
            assertEquals(expected[i], values[i], BEASTTestCase.PRECISION);
        }
        //System.out.println();
    }

    /**
     * Test slice that divides in equidistant intervals between present and tmrca
     * Stop time of the slice is included in the slice
     */
    @Test
    public void testSliceOldestSample2() {

        System.out.println("TreeSlicer: Equidistant between present and oldest sample (tree 2)");

        Tree tree = new TreeParser("((((D4Mexico84:5.0,D4ElSal94:15.0):1.0,D4PRico86:8.0):1.0,D4Tahiti79:2.0):5.0,D4Indon77:5.0);",false);

        TreeSlicer treeSlicer = new TreeSlicer();
        treeSlicer.initByName("tree",tree,"stop","oldestSample","includeLast","true","dimension","5");

        /*   tmrca = 17 / 1977, most recent tip = 1994

             4 intervals:
                17/4 = 4.25
                (0.0, 4.25, 8.5, 12.75, 17)
         */

        Double [] values = treeSlicer.getValues();
        Double [] expected = {0.0, 4.25, 8.5, 12.75, 17.0};

        for (int i = 0; i < values.length; i++) {
            //System.out.print(values[i]+"\t");
            assertEquals(expected[i], values[i], BEASTTestCase.PRECISION);
        }
        //System.out.println();
    }

}
