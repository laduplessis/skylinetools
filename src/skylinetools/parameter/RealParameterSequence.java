package skylinetools.parameter;

import beast.core.Description;
import beast.core.Input;
import beast.core.parameter.RealParameter;

import java.util.Arrays;


/**
 * A parameter object to easily initialise a linear sequence of real numbers
 * Can either use the "by" input to specify the step size or simply use the "dimension"
 * input to specify the length of the sequence (the step size will be calculated).
 *
 * @author Louis du Plessis
 *         Date: 2018/07/17
 */
@Description("A real-valued sequence of numbers with a fixed step-size between them (at initialisation).")
public class RealParameterSequence extends RealParameter {


    public final Input<Double> fromInput = new Input<>("from", "Starting value of the sequence",
            Input.Validate.REQUIRED);

    public final Input<Double> toInput = new Input<>("to", "End value of the sequence",
            Input.Validate.REQUIRED);

    // Specify either byInput or dimensionInput
    public final Input<Double> byInput = new Input<>("by", "Increment of the sequence");


    // Override input rule of RealParameter (base class)
    // (values are derived here)
    public RealParameterSequence() {
        valuesInput.setRule(Input.Validate.OPTIONAL);
    }


    @Override
    public void initAndValidate() {

        double from = fromInput.get().doubleValue(),
               to   = toInput.get().doubleValue();

        double step;
        int dim;

        if (from == to) {
            throw new IllegalArgumentException("from cannot be equal to to");
        }

        // Set limits (from RealParameter)
        if (lowerValueInput.get() != null) {
            m_fLower = lowerValueInput.get();
        } else {
            m_fLower = Double.NEGATIVE_INFINITY;
        }
        if (upperValueInput.get() != null) {
            m_fUpper = upperValueInput.get();
        } else {
            m_fUpper = Double.POSITIVE_INFINITY;
        }

        // Calculate dimension and stepsize
        if (byInput.get() == null) {
            //System.out.println("Length");

            dim  = dimensionInput.get().intValue();

            if (dim <= 0) {
                throw new IllegalArgumentException("Length needs to be bigger than 0");
            }

            step = (to-from)/(dim - 1);
        } else {
            //System.out.println("By");

            step = byInput.get().doubleValue();

            if (step == 0) {
                throw new IllegalArgumentException("Step size cannot be 0");
            }

            dim =  (int) Math.floor((to-from)/step) + 1;

            if ((from > to && step > 0) || (to > from && step < 0)) {
                throw new IllegalArgumentException("No way to reach to from from with by input");
            }

        }

        // Set  values
        dimensionInput.setValue(dim, this);
        values = new Double[dim];
        for (int i = 0; i < dim; i++) {
            values[i] = from + i*step;
        }
        //System.out.println(Arrays.toString(values)+": "+dim);

        // Accounting (from Parameter.Base)
        storedValues = new Double[dim];
        m_bIsDirty = new boolean[dimensionInput.get()];
        minorDimension = minorDimensionInput.get();
        if (minorDimension > 0 && dimensionInput.get() % minorDimension > 0) {
            throw new IllegalArgumentException("Dimension must be divisible by stride");
        }
        this.storedValues = values.clone();

    }

}
