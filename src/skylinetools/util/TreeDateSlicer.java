package skylinetools.util;

import beast.core.Function;
import beast.core.Input;
import beast.evolution.tree.Tree;
import beast.util.HeapSort;

import java.util.Arrays;

/**
 * TreeSlicer class that slices a tree at fixed calendar dates
 *
 * TODO: Make the class read dates in different formats and convert to decimal year date. (or use separate DateParser class)
 *
 * @author Louis du Plessis
 *         Date: 2018/12/28
 *
 **/
public class TreeDateSlicer extends TreeSlicer {

    public Input<Function> datesInput =
            new Input<>("dates", "Dates at which to slice the tree");

    protected double[] dates;

    @Override
    public void initAndValidate() {

        int dimension;

        /* Read tree */
        tree      = treeInput.get();
        //updateAnchorTimes(tree);
        Anchor.update(tree);

        /* Read dates and set dimension accordingly (depends on whether present date is included or not) */
        updateDates();

        if (dates[dates.length-1] == Anchor.PRESENT.getDate()) {
            dimension = dates.length;
        } else
            dimension = dates.length+1;
        dimensionInput.setValue(dimension, this);


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



    /**
     * Update the dates and order them (in case they are sampled)
     */
    protected void updateDates() {
        dates = datesInput.get().getDoubleValues();
        HeapSort.sort(dates);
    }


    @Override
    protected void calculateTimes(Tree tree) {

        /* Update newest, oldest, tmrca */
        //updateAnchorTimes(tree);
        Anchor.update(tree);
        //System.out.println(tmrca+"\t"+oldest+"\t"+heightToDate(oldest)+"\t"+newest+"\t"+presentDate);

        updateDates();
        for (int i = 1; i < getDimension(); i++)
            values[values.length - i] = Math.max(0.0, dateToHeight(dates[i - 1]));
        values[0] = 0.0;   // This has to be set to 0.0 for BDSKY to run

        timesKnown = true;
    }

}