package generators;

public class RandUniformContinuous extends RandGenerator {

    private double min;
    private double max;

    public RandUniformContinuous(double min, double max) {
        super();
        this.min = min;
        this.max = max;
    }

    public RandUniformContinuous(long seed, double min, double max) {
        super(seed);
        this.min = min;
        this.max = max;
    }


    @Override
    public double getNextDouble() {
        return random.nextDouble() * (max - min) + min;
    }

    @Override
    public int getNextInt() {
        throw new IllegalStateException("This should not be called");
    }

}
