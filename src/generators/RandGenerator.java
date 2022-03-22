package generators;

import java.util.Random;

public abstract class RandGenerator {

    protected Random random;

    public RandGenerator() {
        random = new Random();
    }

    public RandGenerator(long seed) {
        random = new Random(seed);
    }

}
