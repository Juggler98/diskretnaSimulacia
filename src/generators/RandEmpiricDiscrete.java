package generators;

import java.util.Random;

public class RandEmpiricDiscrete extends RandGenerator {

    private EmpiricDiscrete[] properties;
    private RandUniformDiscrete[] generators;

    public RandEmpiricDiscrete(EmpiricDiscrete[] properties) {
        super();
        init(properties, new Random());
    }

    public RandEmpiricDiscrete(EmpiricDiscrete[] properties, Random seedGenerator) {
        super(seedGenerator.nextLong());
        init(properties, seedGenerator);
    }

    private void init(EmpiricDiscrete[] properties, Random seedGenerator) {
        this.properties = properties;
        this.generators = new RandUniformDiscrete[properties.length];
        double sum = 0;
        for (EmpiricDiscrete e : properties) {
            sum += e.getP();
        }
        if (sum < 0.9999999999999999 || sum > 1)
            throw new IllegalStateException("Sum of p should be 1 and is: " + sum);
        for (int i = 0; i < generators.length; i++) {
            generators[i] = new RandUniformDiscrete(properties[i].getMin(), properties[i].getMax(), seedGenerator);
        }
    }

    public int nextValue() {
        double randomValue = random.nextDouble();
        double sum = 0;
        for (int i = 0; i < generators.length; i++) {
            if (randomValue >= sum && randomValue < sum + properties[i].getP()) {
                return generators[i].nextValue();
            }
            sum += properties[i].getP();
        }
        throw new IllegalStateException("This should not happened.");
    }



}
