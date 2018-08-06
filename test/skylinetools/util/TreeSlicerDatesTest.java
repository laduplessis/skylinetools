package skylinetools.util;

import beast.evolution.alignment.Taxon;
import beast.evolution.alignment.TaxonSet;
import beast.evolution.tree.TraitSet;
import beast.evolution.tree.Tree;
import beast.util.TreeParser;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Louis du Plessis
 *         Date: 2018/07/10
 */
public class TreeSlicerDatesTest extends TestCase {

    public void testSliceTMRCA1() {

        System.out.println("TreeSlicer: Slice at specific years (tree 1)");



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
                                                                                 "D4SLanka78=1979,"+
                                                                                 "D4Thai78=1979,"+
                                                                                 "D4Thai84=1985");

        tree.initByName("newick","((D4Philip56:3,(D4Philip64:3,D4Philip84:23):7):10,(D4SLanka78:20,(D4Thai78:6,D4Thai84:12):14):15);",
                                 "adjustTipHeights","false", "trait",dateTrait, "taxonset",taxonSet);

        TreeSlicer treeSlicer = new TreeSlicer();
        treeSlicer.initByName("tree",tree,"type","dates","dates","1950 1965 1970 1980");


        /*   tmrca = 41 / 1944, most recent tip = 1985

             4 intervals at 1980, 1970, 1965, 1950: (5, 15, 20, 35)

         */

        Double [] values = treeSlicer.getValues();
        Double [] expected = {0.0, 5.0, 15.0, 20.0, 35.0};

        for (int i = 0; i < values.length; i++) {
            //System.out.print(values[i]+"\t");
            assertEquals(expected[i], values[i]);
        }
        //System.out.println();
    }


}
