import java.util.Random;

public class SemJedna {

    Random random = new Random();
    private final int n;
    private final boolean[] slots;
    private final int strategia;

    public SemJedna(int n, int strategia) {
        //System.out.println("---------Run---------");
        //System.out.println("n: " + n);
        this.n = n;
        slots = new boolean[n];
        this.strategia = strategia;
    }

    public int run() {

        for (int i = 0; i < slots.length; i++) {
            slots[i] = false;
        }

        slots[2] = true;
        slots[5] = true;
        slots[8] = true;

        int k = random.nextInt(n - 3) + 1;
//        System.out.println("k: " + k);
        for (int i = 0; i < k; i++) {
            while (true) {
                int position = random.nextInt(n);
                if (!slots[position]) {
                    slots[position] = true;
                    break;
                }
            }
        }

//        for (int i = 0; i < slots.length; i++) {
//            System.out.print(slots[i] + ", ");
//        }
//        System.out.println();

        int zaciatok;
        if (strategia == 1) {
            zaciatok = n - 1;
        } else if (strategia == 2) {
            zaciatok = n - 1 - (int) Math.ceil(2.0 * n / 3);
        } else {
            zaciatok = n - 1 - (int) Math.ceil(n / 2.0);
        }

        for (int i = zaciatok; i >= 0; i--) {
            if (!slots[i]) {
                return i + 1;
            }
        }
        return 3 * n;
    }

}
