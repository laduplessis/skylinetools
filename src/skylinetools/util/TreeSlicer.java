package skylinetools.util;

import beast.core.*;
import beast.core.parameter.Parameter;
import beast.core.parameter.RealParameter;
import beast.evolution.tree.Node;
import beast.evolution.tree.Tree;
import beast.util.HeapSort;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Base TreeSlicer class
 *
 * Equidistant slices between present and an anchor point on the tree.
 *
 * Input dates always have dimension one less than the treeslicer itself (unless the last date is equal to the most recent tip).
 * (Since the last value in the slice has to be 0 (i.e. the most recent tip in the tree).
 *
 *
 * @author Louis du Plessis
 *         Date: 2018/02/12
 *
 */
public class TreeSlicer extends RealParameter {

    final static int PRESENT       = 0,
                     MRCA          = 1,
                     LASTSAMPLE    = 2;

    final double eps = 1e-7;

    public Input<Tree> treeInput =
            new Input<>("tree", "Tree over which to calculate the slice", Input.Validate.REQUIRED);

    public Input<String> toInput =
            new Input<>("to", "Anchor point to stop the slicing intervals (tmrca/oldestsample)", "tmrca");

    public Input<Boolean> inclusiveInput =
            new Input<>("inclusive", "Include the final anchor point (to criterion) in the vector",true);


    protected Tree tree;
    protected int stop;
    protected boolean inclusive;
    protected double tmrca,                 // Height of TMRCA of the tree
                     oldest,                // Height of the oldest sample
                     newest,                // Height of the most recent sample
                     presentDate;            // Date for translating height to calendar date (most recent sample at time = 0)

    protected boolean timesKnown;


    // Override input rule of RealParameter (base class)
    public TreeSlicer() {
        valuesInput.setRule(Input.Validate.OPTIONAL);
    }

    @Override
    public void initAndValidate() {

        int dimension;
        String stopStr;

        /* Read tree */
        tree      = treeInput.get();
        updateAnchorTimes(tree);


        /* Read dimension of the slice */
        dimension = dimensionInput.get();


        /* Read to input (where to end slice) */
        if (toInput.get() != null)
            stopStr = toInput.get().toLowerCase().trim();
        else
            stopStr = "tmrca";

        if (stopStr.equals("tmrca")) {
            stop = MRCA;
        } else if (stopStr.equals("oldestsample")) {
            stop = LASTSAMPLE;
        } else
            throw new IllegalArgumentException("Error in "+this.getID()+": Unknown anchor point ("+stopStr+") for to input.");


        /* Include the final anchor point as a breakpoint */
        inclusive = inclusiveInput.get();


        /* Initialise arrays */
        values = new Double[dimension];
        storedValues = new Double[dimension];
        calculateTimes(tree);
        // System.out.println(this.ID+"\t"+this.getDimension());


        // Initialization accounting (not really used)
        // Don't want to use super.initAndValidate() because we're doing something else with the values
        // m_fLower = Double.NEGATIVE_INFINITY;
        m_fLower = 0.0; // Height cannot be negative
        m_fUpper = Double.POSITIVE_INFINITY;
        m_bIsDirty = new boolean[dimensionInput.get()];
        minorDimension = minorDimensionInput.get();
        if (minorDimension > 0 && dimensionInput.get() % minorDimension > 0) {
            throw new IllegalArgumentException("Error in "+this.getID()+": Dimension must be divisible by stride");
        }
        this.storedValues = values.clone();

        timesKnown = false;
    }



    /* Methods should only be called after anchor times have been updated */

    protected double dateToHeight(double date) {
        return (presentDate - date);
    }

    protected double heightToDate(double height) {
        return (presentDate - height);
    }


    /**
     * Update anchor times in the tree
     *  - tmrca   (height of tmrca if present = 0 ... tmrca_date = present - tmrca
     *  - oldest  (height of the oldest sample)
     *  - newest  (height of the youngest sample - should be 0)
     *
     * O(n) for n nodes
     *
     * Use getNodesAsArray() instead of getExternalNodes() because it only passes a pointer whereas getExterNodes()
     * requires constructing an ArrayList (so it is O(n) plus a lot of extra memory operations).
     *
     * Unfortunately tree.somethingIsDirty() does NOT appear to work to indicate if anchor times would have changed or
     * not, so this MUST be recalculated every time the times are updated.
     *
     * @param tree
     */
    protected void updateAnchorTimes(Tree tree) {

        //double oldtmrca   = tmrca,
        //       oldoldest  = oldest,
        //       oldnewest  = newest,

        double height;

        tmrca = tree.getRoot().getHeight();

        // This next part should only be necessary when tipdates are sampled (How can this be checked?)
        // May be optimised by skipping the first n-1 nodes, but this way is very secure, though slightly slower
        oldest = 0;
        newest = tmrca;
        for (Node N : tree.getNodesAsArray()) {
            if (N.isLeaf()) {
                height = N.getHeight();

                if (height > oldest) {
                    oldest = height;
                }

                if (height  < newest) {
                    newest  = height;
                    presentDate = N.getDate();
                }
            }
        }

        //boolean somethingHasChanged = (oldtmrca != tmrca) || (oldoldest != oldest) || (oldnewest != newest)
    }


    /**
     * Update the slice times
     *
     * Unfortunately tree.somethingIsDirty() does NOT appear to work to indicate if anchor times have changed so
     * updateAnchortimes() MUST be called.
     *
     * @param tree
     */
    protected void calculateTimes(Tree tree) {

        double endTime, stepSize;

        /* Update newest, oldest, tmrca */
        updateAnchorTimes(tree);
        //System.out.println(tmrca+"\t"+oldest+"\t"+heightToDate(oldest)+"\t"+newest+"\t"+presentDate);

        if (stop == MRCA) {
            endTime = tmrca;
        } else if (stop == LASTSAMPLE) {
            endTime = oldest + eps;
        } else
            endTime = 1.0;


        if (inclusive)
            stepSize = endTime / (getDimension() - 1);
        else
            stepSize = endTime / (getDimension());


        for (int i = 0; i < getDimension(); i++) {
            values[i] = i * stepSize;
        }

        timesKnown = true;
    }

    @Override
    protected boolean requiresRecalculation() {
        // Tree is a stateNode so should always use somethingIsDirty() and NOT isDirtyCalculation!
        //System.out.println("Checking recalculation "+this.ID+" "+tree.isDirtyCalculation()+" "+tree.somethingIsDirty());
        timesKnown = false;
        return true;
        //return tree.somethingIsDirty();
    }


    /* Override methods to make sure times get recalculated whenever times are accessed */

    @Override
    public Double getValue() {
        if (!timesKnown) {
            calculateTimes(tree);
        }
        return values[0];
    }

    @Override
    public Double getValue(final int index) {
        if (!timesKnown) {
            calculateTimes(tree);
        }
        return values[index];
    }

    @Override
    public double getArrayValue() {
        if (!timesKnown) {
            calculateTimes(tree);
        }
        return values[0];
    }

    @Override
    public double getArrayValue(final int index) {
        if (!timesKnown) {
            calculateTimes(tree);
        }
        return values[index];
    }

    @Override
    public Double [] getValues() {
        if (!timesKnown) {
            calculateTimes(tree);
        }
        return Arrays.copyOf(values, values.length);
    }

}
