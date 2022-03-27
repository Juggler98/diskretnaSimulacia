//package generators;
//
//import java.util.Random;
//
//public class RandPoisson extends RandGenerator {
//
//    private double lambda;
//
//    public RandPoisson(double lambda) {
//        super();
//        init(lambda);
//    }
//
//    public RandPoisson(double lambda, long seed) {
//        super(seed);
//        init(lambda);
//    }
//
//    public RandPoisson(double lambda, Random seedGenerator) {
//        super(seedGenerator.nextLong());
//        init(lambda);
//    }
//
//    private void init(double lambda) {
//        this.lambda = 1 / lambda;
//    }
//
//    public int nextValue() {
//        return (int) (-Math.log(1 - random.nextDouble()) / lambda);
//    }
//
//}
