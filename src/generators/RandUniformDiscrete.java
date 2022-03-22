package generators;

import java.util.Random;

public class RandUniformDiscrete extends RandGenerator {

    private int min;
    private int max;

    public RandUniformDiscrete(int min, int max) {
        super();
        init(min, max);
    }

    public RandUniformDiscrete(int min, int max, long seed) {
        super(seed);
        init(min, max);
    }

    public RandUniformDiscrete(int min, int max, Random seedGenerator) {
        super(seedGenerator.nextLong());
        init(min, max);
    }

    private void init(int min, int max) {
        this.min = min;
        this.max = max;
        if (max < min)
            throw new IllegalStateException("max < min");
    }

    public int nextValue() {
        return random.nextInt(max - min + 1) + min;
    }
}
