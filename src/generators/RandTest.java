package generators;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class RandTest {

    public static void main(String[] args) throws IOException {
        RandExponential randExponential = new RandExponential(240);
        RandUniformContinuous randUniformContinuous = new RandUniformContinuous(120, 240);
        RandUniformDiscrete randUniformDiscrete = new RandUniformDiscrete(30, 60);
        RandTriangular randTriangular = new RandTriangular(40, 150, 70);
        EmpiricDiscrete[] empiricDiscretes = {new EmpiricDiscrete(30, 60, 0.2), new EmpiricDiscrete(61, 120, 0.3), new EmpiricDiscrete(40, 80, 0.5)};
        RandEmpiricDiscrete randEmpiricDiscrete = new RandEmpiricDiscrete(empiricDiscretes);

        BufferedWriter writer = new BufferedWriter(new FileWriter( "exponential.txt"));
        BufferedWriter writer2 = new BufferedWriter(new FileWriter( "uniformContinuous.txt"));
        BufferedWriter writer3 = new BufferedWriter(new FileWriter( "uniformDiscrete.txt"));
        BufferedWriter writer4 = new BufferedWriter(new FileWriter( "triangular.txt"));
        BufferedWriter writer5 = new BufferedWriter(new FileWriter( "empiric.txt"));

        for (int i = 0; i < 100000; i++) {
            double value = randExponential.nextValue();
            writer.write(value + "\n");
            //System.out.println(value);

            value = randUniformContinuous.nextValue();
            writer2.write(value + "\n");

            int valueInt = randUniformDiscrete.nextValue();
            writer3.write(valueInt + "\n");

            value = randTriangular.nextValue();
            writer4.write(value + "\n");

            valueInt = randEmpiricDiscrete.nextValue();
            writer5.write(valueInt + "\n");
            //System.out.println(valueInt);
        }
        writer.close();
        writer2.close();
        writer3.close();
        writer4.close();
        writer5.close();
    }

}
