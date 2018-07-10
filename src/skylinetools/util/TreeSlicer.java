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
 *
 * Base TreeSlicer class
 *
 * Slices tree between stopInput and the present into dimension equidistant intervals
 *
 *
 * Output either in height (from most recent tip) or in dates
 *
 * The treeslice is anchored from the newest date in the tree, thus if the tipdates are sampled this should not be used
 *
 *
 * Input dates always have dimension one less than the treeslicer itself (unless the last date is equal to the most recent tip).
 * (Since the last value in the slice has to be 0 (i.e. the most recent tip in the tree).
 *
 * Add flag to limit the biggest interval time to tmrca?
 *
 * @author Louis du Plessis
 *         Date: 2018/02/12
 *
 */
public class TreeSlicer extends RealParameter {

    final static int MRCA          = 0,
                     LASTSAMPLE    = 1,
                     EQUIDISTANT   = 2,
                     DATES         = 3;

    final double eps = 1e-7;

    public Input<Tree> treeInput =
            new Input<>("tree", "Tree over which to calculate the slice", Input.Validate.REQUIRED);

    public Input<String> stopInput =
            new Input<>("stop", "Breakpoint to stop the slicing intervals (tmrca/oldestsample)", "tmrca");

    public Input<Boolean> includeLastInput =
            new Input<>("includeLast", "Include the last breakpoint (stopping criterion) into the vector", true);

    public Input<String> typeInput =
            new Input<>("type", "Type of slice (equidistant/dates)", "equidistant");

    public Input<Function> datesInput =
            new Input<>("dates", "Dates at which to slice the tree");

    protected Tree tree;
    protected int stop, type;
    protected boolean includeLast;
    protected double tmrca,                 // Height of TMRCA of the tree
                     oldest,                // Height of the oldest sample
                     newest,                // Height of the most recent sample
                     anchordate;            // Date for translating height to calendar date (most recent sample at time = 0)
    protected double [] dates;              // Dates of the slice


    private boolean timesKnown;

    //Function outputDates;

    // Override input rule of RealParameter (base class)
    public TreeSlicer() {
        valuesInput.setRule(Input.Validate.OPTIONAL);
    }

    @Override
    public void initAndValidate() {

        int dimension;
        String stopStr, typeStr;

        /* Read tree */
        tree      = treeInput.get();
        updateAnchorTimes(tree);

        // Not necessary
        /* Set anchordate to date of the newest node in the tree
        double minheight = tree.getRoot().getHeight();
        for (Node N : tree.getNodesAsArray()) {
            if (N.getHeight() <= minheight) {
                anchordate = N.getDate();
                minheight  = N.getHeight();
            }
        }*/

        typeStr = typeInput.get().toLowerCase().trim();

        if (typeStr.equals("dates")) {

            /* Read input dates */

            type = DATES;

            updateDates();
            if (dates[dates.length-1] == anchordate) {
                dimension = dates.length;
            } else
                dimension = dates.length+1;

            // this.setDimension();

            dimensionInput.setValue(dimension, this);

        } else
        if (typeStr.equals("equidistant")) {

            /* Read stop criterion */

            type = EQUIDISTANT;

            dimension = dimensionInput.get();

            if (stopInput.get() != null)
                stopStr = stopInput.get().toLowerCase().trim();
            else
                stopStr = "tmrca";


            /* Set stop criterion */
            if (stopStr.equals("tmrca")) {
                stop = MRCA;
            } else if (stopStr.equals("oldestsample")) {
                stop = LASTSAMPLE;
            } else
                throw new IllegalArgumentException("Error in "+this.getID()+": Unknown stop criterion!");

            /* Merge last two intervals? */
            if (includeLastInput.get() != null)
                includeLast = includeLastInput.get();


        } else
            throw new IllegalArgumentException("Error in "+this.getID()+": Unknown type of treeslice!");

        /* Initialise values */
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



    protected double dateToHeight(double date) {
        return (anchordate - date);
    }

    protected double heightToDate(double height) {
        return (anchordate - height);
    }


    /**
     *
     * Update a few state variables
     *
     *  - tmrca   (height of tmrca if present = 0 ... tmrca_date = present - tmrca
     *  - oldest  (height of the oldest sample)
     *  - newest  (height of the youngest sample - should be 0)
     *
     * O(n) for n nodes
     *
     * @param tree
     */
    protected void updateAnchorTimes(Tree tree) {
        double height;

        tmrca = tree.getRoot().getHeight();

        /* This next part is only necessary when tipdate sampling is being done - How to check? */
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
                    anchordate = N.getDate();
                }
            }
        }

    }

    protected void updateDates() {
        dates = datesInput.get().getDoubleValues();
        HeapSort.sort(dates);
    }


    protected void calculateTimes(Tree tree) {

        double endtime, step;

        /* Update newest, oldest, tmrca */
        updateAnchorTimes(tree);
        //System.out.println(tmrca+"\t"+oldest+"\t"+heightToDate(oldest)+"\t"+newest+"\t"+anchordate);

        if (type == DATES) {

            updateDates();

            for (int i = 1; i < getDimension(); i++)
                values[values.length - i] = Math.max(0.0, dateToHeight(dates[i - 1]));
            values[0] = 0.0;

        } else {

            if (stop == MRCA) {
                endtime = tmrca;
            } else if (stop == LASTSAMPLE) {
                endtime = oldest + eps;
            } else
                endtime = 1.0;


            if (includeLast)
                step = endtime / (getDimension() - 1);
            else
                step = endtime / (getDimension());


            for (int i = 0; i < getDimension(); i++) {
                values[i] = i * step;
            }

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


    // Override methods to make sure times get recalculated

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
