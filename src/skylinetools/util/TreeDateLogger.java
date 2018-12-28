package skylinetools.util;

import beast.core.CalculationNode;
import beast.core.Function;
import beast.core.Input;
import beast.core.Loggable;
import beast.core.parameter.RealParameter;
import beast.core.util.Log;
import beast.evolution.tree.Tree;

import java.io.PrintStream;

/**
 * Log date of tMRCA or if parameter is supplied, logs dates of those parameters subtracted from most recent date in tree
 * E.g. log the date of the origin in a birth-death model.
 *
 * @author Louis du Plessis
 *         Date: 2018/08/05
 *
 */
public class TreeDateLogger extends CalculationNode implements Loggable, Function {

    final public Input<Tree> treeInput = new Input<>("tree", "Tree to report dates for.", Input.Validate.REQUIRED);

    final public Input<RealParameter> parameterInput = new Input<>("parameter", "Parameter to report dates for on tree.", Input.Validate.OPTIONAL);

    int dim;

    @Override
    public void initAndValidate() {
        if (!treeInput.get().hasDateTrait()) {
            Log.warning.println("Tree "+treeInput.get().getID()+ " does not have a date trait. TreeDateLogger "+ getID() + " will log heights instead");
        }

        if (parameterInput.get() != null) {
            dim = parameterInput.get().getDimension();
        } else {
            dim = 1;
        }

    }

    @Override
    public void init(PrintStream out) {
        final Tree tree = treeInput.get();

        if (parameterInput.get() != null) {
            if (dim == 1) {
                out.print(tree.getID() + "." + parameterInput.get().getID() + ".date\t");
            } else {

                for (int i = 0; i < dim; i++) {
                    out.print(tree.getID() + "." + parameterInput.get().getID() + ".dates" + (i+1) + "\t");
                }

            }
        } else {
            out.print(tree.getID() + ".height.date\t");
        }
    }

    @Override
    public void log(long sample, PrintStream out) {
        final Tree tree = treeInput.get();

        if (parameterInput.get() != null) {
            for (int i = 0; i < dim; i++) {
                out.print(tree.getDate(parameterInput.get().getValue(i)) + "\t");
            }
        } else {
            out.print(tree.getDate(tree.getRoot().getHeight()) + "\t");
        }
    }

    @Override
    public void close(PrintStream out) {
        // nothing to do
    }

    @Override
    public int getDimension() {
        return dim;
    }

    @Override
    public double getArrayValue() {
        final Tree tree = treeInput.get();

        if (parameterInput.get() != null) {
            return tree.getDate(parameterInput.get().getValue(0));
        } else {
            return tree.getDate(tree.getRoot().getHeight());
        }
    }

    @Override
    public double getArrayValue(int dim) {
        final Tree tree = treeInput.get();

        if (parameterInput.get() != null) {
            return tree.getDate(parameterInput.get().getValue(dim));
        } else {
            return tree.getDate(tree.getRoot().getHeight());
        }
    }

}
