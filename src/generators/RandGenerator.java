package generators;

import java.util.Random;

public abstract class RandGenerator {

    protected Random random = new Random();

    public RandGenerator() {
        random = new Random();
    }

    public RandGenerator(long seed) {
        random = new Random(seed);
    }

    public abstract double getNextDouble();
    public abstract int getNextInt();



}
