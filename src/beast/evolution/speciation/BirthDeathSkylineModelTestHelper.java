package beast.evolution.speciation;

import java.util.List;

/**
 * Created by dlouis on 06/04/16.
 */
public class BirthDeathSkylineModelTestHelper extends BirthDeathSkylineModel {

    public List<Double> getBirthRateChangeTimes() { return birthRateChangeTimes; }

    public List<Double> getDeathRateChangeTimes() { return deathRateChangeTimes; }

    public List<Double> getSamplingRateChangeTimes() { return samplingRateChangeTimes; }

    public List<Double> getrhoSamplingChangeTimes() { return rhoSamplingChangeTimes; }

    public List<Double> rChangeTimes() { return rChangeTimes; }

}
