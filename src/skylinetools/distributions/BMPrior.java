package skylinetools.distributions;

import beast.core.Distribution;
import beast.core.Function;
import beast.core.Input;
import beast.core.State;
import beast.core.parameter.RealParameter;
import beast.math.distributions.ParametricDistribution;

import java.util.List;
import java.util.Random;

/**
 * Implementation of the Brownian motion probability density for use as a prior on a sequence of (x_i,t_i) pairs.
 *
 * The density is *almost* identical to the GMRF smoothing prior used in the Skyride, Skygrid family of models, but with
 * a distribution placed on x0 (can be null, in which case density should be equal to GMRF density)
 *
 * GMRF implementation in Skyride/Skygrid models uses a matrix algebra library to calculate the density (as a MVN
 * distribution) and is paired with a GMRF block update operator.
 *
 * For now missing the same constant as the OUPrior
 *
 * @author Louis du Plessis
 *         Date: 2018/07/23
 */
public class BMPrior extends Distribution {

    // the trajectory to compute Ornstein-Uhlenbeck prior of
    public Input<Function> xInput =
            new Input<>("x", "The x_i values", (Function) null);

    // the times associated with the x_i values
    public Input<Function> timeInput =
            new Input<>("times", "The times t_i specifying when x changes", (Function) null);

    // tau
    public Input<RealParameter> tauInput =
            new Input<RealParameter>("precision", "The precision parameter of the distribution (reciprocal of variance)", (RealParameter) null);

    // sigma
    public Input<RealParameter> sigmaInput =
            new Input<RealParameter>("sigma", "The standard deviation parameter of the distribution", (RealParameter) null, Input.Validate.XOR, tauInput);

    public Input<ParametricDistribution> x0PriorInput =
            new Input<>("x0Prior", "The prior to use on x0, or null if none.", (ParametricDistribution) null);

    public Input<Boolean> logSpace = new Input<>("logspace", "true if prior should be applied to log(x).", false);



    public double calculateLogP() {

        double tau;
        if (tauInput.get() == null) {
            double sigma = sigmaInput.get().getValue();
            tau = 1/(sigma * sigma);
        } else {
            tau = tauInput.get().getValue();
        }

        ParametricDistribution x0Prior = x0PriorInput.get();

        double[] t = timeInput.get().getDoubleValues();
        double[] x = xInput.get().getDoubleValues();

        boolean logspace = logSpace.get();
        if (logspace) {
            for (int i = 0; i < x.length; i++) {
                x[i] = Math.log(x[i]);
            }
        }

        int n = x.length - 1;

        double logL = n/2.0 * Math.log(tau);

        for (int i = 1; i <= n; i++) {

            double dt = t[i]-t[i-1];
            double dx = x[i]-x[i-1];

            logL -= 0.5*Math.log(dt);
            logL -= (0.5*tau*dx*dx)/dt;
        }

        if (x0Prior != null) logL += x0Prior.calcLogP(new Function() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public double getArrayValue() {
                return x[0];
            }

            @Override
            public double getArrayValue(int iDim) {
                return x[0];
            }
        });

        logP = logL;
        return logP;
    }

    @Override
    public List<String> getArguments() {
        return null;
    }

    @Override
    public List<String> getConditions() {
        return null;
    }

    @Override
    public void sample(State state, Random random) {

    }

}
