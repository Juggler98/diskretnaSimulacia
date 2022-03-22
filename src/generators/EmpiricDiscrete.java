package generators;

public class EmpiricDiscrete {

    private final int min;
    private final int max;
    private final double p;

    public EmpiricDiscrete(int min, int max, double p) {
        this.min = min;
        this.max = max;
        this.p = p;
    }

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }

    public double getP() {
        return p;
    }
}
