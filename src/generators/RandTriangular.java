package generators;

import java.util.Random;

public class RandTriangular extends RandGenerator {

    private double min;
    private double max;
    private double mod;

    public RandTriangular(double min, double max, double mod) {
        super();
        init(min, max, mod);
    }

    public RandTriangular(double min, double max, double mod, long seed) {
        super(seed);
        init(min, max, mod);
    }

    public RandTriangular(double min, double max, double mod, Random seedGenerator) {
        super(seedGenerator.nextLong());
        init(min, max, mod);
    }

    private void init(double min, double max, double mod) {
        this.min = min;
        this.max = max;
        this.mod = mod;
        if (max < min)
            throw new IllegalStateException("max < min");
        if (mod < min || mod > max)
            throw new IllegalStateException("mod < min || mod > max");
    }

    public double nextValue() {
        double f = (mod - min)/(max - min);
        double u = random.nextDouble();
        if (u < f) {
            return min + Math.sqrt(u * (max - min) * (mod - min));
        } else {
            return max - Math.sqrt((1 - u) * (max - min) * (max - mod));
        }
    }

}
