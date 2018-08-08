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
 * Implementation of the Ornstein-Uhlenbeck probability density for use as a prior on a sequence of (x_i,t_i) pairs.
 *
 * TODO:
 *  - Check that dimension of timeInput and xInput match
 *  - Add constant to likelihood calculation
 *  - Should sample method be implemented? Shouldn't be too difficult, but this is never used
 *
 * @author Alexei Drummond.
 */
public class OUPrior extends Distribution {


    // the trajectory to compute Ornstein-Uhlenbeck prior of
    public Input<Function> xInput =
            new Input<>("x", "The x_i values", (Function) null);

    // the times associated with the x_i values
    public Input<Function> timeInput =
            new Input<>("times", "The times t_i specifying when x changes", (Function) null);

    // mean
    public Input<RealParameter> meanInput =
            new Input<RealParameter>("mean", "The mean of the equilibrium distribution", (RealParameter) null);

    // sigma
    public Input<RealParameter> sigmaInput =
            new Input<RealParameter>("sigma", "The standard deviation parameter of the equilibrium distribution", (RealParameter) null);

    // nu
    public Input<RealParameter> nuInput =
            new Input<RealParameter>("nu", "The reversion parameter of the Ornstein-Uhlenbeck mean reversion process", (RealParameter) null);

    public Input<ParametricDistribution> x0PriorInput =
            new Input<>("x0Prior", "The prior to use on x0, or null if none.", (ParametricDistribution) null);

    public Input<Boolean> logSpace = new Input<>("logspace", "true if prior should be applied to log(x).", false);

    public Input<Boolean> normalize = new Input<>("normalize", "true if times should be normalized such that t[n]-t[0]=1..", false);


    public double calculateLogP() {

        double mu = meanInput.get().getValue();
        double sigma = sigmaInput.get().getValue();
        double sigsq = sigma * sigma;
        double nu = nuInput.get().getValue();

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


        double logL = -n/2.0 * Math.log(sigsq / (2.0*nu));

        double period = 1;
        // Standardize differences between times
        // (so variance and mean-reversion are not dependent on time-scale)
        if (normalize.get() == true) {
            period = t[t.length-1]-t[0];
        }
        System.out.println(period);

        for (int i = 1; i <= n; i++) {

            double dt = (t[i]-t[i-1])/period;
            double relterm = 1.0-Math.exp(-2.0*nu*(dt));

            System.out.println(dt);

            logL -= Math.log(relterm)/2.0;

            double term = x[i] - mu - (x[i-1]-mu) * Math.exp(-nu*(dt));

            logL -= nu / sigsq * (term*term / relterm);
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
