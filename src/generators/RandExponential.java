package generators;

public class RandExponential extends RandGenerator {

    private double lambda;

    public RandExponential(double lambda) {
        super();
        this.lambda = 1 / lambda;
    }

    public RandExponential(long seed, double lambda) {
        super(seed);
        this.lambda = 1 / lambda;
    }


    @Override
    public double getNextDouble() {
        return -Math.log(1 - random.nextDouble())/lambda;
    }

    @Override
    public int getNextInt() {
        throw new IllegalStateException("This should not be called");
    }
}
