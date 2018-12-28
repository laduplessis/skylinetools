package skylinetools.util;

import beast.evolution.alignment.Taxon;
import beast.evolution.alignment.TaxonSet;
import beast.evolution.tree.TraitSet;
import beast.evolution.tree.Tree;
import beast.util.TreeParser;
import junit.framework.TestCase;
import org.junit.Test;
import skylinetools.parameter.DateParser;
import test.beast.BEASTTestCase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Louis du Plessis
 *         Date: 2018/07/10
 */
public class TreeDateSlicerTest extends TestCase {

    @Test
    public void testSliceTMRCA1() {

        System.out.println("TreeDateSlicer: Slice at specific years (tree 1)");



        String [] taxa =   {"D4Philip56",
                            "D4Philip64",
                            "D4Philip84",
                            "D4SLanka78",
                            "D4Thai78",
                            "D4Thai84"};
        List<Taxon> taxalist = Taxon.createTaxonList(new ArrayList<String>(Arrays.asList(taxa)));
        Tree tree = new TreeParser();
        TaxonSet taxonSet  = new TaxonSet(taxalist);
        TraitSet dateTrait = new TraitSet();

        // Years are not as specified in the names because I modified the trees...
        dateTrait.initByName("traitname","date","taxa",taxonSet,"value","D4Philip56=1956,"+
                                                                                 "D4Philip64=1964,"+
                                                                                 "D4Philip84=1984,"+
                                                                                 "D4SLanka78=1978,"+
                                                                                 "D4Thai78=1978,"+
                                                                                 "D4Thai84=1984");

        tree.initByName("newick","((D4Philip56:2.0,(D4Philip64:3.0,D4Philip84:23.0):7.0):10.0,(D4SLanka78:19.0,(D4Thai78:5.0,D4Thai84:11.0):14.0):15.0);",
                                 "adjustTipHeights","false", "trait",dateTrait, "taxonset",taxonSet);

        TreeSlicer treeSlicer = new TreeDateSlicer();
        treeSlicer.initByName("tree",tree,"dates","1950 1965 1970 1980");


        /*   tmrca = 40 / 1944, most recent tip = 1984

             4 intervals at 1980, 1970, 1965, 1950: (4, 14, 19, 34)

         */

        Double [] values = treeSlicer.getValues();
        Double [] expected = {0.0, 4.0, 14.0, 19.0, 34.0};

        for (int i = 0; i < values.length; i++) {
            //System.out.print(values[i]+"\t");
            assertEquals(expected[i], values[i]);
        }
        //System.out.println();
    }


    @Test
    public void testSliceTMRCA2() {

        System.out.println("TreeDateSlicer: Slice at specific dates as formatted strings (tree 1)");

        String [] taxa =   {"D4Philip56",
                            "D4Philip64",
                            "D4Philip84",
                            "D4SLanka78",
                            "D4Thai78",
                            "D4Thai84"};
        List<Taxon> taxalist = Taxon.createTaxonList(new ArrayList<String>(Arrays.asList(taxa)));
        Tree tree = new TreeParser();
        TaxonSet taxonSet  = new TaxonSet(taxalist);
        TraitSet dateTrait = new TraitSet();

        // Years are not as specified in the names because I modified the trees...
        dateTrait.initByName("traitname","date","taxa",taxonSet,"value","D4Philip56=1956,"+
                                                                                 "D4Philip64=1964,"+
                                                                                 "D4Philip84=1984,"+
                                                                                 "D4SLanka78=1978,"+
                                                                                 "D4Thai78=1978,"+
                                                                                 "D4Thai84=1984");

        tree.initByName("newick","((D4Philip56:2.0,(D4Philip64:3.0,D4Philip84:23.0):7.0):10.0,(D4SLanka78:19.0,(D4Thai78:5.0,D4Thai84:11.0):14.0):15.0);",
                "adjustTipHeights","false", "trait",dateTrait, "taxonset",taxonSet);

        DateParser dates = new DateParser();
        dates.initByName("dates", "21-Jun-1980 15-Sep-1966", "format", "dd-MMM-yyyy");

        TreeSlicer treeSlicer = new TreeDateSlicer();
        treeSlicer.initByName("tree",tree,"dates",dates);

        Double [] values = treeSlicer.getValues();
        Double [] expected = {0.0, 3.5300546448088426, 17.29589041095892};

        for (int i = 0; i < values.length; i++) {
            assertEquals(expected[i], values[i]);
        }
        // System.out.println(Arrays.toString(dates.getValues()));
        // System.out.println(Arrays.toString(values));
    }






}
