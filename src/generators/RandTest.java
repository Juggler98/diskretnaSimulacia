package generators;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class RandTest {

    public static void main(String[] args) throws IOException {
        RandExponential randExponential = new RandExponential(240);
        RandUniformContinuous randUniformContinuous = new RandUniformContinuous(120, 240);
        BufferedWriter writer = new BufferedWriter(new FileWriter( "expoData.txt"));
        BufferedWriter writer2 = new BufferedWriter(new FileWriter( "uniformContinuousData.txt"));
        for (int i = 0; i < 500000; i++) {
            double value = randExponential.getNextDouble();
            writer.write(value + "\n");
            //System.out.println(value);

            value = randUniformContinuous.getNextDouble();
            writer2.write(value + "\n");
            //System.out.println(value);
        }
        writer.close();
    }

}
