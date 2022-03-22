package generators;

import java.util.Random;

public class RandUniformContinuous extends RandGenerator {

    private double min;
    private double max;

    public RandUniformContinuous(double min, double max) {
        super();
        init(min, max);
    }

    public RandUniformContinuous(double min, double max, long seed) {
        super(seed);
        init(min, max);
    }

    public RandUniformContinuous(double min, double max, Random seedGenerator) {
        super(seedGenerator.nextLong());
        init(min, max);
    }

    private void init(double min, double max) {
        this.min = min;
        this.max = max;
        if (max < min)
            throw new IllegalStateException("max < min");
    }

    public double nextValue() {
        return random.nextDouble() * (max - min) + min;
    }
}
