package skylinetools;

import beast.evolution.tree.Tree;
import beast.util.TreeParser;
import junit.framework.TestCase;
import org.junit.Test;
import skylinetools.util.TreeSlicer;
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

        Tree tree = new TreeParser("((D4Philip56:3,(D4Philip64:3,D4Philip84:23):7):10,(D4SLanka78:20,(D4Thai78:6,D4Thai84:12):14):15);",false);

        TreeSlicer treeSlicer = new TreeSlicer();
        treeSlicer.initByName("tree",tree,"stop","tmrca","includeLast","false","dimension","4");


        /*   tmrca = 41 / 1944, most recent tip = 1985

             4 intervals:
                41/4 = 10.25
                (0.0, 10.25, 20.5, 30.75)
         */

        Double [] values = treeSlicer.getValues();
        Double [] expected = {0.0, 10.25, 20.5, 30.75};

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

        Tree tree = new TreeParser("((((D4Mexico84:5,D4ElSal94:15):1,D4PRico86:8):1,D4Tahiti79:2):5,D4Indon77:6);",false);

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

        Tree tree = new TreeParser("((D4Philip56:3,(D4Philip64:3,D4Philip84:23):7):10,(D4SLanka78:20,(D4Thai78:6,D4Thai84:12):14):15);",false);

        TreeSlicer treeSlicer = new TreeSlicer();
        treeSlicer.initByName("tree",tree,"stop","tmrca","includeLast","true","dimension","4");


        /*   tmrca = 41 / 1944, most recent tip = 1985

             4 intervals:
                41/3 = 13.667
                (0.0, 13.667, 27.334, 41)
         */

        Double [] values = treeSlicer.getValues();
        Double [] expected = {0.0, 13.6666666, 27.33333333, 41.0};

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

        Tree tree = new TreeParser("((((D4Mexico84:5,D4ElSal94:15):1,D4PRico86:8):1,D4Tahiti79:2):5,D4Indon77:6);",false);

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

        Tree tree = new TreeParser("((D4Philip56:3,(D4Philip64:3,D4Philip84:23):7):10,(D4SLanka78:20,(D4Thai78:6,D4Thai84:12):14):15);",false);

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

        Tree tree = new TreeParser("((((D4Mexico84:5,D4ElSal94:15):1,D4PRico86:8):1,D4Tahiti79:2):5,D4Indon77:5);",false);

        TreeSlicer treeSlicer = new TreeSlicer();
        treeSlicer.initByName("tree",tree,"stop","oldestSample","includeLast","true","dimension","5");

        /*   tmrca = 17 / 1977, most recent tip = 1994

             4 intervals:
                17/4 = 4.25
                (0.0, 7.334, 14.667, 22)
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
