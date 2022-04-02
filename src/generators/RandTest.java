package generators;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class RandTest {

    public static void main(String[] args) throws IOException {
        main2();

        RandExponential randExponential = new RandExponential(240);
        RandUniformContinuous randUniformContinuous = new RandUniformContinuous(120, 240);
        RandUniformDiscrete randUniformDiscrete = new RandUniformDiscrete(30, 60);
        RandTriangular randTriangular = new RandTriangular(40, 150, 70);
        EmpiricDiscrete[] empiricDiscretes = {new EmpiricDiscrete(50, 60, 0.2), new EmpiricDiscrete(61, 100, 0.3), new EmpiricDiscrete(101, 150, 0.5)};
        RandEmpiricDiscrete randEmpiricDiscrete = new RandEmpiricDiscrete(empiricDiscretes);

        BufferedWriter writer = new BufferedWriter(new FileWriter("exponential.txt"));
        BufferedWriter writer2 = new BufferedWriter(new FileWriter("uniformContinuous.txt"));
        BufferedWriter writer3 = new BufferedWriter(new FileWriter("uniformDiscrete.txt"));
        BufferedWriter writer4 = new BufferedWriter(new FileWriter("triangular.txt"));
        BufferedWriter writer5 = new BufferedWriter(new FileWriter("empiric.txt"));

        int pocetEmpiricFirst = 0;
        int pocetEmpiricSecond = 0;
        int pocetEmpiricThird = 0;

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
            if (valueInt <= empiricDiscretes[0].getMax())
                pocetEmpiricFirst++;
            if (valueInt > empiricDiscretes[0].getMax() && valueInt < empiricDiscretes[1].getMax())
                pocetEmpiricSecond++;
            if (valueInt > empiricDiscretes[1].getMax())
                pocetEmpiricThird++;
            //System.out.println(valueInt);
        }
        System.out.println("first: " + pocetEmpiricFirst);
        System.out.println("second: " + pocetEmpiricSecond);
        System.out.println("third: " + pocetEmpiricThird);
        writer.close();
        writer2.close();
        writer3.close();
        writer4.close();
        writer5.close();
    }

    public static void main2() {
        Random seedGenerator = new Random();
        final RandUniformDiscrete randUcesJednoduchy = new RandUniformDiscrete(10, 30, seedGenerator);
        final EmpiricDiscrete[] empiricDiscretesUcesZlozity = {new EmpiricDiscrete(30, 60, 0.4), new EmpiricDiscrete(61, 120, 0.6)};
        final RandEmpiricDiscrete randUcesZlozity = new RandEmpiricDiscrete(empiricDiscretesUcesZlozity, seedGenerator);
        final EmpiricDiscrete[] empiricDiscretesUcesSvadobny = {new EmpiricDiscrete(50, 60, 0.2), new EmpiricDiscrete(61, 100, 0.3), new EmpiricDiscrete(101, 150, 0.5)};
        final RandEmpiricDiscrete randUcesSvadobny = new RandEmpiricDiscrete(empiricDiscretesUcesSvadobny, seedGenerator);

        double sum = 0;
        int count = 100000;
        for (int i = 0; i < count; i++) {
            double percentage = seedGenerator.nextDouble();
            double endTime;
            if (percentage < 0.4) {
                endTime = randUcesJednoduchy.nextValue();
            } else if (percentage < 0.8) {
                endTime = randUcesZlozity.nextValue();
            } else {
                endTime = randUcesSvadobny.nextValue();
            }
            endTime *= 60;
            sum += endTime;
        }
        System.out.println(sum / count / 60 / 60);

    }

}
