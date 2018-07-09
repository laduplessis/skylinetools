package skylinetools.util;

import beast.core.CalculationNode;
import beast.core.Function;
import beast.core.Input;
import beast.core.Loggable;

import java.io.PrintStream;

public class TreeSliceDateLogger extends CalculationNode implements Loggable, Function {

    final public Input<TreeSlicer> treeSliceInput =
            new Input<>("treeSlice", "Tree slicer to log dates for", Input.Validate.REQUIRED);

    @Override
    public void initAndValidate() {
        // nothing to do
    }

    @Override
    public void init(PrintStream out) {
        final TreeSlicer treeslice = treeSliceInput.get();
        final int valueCount = treeslice.getDimension();

        if (valueCount == 1) {
            out.print(treeslice.getID()+".dates\t");
        } else {
            for (int value = 0; value < valueCount; value++) {
                out.print(treeslice.getID()+ ".dates" + (value + 1) + "\t");

            }
        }
    }

    @Override
    public void log(long sample, PrintStream out) {
        final TreeSlicer treeslice = treeSliceInput.get();

        final int values = treeslice.getDimension();
        for (int value = 0; value < values; value++) {
            out.print(treeslice.heightToDate(treeslice.getValue(value)) + "\t");
        }
    }

    @Override
    public void close(PrintStream out) {
        // nothing to do
    }

    @Override
    public int getDimension() {
        final TreeSlicer treeslice = treeSliceInput.get();
        return treeslice.getDimension();
    }

    @Override
    public double getArrayValue() {
        final TreeSlicer treeslice = treeSliceInput.get();
        return treeslice.getArrayValue();
    }

    @Override
    public double getArrayValue(int dim) {
        final TreeSlicer treeslice = treeSliceInput.get();
        return treeslice.getArrayValue(dim);
    }




}
