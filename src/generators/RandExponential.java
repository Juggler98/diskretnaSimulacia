package generators;

import java.util.Random;

public class RandExponential extends RandGenerator {

    private double lambda;

    public RandExponential(double lambda) {
        super();
        init(lambda);
    }

    public RandExponential(double lambda, long seed) {
        super(seed);
        init(lambda);
    }

    public RandExponential(double lambda, Random seedGenerator) {
        super(seedGenerator.nextLong());
        init(lambda);
    }

    private void init(double lambda) {
        this.lambda = 1 / lambda;
    }

    public double nextValue() {
        return -Math.log(1 - random.nextDouble())/lambda;
    }
}
